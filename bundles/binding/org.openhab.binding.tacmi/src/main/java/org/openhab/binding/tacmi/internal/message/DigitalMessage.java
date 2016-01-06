/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tacmi.internal.message;

import org.openhab.core.library.types.OnOffType;
import org.slf4j.Logger;

/**
 * This class can be used to decode the digital values received in a messag and
 * also to create a new DigitalMessage used to send ON/OFF to an digital CAN
 * Input port
 * 
 * @author Timo Wendt
 * @since 1.8.0
 */
public final class DigitalMessage extends Message {

	public DigitalMessage(byte[] raw) {
		super(raw);
	}

	/**
	 * Create a new message to be sent to the CMI. It is only supported to use
	 * the first port for each CAN node. This is due to the fact that all
	 * digital port for the specific CAN node are send within a single message.
	 */
	public DigitalMessage(byte canNode, boolean state) {
		super(canNode, (byte) 0);
		logger.debug("DigitalMessage: canNode: {}, podNumber: {}",
				this.canNode, this.podNumber);
		int value = state ? 1 : 0;
		setValue(0, value, 0);
	}

	/**
	 * Get the state of the specified port number.
	 * 
	 * @param portNumber
	 * @return
	 */
	public boolean getPortState(int portNumber) {
		return getBit(getValue(0), portNumber - 1);
	}

	/**
	 * Get the state as OnOffType for Item.
	 * 
	 * @param portNumber
	 * @return
	 */
	public OnOffType getPortStateAsOnOffType(int portNumber) {
		boolean state = getPortState(portNumber);
		return state ? OnOffType.ON : OnOffType.OFF;
	}
	
	/**
	 * Read the specified bit from the short value holding the states of all 16
	 * ports.
	 * 
	 * @param portBits
	 * @param portBit
	 * @return
	 */
	private boolean getBit(int portBits, int portBit) {
		int result = (portBits >> portBit) & 0x1;
		return result == 1 ? true : false;
	}

	/**
	 * Set the specified bit to the defined value. Only used when sending data.
	 * 
	 * @param portBits
	 * @param portBit
	 * @param state
	 * @return
	 */
	private int setBit(int portBits, int portBit, boolean state) {
		portBits = (1 << portBit) | portBits;
		return portBits;
	}

	/**
	 * Check if message contains a value for the specified port number. For
	 * digital messages this is always true as all ports are send in
	 * a single message
	 * 
	 * @param portNumber
	 * @return
	 */
	@Override
	public boolean hasPortnumber(int portNumber) {
		return true;
	}
	
	@Override
	public void debug(Logger logger) {
	}

	@Override
	public MessageType getType() {
		return MessageType.D;
	}

}
