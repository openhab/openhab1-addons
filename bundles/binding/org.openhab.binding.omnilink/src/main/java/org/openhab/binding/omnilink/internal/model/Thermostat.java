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
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.digitaldan.jomnilinkII.MessageUtils;
import com.digitaldan.jomnilinkII.MessageTypes.properties.ThermostatProperties;

/**
 * Basic thermostat
 * 
 * @author Dan Cunningham
 * @since 1.5.0
 */
public class Thermostat extends OmnilinkDevice {

	private static final Logger logger = LoggerFactory
			.getLogger(Thermostat.class);
	private ThermostatProperties properties;
	private boolean celsius;
	private static String[] MODES = { "Off", "Heat", "Cool", "Auto",
			"Emergency" };

	public Thermostat(ThermostatProperties properties, boolean celsius) {
		this.properties = properties;
		this.celsius = celsius;
	}

	@Override
	public ThermostatProperties getProperties() {
		return properties;
	}

	public void setProperties(ThermostatProperties properties) {
		this.properties = properties;
	}

	/**
	 * Returns the users preferred measurement type.
	 * @return
	 */
	public boolean isCelsius() {
		return celsius;
	}

	/**
	 * Sets the users preferred measurement type.
	 * @param celsius
	 */
	public void setCelsius(boolean celsius) {
		this.celsius = celsius;
	}

	@Override
	public void updateItem(Item item, OmniLinkBindingConfig config,
			EventPublisher publisher) {
		int setting = 0;
		String mode = null;
		switch (config.getObjectType()) {
		case THERMO_COOL_POINT:
			setting = celsius ? MessageUtils.omniToC(properties
					.getCoolSetpoint()) : MessageUtils.omniToF(properties
					.getCoolSetpoint());
			break;
		case THERMO_TEMP:
			setting = celsius ? MessageUtils.omniToC(properties
					.getTemperature()) : MessageUtils.omniToF(properties
					.getTemperature());
			break;
		case THERMO_HEAT_POINT:
			setting = celsius ? MessageUtils.omniToC(properties
					.getHeatSetpoint()) : MessageUtils.omniToF(properties
					.getHeatSetpoint());
			break;
		case THERMO_FAN_MODE:
			setting = properties.isFan() ? 1 : 0;
			break;
		case THERMO_HOLD_MODE:
			setting = properties.isHold() ? 1 : 0;
			break;
		case THERMO_SYSTEM_MODE:
			setting = properties.getMode();
			if (setting < MODES.length)
				mode = MODES[setting];
			break;
		default:
			return;
		}
		logger.debug("updating item {} for type {} to  {}", item.getName(),
				config.getObjectType(), setting);
		if (item instanceof NumberItem) {
			publisher.postUpdate(item.getName(), new DecimalType(setting));
		} else if (item instanceof SwitchItem) {
			publisher.postUpdate(item.getName(), setting > 0 ? OnOffType.ON
					: OnOffType.OFF);

		} else if (item instanceof StringItem && mode != null) {
			publisher.postUpdate(item.getName(), new StringType(mode));
		}
	}

	public static int getModeForString(String modeString) {
		for (int i = 0; i < MODES.length; i++) {
			if (modeString.equalsIgnoreCase(MODES[i]))
				return i;
		}
		return -1;
	}
}
