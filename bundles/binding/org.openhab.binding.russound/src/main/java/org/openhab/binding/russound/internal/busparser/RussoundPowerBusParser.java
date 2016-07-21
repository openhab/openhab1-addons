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
import org.openhab.binding.russound.internal.AudioZone.ZonePower;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RussoundPowerBusParser implements BusParser {

	private static final Logger logger = LoggerFactory
			.getLogger(RussoundPowerBusParser.class);

	public RussoundPowerBusParser() {

	}

	public ZoneAddress matches(byte[] bytes) {
		ZoneAddress returnValue = null;
		try {
			if (bytes[0] == (byte) 0xF0 && bytes[6] == (byte) 0x7f
					&& bytes[7] == (byte) 0x05 && bytes[14] == (byte) 0xf1
					&& bytes[15] == (byte) 0x23)
				return new ZoneAddress(bytes[1], bytes[19]);
		} catch (ArrayIndexOutOfBoundsException e) {
			// this is fine, can be varying lengths of byte arrays
		}
		logger.debug("matches: " + returnValue);
		return returnValue;

	}

	public void parse(byte[] bytes, AudioZone zone) {
		if (bytes[17] == 1)
			zone.setPower(AudioZone.ZonePower.On);
		else
			zone.setPower(AudioZone.ZonePower.Off);

	}
}
