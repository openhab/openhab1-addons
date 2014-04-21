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
import org.openhab.binding.tellstick.internal.device.iface.DimmableDeviceIntf;

/**
 * A dimmable tellstick device.
 * 
 * @author jarlebh
 * @since 1.5.0
 */
public class DimmableDevice extends Device implements DimmableDeviceIntf {

	public DimmableDevice(int deviceId) throws SupportedMethodsException {
		super(deviceId);
	}

	/**
	 * Dims the lights.
	 * 
	 * @throws TellstickException
	 * @throws IllegalArguementException
	 */
	@Override
	public void dim(int level) throws TellstickException {
		if (level < 0 || level > 255)
			throw new IllegalArgumentException("Dim levels must be between 0 and 255.");
		int status = JNA.CLibrary.INSTANCE.tdDim(getId(), level);
		if (status != TELLSTICK_SUCCESS)
			throw new TellstickException(this, status);
	}

	/**
	 * Since Dimmers can be dimmed, we override the Device::isOn. This checks if
	 * the device is turned on.
	 * 
	 * @return true if device is on. false otherwise.
	 */
	@Override
	public boolean isOn() {
		if (super.isOn() || (JNA.CLibrary.TELLSTICK_DIM & this.getStatus()) > 0)
			return true;
		else
			return false;
	}

	@Override
	public String getType() {
		return "Dimmer Device";
	}

}
