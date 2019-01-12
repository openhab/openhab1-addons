/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.model;


/**
 * Definition of all supported weather providers.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public enum ProviderName {WUNDERGROUND,
    OPENWEATHERMAP,
    FORECASTIO,
    WORLDWEATHERONLINE,
    HAMWEATHER,
    METEOBLUE,
    APIXU,
    WEATHERBIT;

    /**
     * Parses the string and returns the ProviderName enum.
     */
    public static ProviderName parse(String name) {
        if (name == null) {
            return null;
        } else if (WUNDERGROUND.toString().equalsIgnoreCase(name)) {
            return WUNDERGROUND;
        } else if (OPENWEATHERMAP.toString().equalsIgnoreCase(name)) {
            return OPENWEATHERMAP;
        } else if (FORECASTIO.toString().equalsIgnoreCase(name)) {
            return FORECASTIO;
        } else if (WORLDWEATHERONLINE.toString().equalsIgnoreCase(name)) {
            return WORLDWEATHERONLINE;
        } else if (HAMWEATHER.toString().equalsIgnoreCase(name)) {
            return HAMWEATHER;
        } else if (METEOBLUE.toString().equalsIgnoreCase(name)) {
            return METEOBLUE;
        } else if (APIXU.toString().equalsIgnoreCase(name)) {
            return APIXU;
        } else if (WEATHERBIT.toString().equalsIgnoreCase(name)) {
            return WEATHERBIT;
        } else {
            return null;
        }
    }
}
