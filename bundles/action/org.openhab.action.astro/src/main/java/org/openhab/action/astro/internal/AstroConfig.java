/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.astro.internal;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Holds the rule method parameters.
 * 
 * @author Gerhard Riegler
 * @since 1.7.0
 */
public class AstroConfig {
	private Date date;
	private double latitude;
	private double longitude;

	/**
	 * Creates a new AstroConfig.
	 */
	public AstroConfig(Date date, double latitude, double longitude) {
		this.date = date;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Returns the date to calculate astro data.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * The latitude to calculate astro data.
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * The longitude to calculate astro data.
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(date).append(latitude).append(longitude).toHashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof AstroConfig)) {
			return false;
		}
		AstroConfig config = (AstroConfig) obj;
		return new EqualsBuilder().append(date, config.getDate()).append(latitude, config.getLatitude())
				.append(longitude, config.getLongitude()).isEquals();
	}
}
