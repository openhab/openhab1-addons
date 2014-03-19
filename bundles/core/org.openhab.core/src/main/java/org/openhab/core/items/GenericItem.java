/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

/** 
 * The abstract base class for all items. It provides all relevant logic
 * for the infrastructure, such as publishing updates to the event bus
 * or notifying listeners.
 *  
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
abstract public class GenericItem implements Item {
	
	protected EventPublisher eventPublisher;

	protected Set<StateChangeListener> listeners = new CopyOnWriteArraySet<StateChangeListener>(Collections.newSetFromMap(new WeakHashMap<StateChangeListener, Boolean>()));
	
	protected List<String> groupNames = new ArrayList<String>();
	
	final protected String name;
	
	protected State state = UnDefType.NULL;
	
	public GenericItem(String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	public State getState() {
		return state;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public State getStateAs(Class<? extends State> typeClass) {
		if(typeClass!=null && typeClass.isInstance(state)) {
			return state;
		} else {
			return null;
		}
	}
	
	public void initialize() {}
	
	public void dispose() {
		this.eventPublisher = null;
	}
		
	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<String> getGroupNames() {
		return groupNames;
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
		// if nothing has changed, we send update notifications
		Set<StateChangeListener> clonedListeners = null;
		clonedListeners = new CopyOnWriteArraySet<StateChangeListener>(listeners);
		for(StateChangeListener listener : clonedListeners) {
			listener.stateUpdated(this, newState);
		}
		if(!oldState.equals(newState)) {
			for(StateChangeListener listener : clonedListeners) {
				listener.stateChanged(this, oldState, newState);
			}
		}
	}
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getName() + " (" +
			"Type=" + getClass().getSimpleName() + ", " +
			"State=" + getState() + ")";
	}

	public void addStateChangeListener(StateChangeListener listener) {
		synchronized(listeners) {
			listeners.add(listener);
		}
	}
	
	public void removeStateChangeListener(StateChangeListener listener) {
		synchronized(listeners) {
			listeners.remove(listener);
		}
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupNames == null) ? 0 : groupNames.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericItem other = (GenericItem) obj;
		if (groupNames == null) {
			if (other.groupNames != null)
				return false;
		} else if (!groupNames.equals(other.groupNames))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
	
	
}
