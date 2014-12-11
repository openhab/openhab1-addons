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
 * Enumerate the different message types and their identifiers
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public enum MaxCulMsgType {
	PAIR_PING(0x00), PAIR_PONG(0x01), ACK(0x02), TIME_INFO(0x03),

	CONFIG_WEEK_PROFILE(0x10), CONFIG_TEMPERATURES(0x11), CONFIG_VALVE(0x12),

	ADD_LINK_PARTNER(0x20), REMOVE_LINK_PARTNER(0x21), SET_GROUP_ID(0x22), REMOVE_GROUP_ID(
			0x23),

	SHUTTER_CONTACT_STATE(0x30),

	SET_TEMPERATURE(0x40), WALL_THERMOSTAT_CONTROL(0x42), SET_COMFORT_TEMPERATURE(
			0x43), SET_ECO_TEMPERATURE(0x44),

	PUSH_BUTTON_STATE(0x50),

	THERMOSTAT_STATE(0x60),

	WALL_THERMOSTAT_STATE(0x70),

	SET_DISPLAY_ACTUAL_TEMP(0x82),

	WAKEUP(0xF1), RESET(0xF0),

	UNKNOWN(0xFF);

	private final int msgType;

	MaxCulMsgType(int msgType) {
		this.msgType = msgType;
	}

	MaxCulMsgType(byte msgType) {
		this.msgType = msgType;
	}

	public byte toByte() {
		return (byte) msgType;
	}

	public static MaxCulMsgType fromByte(byte b) {
		for (MaxCulMsgType m : values()) {
			if (m.toByte() == b)
				return m;
		}
		return null;
	}

}
