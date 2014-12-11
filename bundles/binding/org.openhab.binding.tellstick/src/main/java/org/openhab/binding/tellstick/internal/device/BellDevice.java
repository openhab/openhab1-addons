/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick.internal.device;

import org.openhab.binding.tellstick.internal.JNA;
import org.openhab.binding.tellstick.internal.device.iface.BellDeviceIntf;

/**
 * A Bell.
 * 
 * @author jarlebh
 * @since 1.5.0
 */
public class BellDevice extends TellstickDevice implements BellDeviceIntf {

	public BellDevice(int deviceId) throws SupportedMethodsException {
		super(deviceId);
	}

	@Override
	public void bell() throws TellstickException {
		int status = JNA.CLibrary.INSTANCE.tdBell(getId());
		if (status != TELLSTICK_SUCCESS)
			throw new TellstickException(this, status);
	}

	public String getType() {
		return "Bell Device";
	}

}
