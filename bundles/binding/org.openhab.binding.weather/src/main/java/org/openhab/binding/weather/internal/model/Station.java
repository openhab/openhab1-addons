/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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

/**
 * Common provider model for station data.
 * 
 * @author Gerhard Riegler
 * @since 1.7.0
 */
public class Station {
	@ProviderMappings({ 
		@Provider(name = ProviderName.WUNDERGROUND, property = "current_observation.observation_location.full"),
		@Provider(name = ProviderName.HAMWEATHER, property = "place.name")
	})
	
	private String name;
	
	@ProviderMappings({ 
		@Provider(name = ProviderName.WUNDERGROUND, property = "current_observation.station_id"),
		@Provider(name = ProviderName.HAMWEATHER, property = "response.id")
	})
	private String id;
	
	@ProviderMappings({ 
		@Provider(name = ProviderName.WUNDERGROUND, property = "current_observation.observation_location.latitude"),
		@Provider(name = ProviderName.HAMWEATHER, property = "loc.lat")
	})
	private Double latitude;
	
	@ProviderMappings({ 
		@Provider(name = ProviderName.WUNDERGROUND, property = "current_observation.observation_location.longitude"),
		@Provider(name = ProviderName.HAMWEATHER, property = "loc.long")
	})
	private Double longitude;

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
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("name", name)
				.append("id", id).append("latitude", latitude).append("longitude", longitude).toString();
	}
	
}
