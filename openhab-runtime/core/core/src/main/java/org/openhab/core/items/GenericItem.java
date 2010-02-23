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

import java.util.List;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.CommandType;
import org.openhab.core.types.DataType;

abstract public class GenericItem {
	
	protected EventPublisher eventPublisher;
	
	final protected String name;
	
	final protected boolean autoupdate;
	
	final protected boolean broadcastOnChangeOnly;
	
	protected DataType state;
	
	public GenericItem(String name) {
		this.name = name;
		this.autoupdate = false;
		this.broadcastOnChangeOnly = true;
	}

	public GenericItem(String name, boolean autoupdate, boolean broadcastOnChangeOnly) {
		this.name = name;
		this.autoupdate = autoupdate;
		this.broadcastOnChangeOnly = broadcastOnChangeOnly;
	}

	public DataType getState() {
		return state;
	}
	
	public void initialize() {}
	
	public void dispose() {
		this.eventPublisher = null;
	}
		
	public String getName() {
		return name;
	}
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	protected void internalSend(CommandType command) {
		eventPublisher.sendCommand(this.getName(), command);
		if(autoupdate && getAcceptedDataTypes().contains(command.getClass())) {
			setState((DataType) command);
		}
	}
	
	protected void setState(DataType state) {
		DataType oldState = this.state;
		this.state = state;
		broadcastUpdate(oldState, state);
	}

	private void broadcastUpdate(DataType oldState, DataType newState) {
		if(!broadcastOnChangeOnly && newState.equals(oldState)) {
			eventPublisher.postUpdate(getName(), getState());
		}
	}
	
	abstract protected List<Class<? extends DataType>> getAcceptedDataTypes();
}
