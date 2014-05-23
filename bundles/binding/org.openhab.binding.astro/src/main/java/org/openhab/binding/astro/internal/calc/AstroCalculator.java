/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.calc;

import java.util.Calendar;

import com.mhuss.AstroLib.Astro;
import com.mhuss.AstroLib.DateOps;
import com.mhuss.AstroLib.Latitude;
import com.mhuss.AstroLib.Longitude;
import com.mhuss.AstroLib.NoInitException;
import com.mhuss.AstroLib.ObsInfo;
import com.mhuss.AstroLib.PlanetData;
import com.mhuss.AstroLib.Planets;
import com.mhuss.AstroLib.RiseSet;
import com.mhuss.AstroLib.TimeOps;
import com.mhuss.AstroLib.TimePair;

/**
 * Calculates the SunPosition (azimuth, elevation) and DayInfo (sunrise, noon,
 * sunset).
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 * @see Based on AstroLib from http://mhuss.com/AstroLib/
 */
public class AstroCalculator {
	private static final double J1970 = 2440588.0;
	private static final double MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;

	private Calendar calendar;
	private double latitude;
	private double longitude;

	/**
	 * Calculates the sun position (azimuth and elevation) with the specified
	 * date at the specified coordinates.
	 */
	public SunPosition getSunPosition(Calendar calendar, double latitude, double longitude) {
		setParameters(calendar, latitude, longitude);
		return getSunPosition();
	}

	/**
	 * Calculates sunrise, noon and sunset with the specified date at the
	 * specified coordinates.
	 */
	public DayInfo getDayInfo(Calendar calendar, double latitude, double longitude) {
		setParameters(calendar, latitude, longitude);
		return getDayInfo();
	}

	private void setParameters(Calendar calendar, double latitude, double longitude) {
		this.calendar = calendar;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	private DayInfo getDayInfo() {
		double offset = TimeOps.tzOffsetInDays(calendar) + TimeOps.dstOffsetInDays(calendar) + Astro.ROUND_UP;
		double jd = DateOps.calendarToDay(calendar) - offset;

		ObsInfo oi = new ObsInfo(new Latitude(latitude), new Longitude(longitude));

		TimePair timePair = RiseSet.getTimes(RiseSet.SUN, jd, oi, new PlanetData());
		Calendar start = toCalendar(timePair.a);
		Calendar end = toCalendar(timePair.b);

		boolean isNextDay = timePair.a != Astro.INVALID && timePair.b != Astro.INVALID && timePair.a > timePair.b;
		if (isNextDay) {
			end.add(Calendar.DAY_OF_MONTH, 1);
		}

		return new DayInfo(start, end);
	}

	/**
	 * Converts minutes from midnight to a calendar object.
	 */
	private Calendar toCalendar(double time) {
		if (time == Astro.INVALID) {
			return null;
		}
		int minutes = (int) (time * Astro.HOURS_PER_DAY * Astro.MINUTES_PER_HOUR + Astro.ROUND_UP);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, minutes / Astro.IMINUTES_PER_HOUR);
		cal.set(Calendar.MINUTE, minutes % Astro.IMINUTES_PER_HOUR);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	private SunPosition getSunPosition() {
		try {
			double jd = getJulianDate();
			ObsInfo oi = new ObsInfo(new Latitude(latitude), new Longitude(longitude));

			PlanetData pd = new PlanetData();
			pd.calc(Planets.SUN, jd, oi);

			return new SunPosition(180 - Math.toDegrees(pd.getAltAzLon()), Math.toDegrees(pd.getAltAzLat()));
		} catch (NoInitException ex) {
			throw new RuntimeException("Can't calculate SunPosition: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Returns the gregorian calendar date to a julian date.
	 */
	private double getJulianDate() {
		return calendar.getTimeInMillis() / MILLISECONDS_PER_DAY - 0.5 + J1970;
	}

}
