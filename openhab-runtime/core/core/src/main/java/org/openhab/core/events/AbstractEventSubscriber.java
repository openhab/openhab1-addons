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

import org.openhab.core.datatypes.DataType;
import org.openhab.core.items.GenericItem;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

abstract public class AbstractEventSubscriber implements EventSubscriber, EventHandler {
		
	abstract public void receiveCommand(GenericItem item, DataType command);

	abstract public void receiveUpdate(GenericItem item, DataType newStatus);
	
	abstract public void refresh(GenericItem item);
	
	public void handleEvent(Event event) {  
		GenericItem item = (GenericItem) event.getProperty("item");
		refresh(item);
	}
}
