/*
 * CapabilityFilter
 * Connect SDK
 * 
 * Copyright (c) 2014 LG Electronics.
 * Created by Hyun Kook Khang on 01 Feb 2014
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

package com.connectsdk.discovery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CapabilityFilter is an object that wraps a List of required capabilities. This CapabilityFilter is used for determining which devices will appear in DiscoveryManager's compatibleDevices array. The contents of a CapabilityFilter's array must be any of the string constants defined in the Capability Class constants.
 *
 * ###CapabilityFilter values
 * Here are some examples of values for the Capability constants.
 *
 * - MediaPlayer.Display_Video = "MediaPlayer.Display.Video"
 * - MediaPlayer.Display_Image = "MediaPlayer.Display.Image"
 * - VolumeControl.Volume_Subscribe = "VolumeControl.Subscribe"
 * - MediaControl.Any = "MediaControl.Any"
 *
 * All Capability header files also define a constant array of all capabilities defined in that header (ex. kVolumeControlCapabilities).
 *
 * ###AND/OR Filtering
 * CapabilityFilter is an AND filter. A ConnectableDevice would need to satisfy all conditions of a CapabilityFilter to pass.
 *
 * The DiscoveryManager capabilityFilters is an OR filter. a ConnectableDevice only needs to satisfy one condition (CapabilityFilter) to pass.
 *
 * ###Examples
 * Filter for all devices that support video playback AND any media controls AND volume up/down.
 *
@code
    List<String> capabilities = new ArrayList<String>();
        capabilities.add(MediaPlayer.Display_Video);
        capabilities.add(MediaControl.Any);
        capabilities.add(VolumeControl.Volume_Up_Down);

    CapabilityFilter filter = 
            new CapabilityFilter(capabilities);

    DiscoveryManager.getInstance().setCapabilityFilters(filter);
@endcode
 *
 * Filter for all devices that support (video playback AND any media controls AND volume up/down) OR (image display).
 *
@code
    CapabilityFilter videoFilter = 
            new CapabilityFilter(
                    MediaPlayer.Display_Video, 
                    MediaControl.Any, 
                    VolumeControl.Volume_Up_Down);

    CapabilityFilter imageFilter = 
            new CapabilityFilter(
                    MediaPlayer.Display_Image);

    DiscoveryManager.getInstance().setCapabilityFilters(videoFilter, imageFilter);
@endcode
 */
public class CapabilityFilter {

    /**
     * List of capabilities required by this filter. This property is readonly -- use the addCapability or addCapabilities to build this object.
     */
    public List<String> capabilities = new ArrayList<String>();

    /**
     * Create an empty CapabilityFilter.
     */
    public CapabilityFilter() {
    }

    /**
     * Create a CapabilityFilter with the given array of required capabilities.
     * 
     * @param capabilities Capabilities to be added to the new filter
     */
    public CapabilityFilter(String ... capabilities) {
        for (String capability : capabilities) {
            addCapability(capability);
        }
    }

    /**
     * Create a CapabilityFilter with the given array of required capabilities.
     * 
     * @param capabilities List of capability names (see capability class files for String constants)
     */
    public CapabilityFilter(List<String> capabilities) {
        addCapabilities(capabilities);
    }

    /**
     * Add a required capability to the filter.
     *
     * @param capability Capability name to add (see capability class files for String constants)
     */
    public void addCapability(String capability) {
        capabilities.add(capability);
    }

    /**
     * Add array of required capabilities to the filter. (see capability class files for String constants)
     *
     * @param capabilities List of capability names 
     */
    public void addCapabilities(List<String> capabilities) {
        this.capabilities.addAll(capabilities);
    }

    /**
     * Add array of required capabilities to the filter. (see capability classes files for String constants)
     *
     * @param capabilities String[] of capability names 
     */
    public void addCapabilities(String... capabilities) {
        Collections.addAll(this.capabilities, capabilities);
    }
}
