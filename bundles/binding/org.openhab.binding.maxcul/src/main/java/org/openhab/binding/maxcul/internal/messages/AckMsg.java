/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcul.internal.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message class to handle ACK/NACK
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class AckMsg extends BaseMsg {

	final static private int ACK_MSG_PAYLOAD_LEN = 2;
	private boolean isNack;

	private static final Logger logger = LoggerFactory.getLogger(AckMsg.class);

	public AckMsg(String rawMsg) {
		super(rawMsg);
		if (this.payload.length == ACK_MSG_PAYLOAD_LEN) {
			isNack = (this.payload[0] == 0x81);
		} else {
			logger.error("Got " + this.msgType
					+ " message with incorrect length!");
		}
		/* TODO should probably check if it's a 0x01 for ACK */
	}

	public AckMsg(byte msgCount, byte msgFlag, byte groupId, String srcAddr,
			String dstAddr, boolean isNack) {
		super(msgCount, msgFlag, MaxCulMsgType.ACK, groupId, srcAddr, dstAddr);

		byte[] payload = new byte[ACK_MSG_PAYLOAD_LEN];
		payload[0] = 0x01;
		if (isNack) {
			payload[0] |= 0x80; // make 0x81 for NACK
		}
		super.appendPayload(payload);
		this.printDebugPayload();
	}

	public boolean getIsNack() {
		return isNack;
	}

	@Override
	protected void printFormattedPayload() {
		logger.debug("\tIs ACK?                  => " + (!this.isNack));
		logger.debug("\tUnknown ACK payload byte => " + this.payload[1]);
	}
}
