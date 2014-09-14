/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.common.binding;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Class with the forecast bindingConfig parsed from an item file.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class ForecastBindingConfig extends WeatherBindingConfig {
	private Integer forecastDay;

	/**
	 * Creates a forecast config.
	 */
	public ForecastBindingConfig(String locationId, Integer forecastDay, String type, String property) {
		super(locationId, type, property);
		this.forecastDay = forecastDay;
	}

	/**
	 * Returns the forecast day, 0 = today, 1 = tomorrow, ...
	 */
	public Integer getForecastDay() {
		return forecastDay;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getLocationId()).append(getType()).append(getProperty()).toHashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ForecastBindingConfig)) {
			return false;
		}
		ForecastBindingConfig comp = (ForecastBindingConfig) obj;
		return new EqualsBuilder().append(getLocationId(), comp.getLocationId()).append(getType(), comp.getType())
				.append(getProperty(), comp.getProperty()).append(forecastDay, comp.getForecastDay()).isEquals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		tsb.append("locationId", getLocationId()).append("forecast", forecastDay).append("type", getType())
				.append("property", getProperty());
		return tsb.toString();
	}

}
