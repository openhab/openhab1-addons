/*
 * VirtualKeycodes
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

package com.connectsdk.service.netcast;

public enum NetcastVirtualKeycodes {
    // reference for code number (http://developer.lge.com/resource/tv/RetrieveDocDevLibrary.dev, search "Annex A URL Encoding Reference")
    POWER (1),
    NUMBER_0 (2),
    NUMBER_1 (3),
    NUMBER_2 (4),
    NUMBER_3 (5),
    NUMBER_4 (6),
    NUMBER_5 (7),
    NUMBER_6 (8),
    NUMBER_7 (9),
    NUMBER_8 (10),
    NUMBER_9 (11),

    KEY_UP (12),
    KEY_DOWN (13),
    KEY_LEFT (14),
    KEY_RIGHT (15),

    OK (20),
    HOME (21),
    MENU (22),
    BACK (23),      // PREVIOUS_KEY

    VOLUME_UP (24),
    VOLUME_DOWN (25),
    MUTE (26),

    CHANNEL_UP (27),
    CHANNEL_DOWN (28),

    BLUE (29),
    GREEN (30),
    RED (31),
    YELLOW (32),

    PLAY (33),
    PAUSE (34),
    STOP (35),
    FAST_FORWARD (36),
    REWIND (37),
    SKIP_FORWARD (38),
    SKIP_BACKWARD (39),
    RECORD (40),
    RECORDING_LIST (41),
    REPEAT (42),
    LIVE_TV (43),
    EPG (44),
    CURRENT_PROGRAM_INFO (45),

    ASPECT_RATIO (46),
    EXTERNAL_INPUT (47),
    PIP_SECONDARY_VIDEO (48),
    SHOW_CHANGE_SUBTITLE (49),
    PROGRAM_LIST (50),

    TELE_TEXT (51),
    MARK (52),

    VIDEO_3D (400),
    AUDIO_3D_L_R (401),

    DASH (402),
    PREVIOUS_CHANNEL (403),         // FLASH BACK
    FAVORITE_CHANNEL (404),

    QUICK_MENU (405),
    TEXT_OPTION (406),
    AUDIO_DESCRIPTION (407),
    NETCAST_KEY (408),      // SAME WITH HOME MENU
    ENERGY_SAVING (409),
    AV_MODE (410),
    SIMPLINK (411),
    EXIT (412),
    RESERVATION_PROGRAM_LIST (413),

    PIP_CHANNEL_UP (414),
    PIP_CHANNEL_DOWN (415),
    SWITCHING_PRIMARY_SECONDARY_VIDEO (416),
    MY_APPS (417);

    private final int code;

    private NetcastVirtualKeycodes (int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
