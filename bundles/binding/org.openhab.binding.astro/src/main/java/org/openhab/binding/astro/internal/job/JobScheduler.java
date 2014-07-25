/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.job;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import org.openhab.binding.astro.AstroBindingProvider;
import org.openhab.binding.astro.internal.bus.PlanetPublisher;
import org.openhab.binding.astro.internal.common.AstroConfig;
import org.openhab.binding.astro.internal.common.AstroContext;
import org.openhab.binding.astro.internal.config.AstroBindingConfig;
import org.openhab.binding.astro.internal.model.PlanetName;
import org.openhab.binding.astro.internal.util.DelayedExecutor;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for scheduling the different jobs.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class JobScheduler {
	private static final Logger logger = LoggerFactory.getLogger(JobScheduler.class);

	private static final String JOB_GROUP = "Astro";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private AstroContext context;
	private Scheduler scheduler;

	private DelayedExecutor delayedExecutor = new DelayedExecutor();

	public JobScheduler(AstroContext context) {
		try {
			this.context = context;
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * Restarts the JobScheduler after a short delay.
	 */
	public void restart() {
		delayedExecutor.cancel();
		delayedExecutor.schedule(new TimerTask() {

			@Override
			public void run() {
				stop();
				start();
			}
		}, 5000);
	}

	/**
	 * Start the jobs if a binding is available.
	 */
	public void start() {
		startAndScheduleSunJob();

		if (isBindingForSunPositionAvailable()) {
			if (context.getConfig().getInterval() > 0) {
				scheduleSunPositionJob();
			} else {
				logger.warn("Azimuth/Elevation binding available, but configuration is disabled (interval = 0)!");
			}
		}
	}

	/**
	 * Stops all scheduled jobs and clears the PlanetPublisher cache.
	 */
	public void stop() {
		try {
			for (JobKey jobKey : scheduler.getJobKeys(jobGroupEquals(JOB_GROUP))) {
				logger.info("Deleting astro job: " + jobKey.getName());
				scheduler.deleteJob(jobKey);
			}
		} catch (SchedulerException ex) {
			logger.error(ex.getMessage(), ex);
		}
		PlanetPublisher.getInstance().clear();
	}

	/**
	 * Checks if a binding for the SunPosition Job is available.
	 */
	private boolean isBindingForSunPositionAvailable() {
		AstroBindingConfig azimuthBindingConfig = new AstroBindingConfig(PlanetName.SUN, "position", "azimuth");
		AstroBindingConfig elevationBindingConfig = new AstroBindingConfig(PlanetName.SUN, "position", "elevation");

		for (AstroBindingProvider provider : context.getProviders()) {
			if (provider.hasBinding(azimuthBindingConfig) || provider.hasBinding(elevationBindingConfig)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Schedules a daily job at midnight for Sun calculation and starts it
	 * immediately too.
	 */
	public void startAndScheduleSunJob() {
		String jobName = SunJob.class.getSimpleName();
		CronTrigger cronTrigger = newTrigger().withIdentity(jobName + "-Trigger", JOB_GROUP).startNow()
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?")).build();
		schedule(jobName, SunJob.class, cronTrigger, new JobDataMap());
		logger.info("Scheduled a daily job at midnight for astro sun calculation");

		Trigger trigger = newTrigger().withIdentity(jobName + "-StartupTrigger", JOB_GROUP).startNow().build();
		schedule(jobName + "-Startup", SunJob.class, trigger, new JobDataMap());
	}

	/**
	 * Schedules SunPosition with the specified interval and starts it
	 * immediately.
	 */
	public void scheduleSunPositionJob() {
		AstroConfig config = context.getConfig();

		String jobName = SunPositionJob.class.getSimpleName();
		Date start = new Date(System.currentTimeMillis() + (config.getInterval()) * 1000);
		Trigger trigger = newTrigger().withIdentity(jobName + "-Trigger", JOB_GROUP).startAt(start)
				.withSchedule(simpleSchedule().repeatForever().withIntervalInSeconds(config.getInterval())).build();

		schedule(jobName, SunPositionJob.class, trigger, new JobDataMap());
		logger.info("Scheduled astro SunPosition job with interval of {} seconds", config.getInterval());
	}

	/**
	 * Schedules a job at the specified date/time from the calendar object.
	 */
	public void schedule(Calendar calendar, String itemName) {
		if (System.currentTimeMillis() < calendar.getTimeInMillis()) {
			Trigger trigger = newTrigger().withIdentity(itemName + "-Trigger", JOB_GROUP).startAt(calendar.getTime())
					.build();
			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put("itemName", itemName);
			schedule(itemName, ItemJob.class, trigger, jobDataMap);
			logger.debug("Scheduled job for item {} at {}", itemName, sdf.format(calendar.getTime()));
		} else {
			logger.debug("Skipping job for item {} for today, starttime is in the past", itemName);
		}
	}

	/**
	 * Schedules a job by trigger.
	 */
	private void schedule(String jobName, Class<? extends Job> job, Trigger trigger, JobDataMap jobDataMap) {
		try {
			JobDetail jobDetail = newJob(job).withIdentity(jobName, JOB_GROUP).usingJobData(jobDataMap).build();
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

}
