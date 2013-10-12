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
 * RFXCOM data class for curtain1 message. See Harrison.
 * 
 * @author Evert van Es
 * @since 1.2.0
 * 
 */
public class RFXComCurtain1Message extends RFXComBaseMessage {

	public enum Commands {
		OPEN(0),
		CLOSE(1),
		STOP(2),
		PROGRAM(3);

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
		HARRISON(0);

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

	public SubType subType = SubType.HARRISON;
	public char sensorId = 'A';
	public byte unitcode = 0;
	public Commands command = Commands.STOP;
	public byte signalLevel = 0;
	public byte batteryLevel = 0;

	public RFXComCurtain1Message() {
		packetType = PacketType.CURTAIN1;

	}

	public RFXComCurtain1Message(byte[] data) {

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
		str += "\n - Battery level = " + batteryLevel;

		return str;
	}

	@Override
	public void encodeMessage(byte[] data) {

		super.encodeMessage(data);

		subType = SubType.values()[super.subType];
		
		sensorId = (char) data[4];
		unitcode = data[5];

		command = Commands.STOP;

		for (Commands loCmd : Commands.values()) {
			if (loCmd.toByte() == data[6]) {
				command = loCmd;
				break;
			}
		}
		signalLevel = (byte) ((data[7] & 0xF0) >> 4);
		batteryLevel = (byte) ((data[7] & 0x0F));

	}

	@Override
	public byte[] decodeMessage() {
		 // Example data 	07 18 00 00 65 01 00 00
		 //                 07 18 00 00 65 02 00 00
		
		byte[] data = new byte[8];

		data[0] = 0x07;
		data[1] = 0x18;
		data[2] = subType.toByte();
		data[3] = seqNbr;
		data[4] = (byte) sensorId;
		data[5] = unitcode;
		data[6] = command.toByte();
		data[7] = (byte) (((signalLevel & 0x0F) << 4) + batteryLevel);

		return data;
	}
	
	@Override
	public String generateDeviceId() {
		 return sensorId + "." + unitcode;
	}


}
