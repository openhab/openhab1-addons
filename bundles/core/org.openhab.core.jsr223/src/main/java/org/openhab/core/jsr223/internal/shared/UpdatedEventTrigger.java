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
 * UpdatedEventTrigger is used by a Script to listen for item updates
 * 
 * @author Steve Bate
 * @since 1.8.0
 */
public class UpdatedEventTrigger implements EventTrigger {
	private String itemName;

	public UpdatedEventTrigger(String itemName) {
		this.itemName = itemName;
	}

	@Override
	public boolean evaluate(Item item, State oldState, State newState, Command command, TriggerType type) {
		return type == TriggerType.UPDATE && item.getName().equals(itemName);
	}

	@Override
	public String getItem() {
		return this.itemName;
	}

}
