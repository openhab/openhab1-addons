/*
 * MediaControl
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

import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceSubscription;

public interface MediaControl extends CapabilityMethods {
    public final static String Any = "MediaControl.Any";

    public final static String Play = "MediaControl.Play";
    public final static String Pause = "MediaControl.Pause";
    public final static String Stop = "MediaControl.Stop";
    public final static String Rewind = "MediaControl.Rewind";
    public final static String FastForward = "MediaControl.FastForward";
    public final static String Seek = "MediaControl.Seek";
    public final static String Duration = "MediaControl.Duration";
    public final static String PlayState = "MediaControl.PlayState";
    public final static String PlayState_Subscribe = "MediaControl.PlayState.Subscribe";
    public final static String Position = "MediaControl.Position";

    /**
     * This capability is deprecated. Use `PlaylistControl.Previous` instead.
     */
    @Deprecated
    public final static String Previous = "MediaControl.Previous";

    /**
     * This capability is deprecated. Use `PlaylistControl.Next` instead.
     */
    @Deprecated
    public final static String Next = "MediaControl.Next";


    public static final int PLAYER_STATE_UNKNOWN = 0;
    public static final int PLAYER_STATE_IDLE = 1;  
    public static final int PLAYER_STATE_PLAYING = 2;
    public static final int PLAYER_STATE_PAUSED = 3;
    public static final int PLAYER_STATE_BUFFERING = 4;


    public final static String[] Capabilities = {
        Play,
        Pause,
        Stop,
        Rewind,
        FastForward,
        Seek,
        Previous,
        Next,
        Duration,
        PlayState,
        PlayState_Subscribe,
        Position,
    };

    /**
     * Enumerates possible playback status
     */
    public enum PlayStateStatus {
        /**
         * Unknown state
         */
        Unknown,

        /**
         * Media source is not set.
         */
        Idle,

        /**
         * Media is playing.
         */
        Playing,

        /**
         * Media is paused.
         */
        Paused,

        /**
         * Media is buffering on the first screen device (e.g. on the TV)
         */
        Buffering,

        /**
         * Playback is finished.
         */
        Finished;

        /**
         * Converts int value into PlayStateStatus
         * @param playerState int value
         * @return PlayStateStatus
         */
        public static PlayStateStatus convertPlayerStateToPlayStateStatus(int playerState) {
            PlayStateStatus status = PlayStateStatus.Unknown;

            switch (playerState) {
            case PLAYER_STATE_BUFFERING:
                status = PlayStateStatus.Buffering;
                break;
            case PLAYER_STATE_IDLE:
                status = PlayStateStatus.Finished;
                break;
            case PLAYER_STATE_PAUSED:
                status = PlayStateStatus.Paused;
                break;
            case PLAYER_STATE_PLAYING:
                status = PlayStateStatus.Playing;
                break;
            case PLAYER_STATE_UNKNOWN:
            default:
                status = PlayStateStatus.Unknown;
                break;
            }

            return status;
        }

        /**
         * Converts String value into PlayStateStatus
         * @param transportState String value
         * @return PlayStateStatus
         */
        public static PlayStateStatus convertTransportStateToPlayStateStatus(String transportState) {
            PlayStateStatus status = PlayStateStatus.Unknown;

            if (transportState.equals("STOPPED")) {
                status = PlayStateStatus.Finished;
            }
            else if (transportState.equals("PLAYING")) {
                status = PlayStateStatus.Playing;
            }
            else if (transportState.equals("TRANSITIONING")) {
                status = PlayStateStatus.Buffering;
            }
            else if (transportState.equals("PAUSED_PLAYBACK")) {
                status = PlayStateStatus.Paused; 
            }
            else if (transportState.equals("PAUSED_RECORDING")) {

            }
            else if (transportState.equals("RECORDING")) {

            }
            else if (transportState.equals("NO_MEDIA_PRESENT")) {

            }
            return status;
        }
    }

    /**
     * Get MediaControl implementation
     * @return MediaControl
     */
    public MediaControl getMediaControl();

    /**
     * Get a capability priority for current implementation
     * @return CapabilityPriorityLevel
     */
    public CapabilityPriorityLevel getMediaControlCapabilityLevel();

    public void play(ResponseListener<Object> listener);

    public void pause(ResponseListener<Object> listener);

    public void stop(ResponseListener<Object> listener);

    public void rewind(ResponseListener<Object> listener);

    public void fastForward(ResponseListener<Object> listener);

    /**
     * This method is deprecated.
     * Use `PlaylistControl#previous(ResponseListener<Object> listener)` instead.
     */
    @Deprecated
    public void previous(ResponseListener<Object> listener);

    /**
     * This method is deprecated.
     * Use `PlaylistControl#next(ResponseListener<Object> listener)` instead.
     */
    @Deprecated
    public void next(ResponseListener<Object> listener);

    /**
     * @param position The new position, in milliseconds from the beginning of the stream
     * @param listener (optional) ResponseListener< Object > with methods to be called on success
     *                 or failure
     */
    public void seek(long position, ResponseListener<Object> listener);

    /**
     * Get the current media duration in milliseconds
     */
    public void getDuration(DurationListener listener);

    /**
     * Get the current playback position in milliseconds
     */
    public void getPosition(PositionListener listener);

    /**
     * Get the current state of playback
     */
    public void getPlayState(PlayStateListener listener);

    /**
     * Subscribe for playback state changes
     * @param listener receives play state notifications
     * @return ServiceSubscription<PlayStateListener>
     */
    public ServiceSubscription<PlayStateListener> subscribePlayState(PlayStateListener listener);

    /**
     * Success block that is called upon any change in a media file's play state.
     *
     * Passes a PlayStateStatus enum of the current media file
     */
    public static interface PlayStateListener extends ResponseListener<PlayStateStatus> { }

    /**
     * Success block that is called upon successfully getting the media file's current playhead position.
     *
     * Passes the position of the current playhead position of the current media file, in seconds
     */
    public static interface PositionListener extends ResponseListener<Long> { }

    /**
     * Success block that is called upon successfully getting the media file's duration.
     *
     * Passes the duration of the current media file, in seconds
     */
    public static interface DurationListener extends ResponseListener<Long> { }
}
