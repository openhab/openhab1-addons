/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel;

import java.util.Map;

import org.openhab.binding.satel.internal.event.SatelEvent;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.openhab.binding.satel.internal.types.IntegraType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * Base class that all Satel configuration classes must extend. Provides methods
 * to convert data between openHAB and Satel module.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public abstract class SatelBindingConfig implements BindingConfig {

	public enum Options {
		COMMANDS_ONLY, FORCE_ARM, INVERT_STATE
	}

	private static final DecimalType DECIMAL_ONE = new DecimalType(1);

	private Map<String, String> options;
	private boolean itemInitialized;

	/**
	 * Checks whether given option is set to <code>true</code>.
	 * 
	 * @param option
	 *            option to check
	 * @return <code>true</code> if option is enabled
	 */
	public boolean hasOptionEnabled(Options option) {
		return Boolean.parseBoolean(getOption(option));
	}

	/**
	 * Returns value of given option.
	 * 
	 * @param option
	 *            option to get value for
	 * @return string value or <code>null</code> if option is not present
	 */
	public String getOption(Options option) {
		return this.options.get(option.name());
	}

	/**
	 * Returns string representation of option map.
	 * 
	 * @return string as pairs of [name]=[value] separated by comma
	 */
	public String optionsAsString() {
		return this.options.toString();
	}

	/**
	 * Returns initialization state of bound item.
	 * 
	 * @return <code>true</code> if bound item has received state update,
	 *         <code>false</code> if it is uninitialized
	 */
	public boolean isItemInitialized() {
		return itemInitialized;
	}

	/**
	 * Notifies that bound item has its state updated.
	 */
	public void setItemInitialized() {
		this.itemInitialized = true;
	}

	/**
	 * Converts data from {@link SatelEvent} to openHAB state of specified item.
	 * 
	 * @param item
	 *            an item to get new state for
	 * @param event
	 *            incoming event
	 * @return new item state
	 */
	public abstract State convertEventToState(Item item, SatelEvent event);

	/**
	 * Converts openHAB command to proper Satel message that changes state of
	 * bound object (output, zone).
	 * 
	 * @param command
	 *            command to convert
	 * @param integraType
	 *            type of connected Integra
	 * @param userCode
	 *            user's password
	 * @return a message to send
	 */
	public abstract SatelMessage convertCommandToMessage(Command command, IntegraType integraType, String userCode);

	/**
	 * Returns message needed to get current state of bound object.
	 * 
	 * @param integraType
	 *            type of connected Integra
	 * @return a message to send
	 */
	public abstract SatelMessage buildRefreshMessage(IntegraType integraType);

	protected SatelBindingConfig(Map<String, String> options) {
		this.options = options;
		this.itemInitialized = false;
	}

	protected State booleanToState(Item item, boolean value) {
		if (item instanceof ContactItem) {
			return value ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
		} else if (item instanceof SwitchItem) {
			return value ? OnOffType.ON : OnOffType.OFF;
		} else if (item instanceof NumberItem) {
			return value ? DECIMAL_ONE : DecimalType.ZERO;
		}

		return null;
	}

}
