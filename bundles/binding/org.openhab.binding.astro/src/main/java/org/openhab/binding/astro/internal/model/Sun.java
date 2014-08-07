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

/**
 * Holds the calculated sun data.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class Sun extends RiseSet {

	private Range astroDawn = new Range();
	private Range nauticDawn = new Range();
	private Range civilDawn = new Range();

	private Range civilDusk = new Range();
	private Range nauticDusk = new Range();
	private Range astroDusk = new Range();

	private Range morningNight = new Range();
	private Range eveningNight = new Range();
	private Range daylight = new Range();
	private Range noon = new Range();
	private Range night = new Range();

	private Position position = new Position();

	private SunZodiac zodiac = new SunZodiac(null, null);

	private Season season = new Season();

	private SunEclipse eclipse = new SunEclipse();

	/**
	 * Returns the astro dawn range.
	 */
	public Range getAstroDawn() {
		return astroDawn;
	}

	/**
	 * Sets the astro dawn range.
	 */
	public void setAstroDawn(Range astroDawn) {
		this.astroDawn = astroDawn;
	}

	/**
	 * Returns the nautic dawn range.
	 */
	public Range getNauticDawn() {
		return nauticDawn;
	}

	/**
	 * Sets the nautic dawn range.
	 */
	public void setNauticDawn(Range nauticDawn) {
		this.nauticDawn = nauticDawn;
	}

	/**
	 * Returns the civil dawn range.
	 */
	public Range getCivilDawn() {
		return civilDawn;
	}

	/**
	 * Sets the civil dawn range.
	 */
	public void setCivilDawn(Range civilDawn) {
		this.civilDawn = civilDawn;
	}

	/**
	 * Returns the civil dusk range.
	 */
	public Range getCivilDusk() {
		return civilDusk;
	}

	/**
	 * Sets the civil dusk range.
	 */
	public void setCivilDusk(Range civilDusk) {
		this.civilDusk = civilDusk;
	}

	/**
	 * Returns the nautic dusk range.
	 */
	public Range getNauticDusk() {
		return nauticDusk;
	}

	/**
	 * Sets the nautic dusk range.
	 */
	public void setNauticDusk(Range nauticDusk) {
		this.nauticDusk = nauticDusk;
	}

	/**
	 * Returns the astro dusk range.
	 */
	public Range getAstroDusk() {
		return astroDusk;
	}

	/**
	 * Sets the astro dusk range.
	 */
	public void setAstroDusk(Range astroDusk) {
		this.astroDusk = astroDusk;
	}

	/**
	 * Returns the noon range, start and end is always equal.
	 */
	public Range getNoon() {
		return noon;
	}

	/**
	 * Sets the noon range.
	 */
	public void setNoon(Range noon) {
		this.noon = noon;
	}

	/**
	 * Returns the daylight range.
	 */
	public Range getDaylight() {
		return daylight;
	}

	/**
	 * Sets the daylight range.
	 */
	public void setDaylight(Range daylight) {
		this.daylight = daylight;
	}

	/**
	 * Returns the morning night range.
	 */
	public Range getMorningNight() {
		return morningNight;
	}

	/**
	 * Sets the morning night range.
	 */
	public void setMorningNight(Range morningNight) {
		this.morningNight = morningNight;
	}

	/**
	 * Returns the evening night range.
	 */
	public Range getEveningNight() {
		return eveningNight;
	}

	/**
	 * Sets the evening night range.
	 */
	public void setEveningNight(Range eveningNight) {
		this.eveningNight = eveningNight;
	}

	/**
	 * Returns the night range.
	 */
	public Range getNight() {
		return night;
	}

	/**
	 * Sets the night range.
	 */
	public void setNight(Range night) {
		this.night = night;
	}

	/**
	 * Returns the sun position.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Sets the sun position.
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * Returns the zodiac.
	 */
	public SunZodiac getZodiac() {
		return zodiac;
	}

	/**
	 * Sets the zodiac.
	 */
	public void setZodiac(SunZodiac zodiac) {
		this.zodiac = zodiac;
	}

	/**
	 * Returns the seasons.
	 */
	public Season getSeason() {
		return season;
	}

	/**
	 * Sets the seasons.
	 */
	public void setSeason(Season season) {
		this.season = season;
	}

	/**
	 * Returns the eclipses.
	 */
	public SunEclipse getEclipse() {
		return eclipse;
	}

	/**
	 * Sets the eclipses.
	 */
	public void setEclipse(SunEclipse eclipse) {
		this.eclipse = eclipse;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("sunrise", getRise())
				.append("noon", getNoon()).append("sunset", getSet()).append("night", night)
				.append("morningNight", morningNight).append("astroDawn", astroDawn).append("nauticDawn", nauticDawn)
				.append("civilDawn", civilDawn).append("civilDusk", civilDusk).append("nauticDusk", nauticDusk)
				.append("astroDusk", astroDusk).append("daylight", getDaylight())
				.append("eveningNight", getEveningNight()).append("eclipse", eclipse).toString();
	}

}
