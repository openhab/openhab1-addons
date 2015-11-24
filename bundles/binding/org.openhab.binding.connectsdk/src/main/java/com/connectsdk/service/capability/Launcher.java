/*
 * Launcher
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

import java.util.List;

import com.connectsdk.core.AppInfo;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceSubscription;
import com.connectsdk.service.sessions.LaunchSession;

public interface Launcher extends CapabilityMethods {
    public final static String Any = "Launcher.Any";

    public final static String Application = "Launcher.App";
    public final static String Application_Params = "Launcher.App.Params";
    public final static String Application_Close = "Launcher.App.Close";
    public final static String Application_List = "Launcher.App.List";
    public final static String Browser = "Launcher.Browser";
    public final static String Browser_Params = "Launcher.Browser.Params";
    public final static String Hulu = "Launcher.Hulu";
    public final static String Hulu_Params = "Launcher.Hulu.Params";
    public final static String Netflix = "Launcher.Netflix";
    public final static String Netflix_Params = "Launcher.Netflix.Params";
    public final static String YouTube = "Launcher.YouTube";
    public final static String YouTube_Params = "Launcher.YouTube.Params";
    public final static String AppStore = "Launcher.AppStore";
    public final static String AppStore_Params = "Launcher.AppStore.Params";
    public final static String AppState = "Launcher.AppState";
    public final static String AppState_Subscribe = "Launcher.AppState.Subscribe";
    public final static String RunningApp = "Launcher.RunningApp";
    public final static String RunningApp_Subscribe = "Launcher.RunningApp.Subscribe";

    public final static String[] Capabilities = {
        Application,
        Application_Params,
        Application_Close,
        Application_List,
        Browser,
        Browser_Params,
        Hulu,
        Hulu_Params,
        Netflix,
        Netflix_Params,
        YouTube,
        YouTube_Params,
        AppStore, 
        AppStore_Params, 
        AppState,
        AppState_Subscribe,
        RunningApp,
        RunningApp_Subscribe
    };

    public Launcher getLauncher();
    public CapabilityPriorityLevel getLauncherCapabilityLevel();

    public void launchAppWithInfo(AppInfo appInfo, AppLaunchListener listener);
    public void launchAppWithInfo(AppInfo appInfo, Object params, AppLaunchListener listener);
    public void launchApp(String appId, AppLaunchListener listener);

    public void closeApp(LaunchSession launchSession, ResponseListener<Object> listener);

    public void getAppList(AppListListener listener);

    public void getRunningApp(AppInfoListener listener);
    public ServiceSubscription<AppInfoListener> subscribeRunningApp(AppInfoListener listener);

    public void getAppState(LaunchSession launchSession, AppStateListener listener);
    public ServiceSubscription<AppStateListener> subscribeAppState(LaunchSession launchSession, AppStateListener listener);

    public void launchBrowser(String url, AppLaunchListener listener);
    public void launchYouTube(String contentId, AppLaunchListener listener);
    public void launchYouTube(String contentId, float startTime, AppLaunchListener listener);
    public void launchNetflix(String contentId, AppLaunchListener listener);
    public void launchHulu(String contentId, AppLaunchListener listener);
    public void launchAppStore(String appId, AppLaunchListener listener);

    /**
     * Success listener that is called upon successfully launching an app.
     *
     * Passes a LaunchSession Object containing important information about the app's launch session
     */
    public static interface AppLaunchListener extends ResponseListener<LaunchSession> { }

    /**
     * Success listener that is called upon requesting info about the current running app.
     *
     * Passes an AppInfo object containing info about the running app
     */
    public static interface AppInfoListener extends ResponseListener<AppInfo> { }

    /**
     * Success block that is called upon successfully getting the app list.
     *
     * Passes a List containing an AppInfo for each available app on the device
     */
    public static interface AppListListener extends ResponseListener<List<AppInfo>> { }

    // @cond INTERNAL
    public static interface AppCountListener extends ResponseListener<Integer> { }
    // @endcond

    /**
     * Success block that is called upon successfully getting an app's state.
     *
     * Passes an AppState object which contains information about the running app.
     */
    public static interface AppStateListener extends ResponseListener<AppState> { }

    /**
     * Helper class used with the AppStateListener to return the current state of an app.
     */
    public static class AppState {
        /** Whether the app is currently running. */
        public boolean running;
        /** Whether the app is currently visible. */
        public boolean visible;

        public AppState(boolean running, boolean visible) {
            this.running = running;
            this.visible = visible;
        }
    }
}
