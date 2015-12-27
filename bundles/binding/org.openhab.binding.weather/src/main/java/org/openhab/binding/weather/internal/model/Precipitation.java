/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.weather.internal.annotation.Provider;
import org.openhab.binding.weather.internal.annotation.ProviderMappings;
import org.openhab.binding.weather.internal.converter.ConverterType;

/**
 * Common provider model for precipitation data.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class Precipitation {

	@ProviderMappings({ 
			@Provider(name = ProviderName.FORECASTIO, property = "precipIntensity"),
			@Provider(name = ProviderName.WUNDERGROUND, property = "current_observation.precip_1hr_metric"),
			@Provider(name = ProviderName.WUNDERGROUND, property = "qpf_allday.mm"),
			@Provider(name = ProviderName.OPENWEATHERMAP, property = "rain.3h", converter = ConverterType.DOUBLE_3H),
			@Provider(name = ProviderName.OPENWEATHERMAP, property = "rain", converter = ConverterType.DOUBLE_3H),
			@Provider(name = ProviderName.OPENWEATHERMAP, property = "rain.1h"),
			@Provider(name = ProviderName.WORLDWEATHERONLINE, property = "precipMM"),
			@Provider(name = ProviderName.HAMWEATHER, property = "precipMM") 
	})
	private Double rain;

	@ProviderMappings({ 
			@Provider(name = ProviderName.WUNDERGROUND, property = "snow_allday.cm"),
			@Provider(name = ProviderName.OPENWEATHERMAP, property = "snow.3h", converter = ConverterType.DOUBLE_3H),
			@Provider(name = ProviderName.HAMWEATHER, property = "snowDepthCM")
	})
	private Double snow;

	@ProviderMappings({ 
			@Provider(name = ProviderName.FORECASTIO, property = "precipType") 
	})
	private String type;

	@ProviderMappings({ 
			@Provider(name = ProviderName.FORECASTIO, property = "precipProbability", converter = ConverterType.FRACTION_INTEGER),
			@Provider(name = ProviderName.WUNDERGROUND, property = "pop")
	})
	private Integer probability;

	@ProviderMappings({ 
		@Provider(name = ProviderName.WUNDERGROUND, property = "precip_today_metric")
	})
	private Double total;

	/**
	 * Returns the rain in millimeters.
	 */
	public Double getRain() {
		return rain;
	}

	/**
	 * Sets the rain in millimeters.
	 */
	public void setRain(Double rain) {
		this.rain = rain;
	}

	/**
	 * Returns the snow in centimeter.
	 */
	public Double getSnow() {
		return snow;
	}

	/**
	 * Sets the snow in centimeter.
	 */
	public void setSnow(Double snow) {
		this.snow = snow;
	}

	/**
	 * Returns the type of precipitation, internal use only.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns the probability in percent.
	 */
	public Integer getProbability() {
		return probability;
	}

	/**
	 * Sets the probability in percent.
	 */
	public void setProbability(Integer probability) {
		this.probability = probability;
	}

	/**
	 * Returns the total amount of todays precipitation.
	 */
	public Double getTotal() {
		return total;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("rain", rain).append("snow", snow)
				.append("probability", probability).append("total", total).toString();
	}
}
