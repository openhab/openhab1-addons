/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.panstamp.internal;

import java.util.Map;

/**
 * A simple container structure for the binding (settings, not items) configuration.
 * 
 * @author Gideon le Grange
 * @since 1.8.0
 */
class PanStampBindingSettings {

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PanStampBindingSettings) {
			PanStampBindingSettings other = (PanStampBindingSettings) obj;
			return !serialDiffers(other) && !networkDiffers(other) && !debugDiffers(other)
					&& !directoriesDiffers(other);
		}
		return false;
	}

	/**
	 * Check if the serial settings differs between two configurations
	 * 
	 * @param other
	 *            settings to compare with this.
	 * @return true if they differ
	 */
	boolean serialDiffers(PanStampBindingSettings other) {
		return !serialPort.equals(other.serialPort) || (serialSpeed != other.serialSpeed);
	}

	/**
	 * check if the network settings differs between two configs
	 * 
	 * @param other
	 *            settings to compare with this.
	 * @return true if they differ
	 */
	boolean networkDiffers(PanStampBindingSettings other) {
		return !((networkChannel == other.networkChannel) && (networkId == other.networkId) && (networkDeviceAddress == other.networkDeviceAddress));
	}

	/**
	 * check if the debug settings differs between two configs
	 * 
	 * @param other
	 *            settings to compare with this.
	 * @return true if they differ
	 */
	boolean debugDiffers(PanStampBindingSettings other) {
		return (debugEnabled != other.debugEnabled) || (debugPort != other.debugPort);
	}

	/**
	 * check if the directory settings differs between two configs
	 * 
	 * @param other
	 *            settings to compare with this.
	 * @return true if they differ
	 */
	boolean directoriesDiffers(PanStampBindingSettings other) {
		return !xmlDir.equals(other.xmlDir);
	}

	/**
	 * Parse the given kv map and return a config object
	 * 
	 * @param con
	 *            the map containing the kv pairs
	 * @return the settings object
	 * @throws ValueException
	 */
	static PanStampBindingSettings parseConfig(final Map<String, Object> con) throws ValueException {
		PanStampBindingSettings cfg = new PanStampBindingSettings();
		cfg.serialPort = PanStampConversions.asString("serial.port", con.get("serial.port"));
		if (con.containsKey("serial.speed")) {
			cfg.serialSpeed = PanStampConversions.asInt("serial.speed", con.get("serial.speed"), new int[] { 9600,
					19200, 38400, 57600, 115200 });
		}
		if (con.containsKey("network.channel")) {
			cfg.networkChannel = PanStampConversions.asInt("network.channel", con.get("network.channel"), 0, 0xFFFF);
		}
		if (con.containsKey("network.id")) {
			cfg.networkId = PanStampConversions.asInt("network.id", con.get("network.id"), 0, 0xFFFF);
		}
		if (con.containsKey("network.deviceAddress")) {
			cfg.networkDeviceAddress = PanStampConversions.asInt("network.deviceAddress",
					con.get("network.deviceAddress"), 0, 0xFFFF);
		}
		if (con.containsKey("directory.xml")) {
			cfg.xmlDir = PanStampConversions.asString("directory.xml", con.get("directory.xml"));
		}
		if (con.containsKey("debug.port")) {
			cfg.debugPort = PanStampConversions.asInt("debug.port", con.get("debug.port"), 1, 65535);
			cfg.debugEnabled = true;
		}
		return cfg;
	}

	/** The modem serial port */
	String serialPort;
	/** The modem serial speed */
	int serialSpeed = 38400;
	/** The SWAP network channel */
	int networkChannel = -1;
	/** The SWAP network ID */
	int networkId = -1;
	/** The local SWAP modem's device address */
	int networkDeviceAddress = -1;
	/** Directory for panStamp XML device defintions */
	String xmlDir = "etc/panstamp/xml";
	/** True if the TCP debug port is enabled */
	boolean debugEnabled = false;
	/** The TCP debug port */
	int debugPort = 3000;

}
