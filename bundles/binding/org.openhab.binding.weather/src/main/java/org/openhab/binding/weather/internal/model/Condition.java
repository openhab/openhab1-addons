/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.model;

import java.util.Calendar;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.weather.internal.annotation.Provider;
import org.openhab.binding.weather.internal.annotation.ProviderMappings;
import org.openhab.binding.weather.internal.converter.ConverterType;

/**
 * Common provider model for current condition data.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class Condition {

	@ProviderMappings({ 
			@Provider(name = ProviderName.WUNDERGROUND, property = "current_observation.weather"),
			@Provider(name = ProviderName.WUNDERGROUND, property = "conditions"),
			@Provider(name = ProviderName.OPENWEATHERMAP, property = "weather.description"),
			@Provider(name = ProviderName.FORECASTIO, property = "summary"),
			@Provider(name = ProviderName.WORLDWEATHERONLINE, property = "weatherDesc.value"),
			@Provider(name = ProviderName.YAHOO, property = "text"),
			@Provider(name = ProviderName.HAMWEATHER, property = "weather") 
	})
	private String text;

	@ProviderMappings({
			@Provider(name = ProviderName.WUNDERGROUND, property = "current_observation.observation_epoch", converter = ConverterType.UNIX_DATE),
			@Provider(name = ProviderName.WUNDERGROUND, property = "date.epoch", converter = ConverterType.UNIX_DATE),
			@Provider(name = ProviderName.OPENWEATHERMAP, property = "dt", converter = ConverterType.UNIX_DATE),
			@Provider(name = ProviderName.FORECASTIO, property = "time", converter = ConverterType.UNIX_DATE),
			@Provider(name = ProviderName.WORLDWEATHERONLINE, property = "localObsDateTime", converter = ConverterType.UTC_DATE),
			@Provider(name = ProviderName.WORLDWEATHERONLINE, property = "date", converter = ConverterType.DATE),
			@Provider(name = ProviderName.YAHOO, property = "lastBuildDate", converter = ConverterType.FULL_UTC_DATE),
			@Provider(name = ProviderName.YAHOO, property = "forecast.date", converter = ConverterType.SIMPLE_DATE),
			@Provider(name = ProviderName.HAMWEATHER, property = "ob.timestamp", converter = ConverterType.UNIX_DATE),
			@Provider(name = ProviderName.HAMWEATHER, property = "periods.timestamp", converter = ConverterType.UNIX_DATE)
	})
	private Calendar observationTime;

	@ProviderMappings({
			@Provider(name = ProviderName.OPENWEATHERMAP, property = "weather.id"),
			@Provider(name = ProviderName.YAHOO, property = "code"),
			@Provider(name = ProviderName.WORLDWEATHERONLINE, property = "weatherCode"),
			@Provider(name = ProviderName.HAMWEATHER, property = "weatherPrimaryCoded", converter = ConverterType.MULTI_ID) 
	})
	private String id;

	@ProviderMappings({ 
			@Provider(name = ProviderName.FORECASTIO, property = "icon"),
			@Provider(name = ProviderName.OPENWEATHERMAP, property = "icon"),
			@Provider(name = ProviderName.WUNDERGROUND, property = "current_observation.icon"),
			@Provider(name = ProviderName.WUNDERGROUND, property = "simpleforecast.forecastday.icon"),
			@Provider(name = ProviderName.HAMWEATHER, property = "icon") 
	})
	private String icon;

	private Calendar lastUpdate;

	/**
	 * Returns the current condition as text.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the current condition as text.
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns the provider observation time.
	 */
	public Calendar getObservationTime() {
		return observationTime;
	}

	/**
	 * Sets the provider observation time.
	 */
	public void setObservationTime(Calendar observationTime) {
		this.observationTime = observationTime;
	}

	/**
	 * Returns the provider specific condition id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the provider specific condition id.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the provider specific icon.
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Sets the provider specific icon.
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * Returns the last update of the weather conditions.
	 */
	public Calendar getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * Sets the last update of the weather conditions.
	 */
	public void setLastUpdate(Calendar lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("text", text)
				.append("lastUpdate", lastUpdate == null ? null : lastUpdate.getTime())
				.append("observationTime", observationTime == null ? null : observationTime.getTime()).append("id", id)
				.append("icon", icon).toString();
	}

}
