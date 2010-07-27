/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
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

package org.openhab.core.items;

import java.util.Collection;

/**
 * The ItemRegistry is the central place, where items are kept in memory and their state
 * is permanently tracked. So any code that requires the current state of items should use
 * this service (instead of trying to keep their own local copy of the items).
 * 
 * Items are registered by {@link ItemProvider}s, which can provision them from any source
 * they like and also dynamically remove or add items.
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public interface ItemRegistry {

	/**
	 * This method retrieves a single item from the registry.
	 * Search patterns and shortened versions are supported, if they uniquely identify an item
	 * 
	 * @param name the item name, a part of the item name or a search pattern
	 * @return the uniquely identified item
	 * @throws ItemNotFoundException if no item matches the input
	 * @throws ItemNotUniqueException if multiply items match the input
	 */
	public Item getItem(String name)
			throws ItemNotFoundException, ItemNotUniqueException;

	/**
	 * This method retrieves all items that are currently available in the registry
	 * 
	 * @return a collection of all available items
	 */
	public Collection<Item> getItems();

	/**
	 * This method retrieves all items that match a given search pattern
	 * 
	 * @return a collection of all items matching the search pattern
	 */
	public Collection<Item> getItems(String pattern);

	/**
	 * Checks whether itemName matches the item name conventions.
	 * Item names must only consist out of alpha-numerical characters and
	 * underscores (_).
	 * 
	 * @param itemName the item name to validate 
	 * @return true, if the name is valid
	 */
	public boolean isValidItemName(String itemName);

	public void addItemChangeListener(ItemChangeListener listener);
	
	public void removeItemChangeListener(ItemChangeListener listener);

}
