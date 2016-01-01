/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mystromecopower.internal.util;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.mystromecopower.internal.api.IMystromClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of Quartz {@link Job}-Interface. It changes the state of a
 * mystrom device.
 * 
 * @author Christophe Jordens
 * @since 1.8.0-SNAPSHOT
 */
public class ChangeStateJob implements Job {

	private static final Logger logger = LoggerFactory
			.getLogger(ChangeStateJob.class);

	public static final String JOB_DATA_CONTENT_KEY = "deviceId";
	public static IMystromClient MystromClient;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		String content = (String) context.getJobDetail().getJobDataMap()
				.get(JOB_DATA_CONTENT_KEY);

		if (StringUtils.isNotBlank(content)) {
			String deviceId = content.split(";")[0];
			String state = content.split(";")[1];

			boolean setToOn = state == "on";

			logger.debug(
					"About to execute ChangeStateJob with arguments {} {}",
					deviceId, state);
			try {
				MystromClient.ChangeState(deviceId, setToOn);
			} catch (Exception e) {
				throw new JobExecutionException(
						"Executing command '"
								+ deviceId
								+ " "
								+ state
								+ "' throws an Exception. Job will be refired immediately.",
						e, true);
			}
		}
	}
}
