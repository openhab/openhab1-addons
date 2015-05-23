/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.jsr223.internal.actions;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a Quartz {@link Job} which executes the code of a closure that is passed to the createTimer() extension
 * method.
 * 
 * @author Kai Kreuzer
 * @author Simon Merschjohann
 * @since 1.7.0
 * 
 */
public class TimerExecutionJob implements Job {

	static final private Logger logger = LoggerFactory.getLogger(TimerExecutionJob.class);

	private Runnable procedure = null;
	private Timer timer;

	/**
	 * Runs the configured closure of this job
	 * 
	 * @param context
	 *            the execution context of the job
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.debug("Executing timer '{}'", context.getJobDetail().getKey().toString());
		if (procedure != null) {
			procedure.run();
		}
		timer.setTerminated(true);
	}

	/**
	 * Sets the 0-parameter closure for this job
	 * 
	 * @param procedure
	 *            a closure without parameters
	 */
	public void setProcedure(Runnable procedure) {
		this.procedure = procedure;
	}

	/**
	 * Sets the {@link TimerImpl} instance that corresponds to this job.
	 * 
	 * @param timer
	 *            the associated timer instance
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
}
