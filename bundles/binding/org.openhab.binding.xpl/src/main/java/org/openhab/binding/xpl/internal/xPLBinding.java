/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xpl.internal;

import java.util.Dictionary;
import java.util.List;

import org.openhab.binding.xpl.xPLBindingProvider;

import org.cdp1802.xpl.xPL_IdentifierI;
import org.cdp1802.xpl.xPL_Manager;
import org.cdp1802.xpl.xPL_MediaHandlerException;
import org.cdp1802.xpl.xPL_MessageI;
import org.cdp1802.xpl.xPL_MessageListenerI;
import org.cdp1802.xpl.device.xPL_DeviceI;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.*;
import org.openhab.core.library.types.*;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openhab.binding.xpl.xPLBindingConfig;

/**
 * xPL binding for openHAB
 * 
 * @author clinique
 * @since 1.5.0
 */
public class xPLBinding extends AbstractActiveBinding<xPLBindingProvider> implements ManagedService, xPL_MessageListenerI {

	private static final Logger logger = LoggerFactory.getLogger(xPLBinding.class);
	private static final String vendor = "clinique";
	private static final String device = "openhab";
	private static xPL_Manager theManager = null;
	private xPL_IdentifierI sourceIdentifier = null;
	public xPL_DeviceI loggerDevice = null;
	
	private EventPublisher eventPublisher;
		
	/**
	 * the refresh interval which is used to poll values 
	 * (optional, defaults to 60000ms)
	 * Not currently used, maybe later ?
	 */
	private long refreshInterval = 60000;

	public xPLBinding() {
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}

	protected void setInstance(String instance) {
		sourceIdentifier = xPL_Manager.getManager().getIdentifierManager()
				.parseNamedIdentifier(vendor + "-" + device + "." + instance);
		logger.debug("xPL Binding source address set to " + sourceIdentifier.toString());
	}

	protected String getInstance() {
		if (sourceIdentifier == null) {
			setInstance("bulk");
		}
		return sourceIdentifier.getInstanceID();
	}

	public void activate() {
		super.activate();
		try {
			theManager = xPL_Manager.getManager();
			theManager.createAndStartNetworkHandler();

			loggerDevice = theManager.getDeviceManager().createDevice(vendor, device, getInstance());
						
			// Enable the device and start logging
			loggerDevice.setEnabled(true);
			theManager.addMessageListener(this);
			logger.debug("xPL Binding manager started");

		} catch (xPL_MediaHandlerException startError) {
			logger.error("xPL Binding : Unable to start xPL Manager" + startError.getMessage());
		}
		setProperlyConfigured(true);
	}

	public void deactivate() {
		theManager.removeMessageListener(this);
		theManager.stopAllMediaHandlers();
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "xPL Binding Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		// logger.debug("execute() method is called!");
	}

	/**
	 * Sends an xPL message upon command received by an Item
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		for (xPLBindingProvider provider : providers) {
			xPLBindingConfig config = provider.getConfig(itemName);
			if ((config == null) || (config.NamedParameter == null)) continue;

			if (config.Message.getSource() == null) 
				config.Message.setSource(sourceIdentifier);
			
			config.Message.setNamedValue(config.NamedParameter, command.toString().toLowerCase());
			theManager.sendMessage(config.Message);
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			logger.debug("xPL Binding : Updating config");

			setInstance((String) config.get("instance"));

			// if (StringUtils.isBlank(getInstance())) {
			// throw new ConfigurationException("xPL",
			// "Parameters xPL:instance is mandatory and must be configured. Please check your openhab.cfg!");
			// }

			setProperlyConfigured(true);
		}
	}

	// xPL Part of the binding
	public void handleXPLMessage(xPL_MessageI theMessage) {
		
		for (xPLBindingProvider provider : providers) {
			List<String> matchingItems = provider.hasMessage(theMessage);
			for (String itemName : matchingItems) {
				xPLBindingConfig config = provider.getConfig(itemName);
				if (config == null) continue;
				
				String current = theMessage.getNamedValue(config.NamedParameter);
			
				Item item = provider.getItem(itemName);
				if (item != null) {
					if (item instanceof SwitchItem) {
					   OnOffType status = ( current.equalsIgnoreCase("on") || current.equalsIgnoreCase("true") ||
							   				current.equalsIgnoreCase("1")	|| current.equalsIgnoreCase("open") || 
							   				current.equalsIgnoreCase("high")) ? OnOffType.ON : OnOffType.OFF;
					   synchronized (item) {
						 if (!item.getState().equals(status)) {
							 eventPublisher.postUpdate(itemName, status);
							 ((SwitchItem) item).setState(status);
						 }
					   }						
					} else 
					if (item instanceof NumberItem) {
						DecimalType value = new DecimalType(current);
						synchronized (item) {
							if (!item.getState().equals(value)) {								
								eventPublisher.postUpdate(itemName, value);
								((NumberItem) item).setState(value);
							}
						}
					}
					if (item instanceof StringItem) {
						StringType value = new StringType(current);
						synchronized (item) {
							if (!item.getState().equals(value)) {								
								eventPublisher.postUpdate(itemName, value);
								((StringItem) item).setState(value);
							}
						}						
					}
				}
			}										
		}

	}
}
