/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.provider;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.weather.internal.model.ProviderName;

/**
 * Simple factory which creates WeatherProvider objects based on the provider
 * name.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class WeatherProviderFactory {
	private static final Map<ProviderName, Class<? extends WeatherProvider>> weatherProviders = new HashMap<ProviderName, Class<? extends WeatherProvider>>();

	static {
		weatherProviders.put(ProviderName.FORECASTIO, ForecastIoProvider.class);
		weatherProviders.put(ProviderName.HAMWEATHER, HamweatherProvider.class);
		weatherProviders.put(ProviderName.OPENWEATHERMAP, OpenWeatherMapProvider.class);
		weatherProviders.put(ProviderName.WORLDWEATHERONLINE, WorldWeatherOnlineProvider.class);
		weatherProviders.put(ProviderName.WUNDERGROUND, WundergroundProvider.class);
		weatherProviders.put(ProviderName.YAHOO, YahooProvider.class);
	}

	/**
	 * Creates a WeatherProvider for the specified provider.
	 */
	public static WeatherProvider createWeatherProvider(ProviderName providerName) throws Exception {
		Class<? extends WeatherProvider> provider = weatherProviders.get(providerName);
		if (provider != null) {
			return provider.newInstance();
		}
		return null;
	}
}
