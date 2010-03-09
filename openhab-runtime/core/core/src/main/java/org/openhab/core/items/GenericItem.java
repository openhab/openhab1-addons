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
package org.openhab.core.items;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

abstract public class GenericItem implements Item {
	
	protected EventPublisher eventPublisher;
	
	final protected String name;
	
	final protected boolean autoupdate;
	
	protected State state;
	
	public GenericItem(String name) {
		this.name = name;
		this.autoupdate = false;
	}

	public GenericItem(String name, boolean autoupdate) {
		this.name = name;
		this.autoupdate = autoupdate;
	}

	/* (non-Javadoc)
	 * @see org.openhab.core.items.Item#getState()
	 */
	public State getState() {
		return state;
	}
	
	public void initialize() {}
	
	public void dispose() {
		this.eventPublisher = null;
	}
		
	/* (non-Javadoc)
	 * @see org.openhab.core.items.Item#getName()
	 */
	public String getName() {
		return name;
	}
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	protected void internalSend(Command command) {
		// first try to send the command to the bus
		if(eventPublisher!=null) {
			eventPublisher.sendCommand(this.getName(), command);
		}
		
		// update the internal state ourself if needed
		if(autoupdate && getAcceptedDataTypes().contains(command.getClass())) {
			State newState = (State) command;
			setState(newState);
			
			// try to send the performed status update to the bus
			if(eventPublisher!=null) {
				eventPublisher.postUpdate(getName(), newState);
			}
		}
	}
	
	public void setState(State state) {
		State oldState = this.state;
		this.state = state;
		notifyListeners(oldState, state);
	}

	private void notifyListeners(State oldState, State newState) {
	}

}
