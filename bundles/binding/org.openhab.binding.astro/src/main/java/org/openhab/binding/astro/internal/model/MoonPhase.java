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
 * Holds the calculates moon phase informations.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class MoonPhase {
	private Calendar firstQuarter;
	private Calendar full;
	private Calendar thirdQuarter;
	private Calendar _new;
	private int age;
	private double illumination;

	private MoonPhaseName name;

	/**
	 * Returns the date at which the moon is in the first quarter.
	 */
	public Calendar getFirstQuarter() {
		return firstQuarter;
	}

	/**
	 * Sets the date at which the moon is in the first quarter.
	 */
	public void setFirstQuarter(Calendar firstQuarter) {
		this.firstQuarter = firstQuarter;
	}

	/**
	 * Returns the date of the full moon.
	 */
	public Calendar getFull() {
		return full;
	}

	/**
	 * Sets the date of the full moon.
	 */
	public void setFull(Calendar full) {
		this.full = full;
	}

	/**
	 * Returns the date at which the moon is in the third quarter.
	 */
	public Calendar getThirdQuarter() {
		return thirdQuarter;
	}

	/**
	 * Sets the date at which the moon is in the third quarter.
	 */
	public void setThirdQuarter(Calendar thirdQuarter) {
		this.thirdQuarter = thirdQuarter;
	}

	/**
	 * Returns the date of the new moon.
	 */
	public Calendar getNew() {
		return _new;
	}

	/**
	 * Sets the date of the new moon.
	 */
	public void setNew(Calendar _new) {
		this._new = _new;
	}

	/**
	 * Returns the age in days.
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Sets the age in days.
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * Returns the illumination.
	 */
	public double getIllumination() {
		return illumination;
	}

	/**
	 * Sets the illumination.
	 */
	public void setIllumination(double illumination) {
		this.illumination = illumination;
	}

	/**
	 * Returns the phase name.
	 */
	public MoonPhaseName getName() {
		return name;
	}

	/**
	 * Sets the phase name.
	 */
	public void setName(MoonPhaseName name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("firstQuarter", DateTimeUtils.getDate(firstQuarter))
				.append("full", DateTimeUtils.getDate(full))
				.append("thirdQuarter", DateTimeUtils.getDate(thirdQuarter)).append("new", DateTimeUtils.getDate(_new))
				.append("age", age).append("illumination", illumination).append("name", name).toString();
	}
}
