package org.openhab.binding.dsmr.internal.messages;

import org.openhab.core.library.types.StringType;

/**
 * Class representing the OBIS String Value.
 * 
 * @author M. Volaart
 * @since 1.7.0
 */
public class OBISStringMsg extends OBISMessage<StringType> {
	/**
	 * Constructor for creating a new OBISStringMsg
	 * 
	 * @param type the {@link OBISMsgType} type of the OBIS value
	 * @param value String representing the value
	 */
	public OBISStringMsg(OBISMsgType type, String value) {
		super(type);

		super.openHabValue = new StringType(value);
	}
}
