/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.device.physical.rf;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.homematic.internal.ccu.CCURF;
import org.openhab.binding.homematic.internal.device.channel.DefaultChannel;
import org.openhab.binding.homematic.internal.device.channel.HMChannel;
import org.openhab.binding.homematic.internal.device.channel.HMMaintenanceChannel;
import org.openhab.binding.homematic.internal.xmlrpc.impl.DeviceDescription;

/**
 * Default implementation of a HM RF device.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class DefaultHMRFDevice extends HMRFDevice {

    private Integer channelCount;
    private List<HMChannel> channels;

    public DefaultHMRFDevice(CCURF ccu, String address) {
        super(ccu.getConnection().getDeviceDescription(address), ccu);
        channels = new ArrayList<HMChannel>();
        initChildren(ccu);
    }

    private void initChildren(CCURF ccu) {
        if (getDeviceDescription().getChildren() != null) {
            for (String child : getDeviceDescription().getChildren()) {
                DeviceDescription childDevDesc = ccu.getConnection().getDeviceDescription(child);
                channels.add(new DefaultChannel(this, childDevDesc));
            }
        }
    }

    @Override
    public Integer getChannelCount() {
        return channelCount;
    }

    @Override
    public HMChannel getChannel(Integer index) {
        return channels.get(index);
    }

    @Override
    public List<HMChannel> getChannels() {
        return channels;
    }

    @Override
    public HMMaintenanceChannel getMaintenanceChannel() {
        for (HMChannel channel : channels) {
            if (channel instanceof HMMaintenanceChannel) {
                return (HMMaintenanceChannel) channel;
            }
        }
        return null;
    }

}
