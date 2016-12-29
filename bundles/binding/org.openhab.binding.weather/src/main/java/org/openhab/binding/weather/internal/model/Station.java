/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.weather.internal.annotation.Provider;
import org.openhab.binding.weather.internal.annotation.ProviderMappings;
import org.openhab.binding.weather.internal.converter.ConverterType;

/**
 * Common provider model for station data.
 *
 * @author Gerhard Riegler
 * @since 1.7.0
 */
public class Station {
    @ProviderMappings({
            @Provider(name = ProviderName.WUNDERGROUND, property = "current_observation.observation_location.full"),
            @Provider(name = ProviderName.HAMWEATHER, property = "place.name"),
            @Provider(name = ProviderName.OPENWEATHERMAP, property = "name") })
    private String name;

    @ProviderMappings({
            @Provider(name = ProviderName.WUNDERGROUND, property = "current_observation.station_id"),
            @Provider(name = ProviderName.HAMWEATHER, property = "response.id") })
    private String id;

    @ProviderMappings({
            @Provider(name = ProviderName.WUNDERGROUND, property = "current_observation.observation_location.latitude"),
            @Provider(name = ProviderName.HAMWEATHER, property = "loc.lat"),
            @Provider(name = ProviderName.METEOBLUE, property = "lat"),
            @Provider(name = ProviderName.FORECASTIO, property = "latitude"),
            @Provider(name = ProviderName.OPENWEATHERMAP, property = "coord.lat") })
    private Double latitude;

    @ProviderMappings({
            @Provider(name = ProviderName.WUNDERGROUND, property = "current_observation.observation_location.longitude"),
            @Provider(name = ProviderName.HAMWEATHER, property = "loc.long"),
            @Provider(name = ProviderName.METEOBLUE, property = "lon"),
            @Provider(name = ProviderName.FORECASTIO, property = "longitude"),
            @Provider(name = ProviderName.OPENWEATHERMAP, property = "coord.lon") })
    private Double longitude;

    @ProviderMappings({
            @Provider(name = ProviderName.WUNDERGROUND, property = "current_observation.observation_location.elevation", converter = ConverterType.FEET),
            @Provider(name = ProviderName.HAMWEATHER, property = "profile.elevM"),
            @Provider(name = ProviderName.METEOBLUE, property = "asl") })
    private Double altitude;

    /**
     * Returns the station name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the station name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the station id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the station id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the station latitude.
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Sets the station latitude.
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the station longitude.
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Sets the station longitude.
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns the station altitude.
     */
    public Double getAltitude() {
        return altitude;
    }

    /**
     * Sets the station altitude.
     */
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("name", name).append("id", id)
                .append("latitude", latitude).append("longitude", longitude).append("altitude", altitude).toString();
    }

}
