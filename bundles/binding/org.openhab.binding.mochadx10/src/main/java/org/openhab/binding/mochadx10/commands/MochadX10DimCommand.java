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
import org.openhab.core.library.types.PercentType;

/**
 * Implementation of the X10 Dim command
 * 
 * @author Jack Sleuters
 * @since  1.7.0
 *
 */
public class MochadX10DimCommand extends MochadX10Command {
	/**
	 * The dim value received from the Mochad X10 server. 
	 * Note this is a relative value in the range [0..DIM_LEVELS)
	 */
	private int value;
	
	/**
	 * Constructor
	 * 
	 * @param eventPublisher	Required to post the command on the openhab bus
	 * @param address			The address for which this command was received
	 * @param value				The dim value received
	 */
	public MochadX10DimCommand(EventPublisher eventPublisher, MochadX10Address address, int value) {
		super(eventPublisher, address);
		
		this.value = value;
	}

	@Override
	public void postCommand(String itemName, int currentLevel) {
		if (value < 0) {
			value = 1;
		}
		level = Math.max(0, currentLevel - value * 100/(MochadX10Command.DIM_LEVELS - 1));
		
		eventPublisher.postCommand(itemName, new PercentType(level));
	}

	@Override
	public String toString() {
		return "MochadX10DimCommand [value=" + value + ", toString()="
				+ super.toString() + "]";
	}
}
