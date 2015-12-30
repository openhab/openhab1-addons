/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus;

import java.util.List;

import org.openhab.core.binding.BindingProvider;

/**
* This interface can parse information from the binding format and provides eBus
* binding informations.
* 
* @author Christian Sowada
* @since 1.7.0
*/
public interface EBusBindingProvider extends BindingProvider {
	
	public String getSet(String itemName);
	
	/**
	 * Return the configurated command for this item or null if not set
	 * @param itemName The openhab item name
	 * @return The value or null
	 */
	public String getCommand(String itemName);
	
	/**
	 * Return the configurated id for this item or null if not set
	 * @param itemName The openhab item name
	 * @return The value or null
	 */
	public String getId(String itemName);

	/**
	 * Return the item name for the ebus id
	 * @param id The ebus id (see ebus-configuration.json)
	 * @return The openhab item names
	 */
	public List<String> getItemNames(String id);
	
	/**
	 * Return the refresh rate for a polling commands
	 * @param itemName
	 * @return The value or null
	 */
	public int getRefreshRate(String itemName);
	
	/**
	 * Return the byte data to send
	 * @param itemName The itemName
	 * @return data or null
	 */
	public byte[] getTelegramData(String itemName);
	
	/**
	 * Return the byte data to send for a type
	 * @param itemName
	 * @param type
	 * @return
	 */
	public byte[] getTelegramData(String itemName, String type);
	
	/**
	 * Return the configurated destination for this item or null if not set
	 * @param itemName
	 * @return
	 */
	public Byte getTelegramDestination(String itemName);
	
	/**
	 * Return the configurated source for this item or null if not set
	 * @param itemName
	 * @return
	 */
	public Byte getTelegramSource(String itemName);
}
