/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mpd;

import org.openhab.binding.mpd.internal.PlayerCommandTypeMapping;
import org.openhab.core.binding.BindingProvider;


/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and MPD items.
 * 
 * Implementing classes should register themselves as a service in order to be 
 * taken into account.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.8.0
 */
public interface MpdBindingProvider extends BindingProvider {

	/**
	 * Returns the matching player command (associated to <code>itemName</code>
	 * and <code>comnand</code>) or <code>null</code> if no playerCommand could
	 * be found.
	 * 
	 * @param itemName the item for which to find a mpdPlayerCommand
	 * @param command the openHAB command for which to find a mpdPlayerCommand
	 * 
	 * @return the matching mpdPlayerCommand or <code>null</code> if no matching
	 * mpdPlayerCommand could be found.
	 */
	String getPlayerCommand(String itemName, String command);
	
	/**
	 * Returns all Items associated to <code>playerId</code> and <code>playerCommand</code> 
	 * 
	 * @param playerId the id of the player for which items should be returned
	 * @param playerCommand the player command for which items should be returned
	 * 
	 * @return the name of all items which are associated to <code>playerId</code>
	 * and <code>playerComannd</code>
	 */
	String[] getItemNamesByPlayerAndPlayerCommand(String playerId, PlayerCommandTypeMapping playerCommand);

}
