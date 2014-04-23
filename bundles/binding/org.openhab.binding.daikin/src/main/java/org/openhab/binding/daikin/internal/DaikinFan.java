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

/**
 * The list of supported modes for the Daikin binding
 *  
 * @author Ben Jones
 * @since 1.5.0
 */
public enum DaikinFan {
	
	Auto("FAuto"),
	F1("Fun1"),
	F2("Fun2"),
	F3("Fun3"),
	F4("Fun4"),
	F5("Fun5"),
	None("None");
	
	String command;
	
	private DaikinFan(String command) {
		this.command = command;
	}
	
	public String getCommand() {
		return command;
	}

	public static DaikinFan fromCommand(String command) {
		if (!StringUtils.isEmpty(command)) {
			for (DaikinFan fan : DaikinFan.values()) {
				if (fan.getCommand().equals(command)) {
					return fan;
				}
			}
		}
		
		throw new IllegalArgumentException("Invalid or unsupported Daikin fan: " + command);
	}
}
