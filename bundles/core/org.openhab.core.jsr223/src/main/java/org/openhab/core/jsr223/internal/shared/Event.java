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
 * Event is used by a Script to listen for specific events
 * 
 * @author Simon Merschjohann
 * @since 1.7.0
 */
public class Event {
	private TriggerType triggerType;
	private Item item;
	private State oldState;
	private State newState;
	private Command command;

	public Event(TriggerType triggerType, Item item, State oldState, State newState, Command command) {
		this.setItem(item);
		this.setOldState(oldState);
		this.setNewState(newState);
		this.setTriggerType(triggerType);
		this.setCommand(command);
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command cmd) {
		this.command = cmd;
	}

	public State getOldState() {
		return oldState;
	}

	public void setOldState(State oldState) {
		this.oldState = oldState;
	}

	public State getNewState() {
		return newState;
	}

	public void setNewState(State newState) {
		this.newState = newState;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public TriggerType getTriggerType() {
		return this.triggerType;
	}

	public void setTriggerType(TriggerType triggerType) {
		this.triggerType = triggerType;
	}

	@Override
	public String toString() {
		return "Event [triggerType=" + triggerType + ", item=" + item + ", oldState=" + oldState + ", newState=" + newState + ", command=" + command + "]";
	}

}
