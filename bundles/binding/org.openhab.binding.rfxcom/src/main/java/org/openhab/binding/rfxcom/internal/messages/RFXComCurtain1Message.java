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
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;

/**
 * RFXCOM data class for curtain1 message. See Harrison.
 * 
 * @author Evert van Es
 * @since 1.2.0
 * 
 */
public class RFXComCurtain1Message extends RFXComBaseMessage {

	public enum SubType {
		HARRISON(0),
		
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
		OPEN(0),
		CLOSE(1),
		STOP(2),
		PROGRAM(3),
		
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
					RFXComValueSelector.BATTERY_LEVEL,
					RFXComValueSelector.COMMAND);
	
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

		try {
			subType = SubType.values()[super.subType];
		} catch (Exception e) {
			subType = SubType.UNKNOWN;
		}
		sensorId = (char) data[4];
		unitcode = data[5];

		try {
			command = Commands.values()[data[6]];
		} catch (Exception e) {
			command = Commands.UNKNOWN;
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

	@Override
	public State convertToState(RFXComValueSelector valueSelector)
			throws RFXComException {
		
		org.openhab.core.types.State state = UnDefType.UNDEF;

		if (valueSelector.getItemClass() == NumberItem.class) {

			if (valueSelector == RFXComValueSelector.SIGNAL_LEVEL) {

				state = new DecimalType(signalLevel);

			} else if (valueSelector == RFXComValueSelector.BATTERY_LEVEL) {

				state = new DecimalType(batteryLevel);

			} else {
				throw new RFXComException("Can't convert "
						+ valueSelector + " to NumberItem");
			}

		} else if (valueSelector.getItemClass() == RollershutterItem.class) {

			if (valueSelector == RFXComValueSelector.COMMAND) {

				switch (command) {
				case CLOSE:
					state = OpenClosedType.CLOSED;
					break;

				case OPEN:
					state = OpenClosedType.OPEN;
					break;
					
				default:
					break;
				}

			} else {
				throw new RFXComException("Can't convert "
						+ valueSelector + " to SwitchItem");
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
		sensorId = ids[0].charAt(0);
		unitcode = Byte.parseByte(ids[1]);

		switch (valueSelector) {
		case SHUTTER:
			if (type instanceof OpenClosedType) {
				command = (type == OpenClosedType.CLOSED ? Commands.CLOSE : Commands.OPEN);
			} else if (type instanceof UpDownType) {
				command = (type == UpDownType.UP ? Commands.CLOSE : Commands.OPEN);
			} else if (type instanceof StopMoveType) {
				command = Commands.STOP;
				
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
