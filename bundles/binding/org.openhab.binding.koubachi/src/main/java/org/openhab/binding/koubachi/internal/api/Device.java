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
package org.openhab.binding.koubachi.internal.api;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represents a device (plant sensor) in the Koubachi domain.
 *
 * @author Thomas.Eichstaedt-Engelen
 * @author John Cocula -- updated to Koubachi v3 API
 * @since 1.2.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Device extends KoubachiResource {

    BigDecimal virtualBatteryLevel;
    String ssid;
    String hardwareProductType;

    Date lastTransmission;
    Date nextTransmission;
    Date associatedSince;

    String recentSoilmoistureReadingValue;
    Date recentSoilmoistureReadingTime;
    BigDecimal recentSoilmoistureReadingSiValue; // added in v3 API
    String recentTemperatureReadingValue;
    Date recentTemperatureReadingTime;
    BigDecimal recentTemperatureReadingSiValue; // added in v3 API
    String recentLightReadingValue;
    Date recentLightReadingTime;
    BigDecimal recentLightReadingSiValue; // added in v3 API
    // below added in Koubachi v3 API:
    String recentSoiltemperatureReadingValue;
    Date recentSoiltemperatureReadingTime;
    BigDecimal recentSoiltemperatureReadingSiValue;
    BigDecimal soiltemperaturePollingInterval;
    String recentIrlightReadingValue;
    Date recentIrlightReadingTime;
    BigDecimal recentIrlightReadingSiValue;
    String hardwareProductGeneration;
    String hardwareProductName;

    @JsonProperty("mac_address")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("virtual_battery_level")
    public BigDecimal getVirtualBatteryLevel() {
        return virtualBatteryLevel;
    }

    @JsonProperty("ssid")
    public String getSsid() {
        return ssid;
    }

    @JsonProperty("hardware_product_type")
    public String getHardwareProductType() {
        return hardwareProductType;
    }

    @JsonProperty("last_transmission")
    public Date getLastTransmission() {
        return lastTransmission;
    }

    @JsonProperty("next_transmission")
    public Date getNextTransmission() {
        return nextTransmission;
    }

    @JsonProperty("associated_since")
    public Date getAssociatedSince() {
        return associatedSince;
    }

    @JsonProperty("recent_soilmoisture_reading_value")
    public String getRecentSoilmoistureReadingValue() {
        return recentSoilmoistureReadingValue;
    }

    @JsonProperty("recent_soilmoisture_reading_time")
    public Date getRecentSoilmoistureReadingTime() {
        return recentSoilmoistureReadingTime;
    }

    @JsonProperty("recent_soilmoisture_reading_si_value")
    public BigDecimal getRecentSoilmoistureReadingSiValue() {
        return recentSoilmoistureReadingSiValue;
    }

    @JsonProperty("recent_temperature_reading_value")
    public String getRecentTemperatureReadingValue() {
        return recentTemperatureReadingValue;
    }

    @JsonProperty("recent_temperature_reading_time")
    public Date getRecentTemperatureReadingTime() {
        return recentTemperatureReadingTime;
    }

    @JsonProperty("recent_temperature_reading_si_value")
    public BigDecimal getRecentTemperatureReadingSiValue() {
        return recentTemperatureReadingSiValue;
    }

    @JsonProperty("recent_light_reading_value")
    public String getRecentLightReadingValue() {
        return recentLightReadingValue;
    }

    @JsonProperty("recent_light_reading_time")
    public Date getRecentLightReadingTime() {
        return recentLightReadingTime;
    }

    @JsonProperty("recent_light_reading_si_value")
    public BigDecimal getRecentLightReadingSiValue() {
        return recentLightReadingSiValue;
    }

    @JsonProperty("recent_soiltemperature_reading_value")
    public String getRecentSoiltemperatureReadingValue() {
        return recentSoiltemperatureReadingValue;
    }

    @JsonProperty("recent_soiltemperature_reading_time")
    public Date getRecentSoiltemperatureReadingTime() {
        return recentSoiltemperatureReadingTime;
    }

    @JsonProperty("recent_soiltemperature_reading_si_value")
    public BigDecimal getRecentSoiltemperatureReadingSiValue() {
        return recentSoiltemperatureReadingSiValue;
    }

    @JsonProperty("soiltemperature_polling_interval")
    public BigDecimal getSoiltemperaturePollingInterval() {
        return soiltemperaturePollingInterval;
    }

    @JsonProperty("recent_irlight_reading_value")
    public String getRecentIrlightReadingValue() {
        return recentIrlightReadingValue;
    }

    @JsonProperty("recent_irlight_reading_time")
    public Date getRecentIrlightReadingTime() {
        return recentIrlightReadingTime;
    }

    @JsonProperty("recent_irlight_reading_si_value")
    public BigDecimal getRecentIrlightReadingSiValue() {
        return recentIrlightReadingSiValue;
    }

    @JsonProperty("hardware_product_generation")
    public String getHardwareProductGeneration() {
        return hardwareProductGeneration;
    }

    @JsonProperty("hardware_product_name")
    public String getHardwareProductName() {
        return hardwareProductName;
    }

    @Override
    public String toString() {
        return "Device [virtualBatteryLevel=" + virtualBatteryLevel + ", ssid=" + ssid + ", hardwareProductType="
                + hardwareProductType + ", lastTransmission=" + lastTransmission + ", nextTransmission="
                + nextTransmission + ", associatedSince=" + associatedSince + ", recentSoilmoistureReadingValue="
                + recentSoilmoistureReadingValue + ", recentSoilmoistureReadingTime=" + recentSoilmoistureReadingTime
                + ", recentSoilmoistureReadingSiValue=" + recentSoilmoistureReadingSiValue
                + ", recentTemperatureReadingValue=" + recentTemperatureReadingValue + ", recentTemperatureReadingTime="
                + recentTemperatureReadingTime + ", recentTemperatureReadingSiValue=" + recentTemperatureReadingSiValue
                + ", recentLightReadingValue=" + recentLightReadingValue + ", recentLightReadingTime="
                + recentLightReadingTime + ", recentLightReadingSiValue=" + recentLightReadingSiValue
                + ", recentSoiltemperatureReadingValue=" + recentSoiltemperatureReadingValue
                + ", recentSoiltemperatureReadingTime=" + recentSoiltemperatureReadingTime
                + ", recentSoiltemperatureReadingSiValue=" + recentSoiltemperatureReadingSiValue
                + ", soiltemperaturePollingInterval=" + soiltemperaturePollingInterval + ", recentIrlightReadingValue="
                + recentIrlightReadingValue + ", recentIrlightReadingTime=" + recentIrlightReadingTime
                + ", recentIrlightReadingSiValue=" + recentIrlightReadingSiValue + ", hardwareProductGeneration="
                + hardwareProductGeneration + ", hardwareProductName=" + hardwareProductName + "]";
    }

}
