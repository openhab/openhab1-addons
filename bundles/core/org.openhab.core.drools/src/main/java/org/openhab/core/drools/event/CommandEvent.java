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
import org.openhab.core.types.Command;

/**
 * This class is used as a fact in rules to inform about received commands on the openHAB event bus.
 * 
 * @author Kai Kreuzer
 * @since 0.7.0
 *
 */
public class CommandEvent extends RuleEvent {

	protected Command command;

	public CommandEvent(Item item, Command command) {
		super(item);
		this.command = command;
	}

	public Command getCommand() {
		return command;
	}
}
