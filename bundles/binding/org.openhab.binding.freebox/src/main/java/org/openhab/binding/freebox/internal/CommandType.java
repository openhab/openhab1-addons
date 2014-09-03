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
	CALLSTATUS("call_status"),
	// xDSL Status
	XDSLSTATUS("xdsl_status"),
	// LCD Configuration
	LCDBRIGHTNESS("lcd_brightness"),
	LCDORIENTATION("lcd_orientation"),
	LCDFORCED("lcd_forced");
	

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
