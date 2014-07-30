/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcul.internal.messages;

/**
 * Message class to response to Pair Ping
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class PairPongMsg extends BaseMsg {

	final static private int PAIR_PONG_PAYLOAD_LEN = 1; /* in bytes */

	public PairPongMsg(byte msgCount, byte msgFlag, byte groupId,
			String srcAddr, String dstAddr) {
		super(msgCount, msgFlag, MaxCulMsgType.PAIR_PONG, groupId, srcAddr,
				dstAddr);

		byte[] payload = new byte[PAIR_PONG_PAYLOAD_LEN];

		payload[0] = 0x00;

		super.appendPayload(payload);
	}

	public PairPongMsg(String data) {
		super(data);
	}

}
