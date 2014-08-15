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
 * Extends the eclipse object with the ring-like eclipse information.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class SunEclipse extends Eclipse {
	private Calendar ring;

	/**
	 * Returns the date of the next ring-like eclipse.
	 */
	public Calendar getRing() {
		return ring;
	}

	/**
	 * Sets the date of the next ring-like eclipse.
	 */
	public void setRing(Calendar ring) {
		this.ring = ring;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("total", DateTimeUtils.getDate(getTotal()))
				.append("partial", DateTimeUtils.getDate(getPartial())).append("ring", DateTimeUtils.getDate(ring))
				.toString();
	}

}
