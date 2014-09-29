/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.events;

import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * An EventSubscriber receives events from the openHAB event bus for further processing.
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 */
public interface EventSubscriber {

	/**
	 * Callback method if a command was sent on the event bus
	 * 
	 * @param itemName the item for which a command was sent
	 * @param command the command that was sent
	 */
	public void receiveCommand(String itemName, Command command);
	
	/**
	 * Callback method if a state update was sent on the event bus
	 * 
	 * @param itemName the item for which a state update was sent
	 * @param state the state that was sent
	 */
	public void receiveUpdate(String itemName, State newStatus);

}
