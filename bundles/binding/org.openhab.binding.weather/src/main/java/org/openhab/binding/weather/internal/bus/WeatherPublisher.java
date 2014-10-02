/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.bus;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
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
import org.openhab.core.items.Item;
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
	private Map<String, Object> itemCache = new HashMap<String, Object>();

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
	 * Clears the item cache.
	 */
	public void clear() {
		itemCache.clear();
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
			itemCache.remove(itemName);
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
				public void next(WeatherBindingConfig bindingConfig, Item item) {
					if (bindingConfig.getLocationId().equals(locationId)) {
						try {
							Weather instance = getInstance(weather, bindingConfig);
							if (instance != null) {
								String weatherProperty = bindingConfig.getWeatherProperty();
								Object value = null;

								if (Weather.isVirtualProperty(weatherProperty)) {
									Temperature temp = instance.getTemperature();
									if (Weather.VIRTUAL_TEMP_MINMAX.equals(weatherProperty)) {
										value = getMinMax(temp.getMin(), temp.getMax(), bindingConfig);
									} else if (Weather.VIRTUAL_TEMP_MINMAX_F.equals(weatherProperty)) {
										value = getMinMax(temp.getMinF(), temp.getMaxF(), bindingConfig);
									}
								} else {
									value = PropertyUtils.getPropertyValue(instance, weatherProperty);
								}
								if (!equalsCachedValue(value, item)) {
									publishValue(item, value, bindingConfig);
									itemCache.put(item.getName(), value);
								}
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
	 * Returns true, if the cached value is equal to the new value.
	 */
	private boolean equalsCachedValue(Object value, Item item) {
		int cachedValueHashCode = ObjectUtils.hashCode(itemCache.get(item.getName()));
		int valueHashCode = ObjectUtils.hashCode(value);
		return cachedValueHashCode == valueHashCode;
	}

	/**
	 * Publishes the item with the value.
	 */
	private void publishValue(Item item, Object value, WeatherBindingConfig bindingConfig) {
		if (value == null) {
			context.getEventPublisher().postUpdate(item.getName(), UnDefType.UNDEF);
		} else if (value instanceof Calendar) {
			Calendar calendar = (Calendar) value;
			if (item.getAcceptedDataTypes().contains(DateTimeType.class)) {
				context.getEventPublisher().postUpdate(item.getName(), new DateTimeType(calendar));
			} else {
				logger.warn("Unsupported type for item {}, only DateTimeType supported!", item.getName());
			}
		} else if (value instanceof Number) {
			if (item.getAcceptedDataTypes().contains(DecimalType.class)) {
				context.getEventPublisher().postUpdate(item.getName(),
						new DecimalType(round(value.toString(), bindingConfig)));
			} else {
				logger.warn("Unsupported type for item {}, only DecimalType supported!", item.getName());
			}
		} else if (value instanceof String || value instanceof Enum) {
			if (item.getAcceptedDataTypes().contains(StringType.class)) {
				if (value instanceof Enum) {
					String enumValue = WordUtils.capitalizeFully(StringUtils.replace(value.toString(), "_", " "));
					context.getEventPublisher().postUpdate(item.getName(), new StringType(enumValue));
				} else {
					context.getEventPublisher().postUpdate(item.getName(), new StringType(value.toString()));
				}
			} else {
				logger.warn("Unsupported type for item {}, only String supported!", item.getName());
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
