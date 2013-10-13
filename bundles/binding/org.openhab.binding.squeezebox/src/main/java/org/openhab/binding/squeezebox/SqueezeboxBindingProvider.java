/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.squeezebox;

import org.openhab.binding.squeezebox.internal.PlayerCommandTypeMapping;
import org.openhab.core.binding.BindingProvider;


/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Squeezebox items.
 * 
 * Implementing classes should register themselves as a service in order to be 
 * taken into account.
 * 
 * @author Markus Wolters
 * @since 1.3.0
 */
public interface SqueezeboxBindingProvider extends BindingProvider {

	/**
	 * Returns the matching player command (associated to <code>itemName</code>
	 * and <code>comnand</code>) or <code>null</code> if no playerCommand could
	 * be found.
	 * 
	 * @param itemName the item for which to find a squeezeboxPlayerCommand
	 * @param command the openHAB command for which to find a squeezePlayerCommand
	 * 
	 * @return the matching squeezePlayerCommand or <code>null</code> if no matching
	 * squeezePlayerCommand could be found.
	 */
	String getPlayerCommand(String itemName, String command);
	
	/**
	 * Returns all Itemnames associated to <code>playerId</code> and <code>playerCommand</code> 
	 * 
	 * @param playerId the id of the player for which items should be returned
	 * @param playerCommand the player command for which items should be returned
	 * 
	 * @return the name of all items which are associated to <code>playerId</code>
	 * and <code>playerComannd</code>
	 */
	String[] getItemNamesByPlayerAndPlayerCommand(String playerId, PlayerCommandTypeMapping playerCommand);
	
	/**
	 * Returns all Playername associated to <code>itemName</code> and <code>command</code> 
	 * 
	 * @param itemName name of the item for which the player should be returned
	 * @param playerCommand the player command for which the player should be returned
	 * 
	 * @return the name of player which is associated to <code>itemName</code>
	 * and <code>command</code>
	 */
	String getPlayerByItemnameAndCommand(String itemName, String command);
	
}
