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

import org.openhab.binding.astro.internal.model.Season;
import org.openhab.binding.astro.internal.model.SeasonName;
import org.openhab.binding.astro.internal.util.DateTimeUtils;

/**
 * Calculates the seasons of the year.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 * @see based on the calculations of http://stellafane.org/misc/equinox.html
 */
public class SeasonCalc {
	private int currentYear;
	private Season currentSeason;

	/**
	 * Returns the seasons of the year of the specified calendar.
	 */
	public Season getSeason(Calendar calendar, double latitude) {
		int year = calendar.get(Calendar.YEAR);
		boolean isSouthernHemisphere = latitude < 0.0;
		Season season = currentSeason;
		if (currentYear != year) {
			season = new Season();
			if (!isSouthernHemisphere) {
				season.setSpring(calcEquiSol(0, year));
				season.setSummer(calcEquiSol(1, year));
				season.setAutumn(calcEquiSol(2, year));
				season.setWinter(calcEquiSol(3, year));
			} else {
				season.setSpring(calcEquiSol(2, year));
				season.setSummer(calcEquiSol(3, year));
				season.setAutumn(calcEquiSol(0, year));
				season.setWinter(calcEquiSol(1, year));
			}
			currentSeason = season;
			currentYear = year;
		}

		season.setName(!isSouthernHemisphere ? getCurrentSeasonNameNorthern(calendar) : getCurrentSeasonNameSouthern(calendar));
		return season;
	}

	/**
	 * Returns the current season name for the northern hemisphere.
	 */
	private SeasonName getCurrentSeasonNameNorthern(Calendar calendar) {
		long currentMillis = calendar.getTimeInMillis();
		if (currentMillis < currentSeason.getSpring().getTimeInMillis()
				|| currentMillis >= currentSeason.getWinter().getTimeInMillis()) {
			return SeasonName.WINTER;
		} else if (currentMillis >= currentSeason.getSpring().getTimeInMillis()
				&& currentMillis < currentSeason.getSummer().getTimeInMillis()) {
			return SeasonName.SPRING;
		} else if (currentMillis >= currentSeason.getSummer().getTimeInMillis()
				&& currentMillis < currentSeason.getAutumn().getTimeInMillis()) {
			return SeasonName.SUMMER;
		} else if (currentMillis >= currentSeason.getAutumn().getTimeInMillis()
				&& currentMillis < currentSeason.getWinter().getTimeInMillis()) {
			return SeasonName.AUTUMN;
		}
		return null;
	}

	/**
	 * Returns the current season name for the southern hemisphere.
	 */
	private SeasonName getCurrentSeasonNameSouthern(Calendar calendar) {
		long currentMillis = calendar.getTimeInMillis();
		if (currentMillis < currentSeason.getAutumn().getTimeInMillis()
				|| currentMillis >= currentSeason.getSummer().getTimeInMillis()) {
			return SeasonName.SUMMER;
		} else if (currentMillis >= currentSeason.getAutumn().getTimeInMillis()
				&& currentMillis < currentSeason.getWinter().getTimeInMillis()) {
			return SeasonName.AUTUMN;
		} else if (currentMillis >= currentSeason.getWinter().getTimeInMillis()
				&& currentMillis < currentSeason.getSpring().getTimeInMillis()) {
			return SeasonName.WINTER;
		} else if (currentMillis >= currentSeason.getSpring().getTimeInMillis()
				&& currentMillis < currentSeason.getSummer().getTimeInMillis()) {
			return SeasonName.SPRING;
		}
		return null;
	}

	/**
	 * Calculates the date of the season.
	 */
	private Calendar calcEquiSol(int season, int year) {
		double estimate = calcInitial(season, year);
		double t = (estimate - 2451545.0) / 36525;
		double w = 35999.373 * t - 2.47;
		double dl = 1 + 0.0334 * cosDeg(w) + 0.0007 * cosDeg(2 * w);
		double s = periodic24(t);
		double julianDate = estimate + ((0.00001 * s) / dl);
		return DateTimeUtils.toCalendar(julianDate);
	}

	/**
	 * Calculate an initial guess of the Equinox or Solstice of a given year.
	 */
	private double calcInitial(int season, int year) {
		double Y = (year - 2000) / 1000d;
		switch (season) {
		case 0:
			return 2451623.80984 + 365242.37404 * Y + 0.05169 * Math.pow(Y, 2) - 0.00411 * Math.pow(Y, 3) - 0.00057
					* Math.pow(Y, 4);
		case 1:
			return 2451716.56767 + 365241.62603 * Y + 0.00325 * Math.pow(Y, 2) + 0.00888 * Math.pow(Y, 3) - 0.00030
					* Math.pow(Y, 4);
		case 2:
			return 2451810.21715 + 365242.01767 * Y - 0.11575 * Math.pow(Y, 2) + 0.00337 * Math.pow(Y, 3) + 0.00078
					* Math.pow(Y, 4);
		case 3:
			return 2451900.05952 + 365242.74049 * Y - 0.06223 * Math.pow(Y, 2) - 0.00823 * Math.pow(Y, 3) + 0.00032
					* Math.pow(Y, 4);
		}
		return 0;
	}

	/**
	 * Calculate 24 periodic terms
	 */
	private double periodic24(double T) {
		int[] a = new int[] { 485, 203, 199, 182, 156, 136, 77, 74, 70, 58, 52, 50, 45, 44, 29, 18, 17, 16, 14, 12, 12,
				12, 9, 8 };
		double[] b = new double[] { 324.96, 337.23, 342.08, 27.85, 73.14, 171.52, 222.54, 296.72, 243.58, 119.81,
				297.17, 21.02, 247.54, 325.15, 60.93, 155.12, 288.79, 198.04, 199.76, 95.39, 287.11, 320.81, 227.73,
				15.45 };
		double[] c = new double[] { 1934.136, 32964.467, 20.186, 445267.112, 45036.886, 22518.443, 65928.934, 3034.906,
				9037.513, 33718.147, 150.678, 2281.226, 29929.562, 31555.956, 4443.417, 67555.328, 4562.452, 62894.029,
				31436.921, 14577.848, 31931.756, 34777.259, 1222.114, 16859.074 };

		double result = 0;
		for (int i = 0; i < 24; i++) {
			result += a[i] * cosDeg(b[i] + (c[i] * T));
		}
		return result;
	}

	/**
	 * Cosinus of a degree value.
	 */
	private double cosDeg(double deg) {
		return Math.cos(deg * Math.PI / 180);
	}

}
