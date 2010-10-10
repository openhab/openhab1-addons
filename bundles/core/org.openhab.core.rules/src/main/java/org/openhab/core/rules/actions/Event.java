package org.openhab.core.rules.actions;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;

public class Event {

	static private EventPublisher eventPublisher;

	public void setEventPublisher(EventPublisher eventPublisher) {
		Event.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		Event.eventPublisher = null;
	}
	
	static public void send(String itemName, Command command) {
		if(eventPublisher!=null) {
			eventPublisher.sendCommand(itemName, command);
		}
	}

	static public void post(String itemName, Command command) {
		if(eventPublisher!=null) {
			eventPublisher.postCommand(itemName, command);
		}
	}
}
