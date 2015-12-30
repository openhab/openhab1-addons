/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tacmi.internal.message;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base message class handling generic functions.
 * 
 * @author Timo Wendt
 * @since 1.8.0
 */
public abstract class Message {

	protected final static Logger logger = LoggerFactory
			.getLogger(Message.class);

	/**
	 * ByteBuffer that stores the content of the message.
	 */
	private ByteBuffer buffer = null;

	/**
	 * CAN Node number used in the message
	 */
	public byte canNode;

	/**
	 * POD number used in the message
	 */
	public byte podNumber;

	/**
	 * Initialize from the bytes of a received message
	 * 
	 * @param raw
	 */
	public Message(byte[] raw) {
		this.buffer = ByteBuffer.wrap(raw);
		this.buffer.order(ByteOrder.LITTLE_ENDIAN);
		this.canNode = buffer.get(0);
		this.podNumber = buffer.get(1);
	}

	/**
	 * Used to create a new message with the specified CAN node and POD number
	 * 
	 * @param canNode
	 * @param podNumber
	 */
	public Message(int canNode, int podNumber) {
		this.buffer = ByteBuffer.allocate(14);
		this.buffer.order(ByteOrder.LITTLE_ENDIAN);
		setCanNode(canNode);
		setPodNumber(podNumber);
	}

	public abstract void debug(Logger logger);

	public abstract MessageType getType();
	
	public abstract boolean hasPortnumber(int portNumber);

	/**
	 * Get the byte array. This can be sent to the CMI.
	 * 
	 * @return raw
	 */
	public byte[] getRaw() {
		return buffer.array();
	}

	/**
	 * Set the CAN node number for this message
	 * 
	 * @param canNode
	 */
	public void setCanNode(int canNode) {
		buffer.put(0, (byte) (canNode & 0xf));
	}

	/**
	 * Set the POD number for this message
	 * 
	 * @param podNumber
	 */
	public void setPodNumber(int podNumber) {
		buffer.put(1, (byte) (podNumber & 0xf));
	}

	/**
	 * Set the value at th specified index within the message and the defined
	 * measure type. The measure type is only used in analog messages. Digital
	 * messages always use 0 for the measure types.
	 * 
	 * @param idx
	 * @param value
	 * @param measureType
	 */
	public void setValue(int idx, int value, int measureType) {
		buffer.putShort(idx + 2, (short) (value & 0xff));
		buffer.put(idx + 10, (byte) (measureType & 0xf));
	}

	/**
	 * Get the value at the specified index within the message. The value will
	 * be converted from thr signed short to an unsigned int.
	 * 
	 * @param idx
	 * @return
	 */
	public int getValue(int idx) {
		return (int) (buffer.getShort(idx * 2 + 2)) & 0xffff;
	}

	/**
	 * Get the measure type for the specified index within the message.
	 * 
	 * @param idx
	 * @return
	 */
	public int getMeasureType(int idx) {
		return (int) (buffer.get(idx + 10)) & 0xffff;
	}
	
	@Override
	public String toString() {
		return ("CAN: " + this.canNode + " POD: " + this.podNumber
				+ " Value1: " + getValue(0) + " Value2: " + getValue(1)
				+ " Value3: " + getValue(2) + " Value4: " + getValue(3)
				+ " MeasureType1 " + getMeasureType(0) + " MeasureType2 "
				+ getMeasureType(1) + " MeasureType3 " + getMeasureType(2)
				+ " MeasureType4 " + getMeasureType(3));
	}
}
