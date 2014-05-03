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

/**
 * Up / Down devices can be such devices as Projector screens.
 * 
 * @author jarlebh
 * @author peec
 * @since 1.5.0
 * 
 */
public class UpDownDevice extends TellstickDevice {

	public UpDownDevice(int deviceId) throws SupportedMethodsException {
		super(deviceId);
	}

	/**
	 * Sends up command.
	 * 
	 * @throws TellstickException
	 */
	public void up() throws TellstickException {
		int status = JNA.CLibrary.INSTANCE.tdUp(getId());
		if (status != TELLSTICK_SUCCESS)
			throw new TellstickException(this, status);
	}

	/**
	 * Sends down command.
	 * 
	 * @throws TellstickException
	 */
	public void down() throws TellstickException {
		int status = JNA.CLibrary.INSTANCE.tdDown(getId());
		if (status != TELLSTICK_SUCCESS)
			throw new TellstickException(this, status);
	}

	/**
	 * Stops execution.
	 * 
	 * @throws TellstickException
	 */
	public void stop() throws TellstickException {
		int status = JNA.CLibrary.INSTANCE.tdStop(getId());
		if (status != TELLSTICK_SUCCESS)
			throw new TellstickException(this, status);
	}

	public String getType() {
		return "Projector Screen";
	}

}
