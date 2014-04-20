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
import java.util.TimerTask;

import org.openhab.binding.astro.AstroBindingProvider;
import org.openhab.binding.astro.internal.common.AstroConfig;
import org.openhab.binding.astro.internal.common.AstroContext;
import org.openhab.binding.astro.internal.common.AstroType;
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

	private AstroContext context = AstroContext.getInstance();
	private Scheduler scheduler;

	private DelayedExecutor delayedExecutor = new DelayedExecutor();

	public JobScheduler() {
		try {
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
		if (isBindingForDayInfoAvailable()) {
			startAndScheduleDayInfoJob();
		}

		if (isBindingForSunPositionAvailable()) {
			if (context.getConfig().getInterval() > 0) {
				startAndScheduleSunPositionJob();
			} else {
				logger.warn("Azimuth/Elevation binding available, but configuration is disabled (interval = 0)!");
			}
		}
	}

	/**
	 * Stops all scheduled jobs.
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
	}

	/**
	 * Checks if a binding for the DayInfo Job is available.
	 */
	private boolean isBindingForDayInfoAvailable() {
		for (AstroBindingProvider provider : context.getProviders()) {
			if (provider.providesBindingFor(AstroType.SUNRISE) || provider.providesBindingFor(AstroType.SUNRISE_TIME)
					|| provider.providesBindingFor(AstroType.NOON) || provider.providesBindingFor(AstroType.NOON_TIME)
					|| provider.providesBindingFor(AstroType.SUNSET)
					|| provider.providesBindingFor(AstroType.SUNSET_TIME)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if a binding for the SunPosition Job is available.
	 */
	private boolean isBindingForSunPositionAvailable() {
		for (AstroBindingProvider provider : context.getProviders()) {
			if (provider.providesBindingFor(AstroType.AZIMUTH) || provider.providesBindingFor(AstroType.ELEVATION)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Schedules a daily job at midnight for DayInfo calculation and starts it immediately too.
	 */
	public void startAndScheduleDayInfoJob() {
		String jobName = DayInfoJob.class.getSimpleName();
		CronTrigger cronTrigger = newTrigger().withIdentity(jobName + "-Trigger", JOB_GROUP).startNow()
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?")).build();
		schedule(jobName, DayInfoJob.class, cronTrigger);
		logger.info("Scheduled a daily job at midnight for astro DayInfo calculation");

		Trigger trigger = newTrigger().withIdentity(jobName + "-StartupTrigger", JOB_GROUP).startNow().build();
		schedule(jobName + "-Startup", DayInfoJob.class, trigger);
	}

	/**
	 * Schedules SunPosition with the specified interval and starts it immediately.
	 */
	public void startAndScheduleSunPositionJob() {
		AstroConfig config = AstroContext.getInstance().getConfig();

		String jobName = SunPositionJob.class.getSimpleName();
		Trigger trigger = newTrigger().withIdentity(jobName + "-Trigger", JOB_GROUP).startNow()
				.withSchedule(simpleSchedule().repeatForever().withIntervalInSeconds(config.getInterval())).build();

		schedule(jobName, SunPositionJob.class, trigger);
		logger.info("Scheduled astro SunPosition job with interval of {} seconds", config.getInterval());
	}

	/**
	 * Schedules a job at the specified date/time in the calendar object.
	 */
	protected void schedule(Class<? extends Job> job, Calendar calendar) {
		String jobName = job.getSimpleName();
		if (System.currentTimeMillis() < calendar.getTimeInMillis()) {
			Trigger trigger = newTrigger().withIdentity(jobName + "-Trigger", JOB_GROUP).startAt(calendar.getTime())
					.build();
			schedule(jobName, job, trigger);
			logger.debug("Scheduled {} for {}", jobName, sdf.format(calendar.getTime()));
		} else {
			logger.debug("Skipping {} for today, starttime is in the past", jobName);
		}
	}

	/**
	 * Schedules a job by trigger.
	 */
	private void schedule(String jobName, Class<? extends Job> job, Trigger trigger) {
		try {
			JobDataMap map = new JobDataMap();
			JobDetail jobDetail = newJob(job).withIdentity(jobName, JOB_GROUP).usingJobData(map).build();
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

}
