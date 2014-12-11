/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.job;

import org.openhab.binding.astro.internal.bus.PlanetPublisher;
import org.openhab.binding.astro.internal.common.AstroContext;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Baseclass for all jobs with common methods.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public abstract class AbstractBaseJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(AbstractBaseJob.class);
	protected AstroContext context = AstroContext.getInstance();
	protected PlanetPublisher planetPublisher = PlanetPublisher.getInstance();

	@Override
	public void execute(JobExecutionContext jobContext) throws JobExecutionException {
		JobDataMap jobDataMap = jobContext.getJobDetail().getJobDataMap();

		if (logger.isDebugEnabled()) {
			String itemName = jobDataMap.getString("itemName");
			if (itemName != null) {
				logger.debug("Starting Astro {} for item {}", this.getClass().getSimpleName(), itemName);
			} else {
				logger.debug("Starting Astro {}", this.getClass().getSimpleName());

			}
		}

		executeJob(jobDataMap);
	}

	/**
	 * Method to override by the different jobs to be executed.
	 */
	protected abstract void executeJob(JobDataMap jobDataMap);

}
