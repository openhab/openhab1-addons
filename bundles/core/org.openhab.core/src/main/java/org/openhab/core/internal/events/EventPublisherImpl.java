/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.internal.events;

import static org.openhab.core.events.EventConstants.TOPIC_PREFIX;
import static org.openhab.core.events.EventConstants.TOPIC_SEPERATOR;

import java.util.Dictionary;
import java.util.Hashtable;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.EventType;
import org.openhab.core.types.State;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the main implementation of the {@link EventPublisher} interface.
 * Through it, openHAB events can be sent to the OSGi EventAdmin service
 * in order to broadcast them.
 * 
 * @author Kai Kreuzer
 *
 */
public class EventPublisherImpl implements EventPublisher {

	private static final Logger logger = 
		LoggerFactory.getLogger(EventPublisherImpl.class);
		
	private EventAdmin eventAdmin;
	
	
	public void setEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
	}

	public void unsetEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = null;
	}
	

	/* (non-Javadoc)
	 * @see org.openhab.core.internal.events.EventPublisher#sendCommand(org.openhab.core.items.GenericItem, org.openhab.core.datatypes.DataType)
	 */
	public void sendCommand(String itemName, Command command) {
		if (command != null) {
			if(eventAdmin!=null) eventAdmin.sendEvent(createCommandEvent(itemName, command));
		} else {
			logger.warn("given command is NULL, couldn't send command to '{}'", itemName);
		}
	}

	/* (non-Javadoc)
	 * @see org.openhab.core.internal.events.EventPublisher#postCommand(org.openhab.core.items.GenericItem, org.openhab.core.datatypes.DataType)
	 */
	public void postCommand(String itemName, Command command) {
		if (command != null) {
			if(eventAdmin!=null) eventAdmin.postEvent(createCommandEvent(itemName, command));
		} else {
			logger.warn("given command is NULL, couldn't post command to '{}'", itemName);
		}
	}

	/* (non-Javadoc)
	 * @see org.openhab.core.internal.events.EventPublisher#postUpdate(org.openhab.core.items.GenericItem, org.openhab.core.datatypes.DataType)
	 */
	public void postUpdate(String itemName, State newState) {
		if (newState != null) {
			if(eventAdmin!=null) eventAdmin.postEvent(createUpdateEvent(itemName, newState));
		} else {
			logger.warn("given new state is NULL, couldn't post update for '{}'", itemName);
		}
	}
	
	private Event createUpdateEvent(String itemName, State newState) {
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put("item", itemName);
		properties.put("state", newState);
		return new Event(createTopic(EventType.UPDATE, itemName), properties);
	}

	private Event createCommandEvent(String itemName, Command command) {
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put("item", itemName);
		properties.put("command", command);
		return new Event(createTopic(EventType.COMMAND, itemName) , properties);
	}

	private String createTopic(EventType type, String itemName) {
		return TOPIC_PREFIX + TOPIC_SEPERATOR + type + TOPIC_SEPERATOR + itemName;
	}
	
	
}
