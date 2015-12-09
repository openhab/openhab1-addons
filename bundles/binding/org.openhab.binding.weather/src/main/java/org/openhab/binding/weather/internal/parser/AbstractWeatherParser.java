/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.parser;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.openhab.binding.weather.internal.converter.Converter;
import org.openhab.binding.weather.internal.converter.property.PressureTrendConverter;
import org.openhab.binding.weather.internal.metadata.MetadataHandler;
import org.openhab.binding.weather.internal.metadata.ProviderMappingInfo;
import org.openhab.binding.weather.internal.model.Atmosphere;
import org.openhab.binding.weather.internal.model.Forecast;
import org.openhab.binding.weather.internal.model.Precipitation;
import org.openhab.binding.weather.internal.model.ProviderName;
import org.openhab.binding.weather.internal.model.Temperature;
import org.openhab.binding.weather.internal.model.Weather;
import org.openhab.binding.weather.internal.model.Wind;
import org.openhab.binding.weather.internal.utils.PropertyResolver;
import org.openhab.binding.weather.internal.utils.PropertyUtils;
import org.openhab.binding.weather.internal.utils.UnitUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common base class for all weather parsers.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public abstract class AbstractWeatherParser implements WeatherParser {
	private static final Logger logger = LoggerFactory.getLogger(AbstractWeatherParser.class);
	private static final String[] EMPTY_VALUES = new String[] { "NA", "N/A", "--", "" };

	private MetadataHandler metadataHandler = MetadataHandler.getInstance();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void parseInto(InputStream is, Weather weather) throws Exception {
		postProcessEach(weather);
		for (Forecast forecast : weather.getForecast()) {
			postProcessEach(forecast);
		}
		postProcess(weather);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(Weather weather, String propertyName, String value) {
		ProviderMappingInfo providerMappingInfo = metadataHandler.getProviderMappingInfo(weather.getProvider(),
				propertyName);
		if (providerMappingInfo != null) {
			logger.trace("Setting property '{} ({})' with value '{}'", providerMappingInfo.getTarget(), propertyName,
					value);
			try {
				String targetProperty = providerMappingInfo.getTarget();
				Object target = PropertyUtils.getNestedObject(weather, targetProperty);
				String objectProperty = PropertyResolver.last(targetProperty);
				String preparedValue = stripEmptyValues(value);

				Converter<?> converter = providerMappingInfo.getConverter();
				Object valueToSet = preparedValue == null ? null : converter.convert(preparedValue);
				if (valueToSet != null) {
					FieldUtils.writeField(target, objectProperty, valueToSet, true);
				}

			} catch (Exception ex) {
				logger.warn("{}: Error setting property '{}' with value '{}' and converter {}", weather.getProvider(),
						propertyName, value, providerMappingInfo.getConverter().getType());
			}
		} else {
			logger.trace("Property not mapped: '{}' with value '{}'", propertyName, value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Weather startIfForecast(Weather weather, String propertyName) {
		if (metadataHandler.isForecast(weather.getProvider(), propertyName)) {
			return new Forecast(weather.getProvider());
		}
		return weather;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean endIfForecast(Weather weather, Weather forecast, String propertyName) {
		if (metadataHandler.isForecast(forecast.getProvider(), propertyName)) {
			Forecast fc = (Forecast) forecast;
			fc.setDay(weather.getForecast().size());
			weather.getForecast().add(fc);
			return true;
		}
		return false;
	}

	/**
	 * Removes empty value markers.
	 */
	protected String stripEmptyValues(String value) {
		for (String string : EMPTY_VALUES) {
			if (StringUtils.equalsIgnoreCase(value, string)) {
				return null;
			}
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postProcessEach(Weather weather) throws Exception {
		Temperature temp = weather.getTemperature();
		if (temp.getCurrent() != null && weather instanceof Forecast) {
			temp.setCurrent(null);
		}

		Atmosphere atm = weather.getAtmosphere();
		if (temp.getFeel() == null && temp.getCurrent() != null && atm.getHumidity() != null) {
			Double humidex = UnitUtils.getHumidex(temp.getCurrent(), atm.getHumidity());
			temp.setFeel(humidex);
		}

		Wind wind = weather.getWind();
		if (wind.getDirection() == null && wind.getDegree() != null) {
			String direction = UnitUtils.getWindDirection(wind.getDegree());
			wind.setDirection(direction);
		}

		Precipitation precip = weather.getPrecipitation();
		if (weather.getProvider() == ProviderName.FORECASTIO && StringUtils.equalsIgnoreCase("snow", precip.getType())) {
			precip.setSnow(precip.getRain());
			precip.setRain(null);
		}

		if (precip.getSnow() == null) {
			precip.setSnow(0.0);
		}
		if (precip.getRain() == null) {
			precip.setRain(0.0);
		}

		CommonIdHandler.getInstance().setCommonId(weather);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postProcess(Weather weather) throws Exception {
		if (weather.getAtmosphere().getPressureTrend() == null) {
			Double currentPressure = weather.getAtmosphere().getPressure();
			if (currentPressure != null && weather.getForecast().size() > 0) {
				Double fcPressure = weather.getForecast().get(0).getAtmosphere().getPressure();
				if (fcPressure != null) {
					if (fcPressure > currentPressure) {
						weather.getAtmosphere().setPressureTrend(PressureTrendConverter.TREND_UP);
					} else if (fcPressure < currentPressure) {
						weather.getAtmosphere().setPressureTrend(PressureTrendConverter.TREND_DOWN);
					} else {
						weather.getAtmosphere().setPressureTrend(PressureTrendConverter.TREND_EQUAL);
					}
				}
			}
		}
	}
}
