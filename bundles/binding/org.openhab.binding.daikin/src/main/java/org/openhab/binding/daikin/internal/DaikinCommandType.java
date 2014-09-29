/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.daikin.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;

/**
 * The list of supported command types for the Daikin binding
 *  
 * @author Ben Jones
 * @since 1.5.0
 */
public enum DaikinCommandType {
	
	POWER("power"),
	MODE("mode"),
	TEMP("temp"),
	FAN("fan"),
	SWING("swing"),
	
	TEMPIN("tempin"),
	TIMER("timer"),
	TEMPOUT("tempout"),
	HUMIDITYIN("humidityin");
	
	String command;
	
	private DaikinCommandType(String command) {
		this.command = command;
	}
	
	public String getCommand() {
		return command;
	}
	
	public static DaikinCommandType fromString(String command) {
		if (!StringUtils.isEmpty(command)) {
			for (DaikinCommandType commandType : DaikinCommandType.values()) {
				if (commandType.getCommand().equals(command)) {
					return commandType;
				}
			}
		}
		
		throw new IllegalArgumentException("Invalid or unsupported Daikin command: " + command);
	}
	
	public Class<? extends Item> getSupportedItemType() {
		switch (this)
		{
			case POWER:
				return SwitchItem.class;
			case TEMP:
			case TEMPIN:
			case HUMIDITYIN:
			case TEMPOUT:
				return NumberItem.class;
			default:
				return StringItem.class;
		}
	}
	
	public boolean isExecutable() {
		switch (this)
		{
			case POWER:
			case MODE:
			case TEMP:
			case FAN:
			case SWING:
				return true;
			default:
				return false;
		}
	}
}
