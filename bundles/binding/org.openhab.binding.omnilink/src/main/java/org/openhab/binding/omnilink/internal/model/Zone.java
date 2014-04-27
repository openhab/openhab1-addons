/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.omnilink.internal.model;

import org.openhab.binding.omnilink.internal.OmniLinkBindingConfig;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StringType;

import com.digitaldan.jomnilinkII.MessageTypes.properties.ZoneProperties;

/**
 * Zones are contacts in a Omni System
 * 
 * @author Dan Cunningham
 * @since 1.5.0
 */
public class Zone extends OmnilinkDevice {
	private static final String CURRENT_TEXT[] = { "Secure", "Not Ready",
			"Trouble" };
	private static final String LATCHED_TEXT[] = { "Latch Secure",
			"Latch Tripped", "Latch Reset" };
	private static final String ARMING_TEXT[] = { "Disarmed", "Armed",
			"User Bypass", "System Bypass" };

	private ZoneProperties properties;

	public Zone(ZoneProperties properties) {
		this.properties = properties;
	}

	@Override
	public ZoneProperties getProperties() {
		return properties;
	}

	public void setProperties(ZoneProperties properties) {
		this.properties = properties;
	}

	@Override
	public void updateItem(Item item, OmniLinkBindingConfig config,
			EventPublisher publisher) {
		int current = ((properties.getStatus() >> 0) & 0x03);
		int latched = ((properties.getStatus() >> 2) & 0x03);
		int arming = ((properties.getStatus() >> 4) & 0x03);
		int trouble = ((properties.getStatus() >> 6) & 0x01);

		String latchedTxt = (latched < LATCHED_TEXT.length ? LATCHED_TEXT[latched]
				: "?");
		String armingTxt = (arming < ARMING_TEXT.length ? ARMING_TEXT[arming]
				: "?");
		String currentTxt = (current < CURRENT_TEXT.length ? CURRENT_TEXT[current]
				: "?");

		int setting = 0;
		String str = "";
		switch (config.getObjectType()) {
		case ZONE_STATUS_ARMING:
			setting = arming;
			str = armingTxt;
			break;
		case ZONE_STATUS_CURRENT:
			setting = current;
			str = currentTxt;
			break;
		case ZONE_STATUS_LATCHED:
			setting = latched;
			str = latchedTxt;
			break;
		case ZONE_STATUS_ALL:
			str = currentTxt + " | " + latchedTxt + " | " + arming;
			break;
		default:
			break;
		}
		if (item instanceof NumberItem) {
			publisher.postUpdate(item.getName(), new DecimalType(setting));
		} else if (item instanceof StringItem) {
			publisher.postUpdate(item.getName(), new StringType(str));
		} else if (item instanceof ContactItem) {
			publisher.postUpdate(item.getName(),
					current == 0 ? OpenClosedType.CLOSED : OpenClosedType.OPEN);
		}
	}
}
