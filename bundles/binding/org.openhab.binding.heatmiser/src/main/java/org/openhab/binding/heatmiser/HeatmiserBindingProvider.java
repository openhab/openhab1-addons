/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.heatmiser;

import java.util.List;

import org.openhab.binding.heatmiser.internal.thermostat.HeatmiserThermostat.Functions;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * Heatmiser binding provider
 * 
 * @author Chris Jackson
 * @since 1.3.0
 */
public interface HeatmiserBindingProvider extends BindingProvider {
	/**
	 * Returns a list of all items at the specified thermostat address
	 * 
	 * @param addr
	 *            Thermostat address
	 * @return List<String> of items
	 */
	public List<String> getBindingItemsAtAddress(int addr);

	/**
	 * Get the thermostat function linked to the thermostat item.
	 * 
	 * @param itemName
	 *            The item to which the function is required
	 * @return The Function
	 */
	public Functions getFunction(String itemName);

	/**
	 * Get the thermostat address associated with a specified item name
	 * 
	 * @param itemName
	 *            The item whose thermostat address is required
	 * @return The Heatmiser network thermostat address
	 */
	public int getAddress(String itemName);

	/**
	 * Get the item type for the specified item
	 * 
	 * @param itemName
	 *            The item whose type is required
	 * @return The openHAB class type
	 */
	public Class<? extends Item> getItemType(String itemName);
}
