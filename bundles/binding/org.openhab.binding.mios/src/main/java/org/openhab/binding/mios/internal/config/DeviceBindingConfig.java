/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios.internal.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.mios.internal.MiosActivator;
import org.openhab.core.items.Item;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.Command;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author Mark Clark
 * @since 1.6.0
 */
public class DeviceBindingConfig extends MiosBindingConfig {

	private static final String DEFAULT_COMMAND_TRANSFORM = "_defaultCommand";

	private static Map<String, String> COMMAND_DEFAULTS = new HashMap<String, String>();

	static {
		COMMAND_DEFAULTS
				.put("ON",
						"urn:upnp-org:serviceId:SwitchPower1/SetTarget(newTargetValue=1)");
		COMMAND_DEFAULTS
				.put("OFF",
						"urn:upnp-org:serviceId:SwitchPower1/SetTarget(newTargetValue=0)");
		COMMAND_DEFAULTS.put("TOGGLE",
				"urn:micasaverde-com:serviceId:HaDevice1/ToggleState()");
		COMMAND_DEFAULTS.put("INCREASE",
				"urn:upnp-org:serviceId:Dimming1/StepUp()");
		COMMAND_DEFAULTS.put("DECREASE",
				"urn:upnp-org:serviceId:Dimming1/StepDown()");
	}

	private static Properties aliasMap = new Properties();
	private static String SERVICE_ALIASES = "org/openhab/binding/mios/internal/config/ServiceAliases.properties";

	static {
		InputStream input = DeviceBindingConfig.class.getClassLoader()
				.getResourceAsStream(SERVICE_ALIASES);

		try {
			aliasMap.load(input);
			logger.debug(
					"Successfully loaded UPnP Service Aliases from '{}', entries '{}'",
					SERVICE_ALIASES, aliasMap.size());
		} catch (Exception e) {
			// Pre-shipped with the Binding, so it should never error out.
			logger.error("Failed to load Service Alias file '{}', Exception",
					SERVICE_ALIASES, e);
		}
	}

	private static final Pattern SERVICE_IN_PATTERN = Pattern
			.compile("service/(?<serviceName>.+)/(?<serviceVar>.+)");

	private static final Pattern SERVICE_COMMAND_TRANSFORM_PATTERN = Pattern
			.compile("(?<transform>(?<transformCommand>[a-zA-Z]+)\\((?<transformParam>.*)\\))");

	private static final Pattern SERVICE_COMMAND_INMAP_PATTERN = Pattern
			.compile("(?<mapName>.+?)(=(?<mapValue>.+))?");

	private String inServiceName;
	private String inServiceVariable;

	private String commandTransformName;
	private String commandTransformParam;
	private Map<String, String> commandMap;

	private TransformationService commandTransformationService;

	private DeviceBindingConfig(String context, String itemName,
			String unitName, int id, String stuff,
			Class<? extends Item> itemType, String commandThing,
			String inTransform, String outTransform) {
		super(context, itemName, unitName, id, stuff, itemType, commandThing,
				inTransform, outTransform);
	}

