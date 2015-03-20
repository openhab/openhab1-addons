/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.events;

import static org.openhab.core.events.EventConstants.TOPIC_PREFIX;
import static org.openhab.core.events.EventConstants.TOPIC_SEPERATOR;

import org.openhab.core.types.AlarmState;
import org.openhab.core.types.AlarmState.AlarmClass;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.EventType;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

abstract public class AbstractEventSubscriber implements EventSubscriber, EventHandler {
	
	/**
	 * {@inheritDoc}
	 */
	public void handleEvent(Event event) {  
		String itemName = (String) event.getProperty("item");
		
		String topic = event.getTopic();
		String[] topicParts = topic.split(TOPIC_SEPERATOR);
		
		if(!(topicParts.length > 2) || !topicParts[0].equals(TOPIC_PREFIX)) {
			return; // we have received an event with an invalid topic
		}
		String operation = topicParts[1];
		
		if(operation.equals(EventType.UPDATE.toString())) {
			State newState = (State) event.getProperty("state");
			if(newState!=null) receiveUpdate(itemName, newState);
		}
		if(operation.equals(EventType.COMMAND.toString())) {
			Command command = (Command) event.getProperty("command");
			if(command!=null) receiveCommand(itemName, command);
		}
		if(operation.equals(EventType.ALARM.toString())) {
			boolean alarmOn = (Boolean) event.getProperty("alarm");
			if (!alarmOn) {
				receiveAlarmCancel(itemName);
			}
			else {
				String alarmText = (String) event.getProperty("alarmtext");
				if(alarmText!=null) {
					AlarmState alarmState;
					AlarmClass alarmClass=(AlarmState.AlarmClass) event.getProperty("alarmclass");
					alarmState=new AlarmState(alarmText, alarmClass);

					long alarmTime=(Long) event.getProperty("alarmtime_utc");
					alarmState.setAlarmTimeUTC(alarmTime);
					receiveAlarm(itemName, alarmState);
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void receiveCommand(String itemName, Command command) {
		// default implementation: do nothing
	}

	/**
	 * {@inheritDoc}
	 */
	public void receiveUpdate(String itemName, State newState) {
		// default implementation: do nothing
	}

	/**
	 * {@inheritDoc}
	 */
	public void receiveAlarm(String itemName, AlarmState alarmState) {
		// default implementation: do nothing
	}

	/**
	 * {@inheritDoc}
	 */
	public void receiveAlarmCancel(String itemName) {
		// default implementation: do nothing
	}
}
