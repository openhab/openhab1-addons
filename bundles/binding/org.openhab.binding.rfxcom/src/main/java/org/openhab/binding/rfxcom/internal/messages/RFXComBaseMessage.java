/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
		TEMPERATURE(80),
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
