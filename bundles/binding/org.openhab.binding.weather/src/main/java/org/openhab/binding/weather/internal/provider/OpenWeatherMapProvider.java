/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.provider;

import org.openhab.binding.weather.internal.model.ProviderName;
import org.openhab.binding.weather.internal.parser.JsonWeatherParser;

import org.openhab.binding.weather.internal.common.WeatherConfig;
import org.openhab.binding.weather.internal.common.WeatherContext;
import org.openhab.binding.weather.internal.common.ProviderConfig;

/**
 * OpenWeatherMap weather provider.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class OpenWeatherMapProvider extends AbstractWeatherProvider {
    private static final String URL = "http://api.openweathermap.org/data/2.5/weather?lat=[LATITUDE]&lon=[LONGITUDE]&lang=[LANGUAGE]&mode=json&units=metric&APPID=[API_KEY]";
    // private static final String FORECAST = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=[LATITUDE]&lon=[LONGITUDE]&lang=[LANGUAGE]&cnt=5&mode=json&units=metric&APPID=[API_KEY]";
    private static final String FORECAST = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=[LATITUDE]&lon=[LONGITUDE]&lang=[LANGUAGE]&cnt=5&mode=json&units=metric&APPID=[API_KEY_2]";
    
    private WeatherConfig config = WeatherContext.getInstance().getConfig();
   
    public OpenWeatherMapProvider() {
        super(new JsonWeatherParser());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderName getProviderName() {
        return ProviderName.OPENWEATHERMAP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getWeatherUrl() {
        return URL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getForecastUrl() {
        ProviderConfig providerConfig = config.getProviderConfig(getProviderName());
        if (providerConfig.getApiKey2() != null && !providerConfig.getApiKey2().equals("null") ) {
            return FORECAST;
        } else {
            return null;
        }
    }
}
