/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.script.internal.actions;

import static org.quartz.TriggerBuilder.newTrigger;

//version 1.0.1: import added to allow construction of cron
import static org.quartz.CronScheduleBuilder.cronSchedule;

import org.joda.time.DateTime;
import org.joda.time.base.AbstractInstant;
import org.openhab.model.script.actions.Timer;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an implementation of the {@link Timer} interface using the Quartz
 * library for scheduling.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 *
 * @author Peter Broucke
 * @since 1.0.1
 * Extended by Peter Broucke to support CronSchedulers
 *
 */
public class TimerImpl implements Timer {

	private static final Logger logger = LoggerFactory.getLogger(TimerImpl.class);

	// the scheduler used for timer events
	public static Scheduler scheduler;

	static {
		 try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
	     logger.error("initializing scheduler throws exception", e);
		}
	}
	
	private JobKey jobKey;
	private TriggerKey triggerKey;
	private AbstractInstant startTime;
	// version 1.0.1 : add also option to use cronExpression instead of time
	private String cronExpression;
	
	private boolean cancelled = false;
	private boolean terminated = false;
	
	public TimerImpl(JobKey jobKey, TriggerKey triggerKey, AbstractInstant startTime) {
		this.jobKey = jobKey;
		this.triggerKey = triggerKey;
		this.startTime = startTime;
	}

	// version 1.0.1 : add also option to create a timer using a cron expression
	public TimerImpl(JobKey jobKey, TriggerKey triggerKey, String cronExpression) {
		this.jobKey = jobKey;
		this.triggerKey = triggerKey;
		this.cronExpression = cronExpression;
	}
	
	
	public boolean cancel() {
		try {
			boolean result = scheduler.deleteJob(jobKey);
			if(result) {
				cancelled = true;
			}
		} catch (SchedulerException e) {
			logger.warn("An error occured while cancelling the job '{}': {}", new String[] { jobKey.toString(), e.getMessage() });
		}
		return cancelled;
	}
	
	public boolean reschedule(AbstractInstant newTime) {
		try {
	        Trigger trigger = newTrigger().startAt(newTime.toDate()).build();
			scheduler.rescheduleJob(triggerKey, trigger);
			this.triggerKey = trigger.getKey();
			this.cancelled = false;
			this.terminated = false;
			return true;
		} catch (SchedulerException e) {
			logger.warn("An error occured while rescheduling the job '{}': {}", new String[] { jobKey.toString(), e.getMessage() });
			return false;
		}
	}
	
	// version 1.0.1 : allow to reschedule the cron timer
	public boolean reschedule(String cronExpression) {
		try {
	        Trigger trigger = newTrigger()
	        		.withSchedule( cronSchedule(cronExpression) )
			        .build();
    
			scheduler.rescheduleJob(triggerKey, trigger);
			this.triggerKey = trigger.getKey();
			this.cancelled = false;
			this.cronExpression = cronExpression;
			this.terminated = false;
			return true;
		} catch (SchedulerException e) {
			logger.warn("An error occured while rescheduling the job '{}': {}", new String[] { jobKey.toString(), e.getMessage() });
			return false;
		}	
	}

	
	public boolean isRunning() {
		try {
			for(JobExecutionContext context : scheduler.getCurrentlyExecutingJobs()) {
				if(context.getJobDetail().getKey().equals(jobKey)) {
					return true;
				}
			}
			return false;
		} catch (SchedulerException e) {
			// fallback implementation
			logger.debug("An error occured getting currently running jobs: {}", e.getMessage());
			return DateTime.now().isAfter(startTime) && !terminated;
		}
	}

	public boolean hasTerminated() {
		return terminated;
	}
	
	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}
}
