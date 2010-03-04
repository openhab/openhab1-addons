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

import org.apache.commons.lang.ArrayUtils;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.ItemChangeListener;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemProvider;
import org.openhab.core.items.ItemRegistry;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemRegistryImpl implements ItemRegistry, ItemChangeListener {
	
	private static final Logger logger = LoggerFactory.getLogger(ItemRegistryImpl.class);

	/** if an EventPublisher service is available, we provide it to all items, so that they can communicate over the bus */
	protected EventPublisher eventPublisher;
	
	/** this is our local map in which we store all our items */
	protected Map<ItemProvider, GenericItem[]> itemMap = new HashMap<ItemProvider, GenericItem[]>();
	
	public void activate(ComponentContext componentContext) {
	}
	
	public void deactivate(ComponentContext componentContext) {
		// first remove ourself as a listener from the item providers
		for(ItemProvider provider : itemMap.keySet()) {
			provider.removeItemChangeListener(this);
		}
		// then release all items
		itemMap.clear();
	}
	
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
			GenericItem[] items = itemProvider.getItems();
			for(GenericItem item : items) {
				item.setEventPublisher(eventPublisher);
				item.initialize();
			}
			itemProvider.addItemChangeListener(this);
			itemMap.put(itemProvider, items);
			logger.debug("Item provider '{}' has been added.", itemProvider.getClass().getSimpleName());
		}
	}
	
	public void removeItemProvider(ItemProvider itemProvider) {
		if(itemMap.containsKey(itemProvider)) {
			for(GenericItem item : itemMap.get(itemProvider)) item.dispose();
			itemMap.remove(itemProvider);
			itemProvider.removeItemChangeListener(this);
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

	public void allItemsChanged(ItemProvider provider) {
		itemMap.put(provider, provider.getItems());
	}

	public void itemAdded(ItemProvider provider, GenericItem item) {
		GenericItem[] items = itemMap.get(provider);
		itemMap.put(provider, (GenericItem[]) ArrayUtils.add(items, item));
	}

	public void itemRemoved(ItemProvider provider, GenericItem item) {
		GenericItem[] items = itemMap.get(provider);
		itemMap.put(provider, (GenericItem[]) ArrayUtils.removeElement(items, item));
	}
}
