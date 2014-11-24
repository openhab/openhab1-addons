/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.weather.WeatherBindingProvider;
import org.openhab.binding.weather.internal.model.Weather;
import org.openhab.binding.weather.internal.scheduler.WeatherJobScheduler;
import org.openhab.core.events.EventPublisher;

/**
 * Singleton with the important objects for this binding.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class WeatherContext {
	private EventPublisher eventPublisher;
	private Collection<WeatherBindingProvider> providers;

	private WeatherConfig config = new WeatherConfig();
	private WeatherJobScheduler jobScheduler = new WeatherJobScheduler(this);
	private Map<String, Weather> weatherByLocationId = new HashMap<String, Weather>();

	private static WeatherContext instance;

	private WeatherContext() {
	}

	/**
	 * Create or returns the instance of this class.
	 */
	public static synchronized WeatherContext getInstance() {
		if (instance == null) {
			instance = new WeatherContext();
		}
		return instance;
	}

	/**
	 * Returns the WeatherConfig.
	 */
	public WeatherConfig getConfig() {
		return config;
	}

	/**
	 * Sets the EventPublisher for use in the binding.
	 */
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	/**
	 * Returns the EventPublisher.
	 */
	public EventPublisher getEventPublisher() {
		return eventPublisher;
	}

	/**
	 * Returns all WeatherBindingProviders.
	 */
	public Collection<WeatherBindingProvider> getProviders() {
		return providers;
	}

	/**
	 * Sets all WeatherBindingProviders for use in the binding.
	 */
	public void setProviders(Collection<WeatherBindingProvider> providers) {
		this.providers = providers;
	}

	/**
	 * Returns the JobScheduler.
	 */
	public WeatherJobScheduler getJobScheduler() {
		return jobScheduler;
	}

	/**
	 * Returns the weather data for the specified locationId.
	 */
	public Weather getWeather(String locationId) {
		return weatherByLocationId.get(locationId);
	}

	/**
	 * Returns the weather data for the specified locationId.
	 */
	public void setWeather(String locationId, Weather weather) {
		weatherByLocationId.put(locationId, weather);
	}

}
