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
package org.openhab.binding.weather.internal.common;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.weather.internal.model.ProviderName;

/**
 * Holds a location configuration from the openhab.cfg.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class LocationConfig {
    private static final int DEFAULT_UPDATE_INTERVAL = 240;

    private ProviderName providerName;
    private String language = "en";
    private Double latitude;
    private Double longitude;
    private String woeid;
    private Integer updateInterval = DEFAULT_UPDATE_INTERVAL;
    private String locationId;
    private String name;
    private String units = "si";

    /**
     * Returns the language.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the language.
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Returns the latitude.
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude.
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the longitude.
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude.
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns the woeid.
     * @deprecated The Yahoo weather API has ceased to exist. This method will be removed eventually.
     */
    public String getWoeid() {
        return woeid;
    }

    /**
     * Sets the woeid.
     * @deprecated The Yahoo weather API has ceased to exist. This method will be removed eventually.
     */
    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    /**
     * Returns the updateInterval in minutes.
     */
    public Integer getUpdateInterval() {
        return updateInterval;
    }

    /**
     * Sets the updateInterval in minutes.
     */
    public void setUpdateInterval(Integer updateInterval) {
        this.updateInterval = updateInterval;
    }

    /**
     * Returns the providerName.
     */
    public ProviderName getProviderName() {
        return providerName;
    }

    /**
     * Sets the providerName.
     */
    public void setProviderName(ProviderName providerName) {
        this.providerName = providerName;
    }

    /**
     * Returns the locationId.
     */
    public String getLocationId() {
        return locationId;
    }

    /**
     * Sets the locationId.
     */
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    /**
     * Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the units of measurement.
     */
    public String getMeasurementUnits() {
        return units;
    }

    /**
     * Sets the units of measurement.
     */
    public void setMeasurementUnits(String u) {
        units = u;
    }

    /**
     * Returns true, if this config is valid.
     */
    public boolean isValid() {
        boolean valid = providerName != null && language != null && updateInterval != null && locationId != null;
        if (!valid) {
            return false;
        }

        return latitude != null && longitude != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("providerName", providerName)
                .append("language", language).append("updateInterval", updateInterval).append("latitude", latitude)
                .append("longitude", longitude).append("locationId", locationId).append("name", name).toString();
    }

}
