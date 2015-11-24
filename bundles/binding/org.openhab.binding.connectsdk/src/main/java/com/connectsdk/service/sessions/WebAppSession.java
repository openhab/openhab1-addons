/*
 * WebAppSession
 * Connect SDK
 * 
 * Copyright (c) 2014 LG Electronics.
 * Created by Jeffrey Glenn on 07 Mar 2014
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

package com.connectsdk.service.sessions;

import com.connectsdk.core.MediaInfo;
import com.connectsdk.core.Util;
import com.connectsdk.service.DeviceService;
import com.connectsdk.service.capability.MediaControl;
import com.connectsdk.service.capability.MediaPlayer;
import com.connectsdk.service.capability.PlaylistControl;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceSubscription;

import org.json.JSONObject;

/**
 * ###Overview When a web app is launched on a first screen device, there are
 * certain tasks that can be performed with that web app. WebAppSession serves
 * as a second screen reference of the web app that was launched. It behaves
 * similarly to LaunchSession, but is not nearly as static.
 * 
 * ###In Depth On top of maintaining session information (contained in the
 * launchSession property), WebAppSession provides access to a number of
 * capabilities. - MediaPlayer - MediaControl - Bi-directional communication
 * with web app
 * 
 * MediaPlayer and MediaControl are provided to allow for the most common first
 * screen use cases -- a media player (audio, video, & images).
 * 
 * The Connect SDK JavaScript Bridge has been produced to provide normalized
 * support for these capabilities across protocols (Chromecast, webOS, etc).
 */
public class WebAppSession implements MediaControl, MediaPlayer, PlaylistControl {

    /** Status of the web app */
    public enum WebAppStatus {
        /** Web app status is unknown */
        Unknown,
        /** Web app is running and in the foreground */
        Open,
        /** Web app is running and in the background */
        Background,
        /** Web app is in the foreground but has not started running yet */
        Foreground,
        /** Web app is not running and is not in the foreground or background */
        Closed
    }

    /**
     * Success block that is called upon successfully getting a web app's status.
     *
     * @param status The current running & foreground status of the web app
     */
    public static interface WebAppPinStatusListener extends ResponseListener<Boolean> { }

    /**
     * LaunchSession object containing key session information. Much of this
     * information is required for web app messaging & closing the web app.
     */
    public LaunchSession launchSession;

    // @cond INTERNAL
    protected DeviceService service;
    private WebAppSessionListener webAppListener;

    // @endcond

    /**
     * Instantiates a WebAppSession object with all the information necessary to
     * interact with a web app.
     * 
     * @param launchSession
     *            LaunchSession containing info about the web app session
     * @param service
     *            DeviceService that was responsible for launching this web app
     */
    public WebAppSession(LaunchSession launchSession, DeviceService service) {
        this.launchSession = launchSession;
        this.service = service;
    }

    /**
     * DeviceService that was responsible for launching this web app.
     */
    protected void setService(DeviceService service) {
    }


    /**
     * Subscribes to changes in the web app's status.
     * 
     * @param listener
     *            (optional) MessageListener to be called on app status change
     */
    public ServiceSubscription<MessageListener> subscribeWebAppStatus(
            MessageListener listener) {
        if (listener != null)
            listener.onError(ServiceCommandError.notSupported());

        return null;
    }

    /**
     * Establishes a communication channel with the web app.
     * 
     * @param connectionListener
     *            (optional) ResponseListener to be called on success
     */
    public void connect(ResponseListener<Object> connectionListener) {
        Util.postError(connectionListener, ServiceCommandError.notSupported());
    }

    /**
     * Establishes a communication channel with a currently running web app.
     * 
     * @param connectionListener
     */
    public void join(ResponseListener<Object> connectionListener) {
        Util.postError(connectionListener, ServiceCommandError.notSupported());
    }

    /**
     * Closes any open communication channel with the web app.
     */
    public void disconnectFromWebApp() {
    }

