/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.calc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.astro.internal.model.SunZodiac;
import org.openhab.binding.astro.internal.model.ZodiacSign;
import org.openhab.binding.astro.internal.util.DateTimeUtils;

/**
 * Calculates the sign and range of the current zodiac.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class SunZodiacCalc {
	private Map<Integer, List<SunZodiac>> zodiacsByYear = new HashMap<Integer, List<SunZodiac>>();

	/**
	 * Returns the zodiac for the specified calendar.
	 */
	public SunZodiac getZodiac(Calendar calendar) {
		int year = calendar.get(Calendar.YEAR);
		List<SunZodiac> zodiacs = zodiacsByYear.get(year);
		if (zodiacs == null) {
			zodiacs = calculateZodiacs(year);
			zodiacsByYear.clear();
			zodiacsByYear.put(year, zodiacs);
		}

		for (SunZodiac zodiac : zodiacs) {
			if (zodiac.isValid(calendar)) {
				return zodiac;
			}
		}

		return null;
	}

	/**
	 * Calculates the zodiacs for the current year.
	 */
	private List<SunZodiac> calculateZodiacs(int year) {
		List<SunZodiac> zodiacs = new ArrayList<SunZodiac>();

		zodiacs.add(new SunZodiac(ZodiacSign.ARIES, DateTimeUtils.getRange(year, Calendar.MARCH, 21, year, Calendar.APRIL, 19)));
		zodiacs.add(new SunZodiac(ZodiacSign.TAURUS, DateTimeUtils.getRange(year, Calendar.APRIL, 20, year, Calendar.MAY, 20)));
		zodiacs.add(new SunZodiac(ZodiacSign.GEMINI, DateTimeUtils.getRange(year, Calendar.MAY, 21, year, Calendar.JUNE, 20)));
		zodiacs.add(new SunZodiac(ZodiacSign.CANCER, DateTimeUtils.getRange(year, Calendar.JUNE, 21, year, Calendar.JULY, 22)));
		zodiacs.add(new SunZodiac(ZodiacSign.LEO, DateTimeUtils.getRange(year, Calendar.JULY, 23, year, Calendar.AUGUST, 22)));
		zodiacs.add(new SunZodiac(ZodiacSign.VIRGO, DateTimeUtils.getRange(year, Calendar.AUGUST, 23, year, Calendar.SEPTEMBER, 22)));
		zodiacs.add(new SunZodiac(ZodiacSign.LIBRA, DateTimeUtils.getRange(year, Calendar.SEPTEMBER, 23, year, Calendar.OCTOBER, 22)));
		zodiacs.add(new SunZodiac(ZodiacSign.SCORPIO, DateTimeUtils.getRange(year, Calendar.OCTOBER, 23, year, Calendar.NOVEMBER, 21)));
		zodiacs.add(new SunZodiac(ZodiacSign.SAGITTARIUS, DateTimeUtils.getRange(year, Calendar.NOVEMBER, 22, year, Calendar.DECEMBER, 21)));
		zodiacs.add(new SunZodiac(ZodiacSign.CAPRICORN, DateTimeUtils.getRange(year, Calendar.DECEMBER, 22, year + 1, Calendar.JANUARY, 19)));
		zodiacs.add(new SunZodiac(ZodiacSign.CAPRICORN, DateTimeUtils.getRange(year - 1, Calendar.DECEMBER, 22, year, Calendar.JANUARY, 19)));
		zodiacs.add(new SunZodiac(ZodiacSign.AQUARIUS, DateTimeUtils.getRange(year, Calendar.JANUARY, 20, year, Calendar.FEBRUARY, 18)));
		zodiacs.add(new SunZodiac(ZodiacSign.PISCES, DateTimeUtils.getRange(year, Calendar.FEBRUARY, 19, year, Calendar.MARCH, 20)));

		return zodiacs;
	}
}
