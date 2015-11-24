package com.connectsdk.service.upnp;

import com.connectsdk.core.MediaInfo;
import com.connectsdk.core.Util;
import com.connectsdk.service.capability.MediaControl.PlayStateStatus;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.URLServiceSubscription;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DLNAHttpServer {
    final int port = 49291;

    volatile ServerSocket welcomeSocket;

    volatile boolean running = false;

    CopyOnWriteArrayList<URLServiceSubscription<?>> subscriptions;

    public DLNAHttpServer() {
        subscriptions = new CopyOnWriteArrayList<URLServiceSubscription<?>>();
    }

    public synchronized void start() {
        if (running) {
            return;
        }

        running = true;

        try {
            welcomeSocket = new ServerSocket(this.port);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        Util.runInBackground(new Runnable() {
            @Override
            public void run() {
                processRequests();
            }
        }, true);
    }

    public synchronized void stop() {
        if (!running) {
            return;
        }

        for (URLServiceSubscription<?> sub : subscriptions) {
            sub.unsubscribe();
        }
        subscriptions.clear();

        if (welcomeSocket != null && !welcomeSocket.isClosed()) {
            try {
                welcomeSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        welcomeSocket = null;
        running = false;
    }

    private void processRequests() {
        while (running) {
            if (welcomeSocket == null || welcomeSocket.isClosed()) {
                break;
            }

            Socket connectionSocket = null;
            BufferedReader inFromClient = null;
            DataOutputStream outToClient = null;

            try {
                connectionSocket = welcomeSocket.accept();
            } catch (IOException ex) {
                ex.printStackTrace();
                // this socket may have been closed, so we'll stop
                break;
            }

            int c = 0;

            String body = null;

            try {
                inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

                StringBuilder sb = new StringBuilder();

                while ((c = inFromClient.read()) != -1) {
                    sb.append((char)c);

                    if (sb.toString().endsWith("\r\n\r\n"))
                        break;
                }
                sb = new StringBuilder();

                while ((c = inFromClient.read()) != -1) {
                    sb.append((char)c);
                    body = sb.toString();

                    if (body.endsWith("</e:propertyset>"))
                        break;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            PrintWriter out = null;

            try {
                outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                out = new PrintWriter(outToClient);
                out.println("HTTP/1.1 200 OK");
                out.println("Connection: Close");
                out.println("Content-Length: 0");
                out.println();
                out.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    inFromClient.close();
                    out.close();
                    outToClient.close();
                    connectionSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }

            if (body == null)
                continue;

            InputStream stream = null;

            try {
                stream = new ByteArrayInputStream(body.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }

            JSONArray propertySet;
            DLNANotifyParser parser = new DLNANotifyParser();

            try {
                propertySet = parser.parse(stream);

                for (int i = 0; i < propertySet.length(); i++) {
                    JSONObject property = propertySet.getJSONObject(i);

                    if (property.has("LastChange")) {
                        JSONObject lastChange = property.getJSONObject("LastChange");
                        handleLastChange(lastChange);
                    }
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleLastChange(JSONObject lastChange) throws JSONException {
        if (lastChange.has("InstanceID")) {
            JSONArray instanceIDs = lastChange.getJSONArray("InstanceID");

            for (int i = 0; i < instanceIDs.length(); i++) {
                JSONArray events = instanceIDs.getJSONArray(i);

                for (int j = 0; j < events.length(); j++) {
                    JSONObject entry = events.getJSONObject(j);
                    handleEntry(entry);
                }
            }
        }
    }

    private void handleEntry(JSONObject entry) throws JSONException {
        if (entry.has("TransportState")) {
            String transportState = entry.getString("TransportState");
            PlayStateStatus status = PlayStateStatus.convertTransportStateToPlayStateStatus(transportState);

            for (URLServiceSubscription<?> sub: subscriptions) {
                if (sub.getTarget().equalsIgnoreCase("playState")) {
                    for (int j = 0; j < sub.getListeners().size(); j++) {
                        @SuppressWarnings("unchecked")
                        ResponseListener<Object> listener = (ResponseListener<Object>) sub.getListeners().get(j);
                        Util.postSuccess(listener, status);
                    }
                }
            }
        }

        if ((entry.has("Volume")&&!entry.has("channel"))||(entry.has("Volume")&&entry.getString("channel").equals("Master"))) {
            int intVolume = entry.getInt("Volume");
            float volume = (float) intVolume / 100;

            for (URLServiceSubscription<?> sub : subscriptions) {
                if (sub.getTarget().equalsIgnoreCase("volume")) {
                    for (int j = 0; j < sub.getListeners().size(); j++) {
                        @SuppressWarnings("unchecked")
                        ResponseListener<Object> listener = (ResponseListener<Object>) sub.getListeners().get(j);
                        Util.postSuccess(listener, volume);
                    }
                }
            }
        }

        if ((entry.has("Mute")&&!entry.has("channel"))||(entry.has("Mute")&&entry.getString("channel").equals("Master"))) {
            String muteStatus = entry.getString("Mute");
            boolean mute;

            try {
                mute = (Integer.parseInt(muteStatus) == 1);
            } catch(NumberFormatException e) {
                mute = Boolean.parseBoolean(muteStatus);
            }

            for (URLServiceSubscription<?> sub : subscriptions) {
                if (sub.getTarget().equalsIgnoreCase("mute")) {
                    for (int j = 0; j < sub.getListeners().size(); j++) {
                        @SuppressWarnings("unchecked")
                        ResponseListener<Object> listener = (ResponseListener<Object>) sub.getListeners().get(j);
                        Util.postSuccess(listener, mute);
                    }
                }
            }
        }

        if (entry.has("CurrentTrackMetaData")) {

            String trackMetaData = entry.getString("CurrentTrackMetaData");

            MediaInfo info = DLNAMediaInfoParser.getMediaInfo(trackMetaData);

            for (URLServiceSubscription<?> sub : subscriptions) {
                if (sub.getTarget().equalsIgnoreCase("info")) {
                    for (int j = 0; j < sub.getListeners().size(); j++) {
                        @SuppressWarnings("unchecked")
                        ResponseListener<Object> listener = (ResponseListener<Object>) sub.getListeners().get(j);
                        Util.postSuccess(listener, info);
                    }
                }
            }

        }

    }

    public int getPort() {
        return port;
    }

    public List<URLServiceSubscription<?>> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<URLServiceSubscription<?>> subscriptions) {
        this.subscriptions = new CopyOnWriteArrayList<URLServiceSubscription<?>>(subscriptions);
    }

    public boolean isRunning() {
        return running;
    }
}
