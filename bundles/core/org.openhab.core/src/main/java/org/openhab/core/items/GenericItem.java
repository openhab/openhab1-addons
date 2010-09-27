/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.core.items;

import java.util.Collection;
import java.util.HashSet;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

abstract public class GenericItem implements Item {
	
	protected EventPublisher eventPublisher;
	
	protected Collection<StateChangeListener> listeners = new HashSet<StateChangeListener>();
	
	final protected String name;
	
	protected State state = UnDefType.UNDEF;

	private boolean changed;
	
	public GenericItem(String name) {
		this.name = name;
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
	
	@Override
	public boolean hasChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	protected void internalSend(Command command) {
		// try to send the command to the bus
		if(eventPublisher!=null) {
			eventPublisher.sendCommand(this.getName(), command);
		}		
	}
	
	public void setState(State state) {
		State oldState = this.state;
		this.state = state;
		notifyListeners(oldState, state);
	}

	private void notifyListeners(State oldState, State newState) {
		// if nothing has changed, we do not need to send notifications
		if(oldState.equals(newState)) return;
		
		for(StateChangeListener listener : listeners) {
			listener.stateChanged(this, oldState, newState);
		}
	}
	
	@Override
	public String toString() {
		return getName() + " (" +
			"Type=" + getClass().getSimpleName() + ", " +
			"State=" + getState() + ")";
	}

	public void addStateChangeListener(StateChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removeStateChangeListener(StateChangeListener listener) {
		listeners.remove(listener);
	}
	
}
