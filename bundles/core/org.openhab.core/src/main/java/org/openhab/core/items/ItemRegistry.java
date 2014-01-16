/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	 * 
	 * @param name the item name
	 * @return the uniquely identified item
	 * @throws ItemNotFoundException if no item matches the input
	 */
	public Item getItem(String name) throws ItemNotFoundException;

	/**
	 * This method retrieves a single item from the registry.
	 * Search patterns and shortened versions are supported, if they uniquely identify an item
	 * 
	 * @param name the item name, a part of the item name or a search pattern
	 * @return the uniquely identified item
	 * @throws ItemNotFoundException if no item matches the input
	 * @throws ItemNotUniqueException if multiply items match the input
	 */
	public Item getItemByPattern(String name) throws ItemNotFoundException, ItemNotUniqueException;

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

	public void addItemRegistryChangeListener(ItemRegistryChangeListener listener);
	
	public void removeItemRegistryChangeListener(ItemRegistryChangeListener listener);

}
