/**
 * Copyright (c) 2010-2013, openHAB.org and others.
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


/**
 * @author Karel Goderis
 * @since 1.1.0
 *
 */
public interface SonosBindingProvider extends BindingProvider {

	/**
	 * Returns a <code>List</code> of matching Sonos ids (associated to <code>itemName</code>
	 * 
	 * @param itemName
	 *            the item for which to find a Sonos id
	 * 
	 * @return a List of matching Sonos ids or <code>null</code> if no matching Sonos id
	 *         could be found.
	 */
	public List<String> getSonosID(String itemName);
	
	/**
	 * Returns the matching Sonos id (associated to <code>itemName</code> and aCommand)
	 * 
	 * @param itemName
	 *            the item for which to find a Sonos id
	 * 
	 * @return a List of matching Sonos ids or <code>null</code> if no matching Sonos id
	 *         could be found.
	 */
	public String getSonosID(String itemName, Command aCommand);

	/**
	 * Returns the matching Sonos command (associated to <code>itemName</code> and aCommand)
	 * 
	 * @param itemName
	 *            the item for which to find a Sonos id
	 * 
	 * @return a List of matching Sonos ids or <code>null</code> if no matching Sonos id
	 *         could be found.
	 */
	public SonosCommandType getSonosCommandType(String itemName, Command aCommand, Direction direction);
	
	/**
	 * Returns the matching direction  (associated to <code>itemName</code> and aCommand)
	 * 
	 * @param itemName
	 *            the item for which to find a Sonos id
	 * 
	 * @return a List of matching Sonos ids or <code>null</code> if no matching Sonos id
	 *         could be found.
	 */
	public Direction getDirection(String itemName, Command aCommand);	

	/**
	 * Returns the list of items  (associated to <code>sonosID</code> and a Sonos Command Type)
	 * 
	 */
	public List<String> getItemNames(String sonosID, SonosCommandType sonosCommandType);
	
	/**
	 * Returns the list of Commands  (associated to an <code>Item</code> and a Sonos Command Type)
	 * 
	 */
	public List<Command> getCommands(String anItem,SonosCommandType sonosCommandType);

	/**
	 * Returns the list of Commands that are linked to variables/updates (associated to an <code>Item</code>)
	 * 
	 */
	public List<Command> getVariableCommands(String itemName);
}
