/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.bus;

import java.util.Dictionary;

import org.openhab.binding.weather.WeatherBindingProvider;
import org.openhab.binding.weather.internal.common.WeatherContext;
import org.openhab.binding.weather.internal.metadata.MetadataHandler;
import org.openhab.binding.weather.internal.model.Weather;
import org.openhab.binding.weather.internal.parser.CommonIdHandler;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.events.EventPublisher;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Weather binding implementation.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class WeatherBinding extends AbstractBinding<WeatherBindingProvider> implements ManagedService {
	private static final Logger logger = LoggerFactory.getLogger(WeatherBinding.class);

	private static WeatherContext context = WeatherContext.getInstance();

	/**
	 * Set EventPublisher in WeatherContext.
	 */
	@Override
	public void setEventPublisher(EventPublisher eventPublisher) {
		super.setEventPublisher(eventPublisher);
		context.setEventPublisher(eventPublisher);
	}

	/**
	 * Set providers in WeatherContext and generates metadata from the weater
	 * model annotations.
	 */
	@Override
	public void activate() {
		context.setProviders(providers);
		try {
			MetadataHandler.getInstance().generate(Weather.class);
			CommonIdHandler.getInstance().loadMapping();
		} catch (Exception ex) {
			logger.error("Error activating WeatherBinding: {}", ex.getMessage(), ex);
		}
	}

	/**
	 * Stops all Weather jobs.
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
			context.getConfig().dump();

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
			context.getJobScheduler().restart();
		}
	}

	/**
	 * Restart scheduler if some binding changes.
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		if (context.getConfig().isValid()) {
			if (provider instanceof WeatherBindingProvider) {
				context.getJobScheduler().restart();
			}
		}
		super.bindingChanged(provider, itemName);
	}
}
