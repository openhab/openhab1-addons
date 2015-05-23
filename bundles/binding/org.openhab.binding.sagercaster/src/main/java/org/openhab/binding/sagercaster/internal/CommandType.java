/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sagercaster.internal;

import org.apache.commons.lang.StringUtils;

/**
 * Represents all valid commands which could be processed by this binding
 * 
 * @author Gaël L'hopital
 * @since 1.7.0
 */
public enum CommandType {
	
	// The items holding these keywords are used as input 
	// for the SagerCaster algorithm
	WINDBEARING("windbearing"),
	SEALEVELPRESSURE("sealevelpressure"),
	CLOUDLEVEL("cloudlevel"),
	RAINING("raining"),
	WINDSPEED("windspeed"),
	
	// Direct derived elements from input items
	COMPASS("compass"),
	WINDTREND("windtrend"),
	PRESSURETREND("pressuretrend"),
	
	// Result parts of the Sager Caster Algorithm
	FORECAST("forecast"),
	VELOCITY("velocity"),
	WINDFROM("windfrom"),
	WINDTO("windto");

	String command;
	
	private CommandType(String command) {
		this.command = command;
	}
	
	public String getCommand() {
		return command;
	}
	
	public static CommandType fromString(String command) {
		if (!StringUtils.isEmpty(command)) {
			for (CommandType commandType : CommandType.values()) {
				if (commandType.getCommand().equals(command)) {
					return commandType;
				}
			}
		}
		
		throw new IllegalArgumentException("Invalid Command Type: " + command);
	}
}
