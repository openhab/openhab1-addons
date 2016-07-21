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

public class ZoneInfoBusParser implements BusParser {

	private static final Logger logger = LoggerFactory
			.getLogger(ZoneInfoBusParser.class);

	public ZoneInfoBusParser() {

	}

	public ZoneAddress matches(byte[] bytes) {
		if (bytes[0] == (byte) 0xF0 && bytes[3] == (byte) 0x70
				&& bytes[9] == (byte) 0x04 && bytes[10] == (byte) 0x02) {
			ZoneAddress zoneAddress = new ZoneAddress(bytes[4], bytes[12]);
			logger.debug("Detected ZoneInfoMessage: " + zoneAddress);
			return zoneAddress;
		} else {
			logger.debug("No match");
			return null;
		}
	}

	public void parse(byte[] bytes, AudioZone zone) {
		if (bytes[20] == 1)
			zone.setPower(AudioZone.ZonePower.On);
		else
			zone.setPower(AudioZone.ZonePower.Off);

		zone.setSource(bytes[21]);
		zone.setVolume(bytes[22] * 2);
	}

}
