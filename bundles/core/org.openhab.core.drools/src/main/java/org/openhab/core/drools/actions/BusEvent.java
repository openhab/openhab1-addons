/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.drools.actions;

import org.openhab.core.drools.internal.RulesActivator;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The static methods of this class are imported as functions into the rules.
 * This gives direct write access to the openHAB event bus from within the rules.
 * Items should not be updated directly (through modify clauses), but updates should
 * be sent to the bus, so that all interested bundles are notified.
 * 
 * @author Kai Kreuzer
 * @since 0.7.0
 *
 */
public class BusEvent {

	static private final Logger logger = LoggerFactory.getLogger(BusEvent.class);
	
	static public void sendCommand(String itemName, String commandString) {
		ItemRegistry registry = (ItemRegistry) RulesActivator.itemRegistryTracker.getService();
		EventPublisher publisher = (EventPublisher) RulesActivator.eventPublisherTracker.getService();
		if(publisher!=null && registry!=null) {
			try {
				Item item = registry.getItem(itemName);
				Command command = TypeParser.parseCommand(item.getAcceptedCommandTypes(), commandString);
				publisher.sendCommand(itemName, command);
			} catch (ItemNotFoundException e) {
				logger.warn("Item '" + itemName + "' does not exist.");
			}
		}
	}

	static public void postUpdate(String itemName, String stateString) {
		ItemRegistry registry = (ItemRegistry) RulesActivator.itemRegistryTracker.getService();
		EventPublisher publisher = (EventPublisher) RulesActivator.eventPublisherTracker.getService();
		if(publisher!=null && registry!=null) {
			try {
				Item item = registry.getItem(itemName);
				State state = TypeParser.parseState(item.getAcceptedDataTypes(), stateString);
				publisher.postUpdate(itemName, state);
			} catch (ItemNotFoundException e) {
				logger.warn("Item '" + itemName + "' does not exist.");
			}
		}
	}

}
