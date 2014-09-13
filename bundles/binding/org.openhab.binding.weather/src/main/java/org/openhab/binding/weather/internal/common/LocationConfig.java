/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	private ProviderName providerName;
	private String language = "en";
	private Double latitude;
	private Double longitude;
	private Integer updateInterval;
	private String locationId;

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
	 * Returns true, if this config is valid.
	 */
	public boolean isValid() {
		return providerName != null && language != null && updateInterval != null && latitude != null
				&& longitude != null && locationId != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("providerName", providerName)
				.append("language", language).append("updateInterval", updateInterval).append("latitude", latitude)
				.append("longitude", longitude).append("locationId", locationId).toString();
	}

}
