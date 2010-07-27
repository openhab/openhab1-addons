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

package org.openhab.core.internal.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.ItemChangeListener;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
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
	protected Map<ItemProvider, Item[]> itemMap = new HashMap<ItemProvider, Item[]>();
	
	/** to keep track of all item change listeners */
	protected Collection<ItemChangeListener> listeners = new HashSet<ItemChangeListener>();

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
	public Item getItem(String name) throws ItemNotFoundException, ItemNotUniqueException {
		Collection<Item> items = getItems(name);
		
		if(items.isEmpty()) {
			throw new ItemNotFoundException(name);
		}
		
		if(items.size()>1) {
			throw new ItemNotUniqueException(name, items);
		}
		
		return items.iterator().next();
		
	}
	
	/* (non-Javadoc)
	 * @see org.openhab.core.internal.items.ItemRegistry#getItems()
	 */
	public Collection<Item> getItems() {
		Collection<Item> allItems = new ArrayList<Item>();
		for(Item[] items : itemMap.values()) {
			allItems.addAll(Arrays.asList(items));
		}
		return allItems;
	}
	
	/* (non-Javadoc)
	 * @see org.openhab.core.internal.items.ItemRegistry#getItems(java.lang.String)
	 */
	public Collection<Item> getItems(String pattern) {
		String regex = pattern.replace("?", ".?").replace("*", ".*?");
		Collection<Item> matchedItems = new ArrayList<Item>();
		for(Item[] items : itemMap.values()) {
			for(Item item : items) {
				if(item.getName().matches(regex)) {
					matchedItems.add(item);
				}
			}
		}
		return matchedItems;
	}

	public void addItemProvider(ItemProvider itemProvider) {
		// only add this provider if it does not already exist
		if(!itemMap.containsKey(itemProvider)) {
			Item[] items = itemProvider.getItems();
			for(Item item : items) {
				if(isValidItemName(item.getName())) {
					if(item instanceof GenericItem) {
						GenericItem genericItem = (GenericItem) item;
						genericItem.setEventPublisher(eventPublisher);
						genericItem.initialize();
					}
				} else {
					logger.warn("Ignoring item '{}' as it does not comply with" +
							" the naming convention.", item.getName());
				}
			}
			itemProvider.addItemChangeListener(this);
			itemMap.put(itemProvider, items);
			logger.debug("Item provider '{}' has been added.", itemProvider.getClass().getSimpleName());
		}
	}
	
	public boolean isValidItemName(String name) {
		return name.matches("[a-zA-Z0-9_]*");
	}

	public void removeItemProvider(ItemProvider itemProvider) {
		if(itemMap.containsKey(itemProvider)) {
			for(Item item : itemMap.get(itemProvider)) {
				if(item instanceof GenericItem) {
					((GenericItem) item).dispose();
				}
			}
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
		for(ItemChangeListener listener : listeners) {
			listener.allItemsChanged(provider);
		}
	}

	public void itemAdded(ItemProvider provider, Item item) {
		Item[] items = itemMap.get(provider);
		itemMap.put(provider, (GenericItem[]) ArrayUtils.add(items, item));
		for(ItemChangeListener listener : listeners) {
			listener.itemAdded(provider, item);
		}
	}

	public void itemRemoved(ItemProvider provider, Item item) {
		Item[] items = itemMap.get(provider);
		itemMap.put(provider, (GenericItem[]) ArrayUtils.removeElement(items, item));
		for(ItemChangeListener listener : listeners) {
			listener.itemRemoved(provider, item);
		}
	}

	public void addItemChangeListener(ItemChangeListener listener) {
		listeners.add(listener);
	}

	public void removeItemChangeListener(ItemChangeListener listener) {
		listeners.remove(listener);
	}
}
