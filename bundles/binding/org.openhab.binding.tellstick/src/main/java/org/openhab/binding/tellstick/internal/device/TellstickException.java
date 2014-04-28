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

import com.sun.jna.Pointer;

/**
 * An generic tellstick exception.
 * 
 * @author jarlebh
 * @since 1.5.0
 */
public class TellstickException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected TellstickDevice dev;

	protected int errorcode;

	public TellstickException(TellstickDevice dev, int errorcode) {
		super();
		this.dev = dev;
		this.errorcode = errorcode;
	}

	@Override
	public String getMessage() {
		Pointer errorP = JNA.CLibrary.INSTANCE.tdGetErrorString(errorcode);
		String error = errorP.getString(0);
		JNA.CLibrary.INSTANCE.tdReleaseString(errorP);
		return dev.getName() + ": " + error;
	}

}
