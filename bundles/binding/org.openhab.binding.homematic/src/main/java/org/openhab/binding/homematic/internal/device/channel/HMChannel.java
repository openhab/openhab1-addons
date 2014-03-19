/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.device.channel;

import org.openhab.binding.homematic.internal.device.HMDevice;
import org.openhab.binding.homematic.internal.device.physical.HMPhysicalDevice;
import org.openhab.binding.homematic.internal.xmlrpc.impl.Paramset;
import org.openhab.binding.homematic.internal.xmlrpc.impl.ParamsetDescription;

/**
 * Channels are logical devices that are part of a physical device. Channels
 * know the XmlRpcConnection to their CCU. Channels must be able to communicate
 * with the CCU through that connection so they can query the current status
 * information and change it when necessary.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public interface HMChannel extends HMDevice {

    public HMPhysicalDevice getParent();

    public void updateProperty(String parameterKey, Object value);

    public ParamsetDescription getValuesDescription();

    public Paramset getValues();

    public void setValues(Paramset paramset);

    public void setValue(String key, Object value);

}
