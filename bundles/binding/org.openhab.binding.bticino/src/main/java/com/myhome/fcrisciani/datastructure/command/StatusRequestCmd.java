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
 * This class represent a Status/Request command in the OpenWebNet For further
 * details check the OpenWebNet reference
 * 
 * @author Flavio Crisciani
 * @serial 1.0
 * @since 1.7.0
 */
public class StatusRequestCmd extends CommandOPEN {
	// ----- TYPES ----- //

	// ---- MEMBERS ---- //

	// ---- METHODS ---- //

	/**
	 * Create a new instance of a StatusRequestCommand
	 * 
	 * @param commandString
	 *            command as a string
	 * @param who
	 *            who field
	 * @param where
	 *            where field
	 */
	public StatusRequestCmd(String commandString, String who, String where) {
		super(commandString, 1, who, where);
		;
	}

	/**
	 * Generate automatically a new Status Request Command passing basic
	 * argument
	 * 
	 * @param who
	 *            who field
	 * @param where
	 *            where field
	 */
	public StatusRequestCmd(String who, String where) {
		super("*#" + who + "*" + where + "##", 1, who, where);
		;
	}

}
