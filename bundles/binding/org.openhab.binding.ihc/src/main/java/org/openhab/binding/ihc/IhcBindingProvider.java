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
package org.openhab.binding.ihc;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and IHC / ELKO LS resource items.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Pauli Anttila
 * @since 1.1.0
 */
public interface IhcBindingProvider extends BindingProvider {

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);
	
	/**
	 * Returns the resource id to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a resource id.
	 * 
	 * @return the corresponding Resource Id to the given <code>itemName</code>.
	 */
	public int getResourceId(String itemName);

	/**
	 * Returns the refresh interval to the given <code>itemName</code>. Is used
	 * by IHC-In-Binding.
	 * 
	 * @param itemName
	 *            the item for which to find a refresh interval.
	 * 
	 * @return the matching refresh interval or <code>null</code> if no
	 *         matching. refresh interval could be found.
	 */
	public int getRefreshInterval(String itemName);

	/**
	 * Returns item binding mode to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a binding mode.
	 * 
	 * @return true if item is out binding only.
	 */
	public boolean isOutBindingOnly(String itemName);

}
