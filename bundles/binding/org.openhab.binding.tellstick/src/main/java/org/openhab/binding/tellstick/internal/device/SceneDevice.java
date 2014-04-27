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
 * A Scene.
 * 
 * @author jarlebh
 * @since 1.5.0
 */
public class SceneDevice extends TellstickDevice {

	public SceneDevice(int deviceId) throws SupportedMethodsException {
		super(deviceId);
	}

	/**
	 * Executes Scene.
	 * 
	 * @throws TellstickException
	 */
	public void execute() throws TellstickException {
		int status = JNA.CLibrary.INSTANCE.tdExecute(getId());
		if (status != TELLSTICK_SUCCESS)
			throw new TellstickException(this, status);
	}

	public String getType() {
		return "Scene";
	}
}
