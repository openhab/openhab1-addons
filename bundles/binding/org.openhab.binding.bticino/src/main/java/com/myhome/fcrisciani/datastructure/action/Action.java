/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.myhome.fcrisciani.datastructure.action;

import java.util.ArrayList;

import com.myhome.fcrisciani.datastructure.command.CommandOPEN;

/**
 * The Action is defined as a list of command that have to be execute on the
 * MyHome plant They can have a list of identifier of sensors that can inhibit
 * the action execution.
 * 
 * @author Flavio Crisciani
 * @serial 1.0
 * @since 1.7.0
 */
public class Action {
	// ----- TYPES ----- //

	// ---- MEMBERS ---- //

	private ArrayList<CommandOPEN> resetCommandList = null; // List of
															// OpenWebNet
															// Commands to
															// execute
	private ArrayList<CommandOPEN> preDelayCommandList = null; // List of
																// OpenWebNet
																// Commands to
																// execute
	private CommandOPEN delayCommand = null; // List of OpenWebNet Commands to
												// execute
	private ArrayList<CommandOPEN> postDelayCommandList = null; // List of
																// OpenWebNet
																// Commands to
																// execute

	private boolean commandWithDelay = false; // Indicates if this action
												// contains a delay as a command
	private Integer[] sensorIdInhibitionList = null; // List of sensors id that
														// can inhibit the
														// Action execution
	private String description = null; // Description associated to this action
	private int commandPriority = 3; // Priority associated to the commands of
										// this Action

	// ---- METHODS ---- //
	/**
	 * Create an Action specifying its description and the list of sensors to
	 * check before its execution
	 * 
	 * @param description
	 *            string to describe the action
	 * @param sensorIdInibitionList
	 *            list of sensors ids
	 */
	public Action(String description, Integer[] sensorIdInibitionList) {
		super();
		this.resetCommandList = new ArrayList<CommandOPEN>();
		this.preDelayCommandList = new ArrayList<CommandOPEN>();
		this.postDelayCommandList = new ArrayList<CommandOPEN>();
		this.description = description;
		this.sensorIdInhibitionList = sensorIdInibitionList;
	}

	/**
	 * Create an Action specifying its description, the list of sensors to check
	 * before its execution and the command priority
	 * 
	 * @param description
	 *            string to describe the action
	 * @param sensorIdInibitionList
	 *            list of sensors ids
	 * @param priority
	 *            queue priority {1 = HIGH, 2 = MEDIUM, 3 = LOW}
	 */
	public Action(String description, Integer[] sensorIdInibitionList,
			int priority) {
		this(description, sensorIdInibitionList);
		this.commandPriority = priority;
	}

	/**
	 * Add a command on top of the list, i.e. to reset the actuator state
	 * 
	 * @param resetCommandList
	 *            command to put on front of the command list
	 */
	public void addResetCommandProcedure(ArrayList<CommandOPEN> resetCommandList) {
		this.resetCommandList.addAll(resetCommandList);
	}

	/**
	 * Add a command to this action that is executed before the delay
	 * 
	 * @param command
	 *            command to put on front of the command list
	 */
	public void addPreDelayCommand(CommandOPEN command) {
		preDelayCommandList.add(command);
	}

	/**
	 * Add a command to this action that is executed before the delay
	 * 
	 * @param command
	 *            command to put on front of the command list
	 */
	public int getPreDelayCommandListLength() {
		return preDelayCommandList.size();
	}

	/**
	 * Add a delay to this action
	 * 
	 * @param command
	 *            command to put on front of the command list
	 */
	public void addDelayCommand(CommandOPEN command) {
		commandWithDelay = true;
		delayCommand = command;
	}

	/**
	 * Add a command to this action that is executed after the delay
	 * 
	 * @param command
	 *            command to put on front of the command list
	 */
	public void addPostDelayCommand(CommandOPEN command) {
		postDelayCommandList.add(command);
	}

	/**
	 * Get the list of command of this Action
	 * 
	 * @return list of command
	 */
	public ArrayList<CommandOPEN> getCommandList() {
		ArrayList<CommandOPEN> returnList = new ArrayList<CommandOPEN>();
		if (resetCommandList != null) {
			returnList.addAll(resetCommandList);
		}
		returnList.addAll(preDelayCommandList);
		if (commandWithDelay) {
			returnList.add(delayCommand);
			returnList.addAll(postDelayCommandList);
		}

		return returnList;
	}

	/**
	 * Get the list of Sensor id to be checked
	 * 
	 * @return list of sensor id
	 */
	public Integer[] getSensorIdInibitionList() {
		return sensorIdInhibitionList;
	}

	/**
	 * Get Action description
	 * 
	 * @return Action description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get Action command priority
	 * 
	 * @return command priority set
	 */
	public int getCommandPriority() {
		return commandPriority;
	}

	/**
	 * Check if this Action has a delay command in the list of commands
	 * 
	 * @return true if the Action has a delay in the command list
	 */
	public boolean isCommandWithDelay() {
		return commandWithDelay;
	}

	@Override
	public String toString() {
		StringBuilder resultString = new StringBuilder();
		resultString.append("Action: " + description + " [commandList:");
		for (CommandOPEN command : getCommandList()) {
			if (command != null) {
				resultString.append(command);
			} else {
				resultString.append("null");
			}
		}
		resultString.append("]");

		return resultString.toString();
	}

}
