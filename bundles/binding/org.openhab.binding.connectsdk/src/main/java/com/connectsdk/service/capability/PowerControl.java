/*
 * PowerControl
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

public interface PowerControl extends CapabilityMethods {
    public final static String Any = "PowerControl.Any";

    public final static String Off = "PowerControl.Off";
    public final static String On = "PowerControl.On";

    public final static String[] Capabilities = {
        Off,
        On
    };

    public PowerControl getPowerControl();
    public CapabilityPriorityLevel getPowerControlCapabilityLevel();

    public void powerOff(ResponseListener<Object> listener);
    public void powerOn(ResponseListener<Object> listener);
}
