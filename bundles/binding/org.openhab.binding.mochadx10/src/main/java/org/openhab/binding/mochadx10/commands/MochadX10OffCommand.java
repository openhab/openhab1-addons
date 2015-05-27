/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mochadx10.commands;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;

/**
 * Implementation of the X10 Off command
 * 
 * @author Jack Sleuters
 * @since  1.7.0
 *
 */
public class MochadX10OffCommand extends MochadX10Command {
	/**
	 * Constructor
	 * 
	 * @param eventPublisher	Required to post the command on the openhab bus
	 * @param address			The address for which this command was received
	 */
	public MochadX10OffCommand(EventPublisher eventPublisher, MochadX10Address address) {
		super(eventPublisher, address);
	}
	
	@Override
	public void postCommand(String itemName, int currentLevel) {
		eventPublisher.postCommand(itemName, OnOffType.OFF);
	}

	@Override
	public int getLevel() {
		return 0;
	}
}
