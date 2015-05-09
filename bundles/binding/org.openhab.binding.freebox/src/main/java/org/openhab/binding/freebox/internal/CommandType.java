/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.freebox.internal;

import org.apache.commons.lang.StringUtils;

/**
 * Represents all valid commands which could be processed by this binding
 * 
 * @author Gaël L'hopital
 * @since 1.5.0
 */
public enum CommandType {
	
	// System configuration commands
	FWVERSION("fw_version"),
	UPTIME("uptime"),
	CPUM("temp_cpum"),
	CPUB("temp_cpub"),
	SW("temp_sw"),
	FAN("fan"),
	REBOOT("reboot"),
	
	// Connection Status commands
	LINESTATUS("line_status"),
	IPV4("ipv4"),
	RATEUP("rate_up"),
	RATEDOWN("rate_down"),
	BYTESUP("bytes_up"),
	BYTESDOWN("bytes_down"),
	// Wifi Status
	WIFISTATUS("wifi_status"),
	// Incoming call commands
	CALLNUMBER("call_number"),
	CALLDURATION("call_duration"),
	CALLTIMESTAMP("call_timestamp"),
	CALLNAME("call_name"),
	CALLSTATUS("call_status"),
	// xDSL Status
	XDSLSTATUS("xdsl_status"),
	// LCD Configuration
	LCDBRIGHTNESS("lcd_brightness"),
	LCDORIENTATION("lcd_orientation"),
	LCDFORCED("lcd_forced"),
	// FTP status
	FTPSTATUS("ftp_status"),
	// UPnP AV status
	UPNPAVSTATUS("upnpav_status"),
	// Air Media status
	AIRMEDIASTATUS("airmedia_status"),
	// Samba file share status
	SAMBAFILESTATUS("sambafileshare_status"),
	// Samba printer share status
	SAMBAPRINTERSTATUS("sambaprintershare_status"),
	// Network device reachable status
	REACHABLENAME("reachable_name"),
	REACHABLEMAC("reachable_mac"),
	REACHABLEIP("reachable_ip");
	

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
