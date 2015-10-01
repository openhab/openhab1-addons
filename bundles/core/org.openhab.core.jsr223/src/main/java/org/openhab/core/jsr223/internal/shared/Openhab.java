/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.jsr223.internal.shared;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.HashMap;

import org.joda.time.base.AbstractInstant;
import org.openhab.core.jsr223.internal.Jsr223CoreActivator;
import org.openhab.core.jsr223.internal.actions.TimerExecutionJob;
import org.openhab.core.jsr223.internal.actions.Timer;
import org.openhab.core.scriptengine.action.ActionService;

import org.openhab.model.script.actions.BusEvent;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Openhab-Class allows interoperability of the script with basic openhab functionality.
 * 
 * It is provides as "oh"-object in scripts.
 * 
 * @author Simon Merschjohann
 * @since 1.7.0
 */
public class Openhab extends BusEvent {
	private static final Logger logger = LoggerFactory.getLogger(Openhab.class);

	private static String LOGGER_NAME_PREFIX = "org.openhab.model.jsr223.";

	/**
	 * Returns all available action providers
	 * 
	 * @return map of all action providers
	 */
	public static HashMap<String, Object> getActions() {
		HashMap<String, Object> actions = new HashMap<String, Object>();

		Object[] services = Jsr223CoreActivator.actionServiceTracker.getServices();
		if (services != null) {
			for (Object service : services) {
				ActionService actionService = (ActionService) service;

				String className = actionService.getActionClassName().substring(actionService.getActionClassName().lastIndexOf(".") + 1);

				actions.put(className, actionService.getActionClass());
			}
		}

		return actions;
	}

	/**
	 * Get an action provider based on its name
	 * 
	 * @param action
	 * @return
	 */
	public static Object getAction(String action) {
		return getActions().get(action);
	}

	public static Logger getLogger(String loggerName) {
		return LoggerFactory.getLogger(LOGGER_NAME_PREFIX.concat(loggerName));
	}

	/**
	 * Creates the Log-Entry <code>format</code> with level <code>DEBUG</code> and logs under the loggers name
	 * <code>org.openhab.model.script.&lt;loggerName&gt;</code>
	 * 
	 * @param loggerName
	 *            the name of the Logger which is prefixed with <code>org.openhab.model.script.</code>
	 * @param format
	 *            the Log-Statement which can contain placeholders ' <code>{}</code>'
	 * @param args
	 *            the arguments to replace the placeholders contained in <code>format</code>
	 * 
	 * @see Logger
	 */
	static public void logDebug(String loggerName, String format, Object... args) {
		LoggerFactory.getLogger(LOGGER_NAME_PREFIX.concat(loggerName)).debug(format, args);
	}

	/**
	 * Creates the Log-Entry <code>format</code> with level <code>INFO</code> and logs under the loggers name
	 * <code>org.openhab.model.script.&lt;loggerName&gt;</code>
	 * 
	 * @param loggerName
	 *            the name of the Logger which is prefixed with <code>org.openhab.model.script.</code>
	 * @param format
	 *            the Log-Statement which can contain placeholders ' <code>{}</code>'
	 * @param args
	 *            the arguments to replace the placeholders contained in <code>format</code>
	 * 
	 * @see Logger
	 */
	static public void logInfo(String loggerName, String format, Object... args) {
		LoggerFactory.getLogger(LOGGER_NAME_PREFIX.concat(loggerName)).info(format, args);
	}

	/**
	 * Creates the Log-Entry <code>format</code> with level <code>WARN</code> and logs under the loggers name
	 * <code>org.openhab.model.script.&lt;loggerName&gt;</code>
	 * 
	 * @param loggerName
	 *            the name of the Logger which is prefixed with <code>org.openhab.model.script.</code>
	 * @param format
	 *            the Log-Statement which can contain placeholders ' <code>{}</code>'
	 * @param args
	 *            the arguments to replace the placeholders contained in <code>format</code>
	 * 
	 * @see Logger
	 */
	static public void logWarn(String loggerName, String format, Object... args) {
		LoggerFactory.getLogger(LOGGER_NAME_PREFIX.concat(loggerName)).warn(format, args);
	}

	/**
	 * Creates the Log-Entry <code>format</code> with level <code>ERROR</code> and logs under the loggers name
	 * <code>org.openhab.model.script.&lt;loggerName&gt;</code>
	 * 
	 * @param loggerName
	 *            the name of the Logger which is prefixed with <code>org.openhab.model.script.</code>
	 * @param format
	 *            the Log-Statement which can contain placeholders ' <code>{}</code>'
	 * @param args
	 *            the arguments to replace the placeholders contained in <code>format</code>
	 * 
	 * @see Logger
	 */
	static public void logError(String loggerName, String format, Object... args) {
		LoggerFactory.getLogger(LOGGER_NAME_PREFIX.concat(loggerName)).error(format, args);
	}

	public static Timer createTimer(AbstractInstant instant, Runnable closure) {
		JobDataMap dataMap = new JobDataMap();
		dataMap.put("procedure", closure);
		return makeTimer(instant, closure.toString(), dataMap);
	}

	private static Timer makeTimer(AbstractInstant instant, String closure, JobDataMap dataMap) {
		JobKey jobKey = new JobKey(instant.toString() + ": " + closure.toString());
		Trigger trigger = newTrigger().startAt(instant.toDate()).build();
		Timer timer = new Timer(jobKey, trigger.getKey(), instant);
		dataMap.put("timer", timer);
		try {
			JobDetail job = newJob(TimerExecutionJob.class).withIdentity(jobKey).usingJobData(dataMap).build();
			Timer.scheduler.scheduleJob(job, trigger);
			logger.debug("Scheduled code for execution at {}", instant.toString());
			return timer;
		} catch (SchedulerException e) {
			logger.error("Failed to schedule code for execution.", e);
			return null;
		}
	}
}
