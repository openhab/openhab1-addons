/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
