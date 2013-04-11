/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
 * 
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
