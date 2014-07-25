/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.job;

import java.util.Calendar;

import org.openhab.binding.astro.internal.calc.SunCalc;
import org.openhab.binding.astro.internal.model.PlanetName;
import org.openhab.binding.astro.internal.model.Sun;
import org.openhab.binding.astro.internal.model.SunPosition;
import org.quartz.JobDataMap;

/**
 * Calculates and publishes the Sun data.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class SunJob extends AbstractBaseJob {

	@Override
	protected void executeJob(JobDataMap jobDataMap) {
		SunCalc sunCalc = new SunCalc();
		Sun sun = sunCalc.getSunInfo(Calendar.getInstance(), context.getConfig().getLatitude(), context.getConfig()
				.getLongitude());

		SunPosition sp = sunCalc.getSunPosition(Calendar.getInstance(), context.getConfig().getLatitude(), context
				.getConfig().getLongitude());

		sun.setPosition(sp);
		context.setPlanet(PlanetName.SUN, sun);

		planetPublisher.publish(PlanetName.SUN);
	}
}
