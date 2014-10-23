/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcul.internal.messages;

/**
 * Define Thermostat control modes
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public enum ThermostatControlMode {
	AUTO(0x0), MANUAL(0x1), TEMPORARY(0x2), BOOST(0x3);

	private final int controlMode;

	ThermostatControlMode(int mode) {
		this.controlMode = mode;
	}

	ThermostatControlMode(byte mode) {
		this.controlMode = mode;
	}

	public byte toByte() {
		return (byte) controlMode;
	}

	public int toInt() {
		return controlMode;
	}
}
