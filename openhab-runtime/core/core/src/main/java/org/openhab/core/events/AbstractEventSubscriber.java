/* 
* openHAB, the open Home Automation Bus.
* Copyright 2010, openHAB.org
*
* See the contributors.txt file in the distribution for a
* full listing of individual contributors.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as
* published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package org.openhab.core.events;

import static org.openhab.core.events.EventConstants.TOPIC_PREFIX;
import static org.openhab.core.events.EventConstants.TOPIC_SEPERATOR;

import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.EventType;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

abstract public class AbstractEventSubscriber implements EventSubscriber, EventHandler {
	
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
	}
	
	public void receiveCommand(String itemName, Command command) {
		// default implementation: do nothing
	}

	public void receiveUpdate(String itemName, State newState) {
		// default implementation: do nothing
	}

}
