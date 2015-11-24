/*
 * TextInputControl
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

import com.connectsdk.core.TextInputStatusInfo;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceSubscription;

public interface TextInputControl extends CapabilityMethods {
    public final static String Any = "TextInputControl.Any";

    public final static String Send = "TextInputControl.Send";
    public final static String Send_Enter = "TextInputControl.Enter";
    public final static String Send_Delete = "TextInputControl.Delete";
    public final static String Subscribe = "TextInputControl.Subscribe";

    public final static String[] Capabilities = {
        Send,
        Send_Enter,
        Send_Delete,
        Subscribe
    };

    public TextInputControl getTextInputControl();
    public CapabilityPriorityLevel getTextInputControlCapabilityLevel();

    public ServiceSubscription<TextInputStatusListener> subscribeTextInputStatus(TextInputStatusListener listener);

    public void sendText(String input);
    public void sendEnter();
    public void sendDelete();

    /**
     * Response block that is fired on any change of keyboard visibility.
     *
     * Passes TextInputStatusInfo object that provides keyboard type & visibility information
     */
    public static interface TextInputStatusListener extends ResponseListener<TextInputStatusInfo> { }
}
