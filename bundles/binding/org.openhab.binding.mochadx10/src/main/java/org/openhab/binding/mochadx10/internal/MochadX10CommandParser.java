/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mochadx10.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.mochadx10.commands.MochadX10Address;
import org.openhab.binding.mochadx10.commands.MochadX10BrightCommand;
import org.openhab.binding.mochadx10.commands.MochadX10Command;
import org.openhab.binding.mochadx10.commands.MochadX10DimCommand;
import org.openhab.binding.mochadx10.commands.MochadX10OffCommand;
import org.openhab.binding.mochadx10.commands.MochadX10OnCommand;
import org.openhab.core.events.EventPublisher;

/**
 * The command parser is used to convert incoming Mochad X10 messages into
 * MochadX10Commands. Furthermore, it keeps track of the last used house code
 * and unit code. This is needed because some commands received only specify the 
 * house code and assume the unit code is the same as in the previous command.
 * 
 * @author Jack Sleuters
 * @since  1.7.0
 */
public class MochadX10CommandParser {
	/**
	 * Pattern for parsing a Mochad X10 dim command
	 */
	static final Pattern DIM_PATTERN = Pattern.compile("dim(\\((?<value>[0-9]*)\\))?");

	/**
	 * Pattern for parsing a Mochad X10 bright command
	 */
	static final Pattern BRIGHT_PATTERN = Pattern.compile("bright(\\((?<value>[0-9]*)\\))?");

	/**
	 * The unit code used by the previous command
	 */
	private String previousUnitCode  = "";
	
	/**
	 * The house code used by the previous command
	 */
	private String previousHouseCode = "";
	
	/**
	 * The pattern to parse a received message containing 'HouseUnit:'.
	 * 
	 * For example: 
	 * 
	 * 02/22 15:50:02 Rx PL HouseUnit: B3
	 */
	private static final Pattern HOUSEUNIT_COMMAND = Pattern.compile("[0-9]{2}/[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2}\\sRx\\s((RF)|(PL))\\sHouseUnit:\\s" +
			"(?<houseCode>[A-P])(?<unitCode>[0-9]*)(\\sFunc:\\s(?<command>.+))?");

	/**
	 * The pattern to parse a received message containing 'House:'.
	 * 
	 * For example: 
	 * 
	 * 02/22 15:50:08 Rx PL House: B Func: Bright(13)
	 */
	private  Pattern HOUSE_COMMAND = Pattern.compile("[0-9]{2}/[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2}\\sRx\\s((RF)|(PL))\\sHouse:\\s" +
			"(?<houseCode>[A-P])\\sFunc:\\s(?<command>.+)");
	
	/**
	 * The eventPublisher to be passed to MochadX10Commands so that they can execute 
	 * postCommand.
	 */
	private EventPublisher eventPublisher;

	/**
	 * Constructor
	 * 
	 * @param eventPublisher	required to post an event on the openhab bus
	 */
	public MochadX10CommandParser(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	/**
	 * Parse the received message and create a corresponding MochadX10Command for it.
	 * 
	 * @param msg	The message received from the Mochad X10 host
	 * @return		A MochadX10Command if parsing was successful, otherwise null
	 */
	public  MochadX10Command parse(String msg) {	
		String command = null;
   		Matcher huc_matcher = HOUSEUNIT_COMMAND.matcher(msg);
		if (huc_matcher.matches()) {
			previousHouseCode = huc_matcher.group("houseCode").toLowerCase();
			previousUnitCode  = huc_matcher.group("unitCode");

			try { command = huc_matcher.group("command"); } catch (IllegalArgumentException e) {} 
			
			if (command != null) {
				return parseCommand(new MochadX10Address(previousHouseCode, previousUnitCode), command);
			}
			else {
				return null;
			}
		}
		
		Matcher hc_matcher  = HOUSE_COMMAND.matcher(msg);
		if (hc_matcher.matches()) {
			String houseCode = hc_matcher.group("houseCode").toLowerCase();
			if (houseCode.equals(previousHouseCode)) {
    			return parseCommand(new MochadX10Address(previousHouseCode, previousUnitCode), hc_matcher.group("command"));
			}
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return "MochadX10CommandParser [previousUnitCode=" + previousUnitCode
				+ ", previousHouseCode=" + previousHouseCode + "]";
	}

	/**
	 * Parse the command part of the received message.
	 * 
	 * @param address			The X10 address of the command
	 * @param commandString		The string containing the X10 command
	 * @return					A MochadX10Command if parsing was successful, otherwise null
	 */
	private MochadX10Command parseCommand(MochadX10Address address, String commandString) {
		if (commandString != null) {
			String command = commandString.toLowerCase();
			Matcher matcher;
			
			if (command.equals("on")) {
				return new MochadX10OnCommand(eventPublisher, address);
			}
			else if (command.equals("off")) {
				return new MochadX10OffCommand(eventPublisher, address);
			}
			else if (command.contains("dim")) {
				matcher = DIM_PATTERN.matcher(command);
				if (matcher.matches()) {
					int level = matcher.group("value") == null ? -1 : Integer.parseInt(matcher.group("value"));
					return new MochadX10DimCommand(eventPublisher, address, level);
				}
			}
			else if (command.contains("bright")) {
				matcher = BRIGHT_PATTERN.matcher(command);
				if (matcher.matches()) {
					int level = matcher.group("value") == null ? -1 : Integer.parseInt(matcher.group("value"));
					return new MochadX10BrightCommand(eventPublisher, address, level);
				}
			}
		}
		
		return null;
	}
}
