/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.squeezebox.internal;

import org.apache.commons.lang.StringUtils;

/**
 * Represents all valid commands which could be processed by this binding
 * 
 * @author Markus Wolters
 * @author Ben Jones
 * @since 1.3.0
 */
public enum CommandType {
	
	POWER("power"),
	MUTE("mute"),
	VOLUME("volume"),
	PLAY("play"),
	PAUSE("pause"),
	STOP("stop"),
	NEXT("next"),
	PREV("prev"),
	FILE("file"),
	HTTP("http"),
	SYNC("sync"),
			
	TITLE("title"),	
	ARTIST("artist"),
	ALBUM("album"),
	COVERART("coverart"),
	YEAR("year"),
	REMOTETITLE("remotetitle"),
	GENRE("genre"),
	
	IRCODE("ircode");
	
	/** Represents the player command as it will be used in *.items configuration */
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
		
		throw new IllegalArgumentException("Invalid command: " + command);
	}
}
