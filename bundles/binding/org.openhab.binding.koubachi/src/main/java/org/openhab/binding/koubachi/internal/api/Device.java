/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	String recentTemperatureReadingValue;
	Date recentTemperatureReadingTime;
	String recentLightReadingValue;
	Date recentLightReadingTime;
	
	
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
	
	@JsonProperty("recent_temperature_reading_value")
	public String getRecentTemperatureReadingValue() {
		return recentTemperatureReadingValue;
	}
	
	@JsonProperty("recent_temperature_reading_time")
	public Date getRecentTemperatureReadingTime() {
		return recentTemperatureReadingTime;
	}
	
	@JsonProperty("recent_light_reading_value")
	public String getRecentLightReadingValue() {
		return recentLightReadingValue;
	}
	
	@JsonProperty("recent_light_reading_time")
	public Date getRecentLightReadingTime() {
		return recentLightReadingTime;
	}

	@Override
	public String toString() {
		return "Device [virtualBatteryLevel=" + virtualBatteryLevel + ", ssid="
				+ ssid + ", hardwareProductType=" + hardwareProductType
				+ ", lastTransmission=" + lastTransmission
				+ ", nextTransmission=" + nextTransmission
				+ ", associatedSince=" + associatedSince
				+ ", recentSoilmoistureReadingValue="
				+ recentSoilmoistureReadingValue
				+ ", recentSoilmoistureReadingTime="
				+ recentSoilmoistureReadingTime
				+ ", recentTemperatureReadingValue="
				+ recentTemperatureReadingValue
				+ ", recentTemperatureReadingTime="
				+ recentTemperatureReadingTime + ", recentLightReadingValue="
				+ recentLightReadingValue + ", recentLightReadingTime="
				+ recentLightReadingTime + "]";
	}
	
}
