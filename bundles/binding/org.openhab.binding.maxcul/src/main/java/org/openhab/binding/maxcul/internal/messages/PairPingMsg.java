/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcul.internal.messages;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message class to handle Ping message in pairing
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class PairPingMsg extends BaseMsg {

	public int firmwareMajor;
	public int firmwareMinor;
	public int type;
	public int testResult;
	public String serial = null;

	private static final Logger logger = LoggerFactory
			.getLogger(PairPingMsg.class);

	public PairPingMsg(String rawMsg) {
		super(rawMsg);

		/* process payload */
		if (this.payload.length > 3) {
			this.firmwareMajor = this.payload[0] / 16;
			this.firmwareMinor = this.payload[0] % 16;
			this.type = this.payload[1];
			this.testResult = this.payload[2];
			this.serial = new String(Arrays.copyOfRange(this.payload, 3,
					this.payload.length));
		}
	}

	@Override
	protected void printFormattedPayload() {
		logger.debug("\tFirmware Version => " + this.firmwareMajor + "."
				+ this.firmwareMinor);
		logger.debug("\tDevice Type      => " + this.type);
		logger.debug("\tTest Result      => " + this.testResult);
		logger.debug("\tSerial Number    => " + this.serial);
	}
}
