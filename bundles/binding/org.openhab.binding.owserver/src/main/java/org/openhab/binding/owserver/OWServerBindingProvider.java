/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.owserver;

import java.util.List;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;


/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and the EDS OWServer.
 * 
 * @author Chris Jackson
 * @since 1.3.0
 */
public interface OWServerBindingProvider extends BindingProvider {

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);
	
	/**
	 * Return the IP serverId for OWServer linked to the item
	 * 
	 * @param itemName the item for which to find the serverId
	 */
	String getServerId(String itemName);

	/**
	 * Return the One Wire ROM ID for the item (linked to the parameter)
	 * 
	 * @param itemName the item for which to find a ROM ID
	 */
	String getRomId(String itemName);

	/**
	 * Return the parameter 'name' for this item. The variable 'name' is the OWServer XML parameter
	 * used for the item. This is linked to the ROM ID.
	 * 
	 * @param itemName the item for which to find the name
	 */
	String getName(String itemName);
	
	/**
	 * Returns the refresh interval to use according to <code>itemName</code>.
	 * Is used by OWServer-In-Binding.
	 *  
	 * @param itemName the item for which to find a refresh interval
	 * 
	 * @return the matching refresh interval or <code>null</code> if no matching
	 * refresh interval could be found.
	 */
	int getRefreshInterval(String itemName);
	
	/**
	 * Returns all items which are mapped to a OWServer-In-Binding
	 * @return item which are mapped to a OWServer-In-Binding
	 */
	List<String> getInBindingItemNames();
}
