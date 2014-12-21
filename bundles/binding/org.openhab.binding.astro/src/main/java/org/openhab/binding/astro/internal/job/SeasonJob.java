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

import org.openhab.binding.astro.internal.calc.SeasonCalc;
import org.openhab.binding.astro.internal.model.PlanetName;
import org.openhab.binding.astro.internal.model.Sun;
import org.quartz.JobDataMap;

/**
 * Calculates and publishes the current season name.
 * Used to update the season name on season change.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class SeasonJob extends AbstractBaseJob {

	@Override
	protected void executeJob(JobDataMap jobDataMap) {
		Sun sun = (Sun) context.getPlanet(PlanetName.SUN);

		SeasonCalc seasonCalc = new SeasonCalc();
		sun.setSeason(seasonCalc.getSeason(Calendar.getInstance(), context.getConfig().getLatitude()));

		planetPublisher.publish(PlanetName.SUN);
	}
}
