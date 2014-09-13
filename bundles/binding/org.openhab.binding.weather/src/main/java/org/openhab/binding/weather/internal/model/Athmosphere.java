/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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
import org.openhab.binding.weather.internal.utils.UnitUtils;

/**
 * Common provider model for athmosphere data.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class Athmosphere {

	@ProviderMappings({
			@Provider(name = ProviderName.WUNDERGROUND, property = "current_observation.relative_humidity", converter = ConverterType.PERCENT_INTEGER),
			@Provider(name = ProviderName.WUNDERGROUND, property = "avehumidity"),
			@Provider(name = ProviderName.OPENWEATHERMAP, property = "humidity"),
			@Provider(name = ProviderName.FORECASTIO, property = "humidity", converter = ConverterType.FRACTION_INTEGER),
			@Provider(name = ProviderName.WORLDWEATHERONLINE, property = "humidity"),
			@Provider(name = ProviderName.YAHOO, property = "atmosphere.humidity"),
			@Provider(name = ProviderName.HAMWEATHER, property = "humidity") 
	})
	private Integer humidity;

	@ProviderMappings({ 
			@Provider(name = ProviderName.WUNDERGROUND, property = "current_observation.visibility_km"),
			@Provider(name = ProviderName.FORECASTIO, property = "visibility"),
			@Provider(name = ProviderName.WORLDWEATHERONLINE, property = "visibility"),
			@Provider(name = ProviderName.YAHOO, property = "atmosphere.visibility"),
			@Provider(name = ProviderName.HAMWEATHER, property = "visibilityKM") 
	})
	private Double visibility;

	@ProviderMappings({ 
			@Provider(name = ProviderName.WUNDERGROUND, property = "current_observation.pressure_mb"),
			@Provider(name = ProviderName.OPENWEATHERMAP, property = "pressure"),
			@Provider(name = ProviderName.FORECASTIO, property = "pressure"),
			@Provider(name = ProviderName.WORLDWEATHERONLINE, property = "pressure"),
			@Provider(name = ProviderName.YAHOO, property = "atmosphere.pressure"),
			@Provider(name = ProviderName.HAMWEATHER, property = "pressureMB") 
	})
	private Double pressure;

	private String pressureTrend;

	@ProviderMappings({ 
			@Provider(name = ProviderName.FORECASTIO, property = "ozone")
	})
	private Integer ozone;

	/**
	 * Returns the humidity in percent.
	 */
	public Integer getHumidity() {
		return humidity;
	}

	/**
	 * Sets the humidity.
	 */
	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}

	/**
	 * Returns the visibility in kilometers.
	 */
	public Double getVisibility() {
		return visibility;
	}

	/**
	 * Returns the visibility in miles per hour.
	 */
	public Double getVisibilityMph() {
		return UnitUtils.kmhToMph(visibility);
	}

	/**
	 * Sets the visibility in kilometers.
	 */
	public void setVisibility(Double visibility) {
		this.visibility = visibility;
	}

	/**
	 * Returns the pressure in millibar.
	 */
	public Double getPressure() {
		return pressure;
	}

	/**
	 * Returns the pressure in inches.
	 */
	public Double getPressureInches() {
		return UnitUtils.millibarToInches(pressure);
	}

	/**
	 * Sets the pressure in millibar.
	 */
	public void setPressure(Double pressure) {
		this.pressure = pressure;
	}

	/**
	 * Returns the pressure trend (up, down, equal).
	 */
	public String getPressureTrend() {
		return pressureTrend;
	}

	/**
	 * Sets the pressure trend.
	 */
	public void setPressureTrend(String pressureTrend) {
		this.pressureTrend = pressureTrend;
	}

	/**
	 * Returns the ozone in ppm.
	 */
	public Integer getOzone() {
		return ozone;
	}

	/**
	 * Sets the ozone.
	 */
	public void setOzone(Integer ozone) {
		this.ozone = ozone;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("humidity", humidity)
				.append("visibility", visibility).append("pressure", pressure).append("pressureTrend", pressureTrend)
				.append("ozone", ozone).toString();
	}

}
