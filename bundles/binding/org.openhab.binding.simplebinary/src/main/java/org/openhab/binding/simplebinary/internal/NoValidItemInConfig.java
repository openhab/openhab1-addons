/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.simplebinary.internal;

/**
 * Exception class for item not found in configuration (device or address)
 * 
 * @author Vita Tucek
 * @since 1.8.0
 */

public class NoValidItemInConfig extends Exception {
	private static final long serialVersionUID = 4901413826466898609L;

	public NoValidItemInConfig() {
		super("Item not found in configuration");
	}

	public NoValidItemInConfig(String msg) {
		super(msg);
	}

	public NoValidItemInConfig(String deviceName, int devId, int address) {
		super("Item was not found in configuration (device address=" + devId + "; item address=" + address);
	}
}
