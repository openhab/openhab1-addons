package org.openhab.binding.dsmr.internal.messages;

import org.openhab.core.types.State;

/**
 * Base class for OBIS Message implementation
 * 
 * @author M. Volaart
 * @since 1.7.0
 * 
 * @param <T> Class representing the OpenHab type of the OBIS value
 */
public abstract class OBISMessage<T extends State> {
	// OBIS Message Type
	private final OBISMsgType type;
	
	// Java type of the value
	protected T openHabValue;

	/**
	 * Construct a new OBISMessage with the specified OBIS Message Type
	 * 
	 * @param type {@link OBISMsgType}
	 */
	protected OBISMessage(OBISMsgType type) {
		this.type = type;
	}

	/**
	 * Return the {@link OBISMsgType}
	 * @return the {@link OBISMsgType}
	 */
	public OBISMsgType getType() {
		return type;
	}

	/**
	 * Returns string representation of this OBISMessage
	 */
	public String toString() {
		return "OBIS Message(type:" + type.toString() + ", description:"
				+ type.description + ", OpenHabValue:" + openHabValue + ")";
	}

	/**
	 * Returns the OpenHab value for this OBISMessage
	 * 
	 * @return value of generic type T
	 */
	public T getOpenHabValue() {
		return openHabValue;
	}
}
