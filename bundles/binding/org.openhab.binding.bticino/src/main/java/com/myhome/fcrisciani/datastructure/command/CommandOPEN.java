/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.myhome.fcrisciani.datastructure.command;

/**
 * This is the base class of each type of command. It has all the base arguments
 * of each type of message. Keep in mind to extend this class everytime you
 * decide to create a new command class.
 * 
 * @author Flavio Crisciani
 * @serial 1.0
 * @since 1.7.0
 */
public class CommandOPEN {
	// ----- TYPES ----- //

	// ---- MEMBERS ---- //

	String commandString = null; // Command as a string
	int commandType = -1; // Type of the command
	String who = null; // Who field
	String where = null; // Where field

	// ---- METHODS ---- //
	/**
	 * Create a new command open instance
	 * 
	 * @param commandString
	 *            string representing the command
	 * @param commandType
	 *            type of the command
	 * @param who
	 *            who field of the command
	 * @param where
	 *            where field of the command
	 */
	public CommandOPEN(String commandString, int commandType, String who,
			String where) {
		super();
		this.commandString = commandString;
		this.commandType = commandType;
		this.who = who;
		this.where = where;
	}

	@Override
	public String toString() {
		return "CommandOPEN [commandString=" + commandString + "]";
	}

	/**
	 * Get the command as a string that can be executed
	 * 
	 * @return the command as a string
	 */
	public String getCommandString() {
		return commandString;
	}

	/**
	 * Get the who field
	 * 
	 * @return who
	 */
	public String getWho() {
		return who;
	}

	/**
	 * Get the where field
	 * 
	 * @return where
	 */
	public String getWhere() {
		return where;
	}

}
