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

import org.openhab.binding.weather.internal.common.LocationConfig;
import org.openhab.binding.weather.internal.model.ProviderName;
import org.openhab.binding.weather.internal.model.Weather;

/**
 * Weather provider definition.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public interface WeatherProvider {

    /**
     * Returns the weather data for the specified location config.
     */
    public Weather getWeather(LocationConfig locationConfig) throws Exception;

    /**
     * Returns the provider name.
     */
    public ProviderName getProviderName();

}
