/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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
			zodiacs = calculateZodiacs();
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
	private List<SunZodiac> calculateZodiacs() {
		List<SunZodiac> zodiacs = new ArrayList<SunZodiac>();

		zodiacs.add(new SunZodiac(ZodiacSign.ARIES, DateTimeUtils.getRange(Calendar.MARCH, 21, Calendar.APRIL, 19)));
		zodiacs.add(new SunZodiac(ZodiacSign.TAURUS, DateTimeUtils.getRange(Calendar.APRIL, 20, Calendar.MAY, 20)));
		zodiacs.add(new SunZodiac(ZodiacSign.GEMINI, DateTimeUtils.getRange(Calendar.MAY, 21, Calendar.JUNE, 20)));
		zodiacs.add(new SunZodiac(ZodiacSign.CANCER, DateTimeUtils.getRange(Calendar.JUNE, 21, Calendar.JULY, 22)));
		zodiacs.add(new SunZodiac(ZodiacSign.LEO, DateTimeUtils.getRange(Calendar.JULY, 23, Calendar.AUGUST, 22)));
		zodiacs.add(new SunZodiac(ZodiacSign.VIRGO, DateTimeUtils.getRange(Calendar.AUGUST, 23, Calendar.SEPTEMBER, 22)));
		zodiacs.add(new SunZodiac(ZodiacSign.LIBRA, DateTimeUtils.getRange(Calendar.SEPTEMBER, 23, Calendar.OCTOBER, 22)));
		zodiacs.add(new SunZodiac(ZodiacSign.SCORPIO, DateTimeUtils.getRange(Calendar.OCTOBER, 23, Calendar.NOVEMBER, 21)));
		zodiacs.add(new SunZodiac(ZodiacSign.SAGITTARIUS, DateTimeUtils.getRange(Calendar.NOVEMBER, 22, Calendar.DECEMBER,
				21)));
		zodiacs.add(new SunZodiac(ZodiacSign.CAPRICORN, DateTimeUtils.getRange(Calendar.DECEMBER, 22, Calendar.DECEMBER,
				31)));
		zodiacs.add(new SunZodiac(ZodiacSign.CAPRICORN, DateTimeUtils.getRange(Calendar.JANUARY, 1, Calendar.JANUARY, 19)));
		zodiacs.add(new SunZodiac(ZodiacSign.AQUARIUS, DateTimeUtils.getRange(Calendar.JANUARY, 20, Calendar.FEBRUARY, 18)));
		zodiacs.add(new SunZodiac(ZodiacSign.PISCES, DateTimeUtils.getRange(Calendar.FEBRUARY, 19, Calendar.MARCH, 20)));

		return zodiacs;
	}
}
