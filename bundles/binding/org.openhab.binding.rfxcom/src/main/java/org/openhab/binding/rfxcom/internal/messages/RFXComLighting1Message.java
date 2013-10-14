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
 * RFXCOM data class for lighting1 message. See X10, ARC, etc..
 * 
 * @author Evert van Es
 * @since 1.2.0
 */
public class RFXComLighting1Message extends RFXComBaseMessage {

	public enum Commands {
		OFF(0),
		ON(1),
		DIM(2),
		BRIGHT(3),
		GROUP_OFF(5),
		GROUP_ON(6),
		CHIME(7);

		private final int command;

		Commands(int command) {
			this.command = command;
		}

		Commands(byte command) {
			this.command = command;
		}

		public byte toByte() {
			return (byte) command;
		}
	}

	public enum SubType {
		X10(0),
		ARC(1),
		AB400D(2),
		WAVEMAN(3),
		EMW200(4),
		IMPULS(5),
		RISINGSUN(6),
		PHILIPS(7);

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

	public SubType subType = SubType.X10;
	public char sensorId = 'A';
	public byte unitcode = 0;
	public Commands command = Commands.ON;
	public byte signalLevel = 0;

	public RFXComLighting1Message() {
		packetType = PacketType.LIGHTING1;

	}

	public RFXComLighting1Message(byte[] data) {

		encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();
		str += "\n - Sub type = " + subType;
		str += "\n - Id = " + sensorId;
		str += "\n - Unit code = " + unitcode;
		str += "\n - Command = " + command;
		str += "\n - Signal level = " + signalLevel;

		return str;
	}

	@Override
	public void encodeMessage(byte[] data) {

		super.encodeMessage(data);

		subType = SubType.values()[super.subType];
		
		sensorId = (char) data[4];
		unitcode = data[5];

		command = Commands.OFF;

		for (Commands loCmd : Commands.values()) {
			if (loCmd.toByte() == data[6]) {
				command = loCmd;
				break;
			}
		}
		signalLevel = (byte) ((data[7] & 0xF0) >> 4);

	}

	@Override
	public byte[] decodeMessage() {
		 // Example data 	07 10 01 00 42 01 01 70
		//                  07 10 01 00 42 10 06 70 
		
		byte[] data = new byte[8];

		data[0] = 0x07;
		data[1] = RFXComBaseMessage.PacketType.LIGHTING1.toByte();
		data[2] = subType.toByte();
		data[3] = seqNbr;
		data[4] = (byte) sensorId;
		data[5] = unitcode;
		data[6] = command.toByte();
		data[7] = (byte) ((signalLevel & 0x0F) << 4);

		return data;
	}
	
	@Override
	public String generateDeviceId() {
		 return sensorId + "." + unitcode;
	}

}
