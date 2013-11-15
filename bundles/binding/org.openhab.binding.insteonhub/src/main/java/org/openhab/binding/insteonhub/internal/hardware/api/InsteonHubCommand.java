/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonhub.internal.hardware.api;

/**
 * A single command for an INSTEON device
 * 
 * @author Eric Thill
 * 
 */
public class InsteonHubCommand {
	public enum CommandType {
		GET_LEVEL, ON_FAST, OFF_FAST, OFF, ON, START_DIM, START_BRT, STOP_DIM_BRT
	}

	public static InsteonHubCommand newGetLevelCommand(String device) {
		return new InsteonHubCommand(CommandType.GET_LEVEL, device, 0);
	}

	public static InsteonHubCommand newOnFastCommand(String device) {
		return new InsteonHubCommand(CommandType.ON_FAST, device, 0);
	}

	public static InsteonHubCommand newOffFastCommand(String device) {
		return new InsteonHubCommand(CommandType.OFF_FAST, device, 0);
	}

	public static InsteonHubCommand newOnCommand(String device, int level) {
		return new InsteonHubCommand(CommandType.ON, device, level);
	}

	public static InsteonHubCommand newOffCommand(String device) {
		return new InsteonHubCommand(CommandType.OFF, device, 0);
	}

	public static InsteonHubCommand newStartDimCommand(String device) {
		return new InsteonHubCommand(CommandType.START_DIM, device, 0);
	}

	public static InsteonHubCommand newStartBrightenCommand(String device) {
		return new InsteonHubCommand(CommandType.START_BRT, device, 0);
	}

	public static InsteonHubCommand newStopAdjustCommand(String device) {
		return new InsteonHubCommand(CommandType.STOP_DIM_BRT, device, 0);
	}

	private final CommandType type;
	private final String device;
	private final int level;

	private InsteonHubCommand(CommandType type, String device, int level) {
		this.type = type;
		this.device = device;
		this.level = level;
	}

	public CommandType getType() {
		return type;
	}

	public String getDevice() {
		return device;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public String toString() {
		return type + " '" + device + "' " + level;
	}
}