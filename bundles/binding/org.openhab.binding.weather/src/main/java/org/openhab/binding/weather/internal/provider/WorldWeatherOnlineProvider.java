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
 * WorldWeatherOnline weather provider.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class WorldWeatherOnlineProvider extends AbstractWeatherProvider {
    private static final String URL = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=[API_KEY]&q=[LATITUDE],[LONGITUDE]&extra=localObsTime&num_of_days=5&format=json&lang=[LANGUAGE]";

    public WorldWeatherOnlineProvider() {
        super(new JsonWeatherParser());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderName getProviderName() {
        return ProviderName.WORLDWEATHERONLINE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getWeatherUrl() {
        return URL;
    }

}
