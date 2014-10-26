/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.ecobee.EcobeeBindingProvider;
import org.openhab.binding.ecobee.internal.messages.Selection;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * <p>
 * Ecobee bindings start with a &lt;, &gt; or =, to indicate if the item
 * receives values from the API (in binding), sends values to the API (out
 * binding), or both (bidirectional binding), respectively.
 * 
 * <p>
 * The first character is then followed by a section between square brackets ([
 * and ] characters):
 * 
 * <p>
 * <code>[&lt;thermostat&gt;#&lt;property&gt;]</code>
 * 
 * <p>
 * Where <code>thermostat</code> is a decimal thermostat identifier for in, out
 * and bidirectional bindings.
 * 
 * <p>
 * For out bindings only, <code>thermostat</code> can instead be selection
 * criteria that specify which thermostats to change. You can use either a
 * comma-separated list of thermostat identifiers, or, for non-EMS thermostats
 * only, a wildcard (the <code>*</code> character).
 * 
 * <p>
 * In the case of out bindings for EMS or Utility accounts, the
 * <code>thermostat</code> criteria can be a path to a management set (for
 * example, <code>/Toronto/Campus/BuildingA</code>).
 * 
 * <p>
 * The <code>thermostat</code> specification can be optionally prepended with a
 * specific app instance name as specified in <code>openhab.cfg</code>, as in
 * <code>condo.123456789</code> when you have specified
 * <code>ecobee:condo.scope</code> and <code>ecobee:condo.appkey</code>
 * properties in <code>openhab.cfg</code>.
 * 
 * <p>
 * <code>property</code> is one of a long list of thermostat properties than you
 * can read and optionally change. See the list below, and peruse this binding's
 * JavaDoc for all specifics as to their meanings.
 * 
 * <table>
 * <thead>
 * <tr>
 * <th>Property</th>
 * <th>In</th>
 * <th>Out</ht>
 * </tr>
 * </thead> <tbody>
 * <tr>
 * <td>name</td>
 * <td>X</td>
 * <td>X</td>
 * </tr>
 * <tr>
 * <td>runtime.actualTemperature</td>
 * <td>X</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>runtime.actualHumidity</td>
 * <td>X</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>settings.hvacMode</td>
 * <td>X</td>
 * <td>X</td>
 * </tr>
 * </tbody>
 * </table>
 * 
 * <p>
 * Example bindings:
 * <ul>
 * <li><code>{ ecobee="&lt;[123456789#name]" }</code>
 * <p>
 * Return the name of the thermostat whose ID is 123456789 using the default
 * Ecobee app instance (configured in openhab.cfg).</li>
 * <li><code>{ ecobee="&lt;[condo.987654321#runtime.actualTemperature]" }</code>
 * <p>
 * Return the current temperature read by the thermostat using the condo account
 * at ecobee.com.</li>
 * <li><code>{ ecobee="&gt;[543212345#settings.fanMinOnTime]" }</code>
 * <p>
 * Set the minimum number of minutes per hour the fan will run on thermostat ID
 * 543212345.</li>
 * <li><code>{ ecobee="&gt;[*#settings.hvacMode]" }</code>
 * <p>
 * Change the HVAC mode to one of <code>"auto"</code>,
 * <code>"auxHeatOnly"</code>, <code>"cool"</code>, <code>"heat"</code>, or
 * <code>"off"</code> on all thermostats registered in the default app instance.
 * </li>
 * <li>
 * <code>{ ecobee="&gt;[lakehouse.*#settings.backlightSleepIntensity]" }</code>
 * <p>
 * Changes the backlight sleep intensity on all thermostats at the lake house
 * (meaning, all thermostats registered to the lakehouse Ecobee account).</li>
 * </ul>
 * 
 * @author John Cocula
 * @since 1.6.0
 */
public class EcobeeGenericBindingProvider extends
		AbstractGenericBindingProvider implements EcobeeBindingProvider {

	private static class EcobeeBindingConfig implements BindingConfig {
		String userid;
		String thermostatIdentifier;
		String property;
		boolean inBound = false;
		boolean outBound = false;

		public EcobeeBindingConfig(final String userid,
				final String thermostatIdentifier, final String property,
				final boolean inBound, final boolean outBound) {
			this.userid = userid;
			this.thermostatIdentifier = thermostatIdentifier;
			this.property = property;
			this.inBound = inBound;
			this.outBound = outBound;
		}

		@Override
		public String toString() {
			return "EcobeeBindingConfig [userid=" + this.userid
					+ "thermostatIdentifier=" + this.thermostatIdentifier
					+ ", property=" + this.property + ", inBound="
					+ this.inBound + ", outBound=" + this.outBound + "]";
		}
	}

	private static Logger logger = LoggerFactory
			.getLogger(EcobeeGenericBindingProvider.class);

	private static final Pattern CONFIG_PATTERN = Pattern
			.compile(".\\[(.*)#(.*)\\]");

	// the first character in the above pattern
	private static final String IN_BOUND = "<";
	private static final String OUT_BOUND = ">";
	private static final String BIDIRECTIONAL = "=";

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "ecobee";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUserid(final String itemName) {
		final EcobeeBindingConfig config = (EcobeeBindingConfig) this.bindingConfigs
				.get(itemName);
		return config != null ? config.userid : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getThermostatIdentifier(final String itemName) {
		EcobeeBindingConfig config = (EcobeeBindingConfig) this.bindingConfigs
				.get(itemName);
		return config != null ? config.thermostatIdentifier : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProperty(final String itemName) {
		EcobeeBindingConfig config = (EcobeeBindingConfig) this.bindingConfigs
				.get(itemName);
		return config != null ? config.property : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInBound(final String itemName) {
		EcobeeBindingConfig config = (EcobeeBindingConfig) this.bindingConfigs
				.get(itemName);
		return config != null ? config.inBound : false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOutBound(final String itemName) {
		EcobeeBindingConfig config = (EcobeeBindingConfig) this.bindingConfigs
				.get(itemName);
		return config != null ? config.outBound : false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(final String context,
			final Item item, final String bindingConfig)
			throws BindingConfigParseException {
		logger.debug("Processing binding configuration: '{}'", bindingConfig);

		super.processBindingConfiguration(context, item, bindingConfig);

		boolean inBound = false;
		boolean outBound = false;

		if (bindingConfig.startsWith(IN_BOUND)) {
			inBound = true;
		} else if (bindingConfig.startsWith(OUT_BOUND)) {
			outBound = true;
		} else if (bindingConfig.startsWith(BIDIRECTIONAL)) {
			inBound = true;
			outBound = true;
		} else {
			throw new BindingConfigParseException("Item \"" + item.getName()
					+ "\" does not start with " + IN_BOUND + ", " + OUT_BOUND
					+ " or " + BIDIRECTIONAL + ".");
		}

		Matcher matcher = CONFIG_PATTERN.matcher(bindingConfig);

		if (!matcher.matches() || matcher.groupCount() != 2)
			throw new BindingConfigParseException("Config for item '"
					+ item.getName() + "' could not be parsed.");

		String userid = null;
		String thermostatIdentifier = matcher.group(1);
		if (thermostatIdentifier.contains(".")) {
			String[] parts = thermostatIdentifier.split("\\.");
			userid = parts[0];
			thermostatIdentifier = parts[1];
		}

		if (inBound && !Selection.isThermostatIdentifier(thermostatIdentifier)) {
			throw new BindingConfigParseException(
					"Only a single thermostat identifier is permitted in an in binding or bidirectional binding.");
		}

		String property = matcher.group(2);

		EcobeeBindingConfig config = new EcobeeBindingConfig(userid,
				thermostatIdentifier, property, inBound, outBound);

		addBindingConfig(item, config);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {

		logger.debug("validateItemType called with bindingConfig={}",
				bindingConfig);

		Matcher matcher = CONFIG_PATTERN.matcher(bindingConfig);

		if (!matcher.matches() || matcher.groupCount() != 2)
			throw new BindingConfigParseException("Config for item '"
					+ item.getName() + "' could not be parsed.");

		String property = matcher.group(2);

		logger.debug("validateItemType called with property={}", property);

		if (!EcobeeItemMapping.isValidItemType(item, property)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "'; only items that accept the '"
							+ EcobeeItemMapping.getStateClass(property)
									.getSimpleName()
							+ "' state are allowed - please check your *.items configuration.");
		}
	}
}
