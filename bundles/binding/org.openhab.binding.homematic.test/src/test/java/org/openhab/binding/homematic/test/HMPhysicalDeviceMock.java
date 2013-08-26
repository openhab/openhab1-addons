/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
