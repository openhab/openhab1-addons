/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

/**
 * RFXCOM data class for thermostat1 message.
 * 
 * @author Les Ashworth
 * @since 1.4.1
 */
public class RFXComThermostat1Message extends RFXComBaseMessage {

	public enum SubType {
		Digimax_TLX7506(0),

		UNKNOWN(255);

		private final int subType;

		SubType(int subType) {
			this.subType = subType;
		}

		SubType(byte subType) {
			this.subType = subType;
		}

		public byte toByte() {
			return (byte) subType;
		}
	}

	/* Added item for ContactTypes */
	public enum Contact {
		NOT_SET(0),
		DEMAND(1), 
		NO_DEMAND(2), 
		INITIALIZING(3), 
		HEATING(8), 
		COOLING(9), 
		UNKNOWN(255);

		private final int contact;

		Contact(int contact) {
			this.contact = contact;
		}

		Contact(byte contact) {
			this.contact = contact;
		}

		public byte toByte() {
			return (byte) contact;
		}
	}

	public SubType subType = SubType.UNKNOWN;
	public int sensorId = 0;
	public byte temperature = 0;
	public byte set = 0;
	public Contact mode = Contact.UNKNOWN;
	public Contact status = Contact.UNKNOWN;
	public byte signalLevel = 0;

	public RFXComThermostat1Message() {
		packetType = PacketType.THERMOSTAT1;
	}

	public RFXComThermostat1Message(byte[] data) {

		encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();
		str += "\n - Sub type = " + subType;
		str += "\n - Id = " + sensorId;
		str += "\n - Temperature = " + temperature;
		str += "\n - Set = " + set;
		str += "\n - Mode = " + mode;
		str += "\n - Status = " + status;
		str += "\n - Signal level = " + signalLevel;

		return str;
	}

	@Override
	public void encodeMessage(byte[] data) {

		super.encodeMessage(data);

		try {
			subType = SubType.values()[super.subType];
		} catch (Exception e) {
			subType = SubType.UNKNOWN;
		}
		sensorId = (data[4] & 0xFF) << 8 | (data[5] & 0xFF);
		temperature = data[6];
		set = data[7];
		mode = Contact.valueOf("HEATING");
		status = Contact.values()[(data[8] & 0x03)];
		signalLevel = (byte) ((data[9] & 0xF0) >> 4);

	}

	@Override
	public byte[] decodeMessage() {
		byte[] data = new byte[11];

		data[0] = 0x08;
		data[1] = RFXComBaseMessage.PacketType.THERMOSTAT1.toByte();
		data[2] = subType.toByte();
		data[3] = seqNbr;
		data[4] = (byte) ((sensorId & 0xFF00) >> 8);
		data[5] = (byte) (sensorId & 0x00FF);
		data[6] = (byte) (temperature);
		data[7] = (byte) (set);
		data[8] = (byte) (mode.toByte() & status.toByte());
		data[9] = (byte) (((signalLevel & 0x0F) << 4));

		return data;
	}

	@Override
	public String generateDeviceId() {
		return String.valueOf(sensorId);
	}

}
