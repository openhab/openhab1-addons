/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios.internal.config;

import java.util.HashMap;
import java.util.Map;
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

	// TODO: Externalize this, and it's initialization, into a file and/or
	// openhab.cfg
	private static Map<String, String> aliasMap = new HashMap<String, String>();

	static {
		// UPnP Service Name mappings
		aliasMap.put("SwitchPower1", "urn:upnp-org:serviceId:SwitchPower1");
		aliasMap.put("Dimming1", "urn:upnp-org:serviceId:Dimming1");
		aliasMap.put("TemperatureSensor1",
				"urn:upnp-org:serviceId:TemperatureSensor1");
		aliasMap.put("HVAC_FanOperatingMode1",
				"urn:upnp-org:serviceId:HVAC_FanOperatingMode1");
		aliasMap.put("HVAC_UserOperatingMode1",
				"urn:upnp-org:serviceId:HVAC_UserOperatingMode1");
		aliasMap.put("TemperatureSetpoint1_Heat",
				"urn:upnp-org:serviceId:TemperatureSetpoint1_Heat");
		aliasMap.put("TemperatureSetpoint1_Cool",
				"urn:upnp-org:serviceId:TemperatureSetpoint1_Cool");
		aliasMap.put("AVTransport", "urn:upnp-org:serviceId:AVTransport");
		aliasMap.put("RenderingControl",
				"urn:upnp-org:serviceId:RenderingControl");
		aliasMap.put("DeviceProperties",
				"urn:upnp-org:serviceId:DeviceProperties");
		aliasMap.put("HouseStatus1", "urn:upnp-org:serviceId:HouseStatus1");
		aliasMap.put("ContentDirectory",
				"urn:upnp-org:serviceId:ContentDirectory");
		aliasMap.put("AudioIn", "urn:upnp-org:serviceId:AudioIn");

		// MiCasaVerde mappings
		aliasMap.put("ZWaveDevice1",
				"urn:micasaverde-com:serviceId:ZWaveDevice1");
		aliasMap.put("ZWaveNetwork1",
				"urn:micasaverde-com:serviceId:ZWaveNetwork1");
		aliasMap.put("HaDevice1", "urn:micasaverde-com:serviceId:HaDevice1");
		aliasMap.put("SceneControllerLED1",
				"urn:micasaverde-com:serviceId:SceneControllerLED1");
		aliasMap.put("SecuritySensor1",
				"urn:micasaverde-com:serviceId:SecuritySensor1");
		aliasMap.put("HumiditySensor1",
				"urn:micasaverde-com:serviceId:HumiditySensor1");
		aliasMap.put("EnergyMetering1",
				"urn:micasaverde-com:serviceId:EnergyMetering1");
		aliasMap.put("SceneController1",
				"urn:micasaverde-com:serviceId:SceneController1");
		aliasMap.put("HVAC_OperatingState1",
				"urn:micasaverde-com:serviceId:HVAC_OperatingState1");
		aliasMap.put("SerialPort1", "urn:micasaverde-org:serviceId:SerialPort1");
		aliasMap.put("DoorLock1", "urn:micasaverde-com:serviceId:DoorLock1");
		aliasMap.put("AlarmPartition2",
				"urn:micasaverde-com:serviceId:AlarmPartition2");
		aliasMap.put("Camera1", "urn:micasaverde-com:serviceId:Camera1");

		// Misc Plugin mappings
		aliasMap.put("SystemMonitor",
				"urn:cd-jackson-com:serviceId:SystemMonitor");
		aliasMap.put("WPSwitch1", "urn:garrettwp-com:serviceId:WPSwitch1");
		aliasMap.put("Nest1", "urn:watou-com:serviceId:Nest1");
		aliasMap.put("NestStructure1", "urn:watou-com:serviceId:NestStructure1");
		aliasMap.put("Weather1", "urn:upnp-micasaverde-com:serviceId:Weather1");
		aliasMap.put("PingSensor1",
				"urn:demo-ted-striker:serviceId:PingSensor1");
		aliasMap.put("Sonos1", "urn:micasaverde-com:serviceId:Sonos1");
		aliasMap.put("ParadoxSecurityEVO1",
				"urn:demo-paradox-com:serviceId:ParadoxSecurityEVO1");
		aliasMap.put("LiftMasterOpener1",
				"urn:macrho-com:serviceId:LiftMasterOpener1");
		aliasMap.put("DirecTVDVR1", "urn:directv-com:serviceId:DVR1");
	}

	private static final Pattern SERVICE_IN_PATTERN = Pattern
			.compile("service/(?<serviceName>.+)/(?<serviceVar>.+)");

	private static final Pattern SERVICE_UPDATE_PATTERN = Pattern
			.compile("((?<serviceName>.+)/)?(?<serviceAction>.+)(\\((?<serviceParam>)\\))?");

	private static final Pattern SERVICE_COMMAND_TRANSFORM_PATTERN = Pattern
			.compile("(?<transform>(?<transformCommand>[a-zA-Z]+)\\((?<transformParam>.*)\\))");

	private static final Pattern SERVICE_COMMAND_INMAP_PATTERN = Pattern
			.compile("(?<mapName>.+?)(=(?<mapValue>.+))?");

	private String inServiceName;
	private String inServiceVariable;

	private String updateServiceName;
	private String updateServiceAction;
	private String updateServiceParam;

	private String commandTransformName;
	private String commandTransformParam;
	private Map<String, String> commandMap;

	private TransformationService commandTransformationService;

	private DeviceBindingConfig(String context, String unitName, int id,
			String stuff, Class<? extends Item> itemType, String commandThing,
			String updateThing, String inTransform, String outTransform) {
		super(context, unitName, id, stuff, itemType, commandThing,
				updateThing, inTransform, outTransform);
	}

	public static final MiosBindingConfig create(String context,
			String unitName, int id, String inStuff,
			Class<? extends Item> itemType, String commandThing,
			String updateThing, String inTransform, String outTransform)
			throws BindingConfigParseException {
		try {
			// Before we initialize, normalize the serviceId string used in any
			// outgoing stuff.
			String newInStuff = inStuff;
			String newCommandThing = commandThing;
			String newUpdateThing = updateThing;

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
				tmp = aliasMap.get(iName);
				if (tmp != null) {
					iName = tmp;
				}

				// Rebuild, since we've normalized the name.
				newInStuff = "service/" + iName + '/' + iVar;
			}

			// Extract and Map the outbound names (if present)
			String uName = null;
			String uAction = null;
			String uParam = null;

			if (newUpdateThing != null && !newUpdateThing.equals("")) {
				matcher = SERVICE_UPDATE_PATTERN.matcher(newUpdateThing);

				if (matcher.matches()) {
					uName = matcher.group("serviceName");
					uAction = matcher.group("serviceAction");
					uParam = matcher.group("serviceParam");

					// If it's null, because the user didn't specify the
					// Parameter section, then we set it to the empty string.
					// This will normalize the result to have "()" as the
					// parameter section.
					uParam = (uParam == null) ? "" : uParam;

					// If the ServiceName hasn't been specified, then use the
					// same
					// one that's used for the inBound binding.
					if (uName == null) {
						uName = iName;
					} else {
						tmp = aliasMap.get(uName);
						if (tmp != null) {
							uName = tmp;
						}
					}

					// Rebuild, since we've normalized the name.
					newUpdateThing = uName + '/' + uAction + '(' + uParam + ')';
				} else {
					throw new BindingConfigParseException(
							"Binding parameter 'update:' doesn't follow the required pattern");
				}
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

			logger.trace(
					"newInStuff '{}', newUpdateThing '{}', iName '{}', iVar '{}', uName '{}', uAction '{}', uParam '{}'",
					new Object[] { newInStuff, newUpdateThing, iName, iVar,
							uName, uAction, uParam });

			DeviceBindingConfig c = new DeviceBindingConfig(context, unitName,
					id, newInStuff, itemType, newCommandThing, newUpdateThing,
					inTransform, outTransform);
			c.initialize();

			c.inServiceName = iName;
			c.inServiceVariable = iVar;

			c.updateServiceName = uName;
			c.updateServiceAction = uAction;
			c.updateServiceParam = uParam;

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

	private String getUpdateServiceName() {
		return updateServiceName;
	}

	private String getUpdateServiceAction() {
		return updateServiceAction;
	}

	private String getUpdateServiceParam() {
		return updateServiceParam;
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

	public String transformCommand(Command command)
			throws TransformationException {
		TransformationService ts = getCommandTransformationService();

		String key = command.toString();
		if (ts != null) {
			return ts.transform(getCommandTransformParam(), key);
		} else {
			Map<String, String> map = getCommandMap();
			if (map != null) {
				String value = map.get(key);
				if (value != null) {
					return value;
				} else { // Attempt to provide a default Mapping for it.
					if (map.containsKey(key)) {
						return COMMAND_DEFAULTS.get(key);
					} else {
						return key;
					}
				}
			} else {
				return COMMAND_DEFAULTS.get(key);
			}
		}
	}
}
