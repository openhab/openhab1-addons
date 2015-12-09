/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.gfx;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openhab.binding.weather.internal.common.LocationConfig;
import org.openhab.binding.weather.internal.common.Unit;
import org.openhab.binding.weather.internal.common.WeatherContext;
import org.openhab.binding.weather.internal.model.Weather;
import org.openhab.binding.weather.internal.utils.PropertyUtils;
import org.openhab.binding.weather.internal.utils.UnitUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Replaces tokens in weather layout files.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class WeatherTokenResolver implements TokenResolver {
	private static final Logger logger = LoggerFactory
			.getLogger(WeatherTokenResolver.class);

	private static final String PREFIX_TOKEN_WEATHER = "weather";
	private static final String PREFIX_TOKEN_FORECAST = "forecast";
	private static final String PREFIX_TOKEN_CONFIG = "config";
	private static final String PREFIX_TOKEN_PARAM = "param";

	private Weather weather;
	private String locationId;
	private Map<String, String> params = new HashMap<String, String>();

	public WeatherTokenResolver(Weather weather, String locationId) {
		this.weather = weather;
		this.locationId = locationId;
	}

	/**
	 * Adds a HTTP request parameter.
	 */
	public void addParameter(String name, String value) {
		params.put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String resolveToken(String tokenName) {
		try {
			Token token = parseTokenName(tokenName);

			if (PREFIX_TOKEN_WEATHER.equals(token.prefix)) {
				return replaceWeather(token, weather);
			} else if (PREFIX_TOKEN_FORECAST.equals(token.prefix)) {
				return replaceForecast(token);
			} else if (PREFIX_TOKEN_CONFIG.equals(token.prefix)) {
				return replaceConfig(token);
			} else if (PREFIX_TOKEN_PARAM.equals(token.prefix)) {
				return replaceParameter(token);
			} else {
				throw new RuntimeException("Invalid weather token: "
						+ tokenName);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * Replaces the token with a property of the weather object.
	 */
	private String replaceWeather(Token token, Weather instance)
			throws Exception {
		if (!PropertyUtils.hasProperty(instance, token.name)) {
			throw new RuntimeException("Invalid weather token: " + token.full);
		}
		Object propertyValue = PropertyUtils.getPropertyValue(instance,
				token.name);
		if (token.unit != null && propertyValue instanceof Double) {
			propertyValue = UnitUtils.convertUnit((Double) propertyValue, token.unit, token.name);
		}
		if (token.formatter != null) {
			return String.format(token.formatter, propertyValue);
		}
		return ObjectUtils.toString(propertyValue);
	}

	/**
	 * Replaces the token with a property of a forecast object.
	 */
	private String replaceForecast(Token token) throws Exception {
		int day = NumberUtils.toInt(token.qualifier);
		if (day < weather.getForecast().size()) {
			Weather forecast = weather.getForecast().get(day);
			return replaceWeather(token, forecast);
		} else {
			throw new RuntimeException("Weather forecast day " + day
					+ " not available, only "
					+ Math.max(weather.getForecast().size() - 1, 0)
					+ " available");
		}
	}

	/**
	 * Replaces the token with properties of the weather LocationConfig object.
	 */
	private String replaceConfig(Token token) {
		LocationConfig locationConfig = WeatherContext.getInstance()
				.getConfig().getLocationConfig(locationId);
		if (locationConfig == null) {
			throw new RuntimeException("Weather locationId '" + locationId
					+ "' does not exist");
		}

		if ("latitude".equals(token.name)) {
			return locationConfig.getLatitude().toString();
		} else if ("longitude".equals(token.name)) {
			return locationConfig.getLongitude().toString();
		} else if ("name".equals(token.name)) {
			return locationConfig.getName();
		} else if ("language".equals(token.name)) {
			return locationConfig.getLanguage();
		} else if ("updateInterval".equals(token.name)) {
			return ObjectUtils.toString(locationConfig.getUpdateInterval());
		} else if ("locationId".equals(token.name)) {
			return locationConfig.getLocationId();
		} else if ("providerName".equals(token.name)) {
			return ObjectUtils.toString(locationConfig.getProviderName());
		} else {
			throw new RuntimeException("Invalid weather token: " + token.full);
		}
	}

	/**
	 * Replaces the token with a HTTP request parameter.
	 */
	private String replaceParameter(Token token) {
		return params.get(token.name);
	}

	/**
	 * Parses the token which should be replaced.
	 */
	private Token parseTokenName(String tokenName) {
		Token token = new Token();
		token.full = tokenName;
		token.prefix = StringUtils.substringBefore(token.full, ":");
		if (StringUtils.contains(token.prefix, "(")) {
			token.qualifier = StringUtils.substringBetween(token.prefix, "(",
					")");
			token.prefix = StringUtils.substringBefore(token.prefix, "(");
		}
		token.name = StringUtils.substringAfter(token.full, ":");
		if (StringUtils.contains(token.name, "(")) {
			token.formatter = StringUtils
					.substringBetween(token.name, "(", ")");
			token.name = StringUtils.substringBefore(token.name, "(");
		}
		if (StringUtils.contains(token.full, "[")) {
			token.unit = Unit.parse(StringUtils.substringBetween(token.full, "[","]"));
			token.name = StringUtils.substringBefore(token.name, "[");
		}
		
		if (!token.isValid()) {
			throw new RuntimeException("Invalid weather token: " + token.full);
		}

		return token;
	}

	/**
	 * Helper class with the parts of a token.
	 * 
	 * @author Gerhard Riegler
	 * @since 1.6.0
	 */
	private class Token {
		public String full;
		public String prefix;
		public String qualifier;
		public String name;
		public String formatter;
		public Unit unit;

		/**
		 * Returns true, if a token contains a prefix and a name.
		 */
		public boolean isValid() {
			return StringUtils.isNotBlank(prefix)
					&& StringUtils.isNotBlank(name);
		}

	}

}
