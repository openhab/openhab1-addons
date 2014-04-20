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

import org.openhab.binding.astro.AstroBindingProvider;
import org.openhab.binding.astro.internal.calc.AstroCalculator;
import org.openhab.binding.astro.internal.calc.DayInfo;
import org.openhab.binding.astro.internal.common.AstroContext;
import org.openhab.binding.astro.internal.common.AstroType;
import org.openhab.core.library.types.DateTimeType;

/**
 * Publishes the DayInfo times and schedules the event jobs.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class DayInfoJob extends AbstractBaseJob {

	private AstroContext context = AstroContext.getInstance();

	@Override
	protected void executeJob() {
		AstroCalculator calc = new AstroCalculator();
		DayInfo dayInfo = calc.getDayInfo(Calendar.getInstance(), context.getConfig().getLatitude(), context
				.getConfig().getLongitude());

		publishState(AstroType.SUNRISE_TIME, new DateTimeType(dayInfo.getSunrise()));
		publishState(AstroType.NOON_TIME, new DateTimeType(dayInfo.getNoon()));
		publishState(AstroType.SUNSET_TIME, new DateTimeType(dayInfo.getSunset()));

		JobScheduler jobScheduler = new JobScheduler();
		if (providesBindingFor(AstroType.SUNRISE)) {
			jobScheduler.schedule(SunriseJob.class, dayInfo.getSunrise());
		}
		if (providesBindingFor(AstroType.NOON)) {
			jobScheduler.schedule(NoonJob.class, dayInfo.getNoon());
		}
		if (providesBindingFor(AstroType.SUNSET)) {
			jobScheduler.schedule(SunsetJob.class, dayInfo.getSunset());
		}
	}

	/**
	 * Returns true, if any provider has a binding for the specified AstroType.
	 */
	private boolean providesBindingFor(AstroType astroType) {
		for (AstroBindingProvider provider : context.getProviders()) {
			if (provider.providesBindingFor(astroType)) {
				return true;
			}
		}
		return false;
	}

}
