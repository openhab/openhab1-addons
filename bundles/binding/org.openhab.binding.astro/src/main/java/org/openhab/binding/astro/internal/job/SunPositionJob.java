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
 * Calculates and publishes the current azimuth and elevation.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class SunPositionJob extends AbstractBaseJob {

	@Override
	protected void executeJob(JobDataMap jobDataMap) {
		SunCalc sunCalc = new SunCalc();
		SunPosition sp = sunCalc.getSunPosition(Calendar.getInstance(), context.getConfig().getLatitude(), context
				.getConfig().getLongitude());

		Sun sun = (Sun) context.getPlanet(PlanetName.SUN);
		sun.setPosition(sp);
		planetPublisher.publish(PlanetName.SUN);
	}
}
