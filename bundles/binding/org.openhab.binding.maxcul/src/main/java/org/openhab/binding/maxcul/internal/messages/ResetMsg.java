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
 * Factory reset device
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class ResetMsg extends BaseMsg {
	final static private int RESET_MESSAGE_PAYLOAD_LEN = 0; /* in bytes */

	public ResetMsg(byte msgCount, byte msgFlag, byte groupId, String srcAddr,
			String dstAddr) {
		super(msgCount, msgFlag, MaxCulMsgType.RESET, groupId, srcAddr, dstAddr);
		/* empty payload */
		byte[] payload = new byte[RESET_MESSAGE_PAYLOAD_LEN];
		super.appendPayload(payload);
	}
}
