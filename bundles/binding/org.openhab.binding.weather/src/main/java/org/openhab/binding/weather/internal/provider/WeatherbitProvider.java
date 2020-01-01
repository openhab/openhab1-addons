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

import org.openhab.binding.weather.internal.model.ProviderName;
import org.openhab.binding.weather.internal.parser.JsonWeatherParser;

/**
 * Weatherbit weather provider.
 *
 * @author Joerg Mahmens
 * @since 1.14.0
 */

public class WeatherbitProvider extends AbstractWeatherProvider {

    private static final String URL = "https://api.weatherbit.io/v2.0/current?key=[API_KEY]&lang=[LANGUAGE]&lat=[LATITUDE]&lon=[LONGITUDE]";
    private static final String FORECAST = "https://api.weatherbit.io/v2.0/forecast/daily?key=[API_KEY]&lang=[LANGUAGE]&lat=[LATITUDE]&lon=[LONGITUDE]&days=10";

    public WeatherbitProvider() {
        super(new JsonWeatherParser());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderName getProviderName() {
        return ProviderName.WEATHERBIT;
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
        return FORECAST;
    }
}
