/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.binding.rfxcom.internal.messages.RFXComBaseMessage.PacketType;

public class RFXComMessageFactory {
	
	final static String classUrl = "org.openhab.binding.rfxcom.internal.messages.";
	
	/**
	 * Command to reset RFXCOM controller.
	 * 
	 */
	public final static byte[] CMD_RESET = new byte[] { 0x0D, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	/**
	 * Command to get RFXCOM controller status.
	 * 
	 */
	public final static byte[] CMD_STATUS = new byte[] { 0x0D, 0x00, 0x00,
			0x01, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	/**
	 * Command to save RFXCOM controller configuration.
	 * 
	 */
	public final static byte[] CMD_SAVE = new byte[] { 0x0D, 0x00, 0x00, 0x00,
			0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	
	public static RFXComMessageInterface getMessageInterface(PacketType packetType) throws RFXComException {
		return packetType.createMessage();
	}
	
	public static RFXComMessageInterface getMessageInterface(byte[] packet) throws RFXComException {
		final PacketType packetType = getPacketType(packet[1]);
		return getMessageInterface(packetType);
	}

	public static PacketType convertPacketType(String packetType)
			throws IllegalArgumentException {

		for (PacketType p : PacketType.values()) {
			if (p.toString().equals(packetType)) {
				return p;
			}
		}

		throw new IllegalArgumentException("Unknown packet type " + packetType);
	}

	private static PacketType getPacketType(byte packetType) {
		for (PacketType p : PacketType.values()) {
			if (p.toByte() == packetType) {
				return p;
			}
		}
		
		return PacketType.UNKNOWN;
	}
}
