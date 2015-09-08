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
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author ollie-dev
 * @since 1.8.0
 */
public class RWESmarthomeBinding extends AbstractActiveBinding<RWESmarthomeBindingProvider> {

	private static final Logger logger = LoggerFactory.getLogger(RWESmarthomeBinding.class);
	private RWESmarthomeCommunicator communicator = new RWESmarthomeCommunicator();
	private RWESmarthomeContext context = RWESmarthomeContext.getInstance();
	
	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate()
	 * method and must not be accessed anymore once the deactivate() method was called or before activate()
	 * was called.
	 */
	private BundleContext bundleContext;

	
	/** 
	 * the refresh interval which is used to poll values from the RWESmarthome
	 * server (optional, defaults to 2000ms)
	 */
	private long refreshInterval = 2000;
	
	/**
	 * stores the time when context.setBindingChanged to true
	 */
	private long lastBindingChangedTime = 0;
	
	
	public RWESmarthomeBinding() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				communicator.stop();
			}
		});
	}
		
	
	/**
	 * Called by the SCR to activate the component with its configuration read from CAS
	 * 
	 * @param bundleContext BundleContext of the Bundle that defines this component
	 * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
	 * @throws ConfigurationException 
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) throws ConfigurationException {
		logger.debug("activate is called.");
		
		this.bundleContext = bundleContext;
		context.setProviders(providers);

		// the configuration is guaranteed not to be null, because the component definition has the
		// configuration-policy set to require. If set to 'optional' then the configuration may be null
		
			
		// to override the default refresh interval one has to add a 
		// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}

		// read further config parameters here ...
		context.getConfig().parse(configuration);
		logger.info(context.getConfig().toString());
		
		communicator.start();
		setProperlyConfigured(true);
	}
	
	/**
	 * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
	 * @param configuration Updated configuration properties
	 * @throws ConfigurationException 
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
//				updateAllItems();
			}
		} else {
			logger.info("RWE Smarthome binding configuration is not present. Please check your configuration file or if not needed remove the RWE Smarthome binding addon.");
		}
	}
	
	/**
	 * Called by the SCR to deactivate the component when either the configuration is removed or
	 * mandatory references are no longer satisfied or the component has simply been stopped.
	 * @param reason Reason code for the deactivation:<br>
	 * <ul>
	 * <li> 0 – Unspecified
     * <li> 1 – The component was disabled
     * <li> 2 – A reference became unsatisfied
     * <li> 3 – A configuration was changed
     * <li> 4 – A configuration was deleted
     * <li> 5 – The component was disposed
     * <li> 6 – The bundle was stopped
     * </ul>
	 */
	public void deactivate(final int reason) {
		logger.debug("Deactivate is called. Reason: {}", reason);
		
		this.bundleContext = null;
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
		
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
		logger.debug("execute() method is called!");

		// restart communicator (=rebuild RWE binding data), if there was no communication for a time (default: 5 min)
		long timeSinceLastEvent = (System.currentTimeMillis() - communicator.getLastEventTime()) / 1000;
		if (timeSinceLastEvent > context.getConfig().getAliveInterval()) {
			logger.info("No event since {} seconds, refreshing RweSmarthome server connections", timeSinceLastEvent);
			communicator.stop();
			communicator.start();
//			updateAllItems();
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
		logger.debug("Ignorelist: " + StringUtils.join(context.getIgnoreEventList(), "|"));
//		List<LogicalDevice> changedDevices = communicator.poll();
//		if(changedDevices != null) {
//			updateItems(changedDevices);
//		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
		communicator.sendCommand(itemName, command);
		context.getIgnoreEventList().add(itemName + command.toString());
		logger.debug("Added event (item='{}', command='{}') to the ignore event list", itemName, command.toString());
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);
	}


}
