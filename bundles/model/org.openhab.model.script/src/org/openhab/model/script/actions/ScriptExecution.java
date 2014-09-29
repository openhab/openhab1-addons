/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.script.actions;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.apache.commons.lang.StringUtils;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.joda.time.base.AbstractInstant;
import org.openhab.core.scriptengine.Script;
import org.openhab.core.scriptengine.ScriptEngine;
import org.openhab.core.scriptengine.ScriptExecutionException;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.script.internal.ScriptActivator;
import org.openhab.model.script.internal.actions.TimerExecutionJob;
import org.openhab.model.script.internal.actions.TimerImpl;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The static methods of this class are made available as functions in the scripts.
 * This allows a script to call another script, which is available as a file.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@SuppressWarnings("restriction")
public class ScriptExecution {

	private static final Logger logger = LoggerFactory.getLogger(ScriptExecution.class);

	/**
	 * Calls a script which must be located in the configurations/scripts folder.
	 * 
	 * @param scriptName the name of the script (if the name does not end with
	 * the .script file extension it is added)
	 * 
	 * @return the return value of the script
	 * @throws ScriptExecutionException if an error occurs during the execution
	 */
	public static Object callScript(String scriptName) throws ScriptExecutionException {
		ModelRepository repo = ScriptActivator.modelRepositoryTracker.getService();
		if(repo!=null) {
			String scriptNameWithExt = scriptName;
			if (!StringUtils.endsWith(scriptName, Script.SCRIPT_FILEEXT)) {
				scriptNameWithExt = scriptName + "." + Script.SCRIPT_FILEEXT;
			}
			XExpression expr = (XExpression) repo.getModel(scriptNameWithExt);
			if(expr!=null) {
				ScriptEngine scriptEngine = ScriptActivator.scriptEngineTracker.getService();
				if(scriptEngine!=null) {
					Script script = scriptEngine.newScriptFromXExpression(expr);
					return script.execute();
				} else {
					throw new ScriptExecutionException("Script engine is not available.");
				}
			} else {
				throw new ScriptExecutionException("Script '" + scriptName + "' cannot be found.");
			}
		} else {
			throw new ScriptExecutionException("Model repository is not available.");
		}
	}
	
	/**
	 * Schedules a block of code for later execution.
	 * 
	 * @param instant the point in time when the code should be executed
	 * @param closure the code block to execute
	 * 
	 * @return a handle to the created timer, so that it can be canceled or rescheduled
	 * @throws ScriptExecutionException if an error occurs during the execution
	 */
	public static Timer createTimer(AbstractInstant instant, Procedure0 closure) {
		JobKey jobKey = new JobKey(instant.toString() + ": " + closure.toString());
        Trigger trigger = newTrigger().startAt(instant.toDate()).build();
		Timer timer = new TimerImpl(jobKey, trigger.getKey(), instant);
		try {
			JobDataMap dataMap = new JobDataMap();
			dataMap.put("procedure", closure);
			dataMap.put("timer", timer);
	        JobDetail job = newJob(TimerExecutionJob.class)
	            .withIdentity(jobKey)
	            .usingJobData(dataMap)
	            .build();	
	        TimerImpl.scheduler.scheduleJob(job, trigger);
			logger.debug("Scheduled code for execution at {}", instant.toString());
			return timer;
		} catch(SchedulerException e) {
			logger.error("Failed to schedule code for execution.", e);
			return null;
		}
	}
}
