/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.piface.internal;

/**
 * Enumeration for supported PiFace commands. These are copied from the Pfion python library
 * which we use to communicate with the PiFace extension board. Not all commands are used
 * by this binding.
 * 
 * @author Ben Jones
 * @since 1.3.0
 */
public enum PifaceCommand {

	ERROR_ACK(0),

	// piface.pfion command constants
	WRITE_OUT_CMD(1),
	WRITE_OUT_ACK(2),
	READ_OUT_CMD(3),
	READ_OUT_ACK(4),
	READ_IN_CMD(5),
	READ_IN_ACK(6),
	DIGITAL_WRITE_CMD(7),
	DIGITAL_WRITE_ACK(8),
	DIGITAL_READ_CMD(9),
	DIGITAL_READ_ACK(10),
	
	// watchdog command constants
	WATCHDOG_CMD(14),
	WATCHDOG_ACK(15);
	
	private final int command;

	PifaceCommand(int command) {
		this.command = command;
	}
	
	public byte toByte() {
		return (byte) command;
	}
}
