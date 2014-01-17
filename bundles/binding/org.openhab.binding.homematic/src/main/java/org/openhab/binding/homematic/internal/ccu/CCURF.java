/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.ccu;

import java.util.HashSet;
import java.util.Set;

import org.openhab.binding.homematic.internal.device.HMDeviceFactory;
import org.openhab.binding.homematic.internal.device.channel.HMChannel;
import org.openhab.binding.homematic.internal.device.physical.HMPhysicalDevice;
import org.openhab.binding.homematic.internal.device.physical.rf.HMRFDevice;
import org.openhab.binding.homematic.internal.xmlrpc.XmlRpcConnectionRF;
import org.openhab.binding.homematic.internal.xmlrpc.callback.CallbackReceiver;
import org.openhab.binding.homematic.internal.xmlrpc.impl.DeviceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the functionality of a CCU object. The main purpose of
 * this class is to provide cached access to objects representing Homematic
 * devices. The implementation is cached because a cache is used to store
 * instances of devices. Due to this caching two consecutive calls of
 * getPhysicalDevice(String addr) with the same value for the addr parameter
 * return the _same_ object. This is very important for delivery of status
 * changes. Let's say there is a GUI component displaying the value of a
 * temperature sensor. The sensor senses a different temperature which will
 * result in a XML-RPC request from the CCU to the CallbackHandler and finally
 * to an instance of CCU. The CCU object now has to deliver this changed value
 * to the appropriate object for the GUI component to update. If device access
 * methods below returned not the exact same objects every time this was not
 * possible.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public class CCURF extends AbstractCCU<HMRFDevice> implements CallbackReceiver {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private XmlRpcConnectionRF connection;

    private DeviceCache<HMRFDevice> cache;

    /**
     * Creates a new instance of CCURF (CCU instance for RF (remote frequency)
     * devices).
     * 
     * @param connection
     *            The XML-RPC connection to the CCU.
     */
    public CCURF(XmlRpcConnectionRF connection) {
        this.connection = connection;
        cache = new DeviceCache<HMRFDevice>();
        logger.info("Connected to CCU at " + connection.getAddress() + ":" + connection.getPort());
    }

    /**
     * Returns the XML-RPC connection used by this CCU.
     */
    @Override
    public XmlRpcConnectionRF getConnection() {
        return connection;
    }

    /**
     * Retrieves all physical devices from the real CCU. As this instance is
     * dedicated for RF devices, this method returns instances of HMRFDevice.
     * The retrieved instances are cached after retrieval or retrieved from the
     * cache if loaded before.
     */
    @Override
    public Set<HMRFDevice> getPhysicalDevices() {
        logger.debug("listing all physical devices");

        Set<DeviceDescription> descriptions = connection.listDevices();
        logger.debug("retrieved " + descriptions.size() + " device descriptions.");

        Set<HMRFDevice> devices = new HashSet<HMRFDevice>();

        for (DeviceDescription descr : descriptions) {
            String address = descr.getAddress();

            if (cache.isCached(address)) {
                devices.add(cache.getDeviceByAddress(address));

            } else {
                HMRFDevice dev = HMDeviceFactory.createRFDevice(this, address);
                if (dev != null) {
                    devices.add(dev);
                } else {
                    logger.warn("unknown device: " + descr.getType());
                }
            }
        }

        cache.addDevices(devices);
        return devices;
    }

    /**
     * Retrieves an instance of HMRFDevice by a given address. Null is returned
     * if no matching device can be found. The device cache is consulted first
     * and the cached device is returned if available.
     * 
     * Synchronized because of cache maintainance
     */
    @Override
    public synchronized HMRFDevice getPhysicalDevice(String address) {
        if (address == null) {
            throw new IllegalArgumentException("address must not be null");
        }

        logger.debug("listing physical device with address " + address);

        HMRFDevice dev = cache.getDeviceByAddress(address);

        if (dev == null) {
            logger.debug("device " + address + " not found in cache - trying to load ...");
            dev = HMDeviceFactory.createRFDevice(this, address);

            if (dev != null) {
                cache.addDevice(dev);
                logger.debug("could load device " + address + " and added to cache");
            } else {
                logger.error("Could not load device " + address);
            }
        }

        return dev;
    }

    /**
     * Allows to retrieve physical devices by type. Again, the cache is
     * consulted first.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends HMRFDevice> Set<T> getPhysicalDevices(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }

        logger.debug("listing all physical devices of type " + clazz.getCanonicalName());

        Set<HMRFDevice> allDevices = getPhysicalDevices();

        Set<T> result = new HashSet<T>();
        for (HMPhysicalDevice device : allDevices) {
            if (device.getClass().isAssignableFrom(clazz)) {
                result.add((T) device);
            }
        }

        cache.addDevices(allDevices);
        return result;
    }

    /*
     * CallbackReceiver Methods
     */

    @Override
    public Integer deleteDevices(String interfaceId, Object[] addresses) {
        String[] addrs = new String[addresses.length];
        System.arraycopy(addresses, 0, addrs, 0, addresses.length);

        for (String addr : addrs) {
            HMRFDevice dev = null;
            // Only continue if device is cached. Otherwise it was never used
            // and deletion of it is ignored
            if (cache.isCached(addr)) {
                dev = getPhysicalDevice(addr);
                cache.removeDevice(addr);
                this.fireDeviceRemoved(dev);
            }
        }

        return null;
    }

    @Override
    public Integer event(String interfaceId, String address, String valueKey, Object value) {
        logger.debug("received callback event " + interfaceId + " for device " + address + "#" + valueKey + " with value " + value);

        String[] addrComponents = address.split(":");
        if (addrComponents.length != 2) {
            return null;
        }
        String phyDevAddr = addrComponents[0];
        Integer channelNum = Integer.parseInt(addrComponents[1]);

        HMPhysicalDevice dev = cache.getDeviceByAddress(phyDevAddr);

        if (dev != null) {
            logger.debug("updating device " + dev.getAddress());
            HMChannel ch = dev.getChannel(channelNum);
            if (ch != null) {
                ch.updateProperty(valueKey, value);
            }
        }

        return null;
    }

    @Override
    public Object[] listDevices(String interfaceId) {
        logger.debug("called list devices");

        Set<HMPhysicalDevice> devices = cache.getAllDevices();

        Set<String> deviceDescriptions = new HashSet<String>();

        for (HMPhysicalDevice device : devices) {
            deviceDescriptions.add(device.getDeviceDescription().getAddress());
        }

        Object[] result = deviceDescriptions.toArray(new Object[0]);

        return result;
    }

    @Override
    public Integer newDevices(String interfaceId, Object[] deviceDescriptions) {
        logger.debug("called newDevices: " + deviceDescriptions);
        return null;
    }

    @Override
    public Integer updateDevice(String interfaceId, String address, Integer hint) {
        logger.debug("called updateDevice: " + address + " " + hint);
        return null;
    }

}
