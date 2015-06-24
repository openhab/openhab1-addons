/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
 * SetGroupId Message
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.8.0
 */
public class SetDisplayActualTemperatureMsg extends BaseMsg {

	final private int SET_DISPLAY_ACTUAL_TEMPERATURE_PAYLOAD_LEN = 1;

	private static final Logger logger = LoggerFactory
			.getLogger(SetDisplayActualTemperatureMsg.class);

	private boolean displayActualTemperature;

	public SetDisplayActualTemperatureMsg(byte msgCount, byte msgFlag, String srcAddr,
			String dstAddr, boolean displayActualTemperature) {
		super(msgCount, msgFlag, MaxCulMsgType.SET_DISPLAY_ACTUAL_TEMP, (byte) 0x0,
				srcAddr, dstAddr);

		this.displayActualTemperature = displayActualTemperature;
		byte[] payload = new byte[SET_DISPLAY_ACTUAL_TEMPERATURE_PAYLOAD_LEN];

		payload[0] = (byte) (displayActualTemperature ? 0x4 : 0x0);

		super.appendPayload(payload);
	}

	public SetDisplayActualTemperatureMsg(String rawmsg) {
		super(rawmsg);
		this.displayActualTemperature = (this.payload[0] == 0x4);
	}

	/**
	 * Print output as decoded fields
	 */
	@Override
	protected void printFormattedPayload() {
		logger.debug("\t Display Actual Temp => " + this.displayActualTemperature);
	}
}
