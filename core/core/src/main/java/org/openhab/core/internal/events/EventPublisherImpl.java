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
package org.openhab.core.internal.events;

import java.util.Dictionary;
import java.util.Hashtable;

import org.openhab.core.datatypes.DataType;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.GenericItem;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

public class EventPublisherImpl implements EventPublisher {

	public static final String TOPIC_PREFIX = "openhab/";
	
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
	public void sendCommand(GenericItem item, DataType command) {
		if(eventAdmin!=null) eventAdmin.sendEvent(createCommandEvent(item, command));
	}

	/* (non-Javadoc)
	 * @see org.openhab.core.internal.events.EventPublisher#postCommand(org.openhab.core.items.GenericItem, org.openhab.core.datatypes.DataType)
	 */
	public void postCommand(GenericItem item, DataType command) {
		if(eventAdmin!=null) eventAdmin.postEvent(createCommandEvent(item, command));
	}

	/* (non-Javadoc)
	 * @see org.openhab.core.internal.events.EventPublisher#postUpdate(org.openhab.core.items.GenericItem, org.openhab.core.datatypes.DataType)
	 */
	public void postUpdate(GenericItem item, DataType newState) {
		if(eventAdmin!=null) eventAdmin.postEvent(createUpdateEvent(item, newState));
	}

	/* (non-Javadoc)
	 * @see org.openhab.core.internal.events.EventPublisher#refresh(org.openhab.core.items.GenericItem)
	 */
	public void refresh(GenericItem item) {
		if(eventAdmin!=null) eventAdmin.postEvent(createRefreshEvent(item));
	}
	
	private Event createUpdateEvent(GenericItem item, DataType newState) {
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put("item", item);
		properties.put("state", newState);
		return new Event(TOPIC_PREFIX + item + "/update", properties);
	}

	private Event createRefreshEvent(GenericItem item) {
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put("item", item);
		return new Event(TOPIC_PREFIX + item + "/refresh", properties);
	}

	private Event createCommandEvent(GenericItem item, DataType command) {
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put("item", item);
		properties.put("cmd", command);
		return new Event(TOPIC_PREFIX + item.getName() + "/command", properties);
	}
}
