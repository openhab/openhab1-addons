/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.simplebinary.internal;

/**
 * Class holding message data
 * 
 * @author Vita Tucek
 * @since 1.8.0
 */
public class SimpleBinaryMessage {
	protected byte messageId;
	protected int deviceId;
	protected int itemAddress;
	
	/**
	 * Constructor
	 * 
	 * @param messageId Message ID
	 * @param deviceId  Device ID
	 * @param itemAddress Item address
	 */
	public SimpleBinaryMessage(byte messageId, int deviceId, int itemAddress) {
		this.messageId = messageId;
		this.deviceId = deviceId;
		this.itemAddress = itemAddress;
	}	

	/**
	 * Return device address
	 * 
	 * @return
	 */
	public int getDeviceId() {
		return this.deviceId;
	}
	
	/**
	 * Return item address
	 * 
	 * @return
	 */
	public int getItemAddress() {
		return this.itemAddress;
	}

	/**
	 * Return message ID
	 * 
	 * @return
	 */
	public byte getMessageId() {
		return this.messageId;
	}

	/**
	 * Return message type depending on message ID
	 * 
	 * @return
	 */
	public SimpleBinaryMessageType getMessageType() {
		switch (messageId) {
		case (byte) 0xD0:
			return SimpleBinaryMessageType.CHECKNEWDATA;
		case (byte) 0xD1:
		case (byte) 0xD2:
		case (byte) 0xD3:
		case (byte) 0xD4:
			return SimpleBinaryMessageType.QUERY;
		case (byte) 0xDA:
		case (byte) 0xDB:
		case (byte) 0xDC:
		case (byte) 0xDD:
		case (byte) 0xDE:
			return SimpleBinaryMessageType.DATA;
		case (byte) 0xE0:
			return SimpleBinaryMessageType.OK;
		case (byte) 0xE1:
			return SimpleBinaryMessageType.RESEND;
		case (byte) 0xE2:
			return SimpleBinaryMessageType.NODATA;
		case (byte) 0xE4:
			return SimpleBinaryMessageType.UNKNOWN_ADDRESS;
		case (byte) 0xE5:
			return SimpleBinaryMessageType.SAVING_ERROR;
		default:
			return SimpleBinaryMessageType.UNKNOWN;
		}
	}
}
