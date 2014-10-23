/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.astro.internal.util.DateTimeUtils;

/**
 * Holds the calculated moon data.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class Moon extends RiseSet {
	private MoonPhase phase = new MoonPhase();
	private MoonDistance apogee = new MoonDistance();
	private MoonDistance perigee = new MoonDistance();
	private MoonDistance distance = new MoonDistance();
	private Eclipse eclipse = new Eclipse();
	private Position position = new Position();
	private Zodiac zodiac = new Zodiac(null);

	/**
	 * Returns the moon phase.
	 */
	public MoonPhase getPhase() {
		return phase;
	}

	/**
	 * Sets the moon phase.
	 */
	public void setPhase(MoonPhase phase) {
		this.phase = phase;
	}

	/**
	 * Returns the apogee.
	 */
	public MoonDistance getApogee() {
		return apogee;
	}

	/**
	 * Sets the apogee.
	 */
	public void setApogee(MoonDistance apogee) {
		this.apogee = apogee;
	}

	/**
	 * Returns the perigee.
	 */
	public MoonDistance getPerigee() {
		return perigee;
	}

	/**
	 * Sets the perigee.
	 */
	public void setPerigee(MoonDistance perigee) {
		this.perigee = perigee;
	}

	/**
	 * Returns the eclipses.
	 */
	public Eclipse getEclipse() {
		return eclipse;
	}

	/**
	 * Sets the eclipses.
	 */
	public void setEclipse(Eclipse eclipse) {
		this.eclipse = eclipse;
	}

	/**
	 * Returns the current distance.
	 */
	public MoonDistance getDistance() {
		return distance;
	}

	/**
	 * Sets the current distance.
	 */
	public void setDistance(MoonDistance distance) {
		this.distance = distance;
	}

	/**
	 * Returns the position.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Sets the position.
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * Returns the zodiac.
	 */
	public Zodiac getZodiac() {
		return zodiac;
	}

	/**
	 * Sets the zodiac.
	 */
	public void setZodiac(Zodiac zodiac) {
		this.zodiac = zodiac;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("rise", DateTimeUtils.getDate(getRise().getStart()))
				.append("set", DateTimeUtils.getDate(getSet().getEnd())).append("phase", phase)
				.append("apogee", apogee).append("perigee", perigee).append("distance", distance)
				.append("eclipse", eclipse).append("position", position).append("zodiac", zodiac).toString();
	}
}
