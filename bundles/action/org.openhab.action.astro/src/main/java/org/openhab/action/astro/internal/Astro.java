/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.astro.internal;

import java.util.Calendar;
import java.util.Date;

import org.openhab.binding.astro.internal.calc.SunCalc;
import org.openhab.binding.astro.internal.model.Sun;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides static methods that can be used in automation rules for
 * calculating astronomical data.
 * 
 * @author Gerhard Riegler
 * @since 1.7.0
 */
public class Astro {
	private static final Logger logger = LoggerFactory.getLogger(Astro.class);

	private static final SunCalc sunCalc = new SunCalc();
	// simple microcache
	private static AstroConfig lastConfig;
	private static Sun lastSun;

	@ActionDoc(text = "Returns the sunrise start for the given date and coordinates")
	public static Calendar getAstroSunriseStart(
			@ParamDoc(name = "date", text = "The date to calculate the sunrise") Date date,
			@ParamDoc(name = "latitude", text = "The latitude") double latitude,
			@ParamDoc(name = "longitude", text = "The longitude") double longitude) {
		return getSun(date, latitude, longitude).getRise().getStart();
	}

	@ActionDoc(text = "Returns the sunrise end for the given date and coordinates")
	public static Calendar getAstroSunriseEnd(
			@ParamDoc(name = "date", text = "The date to calculate the sunrise") Date date,
			@ParamDoc(name = "latitude", text = "The latitude") double latitude,
			@ParamDoc(name = "longitude", text = "The longitude") double longitude) {
		return getSun(date, latitude, longitude).getRise().getEnd();
	}

	@ActionDoc(text = "Returns the sunset start for the given date and coordinates")
	public static Calendar getAstroSunsetStart(
			@ParamDoc(name = "date", text = "The date to calculate the sunset") Date date,
			@ParamDoc(name = "latitude", text = "The latitude") double latitude,
			@ParamDoc(name = "longitude", text = "The longitude") double longitude) {
		return getSun(date, latitude, longitude).getSet().getStart();
	}

	@ActionDoc(text = "Returns the sunset end for the given date and coordinates")
	public static Calendar getAstroSunsetEnd(
			@ParamDoc(name = "date", text = "The date to calculate the sunset") Date date,
			@ParamDoc(name = "latitude", text = "The latitude") double latitude,
			@ParamDoc(name = "longitude", text = "The longitude") double longitude) {
		return getSun(date, latitude, longitude).getSet().getEnd();
	}

	/**
	 * Calculates the sun data.
	 */
	private static Sun getSun(Date date, double latitude, double longitude) {
		if (date == null) {
			logger.warn("Unknown date: {}, using current date", date);
			date = new Date();
		}
		AstroConfig config = new AstroConfig(date, latitude, longitude);
		if (lastConfig == null || !lastConfig.equals(config)) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			lastSun = sunCalc.getSunInfo(cal, latitude, longitude);
			lastConfig = config;
		}
		return lastSun;
	}
}
