/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.scheduler;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;

import java.util.TimerTask;

import org.openhab.binding.weather.WeatherBindingProvider;
import org.openhab.binding.weather.internal.bus.WeatherPublisher;
import org.openhab.binding.weather.internal.common.LocationConfig;
import org.openhab.binding.weather.internal.common.WeatherContext;
import org.openhab.binding.weather.internal.utils.DelayedExecutor;
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
 * Main class for scheduling weather jobs.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class WeatherJobScheduler {
	private static final Logger logger = LoggerFactory.getLogger(WeatherJobScheduler.class);

	private static final String JOB_GROUP = "Weather";

	private WeatherContext context;
	private Scheduler scheduler;

	private DelayedExecutor delayedExecutor = new DelayedExecutor();

	public WeatherJobScheduler(WeatherContext context) {
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
		}, 3000);
	}

	/**
	 * Start the weather jobs if a binding is available.
	 */
	public void start() {
		for (LocationConfig locationConfig : context.getConfig().getAllLocationConfigs()) {
			if (hasBinding(locationConfig.getLocationId())) {
				scheduleIntervalJob(locationConfig);
			} else {
				logger.info("Disabling weather locationId '{}', no binding available", locationConfig.getLocationId());
			}
		}
	}

	/**
	 * Returns true, if a binding for the locationId is available.
	 */
	private boolean hasBinding(String locationId) {
		for (WeatherBindingProvider provider : context.getProviders()) {
			if (provider.hasBinding(locationId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Stops all scheduled jobs and clears the WeatherPublisher cache.
	 */
	public void stop() {
		try {
			for (JobKey jobKey : scheduler.getJobKeys(jobGroupEquals(JOB_GROUP))) {
				logger.info("Deleting " + jobKey.getName());
				scheduler.deleteJob(jobKey);
			}
		} catch (SchedulerException ex) {
			logger.error(ex.getMessage(), ex);
		}
		WeatherPublisher.getInstance().clear();
	}

	/**
	 * Schedules the WeatherJob with the specified interval and starts it
	 * immediately.
	 */
	public void scheduleIntervalJob(LocationConfig locationConfig) {
		String jobName = "weatherJob-" + locationConfig.getLocationId();
		int interval = locationConfig.getUpdateInterval() * 60;
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("locationId", locationConfig.getLocationId());

		try {
			Trigger trigger = newTrigger().withIdentity(jobName + "-Trigger", JOB_GROUP).startNow()
					.withSchedule(simpleSchedule().repeatForever().withIntervalInSeconds(interval)).build();
			JobDetail jobDetail = newJob(WeatherJob.class).withIdentity(jobName, JOB_GROUP).usingJobData(jobDataMap)
					.build();
			scheduler.scheduleJob(jobDetail, trigger);
			logger.info("Starting and scheduling {} with interval of {} minutes", jobName,
					locationConfig.getUpdateInterval());
		} catch (SchedulerException ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

}
