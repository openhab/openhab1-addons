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

/**
 * RFXCOM data class for temperature and humidity message.
 * 
 * @author Pauli Anttila
 * @since 1.2.0
 */
public class RFXComTemperatureHumidityMessage extends RFXComBaseMessage {

	public enum SubType {
		THGN122_123_132_THGR122_228_238_268(1),
		THGN800_THGR810(2),
		RTGR328(3),
		THGR328(4),
		WTGR800(5),
		THGR918_THGRN228_THGN50(6),
		TFA_TS34C__CRESTA(7),
		WT260_WT260H_WT440H_WT450_WT450H(8),
		VIKING_02035_02038(9),
		RUBICSON(10),

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
	public double temperature = 0;
	public byte humidity = 0;
	public HumidityStatus humidityStatus = HumidityStatus.UNKNOWN;
	public byte signalLevel = 0;
	public byte batteryLevel = 0;

	public RFXComTemperatureHumidityMessage() {
		packetType = PacketType.TEMPERATURE_HUMIDITY;
	}

	public RFXComTemperatureHumidityMessage(byte[] data) {

		encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();
		str += "\n - Sub type = " + subType;
		str += "\n - Id = " + sensorId;
		str += "\n - Temperature = " + temperature;
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

		temperature = (short) ((data[6] & 0x7F) << 8 | (data[7] & 0xFF)) * 0.1;
		if ((data[6] & 0x80) != 0)
			temperature = -temperature;

		humidity = data[8];

		try {
			humidityStatus = HumidityStatus.values()[data[9]];
		} catch (Exception e) {
			humidityStatus = HumidityStatus.UNKNOWN;
		}
		signalLevel = (byte) ((data[10] & 0xF0) >> 4);
		batteryLevel = (byte) (data[10] & 0x0F);
	}

	@Override
	public byte[] decodeMessage() {
		byte[] data = new byte[11];

		data[0] = 0x0A;
		data[1] = RFXComBaseMessage.PacketType.TEMPERATURE_HUMIDITY.toByte();
		data[2] = subType.toByte();
		data[3] = seqNbr;
		data[4] = (byte) ((sensorId & 0xFF00) >> 8);
		data[5] = (byte) (sensorId & 0x00FF);

		short temp = (short) Math.abs(temperature * 10);
		data[6] = (byte) ((temp >> 8) & 0xFF);
		data[7] = (byte) (temp & 0xFF);
		if (temperature < 0)
			data[6] |= 0x80;

		data[8] = humidity;
		data[9] = humidityStatus.toByte();
		data[10] = (byte) (((signalLevel & 0x0F) << 4) | (batteryLevel & 0x0F));

		return data;
	}
	
	@Override
	public String generateDeviceId() {
		 return String.valueOf(sensorId);
	}


}
