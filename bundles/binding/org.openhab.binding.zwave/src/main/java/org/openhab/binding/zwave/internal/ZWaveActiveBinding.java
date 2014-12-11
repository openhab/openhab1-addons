/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.zwave.ZWaveBindingConfig;
import org.openhab.binding.zwave.ZWaveBindingProvider;
import org.openhab.binding.zwave.internal.config.ZWaveConfiguration;
import org.openhab.binding.zwave.internal.converter.ZWaveConverterHandler;
import org.openhab.binding.zwave.internal.protocol.SerialInterfaceException;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEventListener;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveInitializationCompletedEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveTransactionCompletedEvent;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZWaveActiveBinding Class. Polls Z-Wave nodes frequently,
 * responds to item commands, and also handles events coming 
 * from the Z-Wave controller.
 * @author Victor Belov
 * @author Brian Crosby
 * @author Jan-Willem Spuij
 * @author Chris Jackson
 * @since 1.3.0
 */
public class ZWaveActiveBinding extends AbstractActiveBinding<ZWaveBindingProvider> implements ManagedService, ZWaveEventListener {
	/**
	 * The refresh interval which is used to poll values from the ZWave binding. 
	 */
	private long refreshInterval = 5000;
	
	private int pollingQueue = 1;

	private static final Logger logger = LoggerFactory.getLogger(ZWaveActiveBinding.class);
	private String port;
	private boolean isSUC = false;
	private boolean softReset = false;
	private Integer healtime = null;
	private Integer timeout = null;
	private volatile ZWaveController zController;
	private volatile ZWaveConverterHandler converterHandler;

	private boolean isZwaveNetworkReady = false;
	
	private Iterator<ZWavePollItem> pollingIterator = null;
	private List<ZWavePollItem> pollingList = new ArrayList<ZWavePollItem>();
	
	// Configuration Service
	ZWaveConfiguration zConfigurationService;
	
	// Network monitoring class
	ZWaveNetworkMonitor networkMonitor;

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getName() {
		return "ZWave Refresh Service";
	}

	/**
	 * Working method that executes refreshing of the bound items. The method is executed
	 * at every refresh interval. The nodes are polled only every 6 refreshes.
	 */
	@Override
	protected void execute() {
		
		if(!isZwaveNetworkReady){
			logger.debug("Zwave Network isn't ready yet!");
			if (this.zController != null)
				this.zController.checkForDeadOrSleepingNodes();
			return;
		}
		
		// Call the network monitor
		networkMonitor.execute();

		// If we're not currently in a poll cycle, restart the polling table
		if(pollingIterator == null) {
			pollingIterator = pollingList.iterator();
		}
		
		// Loop through the polling list. We only allow a certain number of messages
		// into the send queue at a time to avoid congestion within the system.
		// Basically, we don't want the polling to slow down 'important' stuff.
		// The queue ensures all nodes get a chance - if we always started at the top
		// then the last items might never get polled.
		while(pollingIterator.hasNext()) {
			if(zController.getSendQueueLength() >= pollingQueue) {
				logger.trace("Polling queue full!");
				break;
			}
			ZWavePollItem poll = pollingIterator.next();
			converterHandler.executeRefresh(poll.provider, poll.item, false);
		}
		if(pollingIterator.hasNext() == false) {
			pollingIterator = null;
		}
	}
	
	/**
	 * Called, if a single binding has changed. The given item could have been
	 * added or removed. We refresh the binding in case it's in the done stage.
	 * 
	 * @param provider the binding provider where the binding has changed
	 * @param itemName the item name for which the binding has changed
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		logger.trace("bindingChanged {}", itemName);		
		
		ZWaveBindingProvider zProvider = (ZWaveBindingProvider)provider;
		
		if (zProvider != null) {
			ZWaveBindingConfig bindingConfig = zProvider.getZwaveBindingConfig(itemName);
			
			if (bindingConfig != null && converterHandler != null) {
				converterHandler.executeRefresh(zProvider, itemName, true);
			}
		}

		// Bindings have changed - rebuild the polling table
		rebuildPollingTable();
		
		super.bindingChanged(provider, itemName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void allBindingsChanged(BindingProvider provider) {
		logger.trace("allBindingsChanged");		
		super.allBindingsChanged(provider);

		// Bindings have changed - rebuild the polling table
		rebuildPollingTable();
	}

	
	/**
	 * This method rebuilds the polling table. The polling table is a list of items that have
	 * polling enabled (ie a refresh interval is set). This list is then checked periodically
	 * and any item that has passed its polling interval will be polled.
	 */
	private void rebuildPollingTable() {
		// Rebuild the polling table
		pollingList.clear();

		// Loop all binding providers for the Z-wave binding.
		for (ZWaveBindingProvider eachProvider : providers) {
			// loop all bound items for this provider
			for (String name : eachProvider.getItemNames()) {
				logger.trace("Polling list: Checking {} == {}", name, converterHandler.getRefreshInterval(eachProvider, name));

				// This binding is configured to poll - add it to the list
				if (converterHandler.getRefreshInterval(eachProvider, name) > 0) {
					ZWavePollItem item = new ZWavePollItem();
					item.item = name;
					item.provider = eachProvider;
					pollingList.add(item);
					logger.trace("Polling list added {}", name);
				}
			}
		}
		pollingIterator = null;
	}
	
