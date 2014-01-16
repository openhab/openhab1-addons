/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.ccu;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openhab.binding.homematic.internal.device.physical.HMPhysicalDevice;

/**
 * This class caches instances of PhysicalDevice. Access to this cache will
 * usually be about a certain device address, so we use a java.util.Map that
 * maps from the address (String) to the device (PhysicalDevice).
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public class DeviceCache<T extends HMPhysicalDevice> {

    private final Logger log = Logger.getLogger(getClass().getName());

    private Map<String, T> addressMap;

    public DeviceCache() {
        addressMap = Collections.synchronizedMap(new HashMap<String, T>());
    }

    public T getDeviceByAddress(String address) {
        T dev = addressMap.get(address);
        if (dev != null) {
            log.finest("cache hit for device " + address);
        } else {
            log.finest("cache miss for device " + address);
        }
        return dev;
    }

    public Set<HMPhysicalDevice> getAllDevices() {
        return new HashSet<HMPhysicalDevice>(addressMap.values());
    }

    public Boolean isCached(String address) {
        return addressMap.containsKey(address);
    }

    public boolean addDevice(T device) {
        T cachedDev = addressMap.get(device.getAddress());

        if (cachedDev == null) {
            log.fine("adding device to cache: " + device.getAddress());
            return (addressMap.put(device.getAddress(), device) != null);

        } else if (device == cachedDev) {
            log.fine("device already in cache: " + device.getAddress());
            return true;

        } else {
            throw new RuntimeException("two different instances of the same device found!");
        }
    }

    public Integer addDevices(Set<? extends T> devices) {
        if (devices == null) {
            throw new IllegalArgumentException("devices must no be null");
        }

        int counter = 0;
        for (T dev : devices) {
            if (addDevice(dev)) {
                counter++;
            }
        }

        return counter;
    }

    public Integer clearCache() {
        log.warning("CLEARING CACHE!");
        Integer devices = addressMap.size();
        addressMap.clear();
        return devices;
    }

    public void removeDevice(String address) {
        if (address == null) {
            throw new IllegalArgumentException("address must no be null");
        }

        addressMap.remove(address);

        log.log(Level.INFO, "Removed device " + address);
    }

}
