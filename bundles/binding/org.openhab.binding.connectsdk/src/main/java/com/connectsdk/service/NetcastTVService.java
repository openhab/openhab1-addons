/*
 * NetcastTVService
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

import android.graphics.PointF;
import android.util.Log;

import com.connectsdk.core.AppInfo;
import com.connectsdk.core.ChannelInfo;
import com.connectsdk.core.ExternalInputInfo;
import com.connectsdk.core.ImageInfo;
import com.connectsdk.core.MediaInfo;
import com.connectsdk.core.Util;
import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.discovery.DiscoveryFilter;
import com.connectsdk.discovery.DiscoveryManager;
import com.connectsdk.discovery.DiscoveryManager.PairingLevel;
import com.connectsdk.etc.helper.DeviceServiceReachability;
import com.connectsdk.etc.helper.HttpConnection;
import com.connectsdk.etc.helper.HttpMessage;
import com.connectsdk.service.capability.CapabilityMethods;
import com.connectsdk.service.capability.ExternalInputControl;
import com.connectsdk.service.capability.KeyControl;
import com.connectsdk.service.capability.Launcher;
import com.connectsdk.service.capability.MediaControl;
import com.connectsdk.service.capability.MediaPlayer;
import com.connectsdk.service.capability.MouseControl;
import com.connectsdk.service.capability.PowerControl;
import com.connectsdk.service.capability.TVControl;
import com.connectsdk.service.capability.TextInputControl;
import com.connectsdk.service.capability.VolumeControl;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.NotSupportedServiceSubscription;
import com.connectsdk.service.command.ServiceCommand;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceSubscription;
import com.connectsdk.service.command.URLServiceSubscription;
import com.connectsdk.service.config.NetcastTVServiceConfig;
import com.connectsdk.service.config.ServiceConfig;
import com.connectsdk.service.config.ServiceDescription;
import com.connectsdk.service.netcast.NetcastAppNumberParser;
import com.connectsdk.service.netcast.NetcastApplicationsParser;
import com.connectsdk.service.netcast.NetcastChannelParser;
import com.connectsdk.service.netcast.NetcastHttpServer;
import com.connectsdk.service.netcast.NetcastVirtualKeycodes;
import com.connectsdk.service.netcast.NetcastVolumeParser;
import com.connectsdk.service.sessions.LaunchSession;
import com.connectsdk.service.sessions.LaunchSession.LaunchSessionType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class NetcastTVService extends DeviceService implements Launcher, MediaControl, MediaPlayer, TVControl, VolumeControl, ExternalInputControl, MouseControl, TextInputControl, PowerControl, KeyControl {

    public static final String ID = "Netcast TV";

    public static final String UDAP_PATH_PAIRING = "/udap/api/pairing";
    public static final String UDAP_PATH_DATA = "/udap/api/data";
    public static final String UDAP_PATH_COMMAND = "/udap/api/command";
    public static final String UDAP_PATH_EVENT = "/udap/api/event";

    public static final String UDAP_PATH_APPTOAPP_DATA = "/udap/api/apptoapp/data/";
    public static final String UDAP_PATH_APPTOAPP_COMMAND = "/udap/api/apptoapp/command/";
    public static final String ROAP_PATH_APP_STORE = "/roap/api/command/";

    public static final String UDAP_API_PAIRING = "pairing";
    public static final String UDAP_API_COMMAND = "command";
    public static final String UDAP_API_EVENT = "event";

    public final static String TARGET_CHANNEL_LIST = "channel_list";
    public final static String TARGET_CURRENT_CHANNEL = "cur_channel";
    public final static String TARGET_VOLUME_INFO = "volume_info";
    public final static String TARGET_APPLIST_GET = "applist_get";
    public final static String TARGET_APPNUM_GET = "appnum_get";
    public final static String TARGET_3D_MODE = "3DMode";
    public final static String TARGET_IS_3D = "is_3D";
    public static final String SMART_SHARE = "SmartShare™";

    enum State {
        NONE,
        INITIAL,
        CONNECTING,
        PAIRING,
        PAIRED,
        DISCONNECTING
    }

    NetcastHttpServer httpServer;

    DLNAService dlnaService;
    DIALService dialService;

    LaunchSession inputPickerSession;

    List<AppInfo> applications;
    List<URLServiceSubscription<?>> subscriptions;
    StringBuilder keyboardString;

    State state = State.INITIAL;

    PointF mMouseDistance;
    Boolean mMouseIsMoving;

    public NetcastTVService(ServiceDescription serviceDescription, ServiceConfig serviceConfig) {
        super(serviceDescription, serviceConfig);

        pairingType = PairingType.PIN_CODE;

        if (serviceDescription.getPort() != 8080)
            serviceDescription.setPort(8080);

        applications = new ArrayList<AppInfo>();
        subscriptions = new ArrayList<URLServiceSubscription<?>>();

        keyboardString = new StringBuilder();

        state = State.INITIAL;

        inputPickerSession = null;
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
        else if (clazz.equals(Launcher.class)) {
            return getLauncherCapabilityLevel();
        }
        else if (clazz.equals(TVControl.class)) {
            return getTVControlCapabilityLevel();
        }
        else if (clazz.equals(VolumeControl.class)) {
            return getVolumeControlCapabilityLevel();
        }
        else if (clazz.equals(ExternalInputControl.class)) {
            return getExternalInputControlPriorityLevel();
        }
        else if (clazz.equals(MouseControl.class)) {
            return getMouseControlCapabilityLevel();
        }
        else if (clazz.equals(TextInputControl.class)) {
            return getTextInputControlCapabilityLevel();
        }
        else if (clazz.equals(PowerControl.class)) {
            return getPowerControlCapabilityLevel();
        }
        else if (clazz.equals(KeyControl.class)) {
            return getKeyControlCapabilityLevel();
        }
        return CapabilityPriorityLevel.NOT_SUPPORTED;
    }

    @Override
    public void setServiceDescription(ServiceDescription serviceDescription) {
        super.setServiceDescription(serviceDescription);

        if (serviceDescription.getPort() != 8080)
            serviceDescription.setPort(8080);
    }

    @Override
    public void connect() {
        if (state != State.INITIAL) {
            Log.w(Util.T, "already connecting; not trying to connect again: " + state);
            return; // don't try to connect again while connected
        }

        if (!(serviceConfig instanceof NetcastTVServiceConfig)) {
            ServiceConfig.ServiceConfigListener serviceConfigListener = serviceConfig.getListener();
            serviceConfig = new NetcastTVServiceConfig(serviceConfig.getServiceUUID());
            serviceConfig.setListener(serviceConfigListener);
        }

        if (DiscoveryManager.getInstance().getPairingLevel() == PairingLevel.ON) {
            if (((NetcastTVServiceConfig) serviceConfig).getPairingKey() != null 
                    && ((NetcastTVServiceConfig)serviceConfig).getPairingKey().length() != 0) {

                sendPairingKey(((NetcastTVServiceConfig) serviceConfig).getPairingKey());
            }
            else {
                showPairingKeyOnTV();
            }

            Util.runInBackground(new Runnable() {

                @Override
                public void run() {
                    httpServer = new NetcastHttpServer(NetcastTVService.this, getServiceDescription().getPort(), mTextChangedListener);
                    httpServer.setSubscriptions(subscriptions);
                    httpServer.start();
                }
            });
        } else {
            hConnectSuccess();
        }
    }

    @Override
    public void disconnect() {
        endPairing(null);

        connected = false;

        if (mServiceReachability != null)
            mServiceReachability.stop();

        Util.runOnUI(new Runnable() {

            @Override
            public void run() {
                if (listener != null)
                    listener.onDisconnect(NetcastTVService.this, null);
            }
        });

        if (httpServer != null) {
            httpServer.stop();
            httpServer = null;
        }

        state = State.INITIAL;
    }

    @Override
    public boolean isConnectable() {
        return true;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    private void hConnectSuccess() {
        //  TODO:  Fix this for Netcast.  Right now it is using the InetAddress reachable function.  Need to use an HTTP Method.
//        mServiceReachability = DeviceServiceReachability.getReachability(serviceDescription.getIpAddress(), this);
//        mServiceReachability.start();

        connected = true;

        // Pairing was successful, so report connected and ready
        reportConnected(true);
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

    public void hostByeBye () {
        disconnect();
    }

    //============= Auth ==============================
    public void showPairingKeyOnTV() {
        state = State.CONNECTING;

        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                if (listener != null)
                    listener.onPairingRequired(NetcastTVService.this, pairingType, null);
            }

            @Override
            public void onError(ServiceCommandError error) {
                state = State.INITIAL;

                if (listener != null)
                    listener.onConnectionFailure(NetcastTVService.this, error);
            }
        };

        String requestURL = getUDAPRequestURL(UDAP_PATH_PAIRING);

        Map <String,String> params = new HashMap<String,String>();
        params.put("name", "showKey");

        String httpMessage = getUDAPMessageBody(UDAP_API_PAIRING, params);

        ServiceCommand<ResponseListener<Object>> command = new ServiceCommand<ResponseListener<Object>>(this, requestURL, httpMessage, responseListener);
        command.send();
    }

    @Override
    public void cancelPairing() {
        removePairingKeyOnTV();
        state = State.INITIAL;
    }

    public void removePairingKeyOnTV() {
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
            }

            @Override
            public void onError(ServiceCommandError error) {
            }
        };

        String requestURL = getUDAPRequestURL(UDAP_PATH_PAIRING);

        Map <String,String> params = new HashMap<String,String>();
        params.put("name", "CancelAuthKeyReq");

        String httpMessage = getUDAPMessageBody(UDAP_API_PAIRING, params);

        ServiceCommand<ResponseListener<Object>> command = new ServiceCommand<ResponseListener<Object>>(this, requestURL, httpMessage, responseListener);
        command.send();
    }

    @Override
    public void sendPairingKey(final String pairingKey) {
        state = State.PAIRING;

        if (!(serviceConfig instanceof NetcastTVServiceConfig)) {
            serviceConfig = new NetcastTVServiceConfig(serviceConfig.getServiceUUID());
        }

        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                state = State.PAIRED;

                ((NetcastTVServiceConfig)serviceConfig).setPairingKey(pairingKey);

                hConnectSuccess();
            }

            @Override
            public void onError(ServiceCommandError error) {
                state = State.INITIAL;

                if (listener != null)
                    listener.onConnectionFailure(NetcastTVService.this, error);
            }
        };

        String requestURL = getUDAPRequestURL(UDAP_PATH_PAIRING);

        Map <String,String> params = new HashMap<String,String>();
        params.put("name", "hello");
        params.put("value", pairingKey);
        params.put("port", String.valueOf(serviceDescription.getPort()));

        String httpMessage = getUDAPMessageBody(UDAP_API_PAIRING, params);

        ServiceCommand<ResponseListener<Object>> command = new ServiceCommand<ResponseListener<Object>>(this, requestURL, httpMessage, responseListener);
        command.send();
    }

    private void endPairing(ResponseListener<Object> listener) {
        String requestURL = getUDAPRequestURL(UDAP_PATH_PAIRING);

        Map <String,String> params = new HashMap<String,String>();
        params.put("name", "byebye");
        params.put("port", String.valueOf(serviceDescription.getPort()));

        String httpMessage = getUDAPMessageBody(UDAP_API_PAIRING, params);

        ServiceCommand<ResponseListener<Object>> command = new ServiceCommand<ResponseListener<Object>>(this, requestURL, httpMessage, listener);
        command.send();
    }


    /******************
    LAUNCHER
     *****************/
    public Launcher getLauncher() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getLauncherCapabilityLevel() {
        return CapabilityPriorityLevel.HIGH;
    }

    class NetcastTVLaunchSessionR extends LaunchSession {
        String appName;
        NetcastTVService service;

        NetcastTVLaunchSessionR (NetcastTVService service, String auid, String appName) {
            this.service = service;
            appId = auid;
        }

        NetcastTVLaunchSessionR (NetcastTVService service, JSONObject obj) throws JSONException {
            this.service = service;
            fromJSONObject(obj);
        }

        public void close(ResponseListener<Object> responseListener) {
        }

        @Override
        public JSONObject toJSONObject() throws JSONException {
            JSONObject obj = super.toJSONObject();
            obj.put("type", "netcasttv");
            obj.put("appName", appName);
            return obj;
        }

        @Override
        public void fromJSONObject(JSONObject obj) throws JSONException {
            super.fromJSONObject(obj);
            appName = obj.optString("appName");
        }
    }

    public void getApplication(final String appName, final AppInfoListener listener) {
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                final String strObj = (String) response;

                AppInfo appId = new AppInfo() {{
                    setId(decToHex(strObj));
                }};

                Util.postSuccess(listener, appId);
            }

            @Override
            public void onError(ServiceCommandError error) {
                if (listener != null) 
                    Util.postError(listener, error);
            }
        };

        String uri = UDAP_PATH_APPTOAPP_DATA + appName;
        String requestURL = getUDAPRequestURL(uri);

        ServiceCommand<ResponseListener<Object>> command = new ServiceCommand<ResponseListener<Object>>(this, requestURL, null, responseListener);
        command.setHttpMethod(ServiceCommand.TYPE_GET);
        command.send();
    }

    @Override
    public void launchApp(final String appId, final AppLaunchListener listener) {
        getAppInfoForId(appId, new AppInfoListener() {

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }

            @Override
            public void onSuccess(AppInfo appInfo) {
                launchAppWithInfo(appInfo, listener);
            }
        });
    }

    private void getAppInfoForId(final String appId, final AppInfoListener listener) {
        getAppList(new AppListListener() {

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }

            @Override
            public void onSuccess(List<AppInfo> object) {
                for (AppInfo info : object) {
                    if (info.getName().equalsIgnoreCase(appId)) {
                        Util.postSuccess(listener, info);
                        return;
                    }
                }
                Util.postError(listener, new ServiceCommandError(0, "Unable to find the App with id", null));
            }
        });
    }

    private void launchApplication(final String appName, final String auid, final String contentId, final Launcher.AppLaunchListener listener) {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("id", auid);
            jsonObj.put("title", appName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                LaunchSession launchSession = LaunchSession.launchSessionForAppId(auid);
                launchSession.setAppName(appName);
                launchSession.setService(NetcastTVService.this);
                launchSession.setSessionType(LaunchSessionType.App);

                Util.postSuccess(listener, launchSession);
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        };

        String requestURL = getUDAPRequestURL(UDAP_PATH_APPTOAPP_COMMAND);

        Map <String,String> params = new HashMap<String,String>();
        params.put("name", "AppExecute");
        params.put("auid", auid);
        if (appName != null) {
            params.put("appname", appName);
        }
        if (contentId != null) {
            params.put("contentid", contentId);
        }

        String httpMessage = getUDAPMessageBody(UDAP_API_COMMAND, params);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, requestURL, httpMessage, responseListener);
        request.send();
    }

    @Override
    public void launchAppWithInfo(AppInfo appInfo, Launcher.AppLaunchListener listener) {
        launchAppWithInfo(appInfo, null, listener);
    }

    @Override
    public void launchAppWithInfo(AppInfo appInfo, Object params, Launcher.AppLaunchListener listener) {
        String appName = HttpMessage.encode(appInfo.getName());
        String appId = appInfo.getId();
        String contentId = null;
        JSONObject mParams = null;
        if (params instanceof JSONObject)
            mParams = (JSONObject) params;

        if (mParams != null) {
            try {
                contentId = (String) mParams.get("contentId");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        launchApplication(appName, appId, contentId, listener);
    }

    @Override
    public void launchBrowser(String url, final Launcher.AppLaunchListener listener) {
        if (!(url == null || url.length() == 0)) 
            Log.w(Util.T, "Netcast TV does not support deeplink for Browser");

        final String appName = "Internet";

        getApplication(appName, new AppInfoListener() {

            @Override
            public void onSuccess(AppInfo appInfo) {
                String contentId = null;
                launchApplication(appName, appInfo.getId(), contentId, listener);
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        });
    }

    @Override
    public void launchYouTube(String contentId, Launcher.AppLaunchListener listener) {
        launchYouTube(contentId, (float)0.0, listener);
    }

    @Override
    public void launchYouTube(final String contentId, float startTime, final AppLaunchListener listener) {
        if (getDIALService() != null) {
            getDIALService().getLauncher().launchYouTube(contentId, startTime, listener);
            return;
        }

        if (startTime <= 0.0) {
            getApplication("YouTube", new AppInfoListener() {

                @Override
                public void onSuccess(AppInfo appInfo) {
                    launchApplication(appInfo.getName(), appInfo.getId(), contentId, listener);
                }

                @Override
                public void onError(ServiceCommandError error) {
                    Util.postError(listener, error);
                }
            });
        }
        else {
            Util.postError(listener, new ServiceCommandError(0, "Cannot reach DIAL service for launching with provided start time", null));
        }
    }

    @Override
    public void launchHulu(final String contentId, final Launcher.AppLaunchListener listener) {
        final String appName = "Hulu";

        getApplication(appName, new AppInfoListener() {

            @Override
            public void onSuccess(AppInfo appInfo) {
                launchApplication(appName, appInfo.getId(), contentId, listener);
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        });
    }

    @Override
    public void launchNetflix(final String contentId, final Launcher.AppLaunchListener listener) {
        if (!serviceDescription.getModelNumber().equals("4.0")) {
            launchApp("Netflix", listener);
            return;
        }

        final String appName = "Netflix";

        getApplication(appName, new AppInfoListener() {

            @Override
            public void onSuccess(final AppInfo appInfo) {
                JSONObject jsonObj = new JSONObject();

                try {
                    jsonObj.put("id", appInfo.getId());
                    jsonObj.put("name", appName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ResponseListener<Object> responseListener = new ResponseListener<Object>() {

                    @Override
                    public void onSuccess(Object response) {
                        LaunchSession launchSession = LaunchSession.launchSessionForAppId(appInfo.getId());
                        launchSession.setAppName(appName);
                        launchSession.setService(NetcastTVService.this);
                        launchSession.setSessionType(LaunchSessionType.App);

                        Util.postSuccess(listener, launchSession);
                    }

                    @Override
                    public void onError(ServiceCommandError error) {
                        Util.postError(listener, error);
                    }
                };

                String requestURL = getUDAPRequestURL(UDAP_PATH_APPTOAPP_COMMAND);

                Map <String,String> params = new HashMap<String,String>();
                params.put("name", "SearchCMDPlaySDPContent");
                params.put("content_type", "1");
                params.put("conts_exec_type", "20");
                params.put("conts_plex_type_flag", "N");
                params.put("conts_search_id", "2023237");
                params.put("conts_age", "18");
                params.put("exec_id", "netflix");
                params.put("item_id", "-Q m=http%3A%2F%2Fapi.netflix.com%2Fcatalog%2Ftitles%2Fmovies%2F" + contentId + "&amp;source_type=4&amp;trackId=6054700&amp;trackUrl=https%3A%2F%2Fapi.netflix.com%2FAPI_APP_ID_6261%3F%23Search%3F");
                params.put("app_type", "");

                String httpMessage = getUDAPMessageBody(UDAP_API_COMMAND, params);

                ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(NetcastTVService.this, requestURL, httpMessage, responseListener);
                request.send();
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        });
    }

    @Override
    public void launchAppStore(final String appId, final AppLaunchListener listener) {
        if (!serviceDescription.getModelNumber().equals("4.0")) {
            launchApp("LG Smart World", listener);  // TODO: this will not work in Korea, use Korean name instead
            return;
        }

        String targetPath = getUDAPRequestURL(ROAP_PATH_APP_STORE);

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", "SearchCMDPlaySDPContent");
        params.put("content_type", "4");
        params.put("conts_exec_type", "");
        params.put("conts_plex_type_flag", "");
        params.put("conts_search_id", "");
        params.put("conts_age", "12");
        params.put("exec_id", "");
        params.put("item_id", HttpMessage.encode(appId));
        params.put("app_type", "S");

        String httpMessage = getUDAPMessageBody(UDAP_API_COMMAND, params);

        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                LaunchSession launchSession = LaunchSession.launchSessionForAppId(appId);
                launchSession.setAppName("LG Smart World"); // TODO: this will not work in Korea, use Korean name instead
                launchSession.setService(NetcastTVService.this);
                launchSession.setSessionType(LaunchSessionType.App);

                Util.postSuccess(listener, launchSession);
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        };
        ServiceCommand<ResponseListener<Object>> command = new ServiceCommand<ResponseListener<Object>>(this, targetPath, httpMessage, responseListener);
        command.send();
    }

    @Override
    public void closeApp(LaunchSession launchSession, ResponseListener<Object> listener) {
        String requestURL = getUDAPRequestURL(UDAP_PATH_APPTOAPP_COMMAND);

        Map <String,String> params = new HashMap<String,String>();
        params.put("name", "AppTerminate");
        params.put("auid", launchSession.getAppId());
        if (launchSession.getAppName() != null) 
            params.put("appname", HttpMessage.encode(launchSession.getAppName()));

        String httpMessage = getUDAPMessageBody(UDAP_API_COMMAND, params);

        ServiceCommand<ResponseListener<Object>> command = new ServiceCommand<ResponseListener<Object>>(launchSession.getService(), requestURL, httpMessage, listener);
        command.send();
    }

    private void getTotalNumberOfApplications(int type, final AppCountListener listener) {
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                String strObj = (String) response;

                int applicationNumber = parseAppNumberXmlToJSON(strObj);

                Util.postSuccess(listener, applicationNumber);
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        };

        String requestURL = getUDAPRequestURL(UDAP_PATH_DATA, TARGET_APPNUM_GET, String.valueOf(type));

        ServiceCommand<ResponseListener<Object>> command = new ServiceCommand<ResponseListener<Object>>(this, requestURL, null, responseListener);
        command.setHttpMethod(ServiceCommand.TYPE_GET);
        command.send();
    }

    private void getApplications(int type, int number, final AppListListener listener) {
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                String strObj = (String) response;

                JSONArray applicationArray = parseApplicationsXmlToJSON(strObj);
                List<AppInfo> appList = new ArrayList<AppInfo>();

                for (int i = 0; i < applicationArray.length(); i++)
                {
                    try {
                        final JSONObject appJSON = applicationArray.getJSONObject(i);

                        AppInfo appInfo = new AppInfo() {{
                            setId(appJSON.getString("id"));
                            setName(appJSON.getString("title"));
                        }};

                        appList.add(appInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Util.postSuccess(listener, appList);
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        };

        String requestURL = getUDAPRequestURL(UDAP_PATH_DATA, TARGET_APPLIST_GET, String.valueOf(type), "0", String.valueOf(number));

        ServiceCommand<ResponseListener<Object>> command = new ServiceCommand<ResponseListener<Object>>(this, requestURL, null, responseListener);
        command.setHttpMethod(ServiceCommand.TYPE_GET);
        command.send();
    }

    @Override
    public void getAppList(final AppListListener listener) {
        applications.clear();
        getTotalNumberOfApplications(2, new AppCountListener() {

            @Override
            public void onSuccess(final Integer count) {
                getApplications(2, count, new AppListListener() {

                    @Override
                    public void onSuccess(List<AppInfo> apps) {
                        applications.addAll(apps);

                        getTotalNumberOfApplications(3, new AppCountListener() {

                            @Override
                            public void onSuccess(final Integer count) {
                                getApplications(3, count, new AppListListener() {

                                    @Override
                                    public void onSuccess(List<AppInfo> apps) {
                                        applications.addAll(apps);

                                        Util.postSuccess(listener, applications);
                                    }

                                    @Override
                                    public void onError(ServiceCommandError error) {
                                        Util.postError(listener, error);
                                    }
                                });
                            }

                            @Override
                            public void onError(ServiceCommandError error) {
                                Util.postError(listener, error);
                            }
                        });
                    }

                    @Override
                    public void onError(ServiceCommandError error) {
                        Util.postError(listener, error);
                    }
                });
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        });
    }

    @Override
    public void getRunningApp(AppInfoListener listener) {
        // Do nothing - Not Supported
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public ServiceSubscription<AppInfoListener> subscribeRunningApp(AppInfoListener listener) {
        // Do nothing - Not Supported
        Util.postError(listener, ServiceCommandError.notSupported());

        return new NotSupportedServiceSubscription<AppInfoListener>();
    }

    @Override
    public void getAppState(final LaunchSession launchSession, final AppStateListener listener) {
        String requestURL = String.format(Locale.US, "%s%s", 
                getUDAPRequestURL(UDAP_PATH_APPTOAPP_DATA), 
                String.format(Locale.US, "/%s/status", launchSession.getAppId()));

        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }

            @Override
            public void onSuccess(Object object) {
                String response = (String) object;
                AppState appState;
                if (response.equalsIgnoreCase("NONE"))
                    appState = new AppState(false, false);
                else if (response.equalsIgnoreCase("LOAD"))
                    appState = new AppState(false, true);
                else if (response.equalsIgnoreCase("RUN_NF"))
                    appState = new AppState(true, false);
                else if (response.equalsIgnoreCase("TERM"))
                    appState = new AppState(false, true);
                else
                    appState = new AppState(false, false);

                Util.postSuccess(listener, appState);
            }
        };

        ServiceCommand<ResponseListener<Object>> command = new ServiceCommand<ResponseListener<Object>>(this, requestURL, null, responseListener);
        command.setHttpMethod(ServiceCommand.TYPE_GET);
        command.send();
    }

    @Override
    public ServiceSubscription<AppStateListener> subscribeAppState(LaunchSession launchSession, AppStateListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());

        return null;
    }


    /******************
    TV CONTROL
     *****************/
    @Override
    public TVControl getTVControl() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getTVControlCapabilityLevel() {
        return CapabilityPriorityLevel.HIGH;
    }

    @Override
    public void getChannelList(final ChannelListListener listener) {
        String requestURL = getUDAPRequestURL(UDAP_PATH_DATA, TARGET_CHANNEL_LIST);

        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                String strObj = (String)response;

                try {
                    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                    InputStream stream = new ByteArrayInputStream(strObj.getBytes("UTF-8"));
                    SAXParser saxParser = saxParserFactory.newSAXParser();

                    NetcastChannelParser parser = new NetcastChannelParser();
                    saxParser.parse(stream, parser);

                    JSONArray channelArray = parser.getJSONChannelArray();
                    ArrayList<ChannelInfo> channelList = new ArrayList<ChannelInfo>();

                    for (int i = 0; i < channelArray.length(); i++) {
                        JSONObject rawData;
                        try {
                            rawData = (JSONObject) channelArray.get(i);

                            ChannelInfo channel = NetcastChannelParser.parseRawChannelData(rawData);
                            channelList.add(channel);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Util.postSuccess(listener, channelList);
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

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, requestURL, null, responseListener);
        request.setHttpMethod(ServiceCommand.TYPE_GET);
        request.send();
    }

    @Override
    public void channelUp(ResponseListener<Object> listener) {
        sendVirtualKeyCode(NetcastVirtualKeycodes.CHANNEL_UP.getCode(), listener);
    }

    @Override
    public void channelDown(ResponseListener<Object> listener) {
        sendVirtualKeyCode(NetcastVirtualKeycodes.CHANNEL_DOWN.getCode(), listener);
    }

    @Override
    public void setChannel(final ChannelInfo channelInfo, final ResponseListener<Object> listener) {
        getChannelList(new ChannelListListener() {

            @Override
            public void onSuccess(List<ChannelInfo> channelList) {
                String requestURL = getUDAPRequestURL(UDAP_PATH_COMMAND);

                Map<String, String> params = new HashMap<String, String>();

                for (int i = 0; i < channelList.size(); i++) {
                    ChannelInfo ch = channelList.get(i);
                    JSONObject rawData = ch.getRawData();

                    try {
                        String major = channelInfo.getNumber().split("-")[0];
                        String minor = channelInfo.getNumber().split("-")[1];

                        int majorNumber = ch.getMajorNumber();
                        int minorNumber = ch.getMinorNumber();

                        String sourceIndex = (String) rawData.get("sourceIndex");
                        int physicalNum = (Integer) rawData.get("physicalNumber");

                        if (Integer.valueOf(major) == majorNumber
                                && Integer.valueOf(minor) == minorNumber) {
                            params.put("name", "HandleChannelChange");
                            params.put("major", major);
                            params.put("minor", minor);
                            params.put("sourceIndex", sourceIndex);
                            params.put("physicalNum", String.valueOf(physicalNum));

                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                String httpMessage = getUDAPMessageBody(UDAP_API_COMMAND, params);

                ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(NetcastTVService.this, requestURL, httpMessage, listener);
                request.send();
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        });
    }

    @Override
    public void getCurrentChannel(final ChannelListener listener) {
        String requestURL = getUDAPRequestURL(UDAP_PATH_DATA, TARGET_CURRENT_CHANNEL);

        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                String strObj = (String)response;

                try {
                    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                    InputStream stream = new ByteArrayInputStream(strObj.getBytes("UTF-8"));
                    SAXParser saxParser = saxParserFactory.newSAXParser();

                    NetcastChannelParser parser = new NetcastChannelParser();
                    saxParser.parse(stream, parser);

                    JSONArray channelArray = parser.getJSONChannelArray();

                    if (channelArray.length() > 0) {
                        JSONObject rawData = (JSONObject) channelArray.get(0);

                        ChannelInfo channel = NetcastChannelParser.parseRawChannelData(rawData);

                        Util.postSuccess(listener, channel);
                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        };

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, requestURL, null, responseListener);
        request.send();
    }

    @Override
    public ServiceSubscription<ChannelListener> subscribeCurrentChannel(final ChannelListener listener) {
        getCurrentChannel(listener);    // This is for the initial Current TV Channel Info.

        URLServiceSubscription<ChannelListener> request = new URLServiceSubscription<ChannelListener>(this, "ChannelChanged", null, null);
        request.setHttpMethod(ServiceCommand.TYPE_GET);
        request.addListener(listener);
        addSubscription(request);

        return request;
    }

    @Override
    public void getProgramInfo(ProgramInfoListener listener) {
        // Do nothing - Not Supported
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public ServiceSubscription<ProgramInfoListener> subscribeProgramInfo(ProgramInfoListener listener) {
        // Do nothing - Not Supported
        Util.postError(listener, ServiceCommandError.notSupported());

        return null;
    }

    @Override
    public void getProgramList(ProgramListListener listener) {
        // Do nothing - Not Supported
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public ServiceSubscription<ProgramListListener> subscribeProgramList(ProgramListListener listener) {
        // Do nothing - Not Supported
        Util.postError(listener, ServiceCommandError.notSupported());

        return null;
    }

    @Override
    public void set3DEnabled(final boolean enabled, final ResponseListener<Object> listener) {
        get3DEnabled(new State3DModeListener() {

            @Override
            public void onSuccess(Boolean isEnabled) {
                if (enabled != isEnabled) {
                    sendVirtualKeyCode(NetcastVirtualKeycodes.VIDEO_3D.getCode(), listener);
                }
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        });
    }

    @Override
    public void get3DEnabled(final State3DModeListener listener) {
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                String strObj = (String) response;
                String upperStr = strObj.toUpperCase(Locale.ENGLISH);

                Util.postSuccess(listener, upperStr.contains("TRUE"));
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        };

        String requestURL = getUDAPRequestURL(UDAP_PATH_DATA, TARGET_IS_3D);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, requestURL, null, responseListener);
        request.setHttpMethod(ServiceCommand.TYPE_GET);
        request.send();
    }

    @Override
    public ServiceSubscription<State3DModeListener> subscribe3DEnabled(final State3DModeListener listener) {
        get3DEnabled(listener);

        URLServiceSubscription<State3DModeListener> request = new URLServiceSubscription<State3DModeListener>(this, TARGET_3D_MODE, null, null);
        request.setHttpMethod(ServiceCommand.TYPE_GET);
        request.addListener(listener);

        addSubscription(request);

        return request;
    }



    /**************
    VOLUME
     **************/
    @Override
    public VolumeControl getVolumeControl() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getVolumeControlCapabilityLevel() {
        return CapabilityPriorityLevel.HIGH;
    }

    @Override
    public void volumeUp(ResponseListener<Object> listener) {
        sendVirtualKeyCode(NetcastVirtualKeycodes.VOLUME_UP.getCode(), listener);
    }

    @Override
    public void volumeDown(ResponseListener<Object> listener) {
        sendVirtualKeyCode(NetcastVirtualKeycodes.VOLUME_DOWN.getCode(), listener);
    }

    @Override
    public void setVolume(float volume, ResponseListener<Object> listener) {
        if (getDLNAService() != null) {
            getDLNAService().setVolume(volume, listener);
        }
        else 
            Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void getVolume(final VolumeListener listener) {

        getVolumeStatus(new VolumeStatusListener() {

            @Override
            public void onSuccess(VolumeStatus status) {
                Util.postSuccess(listener, status.volume);
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        });

    }

    @Override
    public void setMute(final boolean isMute, final ResponseListener<Object> listener) {

        getVolumeStatus(new VolumeStatusListener() {

            @Override
            public void onSuccess(VolumeStatus status) {
                if (isMute != status.isMute) {
                    sendVirtualKeyCode(NetcastVirtualKeycodes.MUTE.getCode(), listener);
                }
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        });

    }

    @Override
    public void getMute(final MuteListener listener) {

        getVolumeStatus(new VolumeStatusListener() {

            @Override
            public void onSuccess(VolumeStatus status) {
                Util.postSuccess(listener, status.isMute);
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        });

    }

    @Override
    public ServiceSubscription<VolumeListener> subscribeVolume(VolumeListener listener) {
        if (getDLNAService() != null) {
            return getDLNAService().subscribeVolume(listener);
        }
        else 
            Util.postError(listener, ServiceCommandError.notSupported());
        return null;

    }

    @Override
    public ServiceSubscription<MuteListener> subscribeMute(MuteListener listener) {
        if (getDLNAService() != null) {
            return getDLNAService().subscribeMute(listener);
        }
        else 
            Util.postError(listener, ServiceCommandError.notSupported());
        return null;

    }

    private void getVolumeStatus(final VolumeStatusListener listener) {
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                String strObj = (String) response;
                JSONObject volumeStatus = parseVolumeXmlToJSON(strObj);
                try {
                    boolean isMute = (Boolean) volumeStatus.get("mute");
                    int volume = (Integer) volumeStatus.get("level");

                    Util.postSuccess(listener, new VolumeStatus(isMute, volume));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        };

        String requestURL = getUDAPRequestURL(UDAP_PATH_DATA, TARGET_VOLUME_INFO);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, requestURL, null, responseListener);
        request.setHttpMethod(ServiceCommand.TYPE_GET);
        request.send();
    }

    /**************
    EXTERNAL INPUT
     **************/
    @Override
    public ExternalInputControl getExternalInput() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getExternalInputControlPriorityLevel() {
        return CapabilityPriorityLevel.HIGH;
    }

    @Override
    public void launchInputPicker(final AppLaunchListener listener) {
        final String appName = "Input List";
        final String encodedStr = HttpMessage.encode(appName);

        getApplication(encodedStr, new AppInfoListener() {

            @Override
            public void onSuccess(final AppInfo appInfo) {
                Launcher.AppLaunchListener launchListener = new Launcher.AppLaunchListener() {

                    @Override
                    public void onSuccess(LaunchSession session) {
                        if (inputPickerSession == null) {
                            inputPickerSession = session;
                        }

                        Util.postSuccess(listener, session);
                    }

                    @Override
                    public void onError(ServiceCommandError error) {
                        Util.postError(listener, error);
                    }
                };

                launchApplication(appName, appInfo.getId(), null, launchListener);
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        });
    }

    @Override
    public void closeInputPicker(LaunchSession launchSession, ResponseListener<Object> listener) {
        sendVirtualKeyCode(NetcastVirtualKeycodes.EXIT.getCode(), listener);
    }

    @Override
    public void getExternalInputList(ExternalInputListListener listener) {
        // Do nothing - not Supported
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void setExternalInput(ExternalInputInfo input, ResponseListener<Object> listener) {
        // Do nothing - not Supported
        Util.postError(listener, ServiceCommandError.notSupported());
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
        return CapabilityPriorityLevel.HIGH;
    }

    @Override
    public void getMediaInfo(final MediaInfoListener listener) {

        if (getDLNAService() != null) {
            getDLNAService().getMediaInfo(listener);
        }
        else {
            if (listener != null)
                Util.postError(listener, ServiceCommandError.notSupported());
        }
    }

    @Override
    public ServiceSubscription<MediaInfoListener> subscribeMediaInfo(
            MediaInfoListener listener) {
        if (getDLNAService() != null) {
            return getDLNAService().subscribeMediaInfo(listener);
        }
        else {
            if (listener != null)
                Util.postError(listener, ServiceCommandError.notSupported());
            return null;
        }
    }

    @Override
    public void displayImage(final String url, final String mimeType, final String title, final String description, final String iconSrc, final MediaPlayer.LaunchListener listener) {
        if (getDLNAService() != null) {
            final MediaPlayer.LaunchListener launchListener = new LaunchListener() {

                @Override
                public void onError(ServiceCommandError error) {
                    if (listener != null)
                        Util.postError(listener, error);
                }

                @Override
                public void onSuccess(MediaLaunchObject object) {
                    object.launchSession.setAppId(SMART_SHARE);
                    object.launchSession.setAppName(SMART_SHARE);

                    object.mediaControl = NetcastTVService.this.getMediaControl();

                    if (listener != null)
                        Util.postSuccess(listener, object);
                }
            }; 

            getDLNAService().displayImage(url, mimeType, title, description, iconSrc, launchListener);
        }
        else {
            System.err.println("DLNA Service is not ready yet");
        }
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
    public void playMedia(String url, String mimeType, String title, String description, String iconSrc, boolean shouldLoop, MediaPlayer.LaunchListener listener) {
        MediaInfo mediaInfo = new MediaInfo.Builder(url, mimeType)
                .setTitle(title)
                .setDescription(description)
                .setIcon(iconSrc)
                .build();
        playMedia(mediaInfo, shouldLoop, listener);
    }

    @Override
    public void playMedia(MediaInfo mediaInfo, boolean shouldLoop, final MediaPlayer.LaunchListener listener) {
        if (getDLNAService() != null) {
            final MediaPlayer.LaunchListener launchListener = new LaunchListener() {

                @Override
                public void onError(ServiceCommandError error) {
                    if (listener != null)
                        Util.postError(listener, error);
                }

                @Override
                public void onSuccess(MediaLaunchObject object) {
                    object.launchSession.setAppId(SMART_SHARE);
                    object.launchSession.setAppName(SMART_SHARE);

                    object.mediaControl = NetcastTVService.this.getMediaControl();

                    if (listener != null)
                        Util.postSuccess(listener, object);
                }
            };

            getDLNAService().playMedia(mediaInfo, shouldLoop, launchListener);
        }
        else {
            System.err.println("DLNA Service is not ready yet");
        }
    }

    @Override
    public void closeMedia(LaunchSession launchSession, ResponseListener<Object> listener) {
        if (getDLNAService() == null) {
            Util.postError(listener, new ServiceCommandError(0, "Service is not connected", null));
            return;
        }

        getDLNAService().closeMedia(launchSession, listener);
    }

    /******************
    MEDIA CONTROL
     *****************/
    @Override
    public MediaControl getMediaControl() {
        if (DiscoveryManager.getInstance().getPairingLevel() == PairingLevel.OFF)
            return this.getDLNAService();
        else
            return this;
    }

    @Override
    public CapabilityPriorityLevel getMediaControlCapabilityLevel() {
        return CapabilityPriorityLevel.HIGH;
    }

    @Override
    public void play(ResponseListener<Object> listener) {
        sendVirtualKeyCode(NetcastVirtualKeycodes.PLAY.getCode(), listener);
    }

    @Override
    public void pause(ResponseListener<Object> listener) {
        sendVirtualKeyCode(NetcastVirtualKeycodes.PAUSE.getCode(), listener);
    }

    @Override
    public void stop(final ResponseListener<Object> listener) {
        sendVirtualKeyCode(NetcastVirtualKeycodes.STOP.getCode(), listener);
    }

    @Override
    public void rewind(ResponseListener<Object> listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void fastForward(ResponseListener<Object> listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void previous(ResponseListener<Object> listener) {
        sendVirtualKeyCode(NetcastVirtualKeycodes.SKIP_BACKWARD.getCode(), listener);
    }

    @Override
    public void next(ResponseListener<Object> listener) {
        sendVirtualKeyCode(NetcastVirtualKeycodes.SKIP_FORWARD.getCode(), listener);
    }

    @Override
    public void seek(long position, ResponseListener<Object> listener) {
        if (getDLNAService() != null) {
            getDLNAService().seek(position, listener);
        } else {
            if (listener != null)
                Util.postError(listener, ServiceCommandError.notSupported());
        }
    }

    @Override
    public void getDuration(DurationListener listener) {
        if (getDLNAService() != null) {
            getDLNAService().getDuration(listener);
        } else {
            if (listener != null)
                Util.postError(listener, ServiceCommandError.notSupported());
        }
    }

    @Override
    public void getPosition(PositionListener listener) {
        if (getDLNAService() != null) {
            getDLNAService().getPosition(listener);
        } else {
            if (listener != null)
                Util.postError(listener, ServiceCommandError.notSupported());
        }
    }

    @Override
    public void getPlayState(PlayStateListener listener) {
        if (getDLNAService() != null) {
            getDLNAService().getPlayState(listener);
        }
        else {
            if (listener != null)
                Util.postError(listener, ServiceCommandError.notSupported());
        }

    }

    @Override
    public ServiceSubscription<PlayStateListener> subscribePlayState(PlayStateListener listener) {
        if (getDLNAService() != null) {
            return getDLNAService().subscribePlayState(listener);
        }
        else Util.postError(listener, ServiceCommandError.notSupported());
        return null;

    }

    /**************
    MOUSE CONTROL
     **************/
    @Override
    public MouseControl getMouseControl() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getMouseControlCapabilityLevel() {
        return CapabilityPriorityLevel.HIGH;
    }

    private void setMouseCursorVisible(boolean visible, ResponseListener<Object> listener) {
        String requestURL = getUDAPRequestURL(UDAP_PATH_EVENT);

        Map <String,String> params = new HashMap<String,String>();
        params.put("name", "CursorVisible");
        params.put("value", visible? "true" : "false");
        params.put("mode", "auto");

        String httpMessage = getUDAPMessageBody(UDAP_API_EVENT, params);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, requestURL, httpMessage, listener);
        request.send();
    }

    @Override
    public void connectMouse() {
        ResponseListener<Object> listener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                Log.d(Util.T, "Netcast TV's mouse has been connected");

                mMouseDistance = new PointF(0, 0);
                mMouseIsMoving = false;
            }

            @Override
            public void onError(ServiceCommandError error) {
                Log.w(Util.T, "Netcast TV's mouse connection has been failed");
            }
        };

        setMouseCursorVisible(true, listener);
    }

    @Override
    public void disconnectMouse() {
        setMouseCursorVisible(false, null);
    }

    @Override
    public void click() {
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
            }

            @Override
            public void onError(ServiceCommandError error) {
                Log.w(Util.T, "Netcast TV's mouse click has been failed");
            }
        };

        String requestURL = getUDAPRequestURL(UDAP_PATH_COMMAND);

        Map <String,String> params = new HashMap<String,String>();
        params.put("name", "HandleTouchClick");

        String httpMessage = getUDAPMessageBody(UDAP_API_COMMAND, params);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, requestURL, httpMessage, responseListener);
        request.send();
    }

    @Override
    public void move(double dx, double dy) {
        mMouseDistance.x += dx;
        mMouseDistance.y += dy;

        if (!mMouseIsMoving)
        {
            mMouseIsMoving = true;
            this.moveMouse();
        }
    }

    private void moveMouse() {
        String requestURL = getUDAPRequestURL(UDAP_PATH_COMMAND);

        int x = (int)mMouseDistance.x;
        int y = (int)mMouseDistance.y;

        Map <String,String> params = new HashMap<String,String>();
        params.put("name", "HandleTouchMove");
        params.put("x", String.valueOf(x));
        params.put("y", String.valueOf(y));

        mMouseDistance.x = mMouseDistance.y = 0;

        final NetcastTVService mouseService = this;

        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                if (mMouseDistance.x > 0 || mMouseDistance.y > 0)
                    mouseService.moveMouse();
                else
                    mMouseIsMoving = false;
            }

            @Override
            public void onError(ServiceCommandError error) {
                Log.w(Util.T, "Netcast TV's mouse move has failed");

                mMouseIsMoving = false;
            }
        };

        String httpMessage = getUDAPMessageBody(UDAP_API_COMMAND, params);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, requestURL, httpMessage, responseListener);
        request.send();
    }

    @Override
    public void move(PointF diff) {
        move(diff.x, diff.y);
    }

    @Override
    public void scroll(double dx, double dy) {
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onError(ServiceCommandError error) {
                Log.w(Util.T, "Netcast TV's mouse scroll has been failed");
            }
        };

        String requestURL = getUDAPRequestURL(UDAP_PATH_COMMAND);

        Map <String,String> params = new HashMap<String,String>();
        params.put("name", "HandleTouchWheel");
        if (dy > 0) 
            params.put("value", "up");
        else 
            params.put("value", "down");

        String httpMessage = getUDAPMessageBody(UDAP_API_COMMAND, params);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, requestURL, httpMessage, responseListener);
        request.send();
    }

    @Override
    public void scroll(PointF diff) {
        scroll(diff.x, diff.y);
    }


    /**************
    KEYBOARD CONTROL
     **************/
    @Override
    public TextInputControl getTextInputControl() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getTextInputControlCapabilityLevel() {
        return CapabilityPriorityLevel.HIGH;
    }

    @Override
    public ServiceSubscription<TextInputStatusListener> subscribeTextInputStatus(final TextInputStatusListener listener) {
        keyboardString = new StringBuilder();

        URLServiceSubscription<TextInputStatusListener> request = new URLServiceSubscription<TextInputStatusListener>(this, "KeyboardVisible", null, null);
        request.addListener(listener);

        addSubscription(request);

        return request;
    }

    @Override
    public void sendText(final String input) {
        Log.d(Util.T, "Add to Queue: " + input);
        keyboardString.append(input);
        handleKeyboardInput("Editing", keyboardString.toString());
    }

    @Override
    public void sendEnter() {

        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onError(ServiceCommandError error) {
                Log.w(Util.T, "Netcast TV's enter key has been failed");
            }
        };
        handleKeyboardInput("EditEnd", keyboardString.toString());
        sendVirtualKeyCode(NetcastVirtualKeycodes.RED.getCode(), responseListener);       // Send RED Key to enter the "ENTER" button
    }

    @Override
    public void sendDelete() {
        if (keyboardString.length() > 1) {
            keyboardString.deleteCharAt(keyboardString.length()-1);
        }
        else {
            keyboardString = new StringBuilder();
        }

        handleKeyboardInput("Editing", keyboardString.toString());
    }

    private ResponseListener<String> mTextChangedListener = new ResponseListener<String>() {

        @Override
        public void onError(ServiceCommandError error) { }

        @Override
        public void onSuccess(String newValue) {
            keyboardString = new StringBuilder(newValue);
        }
    };

    private void handleKeyboardInput(final String state, final String buffer) {
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onError(ServiceCommandError error) {
                Log.w(Util.T, "Netcast TV's keyboard input has been failed");
            }
        };

        String requestURL = getUDAPRequestURL(UDAP_PATH_EVENT);

        Map <String,String> params = new HashMap<String,String>();
        params.put("name", "TextEdited");
        params.put("state", state);
        params.put("value", buffer);

        String httpMessage = getUDAPMessageBody(UDAP_API_EVENT, params);

        ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(this, requestURL, httpMessage, responseListener);
        request.send();
    }


    /**************
    KEY CONTROL
     **************/
    @Override
    public KeyControl getKeyControl() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getKeyControlCapabilityLevel() {
        return CapabilityPriorityLevel.HIGH;
    }

    @Override
    public void up(final ResponseListener<Object> listener) {
        sendVirtualKeyCode(NetcastVirtualKeycodes.KEY_UP.getCode(), listener);
    }

    @Override
    public void down(final ResponseListener<Object> listener) {
        sendVirtualKeyCode(NetcastVirtualKeycodes.KEY_DOWN.getCode(), listener);
    }

    @Override
    public void left(final ResponseListener<Object> listener) {
        sendVirtualKeyCode(NetcastVirtualKeycodes.KEY_LEFT.getCode(), listener);
    }

    @Override
    public void right(final ResponseListener<Object> listener) {
        sendVirtualKeyCode(NetcastVirtualKeycodes.KEY_RIGHT.getCode(), listener);
    }

    @Override
    public void ok(final ResponseListener<Object> listener) {
        sendVirtualKeyCode(NetcastVirtualKeycodes.OK.getCode(), listener);
    }

    @Override
    public void back(final ResponseListener<Object> listener) {
        sendVirtualKeyCode(NetcastVirtualKeycodes.BACK.getCode(), listener);
    }

    @Override
    public void home(final ResponseListener<Object> listener) {
        sendVirtualKeyCode(NetcastVirtualKeycodes.HOME.getCode(), listener);
    }


    /**************
    POWER CONTROL
     **************/
    @Override
    public PowerControl getPowerControl() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getPowerControlCapabilityLevel() {
        return CapabilityPriorityLevel.HIGH;
    }

    @Override
    public void powerOff(ResponseListener<Object> listener) {
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onError(ServiceCommandError error) {
                Log.w(Util.T, "Netcast TV's power off has been failed");
            }
        };

        sendVirtualKeyCode(NetcastVirtualKeycodes.POWER.getCode(), responseListener);
    }

    @Override
    public void powerOn(ResponseListener<Object> listener) {
        if (listener != null)
            listener.onError(ServiceCommandError.notSupported());
    }

    private JSONObject parseVolumeXmlToJSON(String data) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            InputStream stream = new ByteArrayInputStream(data.getBytes("UTF-8"));

            SAXParser saxParser = saxParserFactory.newSAXParser();
            NetcastVolumeParser handler = new NetcastVolumeParser();
            saxParser.parse(stream, handler);

            return handler.getVolumeStatus();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int parseAppNumberXmlToJSON(String data) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            InputStream stream = new ByteArrayInputStream(data.getBytes("UTF-8"));

            SAXParser saxParser = saxParserFactory.newSAXParser();
            NetcastAppNumberParser handler = new NetcastAppNumberParser();
            saxParser.parse(stream, handler);

            return handler.getApplicationNumber();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private JSONArray parseApplicationsXmlToJSON(String data) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            InputStream stream = new ByteArrayInputStream(data.getBytes("UTF-8"));

            SAXParser saxParser = saxParserFactory.newSAXParser();
            NetcastApplicationsParser handler = new NetcastApplicationsParser();
            saxParser.parse(stream, handler);

            return handler.getApplications();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getHttpMessageForHandleKeyInput(final int keycode) {
        String strKeycode = String.valueOf(keycode);

        Map <String,String> params = new HashMap<String,String>();
        params.put("name", "HandleKeyInput");
        params.put("value", strKeycode);

        return getUDAPMessageBody(UDAP_API_COMMAND, params);
    }

    @Override
    public void sendKeyCode(KeyCode keycode, ResponseListener<Object> listener) {
        switch (keycode) {
            case NUM_0:
                sendVirtualKeyCode(NetcastVirtualKeycodes.NUMBER_0.getCode(), listener);
                break;
            case NUM_1:
                sendVirtualKeyCode(NetcastVirtualKeycodes.NUMBER_1.getCode(), listener);
                break;
            case NUM_2:
                sendVirtualKeyCode(NetcastVirtualKeycodes.NUMBER_2.getCode(), listener);
                break;
            case NUM_3:
                sendVirtualKeyCode(NetcastVirtualKeycodes.NUMBER_3.getCode(), listener);
                break;
            case NUM_4:
                sendVirtualKeyCode(NetcastVirtualKeycodes.NUMBER_4.getCode(), listener);
                break;
            case NUM_5:
                sendVirtualKeyCode(NetcastVirtualKeycodes.NUMBER_5.getCode(), listener);
                break;
            case NUM_6:
                sendVirtualKeyCode(NetcastVirtualKeycodes.NUMBER_6.getCode(), listener);
                break;
            case NUM_7:
                sendVirtualKeyCode(NetcastVirtualKeycodes.NUMBER_7.getCode(), listener);
                break;
            case NUM_8:
                sendVirtualKeyCode(NetcastVirtualKeycodes.NUMBER_8.getCode(), listener);
                break;
            case NUM_9:
                sendVirtualKeyCode(NetcastVirtualKeycodes.NUMBER_9.getCode(), listener);
                break;
            case DASH:
                sendVirtualKeyCode(NetcastVirtualKeycodes.DASH.getCode(), listener);
                break;
            case ENTER:
                sendVirtualKeyCode(NetcastVirtualKeycodes.OK.getCode(), listener);
                break;
            default:
                Util.postError(listener, new ServiceCommandError(0, "The keycode is not available", null));
        }
    }
    
    private void sendVirtualKeyCode(final int keycode, final ResponseListener<Object> listener) {
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String requestURL = getUDAPRequestURL(UDAP_PATH_COMMAND);
                String httpMessage = getHttpMessageForHandleKeyInput(keycode);

                ServiceCommand<ResponseListener<Object>> request = new ServiceCommand<ResponseListener<Object>>(NetcastTVService.this, requestURL, httpMessage, listener);
                request.send();
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        };

        setMouseCursorVisible(false, responseListener);
    }

    private String getUDAPRequestURL(String path) {
        return getUDAPRequestURL(path, null);
    }

    private String getUDAPRequestURL(String path, String target) {
        return getUDAPRequestURL(path, target, null);
    }

    private String getUDAPRequestURL(String path, String target, String type) {
        return getUDAPRequestURL(path, target, type, null, null);
    }

    private String getUDAPRequestURL(String path, String target, String type, String index, String number) {
        // Type Values
        // 1: List of all apps
        // 2: List of apps in the Premium category
        // 3: List of apps in the My Apps category

        StringBuilder sb = new StringBuilder();
        sb.append("http://");
        sb.append(serviceDescription.getIpAddress());
        sb.append(":");
        sb.append(serviceDescription.getPort());
        sb.append(path);

        if (target != null) { 
            sb.append("?target=");
            sb.append(target);

            if (type != null) {
                sb.append("&type=");
                sb.append(type);
            }

            if (index != null) {
                sb.append("&index=");
                sb.append(index);
            }

            if (number != null) {
                sb.append("&number=");
                sb.append(number);
            }
        }

        return sb.toString();
    }

    private String getUDAPMessageBody(String api, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append("<envelope>");
        sb.append("<api type=\"").append(api).append("\">");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            sb.append(createNode(key, value));
        }

        sb.append("</api>");
        sb.append("</envelope>");

        return sb.toString();
    }

    private String createNode(String tag, String value) {
        return "<" + tag + ">" + value + "</" + tag + ">";
    }

    public String decToHex(String dec) {
        if (dec != null && dec.length() > 0) {
            try {
                return decToHex(Long.parseLong(dec.trim()));
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String decToHex(long dec) {
        return String.format("%016x",dec);
    }

    @Override
    public void sendCommand(final ServiceCommand<?> mCommand) {
        Util.runInBackground(new Runnable() {

            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                final ServiceCommand<ResponseListener<Object>> command = (ServiceCommand<ResponseListener<Object>>) mCommand;
                Object payload = command.getPayload();

                try {
                    HttpConnection connection = HttpConnection.newInstance(URI.create(command.getTarget()));
                    connection.setHeader(HttpMessage.USER_AGENT, HttpMessage.UDAP_USER_AGENT);
                    connection.setHeader(HttpMessage.CONTENT_TYPE_HEADER, HttpMessage.CONTENT_TYPE_TEXT_XML);
                    if (payload != null && command.getHttpMethod().equalsIgnoreCase(ServiceCommand.TYPE_POST)) {
                        connection.setMethod(HttpConnection.Method.POST);
                        connection.setPayload(payload.toString());
                    }
                    connection.execute();
                    int code = connection.getResponseCode();
                    Log.d("", "RESP " + code);
                    if (code == 200) {
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

    private void addSubscription(URLServiceSubscription<?> subscription) {
        subscriptions.add(subscription);

        if (httpServer != null)
            httpServer.setSubscriptions(subscriptions);
    }

    @Override
    public void unsubscribe(URLServiceSubscription<?> subscription) {
        subscriptions.remove(subscription);

        if (httpServer != null)
            httpServer.setSubscriptions(subscriptions);
    }

//    @Override
//    public LaunchSession decodeLaunchSession(String type, JSONObject obj) throws JSONException {
//        if ("netcasttv".equals(type)) {
//            LaunchSession launchSession = LaunchSession.launchSessionFromJSONObject(obj);
//            launchSession.setService(this);
//
//            return launchSession;
//        }
//        return null;
//    }

    @Override
    protected void updateCapabilities() {
        List<String> capabilities = new ArrayList<String>();

        if (DiscoveryManager.getInstance().getPairingLevel() == PairingLevel.ON) {
            Collections.addAll(capabilities, TextInputControl.Capabilities);
            Collections.addAll(capabilities, MouseControl.Capabilities);
            Collections.addAll(capabilities, KeyControl.Capabilities);
            Collections.addAll(capabilities, MediaPlayer.Capabilities);

            capabilities.add(PowerControl.Off);

            capabilities.add(Play); 
            capabilities.add(Pause); 
            capabilities.add(Stop); 
            capabilities.add(Duration); 
            capabilities.add(Position); 
            capabilities.add(Seek); 

            capabilities.add(Application); 
            capabilities.add(Application_Close); 
            capabilities.add(Application_List); 
            capabilities.add(Browser); 
            capabilities.add(Hulu); 
            capabilities.add(Netflix); 
            capabilities.add(Netflix_Params); 
            capabilities.add(YouTube); 
            capabilities.add(YouTube_Params); 
            capabilities.add(AppStore); 

            capabilities.add(Channel_Up); 
            capabilities.add(Channel_Down); 
            capabilities.add(Channel_Get); 
            capabilities.add(Channel_List); 
            capabilities.add(Channel_Subscribe); 
            capabilities.add(Get_3D); 
            capabilities.add(Set_3D); 
            capabilities.add(Subscribe_3D); 

            capabilities.add(Picker_Launch); 
            capabilities.add(Picker_Close); 

            capabilities.add(Volume_Get); 
            capabilities.add(Volume_Up_Down); 
            capabilities.add(Mute_Get); 
            capabilities.add(Mute_Set);

            if (serviceDescription.getModelNumber().equals("4.0")) {
                capabilities.add(AppStore_Params); 
            }
        } else {
            Collections.addAll(capabilities, MediaPlayer.Capabilities);
            capabilities.add(Play); 
            capabilities.add(Pause); 
            capabilities.add(Stop); 

            capabilities.add(YouTube); 
            capabilities.add(YouTube_Params); 
        }

        capabilities.add(MediaPlayer.Subtitle_SRT);
        setCapabilities(capabilities);
    }

    public DLNAService getDLNAService() {
        if (dlnaService == null) {
            DiscoveryManager discoveryManager = DiscoveryManager.getInstance();
            ConnectableDevice device = discoveryManager.getAllDevices().get(serviceDescription.getIpAddress());

            if (device != null) {
                DLNAService foundService = null;

                for (DeviceService service: device.getServices()) {
                    if (DLNAService.class.isAssignableFrom(service.getClass())) {
                        foundService = (DLNAService)service;
                        break;
                    }
                }

                dlnaService = foundService;
            }
        }

        return dlnaService;
    }

    public DIALService getDIALService() {
        if (dialService == null) {
            DiscoveryManager discoveryManager = DiscoveryManager.getInstance();
            ConnectableDevice device = discoveryManager.getAllDevices().get(serviceDescription.getIpAddress());

            if (device != null) {
                DIALService foundService = null;

                for (DeviceService service: device.getServices()) {
                    if (DIALService.class.isAssignableFrom(service.getClass())) {
                        foundService = (DIALService)service;
                        break;
                    }
                }

                dialService = foundService;
            }
        }

        return dialService;
    }
}
