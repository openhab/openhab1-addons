/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.model.script.internal.actions;

import static org.quartz.TriggerBuilder.newTrigger;

import org.joda.time.DateTime;
import org.joda.time.base.AbstractInstant;
import org.openhab.model.script.actions.Timer;
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

	private boolean cancelled = false;
	private boolean terminated = false;
	
	public TimerImpl(JobKey jobKey, TriggerKey triggerKey, AbstractInstant startTime) {
		this.jobKey = jobKey;
		this.triggerKey = triggerKey;
		this.startTime = startTime;
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
	
	public boolean isRunning() {
		return DateTime.now().isAfter(startTime) && !terminated;
	}

	public boolean hasTerminated() {
		return terminated;
	}
	
	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}
}
