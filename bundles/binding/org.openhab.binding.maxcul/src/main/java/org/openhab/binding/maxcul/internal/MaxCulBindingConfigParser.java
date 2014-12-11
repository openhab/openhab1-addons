/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcul.internal;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This function parses the Binding Configuration for Max! CUL bindings and
 * populates the configuration object
 * 
 * TODO implement tests to validate this parser
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class MaxCulBindingConfigParser {

	private static final Logger logger = LoggerFactory
			.getLogger(MaxCulBindingConfigParser.class);

	public static void parseMaxCulBindingString(String bindingConfigStr,
			MaxCulBindingConfig cfg) throws BindingConfigParseException {
		String[] configParts = bindingConfigStr.trim().split(":");

		if (bindingConfigStr.startsWith("PairMode")) {
			logger.debug("Pair Mode switch found");
			cfg.setDeviceType(MaxCulDevice.PAIR_MODE);
			return;
		} else if (bindingConfigStr.startsWith("ListenMode")) {
			logger.debug("Listen Mode switch found");
			cfg.setDeviceType(MaxCulDevice.LISTEN_MODE);
			return;
		} else if (bindingConfigStr.startsWith("CreditMonitor")) {
			logger.debug("Credit Monitor binding found");
			cfg.setDeviceType(MaxCulDevice.CREDIT_MONITOR);
		} else if (configParts.length < 2) {
			throw new BindingConfigParseException(
					"MaxCul configuration requires a configuration of at least the format <device_type>:<serial_num> for a MAX! device.");
		} else {

			logger.debug("Found real device");
			/* handle device type */
			logger.debug("Part 0/" + (configParts.length - 1) + " -> "
					+ configParts[0]);
			parseDeviceCategory(configParts[0], cfg);

			/* handle serial number */
			logger.debug("Part 1/" + (configParts.length - 1) + " -> "
					+ configParts[1]);
			cfg.setSerialNumber(configParts[1]);

			/* handle optional config items */
			if (configParts.length > 2) {
				// parts 3 onwards
				for (int idx = 2; idx < configParts.length; idx++) {
					logger.debug("Part " + idx + "/" + (configParts.length - 1)
							+ " -> " + configParts[idx]);
					if (configParts[idx].startsWith("configTemp")) {
						parseConfigTemp(configParts[idx], cfg);
					} else if (configParts[idx].startsWith("associate")) {
						parseAssociation(configParts[idx], cfg);
					} else if (configParts[idx].startsWith("feature")) {
						parseDeviceFeature(configParts[idx], cfg);
					}
				}
			} else {
				/* use defaults - handle all device types */
				switch (cfg.getDeviceType()) {
				case PUSH_BUTTON:
					cfg.setFeature(MaxCulFeature.SWITCH);
					break;
				case RADIATOR_THERMOSTAT:
					cfg.setFeature(MaxCulFeature.THERMOSTAT);
					break;
				case RADIATOR_THERMOSTAT_PLUS:
					cfg.setFeature(MaxCulFeature.THERMOSTAT);
					break;
				case SHUTTER_CONTACT:
					cfg.setFeature(MaxCulFeature.SWITCH);
					break;
				case WALL_THERMOSTAT:
					cfg.setFeature(MaxCulFeature.THERMOSTAT);
					break;
				case CREDIT_MONITOR:
				case PAIR_MODE:
				case LISTEN_MODE:
				case CUBE:
				case UNKNOWN:
					break;
				}
			}
			/*
			 * load stored configuration from pairing (if present) except on
			 * pair/listen mode/credit monitor bindings
			 */
			if (cfg.getDeviceType() != MaxCulDevice.PAIR_MODE
					|| cfg.getDeviceType() != MaxCulDevice.LISTEN_MODE 
					|| cfg.getDeviceType() != MaxCulDevice.CREDIT_MONITOR)
				cfg.loadStoredConfig();
		}
	}

	private static void parseDeviceCategory(String configPart,
			MaxCulBindingConfig cfg) throws BindingConfigParseException {
		cfg.setDeviceType(MaxCulDevice.UNKNOWN);

		if (configPart.equals("RadiatorThermostat")) {
			cfg.setDeviceType(MaxCulDevice.RADIATOR_THERMOSTAT);
		} else if (configPart.equals("RadiatorThermostatPlus")) {
			cfg.setDeviceType(MaxCulDevice.RADIATOR_THERMOSTAT_PLUS);
		} else if (configPart.equals("WallThermostat")) {
			cfg.setDeviceType(MaxCulDevice.WALL_THERMOSTAT);
		} else if (configPart.equals("PushButton")) {
			cfg.setDeviceType(MaxCulDevice.PUSH_BUTTON);
		} else if (configPart.equals("ShutterContact")) {
			cfg.setDeviceType(MaxCulDevice.SHUTTER_CONTACT);
		} else {
			throw new BindingConfigParseException(
					"Invalid device type. Use RadiatorThermostat / RadiatorThermostatPlus / WallThermostat / PushButton / ShutterContact");
		}
	}

	private static void parseDeviceFeature(String configParts,
			MaxCulBindingConfig cfg) throws BindingConfigParseException {
		String[] configPartArray = configParts.split("=");
		if (configPartArray.length == 2) {
			String configPart = configPartArray[1];
			if (configPart.equals("thermostat")) {
				if (cfg.getDeviceType() != MaxCulDevice.RADIATOR_THERMOSTAT
						&& cfg.getDeviceType() != MaxCulDevice.RADIATOR_THERMOSTAT_PLUS
						&& cfg.getDeviceType() != MaxCulDevice.WALL_THERMOSTAT)
					throw new BindingConfigParseException(
							"Invalid device feature. Can only use 'thermostat' on radiator or wall thermostats. This is a "
									+ cfg.getDeviceType());
				cfg.setFeature(MaxCulFeature.THERMOSTAT);
			} else if (configPart.equals("temperature")) {
				if (cfg.getDeviceType() != MaxCulDevice.RADIATOR_THERMOSTAT
						&& cfg.getDeviceType() != MaxCulDevice.RADIATOR_THERMOSTAT_PLUS
						&& cfg.getDeviceType() != MaxCulDevice.WALL_THERMOSTAT)
					throw new BindingConfigParseException(
							"Invalid device feature. Can only use 'temperature' on radiator or wall thermostats. This is a "
									+ cfg.getDeviceType());
				cfg.setFeature(MaxCulFeature.TEMPERATURE);
			} else if (configPart.equals("battery")) {
				cfg.setFeature(MaxCulFeature.BATTERY);
			} else if (configPart.equals("mode")) {
				if (cfg.getDeviceType() != MaxCulDevice.RADIATOR_THERMOSTAT
						&& cfg.getDeviceType() != MaxCulDevice.RADIATOR_THERMOSTAT_PLUS
						&& cfg.getDeviceType() != MaxCulDevice.WALL_THERMOSTAT)
					throw new BindingConfigParseException(
							"Invalid device feature. Can only use 'temperature' on radiator or wall thermostats. This is a "
									+ cfg.getDeviceType());
				cfg.setFeature(MaxCulFeature.MODE);
			} else if (configPart.equals("switch")) {
				if (cfg.getDeviceType() != MaxCulDevice.PUSH_BUTTON
						&& cfg.getDeviceType() != MaxCulDevice.SHUTTER_CONTACT)
					throw new BindingConfigParseException(
							"Invalid device feature. Can only use 'switch' on PushButton or ShutterContact. This is a {}"
									+ cfg.getDeviceType());
				cfg.setFeature(MaxCulFeature.TEMPERATURE);
			} else if (configPart.equals("valvepos")) {
				if (cfg.getDeviceType() != MaxCulDevice.RADIATOR_THERMOSTAT
						&& cfg.getDeviceType() != MaxCulDevice.RADIATOR_THERMOSTAT_PLUS)
					throw new BindingConfigParseException(
							"Invalid device feature. Can only use 'switch' on RadiatorThermostat or RadiatorThermostatPlus. This is a "
									+ cfg.getDeviceType());
				cfg.setFeature(MaxCulFeature.VALVE_POS);
			} else if (configPart.equals("reset")) {
				cfg.setFeature(MaxCulFeature.RESET);
			}
		}
	}

	private static void parseConfigTemp(String configPart,
			MaxCulBindingConfig cfg) throws BindingConfigParseException {
		String[] configKeyValueSplit = configPart.split("=");
		if (configKeyValueSplit.length == 2) {
			String[] configParts = configKeyValueSplit[1].split("/");
			if (configParts.length == 7) {
				// <comfortTemp>/<ecoTemp>/<maxTemp>/<minTemp>/<windowOpenTemperature>/<windowOpenDuration>/<measurementOffset>
				cfg.setComfortTemp(Double.parseDouble(configParts[0]));
				cfg.setEcoTemp(Double.parseDouble(configParts[1]));
				cfg.setMaxTemp(Double.parseDouble(configParts[2]));
				cfg.setMinTemp(Double.parseDouble(configParts[3]));
				cfg.setWindowOpenTemperature(Double.parseDouble(configParts[4]));
				cfg.setWindowOpenDuration(Double.parseDouble(configParts[5]));
				cfg.setMeasurementOffset(Double.parseDouble(configParts[6]));
				cfg.setTemperatureConfigSet(true);
			} else
				throw new BindingConfigParseException(
						"Temperature configuration should be of form 'configTemp=<comfortTemp>/<ecoTemp>/<maxTemp>/<minTemp>/<windowOpenTemperature>/<windowOpenDuration>/<measurementOffset>'");
		} else
			throw new BindingConfigParseException(
					"Temperature configuration should be of form 'configTemp=<comfortTemp>/<ecoTemp>/<maxTemp>/<minTemp>/<windowOpenTemperature>/<windowOpenDuration>/<measurementOffset>'");
	}

	private static void parseAssociation(String configPart,
			MaxCulBindingConfig cfg) throws BindingConfigParseException {
		String[] configKeyValueSplit = configPart.split("=");
		if (configKeyValueSplit.length == 2) {
			String[] associations = configKeyValueSplit[1].split(",");
			cfg.clearAssociatedSerialNum();
			for (int idx = 0; idx < associations.length; idx++) {
				cfg.addAssociatedSerialNum(associations[idx]);
			}
		} else
			throw new BindingConfigParseException(
					"Format of association configuration is incorrect! must be 'association=<serialNum>[,<serialNum>,[...]]'");
	}

	/**
	 * Validate if an item is of a valid type
	 * 
	 * @param config
	 *            Populated configuration to check
	 * @param item
	 *            Item to check
	 * @throws BindingConfigParseException
	 *             Thrown when item type is invalid
	 */
	public static void validateItemType(MaxCulBindingConfig config, Item item)
			throws BindingConfigParseException {
		switch (config.getDeviceType()) {
		case PAIR_MODE:
		case LISTEN_MODE:
			if (!(item instanceof SwitchItem))
				throw new BindingConfigParseException(
						"Invalid item type. PairMode/ListenMode can only be a switch");
			else if (config.getFeature() == MaxCulFeature.RESET
					&& !(item instanceof SwitchItem))
				throw new BindingConfigParseException(
						"Invalid item type. Feature 'reset' can only be a Switch");
			break;
		case PUSH_BUTTON:
		case SHUTTER_CONTACT:
			if (config.getFeature() == MaxCulFeature.BATTERY
					&& !(item instanceof SwitchItem))
				throw new BindingConfigParseException(
						"Invalid item type. Feature 'battery' can only be a Switch");
			else if (config.getFeature() == MaxCulFeature.SWITCH
					&& !(item instanceof SwitchItem))
				throw new BindingConfigParseException(
						"Invalid item type. Feature 'switch' can only be a Switch");
			else if (config.getFeature() == MaxCulFeature.RESET
					&& !(item instanceof SwitchItem))
				throw new BindingConfigParseException(
						"Invalid item type. Feature 'reset' can only be a Switch");
			break;
		case RADIATOR_THERMOSTAT:
		case RADIATOR_THERMOSTAT_PLUS:
		case WALL_THERMOSTAT:
			if (config.getFeature() == MaxCulFeature.TEMPERATURE
					&& !(item instanceof NumberItem))
				throw new BindingConfigParseException(
						"Invalid item type. Feature 'temperature' can only be a Number");
			else if (config.getFeature() == MaxCulFeature.VALVE_POS
					&& !(item instanceof NumberItem))
				throw new BindingConfigParseException(
						"Invalid item type. Feature 'valvepos' can only be a Number");
			else if (config.getFeature() == MaxCulFeature.THERMOSTAT
					&& !((item instanceof NumberItem) || (item instanceof SwitchItem)))
				throw new BindingConfigParseException(
						"Invalid item type. Feature 'thermostat' can only be a Number or a Switch");
			else if (config.getFeature() == MaxCulFeature.BATTERY
					&& !(item instanceof SwitchItem))
				throw new BindingConfigParseException(
						"Invalid item type. Feature 'battery' can only be a Switch");
			else if (config.getFeature() == MaxCulFeature.MODE
					&& !(item instanceof NumberItem))
				throw new BindingConfigParseException(
						"Invalid item type. Feature 'mode' can only be a Number");
			else if (config.getFeature() == MaxCulFeature.RESET
					&& !(item instanceof SwitchItem))
				throw new BindingConfigParseException(
						"Invalid item type. Feature 'reset' can only be a Switch");
			break;
		default:
			throw new BindingConfigParseException(
					"Invalid config device type. Wasn't expecting "
							+ config.getDeviceType());
		}
	}
}
