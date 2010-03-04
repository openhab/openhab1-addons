/* 
* openHAB, the open Home Automation Bus.
* Copyright 2010, openHAB.org
*
* See the contributors.txt file in the distribution for a
* full listing of individual contributors.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as
* published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package org.openhab.core.internal.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemProvider;
import org.openhab.core.items.ItemRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemRegistryImpl implements ItemRegistry {
	
	private static final Logger logger = LoggerFactory.getLogger(ItemRegistryImpl.class);

	/** if an EventPublisher service is available, we provide it to all items, so that they can communicate over the bus */
	protected EventPublisher eventPublisher;
	
	/** this is our local map in which we store all our items */
	protected Map<ItemProvider, GenericItem[]> itemMap = new HashMap<ItemProvider, GenericItem[]>();
	
	/* (non-Javadoc)
	 * @see org.openhab.core.internal.items.ItemRegistry#getItem(java.lang.String)
	 */
	public GenericItem getItem(String name) throws ItemNotFoundException {
		for(GenericItem[] items : itemMap.values()) {
			for(GenericItem item : items) {
				
				if(item.getName().equals(name)) return item;
			}
		}
		throw new ItemNotFoundException(name);
	}
	
	/* (non-Javadoc)
	 * @see org.openhab.core.internal.items.ItemRegistry#getItems()
	 */
	public Collection<Item> getItems() {
		Collection<Item> allItems = new ArrayList<Item>();
		for(GenericItem[] items : itemMap.values()) {
			allItems.addAll(Arrays.asList(items));
		}
		return allItems;
	}
	
	public void addItemProvider(ItemProvider itemProvider) {
		// only add this provider if it does not already exist
		if(!itemMap.containsKey(itemProvider)) {
			logger.debug("Item provider '{}' has been added.", itemProvider.getClass().getSimpleName());
			GenericItem[] items = itemProvider.getItems();
			for(GenericItem item : items) {
				item.setEventPublisher(eventPublisher);
				item.initialize();
			}
			itemMap.put(itemProvider, items);
		}
	}
	
	public void removeItemProvider(ItemProvider itemProvider) {
		if(itemMap.containsKey(itemProvider)) {
			for(GenericItem item : itemMap.get(itemProvider)) item.dispose();
			itemMap.remove(itemProvider);
			logger.debug("Item provider '{}' has been removed.", itemProvider.getClass().getSimpleName());
		}
	}
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
		for(Item item : getItems()) ((GenericItem)item).setEventPublisher(eventPublisher);
	}
	
	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
		for(Item item : getItems()) ((GenericItem)item).setEventPublisher(null);
	}
}
