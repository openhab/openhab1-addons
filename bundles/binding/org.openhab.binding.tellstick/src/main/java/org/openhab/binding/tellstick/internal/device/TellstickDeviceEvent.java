/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick.internal.device;

import org.openhab.binding.tellstick.internal.JNA.Method;

/**
 * A event received by callback and resent to listeners.
 * 
 * @author jarlebh
 * @since 1.5.0
 */
public class TellstickDeviceEvent {

	private TellstickDevice device;
	private Method method; // Look in JNA -TELLSTICK_TURNON and below
	private String data;

	public TellstickDeviceEvent(TellstickDevice device, Method method, String data) {
		super();
		this.device = device;
		this.method = method;
		this.data = data;
	}

	public TellstickDevice getDevice() {
		return device;
	}

	public Method getMethod() {
		return method;
	}

	public String getData() {
		return data;
	}

}
