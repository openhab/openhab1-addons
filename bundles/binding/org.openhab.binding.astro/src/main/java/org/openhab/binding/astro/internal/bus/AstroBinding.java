/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.bus;

import java.util.Dictionary;

import org.openhab.binding.astro.AstroBindingProvider;
import org.openhab.binding.astro.internal.common.AstroContext;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Astro binding implementation.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class AstroBinding extends AbstractBinding<AstroBindingProvider> implements ManagedService {
	private static final Logger logger = LoggerFactory.getLogger(AstroBinding.class);

	private static AstroContext context = AstroContext.getInstance();

	/**
	 * Set EventPublisher in AstroContext.
	 */
	@Override
	public void setEventPublisher(EventPublisher eventPublisher) {
		super.setEventPublisher(eventPublisher);
		context.setEventPublisher(eventPublisher);
	}

	/**
	 * Set providers in AstroContext.
	 */
	@Override
	public void activate() {
		context.setProviders(providers);
	}

	/**
	 * Stops all Astro jobs.
	 */
	@Override
	public void deactivate() {
		context.getJobScheduler().stop();
	}

	/**
	 * Restart scheduler if config changes.
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			context.getJobScheduler().stop();

			context.getConfig().parse(config);
			logger.info(context.getConfig().toString());

			if (context.getConfig().isValid()) {
				context.getJobScheduler().restart();
			}
		}
	}

	/**
	 * Restart scheduler if all binding changes.
	 */
	@Override
	public void allBindingsChanged(BindingProvider provider) {
		if (context.getConfig().isValid()) {
			logger.debug("Astro binding changed, (re)starting Astro jobs");
			context.getJobScheduler().restart();
		}
	}

	/**
	 * Restart scheduler if some binding changes.
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		if (context.getConfig().isValid()) {
			if (provider instanceof AstroBindingProvider) {
				logger.debug("Astro binding item {} changed, (re)starting Astro jobs", itemName);
				context.getJobScheduler().restart();
			}
		}
		super.bindingChanged(provider, itemName);
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.warn("Received command for readonly item {}, republishing state", itemName);
		PlanetPublisher.getInstance().republishItem(itemName);
	}

	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		logger.warn("Received new state for readonly item {}, republishing state", itemName);
		PlanetPublisher.getInstance().republishItem(itemName);
	}
}
