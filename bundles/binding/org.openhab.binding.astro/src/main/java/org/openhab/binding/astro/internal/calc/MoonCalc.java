/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.calc;

import java.math.BigDecimal;
import java.util.Calendar;

import org.openhab.binding.astro.internal.model.Eclipse;
import org.openhab.binding.astro.internal.model.Moon;
import org.openhab.binding.astro.internal.model.MoonDistance;
import org.openhab.binding.astro.internal.model.MoonPhase;
import org.openhab.binding.astro.internal.model.MoonPhaseName;
import org.openhab.binding.astro.internal.model.Position;
import org.openhab.binding.astro.internal.model.Range;
import org.openhab.binding.astro.internal.model.Zodiac;
import org.openhab.binding.astro.internal.model.ZodiacSign;
import org.openhab.binding.astro.internal.util.DateTimeUtils;

/**
 * Calculates the phase, eclipse, rise, set, distance, illumination and age of
 * the moon.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 * @see based on the calculations of
 *      http://www.computus.de/mondphase/mondphase.htm azimuth/elevation and
 *      zodiac based on http://lexikon.astronomie.info/java/sunmoon/
 */

public class MoonCalc {
	private static final double NEW_MOON = 0;
	private static final double FULL_MOON = 0.5;
	private static final double FIRST_QUARTER = 0.25;
	private static final double LAST_QUARTER = 0.75;

	private static final double ECLIPSE_TYPE_MOON = 0.5;
	protected static final double ECLIPSE_TYPE_SUN = 0;
	protected static final int ECLIPSE_MODE_PARTIAL = 0;
	protected static final int ECLIPSE_MODE_TOTAL = 1;
	protected static final int ECLIPSE_MODE_RING = 2;

	/**
	 * Calculates all moon data at the specified coordinates
	 */
	public Moon getMoonInfo(Calendar calendar, double latitude, double longitude) {
		Moon moon = new Moon();

		double julianDate = DateTimeUtils.dateToJulianDate(calendar);
		double julianDateMidnight = DateTimeUtils.midnightDateToJulianDate(calendar);

		double[] riseSet = getRiseSet(calendar, latitude, longitude);
		Calendar rise = DateTimeUtils.timeToCalendar(calendar, riseSet[0]);
		Calendar set = DateTimeUtils.timeToCalendar(calendar, riseSet[1]);

		if (rise == null || set == null) {
			Calendar tomorrow = (Calendar) calendar.clone();
			tomorrow.add(Calendar.DAY_OF_MONTH, 1);

			double[] riseSeTomorrow = getRiseSet(tomorrow, latitude, longitude);
			if (rise == null) {
				rise = DateTimeUtils.timeToCalendar(tomorrow, riseSeTomorrow[0]);
			}
			if (set == null) {
				set = DateTimeUtils.timeToCalendar(tomorrow, riseSeTomorrow[1]);
			}
		}

		moon.setRise(new Range(rise, rise));
		moon.setSet(new Range(set, set));

		MoonPhase phase = moon.getPhase();
		phase.setNew(DateTimeUtils.toCalendar(getNextPhase(calendar, julianDateMidnight, NEW_MOON)));
		phase.setFirstQuarter(DateTimeUtils.toCalendar(getNextPhase(calendar, julianDateMidnight, FIRST_QUARTER)));
		phase.setFull(DateTimeUtils.toCalendar(getNextPhase(calendar, julianDateMidnight, FULL_MOON)));
		phase.setThirdQuarter(DateTimeUtils.toCalendar(getNextPhase(calendar, julianDateMidnight, LAST_QUARTER)));

		Eclipse eclipse = moon.getEclipse();
		double eclipseJd = getEclipse(calendar, ECLIPSE_TYPE_MOON, julianDateMidnight, ECLIPSE_MODE_PARTIAL);
		eclipse.setPartial(DateTimeUtils.toCalendar(eclipseJd));
		eclipseJd = getEclipse(calendar, ECLIPSE_TYPE_MOON, julianDateMidnight, ECLIPSE_MODE_TOTAL);
		eclipse.setTotal(DateTimeUtils.toCalendar(eclipseJd));

		double decimalYear = DateTimeUtils.getDecimalYear(calendar);
		MoonDistance apogee = moon.getApogee();
		double apogeeJd = getApogee(julianDate, decimalYear);
		apogee.setDate(DateTimeUtils.toCalendar(apogeeJd));
		apogee.setKilometer(getDistance(apogeeJd));

		MoonDistance perigee = moon.getPerigee();
		double perigeeJd = getPerigee(julianDate, decimalYear);
		perigee.setDate(DateTimeUtils.toCalendar(perigeeJd));
		perigee.setKilometer(getDistance(perigeeJd));

		setMoonPosition(julianDate, latitude, longitude, moon);
		setAgeAndPhaseName(calendar, phase);
		return moon;
	}

	/**
	 * Calculates the moon illumination and distance.
	 */
	public void setMoonPosition(Calendar calendar, double latitude, double longitude, Moon moon) {
		setMoonPosition(DateTimeUtils.dateToJulianDate(calendar), latitude, longitude, moon);
	}

	/**
	 * Calculates the moon illumination and distance from the julian date.
	 */
	private void setMoonPosition(double julianDate, double latitude, double longitude, Moon moon) {
		MoonPhase phase = moon.getPhase();
		phase.setIllumination(getIllumination(julianDate));

		MoonDistance distance = moon.getDistance();
		distance.setDate(Calendar.getInstance());
		distance.setKilometer(getDistance(julianDate));
		setAzimuthElevationZodiac(julianDate, latitude, longitude, moon);
	}

