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

import java.util.Calendar;

/**
 * Common provider model for current condition data.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class Condition {

    @ProviderMappings({ @Provider(name = ProviderName.OPENWEATHERMAP, property = "weather.description"),
            @Provider(name = ProviderName.FORECASTIO, property = "currently.summary"),
            @Provider(name = ProviderName.FORECASTIO, property = "daily.data.summary"),
            @Provider(name = ProviderName.WORLDWEATHERONLINE, property = "weatherDesc.value"),
            @Provider(name = ProviderName.HAMWEATHER, property = "weather"),
            @Provider(name = ProviderName.APIXU, property = "condition.text"),
            @Provider(name = ProviderName.WEATHERBIT, property = "weather.description") })
    private String text;

    @ProviderMappings({
            @Provider(name = ProviderName.OPENWEATHERMAP, property = "dt", converter = ConverterType.UNIX_DATE),
            @Provider(name = ProviderName.FORECASTIO, property = "time", converter = ConverterType.UNIX_DATE),
            @Provider(name = ProviderName.WORLDWEATHERONLINE, property = "localObsDateTime", converter = ConverterType.UTC_DATE),
            @Provider(name = ProviderName.WORLDWEATHERONLINE, property = "date", converter = ConverterType.DATE),
            @Provider(name = ProviderName.HAMWEATHER, property = "ob.timestamp", converter = ConverterType.UNIX_DATE),
            @Provider(name = ProviderName.HAMWEATHER, property = "periods.timestamp", converter = ConverterType.UNIX_DATE),
            @Provider(name = ProviderName.METEOBLUE, property = "last_model_update", converter = ConverterType.JSON_DATE),
            @Provider(name = ProviderName.APIXU, property = "last_updated_epoch", converter = ConverterType.UNIX_DATE),
            @Provider(name = ProviderName.APIXU, property = "date_epoch", converter = ConverterType.UNIX_DATE),
            @Provider(name = ProviderName.WEATHERBIT, property = "ts", converter = ConverterType.UNIX_DATE) })
    private Calendar observationTime;

    @ProviderMappings({ @Provider(name = ProviderName.OPENWEATHERMAP, property = "weather.id"),
            @Provider(name = ProviderName.WORLDWEATHERONLINE, property = "weatherCode"),
            @Provider(name = ProviderName.HAMWEATHER, property = "weatherPrimaryCoded", converter = ConverterType.MULTI_ID),
            @Provider(name = ProviderName.METEOBLUE, property = "pictocode"),
            @Provider(name = ProviderName.METEOBLUE, property = "pictocode_day"),
            @Provider(name = ProviderName.APIXU, property = "condition.code"),
            @Provider(name = ProviderName.WEATHERBIT, property = "weather.code") })
    private String id;
    private String commonId;

    @ProviderMappings({ @Provider(name = ProviderName.FORECASTIO, property = "currently.icon"),
            @Provider(name = ProviderName.FORECASTIO, property = "daily.data.icon"),
            @Provider(name = ProviderName.OPENWEATHERMAP, property = "icon"),
            @Provider(name = ProviderName.HAMWEATHER, property = "icon"),
            @Provider(name = ProviderName.APIXU, property = "condition.icon"),
            @Provider(name = ProviderName.WEATHERBIT, property = "weather.icon") })
    private String icon;
    private Calendar lastUpdate;

    /**
     * Returns the current condition as text.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the current condition as text.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the provider observation time.
     */
    public Calendar getObservationTime() {
        return observationTime;
    }

    /**
     * Sets the provider observation time.
     */
    public void setObservationTime(Calendar observationTime) {
        this.observationTime = observationTime;
    }

    /**
     * Returns the provider specific condition id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the provider specific condition id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the provider specific icon.
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the provider specific icon.
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * Returns the last update of the weather conditions.
     */
    public Calendar getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Sets the last update of the weather conditions.
     */
    public void setLastUpdate(Calendar lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Returns the common condition id.
     */
    public String getCommonId() {
        return commonId;
    }

    /**
     * Sets the common condition id.
     */
    public void setCommonId(String commonId) {
        this.commonId = commonId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("text", text)
                .append("lastUpdate", (lastUpdate == null) ? null : lastUpdate.getTime())
                .append("observationTime", (observationTime == null) ? null : observationTime.getTime())
                .append("id", id).append("icon", icon).append("commonId", commonId).toString();
    }
}
