/*
 * TVControl
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

import com.connectsdk.core.ChannelInfo;
import com.connectsdk.core.ProgramInfo;
import com.connectsdk.core.ProgramList;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceSubscription;

public interface TVControl extends CapabilityMethods {
    public final static String Any = "TVControl.Any";

    public final static String Channel_Get = "TVControl.Channel.Get";
    public final static String Channel_Set = "TVControl.Channel.Set";
    public final static String Channel_Up = "TVControl.Channel.Up";
    public final static String Channel_Down = "TVControl.Channel.Down";
    public final static String Channel_List = "TVControl.Channel.List";
    public final static String Channel_Subscribe = "TVControl.Channel.Subscribe";
    public final static String Program_Get = "TVControl.Program.Get";
    public final static String Program_List = "TVControl.Program.List";
    public final static String Program_Subscribe = "TVControl.Program.Subscribe";
    public final static String Program_List_Subscribe = "TVControl.Program.List.Subscribe";
    public final static String Get_3D = "TVControl.3D.Get";
    public final static String Set_3D = "TVControl.3D.Set";
    public final static String Subscribe_3D = "TVControl.3D.Subscribe";

    public final static String[] Capabilities = {
        Channel_Get,
        Channel_Set,
        Channel_Up,
        Channel_Down,
        Channel_List,
        Channel_Subscribe,
        Program_Get,
        Program_List,
        Program_Subscribe,
        Program_List_Subscribe,
        Get_3D,
        Set_3D,
        Subscribe_3D
    };

    public TVControl getTVControl();
    public CapabilityPriorityLevel getTVControlCapabilityLevel();

    public void channelUp(ResponseListener<Object> listener);
    public void channelDown(ResponseListener<Object> listener);

    public void setChannel(ChannelInfo channelNumber, ResponseListener<Object> listener);

    public void getCurrentChannel(ChannelListener listener);
    public ServiceSubscription<ChannelListener> subscribeCurrentChannel(ChannelListener listener);

    public void getChannelList(ChannelListListener listener);

    public void getProgramInfo(ProgramInfoListener listener);
    public ServiceSubscription<ProgramInfoListener> subscribeProgramInfo(ProgramInfoListener listener);

    public void getProgramList(ProgramListListener listener);
    public ServiceSubscription<ProgramListListener> subscribeProgramList(ProgramListListener listener);

    public void get3DEnabled(State3DModeListener listener);
    public void set3DEnabled(boolean enabled, ResponseListener<Object> listener);
    public ServiceSubscription<State3DModeListener> subscribe3DEnabled(State3DModeListener listener);

    /**
     * Success block that is called upon successfully getting the TV's 3D mode
     *
     * Passes a Boolean to see Whether 3D mode is currently enabled on the TV
     */
    public static interface State3DModeListener extends ResponseListener<Boolean> { }

    /**
     * Success block that is called upon successfully getting the current channel's information.
     *
     * Passes a ChannelInfo object containing information about the current channel
     */
    public static interface ChannelListener extends ResponseListener<ChannelInfo>{ }

    /**
     * Success block that is called upon successfully getting the channel list.
     *
     * Passes a List of ChannelList objects for each available channel on the TV
     */
    public static interface ChannelListListener extends ResponseListener<List<ChannelInfo>>{ }

    /**
     * Success block that is called upon successfully getting the current program's information.
     *
     * Passes a ProgramInfo object containing information about the current program
     */
    public static interface ProgramInfoListener extends ResponseListener<ProgramInfo> { }

    /**
     * Success block that is called upon successfully getting the program list for the current channel.
     *
     * Passes a ProgramList containing a ProgramInfo object for each available program on the TV's current channel
     */
    public static interface ProgramListListener extends ResponseListener<ProgramList> { }
}
