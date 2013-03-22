/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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

package org.openhab.binding.rfxcom.internal;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.rfxcom.RFXComValueSelector;
import org.openhab.binding.rfxcom.internal.messages.*;
import org.openhab.binding.rfxcom.internal.messages.RFXComBaseMessage.PacketType;
import org.openhab.binding.rfxcom.internal.messages.RFXComLighting2Message.Commands;
import org.openhab.binding.rfxcom.internal.messages.RFXComLighting2Message.DimmingLevel;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;

/**
 * This class provides utilities to convert OpenHAB data types to RFXCOM data
 * types and vice verse.
 * 
 * @author Pauli Anttila
 * @since 1.2.0
 */
public class RFXComDataConverter {

	/**
	 * Generate device id from RFXCOM object data.
	 * 
	 * @param obj
	 *            RFXCOM data object.
	 * 
	 * @return device id.
	 */
	public static String generateDeviceId(Object obj) {
		String id = null;

		if (obj instanceof RFXComLighting2Message) {

			RFXComLighting2Message o = (RFXComLighting2Message) obj;
			id = o.sensorId + "." + o.unitcode;

		} else if (obj instanceof RFXComTemperatureHumidityMessage) {

			RFXComTemperatureHumidityMessage o = (RFXComTemperatureHumidityMessage) obj;
			id = String.valueOf(o.sensorId);
		}

		return id;
	}

	/**
	 * Convert RFXCOM objects to OpenHAB state.
	 * 
	 * @param obj
	 *            RFXCOM data object.
	 * @param valueSelector
	 *            value selector.
	 * 
	 * @return openHAB state.
	 */
	public static State convertRFXCOMValueToOpenHABValue(Object obj,
			RFXComValueSelector valueSelector) throws NumberFormatException {

		if (obj instanceof RFXComLighting2Message)
			return convertLighting2ToState((RFXComLighting2Message) obj,
					valueSelector);

		else if (obj instanceof RFXComTemperatureHumidityMessage)
			return convertTemperatureHumidity2ToState(
					(RFXComTemperatureHumidityMessage) obj, valueSelector);

		throw new NumberFormatException("Can't convert " + obj.getClass()
				+ " to " + valueSelector.getItemClass());
	}

	private static State convertLighting2ToState(RFXComLighting2Message obj,
			RFXComValueSelector valueSelector) {

		org.openhab.core.types.State state = UnDefType.UNDEF;

		if (valueSelector.getItemClass() == NumberItem.class) {

			if (valueSelector == RFXComValueSelector.SIGNAL_LEVEL) {

				state = new DecimalType(obj.signalLevel);

			} else {
				throw new NumberFormatException("Can't convert "
						+ valueSelector + " to NumberItem");
			}

		} else if (valueSelector.getItemClass() == DimmerItem.class
				|| valueSelector.getItemClass() == RollershutterItem.class) {

			if (valueSelector == RFXComValueSelector.DIMMING_LEVEL) {

				switch (obj.dimmingLevel) {
				case LEVEL0:
					state = new PercentType(100);
					break;
				case LEVEL6:
					state = new PercentType(84);
					break;
				case LEVEL12:
					state = new PercentType(78);
					break;
				case LEVEL18:
					state = new PercentType(72);
					break;
				case LEVEL24:
					state = new PercentType(66);
					break;
				case LEVEL30:
					state = new PercentType(60);
					break;
				case LEVEL36:
					state = new PercentType(54);
					break;
				case LEVEL42:
					state = new PercentType(48);
					break;
				case LEVEL48:
					state = new PercentType(42);
					break;
				case LEVEL54:
					state = new PercentType(36);
					break;
				case LEVEL60:
					state = new PercentType(30);
					break;
				case LEVEL66:
					state = new PercentType(24);
					break;
				case LEVEL72:
					state = new PercentType(18);
					break;
				case LEVEL78:
					state = new PercentType(12);
					break;
				case LEVEL84:
					state = new PercentType(6);
					break;
				case LEVEL100:
					state = new PercentType(0);
					break;
				}

			} else {
				throw new NumberFormatException("Can't convert "
						+ valueSelector + " to DimmerItem/RollershutterItem");
			}

		} else if (valueSelector.getItemClass() == SwitchItem.class) {

			if (valueSelector == RFXComValueSelector.COMMAND) {

				switch (obj.command) {
				case OFF:
				case GROUP_OFF:
					state = OnOffType.OFF;
					break;

				case ON:
				case GROUP_ON:
					state = OnOffType.ON;
					break;

				case SET_GROUP_LEVEL:
				case SET_LEVEL:
					throw new NumberFormatException("Can't convert "
							+ obj.command + " to SwitchItem");
				}

			} else {
				throw new NumberFormatException("Can't convert "
						+ valueSelector + " to SwitchItem");
			}

		} else if (valueSelector.getItemClass() == ContactItem.class) {

			if (valueSelector == RFXComValueSelector.COMMAND) {

				switch (obj.command) {
				case OFF:
				case GROUP_OFF:
					state = OpenClosedType.OPEN;
					break;

				case ON:
				case GROUP_ON:
					state = OpenClosedType.CLOSED;
					break;

				case SET_GROUP_LEVEL:
				case SET_LEVEL:
					throw new NumberFormatException("Can't convert "
							+ obj.command + " to ContactItem");
				}

			} else {
				throw new NumberFormatException("Can't convert "
						+ valueSelector + " to ContactItem");
			}

		} else if (valueSelector.getItemClass() == StringItem.class) {

			if (valueSelector == RFXComValueSelector.RAW_DATA) {

				state = new StringType(
						DatatypeConverter.printHexBinary(obj.rawMessage));

			} else {
				throw new NumberFormatException("Can't convert "
						+ valueSelector + " to StringItem");
			}

		} else {

			throw new NumberFormatException("Can't convert " + valueSelector
					+ " to " + valueSelector.getItemClass());

		}

		return state;
	}

