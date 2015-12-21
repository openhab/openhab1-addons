/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.jsr223.internal.shared;

import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * ChangedEventTrigger is used by a Script to listen for item changes
 * 
 * @author Simon Merschjohann
 * @since 1.7.0
 */
public class ChangedEventTrigger implements EventTrigger {
	private String itemName;
	private State fromState;
	private State toState;

	public ChangedEventTrigger(String itemName, State fromState, State toState) {
		this.itemName = itemName;
		this.fromState = fromState;
		this.toState = toState;
	}

	public ChangedEventTrigger(String itemName) {
		this.itemName = itemName;
		this.fromState = null;
		this.toState = null;
	}

	@Override
	public boolean evaluate(Item item, State oldState, State newState, Command command, TriggerType type) {
		return type == TriggerType.CHANGE && item.getName().equals(itemName) 
				&& (this.fromState == null || this.fromState.equals(oldState)) 
				&& (this.toState == null || this.toState.equals(newState));
	}

	@Override
	public String getItem() {
		return this.itemName;
	}

}