	/**
	 * Handles a command update by sending the appropriate Z-Wave instructions
	 * to the controller.
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		boolean handled = false;
		
		// if we are not yet initialized, don't waste time and return
		if((this.isProperlyConfigured() == false) | (isZwaveNetworkReady == false)) {
			logger.debug("internalReceiveCommand Called, But Not Properly Configure yet or Zwave Network Isn't Ready, returning.");
			return;
		}

		logger.trace("internalReceiveCommand(itemname = {}, Command = {})", itemName, command.toString());
		for (ZWaveBindingProvider provider : providers) {

			if (!provider.providesBindingFor(itemName))
				continue;
			
			converterHandler.receiveCommand(provider, itemName, command);
			handled = true;
		}

		if (!handled)
			logger.warn("No converter found for item = {}, command = {}, ignoring.", itemName, command.toString());
	}
	
	/**
	 * Activates the binding. Actually does nothing, because on activation
	 * OpenHAB always calls updated to indicate that the config is updated.
	 * Activation is done there.
	 */
	@Override
	public void activate() {
		
	}
	
	/**
	 * Deactivates the binding. The Controller is stopped and the serial interface
	 * is closed as well.
	 */
	@Override
	public void deactivate() {
		isZwaveNetworkReady = false;
		if (this.converterHandler != null) {
			this.converterHandler = null;
		}

		if (this.zConfigurationService != null) {
			this.zController.removeEventListener(this.zConfigurationService);
			this.zConfigurationService = null;
		}

		ZWaveController controller = this.zController;
		if (controller != null) {
			this.zController = null;
			controller.close();
			controller.removeEventListener(this);
		}
	}
	