	/**
	 * Calculates the age and the current phase.
	 */
	private void setAgeAndPhaseName(Calendar calendar, MoonPhase phase) {
		double julianDateEndOfDay = DateTimeUtils.endOfDayDateToJulianDate(calendar);
		double parentNewMoon = getPreviousPhase(calendar, julianDateEndOfDay, NEW_MOON);
		double age = Math.abs(parentNewMoon - julianDateEndOfDay);
		phase.setAge((int) age);

		int illumination = (int) phase.getIllumination();
		boolean isWaxing = age < (29.530588853 / 2);
		if (DateTimeUtils.isSameDay(calendar, phase.getNew())) {
			phase.setName(MoonPhaseName.NEW);
		} else if (DateTimeUtils.isSameDay(calendar, phase.getFirstQuarter())) {
			phase.setName(MoonPhaseName.FIRST_QUARTER);
		} else if (DateTimeUtils.isSameDay(calendar, phase.getThirdQuarter())) {
			phase.setName(MoonPhaseName.THIRD_QUARTER);
		} else if (DateTimeUtils.isSameDay(calendar, phase.getFull())) {
			phase.setName(MoonPhaseName.FULL);
		} else if (illumination >= 0 && illumination < 50) {
			phase.setName(isWaxing ? MoonPhaseName.WAXING_CRESCENT : MoonPhaseName.WANING_CRESCENT);
		} else if (illumination >= 50 && illumination < 100) {
			phase.setName(isWaxing ? MoonPhaseName.WAXING_GIBBOUS : MoonPhaseName.WANING_GIBBOUS);
		}
	}

