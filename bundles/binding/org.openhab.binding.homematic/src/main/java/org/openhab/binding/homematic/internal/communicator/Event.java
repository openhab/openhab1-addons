/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator;

import org.openhab.binding.homematic.internal.config.binding.ActionConfig;
import org.openhab.binding.homematic.internal.config.binding.HomematicBindingConfig;
import org.openhab.binding.homematic.internal.config.binding.ProgramConfig;
import org.openhab.binding.homematic.internal.config.binding.ValueBindingConfig;
import org.openhab.binding.homematic.internal.config.binding.VariableConfig;
import org.openhab.binding.homematic.internal.model.HmDatapoint;
import org.openhab.binding.homematic.internal.model.HmValueItem;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;

/**
 * Common Event class for Homematic and openHAB events.
 * Used by the HomematicCommunicator.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class Event {
	private Item item;
	private Type type;
	private HomematicBindingConfig bindingConfig;
	private HmValueItem hmValueItem;
	private Object newValue;

	/**
	 * Creates a new event received from the Homematic server.
	 */
	public Event(HomematicBindingConfig bindingConfig, Object newValue) {
		this.bindingConfig = bindingConfig;
		this.newValue = newValue;
	}

	/**
	 * Creates a new event received from openHAB.
	 */
	public Event(Item item, Type type, HomematicBindingConfig bindingConfig) {
		this.item = item;
		this.type = type;
		this.bindingConfig = bindingConfig;
	}

	/**
	 * Returns the item.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Returns the command/state.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Returns the bindingConfig.
	 */
	public HomematicBindingConfig getBindingConfig() {
		return bindingConfig;
	}

	/**
	 * Returns the Homematic valueItem.
	 */
	public HmValueItem getHmValueItem() {
		return hmValueItem;
	}

	/**
	 * Sets the Homematic valueItem.
	 */
	public void setHmValueItem(HmValueItem hmValueItem) {
		this.hmValueItem = hmValueItem;
	}

	/**
	 * Returns the new value for the item.
	 */
	public Object getNewValue() {
		return newValue;
	}

	/**
	 * Sets the new value for the item.
	 */
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	/**
	 * Returnes true if the type is a command.
	 */
	public boolean isCommand() {
		return type instanceof Command && !(type instanceof State);
	}

	/**
	 * Returns true if the bindingConfig is a program.
	 */
	public boolean isProgram() {
		return bindingConfig instanceof ProgramConfig;
	}

	/**
	 * Returns true if the bindingConfig is a action.
	 */
	public boolean isAction() {
		return bindingConfig instanceof ActionConfig;
	}

	/**
	 * Returns true if the bindingConfig is a variable.
	 */
	public boolean isVariable() {
		return bindingConfig instanceof VariableConfig;
	}

	/**
	 * Returns true if the type is a ON command.
	 */
	public boolean isOnType() {
		return type == OnOffType.ON;
	}

	/**
	 * Returns true if the name of the datapoint starts with PRESS_.
	 */
	public boolean isPressValueItem() {
		return hmValueItem.getName().startsWith("PRESS_");
	}

	/**
	 * Returns true if the event is a STOP command to a RollerShutter.
	 */
	public boolean isStopLevelDatapoint() {
		return type == StopMoveType.STOP && "LEVEL".equals(hmValueItem.getName()) && hmValueItem instanceof HmDatapoint;
	}

	/**
	 * Returns true if the current and the new value equals.
	 */
	public boolean isNewValueEqual() {
		return (bindingConfig instanceof ValueBindingConfig
				&& !((ValueBindingConfig) bindingConfig).isForceUpdate() && hmValueItem.getValue().equals(newValue));
	}
}
