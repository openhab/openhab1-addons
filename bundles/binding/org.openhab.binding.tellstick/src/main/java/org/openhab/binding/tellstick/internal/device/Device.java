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
import org.openhab.binding.tellstick.internal.device.iface.DeviceIntf;

/**
 * Base class for a on/off devices.
 * 
 * @author jarlebh
 * @since 1.5.0
 */
public class Device extends TellstickDevice implements DeviceIntf {

	public Device(int deviceId) throws SupportedMethodsException {
		super(deviceId);
	}

	@Override
	public void on() throws TellstickException {
		int status = JNA.CLibrary.INSTANCE.tdTurnOn(getId());
		if (status != TELLSTICK_SUCCESS)
			throw new TellstickException(this, status);
	}

	@Override
	public void off() throws TellstickException {
		int status = JNA.CLibrary.INSTANCE.tdTurnOff(getId());
		if (status != TELLSTICK_SUCCESS)
			throw new TellstickException(this, status);
	}

	/**
	 * Returns true if latest command was turn on signal. This is a virtual
	 * 2-way communication, it does not really know if it's on. But it knows the
	 * latest command sent, so we can determine it this way.
	 * 
	 * @return true if device is on.
	 */
	public boolean isOn() {
		if ((JNA.CLibrary.TELLSTICK_TURNON & this.getStatus()) > 0)
			return true;
		else
			return false;
	}

	public String getType() {
		return "On/Off device";
	}
}
