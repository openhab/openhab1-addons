/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.rfxcom.internal.RFXComException;

/**
 * Base class for RFXCOM data classes. All other data classes should extend this class.
 * 
 * @author Pauli Anttila
 * @since 1.2.0
 */
public abstract class RFXComBaseMessage implements RFXComMessageInterface {

	public enum PacketType {
		INTERFACE_CONTROL(0) { public RFXComControlMessage createMessage() { return new RFXComControlMessage(); }},
		INTERFACE_MESSAGE(1) { public RFXComInterfaceMessage createMessage() { return new RFXComInterfaceMessage(); }},
		TRANSMITTER_MESSAGE(2) { public RFXComTransmitterMessage createMessage() { return new RFXComTransmitterMessage(); }},
		UNDECODED_RF_MESSAGE(3), 
		LIGHTING1(16) { public RFXComLighting1Message createMessage() { return new RFXComLighting1Message(); }},
		LIGHTING2(17) { public RFXComLighting2Message createMessage() { return new RFXComLighting2Message(); }},
		LIGHTING3(18),
		LIGHTING4(19) { public RFXComLighting4Message createMessage() { return new RFXComLighting4Message(); }},
		LIGHTING5(20) { public RFXComLighting5Message createMessage() { return new RFXComLighting5Message(); }},
		LIGHTING6(21) { public RFXComLighting6Message createMessage() { return new RFXComLighting6Message(); }},
		CHIME(22),
		FAN(23),
		CURTAIN1(24) { public RFXComCurtain1Message createMessage() { return new RFXComCurtain1Message(); }},
		BLINDS1(25) { public RFXComBlinds1Message createMessage() { return new RFXComBlinds1Message(); }},
		RFY(26) { public RFXComRfyMessage createMessage() { return new RFXComRfyMessage(); }},
		SECURITY1(32) { public RFXComSecurity1Message createMessage() { return new RFXComSecurity1Message(); }},
		CAMERA1(40),
		REMOTE_CONTROL(48),
		THERMOSTAT1(64) { public RFXComThermostat1Message createMessage() { return new RFXComThermostat1Message(); }},
		THERMOSTAT2(65),
		THERMOSTAT3(66),
		BBQ1(78),
		TEMPERATURE_RAIN(79),
		TEMPERATURE(80) { public RFXComTemperatureMessage createMessage() { return new RFXComTemperatureMessage(); }},
		HUMIDITY(81) { public RFXComHumidityMessage createMessage() { return new RFXComHumidityMessage(); }},
		TEMPERATURE_HUMIDITY(82) { public RFXComTemperatureHumidityMessage createMessage() { return new RFXComTemperatureHumidityMessage(); }},
		BAROMETRIC(83),
		TEMPERATURE_HUMIDITY_BAROMETRIC(84),
		RAIN(85) { public RFXComRainMessage createMessage() { return new RFXComRainMessage(); }},
		WIND(86) { public RFXComWindMessage createMessage() { return new RFXComWindMessage(); }},
		UV(87),
		DATE_TIME(88),
		CURRENT(89),
		ENERGY(90) { public RFXComEnergyMessage createMessage() { return new RFXComEnergyMessage(); }},
		CURRENT_ENERGY(91),
		POWER(92),
		WEIGHT(93),
		GAS(94),
		WATER(95),
		RFXSENSOR(112),
		RFXMETER(113),
		FS20(114),
		IO_LINES(128),
		
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
		
		public RFXComMessageInterface createMessage() throws RFXComException {
			throw new RFXComException("the message for " + name() + " is not implemented");
		}
	}
	
	public byte[] rawMessage;
	public PacketType packetType = PacketType.UNKNOWN;
	public byte packetId = 0;
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
		packetId = data[1];
		
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

	public String toString() {
		String str = "";

		str += "Raw data = " + DatatypeConverter.printHexBinary(rawMessage);
		str += "\n - Packet type = " + packetType;
        str += "\n - Seq number = " + (short)(seqNbr & 0xFF);

		return str;
	}
	
	public String generateDeviceId() {
		return id1 + "." + id2;
	}	
}
