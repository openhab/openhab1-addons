/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import java.util.Arrays;
import java.util.List;

import org.openhab.binding.rfxcom.RFXComValueSelector;
import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;

/**
 * RFXCOM data class for lighting4 message.
 * 
 * a Lighting4 Base command is composed of 24 bit DATA plus PULSE information
 * 
 * DATA:
 * Code          = 014554
 * S1- S24  = <0000 0001 0100 0101 0101> <0100>
 * first 20 are DeviceID last 4 are for Command 
 * 
 * PULSE:
 * default 350
 * 
 * Tested on a PT2262 remote PlugIn module
 * 
 * Example: 
 * 
 * Switch TESTout  "TestOut" (All) {rfxcom=">83205.350:LIGHTING4.PT2262:Command"}
 * (SendCommand  DeviceID(int).Pulse(int):LIGHTING4.Subtype:Command  )
 * 
 * Switch TESTin  "TestIn" (All) {rfxcom="<83205:Command"} 
 * (ReceiveCommand ON/OFF Command  )
 * 
 * @author Alessandro Ballini (ITA)
 * @since 1.5.1
 */
public class RFXComLighting4Message extends RFXComBaseMessage {

	public enum SubType {
		PT2262(0),
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

	public enum Commands {
		UNDEFINED_0(0), ON(1), UNDEFINED_2(2), UNDEFINED_3(3), OFF(4),

		UNKNOWN(255);

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

	private final static List<RFXComValueSelector> supportedValueSelectors = Arrays
			.asList(RFXComValueSelector.RAW_DATA, RFXComValueSelector.COMMAND);

	public SubType subType = SubType.PT2262;
	public int sensorId = 0;
	public Commands command = Commands.OFF;
	public int pulse = 0;
	public byte signalLevel = 0;

	public RFXComLighting4Message() {
		packetType = PacketType.LIGHTING4;
	}

	public RFXComLighting4Message(byte[] data) {
		encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();
		str += "\n - Sub type = " + subType;
		str += "\n - Id = " + sensorId;
		str += "\n - Command = " + command;
		str += "\n - Pulse = " + pulse;

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

		sensorId = (data[4] & 0xFF) << 16 | (data[5] & 0xFF) << 8
				| (data[6] & 0xFF) >>> 4;

		int commandID = (data[6] & 0x0F); // 4 OFF - 1 ON

		pulse = (data[7] & 0xFF) << 8 | (data[8] & 0xFF) << 0;

		try {
			command = Commands.values()[commandID];
		} catch (Exception e) {
			command = Commands.UNKNOWN;
		}

		signalLevel = (byte) ((data[9] & 0xF0) >> 4);
	}

	@Override
	public byte[] decodeMessage() {

		byte[] data = new byte[11];

		data[0] = 0x09;
		data[1] = RFXComBaseMessage.PacketType.LIGHTING4.toByte();
		data[2] = subType.toByte();
		data[3] = seqNbr;
		
		//SENSORID + COMMAND
		data[4] = (byte) ((sensorId >> 16) & 0xFF);
		data[5] = (byte) ((sensorId >> 8) & 0xFF);
		data[6] = (byte) ( ((sensorId >>4) & 0xFF)  | command.ordinal() & 0x0F );

		//PULSE
		data[7] = (byte) ((pulse >> 8) & 0xFF);
		data[8] = (byte) ((pulse >> 0) & 0xFF);
		
		//SIGNAL
		data[9] = 0;
		
		//UNUSED
		data[10] = 0;

		return data;
	}

	@Override
	public String generateDeviceId() {
		return "" + sensorId;
	}

	

	@Override
	public State convertToState(RFXComValueSelector valueSelector)
			throws RFXComException {

		org.openhab.core.types.State state = UnDefType.UNDEF;

		// SWITCHITEM
		if (valueSelector.getItemClass() == SwitchItem.class) {

			if (valueSelector == RFXComValueSelector.COMMAND) {
				switch (command) {
				case OFF:
					state = OnOffType.OFF;
					break;
				case ON:
					state = OnOffType.ON;
					break;
				default:
					throw new RFXComException("Can't convert value " + command
							+ " to COMMAND SwitchItem");
				}
			} else {
				throw new RFXComException("Can't convert " + valueSelector
						+ " to SwitchItem: not supported");
			}

			return state;
		}

		throw new RFXComException("Can't convert " + valueSelector + " to "
				+ valueSelector.getItemClass());

	}

	@Override
	public void convertFromState(RFXComValueSelector valueSelector, String id,
			Object subType, Type type, byte seqNumber) throws RFXComException {

		this.subType = ((SubType) subType);
		seqNbr = seqNumber;
		String[] ids = id.split("\\.");
		sensorId = Integer.parseInt(ids[0]);
		pulse = Integer.parseInt(ids[1]);
		
		switch (valueSelector) {

		case COMMAND:
			if (type instanceof OnOffType) {
				command = (type == OnOffType.ON ? Commands.ON : Commands.OFF);
			} else {
				throw new RFXComException("Can't convert " + type
						+ " to Command");
			}
			break;

		default:
			throw new RFXComException("Can't convert " + type + " to "
					+ valueSelector);
		}
	}

	@Override
	public Object convertSubType(String subType) throws RFXComException {

		for (SubType s : SubType.values()) {
			if (s.toString().equals(subType)) {
				return s;
			}
		}

		throw new RFXComException("Unknown sub type " + subType);
	}

	@Override
	public List<RFXComValueSelector> getSupportedValueSelectors()
			throws RFXComException {
		return supportedValueSelectors;
	}

}
