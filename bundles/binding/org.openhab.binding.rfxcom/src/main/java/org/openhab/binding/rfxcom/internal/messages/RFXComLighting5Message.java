/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.rfxcom.RFXComValueSelector;
import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;

/**
 * RFXCOM data class for lighting5 message.
 * 
 * @author Paul Hampson, Neil Renaud
 * @since 1.3.0
 */
public class RFXComLighting5Message extends RFXComBaseMessage {

	public enum SubType {
		LIGHTWAVERF(0),
		EMW100(1),
		BBSB_NEW(2),
		MDREMOTE(3),
		CONRAD_RSL2(4),
		LIVOLO(5),
		
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
		OFF(0),
		ON(1),
		GROUP_OFF(2),
		MOOD1(3),
		MOOD2(4),
		MOOD3(5),
		MOOD4(6),
		MOOD5(7),
		RESERVED1(8),
		RESERVED2(9),
		UNLOCK(10),
		LOCK(11),
		ALL_LOCK(12),
		CLOSE_RELAY(13),
		STOP_RELAY(14),
		OPEN_RELAY(15),
		SET_LEVEL(16),
		
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
					RFXComValueSelector.COMMAND,
					RFXComValueSelector.MOOD,
					RFXComValueSelector.DIMMING_LEVEL);

	public SubType subType = SubType.LIGHTWAVERF;
	public int sensorId = 0;
	public byte unitcode = 0;
	public Commands command = Commands.OFF;
	public byte dimmingLevel = 0;
	public byte signalLevel = 0;

	public RFXComLighting5Message() {
		packetType = PacketType.LIGHTING5;
	}

	public RFXComLighting5Message(byte[] data) {
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
		str += "\n - Dim level = " + dimmingLevel;
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
		
		sensorId = (data[4] & 0xFF) << 16 | (data[5] & 0xFF) << 8
				| (data[6] & 0xFF) << 0;
		unitcode = data[7];
		
		try {
			command = Commands.values()[data[8]];
		} catch (Exception e) {
			command = Commands.UNKNOWN;
		}
		
		dimmingLevel = data[9];
		signalLevel = (byte) ((data[10] & 0xF0) >> 4);
	}

	@Override
	public byte[] decodeMessage() {

		byte[] data = new byte[11];

		data[0] = 0x0A;
		data[1] = RFXComBaseMessage.PacketType.LIGHTING5.toByte();
		data[2] = subType.toByte();
		data[3] = seqNbr;
		data[4] = (byte) ((sensorId >> 16) & 0xFF);
		data[5] = (byte) ((sensorId >> 8) & 0xFF);
		data[6] = (byte) (sensorId & 0xFF);

		data[7] = unitcode;
		data[8] = command.toByte();
		data[9] = dimmingLevel;
		data[10] = (byte) ((signalLevel & 0x0F) << 4);
		
		return data;
	}
	
	@Override
	public String generateDeviceId() {
		 return sensorId + "." + unitcode;
	}

	/**
	 * Convert a 0-31 scale value to a percent type.
	 * 
	 * @param pt
	 *            percent type to convert
	 * @return converted value 0-31
	 */
	public static int getDimLevelFromPercentType(PercentType pt) {
		return pt
				.toBigDecimal()
				.multiply(BigDecimal.valueOf(31))
				.divide(PercentType.HUNDRED.toBigDecimal(), 0,
						BigDecimal.ROUND_UP).intValue();
	}

	/**
	 * Convert a 0-31 scale value to a percent type.
	 * 
	 * @param pt
	 *            percent type to convert
	 * @return converted value 0-31
	 */
	public static PercentType getPercentTypeFromDimLevel(int value) {
		value = Math.min(value, 31);
		
		return new PercentType(BigDecimal
				.valueOf(value)
				.multiply(BigDecimal.valueOf(100))
				.divide(BigDecimal.valueOf(31), 0,
						BigDecimal.ROUND_UP).intValue());
	}

	@Override
	public State convertToState(RFXComValueSelector valueSelector)
			throws RFXComException {
		
		org.openhab.core.types.State state = UnDefType.UNDEF;

		if (valueSelector.getItemClass() == NumberItem.class) {

			if (valueSelector == RFXComValueSelector.SIGNAL_LEVEL) {

				state = new DecimalType(signalLevel);

			} else if (valueSelector == RFXComValueSelector.MOOD) {
				switch (command) {
				case GROUP_OFF:
					state = new DecimalType(0);
					break;
				case MOOD1:
					state = new DecimalType(1);
					break;
				case MOOD2:
					state = new DecimalType(2);
					break;
				case MOOD3:
					state = new DecimalType(3);
					break;
				case MOOD4:
					state = new DecimalType(4);
					break;
				case MOOD5:
					state = new DecimalType(5);					
					break;
				default:
					throw new RFXComException("Unexpected mood: " + command);
				}
			} else {
				throw new RFXComException("Can't convert "
						+ valueSelector + " to NumberItem");
			}

		} else if (valueSelector.getItemClass() == DimmerItem.class
				|| valueSelector.getItemClass() == RollershutterItem.class) {

			if (valueSelector == RFXComValueSelector.DIMMING_LEVEL) {
				state = RFXComLighting5Message.getPercentTypeFromDimLevel(dimmingLevel);

			} else {
				throw new RFXComException("Can't convert "
						+ valueSelector + " to DimmerItem/RollershutterItem");
			}

		} else if (valueSelector.getItemClass() == SwitchItem.class) {

			if (valueSelector == RFXComValueSelector.COMMAND) {

				switch (command) {
				case OFF:
				case GROUP_OFF:
					state = OnOffType.OFF;
					break;

				case ON:				
					state = OnOffType.ON;
					break;
				
				case SET_LEVEL:
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
					state = OpenClosedType.CLOSED;
					break;
				
				case SET_LEVEL:
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
		unitcode = Byte.parseByte(ids[1]);

		switch (valueSelector) {
		case COMMAND:
			if (type instanceof OnOffType) {
				command = (type == OnOffType.ON ? Commands.ON : Commands.OFF);
				dimmingLevel = 0;
			} else {
				throw new RFXComException("Can't convert " + type + " to Command");
			}
			break;

		case DIMMING_LEVEL:
			if (type instanceof OnOffType) {
				command = (type == OnOffType.ON ? Commands.ON : Commands.OFF);
				dimmingLevel = 0;
			} else if (type instanceof PercentType) {
				command = Commands.SET_LEVEL;
				dimmingLevel = (byte) getDimLevelFromPercentType((PercentType) type);

				if (dimmingLevel == 0) {
					command = Commands.OFF;
				}

			} else if (type instanceof IncreaseDecreaseType) {
				command = Commands.SET_LEVEL;
				// Evert: I do not know how to get previous object state...
				dimmingLevel = 5;

			} else {
				throw new RFXComException("Can't convert " + type + " to Command");
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
	public List<RFXComValueSelector> getSupportedValueSelectors() throws RFXComException {
		return supportedValueSelectors;
	}

}
