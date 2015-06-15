/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal;

import org.apache.commons.lang.StringUtils;

/**
 * The list of supported command types for the Sapp binding
 *  
 * @author Paolo Denti
 * @since 1.0.0
 */
public enum SappCommandType {
	
	ALARM("alarm"),
	INPUT("input"),
	OUTPUT("output"),
	VIRTUAL("virtual"),
	HUMIDITYIN("humidityin");
	
	String command;
	
	private SappCommandType(String command) {
		this.command = command;
	}
	
	public String getCommand() {
		return command;
	}
	
	public static SappCommandType fromString(String command) {
		if (!StringUtils.isEmpty(command)) {
			for (SappCommandType commandType : SappCommandType.values()) {
				if (commandType.getCommand().equals(command)) {
					return commandType;
				}
			}
		}
		
		throw new IllegalArgumentException("Invalid or unsupported Sapp command: " + command);
	}
	
//	public Class<? extends Item> getSupportedItemType() {
//		switch (this)
//		{
//			case ALARM:
//				return SwitchItem.class;
//			case TEMP:
//			case TEMPIN:
//			case HUMIDITYIN:
//			case TEMPOUT:
//				return NumberItem.class;
//			default:
//				return StringItem.class;
//		}
//	}
//	
	public boolean isExecutable() {
		switch (this)
		{
			case ALARM:
			case INPUT:
			case OUTPUT:
			case VIRTUAL:
				return true;
			default:
				return false;
		}
	}
}
