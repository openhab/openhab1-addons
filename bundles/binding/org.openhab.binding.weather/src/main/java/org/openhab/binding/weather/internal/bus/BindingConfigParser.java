/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.bus;

import java.math.RoundingMode;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.weather.internal.common.binding.ForecastBindingConfig;
import org.openhab.binding.weather.internal.common.binding.WeatherBindingConfig;
import org.openhab.binding.weather.internal.model.Weather;
import org.openhab.binding.weather.internal.utils.PropertyUtils;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Class to parse the key - value base config for an Weather item.
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * Number  Temperature    "Temperature [%.2f °C]"        {weather="locationId=home, type=temperature, property=current"} 
 * Number  Humidity       "Humidity [%.0f %]"            {weather="locationId=home, type=athmosphere, property=humidity"}
 * Number  Rain           "Rain [%.2f mm]"               {weather="locationId=home, type=precipitation, property=rain"}
 * 
 * Number  Temperature    "Temperature [%.0f °C]"        {weather="locationId=home, type=precipitation, property=rain, roundingMode=ceiling, scale=0"}
 *
 * </pre>
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class BindingConfigParser {

	/**
	 * Parses the bindingConfig of an item and returns a WeatherBindingConfig.
	 */
	public WeatherBindingConfig parse(Item item, String bindingConfig) throws BindingConfigParseException {
		bindingConfig = StringUtils.trimToEmpty(bindingConfig);
		bindingConfig = StringUtils.removeStart(bindingConfig, "{");
		bindingConfig = StringUtils.removeEnd(bindingConfig, "}");
		String[] entries = bindingConfig.split("[,]");
		WeatherBindingConfigHelper helper = new WeatherBindingConfigHelper();

		for (String entry : entries) {
			String[] entryParts = StringUtils.trimToEmpty(entry).split("[=]");
			if (entryParts.length != 2) {
				throw new BindingConfigParseException("A bindingConfig must have a key and a value");
			}
			String key = StringUtils.trim(entryParts[0]);

			String value = StringUtils.trim(entryParts[1]);
			value = StringUtils.removeStart(value, "\"");
			value = StringUtils.removeEnd(value, "\"");

			try {
				helper.getClass().getDeclaredField(key).set(helper, value);
			} catch (Exception e) {
				throw new BindingConfigParseException("Could not set value " + value + " for attribute " + key);
			}
		}

		if (!helper.isValid()) {
			throw new BindingConfigParseException("Invalid binding: " + bindingConfig);
		}

		WeatherBindingConfig weatherConfig = null;
		if (helper.isForecast()) {
			Integer forecast = parseInteger(helper.forecast, bindingConfig);
			if (forecast < 0) {
				throw new BindingConfigParseException("Invalid binding, forecast must be >= 0: " + bindingConfig);
			}
			weatherConfig = new ForecastBindingConfig(helper.locationId, forecast, helper.type, helper.property);
		} else {
			weatherConfig = new WeatherBindingConfig(helper.locationId, helper.type, helper.property);
		}

		String property = weatherConfig.getWeatherProperty();
		if (!Weather.isVirtualProperty(property) && !PropertyUtils.hasProperty(new Weather(null), property)) {
			throw new BindingConfigParseException("Invalid binding, unknown type or property: " + bindingConfig);
		}

		boolean isDecimalTypeItem = item.getAcceptedDataTypes().contains(DecimalType.class);
		if (isDecimalTypeItem || Weather.isVirtualProperty(property)) {
			RoundingMode roundingMode = RoundingMode.HALF_UP;
			if (helper.roundingMode != null) {
				try {
					roundingMode = RoundingMode.valueOf(StringUtils.upperCase(helper.roundingMode));
				} catch (IllegalArgumentException ex) {
					throw new BindingConfigParseException("Invalid binding, unknown roundingMode: " + bindingConfig);
				}
			}

			Integer scale = 2;
			if (helper.scale != null) {
				scale = parseInteger(helper.scale, bindingConfig);
				if (scale < 0) {
					throw new BindingConfigParseException("Invalid binding, scale must be >= 0: " + bindingConfig);
				}
			}
			weatherConfig.setScale(roundingMode, scale);
		}

		return weatherConfig;
	}

	/**
	 * Parse a string to a integer value.
	 */
	private Integer parseInteger(String valueString, String bindingConfig) throws BindingConfigParseException {
		try {
			return Integer.parseInt(valueString);
		} catch (Exception ex) {
			throw new BindingConfigParseException("Invalid binding, value " + valueString + " is not a number: "
					+ bindingConfig);
		}
	}

	/**
	 * Helper class for parsing the bindingConfig.
	 */
	private class WeatherBindingConfigHelper {
		public String locationId;
		public String type;
		public String property;
		public String forecast;
		public String roundingMode;
		public String scale;

		protected boolean isValid() {
			return StringUtils.isNotBlank(locationId) && StringUtils.isNotBlank(type)
					&& StringUtils.isNotBlank(property);
		}

		protected boolean isForecast() {
			return StringUtils.isNotBlank(forecast);
		}

	}

}