	private static State convertTemperatureHumidity2ToState(
			RFXComTemperatureHumidityMessage obj,
			RFXComValueSelector valueSelector) {

		org.openhab.core.types.State state = UnDefType.UNDEF;

		if (valueSelector.getItemClass() == NumberItem.class) {

			if (valueSelector == RFXComValueSelector.SIGNAL_LEVEL) {

				state = new DecimalType(obj.signalLevel);

			} else if (valueSelector == RFXComValueSelector.BATTERY_LEVEL) {

				state = new DecimalType(obj.batteryLevel);

			} else if (valueSelector == RFXComValueSelector.TEMPERATURE) {

				state = new DecimalType(obj.temperature);

			} else if (valueSelector == RFXComValueSelector.HUMIDITY) {

				state = new DecimalType(obj.humidity);

			} else {
				throw new NumberFormatException("Can't convert "
						+ valueSelector + " to NumberItem");
			}

		} else if (valueSelector.getItemClass() == StringItem.class) {

			if (valueSelector == RFXComValueSelector.RAW_DATA) {

				state = new StringType(
						DatatypeConverter.printHexBinary(obj.rawMessage));

			} else if (valueSelector == RFXComValueSelector.HUMIDITY_STATUS) {

				state = new StringType(obj.humidityStatus.toString());

			} else {
				throw new NumberFormatException("Can't convert "
						+ valueSelector + " to StringItem");
			}
		} else {

			throw new NumberFormatException("Can't convert " + valueSelector
					+ " to " + valueSelector.getItemClass());

		}

		return state;
	}

	/**
	 * Convert OpenHAB value to RFXCOM data object.
	 * 
	 * @param id
	 *            RFXCOM device ID.
	 * @param packetType
	 *            RFXCOM target packet type.
	 * @param subType
	 *            RFXCOM sub type.
	 * @param valueSelector
	 *            value selector.
	 * @param type
	 *            OpenHAB data type.
	 * @param seqNumber
	 *            sequence number.
	 * 
	 * @return openHAB state.
	 */
	public static Object convertOpenHABValueToRFXCOMValue(String id,
			PacketType packetType, Object subType,
			RFXComValueSelector valueSelector, Type type, byte seqNumber) {

		Object obj = null;

		switch (packetType) {
		case LIGHTING2:
			RFXComLighting2Message d = new RFXComLighting2Message();
			d.subType = (RFXComLighting2Message.SubType) subType;
			d.seqNbr = seqNumber;
			String[] ids = id.split("\\.");
			d.sensorId = Integer.parseInt(ids[0]);
			d.unitcode = Byte.parseByte(ids[1]);

			switch (valueSelector) {
			case COMMAND:
				if (type instanceof OnOffType) {
					d.command = (type == OnOffType.ON ? Commands.ON
							: Commands.OFF);
					d.dimmingLevel = (type == OnOffType.ON ? DimmingLevel.LEVEL0
							: DimmingLevel.LEVEL100);
					obj = d;
				} else {
					throw new NumberFormatException("Can't convert " + type
							+ " to Command");
				}
				break;

			case DIMMING_LEVEL:
				if (type instanceof OnOffType) {
					d.command = (type == OnOffType.ON ? Commands.ON
							: Commands.OFF);
					d.dimmingLevel = (type == OnOffType.ON ? DimmingLevel.LEVEL0
							: DimmingLevel.LEVEL100);
					obj = d;
				} else if (type instanceof PercentType) {

				} else {
					throw new NumberFormatException("Can't convert " + type
							+ " to Command");
				}
				break;

			default:
				break;

			}
			break;

		case TEMPERATURE_HUMIDITY:
			break;
		case INTERFACE_CONTROL:
		case INTERFACE_MESSAGE:
		case UNKNOWN:
		default:
			break;
		}

		return obj;
	}

}
