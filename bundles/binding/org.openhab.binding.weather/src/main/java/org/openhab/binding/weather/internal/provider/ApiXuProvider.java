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
 * APIXU weather provider.
 *
 * @author Joerg Mahmens
 * @since 1.14.0
 */
public class ApiXuProvider extends AbstractWeatherProvider {

    private static final String URL = "https://api.apixu.com/v1/current.json?key=[API_KEY]&lang=[LANGUAGE]&q=[LATITUDE],[LONGITUDE]";
    private static final String FORECAST = "https://api.apixu.com/v1/forecast.json?key=[API_KEY]&lang=[LANGUAGE]&q=[LATITUDE],[LONGITUDE]&days=10";

    public ApiXuProvider() {
        super(new JsonWeatherParser());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderName getProviderName() {
        return ProviderName.APIXU;
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
