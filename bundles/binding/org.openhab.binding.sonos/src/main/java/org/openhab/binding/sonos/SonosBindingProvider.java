/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sonos;

import java.util.List;

import org.openhab.binding.sonos.internal.Direction;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;


/**
 * @author Karel Goderis
 * @since 1.1.0
 *
 */
public interface SonosBindingProvider extends BindingProvider {

	/**
	 * Returns a <code>List</code> of matching Sonos ids (associated to <code>itemName</code>)
	 * 
	 * @param itemName
	 *            the item for which to find a Sonos id
	 * 
	 * @return a List of matching Sonos ids or <code>null</code> if no matching Sonos id
	 *         could be found.
	 */
	public List<String> getSonosID(String itemName);
	
	/**
	 * Returns the matching Sonos id (associated to an <code>Item</code> and a Command)
	 * 
	 */
	public String getSonosID(String itemName, Command aCommand);

	/**
	 * Returns the list of accepted DataTypes for the given item
	 * 
	 */
	public  List<Class<? extends State>> getAcceptedDataTypes(String itemName);

	/**
	 * Returns the list of items  (associated to <code>sonosID</code> and a Sonos command )
	 * 
	 */
	public List<String> getItemNames(String sonosID, String sonosCommand);
	
	/**
	 * Returns the list of items  (associated to a Sonos command)
	 * 
	 */
	public List<String> getItemNames(String sonosCommand);
	
	/**
	 * Returns the list of Commands  (associated to an <code>Item</code> and a Sonos Command Type)
	 * 
	 */
	public List<Command> getCommands(String anItem, String sonosCommand);

	/**
	 * Returns the sonos command (associated to an <code>Item</code> and a Command )
	 * 
	 */
	public String getSonosCommand(String anItem, Command aCommand);

	/**
	 * Returns the list of Commands that are linked to variables/updates (associated to an <code>Item</code>)
	 * 
	 */
	public List<Command> getVariableCommands(String itemName);
	
}
