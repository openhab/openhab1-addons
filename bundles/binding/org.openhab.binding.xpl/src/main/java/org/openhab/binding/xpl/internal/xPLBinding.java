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
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.*;
import org.openhab.core.library.types.*;
import org.openhab.core.types.Command;
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
public class xPLBinding extends AbstractBinding<xPLBindingProvider> implements ManagedService, xPL_MessageListenerI {

	private static final Logger logger = LoggerFactory.getLogger(xPLBinding.class);
	private static final String vendor = "clinique";
	private static final String device = "openhab";
	private static xPL_Manager theManager = null;
	private static xPL_IdentifierI sourceIdentifier = null;
	public xPL_DeviceI loggerDevice = null;
	
	private EventPublisher eventPublisher;

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
		logger.info("xPL Binding source address set to " + sourceIdentifier.toString());
	}
	
	protected String getInstance() {
		if (sourceIdentifier == null) {
			setInstance("openhab");
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
			logger.info("xPL Binding manager started");

		} catch (xPL_MediaHandlerException startError) {
			logger.error("xPL Binding : Unable to start xPL Manager" + startError.getMessage());
		}
	}

	public void deactivate() {
		theManager.removeMessageListener(this);
		theManager.stopAllMediaHandlers();
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
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		logger.info("Entering in configuration section");
		if (config != null) {
			String instancename = (String) config.get("instance");
			logger.info("Received new config : " + instancename);
			setInstance(instancename);
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