	/**
	 * Initialises the binding. This is called after the 'updated' method
	 * has been called and all configuration has been passed.
	 * @throws ConfigurationException 
	 */
	private void initialise() throws ConfigurationException {
		try {
			this.setProperlyConfigured(true);
			this.deactivate();
			this.zController = new ZWaveController(isSUC, port, timeout);
			this.converterHandler = new ZWaveConverterHandler(this.zController, this.eventPublisher);
			zController.initialize();
			zController.addEventListener(this);

			// The network monitor service needs to know the controller...
			this.networkMonitor = new ZWaveNetworkMonitor(this.zController);
			if(healtime != null) {
				this.networkMonitor.setHealTime(healtime);
			}
			if(softReset != false) {
				this.networkMonitor.resetOnError(softReset);
			}

			// The config service needs to know the controller and the network monitor...
			this.zConfigurationService = new ZWaveConfiguration(this.zController, this.networkMonitor);
			zController.addEventListener(this.zConfigurationService);
			return;
		} catch (SerialInterfaceException ex) {
			this.setProperlyConfigured(false);
			throw new ConfigurationException("port", ex.getLocalizedMessage(), ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config == null)
			return;
		
		// Check the serial port configuration value.
		// This value is mandatory.
		if (StringUtils.isNotBlank((String) config.get("port"))) {
			port = (String) config.get("port");
			logger.info("Update config, port = {}", port);
		}
		if (StringUtils.isNotBlank((String) config.get("healtime"))) {
			try {
				healtime = Integer.parseInt((String) config.get("healtime"));
				logger.info("Update config, healtime = {}", healtime);
			} catch (NumberFormatException e) {
				healtime = null;
				logger.error("Error parsing 'healtime'. This must be a single number to set the hour to perform the heal.");
			}
		}
		if (StringUtils.isNotBlank((String) config.get("refreshInterval"))) {
			try {
				refreshInterval = Integer.parseInt((String) config.get("refreshInterval"));
				logger.info("Update config, refreshInterval = {}", refreshInterval);
			} catch (NumberFormatException e) {
				refreshInterval = 10000;
				logger.error("Error parsing 'refreshInterval'. This must be a single number time in milliseconds.");
			}
		}
		if (StringUtils.isNotBlank((String) config.get("pollingQueue"))) {
			try {
				pollingQueue = Integer.parseInt((String) config.get("pollingQueue"));
				logger.info("Update config, pollingQueue = {}", pollingQueue);
			} catch (NumberFormatException e) {
				pollingQueue = 2;
				logger.error("Error parsing 'pollingQueue'. This must be a single number time in milliseconds.");
			}
		}
		if (StringUtils.isNotBlank((String) config.get("timeout"))) {
			try {
				timeout = Integer.parseInt((String) config.get("timeout"));
				logger.info("Update config, timeout = {}", timeout);
			} catch (NumberFormatException e) {
				timeout = null;
				logger.error("Error parsing 'timeout'. This must be an Integer.");
			}
		}
		if (StringUtils.isNotBlank((String) config.get("setSUC"))) {
			try {
				isSUC = Boolean.parseBoolean((String) config.get("setSUC"));
				logger.info("Update config, setSUC = {}", isSUC);
			} catch (NumberFormatException e) {
				isSUC = false;
				logger.error("Error parsing 'setSUC'. This must be boolean.");
			}
		}
		if (StringUtils.isNotBlank((String) config.get("softReset"))) {
			try {
				softReset = Boolean.parseBoolean((String) config.get("softReset"));
				logger.info("Update config, softReset = {}", softReset);
			} catch (NumberFormatException e) {
				softReset = false;
				logger.error("Error parsing 'softReset'. This must be boolean.");
			}
		}

		// Now that we've read ALL the configuration, initialise the binding.
		initialise();
	}

	/**
	 * Returns the port value.
	 * @return
	 */
	public String getPort() {
		return port;
	}

	/**
	 * Event handler method for incoming Z-Wave events.
	 * @param event the incoming Z-Wave event.
	 */
	@Override
	public void ZWaveIncomingEvent(ZWaveEvent event) {
		
		// if we are not yet initialized, don't waste time and return
		if (!this.isProperlyConfigured())
			return;
		
		if (!isZwaveNetworkReady) {
			if (event instanceof ZWaveInitializationCompletedEvent) {
				logger.debug("ZWaveIncomingEvent Called, Network Event, Init Done. Setting ZWave Network Ready.");
				isZwaveNetworkReady = true;
				
				// Initialise the polling table
				rebuildPollingTable();

				return;
			}		
		}
		
		logger.debug("ZwaveIncomingEvent");
		
		// ignore transaction completed events.
		if (event instanceof ZWaveTransactionCompletedEvent)
			return;
		
		// handle command class value events.
		if (event instanceof ZWaveCommandClassValueEvent) {
			handleZWaveCommandClassValueEvent((ZWaveCommandClassValueEvent)event);
			return;
		}
	}

	/**
	 * Handle an incoming Command class value event
	 * @param event the incoming Z-Wave event.
	 */
	private void handleZWaveCommandClassValueEvent(
		ZWaveCommandClassValueEvent event) {
		boolean handled = false;

		logger.debug("Got a value event from Z-Wave network for nodeId = {}, endpoint = {}, command class = {}, value = {}", 
				new Object[] { event.getNodeId(), event.getEndpoint(), event.getCommandClass().getLabel(), event.getValue() } );

		for (ZWaveBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				ZWaveBindingConfig bindingConfig = provider.getZwaveBindingConfig(itemName);
				
				if (bindingConfig.getNodeId() != event.getNodeId() || bindingConfig.getEndpoint() != event.getEndpoint())
					continue;
				
				converterHandler.handleEvent(provider, itemName, event);
				handled = true;
			}
		}
		
		if (!handled)
			logger.warn("No item bound for event from nodeId = {}, endpoint = {}, command class = {}, value = {}, ignoring.", 
					new Object[] { event.getNodeId(), event.getEndpoint(), event.getCommandClass().getLabel(), event.getValue() } );
	}
	
	class ZWavePollItem {
		ZWaveBindingProvider provider;
		String item;
	}
}
