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
 * A BindingConfig object targeted at MiOS Device Variables and Attributes.
 * <p>
 * 
 * The device-specific form of a MiOS Binding is:<br>
 * <ul>
 * <li><nobr>
 * <tt>mios="unit:<i>unitName</i>,device:<i>deviceId</i>/service/<i>serviceURN</i>/<i>serviceVariable</i>,<i>optionalTransformations</i></tt>
 * </nobr>
 * <li><nobr>
 * <tt>mios="unit:<i>unitName</i>,device:<i>deviceId</i>/service/<i>serviceAlias</i>/<i>serviceVariable</i>,<i>optionalTransformations</i></tt>
 * </nobr>
 * <li><nobr>
 * <tt>mios="unit:<i>unitName</i>,device:<i>deviceId</i>/<i>attrName</i>,<i>optionalTransformations</i></tt>
 * </nobr>
 * </ul>
 * <p>
 * 
 * 
 * Example Item declarations...<br>
 * <ul>
 * <li>
 * <code><nobr>Number MiOSMemoryAvailable "Available [%.0f KB]"  (BindingDemo) {mios="unit:house,device:382/service/urn:cd-jackson-com:serviceId:SystemMonitor/memoryAvailable"}</nobr></code>
 * <li>
 * <code><nobr>Number Weather_Temperature "Outside Temperature [%.1f F]" &lt;temperature&gt; (Weather_Chart) {mios="unit:house,device:318/service/urn:upnp-org:serviceId:TemperatureSensor1/CurrentTemperature"}</nobr></code>
 * <li>
 * <code><nobr>Switch FamilyTheatreLightsStatus "Family Theatre Lights" (GSwitch) {mios="unit:house,device:13/service/urn:upnp-org:serviceId:SwitchPower1/Status,command:ON|OFF,in:MAP(miosSwitchIn.map),out:MAP(miosSwitchOut.map)"}</nobr></code>
 * <li>
 * <code><nobr>Dimmer FamilyTheatreLightsLoadLevelStatus "Family Theatre Lights [%d] %" &lt;slider&gt; (GDimmer) {mios="unit:house,device:13/service/urn:upnp-org:serviceId:Dimming1/LoadLevelStatus,command:MAP(miosDimmerCommand.map)", autoupdate="false"}</nobr></code>
 * </ul>
 * <p>
 * 
 * There are also a set of pre-defined UPnP Service aliases, so a short-hand
 * notation can be used for common UPnP ServiceId's:<br>
 * <ul>
 * <li>
 * <code><nobr>Number MiOSMemoryAvailable "Available [%.0f KB]" (BindingDemo) {mios="unit:house,device:382/service/SystemMonitor/memoryAvailable"}</nobr></code>
 * <li>
 * <code><nobr>Number Weather_Temperature "Outside Temperature [%.1f F]" &lt;temperature&gt; (Weather_Chart) {mios="unit:house,device:318/service/TemperatureSensor1/CurrentTemperature"}</nobr></code>
 * <li>
 * <code><nobr>Switch FamilyTheatreLightsStatus "Family Theatre Lights" (GSwitch) {mios="unit:house,device:13/service/SwitchPower1/Status,command:ON|OFF,in:MAP(miosSwitchIn.map),out:MAP(miosSwitchOut.map)"}</nobr></code>
 * <li>
 * <code><nobr>Dimmer FamilyTheatreLightsLoadLevelStatus "Family Theatre Lights [%d] %" &lt;slider&gt; (GDimmer) {mios="unit:house,device:13/service/Dimming1/LoadLevelStatus,command:MAP(miosDimmerCommand.map)", autoupdate="false"}</nobr></code>
 * </ul>
 * <p>
 * 
 * The complete list of UPnP Service aliases is listed in the documentation
 * file, or can be seen in the file <code>ServiceAliases.properties</code>.
 * <p>
 * 
 * In addition to binding against a Device's Variables, you can bind to the set
 * of Device Attributes it exposes. Specifically it's <code>id</code> and
 * <code>status</code>:
 * <p>
 * <ul>
 * <li>
 * <code><nobr>Number   FamilyTheatreLightsId "ID [%d]" {mios="unit:house,device:13/id"}</nobr></code>
 * <li>
 * <code><nobr>String   FamilyTheatreLightsDeviceStatus "Device Status [%s]" {mios="unit:house,device:13/status,in:MAP(miosStatusIn.map)"}</nobr></code>
 * </ul>
 * <p>
 * 
 * <b>Optional Transformations</b>
 * <p>
 * In many cases, the values exposed by a MiOS Unit aren't suitable for use
 * within openHAB, and a value mapping/transformation may be needed.
 * <p>
 * 
 * Values may be transformed using an input transformation (<code>in:</code>) or
 * a command transformation (<code>command:</code>). In future, values will also
 * be transformed prior to being sent to a MiOS Unit via the output
 * transformation (<code>out:</code>)
 * <p>
 * 
 * Transformation files are provided, for the common input and command
 * transformations, in the <code>examples/transform</code> directory of the MiOS
 * Binding.
 * <p>
 * The examples presented here assume that these MAP transformation files have
 * been copied to the appropriate openHAB configuration location (typically
 * <code>configuration/transform</code>).
 * <p>
 * 
 * There are example Input transformation maps:
 * <ul>
 * <li><code>miosSwitchIn.map</code> - MiOS Switch device (
 * <code>service/SwitchPower1/Status</code>) properties when mapped to a
 * <code>Switch</code> Item.
 * <li><code>miosStatusIn.map</code> - MiOS Status attributes (
 * <code>/status</code>) to make them readable states when mapped to a
 * <code>String</code> Item.
 * <li><code>miosContactIn.map</code> - MiOS Security Sensor device (
 * <code>/service/SecuritySensor1/Tripped</code>) when mapped to a
 * <code>Contact</code> Item.
 * <li><code>miosZWaveStatusIn.map</code> - MiOS System attribute (
 * <code>system:/ZWaveStatus</code>) when mapped to a <code>String</code> Item.
 * <li><code>miosWeatherConditionGroupIn.map</code> - MiOS
 * </ul>
 * 
 * <p>
 * Example Output transformation maps:
 * <ul>
 * <li><code>miosSwitchOut.map</code> - MiOS
 * <li><code>miosContactOut.map</code> - MiOS
 * </ul>
 * 
 * <p>
 * And example Command transformation maps:
 * <ul>
 * <li><code>miosArmedCommand.map</code> - MiOS Security Sensor device (
 * <code>/service/SecuritySensor1/Armed</code>) when mapped to a
 * <code>Switch</code> Item.
 * <li><code>miosDimmerCommand.map</code> - MiOS Dimmer device (
 * <code>/service/Dimming1/LoadLevelStatus</code>) when mapped to a
 * <code>Dimmer</code> Item.
 * <li><code>miosLockCommand.map</code> - MiOS Lock device (
 * <code>/service/DoorLock1/Status</code>) properties when mapped to a
 * <code>Switch</code> Item.
 * <li><code>miosTStatModeStatusCommand.map</code> - MiOS Thermostat device (
 * <code>/service/HVAC_UserOperatingMode1/ModeStatus</code>) when mapped to a
 * <code>String</code> Item.
 * <li><code>miosTStatSetpointCoolCommand.map</code> - MiOS Thermostat device (
 * <code>/service/TemperatureSetpoint1_Cool/CurrentSetpoint</code>) when mapped
 * to a <code>Number</code> Item.
 * <li><code>miosTStatSetpointHeatCommand.map</code> - MiOS Thermostat device (
 * <code>/service/TemperatureSetpoint1_Heat/CurrentSetpoint</code>) when mapped
 * to a <code>Number</code> Item.
 * <li><code>miosTStatFanOperatingModeCommand.map</code> - MiOS Thermostat
 * device (<code>/service/HVAC_FanOperatingMode1/Mode</code>) when mapped to a
 * <code>String</code> Item.
 * <li><code>miosUPnPRenderingControlVolumeCommand.map</code> - MiOS UPnP Volume
 * (<code>service/RenderingControl/Volume</code>) property when mapped to a
 * <code>Dimmer</code> Item.
 * <li><code>miosUPnPRenderingControlMuteCommand.map</code> - MiOS UPnP Mute (
 * <code>service/RenderingControl/Mute</code>) property when mapped to a
 * <code>Switch</code> Item.
 * <li><code>miosUPnPTransportStatePlayModeCommand.map</code> - MiOS UPnP
 * PlayMode (<code>service/AVTransport/TransportState</code>) when mapped to a
 * <code>String</code> Item.
 * </ul>
 * <p>
 * 
 * More details on these mappings can be found in the
 * <code>README.md<code> file associated
 * with this Binding.
 * <p>
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

	private static HashMap<String, ParameterDefaults> paramDefaults = new HashMap<String, ParameterDefaults>();
	private static String PARAM_DEFAULTS = "org/openhab/binding/mios/internal/config/DeviceDefaults.properties";

	static {
		ClassLoader cl = DeviceBindingConfig.class.getClassLoader();
		InputStream input;

		input = cl.getResourceAsStream(SERVICE_ALIASES);

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

		input = cl.getResourceAsStream(PARAM_DEFAULTS);

		try {
			Properties tmp = new Properties();
			tmp.load(input);

			for (Map.Entry<Object, Object> e : tmp.entrySet()) {
				paramDefaults.put((String) e.getKey(),
						ParameterDefaults.parse((String) e.getValue()));
			}

			logger.debug(
					"Successfully loaded Device Parameter defaults from '{}', entries '{}'",
					PARAM_DEFAULTS, paramDefaults.size());
		} catch (Exception e) {
			// Pre-shipped with the Binding, so it should never error out.
			logger.error(
					"Failed to load Device Parameter defaults file '{}', Exception",
					PARAM_DEFAULTS, e);
		}
	}

	private static final Pattern SERVICE_IN_PATTERN = Pattern
			.compile("service/(?<serviceName>.+)/(?<serviceVar>.+)");

	private static final Pattern SERVICE_COMMAND_TRANSFORM_PATTERN = Pattern
			.compile("(?<transform>(?<transformCommand>[a-zA-Z]+)\\((?<transformParam>.*)\\))");

	private static final Pattern SERVICE_COMMAND_INMAP_PATTERN = Pattern
			.compile("(?<mapName>.+?)(=(?<serviceName>.+)/(?<serviceAction>.+))?");

	private String commandTransformName;
	private String commandTransformParam;
	private Map<String, String> commandMap;

	private TransformationService commandTransformationService;

	private DeviceBindingConfig(String context, String itemName,
			String unitName, int id, String stuff,
			Class<? extends Item> itemType, String commandTransform,
			String inTransform, String outTransform)
			throws BindingConfigParseException {
		super(context, itemName, unitName, id, stuff, itemType,
				commandTransform, inTransform, outTransform);
	}

	/**
	 * Static constructor-method.
	 * 
	 * @return an initialized MiOS Device Binding Configuration object.
	 */
	public static final MiosBindingConfig create(String context,
			String itemName, String unitName, int id, String inStuff,
			Class<? extends Item> itemType, String commandTransform,
			String inTransform, String outTransform)
			throws BindingConfigParseException {
		try {
			// Before we initialize, normalize the serviceId string used in any
			// outgoing stuff.
			String newInStuff = inStuff;

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

			//
			// Apply any "Default" values to the in:, out:, and command:
			// transformations prior
			// to converting them for internal usage.
			//
			ParameterDefaults pd = paramDefaults.get(newInStuff);
			if (pd != null) {
				logger.trace(
						"Device ParameterDefaults FOUND '{}' for '{}', '{}'",
						itemName, newInStuff, pd);
				if (commandTransform == null) {
					commandTransform = pd.getCommandTransform();
					logger.trace(
							"Device ParameterDefaults '{}' defaulted command: to '{}'",
							itemName, commandTransform);
				}
				if (inTransform == null) {
					inTransform = pd.getInTransform();
					logger.trace(
							"Device ParameterDefaults '{}' defaulted in: to '{}'",
							itemName, inTransform);
				}
				if (outTransform == null) {
					outTransform = pd.getOutTransform();
					logger.trace(
							"Device ParameterDefaults '{}' defaulted out: to '{}'",
							itemName, outTransform);
				}
			} else {
				logger.trace(
						"Device ParameterDefaults NOT FOUND '{}' for '{}'",
						itemName, newInStuff);
			}

			String cTransform = null;
			String cParam = null;
			Map<String, String> cMap = null;
			String newCommandTransform = commandTransform;

			if ("".equals(newCommandTransform)) {
				// If it's present, but blank, use the global defaults.
				cMap = COMMAND_DEFAULTS;
			} else if (newCommandTransform != null) {
				// Try for a match as a TransformationService.
				matcher = SERVICE_COMMAND_TRANSFORM_PATTERN
						.matcher(newCommandTransform);
				if (matcher.matches()) {
					cTransform = matcher.group("transformCommand");
					cParam = matcher.group("transformParam");
				} else { // Try as an inline static command mapping.

					String[] commandList = newCommandTransform.split("\\|");
					String command;

					Map<String, String> l = new HashMap<String, String>(
							commandList.length);

					String mapName;
					String serviceName;
					String serviceAction;

					for (int i = 0; i < commandList.length; i++) {
						command = commandList[i];
						matcher = SERVICE_COMMAND_INMAP_PATTERN
								.matcher(command);
						if (matcher.matches()) {
							mapName = matcher.group("mapName");
							serviceName = matcher.group("serviceName");
							serviceAction = matcher.group("serviceAction");

							// Handle any Service Aliases that might have been used in the inline Map.
							tmp = (String) aliasMap.get(serviceName);
							if (tmp != null) {
								serviceName = tmp;
							}

							String oldMapName = l.put(mapName, serviceName + '/' + serviceAction);

							if (oldMapName != null) {
								throw new BindingConfigParseException(
										String.format(
												"Duplicate inline Map entry '%s' in command: '%s'",
												oldMapName, newCommandTransform));
							}
						} else {
							throw new BindingConfigParseException(
									String.format(
											"Invalid command, parameter format '%s' in command: '%s'",
											command, newCommandTransform));
						}
					}
					cMap = l;
				}
			}

			logger.trace("newInStuff '{}', iName '{}', iVar '{}'",
					new Object[] { newInStuff, iName, iVar });

			DeviceBindingConfig c = new DeviceBindingConfig(context, itemName,
					unitName, id, newInStuff, itemType, newCommandTransform,
					inTransform, outTransform);
			c.initialize();

			c.commandTransformName = cTransform;
			c.commandTransformParam = cParam;
			c.commandMap = cMap;
			return c;
		} catch (Exception e) {
			logger.debug(e.toString());
			throw new BindingConfigParseException(e.getMessage());
		}
	}

	/**
	 * Returns the value "<code>device</code>".
	 * 
	 * @return the value "<code>device</code>"
	 */
	@Override
	public String getMiosType() {
		return "device";
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
					name, getCommandTransform());
		}

		return commandTransformationService;
	}

	@Override
	public String transformCommand(Command command)
			throws TransformationException {
		// Quickly return null if we don't support commands.
		if (getCommandTransform() == null) {
			return null;
		}

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