	/**
	 * Calculates moonrise and moonset.
	 */
	private double[] getRiseSet(Calendar calendar, double latitude, double longitude) {
		double lambda = prepareCoordinate(longitude, 180);
		if (longitude > 0) {
			lambda *= -1;
		}
		double phi = prepareCoordinate(latitude, 90);
		if (latitude < 0) {
			phi *= -1;
		}

		double moonJd = Math.floor(DateTimeUtils.midnightDateToJulianDate(calendar)) - 2400000.0;
		moonJd -= ((calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / 60000.0) / 1440.0;

		double sphi = SN(phi);
		double cphi = CS(phi);
		double sinho = SN(8.0 / 60.0);

		int hour = 1;
		double utrise = -1;
		double utset = -1;
		do {
			double yminus = SINALT(moonJd, hour - 1, lambda, cphi, sphi) - sinho;
			double yo = SINALT(moonJd, hour, lambda, cphi, sphi) - sinho;
			double yplus = SINALT(moonJd, hour + 1, lambda, cphi, sphi) - sinho;
			double[] quadRet = QUAD(yminus, yo, yplus);
			if (quadRet[3] == 1) {
				if (yminus < 0) {
					utrise = hour + quadRet[1];
				} else {
					utset = hour + quadRet[1];
				}
			}
			if (quadRet[3] == 2) {
				if (quadRet[0] < 0) {
					utrise = hour + quadRet[2];
					utset = hour + quadRet[1];
				} else {
					utrise = hour + quadRet[1];
					utset = hour + quadRet[2];
				}
			}
			yminus = yplus;
			hour += 2;

		} while (hour < 25 && (utrise == -1 || utset == -1));

		double rise = prepareTime(utrise);
		double set = prepareTime(utset);

		return new double[] { rise, set };
	}

	/**
	 * Prepares the coordinate for moonrise and moonset calculation.
	 */
	private double prepareCoordinate(double coordinate, double system) {
		double c = Math.abs(coordinate);

		if (c - Math.floor(c) >= .599) {
			c = Math.floor(c) + (c - Math.floor(c)) / 1 * .6;
		}
		if (c > system) {
			c = Math.floor(c) % system + (c - Math.floor(c));
		}
		return Math.round(c * 100.0) / 100.0;
	}

	/**
	 * Prepares a time value for converting to a calendar object.
	 */
	private double prepareTime(double riseSet) {
		if (riseSet == -1) {
			return riseSet;
		}
		double riseMinute = (riseSet - Math.floor(riseSet)) * 60.0 / 100.0;
		if (riseMinute >= .595) {
			riseMinute = 0;
			riseSet += 1;
		}
		riseSet = Math.floor(riseSet) + riseMinute;

		BigDecimal bd = new BigDecimal(Double.toString(riseSet));
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	/**
	 * Calculates the moon phase.
	 */
	private double calcMoonPhase(double k, double mode) {
		k = Math.floor(k) + mode;
		double t = k / 1236.85;
		double e = var_e(t);
		double m = var_m(k, t);
		double m1 = var_m1(k, t);
		double f = var_f(k, t);
		double o = var_o(k, t);
		double jd = var_jde(k, t);
		if (mode == NEW_MOON) {
			jd += -.4072 * SN(m1) + .17241 * e * SN(m) + .01608 * SN(2 * m1) + .01039 * SN(2 * f) + .00739 * e
					* SN(m1 - m) - .00514 * e * SN(m1 + m) + .00208 * e * e * SN(2 * m) - .00111 * SN(m1 - 2 * f)
					- .00057 * SN(m1 + 2 * f);
			jd += .00056 * e * SN(2 * m1 + m) - .00042 * SN(3 * m1) + .00042 * e * SN(m + 2 * f) + .00038 * e
					* SN(m - 2 * f) - .00024 * e * SN(2 * m1 - m) - .00017 * SN(o) - .00007 * SN(m1 + 2 * m) + .00004
					* SN(2 * m1 - 2 * f);
			jd += .00004 * SN(3 * m) + .00003 * SN(m1 + m - 2 * f) + .00003 * SN(2 * m1 + 2 * f) - .00003
					* SN(m1 + m + 2 * f) + .00003 * SN(m1 - m + 2 * f) - .00002 * SN(m1 - m - 2 * f) - .00002
					* SN(3 * m1 + m);
			jd += .00002 * SN(4 * m1);
		} else if (mode == FULL_MOON) {
			jd += -.40614 * SN(m1) + .17302 * e * SN(m) + .01614 * SN(2 * m1) + .01043 * SN(2 * f) + .00734 * e
					* SN(m1 - m) - .00515 * e * SN(m1 + m) + .00209 * e * e * SN(2 * m) - .00111 * SN(m1 - 2 * f)
					- .00057 * SN(m1 + 2 * f);
			jd += .00056 * e * SN(2 * m1 + m) - .00042 * SN(3 * m1) + .00042 * e * SN(m + 2 * f) + .00038 * e
					* SN(m - 2 * f) - .00024 * e * SN(2 * m1 - m) - .00017 * SN(o) - .00007 * SN(m1 + 2 * m) + .00004
					* SN(2 * m1 - 2 * f);
			jd += .00004 * SN(3 * m) + .00003 * SN(m1 + m - 2 * f) + .00003 * SN(2 * m1 + 2 * f) - .00003
					* SN(m1 + m + 2 * f) + .00003 * SN(m1 - m + 2 * f) - .00002 * SN(m1 - m - 2 * f) - .00002
					* SN(3 * m1 + m);
			jd += .00002 * SN(4 * m1);
		} else {
			jd += -.62801 * SN(m1) + .17172 * e * SN(m) - .01183 * e * SN(m1 + m) + .00862 * SN(2 * m1) + .00804
					* SN(2 * f) + .00454 * e * SN(m1 - m) + .00204 * e * e * SN(2 * m) - .0018 * SN(m1 - 2 * f) - .0007
					* SN(m1 + 2 * f);
			jd += -.0004 * SN(3 * m1) - .00034 * e * SN(2 * m1 - m) + .00032 * e * SN(m + 2 * f) + .00032 * e
					* SN(m - 2 * f) - .00028 * e * e * SN(m1 + 2 * m) + .00027 * e * SN(2 * m1 + m) - .00017 * SN(o);
			jd += -.00005 * SN(m1 - m - 2 * f) + .00004 * SN(2 * m1 + 2 * f) - .00004 * SN(m1 + m + 2 * f) + .00004
					* SN(m1 - 2 * m) + .00003 * SN(m1 + m - 2 * f) + .00003 * SN(3 * m) + .00002 * SN(2 * m1 - 2 * f);
			jd += .00002 * SN(m1 - m + 2 * f) - .00002 * SN(3 * m1 + m);
			double w = .00306 - .00038 * e * CS(m) + .00026 * CS(m1) - .00002 * CS(m1 - m) + .00002 * CS(m1 + m)
					+ .00002 * CS(2 * f);
			jd += (mode == FIRST_QUARTER) ? w : -w;
		}
		return moonCorrection(jd, t, k);
	}

	/**
	 * Calculates the eclipse.
	 */
	private double getEclipse(double k, double typ, int mode) {
		k = Math.floor(k) + typ;
		double t = k / 1236.85;
		double f = var_f(k, t);
		double jd = 0;
		double ringTest = 0;
		if (SN(Math.abs(f)) <= .36) {
			double o = var_o(k, t);
			double f1 = f - .02665 * SN(o);
			double a1 = 299.77 + .107408 * k - .009173 * t * t;
			double e = var_e(t);
			double m = var_m(k, t);
			double m1 = var_m1(k, t);
			double p = .207 * e * SN(m) + .0024 * e * SN(2 * m) - .0392 * SN(m1) + .0116 * SN(2 * m1) - .0073 * e
					* SN(m1 + m) + .0067 * e * SN(m1 - m) + .0118 * SN(2 * f1);
			double q = 5.2207 - .0048 * e * CS(m) + .002 * e * CS(2 * m) - .3299 * CS(m1) - .006 * e * CS(m1 + m)
					+ .0041 * e * CS(m1 - m);
			double g = (p * CS(f1) + q * SN(f1)) * (1 - .0048 * CS(Math.abs(f1)));
			double u = .0059 + .0046 * e * CS(m) - .0182 * CS(m1) + .0004 * CS(2 * m1) - .0005 * CS(m + m1);
			jd = var_jde(k, t);
			jd += (typ == ECLIPSE_TYPE_MOON) ? -.4065 * SN(m1) + .1727 * e * SN(m) : -.4075 * SN(m1) + .1721 * e
					* SN(m);

			jd += .0161 * SN(2 * m1) - .0097 * SN(2 * f1) + .0073 * e * SN(m1 - m) - .005 * e * SN(m1 + m) - .0023
					* SN(m1 - 2 * f1) + .0021 * e * SN(2 * m);
			jd += .0012 * SN(m1 + 2 * f1) + .0006 * e * SN(2 * m1 + m) - .0004 * SN(3 * m1) - .0003 * e
					* SN(m + 2 * f1) + .0003 * SN(a1) - .0002 * e * SN(m - 2 * f1) - .0002 * e * SN(2 * m1 - m) - .0002
					* SN(o);
			if (typ == ECLIPSE_TYPE_MOON) {
				if ((1.0248 - u - Math.abs(g)) / .545 <= 0)
					jd = 0; // no moon eclipse
				if (mode == ECLIPSE_MODE_PARTIAL && (1.0128 - u - Math.abs(g)) / .545 > 0
						&& (.4678 - u) * (.4678 - u) - g * g > 0)
					jd = 0; // no partial moon eclipse
				if (mode == ECLIPSE_MODE_TOTAL
						&& ((1.0128 - u - Math.abs(g)) / .545 <= 0 != (.4678 - u) * (.4678 - u) - g * g <= 0))
					jd = 0; // no total moon eclipse
			} else {
				if (Math.abs(g) > 1.5433 + u) {
					jd = 0; // no sun eclipse
				}
				if (mode == ECLIPSE_MODE_PARTIAL
						&& ((g >= -.9972 && g <= .9972) || (Math.abs(g) >= .9972 && Math.abs(g) < .9972 + Math.abs(u)))) {
					jd = 0; // no partial sun eclipse
				}
				if (mode > ECLIPSE_MODE_PARTIAL) {
					if ((g < -.9972 || g > .9972) || (Math.abs(g) < .9972 && Math.abs(g) > .9972 + Math.abs(u))) {
						jd = 0; // no ring or total sun eclipse
					}
					if (u > .0047 || u >= .00464 * Math.sqrt(1 - g * g)) {
						ringTest = 1; // no total sun eclipse
					}
					if (ringTest == 1 && mode == ECLIPSE_MODE_TOTAL) {
						jd = 0;
					}
					if (ringTest == 0 && mode == ECLIPSE_MODE_RING) {
						jd = 0;
					}
				}
			}
		}
		return jd;
	}

	/**
	 * Calculates the illumination.
	 */
	private double getIllumination(double jd) {
		double t = (jd - 2451545) / 36525;
		double d = 297.8502042 + 445267.11151686 * t - .00163 * t * t + t * t * t / 545868 - t * t * t * t / 113065000;
		double m = 357.5291092 + 35999.0502909 * t - .0001536 * t * t + t * t * t / 24490000;
		double m1 = 134.9634114 + 477198.8676313 * t + .008997 * t * t + t * t * t / 69699 - t * t * t * t / 14712000;
		double i = 180 - d - 6.289 * SN(m1) + 2.1 * SN(m) - 1.274 * SN(2 * d - m1) - .658 * SN(2 * d) - .241
				* SN(2 * m1) - .110 * SN(d);
		return (1 + CS(i)) / 2 * 100.0;
	}

	/**
	 * Calculates the next moon phase.
	 */
	private double getNextPhase(Calendar cal, double midnightJd, double mode) {
		double tz = 0;
		double phaseJd = 0;
		do {
			double k = var_k(cal, tz);
			tz += 1;
			phaseJd = calcMoonPhase(k, mode);
		} while (phaseJd <= midnightJd);
		return phaseJd;
	}

	/**
	 * Calculates the previous moon phase.
	 */
	public double getPreviousPhase(Calendar cal, double jd, double mode) {
		double tz = 0;
		double phaseJd = 0;
		do {
			double k = var_k(cal, tz);
			tz -= 1;
			phaseJd = calcMoonPhase(k, mode);
		} while (phaseJd > jd);
		return phaseJd;
	}

	/**
	 * Calculates the next eclipse.
	 */
	protected double getEclipse(Calendar cal, double type, double midnightJd, int mode) {
		double tz = 0;
		double eclipseJd = 0;
		do {
			double k = var_k(cal, tz);
			tz += 1;
			eclipseJd = getEclipse(k, type, mode);
		} while (eclipseJd <= midnightJd);
		return eclipseJd;
	}

	/**
	 * Calculates the date, where the moon is furthest away from the earth.
	 */
	private double getApogee(double julianDate, double decimalYear) {
		double k = Math.floor((decimalYear - 1999.97) * 13.2555) + .5;
		double jd = 0;
		do {
			double t = k / 1325.55;
			double d = 171.9179 + 335.9106046 * k - .010025 * t * t - .00001156 * t * t * t + .000000055 * t * t * t
					* t;
			double m = 347.3477 + 27.1577721 * k - .0008323 * t * t - .000001 * t * t * t;
			double f = 316.6109 + 364.5287911 * k - .0125131 * t * t - .0000148 * t * t * t;
			jd = 2451534.6698 + 27.55454988 * k - .0006886 * t * t - .000001098 * t * t * t + .0000000052 * t * t
					+ .4392 * SN(2 * d) + .0684 * SN(4 * d) + (.0456 - .00011 * t) * SN(m) + (.0426 - .00011 * t)
					* SN(2 * d - m) + .0212 * SN(2 * f);
			jd += -.0189 * SN(d) + .0144 * SN(6 * d) + .0113 * SN(4 * d - m) + .0047 * SN(2 * d + 2 * f) + .0036
					* SN(d + m) + .0035 * SN(8 * d) + .0034 * SN(6 * d - m) - .0034 * SN(2 * d - 2 * f) + .0022
					* SN(2 * d - 2 * m) - .0017 * SN(3 * d);
			jd += .0013 * SN(4 * d + 2 * f) + .0011 * SN(8 * d - m) + .001 * SN(4 * d - 2 * m) + .0009 * SN(10 * d)
					+ .0007 * SN(3 * d + m) + .0006 * SN(2 * m) + .0005 * SN(2 * d + m) + .0005 * SN(2 * d + 2 * m)
					+ .0004 * SN(6 * d + 2 * f);
			jd += .0004 * SN(6 * d - 2 * m) + .0004 * SN(10 * d - m) - .0004 * SN(5 * d) - .0004 * SN(4 * d - 2 * f)
					+ .0003 * SN(2 * f + m) + .0003 * SN(12 * d) + .0003 * SN(2 * d + 2 * f - m) - .0003 * SN(d - m);
			k += 1;
		} while (jd < julianDate);
		return jd;
	}

	/**
	 * Calculates the date, where the moon is closest to the earth.
	 */
	private double getPerigee(double julianDate, double decimalYear) {
		double k = Math.floor((decimalYear - 1999.97) * 13.2555);
		double jd = 0;
		do {
			double t = k / 1325.55;
			double d = 171.9179 + 335.9106046 * k - .010025 * t * t - .00001156 * t * t * t + .000000055 * t * t * t
					* t;
			double m = 347.3477 + 27.1577721 * k - .0008323 * t * t - .000001 * t * t * t;
			double f = 316.6109 + 364.5287911 * k - .0125131 * t * t - .0000148 * t * t * t;
			jd = 2451534.6698 + 27.55454988 * k - .0006886 * t * t - .000001098 * t * t * t + .0000000052 * t * t
					- 1.6769 * SN(2 * d) + .4589 * SN(4 * d) - .1856 * SN(6 * d) + .0883 * SN(8 * d);
			jd += -(.0773 + .00019 * t) * SN(2 * d - m) + (.0502 - .00013 * t) * SN(m) - .046 * SN(10 * d)
					+ (.0422 - .00011 * t) * SN(4 * d - m) - .0256 * SN(6 * d - m) + .0253 * SN(12 * d) + .0237 * SN(d);
			jd += .0162 * SN(8 * d - m) - .0145 * SN(14 * d) + .0129 * SN(2 * f) - .0112 * SN(3 * d) - .0104
					* SN(10 * d - m) + .0086 * SN(16 * d) + .0069 * SN(12 * d - m) + .0066 * SN(5 * d) - .0053
					* SN(2 * d + 2 * f);
			jd += -.0052 * SN(18 * d) - .0046 * SN(14 * d - m) - .0041 * SN(7 * d) + .004 * SN(2 * d + m) + .0032
					* SN(20 * d) - .0032 * SN(d + m) + .0031 * SN(16 * d - m);
			jd += -.0029 * SN(4 * d + m) - .0027 * SN(2 * d - 2 * m) + .0024 * SN(4 * d - 2 * m) - .0021
					* SN(6 * d - 2 * m) - .0021 * SN(22 * d) - .0021 * SN(18 * d - m);
			jd += .0019 * SN(6 * d + m) - .0018 * SN(11 * d) - .0014 * SN(8 * d + m) - .0014 * SN(4 * d - 2 * f)
					- .0014 * SN(6 * d - 2 * f) + .0014 * SN(3 * d + m) - .0014 * SN(5 * d + m) + .0013 * SN(13 * d);
			jd += .0013 * SN(20 * d - m) + .0011 * SN(3 * d + 2 * m) - .0011 * SN(4 * d + 2 * f - 2 * m) - .001
					* SN(d + 2 * m) - .0009 * SN(22 * d - m) - .0008 * SN(4 * f) + .0008 * SN(6 * d - 2 * f) + .0008
					* SN(2 * d - 2 * f + m);
			jd += .0007 * SN(2 * m) + .0007 * SN(2 * f - m) + .0007 * SN(2 * d + 4 * f) - .0006 * SN(2 * f - 2 * m)
					- .0006 * SN(2 * d - 2 * f + 2 * m) + .0006 * SN(24 * d) + .0005 * SN(4 * d - 4 * f) + .0005
					* SN(2 * d + 2 * m) - .0004 * SN(d - m) + .0027 * SN(9 * d) + .0027 * SN(4 * d + 2 * f);
			k += 1;
		} while (jd < julianDate);
		return jd;
	}

	/**
	 * Calculates the distance from the moon to earth.
	 */
	private double getDistance(double jd) {
		double t = (jd - 2451545) / 36525;
		double d = 297.8502042 + 445267.11151686 * t - .00163 * t * t + t * t * t / 545868 - t * t * t * t / 113065000;
		double m = 357.5291092 + 35999.0502909 * t - .0001536 * t * t + t * t * t / 24490000;
		double m1 = 134.9634114 + 477198.8676313 * t + .008997 * t * t + t * t * t / 69699 - t * t * t * t / 14712000;
		double f = 93.27209929999999 + 483202.0175273 * t - .0034029 * t * t - t * t * t / 3526000 + t * t * t * t
				/ 863310000;
		double sr = 385000.56 + getCoefficient(d, m, m1, f) / 1000;
		return sr;
	}

	public double[] calcMoon(double t) {
		double p2 = 6.283185307;
		double arc = 206264.8062;
		double coseps = .91748;
		double sineps = .39778;
		double lo = FRAK(.606433 + 1336.855225 * t);
		double l = p2 * FRAK(.374897 + 1325.55241 * t);
		double ls = p2 * FRAK(.993133 + 99.997361 * t);
		double d = p2 * FRAK(.827361 + 1236.853086 * t);
		double f = p2 * FRAK(.259086 + 1342.227825 * t);
		double dl = 22640 * Math.sin(l) - 4586 * Math.sin(l - 2 * d) + 2370 * Math.sin(2 * d) + 769 * Math.sin(2 * l)
				- 668 * Math.sin(ls) - 412 * Math.sin(2 * f) - 212 * Math.sin(2 * l - 2 * d) - 206
				* Math.sin(l + ls - 2 * d) + 192 * Math.sin(l + 2 * d) - 165 * Math.sin(ls - 2 * d) - 125 * Math.sin(d)
				- 110 * Math.sin(l + ls) + 148 * Math.sin(l - ls) - 55 * Math.sin(2 * f - 2 * d);
		double s = f + (dl + 412 * Math.sin(2 * f) + 541 * Math.sin(ls)) / arc;
		double h = f - 2 * d;
		double n = -526 * Math.sin(h) + 44 * Math.sin(l + h) - 31 * Math.sin(-l + h) - 23 * Math.sin(ls + h) + 11
				* Math.sin(-ls + h) - 25 * Math.sin(-2 * l + f) + 21 * Math.sin(-l + f);
		double lmoon = p2 * FRAK(lo + dl / 1296000);
		double bmoon = (18520 * Math.sin(s) + n) / arc;
		double cb = Math.cos(bmoon);
		double x = cb * Math.cos(lmoon);
		double v = cb * Math.sin(lmoon);
		double w = Math.sin(bmoon);
		double y = coseps * v - sineps * w;
		double z = sineps * v + coseps * w;
		double rho = Math.sqrt(1 - z * z);
		double dec = (360 / p2) * Math.atan(z / rho);
		double ra = (48 / p2) * Math.atan(y / (x + rho));
		if (ra < 0) {
			ra += 24;
		}
		return new double[] { dec, ra };
	}

	private double CS(double x) {
		return Math.cos(x * SunCalc.DEG2RAD);
	}

	private double SN(double x) {
		return Math.sin(x * SunCalc.DEG2RAD);
	}

	private double SINALT(double moonJd, int hour, double lambda, double cphi, double sphi) {
		double jdo = moonJd + hour / 24.0;
		double t = (jdo - 51544.5) / 36525.0;
		double decra[] = calcMoon(t);
		double tau = 15.0 * (LMST(jdo, lambda) - decra[1]);
		return sphi * SN(decra[0]) + cphi * CS(decra[0]) * CS(tau);
	}

	private double LMST(double moonJd, double lambda) {
		double moonJdo = Math.floor(moonJd);
		double ut = (moonJd - moonJdo) * 24.0;
		double t = (moonJdo - 51544.5) / 36525.0;
		double gmst = 6.697374558 + 1.0027379093 * ut + (8640184.812866 + (.093104 - .0000062 * t) * t) * t / 3600.0;
		return 24.0 * FRAK((gmst - lambda / 15.0) / 24.0);
	}

	private double FRAK(double x) {
		x = x - (int) (x);
		if (x < 0) {
			x += 1;
		}
		return x;
	}

	private double[] QUAD(double yminus, double yo, double yplus) {
		double nz = 0;
		double a = .5 * (yminus + yplus) - yo;
		double b = .5 * (yplus - yminus);
		double c = yo;
		double xe = -b / (2 * a);
		double ye = (a * xe + b) * xe + c;
		double dis = b * b - 4 * a * c;
		double zero1 = 0;
		double zero2 = 0;
		if (dis >= 0) {
			double dx = .5 * Math.sqrt(dis) / Math.abs(a);
			zero1 = xe - dx;
			zero2 = xe + dx;
			if (Math.abs(zero1) <= 1) {
				nz += 1;
			}
			if (Math.abs(zero2) <= 1) {
				nz += 1;
			}
			if (zero1 < -1) {
				zero1 = zero2;
			}
		}
		return new double[] { ye, zero1, zero2, nz };
	}

	private double var_o(double k, double t) {
		return 124.7746 - 1.5637558 * k + .0020691 * t * t + .00000215 * t * t * t;
	}

	private double var_f(double k, double t) {
		return 160.7108 + 390.67050274 * k - .0016341 * t * t - .00000227 * t * t * t + .000000011 * t * t * t * t;
	}

	private double var_m1(double k, double t) {
		return 201.5643 + 385.81693528 * k + .1017438 * t * t + .00001239 * t * t * t - .000000058 * t * t * t * t;
	}

	private double var_m(double k, double t) {
		return 2.5534 + 29.10535669 * k - .0000218 * t * t - .00000011 * t * t * t;
	}

	private double var_e(double t) {
		return 1 - .002516 * t - .0000074 * t * t;
	}

	private double var_jde(double k, double t) {
		return 2451550.09765 + 29.530588853 * k + .0001337 * t * t - .00000015 * t * t * t + .00000000073 * t * t * t
				* t;
	}

	private double var_k(Calendar cal, double tz) {
		return (cal.get(Calendar.YEAR) + (cal.get(Calendar.DAY_OF_YEAR) + tz) / 365 - 2000) * 12.3685;
	}

	private double moonCorrection(double jd, double t, double k) {
		jd += .000325 * SN(299.77 + .107408 * k - .009173 * t * t) + .000165 * SN(251.88 + .016321 * k) + .000164
				* SN(251.83 + 26.651886 * k) + .000126 * SN(349.42 + 36.412478 * k) + .00011
				* SN(84.66 + 18.206239 * k);
		jd += .000062 * SN(141.74 + 53.303771 * k) + .00006 * SN(207.14 + 2.453732 * k) + .000056
				* SN(154.84 + 7.30686 * k) + .000047 * SN(34.52 + 27.261239 * k) + .000042 * SN(207.19 + .121824 * k)
				+ .00004 * SN(291.34 + 1.844379 * k);
		jd += .000037 * SN(161.72 + 24.198154 * k) + .000035 * SN(239.56 + 25.513099 * k) + .000023
				* SN(331.55 + 3.592518 * k);
		return jd;
	}

	private double getCoefficient(double d, double m, double m1, double f) {
		int[] kd = new int[] { 0, 2, 2, 0, 0, 0, 2, 2, 2, 2, 0, 1, 0, 2, 0, 0, 4, 0, 4, 2, 2, 1, 1, 2, 2, 4, 2, 0, 2,
				2, 1, 2, 0, 0, 2, 2, 2, 4, 0, 3, 2, 4, 0, 2, 2, 2, 4, 0, 4, 1, 2, 0, 1, 3, 4, 2, 0, 1, 2, 2 };
		int[] km = new int[] { 0, 0, 0, 0, 1, 0, 0, -1, 0, -1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, -1, 0, 0, 0, 1,
				0, -1, 0, -2, 1, 2, -2, 0, 0, -1, 0, 0, 1, -1, 2, 2, 1, -1, 0, 0, -1, 0, 1, 0, 1, 0, 0, -1, 2, 1, 0, 0 };
		int[] km1 = new int[] { 1, -1, 0, 2, 0, 0, -2, -1, 1, 0, -1, 0, 1, 0, 1, 1, -1, 3, -2, -1, 0, -1, 0, 1, 2, 0,
				-3, -2, -1, -2, 1, 0, 2, 0, -1, 1, 0, -1, 2, -1, 1, -2, -1, -1, -2, 0, 1, 4, 0, -2, 0, 2, 1, -2, -3, 2,
				1, -1, 3, -1 };
		int[] kf = new int[] { 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, -2, 2, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
				0, 0, 0, 0, 0, 0, -2, 2, 0, 2, 0, 0, 0, 0, 0, 0, -2, 0, 0, 0, 0, -2, -2, 0, 0, 0, 0, 0, 0, 0, -2 };
		int[] kr = new int[] { -20905355, -3699111, -2955968, -569925, 48888, -3149, 246158, -152138, -170733, -204586,
				-129620, 108743, 104755, 10321, 0, 79661, -34782, -23210, -21636, 24208, 30824, -8379, -16675, -12831,
				-10445, -11650, 14403, -7003, 0, 10056, 6322, -9884, 5751, 0, -4950, 4130, 0, -3958, 0, 3258, 2616,
				-1897, -2117, 2354, 0, 0, -1423, -1117, -1571, -1739, 0, -4421, 0, 0, 0, 0, 1165, 0, 0, 8752 };
		double sr = 0;
		for (int t = 0; t < 60; t++) {
			sr += kr[t] * CS(kd[t] * d + km[t] * m + km1[t] * m1 + kf[t] * f);
		}
		return sr;
	}

	/**
	 * Sets the azimuth, elevation and zodiac in the moon object.
	 */
	private void setAzimuthElevationZodiac(double julianDate, double latitude, double longitude, Moon moon) {
		double lat = latitude * SunCalc.DEG2RAD;
		double lon = longitude * SunCalc.DEG2RAD;

		double gmst = toGMST(julianDate);
		double lmst = toLMST(gmst, lon) * 15. * SunCalc.DEG2RAD;

		double d = julianDate - 2447891.5;
		double anomalyMean = 360 * SunCalc.DEG2RAD / 365.242191 * d + 4.87650757829735 - 4.935239984568769;
		double nu = anomalyMean + 360.0 * SunCalc.DEG2RAD / Math.PI * 0.016713 * Math.sin(anomalyMean);
		double sunLon = mod2Pi(nu + 4.935239984568769);

		double l0 = 318.351648 * SunCalc.DEG2RAD;
		double p0 = 36.340410 * SunCalc.DEG2RAD;
		double n0 = 318.510107 * SunCalc.DEG2RAD;
		double i = 5.145396 * SunCalc.DEG2RAD;
		double l = 13.1763966 * SunCalc.DEG2RAD * d + l0;
		double mMoon = l - 0.1114041 * SunCalc.DEG2RAD * d - p0;
		double n = n0 - 0.0529539 * SunCalc.DEG2RAD * d;
		double c = l - sunLon;
		double ev = 1.2739 * SunCalc.DEG2RAD * Math.sin(2 * c - mMoon);
		double ae = 0.1858 * SunCalc.DEG2RAD * Math.sin(anomalyMean);
		double a3 = 0.37 * SunCalc.DEG2RAD * Math.sin(anomalyMean);
		double mMoon2 = mMoon + ev - ae - a3;
		double ec = 6.2886 * SunCalc.DEG2RAD * Math.sin(mMoon2);
		double a4 = 0.214 * SunCalc.DEG2RAD * Math.sin(2 * mMoon2);
		double l2 = l + ev + ec - ae + a4;
		double v = 0.6583 * SunCalc.DEG2RAD * Math.sin(2 * (l2 - sunLon));
		double l3 = l2 + v;
		double n2 = n - 0.16 * SunCalc.DEG2RAD * Math.sin(anomalyMean);

		double moonLon = mod2Pi(n2 + Math.atan2(Math.sin(l3 - n2) * Math.cos(i), Math.cos(l3 - n2)));
		double moonLat = Math.asin(Math.sin(l3 - n2) * Math.sin(i));

		double raDec[] = ecl2Equ(moonLat, moonLon, julianDate);

		double distance = (1 - 0.00301401) / (1 + 0.054900 * Math.cos(mMoon2 + ec)) * 384401;

		double raDecTopo[] = geoEqu2TopoEqu(raDec, distance, lat, lmst);
		double azAlt[] = equ2AzAlt(raDecTopo[0], raDecTopo[1], lat, lmst);

		Position position = moon.getPosition();
		position.setAzimuth(azAlt[0] * SunCalc.RAD2DEG);
		position.setElevation(azAlt[1] * SunCalc.RAD2DEG + refraction(azAlt[1]));

		// zodiac
		double idxd = Math.floor(moonLon * SunCalc.RAD2DEG / 30);
		int idx = 0;
		if (idxd < 0) {
			idx = (int) (Math.ceil(idxd));
		} else
			idx = (int) (Math.floor(idxd));

		if (idx >= 0 || idx <= ZodiacSign.values().length) {
			moon.setZodiac(new Zodiac(ZodiacSign.values()[idx]));
		}
	}

	private double mod2Pi(double x) {
		return (mod(x, 2. * Math.PI));
	}

	private double mod(double a, double b) {
		return (a - Math.floor(a / b) * b);
	}

	/**
	 * Transform equatorial coordinates (ra/dec) to horizonal coordinates
	 * (azimuth/altitude).
	 */
	private double[] equ2AzAlt(double ra, double dec, double geolat, double lmst) {
		double cosdec = Math.cos(dec);
		double sindec = Math.sin(dec);
		double lha = lmst - ra;
		double coslha = Math.cos(lha);
		double sinlha = Math.sin(lha);
		double coslat = Math.cos(geolat);
		double sinlat = Math.sin(geolat);

		double n = -cosdec * sinlha;
		double d = sindec * coslat - cosdec * coslha * sinlat;
		double az = mod2Pi(Math.atan2(n, d));
		double alt = Math.asin(sindec * sinlat + cosdec * coslha * coslat);

		return new double[] { az, alt };
	}

	/**
	 * Transform ecliptical coordinates (lon/lat) to equatorial coordinates
	 * (ra/dec)
	 */
	private double[] ecl2Equ(double lat, double lon, double jd) {
		double t = (jd - 2451545.0) / 36525.0;
		double eps = (23. + (26 + 21.45 / 60.) / 60. + t * (-46.815 + t * (-0.0006 + t * 0.00181)) / 3600.)
				* SunCalc.DEG2RAD;
		double coseps = Math.cos(eps);
		double sineps = Math.sin(eps);

		double sinlon = Math.sin(lon);
		double ra = mod2Pi(Math.atan2((sinlon * coseps - Math.tan(lat) * sineps), Math.cos(lon)));
		double dec = Math.asin(Math.sin(lat) * coseps + Math.cos(lat) * sineps * sinlon);

		return new double[] { ra, dec };
	}

	/**
	 * Transform geocentric equatorial coordinates (rA/dec) to topocentric
	 * equatorial coordinates.
	 */
	private double[] geoEqu2TopoEqu(double[] raDec, double distance, double observerLat, double lmst) {
		double cosdec = Math.cos(raDec[1]);
		double sindec = Math.sin(raDec[1]);
		double coslst = Math.cos(lmst);
		double sinlst = Math.sin(lmst);
		double coslat = Math.cos(observerLat);
		double sinlat = Math.sin(observerLat);
		double rho = getCenterDistance(observerLat);

		double x = distance * cosdec * Math.cos(raDec[0]) - rho * coslat * coslst;
		double y = distance * cosdec * Math.sin(raDec[0]) - rho * coslat * sinlst;
		double z = distance * sindec - rho * sinlat;

		double distanceTopocentric = Math.sqrt(x * x + y * y + z * z);
		double raTopo = mod2Pi(Math.atan2(y, x));
		double decTopo = Math.asin(z / distanceTopocentric);

		return new double[] { raTopo, decTopo };
	}

	/**
	 * Convert julian date to greenwich mean sidereal time.
	 */
	private double toGMST(double jd) {
		double ut = (jd - 0.5 - Math.floor(jd - 0.5)) * 24.;
		jd = Math.floor(jd - 0.5) + 0.5;
		double t = (jd - 2451545.0) / 36525.0;
		double t0 = 6.697374558 + t * (2400.051336 + t * 0.000025862);
		return (mod(t0 + ut * 1.002737909, 24.));
	}

	/**
	 * Convert greenwich mean sidereal time to local mean sidereal time.
	 */
	private double toLMST(double gmst, double lon) {
		return mod(gmst + SunCalc.RAD2DEG * lon / 15., 24.);
	}

	/**
	 * Returns geocentric distance from earth center.
	 */
	private double getCenterDistance(double lat) {
		double co = Math.cos(lat);
		double si = Math.sin(lat);
		double fl = 1.0 - 1.0 / 298.257223563;
		fl = fl * fl;
		si = si * si;
		double u = 1.0 / Math.sqrt(co * co + fl * si);
		double a = 6378.137 * u;
		double b = 6378.137 * fl * u;
		return Math.sqrt(a * a * co * co + b * b * si);
	}

	/**
	 * Returns altitude increase in altitude in degrees. Rough refraction
	 * formula using standard atmosphere: 1015 mbar and 10°C.
	 */
	private double refraction(double alt) {
		int pressure = 1015;
		int temperature = 10;
		double altdeg = alt * SunCalc.RAD2DEG;

		if (altdeg < -2 || altdeg >= 90)
			return 0;

		if (altdeg > 15)
			return 0.00452 * pressure / ((273 + temperature) * Math.tan(alt));

		double y = alt;
		double d = 0.0;
		double p = (pressure - 80.0) / 930.0;
		double q = 0.0048 * (temperature - 10.0);
		double y0 = y;
		double d0 = d;
		double n = 0.0;

		for (int i = 0; i < 3; i++) {
			n = y + (7.31 / (y + 4.4));
			n = 1.0 / Math.tan(n * SunCalc.DEG2RAD);
			d = n * p / (60.0 + q * (n + 39.0));
			n = y - y0;
			y0 = d - d0 - n;
			n = ((n != 0.0) && (y0 != 0.0)) ? y - n * (alt + d - y) / y0 : alt + d;
			y0 = y;
			d0 = d;
			y = n;
		}
		return d;
	}
}
