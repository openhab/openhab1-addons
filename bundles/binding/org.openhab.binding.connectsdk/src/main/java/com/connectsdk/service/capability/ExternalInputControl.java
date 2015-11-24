/*
 * ExternalInputControl
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

import com.connectsdk.core.ExternalInputInfo;
import com.connectsdk.service.capability.Launcher.AppLaunchListener;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.sessions.LaunchSession;

public interface ExternalInputControl extends CapabilityMethods {
    public final static String Any = "ExternalInputControl.Any";

    public final static String Picker_Launch = "ExternalInputControl.Picker.Launch";
    public final static String Picker_Close = "ExternalInputControl.Picker.Close";
    public final static String List = "ExternalInputControl.List";
    public final static String Set = "ExternalInputControl.Set";

    public final static String[] Capabilities = {
        Picker_Launch,
        Picker_Close,
        List,
        Set
    };

    public ExternalInputControl getExternalInput();
    public CapabilityPriorityLevel getExternalInputControlPriorityLevel();

    public void launchInputPicker(AppLaunchListener listener);
    public void closeInputPicker(LaunchSession launchSessionm, ResponseListener<Object> listener);

    public void getExternalInputList(ExternalInputListListener listener);
    public void setExternalInput(ExternalInputInfo input, ResponseListener<Object> listener);

    /**
     * Success block that is called upon successfully getting the external input list.
     *
     * Passes a list containing an ExternalInputInfo object for each available external input on the device
     */
    public interface ExternalInputListListener extends ResponseListener<List<ExternalInputInfo>> { }
}
