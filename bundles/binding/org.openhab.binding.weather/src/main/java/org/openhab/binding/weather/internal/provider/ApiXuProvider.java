/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.provider;

import org.openhab.binding.weather.internal.model.ProviderName;
import org.openhab.binding.weather.internal.parser.JsonWeatherParser;

/**
 * APIUX weather provider.
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
