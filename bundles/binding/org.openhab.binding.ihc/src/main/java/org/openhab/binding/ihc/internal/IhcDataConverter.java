/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.internal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.openhab.binding.ihc.ws.IhcEnumValue;
import org.openhab.binding.ihc.ws.datatypes.WSBooleanValue;
import org.openhab.binding.ihc.ws.datatypes.WSDateValue;
import org.openhab.binding.ihc.ws.datatypes.WSEnumValue;
import org.openhab.binding.ihc.ws.datatypes.WSFloatingPointValue;
import org.openhab.binding.ihc.ws.datatypes.WSIntegerValue;
import org.openhab.binding.ihc.ws.datatypes.WSResourceValue;
import org.openhab.binding.ihc.ws.datatypes.WSTimeValue;
import org.openhab.binding.ihc.ws.datatypes.WSTimerValue;
import org.openhab.binding.ihc.ws.datatypes.WSWeekdayValue;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;

public class IhcDataConverter {

	/**
	 * Convert IHC data type to openHAB data type.
	 * 
	 * @param itemType
	 *            OpenHAB data type class
	 * @param value
	 *            IHC data value
	 * 
	 * @return openHAB {@link State}
	 */
	public static State convertResourceValueToState(
			Class<? extends Item> itemType, WSResourceValue value)
			throws NumberFormatException {

		org.openhab.core.types.State state = UnDefType.UNDEF;

		if (itemType == NumberItem.class) {

			if (value.getClass() == WSFloatingPointValue.class) {
				// state = new
				// DecimalType(((WSFloatingPointValue)value).getFloatingPointValue());

				// Controller might send floating point value with >10 decimals
				// (22.299999237060546875), so round value to have max 2
				// decimals
				double d = ((WSFloatingPointValue) value)
						.getFloatingPointValue();
				BigDecimal bd = new BigDecimal(d).setScale(2,
						RoundingMode.HALF_EVEN);
				state = new DecimalType(bd);
			}

			else if (value.getClass() == WSBooleanValue.class) {
				state = new DecimalType(((WSBooleanValue) value).isValue() ? 1 : 0);
			}
			
			else if (value.getClass() == WSIntegerValue.class) {
				state = new DecimalType(((WSIntegerValue) value).getInteger());
			}
			
			else if (value.getClass() == WSTimerValue.class) {
				state = new DecimalType(((WSTimerValue) value).getMilliseconds());
			}
			
			else if (value.getClass() == WSEnumValue.class) {
				state = new DecimalType(((WSEnumValue) value).getEnumValueID());
			}
			
			else if (value.getClass() == WSWeekdayValue.class) {
				state = new DecimalType(((WSWeekdayValue) value).getWeekdayNumber());
			}
			
			else {
				throw new NumberFormatException("Can't convert "
						+ value.getClass().toString() + " to NumberItem");
			}

		} else if (itemType == DimmerItem.class) {

			// Dimmer item extends SwitchItem, so it need to be handled before
			// SwitchItem

			if (value.getClass() == WSIntegerValue.class) {
				state = new PercentType(((WSIntegerValue) value).getInteger());

			} else {
				throw new NumberFormatException("Can't convert "
						+ value.getClass().toString() + " to NumberItem");
			}

		} else if (itemType == SwitchItem.class) {

			if (value.getClass() == WSBooleanValue.class) {
				if (((WSBooleanValue) value).isValue()) {
					state = OnOffType.ON;
				} else {
					state = OnOffType.OFF;
				}
			} else {
				throw new NumberFormatException("Can't convert "
						+ value.getClass().toString() + " to SwitchItem");
			}

		} else if (itemType == ContactItem.class) {

			if (value.getClass() == WSBooleanValue.class) {
				if (((WSBooleanValue) value).isValue()) {
					state = OpenClosedType.OPEN;
				} else {
					state = OpenClosedType.CLOSED;
				}
			} else {
				throw new NumberFormatException("Can't convert "
						+ value.getClass().toString() + " to ContactItem");
			}

		} else if (itemType == DateTimeItem.class) {

			if (value.getClass() == WSDateValue.class) {

				Calendar cal = WSDateTimeToCalendar((WSDateValue) value, null);
				state = new DateTimeType(cal);

			} else if (value.getClass() == WSTimeValue.class) {

				Calendar cal = WSDateTimeToCalendar(null, (WSTimeValue) value);
				state = new DateTimeType(cal);

			} else {

				throw new NumberFormatException("Can't convert "
						+ value.getClass().toString() + " to DateTimeItem");
			}

		} else if (itemType == StringItem.class) {

			if (value.getClass() == WSEnumValue.class) {

				state = new StringType(((WSEnumValue) value).getEnumName());

			} else {

				throw new NumberFormatException("Can't convert "
						+ value.getClass().toString() + " to StringItem");
			}

		} else if (itemType == RollershutterItem.class) {

			if (value.getClass() == WSIntegerValue.class)
				state = new PercentType(((WSIntegerValue) value).getInteger());

			else
				throw new NumberFormatException("Can't convert "
						+ value.getClass().toString() + " to NumberItem");

		}

		return state;
	}

