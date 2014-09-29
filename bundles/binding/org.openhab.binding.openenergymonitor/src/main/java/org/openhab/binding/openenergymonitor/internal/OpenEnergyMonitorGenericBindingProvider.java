/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
 * <li><code>openenergymonitor="phase1RealPower+phase2RealPower+phase3RealPower"</code></li>
 * </ul>
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class OpenEnergyMonitorGenericBindingProvider extends
		AbstractGenericBindingProvider implements
		OpenEnergyMonitorBindingProvider {

	/** RegEx to extract a transformation string <code>'(.*?)\((.*)\)'</code> */
	private static final Pattern REGEX_EXTRACT_PATTERN = Pattern
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

		config.variable = configParts[0].trim();

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
		Matcher matcher = REGEX_EXTRACT_PATTERN.matcher(transformation);

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
		String transformationType = null;
		String transformationFunction = null;

		@Override
		public String toString() {
			return "OpenEnergyMonitorBindingConfigElement ["
					+ "variable=" + variable 
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