	public static final MiosBindingConfig create(String context,
			String itemName, String unitName, int id, String inStuff,
			Class<? extends Item> itemType, String commandThing,
			String inTransform, String outTransform)
			throws BindingConfigParseException {
		try {
			// Before we initialize, normalize the serviceId string used in any
			// outgoing stuff.
			String newInStuff = inStuff;
			String newCommandThing = commandThing;

			// String newOutStuff = outStuff;

			String tmp;
			Matcher matcher;

			// Extract and Map the inbound names.
			String iName = null;
			String iVar = null;

			// Not a full match, will only modify things that start with
			// "/service/"
			matcher = SERVICE_IN_PATTERN.matcher(newInStuff);
			if (matcher.matches()) {
				iName = matcher.group("serviceName");
				iVar = matcher.group("serviceVar");

				// Handle service name aliases.
				tmp = (String) aliasMap.get(iName);
				if (tmp != null) {
					iName = tmp;
				}

				// Rebuild, since we've normalized the name.
				newInStuff = "service/" + iName + '/' + iVar;
			}

			String cTransform = null;
			String cParam = null;
			Map<String, String> cMap = null;

			if ("".equals(newCommandThing)) {
				// If it's present, but blank, use the global defaults.
				cMap = COMMAND_DEFAULTS;
			} else if (newCommandThing != null) {
				// Try for a match as a TransformationService.
				matcher = SERVICE_COMMAND_TRANSFORM_PATTERN
						.matcher(newCommandThing);
				if (matcher.matches()) {
					cTransform = matcher.group("transformCommand");
					cParam = matcher.group("transformParam");
				} else { // Try as an inline static command mapping.

					String[] commandList = newCommandThing.split("\\|");
					String command;

					Map<String, String> l = new HashMap<String, String>(
							commandList.length);

					for (int i = 0; i < commandList.length; i++) {
						command = commandList[i];
						matcher = SERVICE_COMMAND_INMAP_PATTERN
								.matcher(command);
						if (matcher.matches()) {
							String mapName = matcher.group("mapName");
							String mapValue = matcher.group("mapValue");
							String oldMapName = l.put(mapName, mapValue);

							if (oldMapName != null) {
								throw new BindingConfigParseException(
										String.format(
												"Duplicate inline Map entry '%s' in command: '%s'",
												oldMapName, newCommandThing));
							}
						} else {
							throw new BindingConfigParseException(
									String.format(
											"Invalid command, parameter format '%s' in command: '%s'",
											command, newCommandThing));
						}
					}
					cMap = l;
				}
			}

			logger.trace("newInStuff '{}', iName '{}', iVar '{}'",
					new Object[] { newInStuff, iName, iVar });

			DeviceBindingConfig c = new DeviceBindingConfig(context, itemName,
					unitName, id, newInStuff, itemType, newCommandThing,
					inTransform, outTransform);
			c.initialize();

			c.inServiceName = iName;
			c.inServiceVariable = iVar;

			c.commandTransformName = cTransform;
			c.commandTransformParam = cParam;
			c.commandMap = cMap;
			return c;
		} catch (Exception e) {
			logger.debug(e.toString());
			throw new BindingConfigParseException(e.getMessage());
		}
	}

	@Override
	public String getType() {
		return "device";
	}

	public String getInServiceName() {
		return inServiceName;
	}

	public String getInServiceVariable() {
		return inServiceVariable;
	}

	private String getCommandTransformName() {
		return commandTransformName;
	}

	private String getCommandTransformParam() {
		return commandTransformParam;
	}

	private Map<String, String> getCommandMap() {
		return commandMap;
	}

	private TransformationService getCommandTransformationService() {
		String name = getCommandTransformName();

		if (name == null || name.equals("")) {
			return null;
		}

		if (commandTransformationService != null) {
			return commandTransformationService;
		}

		BundleContext context = MiosActivator.getContext();

		commandTransformationService = TransformationHelper
				.getTransformationService(context, name);

		if (commandTransformationService == null) {

			logger.warn(
					"Transformation Service (command) '{}' not found for declaration '{}'.",
					name, getCommandThing());
		}

		return commandTransformationService;
	}

	@Override
	public String transformCommand(Command command)
			throws TransformationException {
		TransformationService ts = getCommandTransformationService();
		String result;

		String key = command.toString();
		if (ts != null) {
			result = ts.transform(getCommandTransformParam(), key);

			// If we don't have a transform, look for a special one called
			// "_default".
			if (result == null || "".equals(result)) {
				result = ts.transform(getCommandTransformParam(),
						DEFAULT_COMMAND_TRANSFORM);
			}
		} else {
			Map<String, String> map = getCommandMap();
			if (map != null) {
				String value = map.get(key);
				if (value != null) {
					result = value;
				} else { // Attempt to provide a default Mapping for it.
					if (map.containsKey(key)) {
						result = COMMAND_DEFAULTS.get(key);
					} else {
						result = key;
					}
				}
			} else {
				result = COMMAND_DEFAULTS.get(key);
			}
		}

		return result;
	}
}
