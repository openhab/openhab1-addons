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

public class VolumeChangeBusParser implements BusParser {
	private static final Logger logger = LoggerFactory
			.getLogger(VolumeChangeBusParser.class);

	public ZoneAddress matches(byte[] bytes) {
		if (bytes[0] == (byte) 0xF0 && bytes[10] == (byte) 0xf1
				&& bytes[11] == (byte) 0x6f) {
			logger.debug("matches: " + true);
			return new ZoneAddress(bytes[1], bytes[2]);
		} else
			return null;
	}

	public void parse(byte[] bytes, AudioZone zone) {
		zone.setVolume(bytes[8] * 2);
	}

}
