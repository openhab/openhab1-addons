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
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.digitaldan.jomnilinkII.MessageTypes.properties.AreaProperties;

/**
 * Areas represent the basic security system in a Omni System
 * 
 * @author Dan Cunningham
 * @since 1.5.0
 */
public class Area extends OmnilinkDevice {
	private static final Logger logger = LoggerFactory.getLogger(Area.class);

	public static final String[] omniText = { "Off", "Day", "Night", "Away",
			"Vacation", "Day-Instant", "Night-Delayed" };
	public static final String[] luminaText = { "Off", "Home", "Sleep", "Away",
			"Vacation", "Party", "Special" };
	public static final String[] alarms = { "Burglary", "Fire", "Gas",
			"Auxiliary", "Freeze", "Water", "Duress", "Temperature" };
	static int alarm_bits[] = { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80 };
	private AreaProperties properties;
	private boolean omni;

	public Area(AreaProperties properties, boolean omni) {
		this.properties = properties;
		this.omni = omni;
	}

	@Override
	public AreaProperties getProperties() {
		return properties;
	}

	public void setProperties(AreaProperties properties) {
		this.properties = properties;
	}

	/**
	 * Returns true if the system is a omni type
	 * @return
	 */
	public boolean isOmni() {
		return omni;
	}

	/**
	 * Set if this system is a omni type
	 * @param omni
	 */
	public void setOmni(boolean omni) {
		this.omni = omni;
	}

	@Override
	public void updateItem(Item item, OmniLinkBindingConfig config,
			EventPublisher publisher) {
		int setting = 0;
		String str = "";
		switch (config.getObjectType()) {
		case AREA_ENTRY_TIMER:
			setting = properties.getEntryTimer();
			break;
		case AREA_EXIT_TIMER:
			setting = properties.getExitTimer();
			break;
		case AREA_STATUS_ENTRY_DELAY:
			setting = properties.getEntryDelay();
			break;
		case AREA_STATUS_EXIT_DELAY:
			setting = properties.getExitDelay();
			break;
		case AREA_STATUS_MODE:
			setting = properties.getMode();
			str = omni ? (setting < omniText.length ? omniText[setting]
					: "Unknown")
					: (setting < luminaText.length ? luminaText[setting]
							: "Unknown");
			break;
		case AREA_STATUS_ALARM:
			setting = properties.getAlarms();

			for (int i = 0; i < alarms.length; i++) {
				if ((setting & alarm_bits[i]) != 0) {
					if (str.length() > 0)
						str += " | ";
					str += alarms[i];
				}
			}

			if (setting < alarms.length)
				;
			str = alarms[setting];
			break;
		default:
			break;
		}
		logger.debug("updating item {} for type {} to  {}", item.getName(),
				config.getObjectType(), setting);
		if (item instanceof NumberItem) {
			publisher.postUpdate(item.getName(), new DecimalType(setting));
		} else if (item instanceof StringItem) {
			publisher.postUpdate(item.getName(), new StringType(str));
		}
	}

	public int getModeForString(String mode) {
		String[] modes = omni ? omniText : luminaText;
		for (int i = 0; i < modes.length; i++) {
			if (modes[i].equalsIgnoreCase(mode)) {
				return i;
			}
		}
		// nothing
		return -1;
	}
}
