package org.openhab.binding.dsmr.internal.messages;

import java.text.ParseException;

import org.openhab.core.library.types.DecimalType;

/**
 * Class representing the OBIS Float Value.
 * <p>
 * When creating a new instance the constructor will convert the String value to a DecimalType.
 * 
 * @author M. Volaart
 * @since 1.7.0
 */
public class OBISFloatMsg extends OBISMessage<DecimalType> {
	/**
	 * Constructor for creating a new OBISFloatMsg.
	 * 
	 * @param type the {@link OBISMsgType} type of the OBIS value
	 * @param value String representing the value
	 * 
	 * @throws ParseException if parsing of the String value failed
	 */
	public OBISFloatMsg(OBISMsgType type, String value) throws ParseException {
		super(type);

		if (type.unit.length() > 0) {
			value = value.substring(0, value.indexOf('*'));
		}

		try {
			super.openHabValue = new DecimalType(Float.parseFloat(value));
		} catch (NumberFormatException nfe) {
			throw new ParseException("value:" + value
					+ " is not a float number", 0);
		}
	}
}
