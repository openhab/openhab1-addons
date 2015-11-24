/*
 * DiscoveryProvider
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

package com.connectsdk.discovery;

import java.util.List;


/**
 * ###Overview
 * From a high-level perspective, DiscoveryProvider completely abstracts the functionality of discovering services of a particular protocol (SSDP, Cast, etc). The DiscoveryProvider will pass service information to the DiscoveryManager, which will then create a DeviceService object and attach it to a ConnectableDevice object.
 *
 * ###In Depth
 * DiscoveryProvider is an abstract class that is meant to be extended. You shouldn't ever use DiscoveryProvider directly, unless extending it to provide support for another discovery protocol.
 *
 * By default, DiscoveryManager will set itself as a DiscoveryProvider's listener. You should not change the listener as it could cause unexpected inconsistencies within the discovery process.
 *
 * See CastDiscoveryProvider and SSDPDiscoveryProvider for implementations.
 */
public interface DiscoveryProvider {
    public final static int RESCAN_INTERVAL = 10000;
    public final static int RESCAN_ATTEMPTS = 6;
    public final static int TIMEOUT = RESCAN_INTERVAL * RESCAN_ATTEMPTS;

    /**
     * Starts the DiscoveryProvider.
     */
    public void start();

    /**
     * Stops the DiscoveryProvider.
     */
    public void stop();

    /**
     * Restarts the DiscoveryProvider.
     */
    public void restart();

    /**
     * Sends out discovery query without a full restart
     */
    public void rescan();

    /**
     * Resets the DiscoveryProvider.
     */
    public void reset();

    /** Adds a DiscoveryProviderListener, which should be the DiscoveryManager */
    public void addListener(DiscoveryProviderListener listener);

    /** Removes a DiscoveryProviderListener. */
    public void removeListener(DiscoveryProviderListener listener);

    /**
     * Adds a device filter for a particular DeviceService.
     *
     * @param filter filter to be used for discovering a particular DeviceService
     */
    public void addDeviceFilter(DiscoveryFilter filter);

    /**
     * Removes a device filter for a particular DeviceService. If the DiscoveryProvider has no other devices to be searching for, the DiscoveryProvider will be stopped and de-referenced.
     *
     * @param filter filter to be used for discovering a particular DeviceService
     */
    public void removeDeviceFilter(DiscoveryFilter filter);

    /**
     * Set filters for a list of particular DeviceServices
     *
     * @param filters filters to be used for discovering a list of particular DeviceServices
     */
    public void setFilters(List<DiscoveryFilter> filters);

    /**
     * Whether or not the DiscoveryProvider has any services it is supposed to be searching for. If YES, then the DiscoveryProvider will be stopped and de-referenced by the DiscoveryManager.
     */
    public boolean isEmpty();
}
