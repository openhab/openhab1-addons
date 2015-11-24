/*
 * WebAppLauncher
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

package com.connectsdk.service.capability;

import org.json.JSONObject;

import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceSubscription;
import com.connectsdk.service.sessions.LaunchSession;
import com.connectsdk.service.sessions.WebAppSession.LaunchListener;
import com.connectsdk.service.sessions.WebAppSession.WebAppPinStatusListener;


public interface WebAppLauncher extends CapabilityMethods {
    public final static String Any = "WebAppLauncher.Any";

    public final static String Launch = "WebAppLauncher.Launch";
    public final static String Launch_Params = "WebAppLauncher.Launch.Params";
    public final static String Message_Send = "WebAppLauncher.Message.Send";
    public final static String Message_Receive = "WebAppLauncher.Message.Receive";
    public final static String Message_Send_JSON = "WebAppLauncher.Message.Send.JSON";
    public final static String Message_Receive_JSON = "WebAppLauncher.Message.Receive.JSON";
    public final static String Connect = "WebAppLauncher.Connect";
    public final static String Disconnect = "WebAppLauncher.Disconnect";
    public final static String Join = "WebAppLauncher.Join";
    public final static String Close = "WebAppLauncher.Close";
    public final static String Pin = "WebAppLauncher.Pin";

    public final static String[] Capabilities = {
        Launch,
        Launch_Params,
        Message_Send,
        Message_Receive,
        Message_Send_JSON,
        Message_Receive_JSON,
        Connect,
        Disconnect,
        Join,
        Close,
        Pin
    };

    public WebAppLauncher getWebAppLauncher();
    public CapabilityPriorityLevel getWebAppLauncherCapabilityLevel();

    public void launchWebApp(String webAppId, LaunchListener listener);
    public void launchWebApp(String webAppId, boolean relaunchIfRunning, LaunchListener listener);
    public void launchWebApp(String webAppId, JSONObject params, LaunchListener listener);
    public void launchWebApp(String webAppId, JSONObject params, boolean relaunchIfRunning, LaunchListener listener);

    public void joinWebApp(LaunchSession webAppLaunchSession, LaunchListener listener);
    public void joinWebApp(String webAppId, LaunchListener listener);

    public void closeWebApp(LaunchSession launchSession, ResponseListener<Object> listener);

    public void pinWebApp(String webAppId, ResponseListener<Object> listener);
    public void unPinWebApp(String webAppId, ResponseListener<Object> listener);
    public void isWebAppPinned(String webAppId, WebAppPinStatusListener listener);
    public ServiceSubscription<WebAppPinStatusListener> subscribeIsWebAppPinned(String webAppId, WebAppPinStatusListener listener);
}
