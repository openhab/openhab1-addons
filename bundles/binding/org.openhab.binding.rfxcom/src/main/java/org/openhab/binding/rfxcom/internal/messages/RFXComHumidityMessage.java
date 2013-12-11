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
 * RFXCOM data class for humidity message.
 * 
 * @author Pauli Anttila, Jan Sjölander
 * @since 1.4.0
 */
public class RFXComHumidityMessage extends RFXComBaseMessage {

	public enum SubType {
		LACROSSE_TX3(1),
		LACROSSE_WS2300(2),
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

	public enum HumidityStatus {
		NORMAL(0),
		COMFORT(1),
		DRY(2),
		WET(3),
		UNKNOWN(255);

		private final int humidityStatus;

		HumidityStatus(int humidityStatus) {
			this.humidityStatus = humidityStatus;
		}

		HumidityStatus(byte humidityStatus) {
			this.humidityStatus = humidityStatus;
		}

		public byte toByte() {
			return (byte) humidityStatus;
		}
	}

	public SubType subType = SubType.UNKNOWN;
	public int sensorId = 0;
	public byte humidity = 0;
	public HumidityStatus humidityStatus = HumidityStatus.UNKNOWN;
	public byte signalLevel = 0;
	public byte batteryLevel = 0;

	public RFXComHumidityMessage() {
		packetType = PacketType.HUMIDITY;
	}

	public RFXComHumidityMessage(byte[] data) {

		encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();
		str += "\n - Sub type = " + subType;
		str += "\n - Id = " + sensorId;
		str += "\n - Humidity = " + humidity;
		str += "\n - Humidity status = " + humidityStatus;
		str += "\n - Signal level = " + signalLevel;
		str += "\n - Battery level = " + batteryLevel;

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
		humidity = data[6];

		try {
			humidityStatus = HumidityStatus.values()[data[7]];
		} catch (Exception e) {
			humidityStatus = HumidityStatus.UNKNOWN;
		}
		signalLevel = (byte) ((data[8] & 0xF0) >> 4);
		batteryLevel = (byte) (data[8] & 0x0F);
	}

	@Override
	public byte[] decodeMessage() {
		byte[] data = new byte[9];

		data[0] = 0x0A;
		data[1] = RFXComBaseMessage.PacketType.HUMIDITY.toByte();
		data[2] = subType.toByte();
		data[3] = seqNbr;
		data[4] = (byte) ((sensorId & 0xFF00) >> 8);
		data[5] = (byte) (sensorId & 0x00FF);
		data[6] = humidity;
		data[7] = humidityStatus.toByte();
		data[8] = (byte) (((signalLevel & 0x0F) << 4) | (batteryLevel & 0x0F));// Janne

		return data;
	}

	@Override
	public String generateDeviceId() {
		return String.valueOf(sensorId);
	}

}
