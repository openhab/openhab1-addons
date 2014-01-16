/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.device.physical;

import java.util.List;

import org.openhab.binding.homematic.internal.ccu.CCU;
import org.openhab.binding.homematic.internal.device.HMDevice;
import org.openhab.binding.homematic.internal.device.channel.HMChannel;
import org.openhab.binding.homematic.internal.device.channel.HMMaintenanceChannel;

/**
 * PhysicalDevices have Channels (which again are Devices) as children and
 * provide access to those Channels. Further PhysicalDevices must know the
 * XmlRpcConnection to reach their owning CCU.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public interface HMPhysicalDevice extends HMDevice {

    public CCU<? extends HMPhysicalDevice> getCCU();

    public String getFirmware();

    public Integer getVersion();

    public Integer getChannelCount();

    public HMChannel getChannel(Integer index);

    public List<HMChannel> getChannels();

    public HMMaintenanceChannel getMaintenanceChannel();

}
