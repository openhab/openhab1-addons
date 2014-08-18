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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.openhab.binding.astro.AstroBindingProvider;
import org.openhab.binding.astro.internal.bus.PlanetPublisher;
import org.openhab.binding.astro.internal.common.AstroConfig;
import org.openhab.binding.astro.internal.common.AstroContext;
import org.openhab.binding.astro.internal.config.AstroBindingConfig;
import org.openhab.binding.astro.internal.model.PlanetName;
import org.openhab.binding.astro.internal.model.Season;
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
		startAndScheduleDailyJob();

		if (isBindingForIntervalJobAvailable()) {
			if (context.getConfig().getInterval() > 0) {
				scheduleIntervalJob();
			} else {
				logger.warn("Sun azimuth/elevation and/or moon distance/illumination binding available, but configuration is disabled (interval = 0)!");
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
	 * Checks if a binding for the IntervalJob is available.
	 */
	private boolean isBindingForIntervalJobAvailable() {
		List<AstroBindingConfig> intervalBindings = new ArrayList<AstroBindingConfig>();
		intervalBindings.add(new AstroBindingConfig(PlanetName.SUN, "position", "azimuth"));
		intervalBindings.add(new AstroBindingConfig(PlanetName.SUN, "position", "elevation"));
		intervalBindings.add(new AstroBindingConfig(PlanetName.MOON, "distance", "kilometer"));
		intervalBindings.add(new AstroBindingConfig(PlanetName.MOON, "distance", "miles"));
		intervalBindings.add(new AstroBindingConfig(PlanetName.MOON, "distance", "date"));
		intervalBindings.add(new AstroBindingConfig(PlanetName.MOON, "phase", "illumination"));
		intervalBindings.add(new AstroBindingConfig(PlanetName.MOON, "zodiac", "sign"));
		intervalBindings.add(new AstroBindingConfig(PlanetName.MOON, "position", "azimuth"));
		intervalBindings.add(new AstroBindingConfig(PlanetName.MOON, "position", "elevation"));

		for (AstroBindingProvider provider : context.getProviders()) {
			for (AstroBindingConfig astroBindingConfig : intervalBindings) {
				if (provider.hasBinding(astroBindingConfig)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Schedules a daily job at midnight for astro calculation and starts it
	 * immediately too.
	 */
	public void startAndScheduleDailyJob() {
		String jobName = DailyJob.class.getSimpleName();
		CronTrigger cronTrigger = newTrigger().withIdentity(jobName + "-Trigger", JOB_GROUP).startNow()
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?")).build();
		schedule(jobName, DailyJob.class, cronTrigger, new JobDataMap());
		logger.info("Scheduled a daily job at midnight for astro calculation");

		Trigger trigger = newTrigger().withIdentity(jobName + "-StartupTrigger", JOB_GROUP).startNow().build();
		schedule(jobName + "-Startup", DailyJob.class, trigger, new JobDataMap());
	}

	/**
	 * Schedules the IntervalJob with the specified interval and starts it
	 * immediately.
	 */
	public void scheduleIntervalJob() {
		AstroConfig config = context.getConfig();

		String jobName = IntervalJob.class.getSimpleName();
		Date start = new Date(System.currentTimeMillis() + (config.getInterval()) * 1000);
		Trigger trigger = newTrigger().withIdentity(jobName + "-Trigger", JOB_GROUP).startAt(start)
				.withSchedule(simpleSchedule().repeatForever().withIntervalInSeconds(config.getInterval())).build();

		schedule(jobName, IntervalJob.class, trigger, new JobDataMap());
		logger.info("Scheduled astro job with interval of {} seconds", config.getInterval());
	}

	/**
	 * Schedules next Season job.
	 */
	public void scheduleSeasonJob(Season season) {
		schedule(season.getNextSeason(), "Season", new JobDataMap());
	}

	/**
	 * Schedules a item job at the specified date/time from the calendar object.
	 */
	public void scheduleItem(Calendar calendar, String itemName) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("itemName", itemName);
		schedule(calendar, itemName, jobDataMap);
	}

	/**
	 * Schedules a job at the specified date/time, deletes a previously
	 * scheduled job.
	 */
	private void schedule(Calendar calendar, String jobName, JobDataMap jobDataMap) {
		if (System.currentTimeMillis() < calendar.getTimeInMillis()) {
			try {
				JobKey jobKey = new JobKey(jobName, JOB_GROUP);
				if (scheduler.getJobDetail(jobKey) != null) {
					scheduler.deleteJob(jobKey);
				}
				Trigger trigger = newTrigger().withIdentity(jobName + "-Trigger", JOB_GROUP)
						.startAt(calendar.getTime()).build();
				JobDetail jobDetail = newJob(ItemJob.class).withIdentity(jobKey).usingJobData(jobDataMap).build();
				scheduler.scheduleJob(jobDetail, trigger);
				logger.debug("Scheduled job with name {} at {}", jobName, sdf.format(calendar.getTime()));
			} catch (SchedulerException ex) {
				logger.error(ex.getMessage(), ex);
			}
		} else {
			logger.debug("Skipping job with name {} for today, starttime is in the past", jobName);
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
