/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.drools.event;

import org.openhab.core.items.Item;
import org.openhab.core.types.State;

/**
 * This class is used as a fact in rules to inform about received status updates on the openHAB event bus.
 * 
 * @author Kai Kreuzer
 * @since 0.7.0
 *
 */
public class StateEvent extends RuleEvent {

	protected boolean changed;
	protected State oldState;
	protected State newState;

	public StateEvent(Item item, State oldState, State newState) {
		super(item);
		this.oldState = oldState;
		this.newState = newState;
		this.changed = !oldState.equals(newState);
	}

	public StateEvent(Item item, State state) {
		this(item, state, state);
	}
	
	public boolean isChanged() {
		return changed;
	}

	public State getOldState() {
		return oldState;
	}

	public State getNewState() {
		return newState;
	}	
}
