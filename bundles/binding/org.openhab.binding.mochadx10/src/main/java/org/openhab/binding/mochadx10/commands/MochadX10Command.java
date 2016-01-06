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

/**
 * This is the base class for commands received from the Mochad X10 Host. It
 * keeps the house code and unit code of the command and provides functionality
 * to post the command to the openHAB event bus.
 * 
 * @author Jack Sleuters
 * @since  1.7.0
 *
 */
public class MochadX10Command {
	/**
	 * Number of levels used by the 'dim' command
	 */
	public static final int DIM_LEVELS  = 22;
	
	/**
	 * Number of levels used by the 'xdim' command
	 */
	public static final int XDIM_LEVELS = 64;
		
	/**
	 * The eventPublisher required to postCommand
	 */
	protected EventPublisher eventPublisher;

	/**
	 * The X10 address for which this command holds
	 */
	private MochadX10Address address;
	
	/**
	 * The openhab level [0..100] after this command has been executed
	 */
	protected int level;

	/**
	 * Constructor
	 * 
	 * @param eventPublisher  	Required to execute postCommand
	 * @param address	 		The X10 address of the received X10 command
	 */
	public MochadX10Command(EventPublisher eventPublisher, MochadX10Address address) {
		this.address        = address;
		this.eventPublisher = eventPublisher;
		this.level          = 0;
	}
	
	/**
	 * @return The house code of the command
	 */
	public String getHouseCode() {
		return address.getHouseCode();
	}

	/**
	 * @return The unit code of the command
	 */
	public String getUnitCode() {
		return address.getUnitCode();
	}
	
	/**
	 * @return The address of the command
	 */
	public MochadX10Address getAddress() {
		return address;
	}

	/**
	 * @return The openhab level after the command has executed
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * This function should be overridden by sub-classes and implement the command
	 * specific code. It posts the command on the openHAB event bus.
	 * 
	 * The currentState is required because some commands upate the state in a 
	 * relative fashion.
	 * 
	 * @param itemName		The itemName to post the command to
	 * @param currentState	The currentState of the item
	 */
	public void postCommand(String itemName, int level) {
		// To be implemented by specific command classes
	}

	@Override
	public String toString() {
		return "MochadX10Command [eventPublisher=" + eventPublisher
				+ ", address=" + address + ", level=" + level + "]";
	}
}
