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
 * Common provider model for clouds data.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class Clouds {
    @ProviderMappings({ @Provider(name = ProviderName.OPENWEATHERMAP, property = "clouds.all"),
            @Provider(name = ProviderName.OPENWEATHERMAP, property = "clouds"),
            @Provider(name = ProviderName.FORECASTIO, property = "cloudCover", converter = ConverterType.FRACTION_INTEGER),
            @Provider(name = ProviderName.WORLDWEATHERONLINE, property = "cloudcover"),
            @Provider(name = ProviderName.HAMWEATHER, property = "sky"),
            @Provider(name = ProviderName.APIXU, property = "cloud"),
            @Provider(name = ProviderName.WEATHERBIT, property = "clouds") })
    private Integer percent;

    /**
     * Returns the sky cloud coverage in percent.
     */
    public Integer getPercent() {
        return percent;
    }

    /**
     * Sets the sky cloud coverage in percent.
     */
    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("percent", percent).toString();
    }
}
