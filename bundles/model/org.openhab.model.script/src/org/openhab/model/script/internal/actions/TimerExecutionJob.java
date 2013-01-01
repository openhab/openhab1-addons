/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
