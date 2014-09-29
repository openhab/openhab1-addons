/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xpl.internal;

import java.util.List;

import org.openhab.binding.xpl.xPLBindingConfig;
import org.openhab.binding.xpl.xPLBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.io.transport.xpl.XplTransportService;
import org.cdp1802.xpl.xPL_MessageI;
import org.cdp1802.xpl.xPL_MessageListenerI;

/**
 * xPL binding for openHAB
 * 
 * @author clinique
 * @since 1.5.0
 */
public class xPLBinding extends AbstractBinding<xPLBindingProvider> implements xPL_MessageListenerI {

	//private static final Logger logger = LoggerFactory.getLogger(xPLBinding.class);	
	private XplTransportService xplTransportService;
	private EventPublisher eventPublisher;

	public xPLBinding() {
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
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
				config.Message.setSource(xplTransportService.getSourceIdentifier());
			
			config.Message.setNamedValue(config.NamedParameter, command.toString().toLowerCase());
			xplTransportService.sendMessage(config.Message);
		}
	}

	@Override	 
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
	
	/**
	 * Setter for Declarative Services. Adds the XplTransportService instance.
	 * 
	 * @param xplTransportService
	 *            Service.
	 */
	public void setXplTransportService(XplTransportService xplTransportService) {
		this.xplTransportService = xplTransportService;
		this.xplTransportService.addMessageListener(this);
	}

	/**
	 * Unsetter for Declarative Services.
	 * 
	 * @param xplTransportService
	 *            Service to remove.
	 */
	public void unsetXplTransportService(XplTransportService xplTransportService) {
		this.xplTransportService.removeMessageListener(this);
		this.xplTransportService = null;
	}

}
