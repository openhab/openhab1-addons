/*
 * RokuService
 * Connect SDK
 *
 * Copyright (c) 2014 LG Electronics.
 * Created by Hyun Kook Khang on 26 Feb 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.connectsdk.service;

import android.text.TextUtils;
import android.util.Log;

import com.connectsdk.core.AppInfo;
import com.connectsdk.core.ImageInfo;
import com.connectsdk.core.MediaInfo;
import com.connectsdk.core.Util;
import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.discovery.DiscoveryFilter;
import com.connectsdk.discovery.DiscoveryManager;
import com.connectsdk.etc.helper.DeviceServiceReachability;
import com.connectsdk.etc.helper.HttpConnection;
import com.connectsdk.etc.helper.HttpMessage;
import com.connectsdk.service.capability.CapabilityMethods;
import com.connectsdk.service.capability.KeyControl;
import com.connectsdk.service.capability.Launcher;
import com.connectsdk.service.capability.MediaControl;
import com.connectsdk.service.capability.MediaPlayer;
import com.connectsdk.service.capability.TextInputControl;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.NotSupportedServiceSubscription;
import com.connectsdk.service.command.ServiceCommand;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceSubscription;
import com.connectsdk.service.command.URLServiceSubscription;
import com.connectsdk.service.config.ServiceConfig;
import com.connectsdk.service.config.ServiceDescription;
import com.connectsdk.service.roku.RokuApplicationListParser;
import com.connectsdk.service.sessions.LaunchSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class RokuService extends DeviceService implements Launcher, MediaPlayer, MediaControl, KeyControl, TextInputControl {

    public static final String ID = "Roku";

    private static List<String> registeredApps = new ArrayList<String>();

    DIALService dialService;

    static {
        registeredApps.add("YouTube");
        registeredApps.add("Netflix");
        registeredApps.add("Amazon");
    }

    public static void registerApp(String appId) {
        if (!registeredApps.contains(appId))
            registeredApps.add(appId);
    }

    public RokuService(ServiceDescription serviceDescription,
            ServiceConfig serviceConfig) {
        super(serviceDescription, serviceConfig);
    }

    @Override
    public void setServiceDescription(ServiceDescription serviceDescription) {
        super.setServiceDescription(serviceDescription);

        if (this.serviceDescription != null)
            this.serviceDescription.setPort(8060);

        probeForAppSupport();
    }

    public static DiscoveryFilter discoveryFilter() {
        return new DiscoveryFilter(ID, "roku:ecp");
    }


    @Override
    public CapabilityPriorityLevel getPriorityLevel(Class<? extends CapabilityMethods> clazz) {
        if (clazz.equals(MediaPlayer.class)) {
            return getMediaPlayerCapabilityLevel();
        }
        else if (clazz.equals(MediaControl.class)) {
            return getMediaControlCapabilityLevel();
        }
        else if (clazz.equals(Launcher.class)) {
            return getLauncherCapabilityLevel();
        }
        else if (clazz.equals(TextInputControl.class)) {
            return getTextInputControlCapabilityLevel();
        }
        else if (clazz.equals(KeyControl.class)) {
            return getKeyControlCapabilityLevel();
        }
        return CapabilityPriorityLevel.NOT_SUPPORTED;
    }

    @Override
    public Launcher getLauncher() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getLauncherCapabilityLevel() {
        return CapabilityPriorityLevel.HIGH;
    }

    class RokuLaunchSession extends LaunchSession {

        public void close(ResponseListener<Object> responseListener) {
            home(responseListener);
        }
    }

    @Override
    public void launchApp(String appId, AppLaunchListener listener) {
        if (appId == null) {
            Util.postError(listener, new ServiceCommandError(0,
                    "Must supply a valid app id", null));
            return;
        }

        AppInfo appInfo = new AppInfo();
        appInfo.setId(appId);

        launchAppWithInfo(appInfo, listener);
    }

    @Override
    public void launchAppWithInfo(AppInfo appInfo,
            Launcher.AppLaunchListener listener) {
        launchAppWithInfo(appInfo, null, listener);
    }

    @Override
    public void launchAppWithInfo(final AppInfo appInfo, Object params,
            final Launcher.AppLaunchListener listener) {
        if (appInfo == null || appInfo.getId() == null) {
            Util.postError(listener, new ServiceCommandError(-1,
                    "Cannot launch app without valid AppInfo object",
                    appInfo));

            return;
        }

        String baseTargetURL = requestURL("launch", appInfo.getId());
        String queryParams = "";

        if (params != null && params instanceof JSONObject) {
            JSONObject jsonParams = (JSONObject) params;

            int count = 0;
            Iterator<?> jsonIterator = jsonParams.keys();

            while (jsonIterator.hasNext()) {
                String key = (String) jsonIterator.next();
                String value = null;

                try {
                    value = jsonParams.getString(key);
                } catch (JSONException ex) {
                }

                if (value == null)
                    continue;

                String urlSafeKey = null;
                String urlSafeValue = null;
                String prefix = (count == 0) ? "?" : "&";

                try {
                    urlSafeKey = URLEncoder.encode(key, "UTF-8");
                    urlSafeValue = URLEncoder.encode(value, "UTF-8");
                } catch (UnsupportedEncodingException ex) {

                }

                if (urlSafeKey == null || urlSafeValue == null)
                    continue;

                String appendString = prefix + urlSafeKey + "=" + urlSafeValue;
                queryParams = queryParams + appendString;

                count++;
            }
        }

        String targetURL = null;

        if (queryParams.length() > 0)
            targetURL = baseTargetURL + queryParams;
        else
            targetURL = baseTargetURL;

        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                LaunchSession launchSession = new RokuLaunchSession();
                launchSession.setService(RokuService.this);
                launchSession.setAppId(appInfo.getId());
                launchSession.setAppName(appInfo.getName());
                launchSession.setSessionType(LaunchSession.LaunchSessionType.App);
                Util.postSuccess(listener, launchSession);
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        };

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, targetURL, null, responseListener);
        request.send();
    }

    @Override
    public void closeApp(LaunchSession launchSession,
            ResponseListener<Object> listener) {
        home(listener);
    }

    @Override
    public void getAppList(final AppListListener listener) {
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                String msg = (String) response;

                SAXParserFactory saxParserFactory = SAXParserFactory
                        .newInstance();
                InputStream stream;
                try {
                    stream = new ByteArrayInputStream(msg.getBytes("UTF-8"));
                    SAXParser saxParser = saxParserFactory.newSAXParser();

                    RokuApplicationListParser parser = new RokuApplicationListParser();
                    saxParser.parse(stream, parser);

                    List<AppInfo> appList = parser.getApplicationList();

                    Util.postSuccess(listener, appList);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        };

        String action = "query";
        String param = "apps";

        String uri = requestURL(action, param);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, uri, null, responseListener);
        request.setHttpMethod(ServiceCommand.TYPE_GET);
        request.send();
    }

    @Override
    public void getRunningApp(AppInfoListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public ServiceSubscription<AppInfoListener> subscribeRunningApp(
            AppInfoListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());

        return new NotSupportedServiceSubscription<AppInfoListener>();
    }

    @Override
    public void getAppState(LaunchSession launchSession,
            AppStateListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public ServiceSubscription<AppStateListener> subscribeAppState(
            LaunchSession launchSession, AppStateListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());

        return null;
    }

    @Override
    public void launchBrowser(String url, Launcher.AppLaunchListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void launchYouTube(String contentId,
            Launcher.AppLaunchListener listener) {
        launchYouTube(contentId, (float) 0.0, listener);
    }

    @Override
    public void launchYouTube(String contentId, float startTime,
            AppLaunchListener listener) {
        if (getDIALService() != null) {
            getDIALService().getLauncher().launchYouTube(contentId, startTime,
                    listener);
        } else {
            Util.postError(listener, new ServiceCommandError(
                    0,
                    "Cannot reach DIAL service for launching with provided start time",
                    null));
        }
    }

    @Override
    public void launchNetflix(final String contentId,
            final Launcher.AppLaunchListener listener) {
        getAppList(new AppListListener() {

            @Override
            public void onSuccess(List<AppInfo> appList) {
                for (AppInfo appInfo : appList) {
                    if (appInfo.getName().equalsIgnoreCase("Netflix")) {
                        JSONObject payload = new JSONObject();
                        try {
                            payload.put("mediaType", "movie");

                            if (contentId != null && contentId.length() > 0)
                                payload.put("contentId", contentId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        launchAppWithInfo(appInfo, payload, listener);
                        break;
                    }
                }
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        });
    }

    @Override
    public void launchHulu(final String contentId,
            final Launcher.AppLaunchListener listener) {
        getAppList(new AppListListener() {

            @Override
            public void onSuccess(List<AppInfo> appList) {
                for (AppInfo appInfo : appList) {
                    if (appInfo.getName().contains("Hulu")) {
                        JSONObject payload = new JSONObject();
                        try {
                            payload.put("contentId", contentId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        launchAppWithInfo(appInfo, payload, listener);
                        break;
                    }
                }
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        });
    }

    @Override
    public void launchAppStore(final String appId, AppLaunchListener listener) {
        AppInfo appInfo = new AppInfo("11");
        appInfo.setName("Channel Store");

        JSONObject params = null;
        try {
            params = new JSONObject() {
                {
                    put("contentId", appId);
                }
            };
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        launchAppWithInfo(appInfo, params, listener);
    }

    @Override
    public KeyControl getKeyControl() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getKeyControlCapabilityLevel() {
        return CapabilityPriorityLevel.HIGH;
    }

    @Override
    public void up(ResponseListener<Object> listener) {
        String action = "keypress";
        String param = "Up";

        String uri = requestURL(action, param);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, uri, null, listener);
        request.send();
    }

    @Override
    public void down(final ResponseListener<Object> listener) {
        String action = "keypress";
        String param = "Down";

        String uri = requestURL(action, param);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, uri, null, listener);
        request.send();
    }

    @Override
    public void left(ResponseListener<Object> listener) {
        String action = "keypress";
        String param = "Left";

        String uri = requestURL(action, param);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, uri, null, listener);
        request.send();
    }

    @Override
    public void right(ResponseListener<Object> listener) {
        String action = "keypress";
        String param = "Right";

        String uri = requestURL(action, param);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, uri, null, listener);
        request.send();
    }

    @Override
    public void ok(final ResponseListener<Object> listener) {
        String action = "keypress";
        String param = "Select";

        String uri = requestURL(action, param);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, uri, null, listener);
        request.send();
    }

    @Override
    public void back(ResponseListener<Object> listener) {
        String action = "keypress";
        String param = "Back";

        String uri = requestURL(action, param);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, uri, null, listener);
        request.send();
    }

    @Override
    public void home(ResponseListener<Object> listener) {
        String action = "keypress";
        String param = "Home";

        String uri = requestURL(action, param);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, uri, null, listener);
        request.send();
    }

    @Override
    public MediaControl getMediaControl() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getMediaControlCapabilityLevel() {
        return CapabilityPriorityLevel.HIGH;
    }

    @Override
    public void play(ResponseListener<Object> listener) {
        String action = "keypress";
        String param = "Play";

        String uri = requestURL(action, param);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, uri, null, listener);
        request.send();
    }

    @Override
    public void pause(ResponseListener<Object> listener) {
        String action = "keypress";
        String param = "Play";

        String uri = requestURL(action, param);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, uri, null, listener);
        request.send();
    }

    @Override
    public void stop(ResponseListener<Object> listener) {
        String action = null;
        String param = "input?a=sto";

        String uri = requestURL(action, param);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, uri, null, listener);
        request.send();
    }

    @Override
    public void rewind(ResponseListener<Object> listener) {
        String action = "keypress";
        String param = "Rev";

        String uri = requestURL(action, param);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, uri, null, listener);
        request.send();
    }

    @Override
    public void fastForward(ResponseListener<Object> listener) {
        String action = "keypress";
        String param = "Fwd";

        String uri = requestURL(action, param);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, uri, null, listener);
        request.send();
    }

    @Override
    public void previous(ResponseListener<Object> listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void next(ResponseListener<Object> listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void getDuration(DurationListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void getPosition(PositionListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void seek(long position, ResponseListener<Object> listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public MediaPlayer getMediaPlayer() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getMediaPlayerCapabilityLevel() {
        return CapabilityPriorityLevel.HIGH;
    }

    @Override
    public void getMediaInfo(MediaInfoListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public ServiceSubscription<MediaInfoListener> subscribeMediaInfo(
            MediaInfoListener listener) {
        listener.onError(ServiceCommandError.notSupported());
        return null;
    }

    private void displayMedia(String url, String mimeType, String title,
            String description, String iconSrc,
            final MediaPlayer.LaunchListener listener) {
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                LaunchSession launchSession = new RokuLaunchSession();
                launchSession.setService(RokuService.this);
                launchSession.setSessionType(LaunchSession.LaunchSessionType.Media);
                Util.postSuccess(listener, new MediaLaunchObject(launchSession, RokuService.this));
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        };

        String action = "input";
        String mediaFormat = mimeType;
        if (mimeType.contains("/")) {
            int index = mimeType.indexOf("/") + 1;
            mediaFormat = mimeType.substring(index);
        }

        String param;
        if (mimeType.contains("image")) {
            param = String.format("15985?t=p&u=%s&tr=crossfade", HttpMessage.encode(url));
        } else if (mimeType.contains("video")) {
            param = String.format(
                    "15985?t=v&u=%s&k=(null)&videoName=%s&videoFormat=%s",
                    HttpMessage.encode(url),
                    TextUtils.isEmpty(title) ? "(null)" : HttpMessage.encode(title),
                            HttpMessage.encode(mediaFormat));
        } else { // if (mimeType.contains("audio")) {
            param = String
                    .format("15985?t=a&u=%s&k=(null)&songname=%s&artistname=%s&songformat=%s&albumarturl=%s",
                            HttpMessage.encode(url),
                            TextUtils.isEmpty(title) ? "(null)" : HttpMessage.encode(title),
                            TextUtils.isEmpty(description) ? "(null)" : HttpMessage.encode(description),
                            HttpMessage.encode(mediaFormat),
                            TextUtils.isEmpty(iconSrc) ? "(null)" : HttpMessage.encode(iconSrc));
        }

        String uri = requestURL(action, param);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, uri, null, responseListener);
        request.send();
    }

    @Override
    public void displayImage(String url, String mimeType, String title,
            String description, String iconSrc,
            MediaPlayer.LaunchListener listener) {
        displayMedia(url, mimeType, title, description, iconSrc, listener);
    }

    @Override
    public void displayImage(MediaInfo mediaInfo,
            MediaPlayer.LaunchListener listener) {
        String mediaUrl = null;
        String mimeType = null;
        String title = null;
        String desc = null;
        String iconSrc = null;

        if (mediaInfo != null) {
            mediaUrl = mediaInfo.getUrl();
            mimeType = mediaInfo.getMimeType();
            title = mediaInfo.getTitle();
            desc = mediaInfo.getDescription();

            if (mediaInfo.getImages() != null && mediaInfo.getImages().size() > 0) {
                ImageInfo imageInfo = mediaInfo.getImages().get(0);
                iconSrc = imageInfo.getUrl();
            }
        }

        displayImage(mediaUrl, mimeType, title, desc, iconSrc, listener);
    }

    @Override
    public void playMedia(String url, String mimeType, String title,
            String description, String iconSrc, boolean shouldLoop,
            MediaPlayer.LaunchListener listener) {
        displayMedia(url, mimeType, title, description, iconSrc, listener);
    }

    @Override
    public void playMedia(MediaInfo mediaInfo, boolean shouldLoop,
            MediaPlayer.LaunchListener listener) {
        String mediaUrl = null;
        String mimeType = null;
        String title = null;
        String desc = null;
        String iconSrc = null;

        if (mediaInfo != null) {
            mediaUrl = mediaInfo.getUrl();
            mimeType = mediaInfo.getMimeType();
            title = mediaInfo.getTitle();
            desc = mediaInfo.getDescription();

            if (mediaInfo.getImages() != null && mediaInfo.getImages().size() > 0) {
                ImageInfo imageInfo = mediaInfo.getImages().get(0);
                iconSrc = imageInfo.getUrl();
            }
        }

        playMedia(mediaUrl, mimeType, title, desc, iconSrc, shouldLoop, listener);
    }

    @Override
    public void closeMedia(LaunchSession launchSession,
            ResponseListener<Object> listener) {
        home(listener);
    }

    @Override
    public TextInputControl getTextInputControl() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getTextInputControlCapabilityLevel() {
        return CapabilityPriorityLevel.HIGH;
    }

    @Override
    public ServiceSubscription<TextInputStatusListener> subscribeTextInputStatus(
            TextInputStatusListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());

        return new NotSupportedServiceSubscription<TextInputStatusListener>();
    }

    @Override
    public void sendText(String input) {
        if (input == null || input.length() == 0) {
            return;
        }

        ResponseListener<Object> listener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(ServiceCommandError error) {
                // TODO Auto-generated method stub

            }
        };

        String action = "keypress";
        String param = null;
        try {
            param = "Lit_" + URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // This can be safetly ignored since it isn't a dynamic encoding.
            e.printStackTrace();
        }

        String uri = requestURL(action, param);

        Log.d(Util.T, "RokuService::send() | uri = " + uri);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, uri, null, listener);
        request.send();
    }

    @Override
    public void sendKeyCode(KeyCode keyCode, ResponseListener<Object> listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void sendEnter() {
        ResponseListener<Object> listener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(ServiceCommandError error) {
                // TODO Auto-generated method stub

            }
        };

        String action = "keypress";
        String param = "Enter";

        String uri = requestURL(action, param);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, uri, null, listener);
        request.send();
    }

    @Override
    public void sendDelete() {
        ResponseListener<Object> listener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(ServiceCommandError error) {
                // TODO Auto-generated method stub

            }
        };

        String action = "keypress";
        String param = "Backspace";

        String uri = requestURL(action, param);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(
                this, uri, null, listener);
        request.send();
    }

    @Override
    public void unsubscribe(URLServiceSubscription<?> subscription) {
    }

    @Override
    public void sendCommand(final ServiceCommand<?> mCommand) {
        Util.runInBackground(new Runnable() {

            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                ServiceCommand<ResponseListener<Object>> command = (ServiceCommand<ResponseListener<Object>>) mCommand;
                Object payload = command.getPayload();

                try {
                    Log.d("", "RESP " + command.getTarget());
                    HttpConnection connection = HttpConnection.newInstance(URI.create(command.getTarget()));
                    if (command.getHttpMethod().equalsIgnoreCase(ServiceCommand.TYPE_POST)) {
                        connection.setMethod(HttpConnection.Method.POST);
                        if (payload != null) {
                            connection.setPayload(payload.toString());
                        }
                    }
                    connection.execute();
                    int code = connection.getResponseCode();
                    Log.d("", "RESP " + code);
                    if (code == 200 || code == 201) {
                        Util.postSuccess(command.getResponseListener(), connection.getResponseString());
                    } else {
                        Util.postError(command.getResponseListener(), ServiceCommandError.getError(code));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Util.postError(command.getResponseListener(), new ServiceCommandError(0, e.getMessage(), null));
                }

            }
        });
    }

    private String requestURL(String action, String parameter) {
        StringBuilder sb = new StringBuilder();

        sb.append("http://");
        sb.append(serviceDescription.getIpAddress()).append(":");
        sb.append(serviceDescription.getPort()).append("/");

        if (action != null)
            sb.append(action);

        if (parameter != null)
            sb.append("/").append(parameter);

        return sb.toString();
    }

    private void probeForAppSupport() {
        getAppList(new AppListListener() {

            @Override
            public void onError(ServiceCommandError error) {
            }

            @Override
            public void onSuccess(List<AppInfo> object) {
                List<String> appsToAdd = new ArrayList<String>();

                for (String probe : registeredApps) {
                    for (AppInfo app : object) {
                        if (app.getName().contains(probe)) {
                            appsToAdd.add("Launcher." + probe);
                            appsToAdd.add("Launcher." + probe + ".Params");
                        }
                    }
                }

                addCapabilities(appsToAdd);
            }
        });
    }

    @Override
    protected void updateCapabilities() {
        List<String> capabilities = new ArrayList<String>();

        capabilities.add(Up);
        capabilities.add(Down);
        capabilities.add(Left);
        capabilities.add(Right);
        capabilities.add(OK);
        capabilities.add(Back);
        capabilities.add(Home);
        capabilities.add(Send_Key);

        capabilities.add(Application);

        capabilities.add(Application_Params);
        capabilities.add(Application_List);
        capabilities.add(AppStore);
        capabilities.add(AppStore_Params);
        capabilities.add(Application_Close);

        capabilities.add(Display_Image);
        capabilities.add(Play_Video);
        capabilities.add(Play_Audio);
        capabilities.add(Close);
        capabilities.add(MetaData_Title);

        capabilities.add(FastForward);
        capabilities.add(Rewind);
        capabilities.add(Play);
        capabilities.add(Pause);

        capabilities.add(Send);
        capabilities.add(Send_Delete);
        capabilities.add(Send_Enter);

        setCapabilities(capabilities);
    }

    @Override
    public void getPlayState(PlayStateListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public ServiceSubscription<PlayStateListener> subscribePlayState(
            PlayStateListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());

        return null;
    }

    @Override
    public boolean isConnectable() {
        return true;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void connect() {
        // TODO: Fix this for roku. Right now it is using the InetAddress
        // reachable function. Need to use an HTTP Method.
        // mServiceReachability =
        // DeviceServiceReachability.getReachability(serviceDescription.getIpAddress(),
        // this);
        // mServiceReachability.start();

        connected = true;

        reportConnected(true);
    }

    @Override
    public void disconnect() {
        connected = false;

        if (mServiceReachability != null)
            mServiceReachability.stop();

        Util.runOnUI(new Runnable() {

            @Override
            public void run() {
                if (listener != null)
                    listener.onDisconnect(RokuService.this, null);
            }
        });
    }

    @Override
    public void onLoseReachability(DeviceServiceReachability reachability) {
        if (connected) {
            disconnect();
        } else {
            if (mServiceReachability != null)
                mServiceReachability.stop();
        }
    }

    public DIALService getDIALService() {
        if (dialService == null) {
            DiscoveryManager discoveryManager = DiscoveryManager.getInstance();
            ConnectableDevice device = discoveryManager.getAllDevices().get(
                    serviceDescription.getIpAddress());

            if (device != null) {
                DIALService foundService = null;

                for (DeviceService service : device.getServices()) {
                    if (DIALService.class.isAssignableFrom(service.getClass())) {
                        foundService = (DIALService) service;
                        break;
                    }
                }

                dialService = foundService;
            }
        }

        return dialService;
    }
}
