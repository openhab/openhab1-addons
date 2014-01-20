/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal;

import java.util.Dictionary;

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
	private static final long REFRESH_INTERVAL = 10000;

	private static final Logger logger = LoggerFactory.getLogger(ZWaveActiveBinding.class);
	private String port;
	private volatile ZWaveController zController;
	private volatile ZWaveConverterHandler converterHandler;

	private boolean isZwaveNetworkReady = false;
	
	// Configuration Service
	ZWaveConfiguration zConfigurationService;

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return REFRESH_INTERVAL;
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
		
		// loop all binding providers for the Z-wave binding.
		for (ZWaveBindingProvider provider : providers) {
			// loop all bound items for this provider
			for (String itemName : provider.getItemNames()) {
				converterHandler.executeRefresh(provider, itemName, false);
			}
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
		
		ZWaveBindingProvider zProvider = (ZWaveBindingProvider)provider;
		
		if (zProvider != null) {
			ZWaveBindingConfig bindingConfig = zProvider.getZwaveBindingConfig(itemName);
			
			if (bindingConfig != null && converterHandler != null) {
					converterHandler.executeRefresh(zProvider, itemName, true);
			}
		}
		
		super.bindingChanged(provider, itemName);
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
	 * {@inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config == null)
			return;
		
		// Check the serial port configuration value.
		// This value is mandatory.
		if (StringUtils.isNotBlank((String) config.get("port"))) {
			try {
				port = (String) config.get("port");
				logger.info("Update config, port = {}", port);
				this.setProperlyConfigured(true);
				this.deactivate();
				this.zController = new ZWaveController(port);
				this.converterHandler = new ZWaveConverterHandler(this.zController, this.eventPublisher);
				zController.initialize();
				zController.addEventListener(this);
				
				// The config service needs to know the controller...
				this.zConfigurationService = new ZWaveConfiguration(this.zController);
				zController.addEventListener(this.zConfigurationService);
				return;
			} catch (SerialInterfaceException ex) {
				this.setProperlyConfigured(false);
				throw new ConfigurationException("port", ex.getLocalizedMessage(), ex);
			}
		}
		this.setProperlyConfigured(false);
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

		logger.warn("Unknown event type {}", event.getClass().getName());
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
}
