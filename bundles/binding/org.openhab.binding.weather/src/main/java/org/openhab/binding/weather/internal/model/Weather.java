/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.weather.internal.annotation.Forecast;
import org.openhab.binding.weather.internal.annotation.ForecastMappings;
import org.openhab.binding.weather.internal.annotation.Provider;
import org.openhab.binding.weather.internal.annotation.ProviderMappings;

/**
 * Common provider model for weather data.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class Weather {
	public static final String VIRTUAL_TEMP_MINMAX = "temperature.minMax";
	public static final String VIRTUAL_TEMP_MINMAX_F = "temperature.minMaxF";
	private static final String[] VIRTUAL_PROPERTIES = new String[] {VIRTUAL_TEMP_MINMAX, VIRTUAL_TEMP_MINMAX_F};

	private Athmosphere athmosphere = new Athmosphere();
	private Clouds clouds = new Clouds();
	private Condition condition = new Condition();
	private Precipitation precipitation = new Precipitation();
	private Temperature temperature = new Temperature();
	private Wind wind = new Wind();

	private ProviderName provider;

	@ProviderMappings({ 
			@Provider(name = ProviderName.HAMWEATHER, property = "error.description"),
			@Provider(name = ProviderName.FORECASTIO, property = "error"),
			@Provider(name = ProviderName.OPENWEATHERMAP, property = "message"),
			@Provider(name = ProviderName.WORLDWEATHERONLINE, property = "data.error.msg"),
			@Provider(name = ProviderName.WUNDERGROUND, property = "response.error.type"),
			@Provider(name = ProviderName.YAHOO, property = "error.description") 
	})
	private String error;

	@ProviderMappings({ 
			@Provider(name = ProviderName.OPENWEATHERMAP, property = "cod")
	})
	private Integer responseCode;

	@ForecastMappings({ 
			@Forecast(provider = ProviderName.OPENWEATHERMAP, property = "list"),
			@Forecast(provider = ProviderName.WUNDERGROUND, property = "forecast.simpleforecast.forecastday"),
			@Forecast(provider = ProviderName.FORECASTIO, property = "daily.data"),
			@Forecast(provider = ProviderName.WORLDWEATHERONLINE, property = "data.weather"),
			@Forecast(provider = ProviderName.YAHOO, property = "query.results.channel.item.forecast"),
			@Forecast(provider = ProviderName.HAMWEATHER, property = "response.responses.response.periods") 
	})
	private List<org.openhab.binding.weather.internal.model.Forecast> forecast = new ArrayList<org.openhab.binding.weather.internal.model.Forecast>();

	/**
	 * Creates a new Weather object for the specified provider.
	 */
	public Weather(ProviderName provider) {
		this.provider = provider;
	}

	/**
	 * Returns athmosphere data.
	 */
	public Athmosphere getAthmosphere() {
		return athmosphere;
	}

	/**
	 * Returns clouds data.
	 */
	public Clouds getClouds() {
		return clouds;
	}

	/**
	 * Returns condition data.
	 */
	public Condition getCondition() {
		return condition;
	}

	/**
	 * Returns precipitation data.
	 */
	public Precipitation getPrecipitation() {
		return precipitation;
	}

	/**
	 * Returns temperature data.
	 */
	public Temperature getTemperature() {
		return temperature;
	}

	/**
	 * Returns wind data.
	 */
	public Wind getWind() {
		return wind;
	}

	/**
	 * Returns forecast data.
	 */
	public List<org.openhab.binding.weather.internal.model.Forecast> getForecast() {
		return forecast;
	}

	/**
	 * Returns the provider name.
	 */
	public ProviderName getProvider() {
		return provider;
	}

	/**
	 * Returns a possible error retrieving weather data.
	 */
	public String getError() {
		return error;
	}

	/**
	 * Sets a possible error retrieving weather data.
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Returns true, if a error occurred retrieving weather data.
	 */
	public boolean hasError() {
		return error != null;
	}

	/**
	 * Returns the response code, only used for openweathermap provider.
	 */
	public Integer getResponseCode() {
		return responseCode;
	}

	/**
	 * Returns true, if the specified property is a virtual property.
	 */
	public static boolean isVirtualProperty(String property) {
		return ArrayUtils.contains(VIRTUAL_PROPERTIES, property);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("temperature", temperature)
				.append("athmosphere", athmosphere).append("clouds", clouds).append("condition", condition)
				.append("precipitation", precipitation).append("wind", wind).append("error", error).toString();
	}

}
