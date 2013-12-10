/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import javax.xml.bind.DatatypeConverter;

/**
 * Base class for RFXCOM data classes. All other data classes should extend this class.
 * 
 * @author Pauli Anttila
 * @since 1.2.0
 */
public abstract class RFXComBaseMessage implements RFXComMessageInterface {

	public enum PacketType {
		INTERFACE_CONTROL(0),
		INTERFACE_MESSAGE(1),
		TRANSMITTER_MESSAGE(2),
		LIGHTING1(16),
		LIGHTING2(17),
		LIGHTING5(20),
		CURTAIN1(18),
		SECURITY1(32),
		THERMOSTAT1(64),
		TEMPERATURE(80),
		HUMIDITY(81),
		TEMPERATURE_HUMIDITY(82),
		ENERGY(90),

		UNKNOWN(255);

		private final int packetType;

		PacketType(int packetType) {
			this.packetType = packetType;
		}

		PacketType(byte packetType) {
			this.packetType = packetType;
		}

		public byte toByte() {
			return (byte) packetType;
		}
	}

	public byte[] rawMessage;
	public PacketType packetType = PacketType.UNKNOWN;
	public byte subType = 0;
	public byte seqNbr = 0;
	public byte id1 = 0;
	public byte id2 = 0;

	public RFXComBaseMessage() {

	}

	public RFXComBaseMessage(byte[] data) {

		encodeMessage(data);
	}

	public void encodeMessage(byte[] data) {

		rawMessage = data;

		packetType = PacketType.UNKNOWN;

		for (PacketType pt : PacketType.values()) {
			if (pt.toByte() == data[1]) {
				packetType = pt;
				break;
			}
		}

		subType = data[2];
		seqNbr = data[3];
		id1 = data[4];

		if (data.length > 5) {
			id2 = data[5];
		}

	}

	public abstract byte[] decodeMessage();

	public String toString() {
		String str = "";

		str += "Raw data = " + DatatypeConverter.printHexBinary(rawMessage);
		str += "\n - Packet type = " + packetType;
		str += "\n - Seq number = " + seqNbr;

		return str;
	}
	
	public String generateDeviceId() {
	 return id1 + "." + id2;
	}

}
