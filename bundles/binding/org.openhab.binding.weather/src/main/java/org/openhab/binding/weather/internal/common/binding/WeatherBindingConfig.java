/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.common.binding;

import java.math.RoundingMode;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.core.binding.BindingConfig;

/**
 * Class with the weather bindingConfig parsed from an item file.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class WeatherBindingConfig implements BindingConfig {
	private String locationId;
	private String type;
	private String property;
	private RoundingMode roundingMode;
	private Integer scale;

	/**
	 * Creates a weather config.
	 */
	public WeatherBindingConfig(String locationId, String type, String property) {
		this.locationId = locationId;
		this.type = type;
		this.property = property;
	}

	public void setScale(RoundingMode roundingMode, Integer scale) {
		this.roundingMode = roundingMode;
		this.scale = scale;
	}

	/**
	 * Returns the location id.
	 */
	public String getLocationId() {
		return locationId;
	}

	/**
	 * Returns the property.
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns the scale for rounding a numeric value.
	 */
	public Integer getScale() {
		return scale;
	}

	/**
	 * Returns the scale mode.
	 */
	public RoundingMode getRoundingMode() {
		return roundingMode;
	}

	/**
	 * Returns the full property string.
	 */
	public String getWeatherProperty() {
		return type + "." + property;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(locationId).append(type).append(property).append(roundingMode)
				.append(scale).toHashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof WeatherBindingConfig)) {
			return false;
		}
		WeatherBindingConfig comp = (WeatherBindingConfig) obj;
		return new EqualsBuilder().append(locationId, comp.getLocationId()).append(type, comp.getType())
				.append(property, comp.getProperty()).append(roundingMode, comp.getRoundingMode())
				.append(scale, comp.getScale()).isEquals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		tsb.append("locationId", locationId).append("type", type).append("property", property);
		if (roundingMode != null) {
			tsb.append("roundingMode", roundingMode).append("scale", scale);
		}
		return tsb.toString();
	}
}