	private static Calendar WSDateTimeToCalendar(WSDateValue date,
			WSTimeValue time) {

		Calendar cal = new GregorianCalendar(1900, 01, 01);

		if (date != null) {
			short year = date.getYear();
			short month = date.getMonth();
			short day = date.getDay();

			cal.set(year, month, day, 0, 0, 0);
		}

		if (time != null) {
			int hour = time.getHours();
			int minute = time.getMinutes();
			int second = time.getSeconds();

			cal.set(1900, 1, 1, hour, minute, second);
		}

		return cal;
	}

	/**
	 * Convert openHAB data type to IHC data type.
	 * 
	 * @param type
	 *            openHAB data type
	 * @param value
	 * 
	 * @param enumValues
	 * 
	 * @return IHC data type
	 */
	public static WSResourceValue convertCommandToResourceValue(Type type,
			WSResourceValue value, ArrayList<IhcEnumValue> enumValues) {

		if (type instanceof DecimalType) {

			if (value instanceof WSFloatingPointValue) {

				double newVal = ((DecimalType) type).doubleValue();
				double max = ((WSFloatingPointValue) value).getMaximumValue();
				double min = ((WSFloatingPointValue) value).getMinimumValue();

				if (newVal >= min && newVal <= max) {
					((WSFloatingPointValue) value).setFloatingPointValue(newVal);
				} else {
					throw new NumberFormatException(
							"Value is not between accetable limits (min=" + min
									+ ", max=" + max + ")");
				}

			} else if (value instanceof WSBooleanValue) {

				((WSBooleanValue) value).setValue(((DecimalType) type)
						.intValue() > 0 ? true : false);

			} else if (value instanceof WSIntegerValue) {

				int newVal = ((DecimalType) type).intValue();
				int max = ((WSIntegerValue) value).getMaximumValue();
				int min = ((WSIntegerValue) value).getMinimumValue();

				if (newVal >= min && newVal <= max) {
					((WSIntegerValue) value).setInteger(newVal);
				} else {
					throw new NumberFormatException(
							"Value is not between accetable limits (min=" + min
									+ ", max=" + max + ")");
				}

			} else if (value instanceof WSTimerValue) {

				((WSTimerValue) value).setMilliseconds(((DecimalType) type).longValue());

			} else if (value instanceof WSWeekdayValue) {

				((WSWeekdayValue) value).setWeekdayNumber(((DecimalType) type).intValue());

			} else {

				throw new NumberFormatException("Can't convert DecimalType to "
						+ value.getClass());

			}

		} else if (type instanceof OnOffType) {

			if (value instanceof WSBooleanValue) {

				((WSBooleanValue) value).setValue(type == OnOffType.ON ? true : false);

			} else if (value instanceof WSIntegerValue) {

				int newVal = type == OnOffType.ON ? 100 : 0;
				int max = ((WSIntegerValue) value).getMaximumValue();
				int min = ((WSIntegerValue) value).getMinimumValue();

				if (newVal >= min && newVal <= max) {
					((WSIntegerValue) value).setInteger(newVal);
				} else {
					throw new NumberFormatException(
							"Value is not between accetable limits (min=" + min
									+ ", max=" + max + ")");
				}

			} else {

				throw new NumberFormatException("Can't convert OnOffType to "
						+ value.getClass());

			}
		} else if (type instanceof OpenClosedType) {

			((WSBooleanValue) value).setValue(type == OpenClosedType.OPEN ? true : false);

		} else if (type instanceof DateTimeItem) {

			if (value instanceof WSDateValue) {

				short year = Short.parseShort(type.format("yyyy"));
				byte month = Byte.parseByte(type.format("MM"));
				byte day = Byte.parseByte(type.format("dd"));

				((WSDateValue) value).setYear(year);
				((WSDateValue) value).setMonth(month);
				((WSDateValue) value).setDay(day);

			} else if (value instanceof WSTimeValue) {

				int hours = Integer.parseInt(type.format("hh"));
				int minutes = Integer.parseInt(type.format("mm"));
				int seconds = Integer.parseInt(type.format("ss"));

				((WSTimeValue) value).setHours(hours);
				((WSTimeValue) value).setMinutes(minutes);
				((WSTimeValue) value).setSeconds(seconds);

			} else {

				throw new NumberFormatException(
						"Can't convert DateTimeItem to " + value.getClass());

			}

		} else if (type instanceof StringType) {

			if (value instanceof WSEnumValue) {

				boolean found = false;

				for (IhcEnumValue item : enumValues) {

					if (item.name.equals(type.toString())) {

						((WSEnumValue) value).setEnumValueID(item.id);
						((WSEnumValue) value).setEnumName(type.toString());
						found = true;
						break;
					}
				}

				if (found == false) {
					throw new NumberFormatException(
							"Can't find enum value for string "
									+ type.toString());
				}

			} else {

				throw new NumberFormatException("Can't convert StringType to "
						+ value.getClass());

			}

		} else if (type instanceof PercentType) {

			if (value instanceof WSIntegerValue) {

				int newVal = ((DecimalType) type).intValue();
				int max = ((WSIntegerValue) value).getMaximumValue();
				int min = ((WSIntegerValue) value).getMinimumValue();

				if (newVal >= min && newVal <= max) {
					((WSIntegerValue) value).setInteger(newVal);
				} else {
					throw new NumberFormatException(
							"Value is not between accetable limits (min=" + min
									+ ", max=" + max + ")");
				}

			} else {

				throw new NumberFormatException("Can't convert PercentType to "
						+ value.getClass());

			}

		} else if (type instanceof UpDownType) {

			if (value instanceof WSBooleanValue) {

				((WSBooleanValue) value).setValue(type == UpDownType.DOWN ? true : false);

			} else if (value instanceof WSIntegerValue) {

				int newVal = type == UpDownType.DOWN ? 100 : 0;
				int max = ((WSIntegerValue) value).getMaximumValue();
				int min = ((WSIntegerValue) value).getMinimumValue();

				if (newVal >= min && newVal <= max) {
					((WSIntegerValue) value).setInteger(newVal);
				} else {
					throw new NumberFormatException(
							"Value is not between accetable limits (min=" + min
									+ ", max=" + max + ")");
				}

			} else {

				throw new NumberFormatException("Can't convert UpDownType to "
						+ value.getClass());
			}

		} else {

			throw new NumberFormatException("Can't convert "
					+ type.getClass().toString());
		}

		return value;
	}
}
