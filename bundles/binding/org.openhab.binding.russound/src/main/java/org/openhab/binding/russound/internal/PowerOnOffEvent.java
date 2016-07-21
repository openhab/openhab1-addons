/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.russound.internal;

import org.openhab.binding.russound.utils.StringHexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PowerOnOffEvent implements RussoundBusListener {

	private static final Logger logger = LoggerFactory
			.getLogger(PowerOnOffEvent.class);

	public boolean matches(byte[] bytes) {
		return bytes[0] == (byte) 0xF0 && bytes[7] == (byte) 0x05;
	}

	public void notify(byte[] bytes) {

		// logger.debug("Handling: " + StringHexUtils.byteArrayToHex(bytes));
		int controller = bytes[4] + 1;
		int zone = bytes[5] + 1;

		String cmd = "unspecified: " + StringHexUtils.byteArrayToHex(bytes);
		if (bytes[15] == (byte) 0x6c || bytes[14] == (byte) 0xf1
				&& bytes[15] == (byte) 0x23)
			cmd = "Power On/Off";
		else if (bytes[15] == (byte) 0x7f)
			cmd = "Volume Up";
		else if (bytes[15] == (byte) 0xf1 && bytes[16] == (byte) 0x37)
			cmd = "Volume Down";
		else if (bytes[15] == (byte) 0xf1 && bytes[16] == (byte) 0x3e)
			cmd = "Source Change";
		else if (bytes[15] == (byte) 0x6b)
			cmd = "Source Step";

		StringBuilder returnText = new StringBuilder("Russound Event: ");
		returnText.append("Controller:").append(controller);
		returnText.append(", Zone:").append(zone);
		returnText.append(", Command: ").append(cmd);

		// logger.debug(returnText.toString());
	}

	public ZoneAddress getZoneAddress(byte[] bytes) {
		if (matches(bytes))
			return new ZoneAddress(bytes[4], bytes[5]);
		else
			throw new IllegalArgumentException(
					"Bytes are in invalid format for zone power change");
	}
}
