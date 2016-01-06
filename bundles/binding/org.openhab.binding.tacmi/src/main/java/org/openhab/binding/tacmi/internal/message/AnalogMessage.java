/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tacmi.internal.message;

import org.openhab.binding.tacmi.internal.TACmiMeasureType;
import org.openhab.core.library.types.DecimalType;
import org.slf4j.Logger;

/**
 * Format of analog messages is as follows:
 *    1        2      3       4       5       6        7      8       9       10     11     12     13      14
 *    0        1      2       3       4       5        6      7       8        9     10     11     12      13
 * canNode 1|2|3|4 1.lower 1.upper 2.lower 2.upper 3.lower 3.upper 4.lower 4.upper 1.type 2.type 3.type 4.type
 * @author Timo Wendt
 * @since 1.7.0
 */

/**
 * This class can be used to decode the analog values received in a message and
 * also to create a new AnalogMessage used to send analog values to an analog
 * CAN Input port. Creation of new message is not implemented so far.
 * 
 * @author Timo Wendt
 * @since 1.8.0
 */
public final class AnalogMessage extends Message {

	/**
	 * Used to parse the data received from the CMI.
	 * 
	 * @param raw
	 */
	public AnalogMessage(byte[] raw) {
		super(raw);
	}

	/**
	 * Create a new message to be sent to the CMI. It is only supported to use
	 * the first port for each podNumber. 
	 */
	public AnalogMessage(byte canNode, int podNumber, DecimalType value, TACmiMeasureType measureType) {
		super(canNode, (byte) podNumber);
		logger.debug("AnalogMessage: canNode: {}, podNumber: {}, value: {}, type: {}",
				this.canNode, this.podNumber, value, measureType);
		setValue(0, value.intValue(), measureType.getMeasure());
	}
	
	/**
	 * Get the value for the specified port number.
	 * 
	 * @param portNumber
	 * @return
	 */
	public AnalogValue getAnalogValue(int portNumber) {
		// Get the internal index for portNumber within the message
		int idx = (portNumber - 1) % 4;
		AnalogValue value = new AnalogValue(this.getValue(idx),
				getMeasureType(idx));
		return value;
	}

	/**
	 * Check if message contains a value for the specified port number. It
	 * doesn't matter though if the port has a value of 0.
	 * 
	 * @param portNumber
	 * @return
	 */
	@Override
	public boolean hasPortnumber(int portNumber) {
		return (int) ((portNumber - 1) / 4) == podNumber - 1 ? true : false;
	}

	@Override
	public void debug(Logger logger) {

	}

	@Override
	public MessageType getType() {
		return MessageType.A;
	}

}
