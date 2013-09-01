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

}
