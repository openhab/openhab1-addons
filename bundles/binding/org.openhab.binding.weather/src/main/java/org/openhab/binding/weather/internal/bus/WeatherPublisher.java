/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.bus;

import java.math.BigDecimal;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.openhab.binding.weather.WeatherBindingProvider;
import org.openhab.binding.weather.internal.common.WeatherContext;
import org.openhab.binding.weather.internal.common.binding.ForecastBindingConfig;
import org.openhab.binding.weather.internal.common.binding.WeatherBindingConfig;
import org.openhab.binding.weather.internal.model.Temperature;
import org.openhab.binding.weather.internal.model.Weather;
import org.openhab.binding.weather.internal.utils.ItemIterator;
import org.openhab.binding.weather.internal.utils.ItemIterator.ItemIteratorCallback;
import org.openhab.binding.weather.internal.utils.PropertyUtils;
import org.openhab.binding.weather.internal.utils.UnitUtils;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Publishes object properties to items.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class WeatherPublisher {
	private static final Logger logger = LoggerFactory.getLogger(WeatherPublisher.class);
	private WeatherContext context = WeatherContext.getInstance();

	private static WeatherPublisher instance = null;

	private WeatherPublisher() {
	}

	/**
	 * Returns the singleton instance of WeatherPublisher.
	 */
	public static WeatherPublisher getInstance() {
		if (instance == null) {
			instance = new WeatherPublisher();
		}
		return instance;
	}

	/**
	 * Republish the state of the item.
	 */
	public void republishItem(String itemName) {
		WeatherBindingConfig bindingConfig = null;
		for (WeatherBindingProvider provider : context.getProviders()) {
			if (bindingConfig == null) {
				bindingConfig = provider.getBindingFor(itemName);
			}
		}
		if (bindingConfig == null) {
			logger.warn("Weather binding for item {} not found", itemName);
		} else {
			publish(bindingConfig.getLocationId());
		}
	}

	/**
	 * Iterates through all items and publishes the states for the locationId.
	 */
	public void publish(final String locationId) {
		final Weather weather = context.getWeather(locationId);
		if (weather != null) {
			new ItemIterator().iterate(new ItemIteratorCallback() {

				@Override
				public void next(WeatherBindingConfig bindingConfig, String itemName) {
					if (bindingConfig.getLocationId().equals(locationId)) {
						try {
							Weather instance = getInstance(weather, bindingConfig);
							if (instance != null) {
								String weatherProperty = bindingConfig.getWeatherProperty();
								Object value = null;

								if (Weather.isVirtualProperty(weatherProperty)) {
									Temperature temp = instance.getTemperature();
									if (Weather.VIRTUAL_TEMP_MINMAX.equals(weatherProperty)) {
										Double min = UnitUtils.convertUnit(temp.getMin(), bindingConfig.getUnit(), bindingConfig.getWeatherProperty());
										Double max = UnitUtils.convertUnit(temp.getMax(), bindingConfig.getUnit(), bindingConfig.getWeatherProperty());
										value = getMinMax(min, max, bindingConfig);
									}
								} else {
									value = PropertyUtils.getPropertyValue(instance, weatherProperty);
									if (bindingConfig.hasUnit()) {
										value = UnitUtils.convertUnit((Double) value, bindingConfig.getUnit(), bindingConfig.getWeatherProperty());
									}
								}

								publishValue(itemName, value, bindingConfig);
							}
						} catch (Exception ex) {
							logger.warn(ex.getMessage(), ex);
						}
					}
				}
			});
		}
	}

	/**
	 * Returns the minMax virtual property as a string.
	 */
	private String getMinMax(Double min, Double max, WeatherBindingConfig bindingConfig) {
		return toDisplayString(min, bindingConfig) + "/" + toDisplayString(max, bindingConfig);
	}

	/**
	 * Returns a rounded double value as string.
	 */
	private String toDisplayString(Double value, WeatherBindingConfig bindingConfig) {
		return value == null ? "-" : round(value.toString(), bindingConfig).toString();
	}

	/**
	 * Returns the weather or the correct forecast object instance.
	 */
	private Weather getInstance(Weather weather, WeatherBindingConfig bindingConfig) {
		Weather instance = weather;
		if (bindingConfig instanceof ForecastBindingConfig) {
			ForecastBindingConfig fcConfig = (ForecastBindingConfig) bindingConfig;
			if (fcConfig.getForecastDay() < weather.getForecast().size()) {
				instance = weather.getForecast().get(fcConfig.getForecastDay());
			} else {
				logger.warn("Weather forecast day {} not available for locationId '{}', only {} available",
						fcConfig.getForecastDay(), bindingConfig.getLocationId(),
						Math.max(weather.getForecast().size() - 1, 0));
				instance = null;
			}
		}
		return instance;
	}

	/**
	 * Publishes the item with the value.
	 */
	private void publishValue(String itemName, Object value, WeatherBindingConfig bindingConfig) {
		if (value == null) {
			context.getEventPublisher().postUpdate(itemName, UnDefType.UNDEF);
		} else if (value instanceof Calendar) {
			Calendar calendar = (Calendar) value;
			context.getEventPublisher().postUpdate(itemName, new DateTimeType(calendar));
		} else if (value instanceof Number) {
			context.getEventPublisher().postUpdate(itemName, new DecimalType(round(value.toString(), bindingConfig)));
		} else if (value instanceof String || value instanceof Enum) {
			if (value instanceof Enum) {
				String enumValue = WordUtils.capitalizeFully(StringUtils.replace(value.toString(), "_", " "));
				context.getEventPublisher().postUpdate(itemName, new StringType(enumValue));
			} else {
				context.getEventPublisher().postUpdate(itemName, new StringType(value.toString()));
			}
		} else {
			logger.warn("Unsupported value type {}", value.getClass().getSimpleName());
		}
	}

	/**
	 * Returns a rounded value from the string.
	 */
	private BigDecimal round(String value, WeatherBindingConfig bindingConfig) {
		if (bindingConfig.getRoundingMode() == null) {
			return new BigDecimal(value);
		}
		return new BigDecimal(value).setScale(bindingConfig.getScale(), bindingConfig.getRoundingMode());
	}
}
