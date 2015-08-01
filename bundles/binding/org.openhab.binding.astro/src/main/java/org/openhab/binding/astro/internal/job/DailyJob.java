/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.job;

import java.util.Calendar;

import org.openhab.binding.astro.internal.calc.MoonCalc;
import org.openhab.binding.astro.internal.calc.SeasonCalc;
import org.openhab.binding.astro.internal.calc.SunCalc;
import org.openhab.binding.astro.internal.calc.SunZodiacCalc;
import org.openhab.binding.astro.internal.model.Moon;
import org.openhab.binding.astro.internal.model.PlanetName;
import org.openhab.binding.astro.internal.model.Sun;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Calculates and publishes the Sun data.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class DailyJob extends AbstractBaseJob {
	private static final Logger logger = LoggerFactory.getLogger(DailyJob.class);

	@Override
	protected void executeJob(JobDataMap jobDataMap) {
		Calendar now = Calendar.getInstance();

		// sun
		SunCalc sunCalc = new SunCalc();
		Sun sun = sunCalc.getSunInfo(now, context.getConfig().getLatitude(), context.getConfig().getLongitude());
		sunCalc.setSunPosition(now, context.getConfig().getLatitude(), context.getConfig().getLongitude(), sun);

		SunZodiacCalc zodiacCalc = new SunZodiacCalc();
		sun.setZodiac(zodiacCalc.getZodiac(now));

		SeasonCalc seasonCalc = new SeasonCalc();
		sun.setSeason(seasonCalc.getSeason(now, context.getConfig().getLatitude()));
		context.getJobScheduler().scheduleSeasonJob(sun.getSeason());

		context.setPlanet(PlanetName.SUN, sun);
		logger.debug("{}", sun);
		planetPublisher.publish(PlanetName.SUN);

		// moon
		MoonCalc moonCalc = new MoonCalc();
		Moon moon = moonCalc.getMoonInfo(now, context.getConfig().getLatitude(), context.getConfig().getLongitude());
		context.setPlanet(PlanetName.MOON, moon);
		logger.debug("{}", moon);
		planetPublisher.publish(PlanetName.MOON);
	}
}
