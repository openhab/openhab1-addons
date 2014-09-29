/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.script.internal.actions;

import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a Quartz {@link Job} which executes the code of a closure that is passed
 * to the createTimer() extension method.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 *
 */
public class TimerExecutionJob implements Job {

	static final private Logger logger = LoggerFactory.getLogger(TimerExecutionJob.class);
	
	private Procedure0 procedure;
	private TimerImpl timer;

	/**
	 * Runs the configured closure of this job
	 * 
	 * @param context the execution context of the job
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.debug("Executing timer '{}'", context.getJobDetail().getKey().toString());
		procedure.apply();
		timer.setTerminated(true);
	}

	/**
	 * Sets the closure for this job
	 * 
	 * @param procedure a closure without parameters
	 */
	public void setProcedure(Procedure0 procedure) {
		this.procedure = procedure;
	}

	/** 
	 * Sets the {@link TimerImpl} instance that corresponds to this job.
	 * 
	 * @param timer the associated timer instance
	 */
	public void setTimer(TimerImpl timer) {
		this.timer = timer;
	}
}
