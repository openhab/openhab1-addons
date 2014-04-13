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
public enum DaikinSwing {
	
	UpDown("Ud"),
	Off("Off"),
	None("None");
	
	String command;
	
	private DaikinSwing(String command) {
		this.command = command;
	}
	
	public String getCommand() {
		return command;
	}

	public static DaikinSwing fromCommand(String command) {
		if (!StringUtils.isEmpty(command)) {
			for (DaikinSwing swing : DaikinSwing.values()) {
				if (swing.getCommand().equals(command)) {
					return swing;
				}
			}
		}
		
		throw new IllegalArgumentException("Invalid or unsupported Daikin swing: " + command);
	}
}
