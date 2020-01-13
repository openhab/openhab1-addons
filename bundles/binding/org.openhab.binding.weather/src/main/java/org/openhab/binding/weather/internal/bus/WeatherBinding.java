/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
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
     * Sets providers in WeatherContext and generates metadata from the weather
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
        if (config == null) {
            logger.warn("Unable to find any configuration settings for weather binding. Check openhab.cfg.");
            throw new ConfigurationException("weather",
                    "Unable to find any configuration settings for weather binding. Check openhab.cfg.");
        }

        context.getJobScheduler().stop();

        context.getConfig().parse(config);
        context.getConfig().dump();

        if (context.getConfig().isValid()) {
            context.getJobScheduler().restart();
        } else {
            logger.warn("Unable to restart weather job because weather configuration is not valid. Check openhab.cfg.");
            throw new ConfigurationException("weather",
                    "Unable to restart weather job because weather configuration is not valid. Check openhab.cfg.");
        }
    }

    protected void addBindingProvider(WeatherBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(WeatherBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * Restart scheduler if all binding changes.
     */
    @Override
    public void allBindingsChanged(BindingProvider provider) {
        if (!context.getConfig().finishedParsing()) {
            return;
        }

        if (context.getConfig().isValid()) {
            context.getJobScheduler().restart();
        } else {
            logger.warn(
                    "All bindings changed, but unable to restart weather job because weather configuration is not valid. Check openhab.cfg.");
        }
    }

    /**
     * Restart scheduler if some binding changes.
     */
    @Override
    public void bindingChanged(BindingProvider provider, String itemName) {
        if (!context.getConfig().finishedParsing()) {
            return;
        }

        if (context.getConfig().isValid()) {
            if (provider instanceof WeatherBindingProvider) {
                context.getJobScheduler().restart();
            }
        } else {
            logger.debug("Binding for item '{}' changed, but unable to restart weather job "
                    + " because weather configuration is not valid. Check openhab.cfg.", itemName);
        }
        super.bindingChanged(provider, itemName);
    }
}
