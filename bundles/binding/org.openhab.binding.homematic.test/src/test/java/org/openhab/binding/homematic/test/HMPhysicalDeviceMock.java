/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.homematic.internal.ccu.CCU;
import org.openhab.binding.homematic.internal.device.channel.HMChannel;
import org.openhab.binding.homematic.internal.device.channel.HMMaintenanceChannel;
import org.openhab.binding.homematic.internal.device.physical.HMPhysicalDevice;
import org.openhab.binding.homematic.internal.xmlrpc.impl.DeviceDescription;
import org.openhab.binding.homematic.internal.xmlrpc.impl.ParamsetDescription;

public class HMPhysicalDeviceMock implements HMPhysicalDevice {

    private ParamsetDescription master;
    private HMChannelMock channel = new HMChannelMock();
    private DeviceDescription deviceDescription;

    public HMPhysicalDeviceMock() {
        Map<String, Object> values = new HashMap<String, Object>();
        deviceDescription = new DeviceDescription(values);
    }

    public String getAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    public DeviceDescription getDeviceDescription() {
        return deviceDescription;
    }

    public ParamsetDescription getMaster() {
        return master;
    }

    public void setMaster(ParamsetDescription ParamsetDescription) {
        this.master = ParamsetDescription;
    }

    public void sync() {
        // TODO Auto-generated method stub

    }

    public CCU<? extends HMPhysicalDevice> getCCU() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getFirmware() {
        // TODO Auto-generated method stub
        return null;
    }

    public Integer getVersion() {
        // TODO Auto-generated method stub
        return null;
    }

    public Integer getChannelCount() {
        // TODO Auto-generated method stub
        return null;
    }

    public HMChannel getChannel(Integer index) {
        return channel;
    }

    public List<HMChannel> getChannels() {
        // TODO Auto-generated method stub
        return null;
    }

    public HMMaintenanceChannel getMaintenanceChannel() {
        // TODO Auto-generated method stub
        return null;
    }

}
