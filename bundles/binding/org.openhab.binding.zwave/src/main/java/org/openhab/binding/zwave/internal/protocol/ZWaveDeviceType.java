/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol;

/**
 * enum defining the different ZWave node types
 * 
 * @author Chris Jackson
 * @since 1.5
 */
public enum ZWaveDeviceType {
	UNKNOWN("Unknown"), 
	SLAVE("Slave"), 
	PRIMARY("Primary Controller"), 
	SECONDARY("Secondary Controller"),
	SUC("Static Update Controller");

	private ZWaveDeviceType(final String text) {
		this.text = text;
	}

	private final String text;

	public String getLabel() {
		return text;
	}

	public static ZWaveDeviceType fromString(String text) {
		if (text != null) {
			for (ZWaveDeviceType c : ZWaveDeviceType.values()) {
				if (text.equalsIgnoreCase(c.name())) {
					return c;
				}
			}
		}
		return null;
	}
}
