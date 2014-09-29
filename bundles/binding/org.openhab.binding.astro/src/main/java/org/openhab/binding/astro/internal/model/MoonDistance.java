/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.model;

import java.util.Calendar;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.astro.internal.util.DateTimeUtils;

/**
 * Holds a distance informations.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */

public class MoonDistance {
	private static final double KM_TO_MILES = 0.621371192;

	private Calendar date;
	private double kilometer;

	/**
	 * Returns the date of the calculated distance.
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * Sets the date of the calculated distance.
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * Returns the distance in kilometers.
	 */
	public double getKilometer() {
		return kilometer;
	}

	/**
	 * Sets the distance in kilometers.
	 */
	public void setKilometer(double kilometer) {
		this.kilometer = kilometer;
	}

	/**
	 * Returns the distance in miles.
	 */
	public double getMiles() {
		return kilometer * KM_TO_MILES;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("date", DateTimeUtils.getDate(date))
				.append("kilometer", kilometer).append("miles", getMiles()).toString();
	}

}
