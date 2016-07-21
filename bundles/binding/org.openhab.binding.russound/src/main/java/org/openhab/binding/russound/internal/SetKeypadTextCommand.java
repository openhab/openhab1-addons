/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.russound.internal;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetKeypadTextCommand implements RussoundBusListener {

	private static final Logger logger = LoggerFactory
			.getLogger(SetKeypadTextCommand.class);

	public boolean matches(byte[] bytes) {
		return bytes[0] == (byte) 0xF0 && bytes[7] == (byte) 0x0
				&& bytes[8] == (byte) 0x02 && bytes[9] == (byte) 0x01
				&& bytes[10] == (byte) 0x01 && bytes[11] == (byte) 0x02;
	}

	public void notify(byte[] bytes) {
		int controller = bytes[1] + 1;
		int zone = bytes[2] + 1;
		// 21 time to display
		int timeToDisplay = bytes[21];
		// 23 text
		// let's figure out where text ends, look for 0x00 after 23
		int endIndex = -1;
		for (int i = 23; i < bytes.length; i++)
			if (bytes[i] == 0x00)

				endIndex = i;
		String displayText = "unknown";
		if (endIndex > -1)
			displayText = new String(Arrays.copyOfRange(bytes, 23, endIndex));
		StringBuilder returnText = new StringBuilder("Set Text Event: ");
		returnText.append("Controller:").append(controller);
		returnText.append(", Zone:").append(zone);
		returnText.append(", Display Text: ").append(displayText);
		logger.debug(returnText.toString());
	}

	public ZoneAddress getZoneAddress(byte[] bytes) {
		if (matches(bytes))
			return new ZoneAddress(bytes[1], bytes[2]);
		else
			throw new IllegalArgumentException(
					"Bytes are in invalid format for zone text listener");
	}
}
