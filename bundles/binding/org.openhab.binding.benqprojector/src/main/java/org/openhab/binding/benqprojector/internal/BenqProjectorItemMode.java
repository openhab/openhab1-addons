/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.benqprojector.internal;

/**
 * Define item modes with the ability to generate relevant
 * strings for sending to the projector for querying etc
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public enum BenqProjectorItemMode {
	
	POWER("pow"),
	MUTE("mute");
	
	private final String command;
	
	private BenqProjectorItemMode(String command)
	{
		this.command = command;
	}
	
	public String getItemModeCommandQueryString()
	{
		return getItemModeCommandSetString("?");
	}
	
	public String getItemModeCommandSetString(String value)
	{
		return "\r*"+this.command+"="+value+"#\r";
	}
}