    /**
     * Pin the web app on the launcher.
     */
    public void pinWebApp(String webAppId, ResponseListener<Object> listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    /**
     * UnPin the web app on the launcher.
     *
     * @param webAppId NSString webAppId to be unpinned.
     */
    public void unPinWebApp(String webAppId, ResponseListener<Object> listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    /**
     * To check if the web app is pinned or not
     */
    public void isWebAppPinned(String webAppId, WebAppPinStatusListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    /**
     * Subscribe to check if the web app is pinned or not
     */
    public ServiceSubscription<WebAppPinStatusListener> subscribeIsWebAppPinned(String webAppId, WebAppPinStatusListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
        return null;
    }

    /**
     * Closes the web app on the first screen device.
     * 
     * @param listener
     *            (optional) ResponseListener to be called on success
     */
    public void close(ResponseListener<Object> listener) {
        if (listener != null)
            listener.onError(ServiceCommandError.notSupported());
    }

    /**
     * Sends a simple string to the web app. The Connect SDK JavaScript Bridge
     * will receive this message and hand it off as a string object.
     * 
     * @param listener
     *            (optional) ResponseListener to be called on success
     */
    public void sendMessage(String message, ResponseListener<Object> listener) {
        if (listener != null)
            listener.onError(ServiceCommandError.notSupported());
    }

    /**
     * Sends a JSON object to the web app. The Connect SDK JavaScript Bridge
     * will receive this message and hand it off as a JavaScript object.
     * 
     * @param success
     *            (optional) ResponseListener to be called on success
     */
    public void sendMessage(JSONObject message,
            ResponseListener<Object> listener) {
        if (listener != null) {
            listener.onError(ServiceCommandError.notSupported());
        }
    }

    // @cond INTERNAL
    @Override
    public MediaControl getMediaControl() {
        return null;
    }

    @Override
    public CapabilityPriorityLevel getMediaControlCapabilityLevel() {
        return CapabilityPriorityLevel.VERY_LOW;
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

    @Override
    public void play(ResponseListener<Object> listener) {
        MediaControl mediaControl = null;

        if (service != null)
            mediaControl = service.getAPI(MediaControl.class);

        if (mediaControl != null)
            mediaControl.play(listener);
        else if (listener != null)
            listener.onError(ServiceCommandError.notSupported());
    }

    @Override
    public void pause(ResponseListener<Object> listener) {
        MediaControl mediaControl = null;

        if (service != null)
            mediaControl = service.getAPI(MediaControl.class);

        if (mediaControl != null)
            mediaControl.pause(listener);
        else if (listener != null)
            listener.onError(ServiceCommandError.notSupported());
    }

    @Override
    public void stop(ResponseListener<Object> listener) {
        MediaControl mediaControl = null;

        if (service != null)
            mediaControl = service.getAPI(MediaControl.class);

        if (mediaControl != null)
            mediaControl.stop(listener);
        else if (listener != null)
            listener.onError(ServiceCommandError.notSupported());
    }

    @Override
    public void rewind(ResponseListener<Object> listener) {
        MediaControl mediaControl = null;

        if (service != null)
            mediaControl = service.getAPI(MediaControl.class);

        if (mediaControl != null)
            mediaControl.rewind(listener);
        else if (listener != null)
            listener.onError(ServiceCommandError.notSupported());
    }

    @Override
    public void fastForward(ResponseListener<Object> listener) {
        MediaControl mediaControl = null;

        if (service != null)
            mediaControl = service.getAPI(MediaControl.class);

        if (mediaControl != null)
            mediaControl.fastForward(listener);
        else if (listener != null)
            listener.onError(ServiceCommandError.notSupported());
    }

    @Override
    public void previous(ResponseListener<Object> listener) {
        MediaControl mediaControl = null;

        if (service != null)
            mediaControl = service.getAPI(MediaControl.class);

        if (mediaControl != null)
            mediaControl.previous(listener);
        else if (listener != null)
            listener.onError(ServiceCommandError.notSupported());
    }

    @Override
    public void next(ResponseListener<Object> listener) {
        MediaControl mediaControl = null;

        if (service != null)
            mediaControl = service.getAPI(MediaControl.class);

        if (mediaControl != null)
            mediaControl.next(listener);
        else if (listener != null)
            listener.onError(ServiceCommandError.notSupported());
    }

    @Override
    public void seek(long position, ResponseListener<Object> listener) {
        MediaControl mediaControl = null;

        if (service != null)
            mediaControl = service.getAPI(MediaControl.class);

        if (mediaControl != null)
            mediaControl.seek(position, listener);
        else if (listener != null)
            listener.onError(ServiceCommandError.notSupported());
    }

    @Override
    public void getDuration(DurationListener listener) {
        MediaControl mediaControl = null;

        if (service != null)
            mediaControl = service.getAPI(MediaControl.class);

        if (mediaControl != null)
            mediaControl.getDuration(listener);
        else if (listener != null)
            listener.onError(ServiceCommandError.notSupported());
    }

    @Override
    public void getPosition(PositionListener listener) {
        MediaControl mediaControl = null;

        if (service != null)
            mediaControl = service.getAPI(MediaControl.class);

        if (mediaControl != null)
            mediaControl.getPosition(listener);
        else if (listener != null)
            listener.onError(ServiceCommandError.notSupported());
    }

    @Override
    public void getPlayState(PlayStateListener listener) {
        MediaControl mediaControl = null;

        if (service != null)
            mediaControl = service.getAPI(MediaControl.class);

        if (mediaControl != null)
            mediaControl.getPlayState(listener);
        else if (listener != null)
            listener.onError(ServiceCommandError.notSupported());
    }

    @Override
    public ServiceSubscription<PlayStateListener> subscribePlayState(
            PlayStateListener listener) {
        MediaControl mediaControl = null;

        if (service != null)
            mediaControl = service.getAPI(MediaControl.class);

        if (mediaControl != null)
            return mediaControl.subscribePlayState(listener);
        else if (listener != null)
            listener.onError(ServiceCommandError.notSupported());

        return null;
    }

    @Override
    public void closeMedia(LaunchSession launchSession,
            ResponseListener<Object> listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void displayImage(String url, String mimeType, String title,
            String description, String iconSrc,
            MediaPlayer.LaunchListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void displayImage(MediaInfo mediaInfo, MediaPlayer.LaunchListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void playMedia(String url, String mimeType, String title, String description, String iconSrc, boolean shouldLoop, MediaPlayer.LaunchListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void playMedia(MediaInfo mediaInfo, boolean shouldLoop,
            MediaPlayer.LaunchListener listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public MediaPlayer getMediaPlayer() {
        return null;
    }

    @Override
    public CapabilityPriorityLevel getMediaPlayerCapabilityLevel() {
        return CapabilityPriorityLevel.VERY_LOW;
    }

    @Override
    public PlaylistControl getPlaylistControl() {
        return null;
    }

    @Override
    public CapabilityPriorityLevel getPlaylistControlCapabilityLevel() {
        return CapabilityPriorityLevel.VERY_LOW;
    }

    @Override
    public void jumpToTrack(long index, ResponseListener<Object> listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    @Override
    public void setPlayMode(PlayMode playMode, ResponseListener<Object> listener) {
        Util.postError(listener, ServiceCommandError.notSupported());
    }

    // @endcond

    /**
     * When messages are received from a web app, they are parsed into the
     * appropriate object type (string vs JSON/NSDictionary) and routed to the
     * WebAppSessionListener.
     */
    public WebAppSessionListener getWebAppSessionListener() {
        return webAppListener;
    }

    /**
     * When messages are received from a web app, they are parsed into the
     * appropriate object type (string vs JSON/NSDictionary) and routed to the
     * WebAppSessionListener.
     * 
     * @param listener
     *            WebAppSessionListener to be called when messages are received
     *            from the web app
     */
    public void setWebAppSessionListener(WebAppSessionListener listener) {
        webAppListener = listener;
    }

    /**
     * Success block that is called upon successfully launch of a web app.
     * 
     * Passes a WebAppSession Object containing important information about the
     * web app's session. This object is required to perform many functions with
     * the web app, including app-to-app communication, media playback, closing,
     * etc.
     */
    public static interface LaunchListener extends
    ResponseListener<WebAppSession> {
    }

    /**
     * Success block that is called upon successfully getting a web app's
     * status.
     * 
     * Passes a WebAppStatus of the current running & foreground status of the
     * web app
     */
    public static interface StatusListener extends
    ResponseListener<WebAppStatus> {
    }

    // @cond INTERNAL
    public static interface MessageListener extends ResponseListener<Object> {
        abstract public void onMessage(Object message);
    }
    // @endcond
}
