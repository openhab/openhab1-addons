/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.openenergymonitor.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.openenergymonitor.OpenEnergyMonitorBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * <p>
 * This class can parse information from the generic binding format and provides
 * Open Energy Monitor binding information from it.
 * 
 * <p>
 * Examples for valid binding configuration strings:
 * 
 * <ul>
 * <li><code>openenergymonitor="realPower"</code></li>
 * <li><code>openenergymonitor="phase1Current:JS(divideby100.js)"</code></li>
 * <li><code>openenergymonitor="phase1RealPower+phase2RealPower+phase3RealPower"</code></li>
 * <li><code>openenergymonitor="phase1Current+phase2Current+phase3Current:JS(divideby100.js)"</code></li>
 * <li><code>openenergymonitor="kwh(phase1RealPower+phase2RealPower+phase3RealPower)"</code></li>
 * <li><code>openenergymonitor="kwh/d(phase1RealPower+phase2RealPower+phase3RealPower)"</code></li>
 * </ul>
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class OpenEnergyMonitorGenericBindingProvider extends
		AbstractGenericBindingProvider implements
		OpenEnergyMonitorBindingProvider {

	/** RegEx to extract a parse a function String <code>'(.*?)\((.*)\)'</code> */
	private static final Pattern EXTRACT_FUNCTION_PATTERN = Pattern
			.compile("(.*?)\\((.*)\\)");

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "openenergymonitor";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof NumberItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only NumberItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		OpenEnergyMonitorBindingConfig config = new OpenEnergyMonitorBindingConfig();

		String[] configParts = bindingConfig.trim().split(":");

		if (configParts.length > 2) {
			throw new BindingConfigParseException(
					"Open Energy Monitor binding must contain 1-2 parts separated by ':'");
		}

		try {
			String[] parts = splitTransformationConfig(configParts[0].trim());

			if (parts.length > 1) {

				try {
					config.function = OpenEnergyMonitorFunctionType
							.getFunctionType(parts[0]);
				} catch (IllegalArgumentException e) {
					throw new BindingConfigParseException("'" + parts[0]
							+ "' is not a valid function type");
				}

				config.variable = parts[1];
			}
		} catch (IllegalArgumentException e) {
			config.variable = configParts[0].trim();
			config.function = null;
		}

		if (configParts.length == 2) {
			String[] parts = splitTransformationConfig(configParts[1].trim());
			config.transformationType = parts[0];
			config.transformationFunction = parts[1];
		} else {
			config.transformationType = null;
			config.transformationFunction = null;
		}

		addBindingConfig(item, config);
	}

	/**
	 * Splits a transformation configuration string into its two parts - the
	 * transformation type and the function/pattern to apply.
	 * 
	 * @param transformation
	 *            the string to split
	 * @return a string array with exactly two entries for the type and the
	 *         function
	 */
	protected String[] splitTransformationConfig(String transformation) {
		Matcher matcher = EXTRACT_FUNCTION_PATTERN.matcher(transformation);

		if (!matcher.matches()) {
			throw new IllegalArgumentException(
					"given transformation function '"
							+ transformation
							+ "' does not follow the expected pattern '<function>(<pattern>)'");
		}
		matcher.reset();

		matcher.find();
		String type = matcher.group(1);
		String pattern = matcher.group(2);

		return new String[] { type, pattern };
	}

	class OpenEnergyMonitorBindingConfig implements BindingConfig {
		public String variable = null;
		public OpenEnergyMonitorFunctionType function = null;;
		String transformationType = null;
		String transformationFunction = null;

		@Override
		public String toString() {
			return "OpenEnergyMonitorBindingConfigElement [variable="
					+ variable + ", function=" + function
					+ ", transformation type=" + transformationType
					+ ", transformation function=" + transformationFunction
					+ "]";
		}

	}

	@Override
	public String getVariable(String itemName) {
		OpenEnergyMonitorBindingConfig config = (OpenEnergyMonitorBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.variable : null;
	}

	@Override
	public OpenEnergyMonitorFunctionType getFunction(String itemName) {
		OpenEnergyMonitorBindingConfig config = (OpenEnergyMonitorBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.function : null;
	}

	@Override
	public String getTransformationType(String itemName) {
		OpenEnergyMonitorBindingConfig config = (OpenEnergyMonitorBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.transformationType : null;
	}

	@Override
	public String getTransformationFunction(String itemName) {
		OpenEnergyMonitorBindingConfig config = (OpenEnergyMonitorBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.transformationFunction : null;
	}

}
