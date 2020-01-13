/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
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
        weatherProviders.put(ProviderName.METEOBLUE, MeteoBlueProvider.class);
        weatherProviders.put(ProviderName.APIXU, ApiXuProvider.class);
        weatherProviders.put(ProviderName.WEATHERBIT, WeatherbitProvider.class);
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
