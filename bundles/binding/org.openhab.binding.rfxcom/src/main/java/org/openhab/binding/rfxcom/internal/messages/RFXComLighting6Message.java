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

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.rfxcom.RFXComValueSelector;
import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;

/**
 * RFXCOM data class for lighting6 message. See Blyss.
 * 
 * @author Damien Servant
 * @since 1.4.0
 */
public class RFXComLighting6Message extends RFXComBaseMessage {

	public enum SubType {
		BLYSS(0),
		
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
		ON(0),
		OFF(1),
		GROUP_ON(2),
		GROUP_OFF(3),
		
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
			.asList(RFXComValueSelector.RAW_DATA,
					RFXComValueSelector.SIGNAL_LEVEL,
					RFXComValueSelector.COMMAND);

	public SubType subType = SubType.BLYSS;
	public int sensorId = 0;
	public char groupCode = 'A';	
	public byte unitcode = 0;
	public Commands command = Commands.OFF;
	public byte signalLevel = 0;

	public RFXComLighting6Message() {
		packetType = PacketType.LIGHTING6;
	}

	public RFXComLighting6Message(byte[] data) {

		encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();
		str += "\n - Sub type = " + subType;
		str += "\n - Id = " + sensorId;
		str += "\n - Group code = " + groupCode;
		str += "\n - Unit code = " + unitcode;
		str += "\n - Command = " + command;
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
		
		sensorId = (data[4] & 0xFF) << 8 | (data[5] & 0xFF) << 0;
		groupCode = (char) data[6];
		unitcode = data[7];

		try {
			command = Commands.values()[data[8]];
		} catch (Exception e) {
			command = Commands.UNKNOWN;
		}
		
		signalLevel = (byte) ((data[11] & 0xF0) >> 4);
	}

	@Override
	public byte[] decodeMessage() {
		 // Example data 	0B 15 00 02 01 01 41 01 00 04 8E 00
		//                  0B 15 00 02 01 01 41 01 01 04 8E 00 
		
		byte[] data = new byte[12];

		data[0] = 0x0B;
		data[1] = RFXComBaseMessage.PacketType.LIGHTING6.toByte();
		data[2] = subType.toByte();
		data[3] = seqNbr;
		data[4] = (byte) ((sensorId >> 8) & 0xFF);
		data[5] = (byte) (sensorId & 0xFF);
		data[6] = (byte) groupCode;
		data[7] = unitcode;
		data[8] = command.toByte();
		data[9] = 0x00; //CmdSeqNbr1 - 0 to 4 - Useless for a Blyss Switch
		data[10] = 0x00; //CmdSeqNbr2 - 0 to 145 - Useless for a Blyss Switch
		data[11] = (byte) ((signalLevel & 0x0F) << 4);

		return data;
	}
	
	@Override
	public String generateDeviceId() {
		 return sensorId + groupCode + "." + unitcode;
	}

	@Override
	public State convertToState(
			RFXComValueSelector valueSelector) throws RFXComException {
		
		org.openhab.core.types.State state = UnDefType.UNDEF;

		if (valueSelector.getItemClass() == NumberItem.class) {

			if (valueSelector == RFXComValueSelector.SIGNAL_LEVEL) {

				state = new DecimalType(signalLevel);

			} else {
				throw new RFXComException("Can't convert "
						+ valueSelector + " to NumberItem");
			}

		} else if (valueSelector.getItemClass() == SwitchItem.class) {

			if (valueSelector == RFXComValueSelector.COMMAND) {

				switch (command) {
				case OFF:
				case GROUP_OFF:
					state = OnOffType.OFF;
					break;

				case ON:
				case GROUP_ON:
					state = OnOffType.ON;
					break;
					
				default:
					throw new RFXComException("Can't convert "
							+ command + " to SwitchItem");
				}

			} else {
				throw new RFXComException("Can't convert "
						+ valueSelector + " to SwitchItem");
			}

		} else if (valueSelector.getItemClass() == ContactItem.class) {

			if (valueSelector == RFXComValueSelector.COMMAND) {

				switch (command) {
				case OFF:
				case GROUP_OFF:
					state = OpenClosedType.OPEN;
					break;

				case ON:
				case GROUP_ON:
					state = OpenClosedType.CLOSED;
					break;
					
				default:
					throw new RFXComException("Can't convert "
							+ command + " to ContactItem");
				}

			} else {
				throw new RFXComException("Can't convert "
						+ valueSelector + " to ContactItem");
			}

		} else if (valueSelector.getItemClass() == StringItem.class) {

			if (valueSelector == RFXComValueSelector.RAW_DATA) {

				state = new StringType(
						DatatypeConverter.printHexBinary(rawMessage));

			} else {
				throw new RFXComException("Can't convert "
						+ valueSelector + " to StringItem");
			}

		} else {

			throw new RFXComException("Can't convert " + valueSelector
					+ " to " + valueSelector.getItemClass());

		}

		return state;
	}

	@Override
	public void convertFromState(RFXComValueSelector valueSelector, String id,
			Object subType, Type type, byte seqNumber) throws RFXComException {

		this.subType = ((SubType) subType);
		seqNbr = seqNumber;
		String[] ids = id.split("\\.");
		sensorId = Integer.parseInt(ids[0]);
		groupCode = ids[1].charAt(0);
		unitcode = Byte.parseByte(ids[2]);

		switch (valueSelector) {
		case COMMAND:
			if (type instanceof OnOffType) {
				command = (type == OnOffType.ON ? Commands.ON : Commands.OFF);
			} else {
				throw new RFXComException("Can't convert " + type + " to Command");
			}
			break;

		default:
			throw new RFXComException("Can't convert " + type + " to " + valueSelector);
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
	public List<RFXComValueSelector> getSupportedValueSelectors() throws RFXComException {
		return supportedValueSelectors;
	}

}
