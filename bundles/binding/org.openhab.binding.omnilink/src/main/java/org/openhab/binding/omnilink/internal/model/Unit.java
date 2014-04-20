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
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.digitaldan.jomnilinkII.MessageTypes.properties.UnitProperties;

/**
 * Units are most commonly lights, they can also be flags
 * 
 * @author Dan Cunningham
 * @since 1.5.0
 */
public class Unit extends OmnilinkDevice {
	private static final Logger logger = LoggerFactory.getLogger(Unit.class);
	public static final int UNIT_OFF = 0;
	public static final int UNIT_ON = 1;
	public static final int UNIT_SCENE_A = 2;
	public static final int UNIT_SCENE_L = 13;
	public static final int UNIT_DIM_1 = 17;
	public static final int UNIT_DIM_9 = 25;
	public static final int UNIT_BRIGHTEN_1 = 33;
	public static final int UNIT_BRIGHTEN_9 = 41;
	public static final int UNIT_LEVEL_0 = 100;
	public static final int UNIT_LEVEL_100 = 200;

	private UnitProperties properties;

	public Unit(UnitProperties properties) {
		this.properties = properties;
	}

	@Override
	public UnitProperties getProperties() {
		return properties;
	}

	public void setProperties(UnitProperties properties) {
		this.properties = properties;
	}

	@Override
	public void updateItem(Item item, OmniLinkBindingConfig config,
			EventPublisher publisher) {
		int status = properties.getState();
		logger.debug("Unit state {}", status);
		int level = 0;
		String display = "Off";
		if (status == UNIT_ON) {
			level = 100;
			display = "On";
		} else if ((status >= UNIT_SCENE_A) && (status <= UNIT_SCENE_L)) {
			level = 100;
			display = String.format("Scene %c", status - UNIT_SCENE_A + 'A');
		} else if ((status >= UNIT_DIM_1) && (status <= UNIT_DIM_9)) {
			// level = status - UNIT_DIM_1 +1;
			level = -1;
			display = String.format("Dim %d", level);
		} else if ((status >= UNIT_BRIGHTEN_1) && (status <= UNIT_BRIGHTEN_9)) {
			// level = status - UNIT_BRIGHTEN_1 +1;
			level = -1;
			display = String.format("Brighten %d", level);
		} else if ((status >= UNIT_LEVEL_0) && (status <= UNIT_LEVEL_100)) {
			level = status - UNIT_LEVEL_0;
			display = String.format("Level %d", level);
		}

		if (level >= 0 && level <= 100) {

			if (item instanceof DimmerItem) {
				logger.debug("updating percent type {}", level);
				publisher.postUpdate(item.getName(), new PercentType(level));
			} else if (item instanceof SwitchItem) {
				logger.debug("updating switch type {}",
						level > 0 ? OnOffType.ON : OnOffType.OFF);
				publisher.postUpdate(item.getName(), level > 0 ? OnOffType.ON
						: OnOffType.OFF);
			} else if (item instanceof StringItem) {
				logger.debug("updating switch type {}",
						level > 0 ? OnOffType.ON : OnOffType.OFF);
				publisher.postUpdate(item.getName(), new StringType(display));
			}
			for (String groupName : item.getGroupNames()) {
				publisher.postUpdate(groupName, level > 0 ? OnOffType.ON
						: OnOffType.OFF);
			}
		} else {
			logger.debug("Bad Level {}, not sending update", level);
		}

	}
}
