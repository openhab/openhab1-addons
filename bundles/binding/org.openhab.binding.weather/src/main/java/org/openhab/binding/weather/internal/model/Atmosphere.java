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
 * Common provider model for atmosphere data.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class Atmosphere {

    @ProviderMappings({ @Provider(name = ProviderName.OPENWEATHERMAP, property = "humidity"),
            @Provider(name = ProviderName.FORECASTIO, property = "humidity", converter = ConverterType.FRACTION_INTEGER),
            @Provider(name = ProviderName.WORLDWEATHERONLINE, property = "humidity"),
            @Provider(name = ProviderName.HAMWEATHER, property = "humidity"),
            @Provider(name = ProviderName.METEOBLUE, property = "relative_humidity_avg"),
            @Provider(name = ProviderName.APIXU, property = "humidity"),
            @Provider(name = ProviderName.APIXU, property = "day.avghumidity"),
            @Provider(name = ProviderName.WEATHERBIT, property = "rh") })
    private Integer humidity;

    @ProviderMappings({ @Provider(name = ProviderName.FORECASTIO, property = "visibility"),
            @Provider(name = ProviderName.WORLDWEATHERONLINE, property = "visibility"),
            @Provider(name = ProviderName.HAMWEATHER, property = "visibilityKM"),
            @Provider(name = ProviderName.APIXU, property = "vis_km"),
            @Provider(name = ProviderName.APIXU, property = "day.avgvis_km"),
            @Provider(name = ProviderName.WEATHERBIT, property = "vis") })
    private Double visibility;

    @ProviderMappings({ @Provider(name = ProviderName.OPENWEATHERMAP, property = "pressure"),
            @Provider(name = ProviderName.FORECASTIO, property = "pressure"),
            @Provider(name = ProviderName.WORLDWEATHERONLINE, property = "pressure"),
            @Provider(name = ProviderName.HAMWEATHER, property = "pressureMB"),
            @Provider(name = ProviderName.METEOBLUE, property = "pressure_hpa"),
            @Provider(name = ProviderName.APIXU, property = "pressure_mb"),
            @Provider(name = ProviderName.WEATHERBIT, property = "pres") })
    private Double pressure;

    private String pressureTrend;

    @ProviderMappings({ @Provider(name = ProviderName.FORECASTIO, property = "ozone"),
            @Provider(name = ProviderName.WEATHERBIT, property = "ozone") })
    private Integer ozone;

    @ProviderMappings({ @Provider(name = ProviderName.FORECASTIO, property = "uvIndex"),
            @Provider(name = ProviderName.WORLDWEATHERONLINE, property = "uvIndex"),
            @Provider(name = ProviderName.METEOBLUE, property = "uv_index"),
            @Provider(name = ProviderName.APIXU, property = "uv"),
            @Provider(name = ProviderName.WEATHERBIT, property = "uv") })
    private Integer uvIndex;

    /**
     * Returns the humidity in percent.
     */
    public Integer getHumidity() {
        return humidity;
    }

    /**
     * Sets the humidity.
     */
    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    /**
     * Returns the visibility in kilometers.
     */
    public Double getVisibility() {
        return visibility;
    }

    /**
     * Sets the visibility in kilometers.
     */
    public void setVisibility(Double visibility) {
        this.visibility = visibility;
    }

    /**
     * Returns the pressure in millibar.
     */
    public Double getPressure() {
        return pressure;
    }

    /**
     * Sets the pressure in millibar.
     */
    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    /**
     * Returns the pressure trend (up, down, equal).
     */
    public String getPressureTrend() {
        return pressureTrend;
    }

    /**
     * Sets the pressure trend.
     */
    public void setPressureTrend(String pressureTrend) {
        this.pressureTrend = pressureTrend;
    }

    /**
     * Returns the ozone in ppm.
     */
    public Integer getOzone() {
        return ozone;
    }

    /**
     * Sets the ozone.
     */
    public void setOzone(Integer ozone) {
        this.ozone = ozone;
    }

    /**
     * Returns the uv Index.
     */
    public Integer getUvIndex() {
        return uvIndex;
    }

    /**
     * Sets the uvIndex.
     */
    public void setUvIndex(Integer uvIndex) {
        this.uvIndex = uvIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("humidity", humidity)
                .append("visibility", visibility).append("pressure", pressure).append("pressureTrend", pressureTrend)
                .append("ozone", ozone).append("uvIndex", uvIndex).toString();
    }
}
