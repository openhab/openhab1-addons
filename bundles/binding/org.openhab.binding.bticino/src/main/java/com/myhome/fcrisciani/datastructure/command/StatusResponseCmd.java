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
 * This class represent a Status response command in the OpenWebNet For further
 * details check the OpenWebNet reference
 * 
 * @author Flavio Crisciani
 * @serial 1.0
 * @since 1.7.0
 */
public class StatusResponseCmd extends CommandOPEN {

	// ----- TYPES ----- //

	// ---- MEMBERS ---- //

	String what = null; // What field of the standard

	// ---- METHODS ---- //
	/**
	 * Create a new instance of a Status response command
	 * 
	 * @param commandString
	 *            command as a string
	 * @param who
	 *            who field
	 * @param what
	 *            what field
	 * @param where
	 *            where field
	 */
	public StatusResponseCmd(String commandString, String who, String what,
			String where) {
		super(commandString, 2, who, where);
		this.what = what;
	}

	/**
	 * Generate automatically a new Status Response Command passing basic
	 * argument
	 * 
	 * @param who
	 *            who field
	 * @param what
	 *            what field
	 * @param where
	 *            where field
	 */
	public StatusResponseCmd(String who, String what, String where) {
		super("*" + who + "*" + what + "*" + where + "##", 2, who, where);
		this.what = what;
	}

	/**
	 * Returns what field
	 * 
	 * @return what field
	 */
	public String getWhat() {
		return what;
	}
}
