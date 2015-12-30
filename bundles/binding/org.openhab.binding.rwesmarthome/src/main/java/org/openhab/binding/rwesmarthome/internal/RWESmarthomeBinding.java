/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rwesmarthome.internal;

import java.util.Map;

import org.openhab.binding.rwesmarthome.RWESmarthomeBindingProvider;
import org.openhab.binding.rwesmarthome.internal.communicator.RWESmarthomeCommunicator;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * RWE Smarthome binding implementation.
 * 
 * @author ollie-dev
 * @since 1.8.0
 */
public class RWESmarthomeBinding extends AbstractActiveBinding<RWESmarthomeBindingProvider> {

	private static final Logger logger = LoggerFactory.getLogger(RWESmarthomeBinding.class);
	private RWESmarthomeCommunicator communicator = new RWESmarthomeCommunicator();
	private RWESmarthomeContext context = RWESmarthomeContext.getInstance();
	
	/** 
	 * the refresh interval which is used to poll values from the RWESmarthome
	 * server (optional, defaults to 2000ms)
	 */
	private long refreshInterval = 2000;
	
	/**
	 * stores the time when context.setBindingChanged to true
	 */
	private long lastBindingChangedTime = 0;
	
		
	/**
	 * {@inheritDoc}
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) throws ConfigurationException {
		logger.debug("activate is called.");
		
		context.setProviders(providers);
			
		String refreshIntervalString = (String) configuration.get("poll.interval");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}

		// read further config parameters here ...
		context.getConfig().parse(configuration);
		logger.debug(context.getConfig().toString());
		
		communicator.start();
		setProperlyConfigured(true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void modified(final Map<String, Object> configuration) throws ConfigurationException {
		logger.debug("modified called");
		
		if (configuration != null) {
			setProperlyConfigured(false);
			communicator.stop();
			
			context.getConfig().parse(configuration);
			logger.info(context.getConfig().toString());
			
			if (context.getConfig().isValid()) {
				communicator.start();
				setProperlyConfigured(true);
			}
		} else {
			logger.info("RWE Smarthome binding configuration is not present. Please check your configuration file or if not needed remove the RWE Smarthome binding addon.");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deactivate(final int reason) {
		logger.debug("Deactivate is called. Reason: {}", reason);
		
		communicator.stop();
		// TODO: further deallocation of unneeded resources
	}

	/**
	 * Saves the eventPublisher in the RweSmarthome context.
	 */
	@Override
	public void setEventPublisher(EventPublisher eventPublisher) {
		super.setEventPublisher(eventPublisher);
		context.setEventPublisher(eventPublisher);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "RWESmarthome Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.trace("execute() method is called!");

		// restart communicator (=rebuild RWE binding data), if there was no communication for a time (default: 5 min)
		long timeSinceLastEvent = (System.currentTimeMillis() - communicator.getLastEventTime()) / 1000;
		if (timeSinceLastEvent > context.getConfig().getAliveInterval()) {
			logger.info("No event since {} seconds, refreshing RweSmarthome server connections", timeSinceLastEvent);
			communicator.stop();
			communicator.start();
		}
		
		// if bindingChanged is set, save the current time
		if(context.getBindingChanged()) {
			lastBindingChangedTime = System.currentTimeMillis();
			context.setBindingChanged(false);
		}

		if(lastBindingChangedTime > 0) {
			long timeSinceBindingChanged = (System.currentTimeMillis() - lastBindingChangedTime) / 1000;
			if(timeSinceBindingChanged > context.getConfig().getBindingChangedInterval()) {
				logger.info("Binding changed - reload RWE Smarthome data");
				communicator.loadDeviceStates();
				lastBindingChangedTime = 0;
			}
		}
		
		communicator.poll();

		if(logger.isTraceEnabled()) {
		    for (String key : context.getIgnoreEventList().keySet()) {
		    	logger.trace("Ignorelist: {} {} (age: {} ms)", key, context.getIgnoreEventList().get(key), System.currentTimeMillis()-context.getIgnoreEventList().get(key));
		    }
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
		communicator.sendCommand(itemName, command);
		context.getIgnoreEventList().put(itemName + command.toString(), System.currentTimeMillis());
		logger.debug("Added event (item='{}', command='{}') to the ignore event list", itemName, command.toString());
	}
}
