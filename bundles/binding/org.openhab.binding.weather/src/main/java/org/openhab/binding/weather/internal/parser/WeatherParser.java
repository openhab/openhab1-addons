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
package org.openhab.binding.weather.internal.parser;

import org.openhab.binding.weather.internal.model.Weather;

/**
 * Weather parser definition.
 *
 * @author Gerhard Riegler
 * @author Christoph Weitkamp - Replaced org.apache.commons.httpclient with HttpUtil
 * @since 1.6.0
 */
public interface WeatherParser {

    /**
     * Parses the response String into the weather object.
     */
    public void parseInto(String r, Weather weather) throws Exception;

    /**
     * Sets the property of the weather object with the value.
     */
    public void setValue(Weather weather, String propertyName, String value);

    /**
     * Returns a new forecast object if the property marks the start of forecast
     * data in the stream.
     */
    public Weather startIfForecast(Weather weather, String propertyName);

    /**
     * Adds the forecast to the weather object if the property marks the end of
     * forecast data in the stream.
     */
    public boolean endIfForecast(Weather weather, Weather forecast, String propertyName);

    /**
     * Postprocess the weather and each forecast object.
     */
    public void postProcessEach(Weather weather) throws Exception;

    /**
     * Postprocess only the weather object.
     */
    public void postProcess(Weather weather) throws Exception;
}
