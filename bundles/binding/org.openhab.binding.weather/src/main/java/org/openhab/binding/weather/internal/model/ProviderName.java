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
package org.openhab.binding.weather.internal.model;

/**
 * Definition of all supported weather providers.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public enum ProviderName {
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
