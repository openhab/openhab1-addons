/*
 * MediaPlayer
 * Connect SDK
 * 
 * Copyright (c) 2014 LG Electronics.
 * Created by Hyun Kook Khang on Jan 19 2014
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

import com.connectsdk.core.MediaInfo;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceSubscription;
import com.connectsdk.service.sessions.LaunchSession;

public interface MediaPlayer extends CapabilityMethods {
    public final static String Any = "MediaPlayer.Any";

    /**
     * This capability is deprecated. Use `MediaPlayer.Play_Video` instead.
     */
    @Deprecated
    public final static String Display_Video = "MediaPlayer.Play.Video";

    /**
     * This capability is deprecated. Use `MediaPlayer.Play_Audio` instead.
     */
    @Deprecated
    public final static String Display_Audio = "MediaPlayer.Play.Audio";

    public final static String Display_Image = "MediaPlayer.Display.Image";
    public final static String Play_Video = "MediaPlayer.Play.Video";
    public final static String Play_Audio = "MediaPlayer.Play.Audio";
    public final static String Play_Playlist = "MediaPlayer.Play.Playlist";
    public final static String Close = "MediaPlayer.Close";
    public final static String Loop = "MediaPlayer.Loop";
    public final static String Subtitle_SRT = "MediaPlayer.Subtitle.SRT";
    public final static String Subtitle_WebVTT = "MediaPlayer.Subtitle.WebVTT";

    public final static String MetaData_Title = "MediaPlayer.MetaData.Title";
    public final static String MetaData_Description = "MediaPlayer.MetaData.Description";
    public final static String MetaData_Thumbnail = "MediaPlayer.MetaData.Thumbnail";
    public final static String MetaData_MimeType = "MediaPlayer.MetaData.MimeType";

    public final static String MediaInfo_Get = "MediaPlayer.MediaInfo.Get";
    public final static String MediaInfo_Subscribe = "MediaPlayer.MediaInfo.Subscribe";

    public final static String[] Capabilities = {
        Display_Image,
        Play_Video,
        Play_Audio,
        Close,
        MetaData_Title,
        MetaData_Description,
        MetaData_Thumbnail,
        MetaData_MimeType,
        MediaInfo_Get,
        MediaInfo_Subscribe
    };

    public MediaPlayer getMediaPlayer();

    public CapabilityPriorityLevel getMediaPlayerCapabilityLevel();

    public void getMediaInfo(MediaInfoListener listener);

    public ServiceSubscription<MediaInfoListener> subscribeMediaInfo(MediaInfoListener listener);

    public void displayImage(MediaInfo mediaInfo, LaunchListener listener);

    public void playMedia(MediaInfo mediaInfo, boolean shouldLoop, LaunchListener listener);

    public void closeMedia(LaunchSession launchSession, ResponseListener<Object> listener);

    /**
     * This method is deprecated.
     * Use `MediaPlayer#displayImage(MediaInfo mediaInfo, LaunchListener listener)` instead.
     */
    @Deprecated
    public void displayImage(String url, String mimeType, String title, String description, String iconSrc, LaunchListener listener);

    /**
     * This method is deprecated.
     * Use `MediaPlayer#playMedia(MediaInfo mediaInfo, boolean shouldLoop, LaunchListener listener)`
     * instead.
     */
    @Deprecated
    public void playMedia(String url, String mimeType, String title, String description, String iconSrc, boolean shouldLoop, LaunchListener listener);

    /**
     * Success block that is called upon successfully playing/displaying a media file.
     *
     * Passes a MediaLaunchObject which contains the objects for controlling media playback.
     */
    public static interface LaunchListener extends ResponseListener<MediaLaunchObject> { }

    /**
     * Helper class used with the MediaPlayer.LaunchListener to return the current media playback.
     */
    public static class MediaLaunchObject {
        /** The LaunchSession object for the media launched. */
        public LaunchSession launchSession;
        /** The MediaControl object for the media launched. */
        public MediaControl mediaControl;
        /** The PlaylistControl object for the media launched */
        public PlaylistControl playlistControl;

        public MediaLaunchObject(LaunchSession launchSession, MediaControl mediaControl) {
            this.launchSession = launchSession;
            this.mediaControl = mediaControl;
        }

        public MediaLaunchObject(LaunchSession launchSession, MediaControl mediaControl, PlaylistControl playlistControl) {
            this.launchSession = launchSession;
            this.mediaControl = mediaControl;
            this.playlistControl = playlistControl;
        }
    }

    public static interface MediaInfoListener extends ResponseListener<com.connectsdk.core.MediaInfo> { }

}
