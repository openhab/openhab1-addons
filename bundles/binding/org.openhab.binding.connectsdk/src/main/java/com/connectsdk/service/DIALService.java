/*
 * DIALService
 * Connect SDK
 * 
 * Copyright (c) 2014 LG Electronics.
 * Created by Hyun Kook Khang on 24 Jan 2014
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

import android.util.Log;

import com.connectsdk.core.AppInfo;
import com.connectsdk.core.Util;
import com.connectsdk.discovery.DiscoveryFilter;
import com.connectsdk.etc.helper.DeviceServiceReachability;
import com.connectsdk.etc.helper.HttpConnection;
import com.connectsdk.etc.helper.HttpMessage;
import com.connectsdk.service.capability.CapabilityMethods;
import com.connectsdk.service.capability.Launcher;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.NotSupportedServiceSubscription;
import com.connectsdk.service.command.ServiceCommand;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceSubscription;
import com.connectsdk.service.config.ServiceConfig;
import com.connectsdk.service.config.ServiceDescription;
import com.connectsdk.service.sessions.LaunchSession;
import com.connectsdk.service.sessions.LaunchSession.LaunchSessionType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DIALService extends DeviceService implements Launcher {

    public static final String ID = "DIAL";
    private static final String APP_NETFLIX = "Netflix";

    private static List<String> registeredApps = new ArrayList<String>();

    static {
        registeredApps.add("YouTube");
        registeredApps.add("Netflix");
        registeredApps.add("Amazon");
    }

    public static void registerApp(String appId) {
        if (!registeredApps.contains(appId))
            registeredApps.add(appId);
    }

    public DIALService(ServiceDescription serviceDescription, ServiceConfig serviceConfig) {
        super(serviceDescription, serviceConfig);
    }

    @Override
    public CapabilityPriorityLevel getPriorityLevel(Class<? extends CapabilityMethods> clazz) {
        if (clazz.equals(Launcher.class)) {
            return getLauncherCapabilityLevel();
        }
        return CapabilityPriorityLevel.NOT_SUPPORTED;
    }


    public static DiscoveryFilter discoveryFilter() {
        return new DiscoveryFilter(ID, "urn:dial-multiscreen-org:service:dial:1");
    }

    @Override
    public void setServiceDescription(ServiceDescription serviceDescription) {
        super.setServiceDescription(serviceDescription);

        Map<String, List<String>> responseHeaders = this.getServiceDescription().getResponseHeaders(); 

        if (responseHeaders != null) {
            String commandPath;
            List<String> commandPaths = responseHeaders.get("Application-URL");

            if (commandPaths != null && commandPaths.size() > 0) {
                commandPath = commandPaths.get(0);
                this.getServiceDescription().setApplicationURL(commandPath);
            }
        }

        probeForAppSupport();
    }

    @Override
    public Launcher getLauncher() {
        return this;
    }

    @Override
    public CapabilityPriorityLevel getLauncherCapabilityLevel() {
        return CapabilityPriorityLevel.NORMAL;
    }

    @Override
    public void launchApp(String appId, AppLaunchListener listener) {
        launchApp(appId, null, listener);
    }

    private void launchApp(String appId, JSONObject params, AppLaunchListener listener) {
        if (appId == null || appId.length() == 0) {
            Util.postError(listener, new ServiceCommandError(0, "Must pass a valid appId", null));
            return;
        }

        AppInfo appInfo = new AppInfo();
        appInfo.setName(appId);
        appInfo.setId(appId);

        launchAppWithInfo(appInfo, listener);
    }

    @Override
    public void launchAppWithInfo(AppInfo appInfo, AppLaunchListener listener) {
        launchAppWithInfo(appInfo, null, listener);
    }

    @Override
    public void launchAppWithInfo(final AppInfo appInfo, Object params, final AppLaunchListener listener) {
        ServiceCommand<ResponseListener<Object>> command =
                new ServiceCommand<ResponseListener<Object>>(getCommandProcessor(),
                        requestURL(appInfo.getName()), params, new ResponseListener<Object>() {
            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, new ServiceCommandError(0, "Problem Launching app", null));
            }

            @Override
            public void onSuccess(Object object) {
                LaunchSession launchSession = LaunchSession.launchSessionForAppId(appInfo.getId());
                launchSession.setAppName(appInfo.getName());
                launchSession.setSessionId((String)object);
                launchSession.setService(DIALService.this);
                launchSession.setSessionType(LaunchSessionType.App);

                Util.postSuccess(listener, launchSession);
            }
        });

        command.send();
    }

    @Override
    public void launchBrowser(String url, AppLaunchListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void closeApp(final LaunchSession launchSession, final ResponseListener<Object> listener) {
        getAppState(launchSession.getAppName(), new AppStateListener() {

            @Override
            public void onSuccess(AppState state) {
                String uri = requestURL(launchSession.getAppName());

                if (launchSession.getSessionId().contains("http://")
                        || launchSession.getSessionId().contains("https://"))
                    uri = launchSession.getSessionId();
                else if (launchSession.getSessionId().endsWith("run")
                        || launchSession.getSessionId().endsWith("run/"))
                    uri = requestURL(launchSession.getAppId() + "/run");
                else
                    uri = requestURL(launchSession.getSessionId());

                ServiceCommand<ResponseListener<Object>> command =
                        new ServiceCommand<ResponseListener<Object>>(launchSession.getService(),
                                uri, null, listener);
                command.setHttpMethod(ServiceCommand.TYPE_DEL);
                command.send();
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        });
    }

    @Override
    public void launchYouTube(String contentId, AppLaunchListener listener) {
        launchYouTube(contentId, (float) 0.0, listener);
    }

    @Override
    public void launchYouTube(String contentId, float startTime, AppLaunchListener listener) {
        String params = null;
        AppInfo appInfo = new AppInfo("YouTube");
        appInfo.setName(appInfo.getId());

        if (contentId != null && contentId.length() > 0) {
            if (startTime < 0.0) {
                if (listener != null) {
                    listener.onError(new ServiceCommandError(0, "Start time may not be negative", null));
                }

                return;
            }

            String pairingCode = java.util.UUID.randomUUID().toString();
            params = String.format(Locale.US, "pairingCode=%s&v=%s&t=%.1f", pairingCode, contentId, startTime);
        }

        launchAppWithInfo(appInfo, params, listener);
    }

    @Override
    public void launchHulu(String contentId, AppLaunchListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void launchNetflix(final String contentId, AppLaunchListener listener) {
        JSONObject params = null;

        if (contentId != null && contentId.length() > 0) {
            try {
                params = new JSONObject() {{
                    put("v", contentId);
                }};
            } catch (JSONException e) {
                Log.e(Util.T, "Launch Netflix error", e);
            }
        }

        AppInfo appInfo = new AppInfo(APP_NETFLIX);
        appInfo.setName(appInfo.getId());

        launchAppWithInfo(appInfo, params, listener);
    }

    @Override
    public void launchAppStore(String appId, AppLaunchListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    private void getAppState(String appName, final AppStateListener listener) {
        ResponseListener<Object> responseListener = new ResponseListener<Object>() {

            @Override
            public void onSuccess(Object response) {
                String str = (String)response;
                String[] stateTAG = new String[2];
                stateTAG[0] = "<state>";
                stateTAG[1] = "</state>";


                int start = str.indexOf(stateTAG[0]);
                int end = str.indexOf(stateTAG[1]);

                if (start != -1 && end != -1) {
                    start += stateTAG[0].length();

                    String state = str.substring(start, end);
                    AppState appState = new AppState("running".equals(state), "running".equals(state));

                    Util.postSuccess(listener, appState);
                    // TODO: This isn't actually reporting anything.
//                    if (listener != null) 
//                        listener.onAppStateSuccess(state);
                } else {
                    Util.postError(listener, new ServiceCommandError(0, "Malformed response for app state", null));
                }
            }

            @Override
            public void onError(ServiceCommandError error) {
                Util.postError(listener, error);
            }
        };

        String uri = requestURL(appName);

        ServiceCommand<ResponseListener<Object>> request =
                new ServiceCommand<ResponseListener<Object>>(getCommandProcessor(), uri, null,
                        responseListener);
        request.setHttpMethod(ServiceCommand.TYPE_GET);

        request.send();
    }

    @Override
    public void getAppList(AppListListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void getRunningApp(AppInfoListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public ServiceSubscription<AppInfoListener> subscribeRunningApp(AppInfoListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());

        return new NotSupportedServiceSubscription<AppInfoListener>();
    }

    @Override
    public void getAppState(LaunchSession launchSession, AppStateListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public ServiceSubscription<AppStateListener> subscribeAppState(
            LaunchSession launchSession,
            com.connectsdk.service.capability.Launcher.AppStateListener listener) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void closeLaunchSession(LaunchSession launchSession, ResponseListener<Object> listener) {
        if (launchSession.getSessionType() == LaunchSessionType.App) {
            this.getLauncher().closeApp(launchSession, listener);
        } else
        {
            Util.postError(listener, new ServiceCommandError(-1, "Could not find a launcher associated with this LaunchSession", launchSession));
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

    @Override
    public void disconnect() {
        connected = false;

        if (mServiceReachability != null)
            mServiceReachability.stop();

        Util.runOnUI(new Runnable() {

            @Override
            public void run() {
                if (listener != null)
                    listener.onDisconnect(DIALService.this, null);
            }
        });
    }

    @Override
    public void onLoseReachability(DeviceServiceReachability reachability) {
        if (connected) {
            disconnect();
        } else {
            mServiceReachability.stop();
        }
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
                    HttpConnection connection = createHttpConnection(mCommand.getTarget());
                    if (payload != null || command.getHttpMethod().equalsIgnoreCase(ServiceCommand.TYPE_POST)) {
                        connection.setMethod(HttpConnection.Method.POST);
                        if (payload != null) {
                            connection.setHeader(HttpMessage.CONTENT_TYPE_HEADER, "text/plain; " +
                                    "charset=\"utf-8\"");
                            connection.setPayload(payload.toString());
                        }
                    } else if (command.getHttpMethod().equalsIgnoreCase(ServiceCommand.TYPE_DEL)) {
                        connection.setMethod(HttpConnection.Method.DELETE);
                    }
                    connection.execute();
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        Util.postSuccess(command.getResponseListener(), connection.getResponseString());
                    } else if (code == 201) {
                        Util.postSuccess(command.getResponseListener(), connection.getResponseHeader("Location"));
                    } else {
                        Util.postError(command.getResponseListener(), ServiceCommandError.getError(code));
                    }
                } catch (Exception e) {
                    Util.postError(command.getResponseListener(), new ServiceCommandError(0, e.getMessage(), null));
                }
            }
        });
    }

    HttpConnection createHttpConnection(String target) throws IOException {
        return HttpConnection.newInstance(URI.create(target));
    }

    private String requestURL(String appName) {
        String applicationURL = serviceDescription != null ? serviceDescription.getApplicationURL() : null;

        if (applicationURL == null) {
            throw new IllegalStateException("DIAL service application URL not available");
        }

        StringBuilder sb = new StringBuilder();

        sb.append(applicationURL);

        if (!applicationURL.endsWith("/"))
            sb.append("/");

        sb.append(appName);

        return sb.toString();
    }

    @Override
    protected void updateCapabilities() {
        List<String> capabilities = new ArrayList<String>();

        capabilities.add(Application);
        capabilities.add(Application_Params);
        capabilities.add(Application_Close);
        capabilities.add(AppState);

        setCapabilities(capabilities);
    }

    private void hasApplication(String appID, ResponseListener<Object> listener) {
        String uri = requestURL(appID);

        ServiceCommand<ResponseListener<Object>> command =
                new ServiceCommand<ResponseListener<Object>>(getCommandProcessor(), uri, null, listener);
        command.setHttpMethod(ServiceCommand.TYPE_GET);
        command.send();
    }

    private void probeForAppSupport() {
        if (serviceDescription.getApplicationURL() == null) {
            Log.d(Util.T, "unable to check for installed app; no service application url");
            return;
        }

        for (final String appID : registeredApps) {
            hasApplication(appID, new ResponseListener<Object>() {

                @Override public void onError(ServiceCommandError error) { }

                @Override
                public void onSuccess(Object object) {
                    addCapability("Launcher." + appID);
                    addCapability("Launcher." + appID + ".Params");
                }
            });
        }
    }
}
