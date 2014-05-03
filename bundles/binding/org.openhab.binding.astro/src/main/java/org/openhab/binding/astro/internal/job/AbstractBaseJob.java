/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.job;

import org.openhab.binding.astro.AstroBindingProvider;
import org.openhab.binding.astro.internal.common.AstroContext;
import org.openhab.binding.astro.internal.common.AstroType;
import org.openhab.binding.astro.internal.config.AstroBindingConfig;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.State;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Baseclass for all jobs with common methods.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public abstract class AbstractBaseJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(AbstractBaseJob.class);

	@Override
	public void execute(JobExecutionContext jobContext) throws JobExecutionException {
		logger.debug("Starting Astro " + this.getClass().getSimpleName());
		executeJob();
	}

	/**
	 * Publishes the State to all AstroType bindings. For sunrise, noon and
	 * sunset a OFF is published immediately after ON.
	 */
	protected void publishState(AstroType publishType, State state) {
		AstroContext context = AstroContext.getInstance();
		for (AstroBindingProvider provider : context.getProviders()) {
			for (String itemName : provider.getItemNames()) {
				AstroBindingConfig config = provider.getBindingFor(itemName);
				if (config != null && config.getType() == publishType) {
					context.getEventPublisher().postUpdate(itemName, state);
					if (state.getClass() == OnOffType.class) {
						context.getEventPublisher().postUpdate(itemName, OnOffType.OFF);
					}
				}
			}
		}
	}

	/**
	 * Method to override by the different jobs to be executed.
	 */
	protected abstract void executeJob();
}
