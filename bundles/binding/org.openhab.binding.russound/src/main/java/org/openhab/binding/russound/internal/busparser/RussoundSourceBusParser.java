/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.russound.internal.busparser;

import org.openhab.binding.russound.internal.AudioZone;
import org.openhab.binding.russound.internal.ZoneAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RussoundSourceBusParser implements BusParser {

	private static final Logger logger = LoggerFactory
			.getLogger(RussoundSourceBusParser.class);

	public RussoundSourceBusParser() {

	}

	public ZoneAddress matches(byte[] bytes) {
		if (bytes[0] == (byte) 0xF0 && bytes[6] == (byte) 0x7f
				&& bytes[7] == (byte) 0x06 && bytes[12] == (byte) 0x05) {
			logger.debug("matches: " + true);
			return new ZoneAddress(bytes[1], bytes[2]);
		} else
			return null;
	}

	public void parse(byte[] bytes, AudioZone zone) {
		zone.setSource(bytes[9]);

	}
}
