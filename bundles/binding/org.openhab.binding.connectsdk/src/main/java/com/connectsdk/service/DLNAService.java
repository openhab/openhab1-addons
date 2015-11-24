/*
 * DLNAService
 * Connect SDK
 *
 * Copyright (c) 2014 LG Electronics.
 * Created by Hyun Kook Khang on 19 Jan 2014
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

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.util.Xml;

import com.connectsdk.core.ImageInfo;
import com.connectsdk.core.MediaInfo;
import com.connectsdk.core.SubtitleInfo;
import com.connectsdk.core.Util;
import com.connectsdk.discovery.DiscoveryFilter;
import com.connectsdk.discovery.DiscoveryManager;
import com.connectsdk.discovery.provider.ssdp.Service;
import com.connectsdk.etc.helper.DeviceServiceReachability;
import com.connectsdk.etc.helper.HttpConnection;
import com.connectsdk.service.capability.CapabilityMethods;
import com.connectsdk.service.capability.MediaControl;
import com.connectsdk.service.capability.MediaPlayer;
import com.connectsdk.service.capability.PlaylistControl;
import com.connectsdk.service.capability.VolumeControl;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommand;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceSubscription;
import com.connectsdk.service.command.URLServiceSubscription;
import com.connectsdk.service.config.ServiceConfig;
import com.connectsdk.service.config.ServiceDescription;
import com.connectsdk.service.sessions.LaunchSession;
import com.connectsdk.service.sessions.LaunchSession.LaunchSessionType;
import com.connectsdk.service.upnp.DLNAHttpServer;
import com.connectsdk.service.upnp.DLNAMediaInfoParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class DLNAService extends DeviceService implements PlaylistControl, MediaControl, MediaPlayer, VolumeControl {
    public static final String ID = "DLNA";

    protected static final String SUBSCRIBE = "SUBSCRIBE";
    protected static final String UNSUBSCRIBE = "UNSUBSCRIBE";

    public static final String AV_TRANSPORT_URN = "urn:schemas-upnp-org:service:AVTransport:1";
    public static final String CONNECTION_MANAGER_URN = "urn:schemas-upnp-org:service:ConnectionManager:1";
    public static final String RENDERING_CONTROL_URN = "urn:schemas-upnp-org:service:RenderingControl:1";

    protected static final String AV_TRANSPORT = "AVTransport";
    protected static final String CONNECTION_MANAGER = "ConnectionManager";
    protected static final String RENDERING_CONTROL = "RenderingControl";
    protected static final String GROUP_RENDERING_CONTROL = "GroupRenderingControl";

    public static final String PLAY_STATE = "playState";
    public static final String DEFAULT_SUBTITLE_MIMETYPE = "text/srt";
    public static final String DEFAULT_SUBTITLE_TYPE = "srt";

    Context context;

    String avTransportURL, renderingControlURL, connectionControlURL;

    DLNAHttpServer httpServer;

    Map<String, String> SIDList;
    Timer resubscriptionTimer;

    private static int TIMEOUT = 300;

    interface PositionInfoListener {
        public void onGetPositionInfoSuccess(String positionInfoXml);
        public void onGetPositionInfoFailed(ServiceCommandError error);
    }

    public DLNAService(ServiceDescription serviceDescription, ServiceConfig serviceConfig) {
        this(serviceDescription, serviceConfig, DiscoveryManager.getInstance().getContext(), new DLNAHttpServer());
    }

    public DLNAService(ServiceDescription serviceDescription, ServiceConfig serviceConfig, Context context, DLNAHttpServer dlnaServer) {
        super(serviceDescription, serviceConfig);
        this.context = context;
        SIDList = new HashMap<String, String>();
        updateControlURL();
        httpServer = dlnaServer;
    }

    public static DiscoveryFilter discoveryFilter() {
        return new DiscoveryFilter(ID, "urn:schemas-upnp-org:device:MediaRenderer:1");
    }

    @Override
    public CapabilityPriorityLevel getPriorityLevel(Class<? extends CapabilityMethods> clazz) {
        if (clazz.equals(MediaPlayer.class)) {
            return getMediaPlayerCapabilityLevel();
        }
        else if (clazz.equals(MediaControl.class)) {
            return getMediaControlCapabilityLevel();
        }
        else if (clazz.equals(VolumeControl.class)) {
            return getVolumeControlCapabilityLevel();
        }
        else if (clazz.equals(PlaylistControl.class)) {
            return getPlaylistControlCapabilityLevel();
        }
        return CapabilityPriorityLevel.NOT_SUPPORTED;
    }


    @Override
    public void setServiceDescription(ServiceDescription serviceDescription) {
        super.setServiceDescription(serviceDescription);

        updateControlURL();
    }

    private void updateControlURL() {
        List<Service> serviceList = serviceDescription.getServiceList();

        if (serviceList != null) {
            for (int i = 0; i < serviceList.size(); i++) {
                if(!serviceList.get(i).baseURL.endsWith("/")) {
                    serviceList.get(i).baseURL += "/";
                }

                if (serviceList.get(i).serviceType.contains(AV_TRANSPORT)) {
                    avTransportURL = makeControlURL(serviceList.get(i).baseURL, serviceList.get(i).controlURL);
                }
                else if ((serviceList.get(i).serviceType.contains(RENDERING_CONTROL)) && !(serviceList.get(i).serviceType.contains(GROUP_RENDERING_CONTROL))) {
                    renderingControlURL = makeControlURL(serviceList.get(i).baseURL, serviceList.get(i).controlURL);
                }
                else if ((serviceList.get(i).serviceType.contains(CONNECTION_MANAGER)) ) {
                    connectionControlURL = makeControlURL(serviceList.get(i).baseURL, serviceList.get(i).controlURL);
                }

            }
        }
    }

    String makeControlURL(String base, String path) {
        if (base == null || path == null) {
            return null;
        }
        if (path.startsWith("/")) {
            return base + path.substring(1);
        }
        return base + path;
    }

    /******************
     MEDIA PLAYER
     *****************/
    @Override
    public MediaPlayer getMediaPlayer() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getMediaPlayerCapabilityLevel() {
        return CapabilityPriorityLevel.NORMAL;
    }

    @Override
    public void getMediaInfo(final MediaInfoListener listener) {
        getPositionInfo(new PositionInfoListener() {

            @Override
            public void onGetPositionInfoSuccess(final String positionInfoXml) {
                Util.runInBackground(new Runnable() {
                    @Override
                    public void run() {
                        String baseUrl = "http://" + getServiceDescription().getIpAddress() + ":" + getServiceDescription().getPort();
                        String trackMetaData = parseData(positionInfoXml, "TrackMetaData");
                        MediaInfo info = DLNAMediaInfoParser.getMediaInfo(trackMetaData, baseUrl);
                        Util.postSuccess(listener, info);
                    }
                });
            }

            @Override
            public void onGetPositionInfoFailed(ServiceCommandError error) {
                Util.postError(listener, error);

            }
        });
    }

    @Override
    public ServiceSubscription<MediaInfoListener> subscribeMediaInfo(MediaInfoListener listener) {
        URLServiceSubscription<MediaInfoListener> request = new URLServiceSubscription<MediaInfoListener>(this, "info", null, null);
        request.addListener(listener);
        addSubscription(request);
        return request;

    }

    @Deprecated
    public void displayMedia(String url, String mimeType, String title, String description, String iconSrc, final LaunchListener listener) {
        displayMedia(url, null, mimeType, title, description, iconSrc, listener);
    }

    private void displayMedia(String url, SubtitleInfo subtitle, String mimeType, String title, String description, String iconSrc, final LaunchListener listener) {
        final String instanceId = "0";
        String[] mediaElements = mimeType.split("/");
        String mediaType = mediaElements[0];
        String mediaFormat = mediaElements[1];

        if (mediaType == null || mediaType.length() == 0 || mediaFormat == null || mediaFormat.length() == 0) {
            Util.postError(listener, new ServiceCommandError(0, "You must provide a valid mimeType (audio/*,  video/*, etc)", null));
            return;
        }

        mediaFormat = "mp3".equals(mediaFormat) ? "mpeg" : mediaFormat;
        String mMimeType = String.format("%s/%s", mediaType, mediaFormat);

        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                String method = "Play";

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("Speed", "1");

                String payload = getMessageXml(AV_TRANSPORT_URN, method, "0", parameters);

                ResponseListener<Object> playResponseListener = new ResponseListener<Object> () {
                    @Override
                    public void onSuccess(Object response) {
                        LaunchSession launchSession = new LaunchSession();
                        launchSession.setService(DLNAService.this);
                        launchSession.setSessionType(LaunchSessionType.Media);

                        Util.postSuccess(listener, new MediaLaunchObject(launchSession, DLNAService.this, DLNAService.this));
                    }

                    @Override
                    public void onError(ServiceCommandError error) {
                        Util.postError(listener, error);
                    }
                };

                ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(DLNAService.this, method, payload, playResponseListener);
                request.send();
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        };

        String method = "SetAVTransportURI";
        String metadata = getMetadata(url, subtitle, mMimeType, title, description, iconSrc);
        if (metadata == null) {
            Util.postError(listener, ServiceCommandError.getError(500));
            return;
        }

        Map<String, String> params = new LinkedHashMap<String, String>();
        try {
            params.put("CurrentURI", encodeURL(url));
        } catch (Exception e) {
            Util.postError(listener, ServiceCommandError.getError(500));
            return;
        }
        params.put("CurrentURIMetaData", metadata);

        String payload = getMessageXml(AV_TRANSPORT_URN, method, instanceId, params);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(DLNAService.this, method, payload, responseListener);
        request.send();
    }

    @Override
    public void displayImage(String url, String mimeType, String title, String description, String iconSrc, LaunchListener listener) {
        displayMedia(url, null, mimeType, title, description, iconSrc, listener);
    }

    @Override
    public void displayImage(MediaInfo mediaInfo, LaunchListener listener) {
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
    public void playMedia(String url, String mimeType, String title, String description, String iconSrc, boolean shouldLoop, LaunchListener listener) {
        displayMedia(url, null, mimeType, title, description, iconSrc, listener);
    }

    @Override
    public void playMedia(MediaInfo mediaInfo, boolean shouldLoop,
            LaunchListener listener) {
        String mediaUrl = null;
        SubtitleInfo subtitle = null;
        String mimeType = null;
        String title = null;
        String desc = null;
        String iconSrc = null;

        if (mediaInfo != null) {
            mediaUrl = mediaInfo.getUrl();
            subtitle = mediaInfo.getSubtitleInfo();
            mimeType = mediaInfo.getMimeType();
            title = mediaInfo.getTitle();
            desc = mediaInfo.getDescription();

            if (mediaInfo.getImages() != null && mediaInfo.getImages().size() > 0) {
                ImageInfo imageInfo = mediaInfo.getImages().get(0);
                iconSrc = imageInfo.getUrl();
            }
        }

        displayMedia(mediaUrl, subtitle, mimeType, title, desc, iconSrc, listener);
    }

    @Override
    public void closeMedia(LaunchSession launchSession, ResponseListener<Object> listener) {
        if (launchSession.getService() instanceof DLNAService)
            ((DLNAService) launchSession.getService()).stop(listener);
    }

    /******************
     MEDIA CONTROL
     *****************/
    @Override
    public MediaControl getMediaControl() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getMediaControlCapabilityLevel() {
        return CapabilityPriorityLevel.NORMAL;
    }

    @Override
    public void play(ResponseListener<Object> listener) {
        String method = "Play";
        String instanceId = "0";

        Map<String, String> parameters = new LinkedHashMap<String, String>();
        parameters.put("Speed", "1");

        String payload = getMessageXml(AV_TRANSPORT_URN, method, instanceId, parameters);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, method, payload, listener);
        request.send();
    }

    @Override
    public void pause(ResponseListener<Object> listener) {
        String method = "Pause";
        String instanceId = "0";

        String payload = getMessageXml(AV_TRANSPORT_URN, method, instanceId, null);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, method, payload, listener);
        request.send();
    }

    @Override
    public void stop(ResponseListener<Object> listener) {
        String method = "Stop";
        String instanceId = "0";

        String payload = getMessageXml(AV_TRANSPORT_URN, method, instanceId, null);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, method, payload, listener);
        request.send();
    }

    @Override
    public void rewind(ResponseListener<Object> listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void fastForward(ResponseListener<Object> listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    /******************
     PLAYLIST CONTROL
     *****************/

    @Override
    public PlaylistControl getPlaylistControl() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getPlaylistControlCapabilityLevel() {
        return CapabilityPriorityLevel.NORMAL;
    }

    @Override
    public void previous(ResponseListener<Object> listener) {
        String method = "Previous";
        String instanceId = "0";

        String payload = getMessageXml(AV_TRANSPORT_URN, method, instanceId, null);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, method, payload, listener);
        request.send();
    }

    @Override
    public void next(ResponseListener<Object> listener) {
        String method = "Next";
        String instanceId = "0";

        String payload = getMessageXml(AV_TRANSPORT_URN, method, instanceId, null);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, method, payload, listener);
        request.send();
    }

    @Override
    public void jumpToTrack(long index, ResponseListener<Object> listener) {
        // DLNA requires start index from 1. 0 is a special index which means the end of media.
        ++index;
        seek("TRACK_NR", Long.toString(index), listener);
    }

    @Override
    public void setPlayMode(PlayMode playMode, ResponseListener<Object> listener) {
        String method = "SetPlayMode";
        String instanceId = "0";
        String mode;

        switch (playMode) {
        case RepeatAll:
            mode = "REPEAT_ALL";
            break;
        case RepeatOne:
            mode = "REPEAT_ONE";
            break;
        case Shuffle:
            mode = "SHUFFLE";
            break;
        default:
            mode = "NORMAL";
        }

        Map<String, String> parameters = new LinkedHashMap<String, String>();
        parameters.put("NewPlayMode", mode);

        String payload = getMessageXml(AV_TRANSPORT_URN, method, instanceId, parameters);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, method, payload, listener);
        request.send();
    }

    @Override
    public void seek(long position, ResponseListener<Object> listener) {
        long second = (position / 1000) % 60;
        long minute = (position / (1000 * 60)) % 60;
        long hour = (position / (1000 * 60 * 60)) % 24;
        String time = String.format(Locale.US, "%02d:%02d:%02d", hour, minute, second);
        seek("REL_TIME", time, listener);
    }

    private void getPositionInfo(final PositionInfoListener listener) {
        String method = "GetPositionInfo";
        String instanceId = "0";

        String payload = getMessageXml(AV_TRANSPORT_URN, method, instanceId, null);
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                if (listener != null) {
                    listener.onGetPositionInfoSuccess((String)response);
                }
            }

            @Override
            public void onError(ServiceCommandError error) {
                if (listener != null) {
                    listener.onGetPositionInfoFailed(error);
                }
            }
        };

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, method, payload, responseListener);
        request.send();
    }

    @Override
    public void getDuration(final DurationListener listener) {
        getPositionInfo(new PositionInfoListener() {

            @Override
            public void onGetPositionInfoSuccess(String positionInfoXml) {
                String strDuration = parseData(positionInfoXml, "TrackDuration");

                String trackMetaData = parseData(positionInfoXml, "TrackMetaData");
                MediaInfo info = DLNAMediaInfoParser.getMediaInfo(trackMetaData);
                // Check if duration we get not equals 0 or media is image, otherwise wait 1 second and try again
                if ((!strDuration.equals("0:00:00")) || (info.getMimeType().contains("image"))) {
                    long milliTimes = convertStrTimeFormatToLong(strDuration);

                    Util.postSuccess(listener, milliTimes);
                } else new Timer().schedule(new TimerTask() {

                    @Override
                    public void run() {
                        getDuration(listener);

                    }
                }, 1000);

            }

            @Override
            public void onGetPositionInfoFailed(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        });
    }

    @Override
    public void getPosition(final PositionListener listener) {
        getPositionInfo(new PositionInfoListener() {

            @Override
            public void onGetPositionInfoSuccess(String positionInfoXml) {
                String strDuration = parseData(positionInfoXml, "RelTime");

                long milliTimes = convertStrTimeFormatToLong(strDuration);

                Util.postSuccess(listener, milliTimes);
            }

            @Override
            public void onGetPositionInfoFailed(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        });
    }

    protected void seek(String unit, String target, ResponseListener<Object> listener) {
        String method = "Seek";
        String instanceId = "0";

        Map<String, String> parameters = new LinkedHashMap<String, String>();
        parameters.put("Unit", unit);
        parameters.put("Target", target);

        String payload = getMessageXml(AV_TRANSPORT_URN, method, instanceId, parameters);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, method, payload, listener);
        request.send();
    }

    protected String getMessageXml(String serviceURN, String method, String instanceId, Map<String, String> params) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            doc.setXmlStandalone(true);
            doc.setXmlVersion("1.0");

            Element root = doc.createElement("s:Envelope");
            Element bodyElement = doc.createElement("s:Body");
            Element methodElement = doc.createElementNS(serviceURN, "u:" + method);
            Element instanceElement = doc.createElement("InstanceID");

            root.setAttribute("s:encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/");
            root.setAttribute("xmlns:s", "http://schemas.xmlsoap.org/soap/envelope/");

            doc.appendChild(root);
            root.appendChild(bodyElement);
            bodyElement.appendChild(methodElement);
            if (instanceId != null) {
                instanceElement.setTextContent(instanceId);
                methodElement.appendChild(instanceElement);
            }

            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    Element element = doc.createElement(key);
                    element.setTextContent(value);
                    methodElement.appendChild(element);
                }
            }
            return xmlToString(doc, true);
        } catch (Exception e) {
            return null;
        }
    }

    protected String getMetadata(String mediaURL, SubtitleInfo subtitle, String mime, String title, String description, String iconUrl) {
        try {
            String objectClass = "";
            if (mime.startsWith("image")) {
                objectClass = "object.item.imageItem";
            } else if (mime.startsWith("video")) {
                objectClass = "object.item.videoItem";
            } else if (mime.startsWith("audio")) {
                objectClass = "object.item.audioItem";
            }

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            Element didlRoot = doc.createElement("DIDL-Lite");
            Element itemElement = doc.createElement("item");
            Element titleElement = doc.createElement("dc:title");
            Element descriptionElement = doc.createElement("dc:description");
            Element resElement = doc.createElement("res");
            Element albumArtElement = doc.createElement("upnp:albumArtURI");
            Element clazzElement = doc.createElement("upnp:class");

            didlRoot.appendChild(itemElement);
            itemElement.appendChild(titleElement);
            itemElement.appendChild(descriptionElement);
            itemElement.appendChild(resElement);
            itemElement.appendChild(albumArtElement);
            itemElement.appendChild(clazzElement);

            didlRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/");
            didlRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:upnp", "urn:schemas-upnp-org:metadata-1-0/upnp/");
            didlRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:dc", "http://purl.org/dc/elements/1.1/");
            didlRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:sec", "http://www.sec.co.kr/");

            titleElement.setTextContent(title);
            descriptionElement.setTextContent(description);
            resElement.setTextContent(encodeURL(mediaURL));
            albumArtElement.setTextContent(encodeURL(iconUrl));
            clazzElement.setTextContent(objectClass);

            itemElement.setAttribute("id", "1000");
            itemElement.setAttribute("parentID", "0");
            itemElement.setAttribute("restricted", "0");

            resElement.setAttribute("protocolInfo", "http-get:*:" + mime + ":DLNA.ORG_OP=01");

            if (subtitle != null) {
                String mimeType = (subtitle.getMimeType() == null) ? DEFAULT_SUBTITLE_TYPE : subtitle.getMimeType();
                String type;
                String[] typeParts =  mimeType.split("/");
                if (typeParts != null && typeParts.length == 2) {
                    type = typeParts[1];
                } else {
                    mimeType = DEFAULT_SUBTITLE_MIMETYPE;
                    type = DEFAULT_SUBTITLE_TYPE;
                }


                resElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:pv", "http://www.pv.com/pvns/");
                resElement.setAttribute("pv:subtitleFileUri", subtitle.getUrl());
                resElement.setAttribute("pv:subtitleFileType", type);

                Element smiResElement = doc.createElement("res");
                smiResElement.setAttribute("protocolInfo", "http-get:*:smi/caption");
                smiResElement.setTextContent(subtitle.getUrl());
                itemElement.appendChild(smiResElement);

                Element srtResElement = doc.createElement("res");
                srtResElement.setAttribute("protocolInfo", "http-get:*:"+mimeType+":");
                srtResElement.setTextContent(subtitle.getUrl());
                itemElement.appendChild(srtResElement);

                Element captionInfoExElement = doc.createElement("sec:CaptionInfoEx");
                captionInfoExElement.setAttribute("sec:type", type);
                captionInfoExElement.setTextContent(subtitle.getUrl());
                itemElement.appendChild(captionInfoExElement);

                Element captionInfoElement = doc.createElement("sec:CaptionInfo");
                captionInfoElement.setAttribute("sec:type", type);
                captionInfoElement.setTextContent(subtitle.getUrl());
                itemElement.appendChild(captionInfoElement);
            }

            doc.appendChild(didlRoot);
            return xmlToString(doc, false);
        } catch (Exception e) {
            return null;
        }
    }

    String encodeURL(String mediaURL) throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        if (mediaURL == null || mediaURL.isEmpty()) {
            return "";
        }
        String decodedURL = URLDecoder.decode(mediaURL, "UTF-8");
        if (decodedURL.equals(mediaURL)) {
            URL url = new URL(mediaURL);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            return uri.toASCIIString();
        }
        return mediaURL;
    }

    String xmlToString(Node source, boolean xmlDeclaration) throws TransformerException {
        DOMSource domSource = new DOMSource(source);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        if (!xmlDeclaration) {
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        }
        transformer.transform(domSource, result);
        return writer.toString();
    }


    @Override
    public void sendCommand(final ServiceCommand<?> mCommand) {
        Util.runInBackground(new Runnable() {

            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                ServiceCommand<ResponseListener<Object>> command = (ServiceCommand<ResponseListener<Object>>) mCommand;

                String method = command.getTarget();
                String payload = (String) command.getPayload();

                String targetURL = null;
                String serviceURN = null;

                if (payload == null) {
                    Util.postError(command.getResponseListener(), new ServiceCommandError(0, "Cannot process the command, \"payload\" is missed", null));
                    return;
                }

                if (payload.contains(AV_TRANSPORT_URN)) {
                    targetURL = avTransportURL;
                    serviceURN = AV_TRANSPORT_URN;
                } else if (payload.contains(RENDERING_CONTROL_URN)) {
                    targetURL = renderingControlURL;
                    serviceURN = RENDERING_CONTROL_URN;
                } else if (payload.contains(CONNECTION_MANAGER_URN)) {
                    targetURL = connectionControlURL;
                    serviceURN = CONNECTION_MANAGER_URN;
                }

                if (serviceURN == null) {
                    Util.postError(command.getResponseListener(), new ServiceCommandError(0, "Cannot process the command, \"serviceURN\" is missed", null));
                    return;
                }

                if (targetURL == null) {
                    Util.postError(command.getResponseListener(), new ServiceCommandError(0, "Cannot process the command, \"targetURL\" is missed", null));
                    return;
                }

                try {
                    HttpConnection connection = createHttpConnection(targetURL);
                    connection.setHeader("Content-Type", "text/xml; charset=utf-8");
                    connection.setHeader("SOAPAction", String.format("\"%s#%s\"", serviceURN, method));
                    connection.setMethod(HttpConnection.Method.POST);
                    connection.setPayload(payload);
                    connection.execute();
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        Util.postSuccess(command.getResponseListener(), connection.getResponseString());
                    } else {
                        Util.postError(command.getResponseListener(), ServiceCommandError.getError(code));
                    }
                } catch (IOException e) {
                    Util.postError(command.getResponseListener(), new ServiceCommandError(0, e.getMessage(), null));
                }
            }
        });
    }

    HttpConnection createHttpConnection(String targetURL) throws IOException {
        return HttpConnection.newInstance(URI.create(targetURL));
    }

    @Override
    protected void updateCapabilities() {
        List<String> capabilities = new ArrayList<String>();

        capabilities.add(Display_Image);
        capabilities.add(Play_Video);
        capabilities.add(Play_Audio);
        capabilities.add(Play_Playlist);
        capabilities.add(Close);
        capabilities.add(Subtitle_SRT);

        capabilities.add(MetaData_Title);
        capabilities.add(MetaData_MimeType);
        capabilities.add(MediaInfo_Get);
        capabilities.add(MediaInfo_Subscribe);

        capabilities.add(Play);
        capabilities.add(Pause);
        capabilities.add(Stop);
        capabilities.add(Seek);
        capabilities.add(Position);
        capabilities.add(Duration);
        capabilities.add(PlayState);
        capabilities.add(PlayState_Subscribe);

        // for supporting legacy apps. it might be removed in future releases  
        capabilities.add(MediaControl.Next);
        capabilities.add(MediaControl.Previous);

        // playlist capabilities
        capabilities.add(PlaylistControl.Next);
        capabilities.add(PlaylistControl.Previous);
        capabilities.add(PlaylistControl.JumpToTrack);
        capabilities.add(PlaylistControl.SetPlayMode);

        capabilities.add(Volume_Set);
        capabilities.add(Volume_Get);
        capabilities.add(Volume_Up_Down);
        capabilities.add(Volume_Subscribe);
        capabilities.add(Mute_Get);
        capabilities.add(Mute_Set);
        capabilities.add(Mute_Subscribe);

        setCapabilities(capabilities);
    }

    @Override
    public LaunchSession decodeLaunchSession(String type, JSONObject sessionObj) throws JSONException {
        if (type.equals("dlna")) {
            LaunchSession launchSession = LaunchSession.launchSessionFromJSONObject(sessionObj);
            launchSession.setService(this);

            return launchSession;
        }
        return null;
    }

    private boolean isXmlEncoded(final String xml) {
        if (xml == null || xml.length() < 4) {
            return false;
        }
        return xml.trim().substring(0, 4).equals("&lt;");
    }

    String parseData(String response, String key) {
        if (isXmlEncoded(response)) {
            response = Html.fromHtml(response).toString();
        }
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(new StringReader(response));
            int event;
            boolean isFound = false;
            do {
                event = parser.next();
                if (event == XmlPullParser.START_TAG) {
                    String tag = parser.getName();
                    if (key.equals(tag)) {
                        isFound = true;
                    }
                } else if (event == XmlPullParser.TEXT && isFound) {
                    return parser.getText();
                }
            } while (event != XmlPullParser.END_DOCUMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    long convertStrTimeFormatToLong(String strTime) {
        long time = 0;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        try {
            Date d = df.parse(strTime);
            Date d2 = df.parse("00:00:00");
            time = d.getTime() - d2.getTime();
        } catch (ParseException e) {
            Log.w(Util.T, "Invalid Time Format: " + strTime);
        } catch (NullPointerException e) {
            Log.w(Util.T, "Null time argument");
        }

        return time;
    }

    @Override
    public void getPlayState(final PlayStateListener listener) {
        String method = "GetTransportInfo";
        String instanceId = "0";

        String payload = getMessageXml(AV_TRANSPORT_URN, method, instanceId, null);

        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                String transportState = parseData((String)response, "CurrentTransportState");
                PlayStateStatus status = PlayStateStatus.convertTransportStateToPlayStateStatus(transportState);

                Util.postSuccess(listener, status);
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        };

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, method, payload, responseListener);
        request.send();
    }

    @Override
    public ServiceSubscription<PlayStateListener> subscribePlayState(PlayStateListener listener) {
        URLServiceSubscription<PlayStateListener> request = new URLServiceSubscription<MediaControl.PlayStateListener>(this, PLAY_STATE, null, null);
        request.addListener(listener);
        addSubscription(request);
        return request;
    }

    private void addSubscription(URLServiceSubscription<?> subscription) {
        if (!httpServer.isRunning()) {
            Util.runInBackground(new Runnable() {

                @Override
                public void run() {
                    httpServer.start();
                }
            });
            subscribeServices();
        }

        httpServer.getSubscriptions().add(subscription);
    }

    @Override
    public void unsubscribe(URLServiceSubscription<?> subscription) {
        httpServer.getSubscriptions().remove(subscription);

        if (httpServer.getSubscriptions().isEmpty()) {
            unsubscribeServices();
        }
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
        //  TODO:  Fix this for roku.  Right now it is using the InetAddress reachable function.  Need to use an HTTP Method.
//        mServiceReachability = DeviceServiceReachability.getReachability(serviceDescription.getIpAddress(), this);
//        mServiceReachability.start();

        connected = true;

        reportConnected(true);
    }

    private void getDeviceCapabilities(final PositionInfoListener listener) {
        String method = "GetDeviceCapabilities";
        String instanceId = "0";

        String payload = getMessageXml(AV_TRANSPORT_URN, method, instanceId, null);
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                if (listener != null) {
                    listener.onGetPositionInfoSuccess((String)response);
                }
            }

            @Override
            public void onError(ServiceCommandError error) {
                if (listener != null) {
                    listener.onGetPositionInfoFailed(error);
                }
            }
        };

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, method, payload, responseListener);
        request.send();
    }

    private void getProtocolInfo(final PositionInfoListener listener) {
        String method = "GetProtocolInfo";
        String instanceId = null;

        String payload = getMessageXml(CONNECTION_MANAGER_URN, method, instanceId, null);
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                if (listener != null) {
                    listener.onGetPositionInfoSuccess((String) response);
                }
            }

            @Override
            public void onError(ServiceCommandError error) {
                if (listener != null) {
                    listener.onGetPositionInfoFailed(error);
                }
            }
        };

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, method, payload, responseListener);
        request.send();
    }

    @Override
    public void disconnect() {
        connected = false;

        if (mServiceReachability != null)
            mServiceReachability.stop();

        Util.runOnUI(new Runnable() {

            @Override
            public void run() {
                if (listener != null) {
                    listener.onDisconnect(DLNAService.this, null);
                }
            }
        });

        Util.runInBackground(new Runnable() {
            @Override
            public void run() {
                httpServer.stop();
            }
        }, true);

    }

    @Override
    public void onLoseReachability(DeviceServiceReachability reachability) {
        if (connected) {
            disconnect();
        } else {
            mServiceReachability.stop();
        }
    }

    public void subscribeServices() {
        Util.runInBackground(new Runnable() {

            @Override
            public void run() {
                String myIpAddress = null;
                try {
                    myIpAddress = Util.getIpAddress(context).getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                List<Service> serviceList = serviceDescription.getServiceList();

                if (serviceList != null) {
                    for (int i = 0; i < serviceList.size(); i++) {
                        String eventSubURL = makeControlURL("/", serviceList.get(i).eventSubURL);
                        if (eventSubURL == null) {
                            continue;
                        }

                        try {
                            HttpConnection connection = HttpConnection.newSubscriptionInstance(
                                    new URI("http", "", serviceDescription.getIpAddress(), serviceDescription.getPort(), eventSubURL, "", ""));
                            connection.setMethod(HttpConnection.Method.SUBSCRIBE);
                            connection.setHeader("CALLBACK", "<http://" + myIpAddress + ":" + httpServer.getPort() + eventSubURL + ">");
                            connection.setHeader("NT", "upnp:event");
                            connection.setHeader("TIMEOUT", "Second-" + TIMEOUT);
                            connection.setHeader("Connection", "close");
                            connection.setHeader("Content-length", "0");
                            connection.setHeader("USER-AGENT", "Android UPnp/1.1 ConnectSDK");
                            connection.execute();
                            if (connection.getResponseCode() == 200) {
                                SIDList.put(serviceList.get(i).serviceType, connection.getResponseHeader("SID"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });

        resubscribeServices();
    }

    public void resubscribeServices() {
        resubscriptionTimer = new Timer();
        resubscriptionTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Util.runInBackground(new Runnable() {

                    @Override
                    public void run() {
                        List<Service> serviceList = serviceDescription.getServiceList();

                        if (serviceList != null) {
                            for (int i = 0; i < serviceList.size(); i++) {
                                String eventSubURL = makeControlURL("/", serviceList.get(i).eventSubURL);
                                if (eventSubURL == null) {
                                    continue;
                                }

                                String SID = SIDList.get(serviceList.get(i).serviceType);
                                try {
                                    HttpConnection connection = HttpConnection.newSubscriptionInstance(
                                            new URI("http", "", serviceDescription.getIpAddress(), serviceDescription.getPort(), eventSubURL, "", ""));
                                    connection.setMethod(HttpConnection.Method.SUBSCRIBE);
                                    connection.setHeader("TIMEOUT", "Second-" + TIMEOUT);
                                    connection.setHeader("SID", SID);
                                    connection.execute();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        }, TIMEOUT/2*1000, TIMEOUT/2*1000);
    }

    public void unsubscribeServices() {
        if (resubscriptionTimer != null) {
            resubscriptionTimer.cancel();
        }

        Util.runInBackground(new Runnable() {

            @Override
            public void run() {
                final List<Service> serviceList = serviceDescription.getServiceList();

                if (serviceList != null) {
                    for (int i = 0; i < serviceList.size(); i++) {
                        String eventSubURL = makeControlURL("/", serviceList.get(i).eventSubURL);
                        if (eventSubURL == null) {
                            continue;
                        }

                        String sid = SIDList.get(serviceList.get(i).serviceType);
                        try {
                            HttpConnection connection = HttpConnection.newSubscriptionInstance(
                                    new URI("http", "", serviceDescription.getIpAddress(), serviceDescription.getPort(), eventSubURL, "", ""));
                            connection.setMethod(HttpConnection.Method.UNSUBSCRIBE);
                            connection.setHeader("SID", sid);
                            connection.execute();
                            if (connection.getResponseCode() == 200) {
                                SIDList.remove(serviceList.get(i).serviceType);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    public VolumeControl getVolumeControl() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getVolumeControlCapabilityLevel() {
        return CapabilityPriorityLevel.NORMAL;
    }

    @Override
    public void volumeUp(final ResponseListener<Object> listener) {
        getVolume(new VolumeListener() {

            @Override
            public void onSuccess(final Float volume) {
                if (volume >= 1.0) {
                    Util.postSuccess(listener, null);
                }
                else {
                    float newVolume = (float) (volume + 0.01);

                    if (newVolume > 1.0)
                        newVolume = (float) 1.0;

                    setVolume(newVolume, listener);

                    Util.postSuccess(listener, null);
                }
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        });

    }

    @Override
    public void volumeDown(final ResponseListener<Object> listener) {
        getVolume(new VolumeListener() {

            @Override
            public void onSuccess(final Float volume) {
                if (volume <= 0.0) {
                    Util.postSuccess(listener, null);
                }
                else {
                    float newVolume = (float) (volume - 0.01);

                    if (newVolume < 0.0)
                        newVolume = (float) 0.0;

                    setVolume(newVolume, listener);

                    Util.postSuccess(listener, null);
                }
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        });
    }

    @Override
    public void setVolume(float volume, ResponseListener<Object> listener) {
        String method = "SetVolume";
        String instanceId = "0";
        String channel = "Master";
        String value = String.valueOf((int)(volume*100));

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("Channel", channel);
        params.put("DesiredVolume", value);

        String payload = getMessageXml(RENDERING_CONTROL_URN, method, instanceId, params);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, method, payload, listener);
        request.send();
    }

    @Override
    public void getVolume(final VolumeListener listener) {
        String method = "GetVolume";
        String instanceId = "0";
        String channel = "Master";

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("Channel", channel);

        String payload = getMessageXml(RENDERING_CONTROL_URN, method, instanceId, params);

        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                String currentVolume = parseData((String) response, "CurrentVolume");
                int iVolume = 0;
                try {
                    //noinspection ResultOfMethodCallIgnored
                    Integer.parseInt(currentVolume);
                } catch (RuntimeException ex) {
                    ex.printStackTrace();
                }
                float fVolume = (float) (iVolume / 100.0);

                Util.postSuccess(listener, fVolume);
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        };

        ServiceCommand<VolumeListener> request = new ServiceCommand<VolumeListener>(this, method, payload, responseListener);
        request.send();
    }

    @Override
    public void setMute(boolean isMute, ResponseListener<Object> listener) {
        String method = "SetMute";
        String instanceId = "0";
        String channel = "Master";
        int muteStatus = (isMute) ? 1 : 0;

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("Channel", channel);
        params.put("DesiredMute", String.valueOf(muteStatus));

        String payload = getMessageXml(RENDERING_CONTROL_URN, method, instanceId, params);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, method, payload, listener);
        request.send();
    }

    @Override
    public void getMute(final MuteListener listener) {
        String method = "GetMute";
        String instanceId = "0";
        String channel = "Master";

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("Channel", channel);

        String payload = getMessageXml(RENDERING_CONTROL_URN, method, instanceId, params);

        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                String currentMute = parseData((String) response, "CurrentMute");
                boolean isMute = Boolean.parseBoolean(currentMute);

                Util.postSuccess(listener, isMute);
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        };

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, method, payload, responseListener);
        request.send();
    }

    @Override
    public ServiceSubscription<VolumeListener> subscribeVolume(VolumeListener listener) {
        URLServiceSubscription<VolumeListener> request = new URLServiceSubscription<VolumeListener>(this, "volume", null, null);
        request.addListener(listener);
        addSubscription(request);
        return request;
    }

    @Override
    public ServiceSubscription<MuteListener> subscribeMute(MuteListener listener) {
        URLServiceSubscription<MuteListener> request = new URLServiceSubscription<MuteListener>(this, "mute", null, null);
        request.addListener(listener);
        addSubscription(request);
        return request;
    }

}
