/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.binding.sonos;

import java.util.List;

import org.openhab.binding.sonos.internal.Direction;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;


/**
 * @author Karel Goderis
 *
 */
public interface SonosBindingProvider extends BindingProvider {

	/**
	 * Returns a <code>List</code> of matching Sonos ids (associated to <code>itemName</code>
	 * 
	 * @param itemName
	 *            the item for which to find a VDR id
	 * 
	 * @return a List of matching Sonos ids or <code>null</code> if no matching Sonos id
	 *         could be found.
	 */
	public List<String> getSonosID(String itemName);
	
	/**
	 * Returns the matching Sonos id (associated to <code>itemName</code> and aCommand)
	 * 
	 * @param itemName
	 *            the item for which to find a VDR id
	 * 
	 * @return a List of matching Sonos ids or <code>null</code> if no matching Sonos id
	 *         could be found.
	 */
	public String getSonosID(String itemName, Command aCommand);

	/**
	 * Returns the matching Sonos command (associated to <code>itemName</code> and aCommand)
	 * 
	 * @param itemName
	 *            the item for which to find a VDR id
	 * 
	 * @return a List of matching Sonos ids or <code>null</code> if no matching Sonos id
	 *         could be found.
	 */
	public String getSonosCommand(String itemName, Command aCommand);
	
	/**
	 * Returns the matching direction  (associated to <code>itemName</code> and aCommand)
	 * 
	 * @param itemName
	 *            the item for which to find a VDR id
	 * 
	 * @return a List of matching Sonos ids or <code>null</code> if no matching Sonos id
	 *         could be found.
	 */
	public Direction getDirection(String itemName, Command aCommand);	

	public List<String> getItemNames(String sonosID, String sonosCommand);

	public List<Command> getCommands(String anItem);

	public List<Command> getVariableCommands(String itemName);
}
