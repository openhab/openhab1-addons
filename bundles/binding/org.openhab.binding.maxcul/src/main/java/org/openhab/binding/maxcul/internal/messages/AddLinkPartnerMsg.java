/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcul.internal.messages;

import org.openhab.binding.maxcul.internal.MaxCulDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message to associate devices with each other
 *
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class AddLinkPartnerMsg extends BaseMsg {
	final private int ADD_LINK_PARTNER_PAYLOAD_LEN = 4;

	private static final Logger logger = LoggerFactory
			.getLogger(AddLinkPartnerMsg.class);

	private MaxCulDevice devType;
	private String partnerAddr;

	public AddLinkPartnerMsg(byte msgCount, byte msgFlag, byte groupId,
			String srcAddr, String dstAddr, String partnerAddr,
			MaxCulDevice devType) {
		super(msgCount, msgFlag, MaxCulMsgType.ADD_LINK_PARTNER, groupId,
				srcAddr, dstAddr);

		this.partnerAddr = partnerAddr;
		this.devType = devType;
		byte[] payload = new byte[ADD_LINK_PARTNER_PAYLOAD_LEN];

		payload[0] = (byte) (Integer.parseInt(partnerAddr.substring(0, 2), 16) & 0xff);
		payload[1] = (byte) (Integer.parseInt(partnerAddr.substring(2, 4), 16) & 0xff);
		payload[2] = (byte) (Integer.parseInt(partnerAddr.substring(4, 6), 16) & 0xff);
		payload[3] = (byte) devType.getDeviceTypeInt();
		super.appendPayload(payload);
	}

	@Override
	protected void printFormattedPayload() {
		super.printFormattedPayload();
		logger.debug("\tDevice Type  => " + this.devType);
		logger.debug("\tPartner Addr => " + this.partnerAddr);
	}
}
