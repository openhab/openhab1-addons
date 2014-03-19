/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and tinkerforge devices.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Theo Weiss
 * @since 1.3.0
 */
public interface TinkerforgeBindingProvider extends BindingProvider {
	/**
	 * Returns the uid of the tinkerforge device identified by {@code itemName}.
	 * 
	 * @param itemName
	 *            the name of the Item to find the tinkerforge uid for.
	 * @return The uid of the device identified by {@code itemName} as String.
	 */
	public String getUid(String itemName);

	/**
	 * Returns the subid of the tinkerforge device identified by
	 * {@code itemName}.
	 * 
	 * @param itemName
	 *            the name of the Item to find the tinkerforge subid for.
	 * @return The subid of the device identified by {@code itemName} as String
	 *         or <code>null</code> if it is not a subdevice.
	 */
	public String getSubId(String itemName);

	/**
	 * Returns the name of the tinkerforge device identified by {@code itemName}
	 * . The name is only available if there is a configuration entry in the
	 * openhab.cfg configuration file.
	 * 
	 * @param itemName
	 *            the name of the Item to find the device name for.
	 * @return The name of the device identified by {@code itemName} as String
	 *         or <code>null</code> if the device is unnamed.
	 */
	public String getName(String itemName);

	/**
	 * Returns the Item identified by {@code itemName}.
	 * 
	 * @param itemName
	 *            the name of the Item.
	 * @return The Item identified by {@code itemName}.
	 * 
	 */
	public Item getItem(String itemName);

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	public Class<? extends Item> getItemType(String itemName);

}
