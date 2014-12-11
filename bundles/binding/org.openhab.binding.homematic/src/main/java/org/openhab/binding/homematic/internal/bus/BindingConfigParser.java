/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.bus;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.homematic.internal.config.BindingAction;
import org.openhab.binding.homematic.internal.config.binding.ActionConfig;
import org.openhab.binding.homematic.internal.config.binding.DatapointConfig;
import org.openhab.binding.homematic.internal.config.binding.HomematicBindingConfig;
import org.openhab.binding.homematic.internal.config.binding.ProgramConfig;
import org.openhab.binding.homematic.internal.config.binding.VariableConfig;
import org.openhab.binding.homematic.internal.converter.ConverterFactory;
import org.openhab.binding.homematic.internal.converter.state.Converter;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to parse the key - value based config for an Homematic item.
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * Dimmer   Light_Kitchen     "Light Kitchen [%.2f]"   {homematic="address=KEQ0012345, channel=1, parameter=PRESS_SHORT"}
 * 
 * Switch   Test_Program      "Start TestProgram"      {homematic="program=TestProgram"}
 * 
 * Switch   Var_Holidaymode   "Holidaymode"            {homematic="variable=Holidaymode"}
 * </pre>
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class BindingConfigParser {
	private static final Logger logger = LoggerFactory.getLogger(BindingConfigParser.class);

	/**
	 * Parses the bindingConfig of an item and returns a HomematicBindingConfig.
	 */
	public HomematicBindingConfig parse(Item item, String bindingConfig) throws BindingConfigParseException {
		bindingConfig = StringUtils.trimToEmpty(bindingConfig);
		bindingConfig = StringUtils.removeStart(bindingConfig, "{");
		bindingConfig = StringUtils.removeEnd(bindingConfig, "}");
		String[] entries = bindingConfig.split("[,]");
		HomematicBindingConfigHelper helper = new HomematicBindingConfigHelper();

		for (String entry : entries) {
			String[] entryParts = StringUtils.trimToEmpty(entry).split("[=]");
			if (entryParts.length != 2) {
				throw new BindingConfigParseException("Each entry must have a key and a value");
			}

			String key = StringUtils.trim(entryParts[0]);

			// convert entry id to device if necessary
			if ("id".equalsIgnoreCase(key)) {
				logger.info("Please change the Homematic binding with the attribute 'id' to 'address' in entry: "
						+ entry + " -> " + StringUtils.replace(entry, "id=", "address="));
				key = "address";
			}
			String value = StringUtils.trim(entryParts[1]);
			value = StringUtils.removeStart(value, "\"");
			value = StringUtils.removeEnd(value, "\"");

			try {
				helper.getClass().getDeclaredField(key).set(helper, value);
			} catch (Exception e) {
				throw new BindingConfigParseException("Could not set value " + value + " for attribute " + key);
			}
		}

		Converter<?> converter = null;
		// if (helper.isValidDatapoint() || helper.isValidVariable()) {
		// converter = instantiateConverter(helper.converter);
		// }

		BindingAction bindingAction = getBindingAction(item, helper.action);

		if (helper.isValidDatapoint()) {
			return new DatapointConfig(helper.address, helper.channel, helper.parameter, converter, bindingAction,
					helper.isForceUpdate());
		} else if (helper.isValidVariable()) {
			return new VariableConfig(helper.variable, converter, bindingAction, helper.isForceUpdate());
		} else if (helper.isValidProgram()) {
			if (!acceptsOnOffType(item)) {
				throw new BindingConfigParseException(
						"Programs can only be attached to items which accepts OnOffType commands, ignoring item "
								+ item.getName());
			}
			return new ProgramConfig(helper.program, bindingAction);
		} else if (bindingAction != null) {
			return new ActionConfig(bindingAction);
		} else {
			throw new BindingConfigParseException("Invalid binding: " + bindingConfig);
		}
	}

	/**
	 * If a converter is specified in the binding, try to instantiate it.
	 */
	@SuppressWarnings("unused")
	private Converter<?> instantiateConverter(String converterName) {
		Converter<?> converter = null;
		if (converterName != null) {
			try {
				converter = (Converter<?>) Class.forName(converterName).newInstance();
			} catch (Exception e) {
				try {
					converter = (Converter<?>) Class.forName(ConverterFactory.CONVERTER_PACKAGE + converterName)
							.newInstance();
				} catch (Exception e1) {
					logger.warn("Can't instantiate converter " + converterName + ", ignoring it!");
				}
			}
		}
		return converter;
	}

	/**
	 * Parses the BindingAction if available.
	 */
	private BindingAction getBindingAction(Item item, String action) {
		if (action == null) {
			return null;
		}

		BindingAction bindingAction = BindingAction.parse(action);
		if (bindingAction == null) {
			logger.warn("Can't parse action {}, only {} and {} allowed. Ignoring action parameter in item {}!", action,
					BindingAction.RELOAD_VARIABLES, BindingAction.RELOAD_DATAPOINTS, item.getName());
		} else if (!acceptsOnOffType(item)) {
			logger.warn(
					"Actions can only be attached to items which accepts OnOffType commands, ignoring action attribute in item {}!",
					action, item.getName());
			bindingAction = null;
		}
		return bindingAction;
	}

	/**
	 * Returns true, if the item accepts the OnOffType.
	 */
	private boolean acceptsOnOffType(Item item) {
		return item.getAcceptedCommandTypes().contains(OnOffType.class);
	}

	/**
	 * Helper class for parsing the bindingConfig.
	 */
	private class HomematicBindingConfigHelper {
		public String address;
		public String channel;
		public String parameter;
		@SuppressWarnings("unused")
		public String converter;
		public String variable;
		public String program;
		public String action;
		public String forceUpdate;

		protected boolean isValidDatapoint() {
			return StringUtils.isNotBlank(address) && StringUtils.isNotBlank(channel)
					&& StringUtils.isNotBlank(parameter);
		}

		protected boolean isValidVariable() {
			return StringUtils.isNotBlank(variable);
		}

		protected boolean isValidProgram() {
			return StringUtils.isNotBlank(program);
		}

		protected boolean isForceUpdate() {
			return "true".equalsIgnoreCase(forceUpdate);
		}
	}
}
