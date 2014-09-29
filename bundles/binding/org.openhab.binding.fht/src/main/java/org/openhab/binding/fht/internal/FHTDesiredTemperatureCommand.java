/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fht.internal;

/**
 * This class repsents a waiting command to be send to a FHT-80b.
 * 
 * @author Till Klocke
 * @since 1.4.0
 * 
 */
public class FHTDesiredTemperatureCommand {

	private String address;
	private String command;

	public FHTDesiredTemperatureCommand(String address, String command) {
		this.address = address;
		this.command = command;
	}

	public String getAddress() {
		return address;
	}

	public String getCommand() {
		return command;
	}

}
