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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.openhab.binding.weather.internal.annotation.Provider;
import org.openhab.binding.weather.internal.annotation.ProviderMappings;
import org.openhab.binding.weather.internal.converter.ConverterType;

/**
 * Common provider model for wind data.
 *
 * @author Gerhard Riegler
 * @author Christoph Weitkamp - Added mapping for OpenWeatherMap for channel 'gust'
 * @since 1.6.0
 */
public class Wind {
    @ProviderMappings({
            @Provider(name = ProviderName.OPENWEATHERMAP, property = "speed", converter = ConverterType.WIND_MPS),
            @Provider(name = ProviderName.FORECASTIO, property = "windSpeed", converter = ConverterType.WIND_MPS),
            @Provider(name = ProviderName.WORLDWEATHERONLINE, property = "windspeedKmph"),
            @Provider(name = ProviderName.HAMWEATHER, property = "windSpeedKPH"),
            @Provider(name = ProviderName.METEOBLUE, property = "wind_speed"),
            @Provider(name = ProviderName.METEOBLUE, property = "wind_speed_max"),
            @Provider(name = ProviderName.APIXU, property = "wind_kph"),
            @Provider(name = ProviderName.APIXU, property = "maxwind_kph"),
            @Provider(name = ProviderName.WEATHERBIT, property = "wind_spd") })
    private Double speed;

    @ProviderMappings({ @Provider(name = ProviderName.WORLDWEATHERONLINE, property = "winddir16Point"),
            @Provider(name = ProviderName.METEOBLUE, property = "wind_direction_dominant"),
            @Provider(name = ProviderName.APIXU, property = "wind_dir"),
            @Provider(name = ProviderName.WEATHERBIT, property = "wind_cdir_full") })
    private String direction;

    @ProviderMappings({ @Provider(name = ProviderName.OPENWEATHERMAP, property = "deg"),
            @Provider(name = ProviderName.FORECASTIO, property = "windBearing"),
            @Provider(name = ProviderName.WORLDWEATHERONLINE, property = "winddirDegree"),
            @Provider(name = ProviderName.HAMWEATHER, property = "windDirDEG"),
            @Provider(name = ProviderName.APIXU, property = "wind_degree"),
            @Provider(name = ProviderName.WEATHERBIT, property = "wind_dir") })
    private Integer degree;

    @ProviderMappings({
            @Provider(name = ProviderName.FORECASTIO, property = "windGust", converter = ConverterType.WIND_MPS),
            @Provider(name = ProviderName.OPENWEATHERMAP, property = "gust", converter = ConverterType.WIND_MPS),
            @Provider(name = ProviderName.HAMWEATHER, property = "windGustKPH"),
            @Provider(name = ProviderName.METEOBLUE, property = "wind_gust_max"),
            @Provider(name = ProviderName.WEATHERBIT, property = "wind_gust_spd") })
    private Double gust;

    @ProviderMappings({ @Provider(name = ProviderName.HAMWEATHER, property = "windchillC") })
    private Double chill;

    /**
     * Returns the windspeed in kilometer per hour.
     */
    public Double getSpeed() {
        return speed;
    }

    /**
     * Sets the windspeed in kilometer per hour.
     */
    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    /**
     * Returns the wind direction.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the wind direction.
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Returns the wind degree.
     */
    public Integer getDegree() {
        return degree;
    }

    /**
     * Returns the wind degree.
     */
    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    /**
     * Returns the wind gust.
     */
    public Double getGust() {
        return gust;
    }

    /**
     * Sets the wind gust.
     */
    public void setGust(Double gust) {
        this.gust = gust;
    }

    /**
     * Returns the wind chill in degrees.
     */
    public Double getChill() {
        return chill;
    }

    /**
     * Sets the wind chill in degrees.
     */
    public void setChill(Double chill) {
        this.chill = chill;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("speed", speed)
                .append("direction", direction).append("degree", degree).append("gust", gust).append("chill", chill)
                .toString();
    }
}
