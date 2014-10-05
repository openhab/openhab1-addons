/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera.internal;

import org.openhab.binding.vera.VeraBindingConstants;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This {@link Job} is responsible for re-subscribing 
 * to failed or broken subscriptions.
 *  
 * @author Matthew Bowman
 * @since 1.6.0
 */
public class VeraSubscriptionJob implements Job {
	
	private static final Logger logger = LoggerFactory.getLogger(VeraSubscriptionJob.class);

	/**
	 * Simply calls <code>subscribe()</code> on the {@link VeraBinding}
	 * from the {@link JobDataMap} supplied during execution. 
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobData = context.getMergedJobDataMap();
		VeraBinding binding = (VeraBinding) jobData.get(VeraBindingConstants.SUBSCRIPTION_JOB_DATA_KEY);
		logger.trace("[{}] execute()", binding.getBindingConfig().getItemName());
		binding.subscribe();
	}
	
}
