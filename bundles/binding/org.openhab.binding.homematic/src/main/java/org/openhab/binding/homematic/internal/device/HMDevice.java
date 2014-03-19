/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.device;

import org.openhab.binding.homematic.internal.xmlrpc.impl.DeviceDescription;
import org.openhab.binding.homematic.internal.xmlrpc.impl.ParamsetDescription;

/**
 * Interface of all homematic devices.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public interface HMDevice {

    public String getAddress();

    public DeviceDescription getDeviceDescription();

    public ParamsetDescription getMaster();

    public void setMaster(ParamsetDescription paramset);

}
