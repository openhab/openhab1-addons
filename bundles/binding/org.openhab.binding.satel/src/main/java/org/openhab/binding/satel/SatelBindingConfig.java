/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel;

import org.openhab.binding.satel.internal.event.SatelEvent;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.openhab.binding.satel.internal.types.IntegraType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * Base interface that all Satel configuration classes must implement. Provides
 * methods to converts data between OpenHAB and Satel module.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public interface SatelBindingConfig extends BindingConfig {

	/**
	 * Converts data from {@link SatelEvent} to OpenHAB state of specified item.
	 * 
	 * @param item
	 *            an item to get new state for
	 * @param event
	 *            incoming event
	 * @return new item state
	 */
	State convertEventToState(Item item, SatelEvent event);

	/**
	 * Converts OpenHAB command to proper Satel message that changes state of
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
	SatelMessage handleCommand(Command command, IntegraType integraType, String userCode);

	/**
	 * Returns message needed to get current state of bound object.
	 * 
	 * @param integraType
	 *            type of connected Integra
	 * @return a message to send
	 */
	SatelMessage buildRefreshMessage(IntegraType integraType);
}
