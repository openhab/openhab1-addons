/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios.internal.model.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemFactory;
import org.openhab.core.items.ItemProvider;
import org.openhab.core.items.ItemsChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiosItemProvider implements ItemProvider {

	private static final Logger logger = LoggerFactory
			.getLogger(MiosItemProvider.class);

	// Keep track of the listeners, so they can be notified of changes.
	private Collection<ItemsChangeListener> listeners = new HashSet<ItemsChangeListener>();

	private Collection<ItemFactory> itemFactories = new ArrayList<ItemFactory>();

	@Override
	public Collection<Item> getItems() {
		logger.debug("getItems");

		Item test = createItemOfType("StringItem", "BIGTest");
		List<Item> items = new ArrayList<Item>();
		items.add(test);

		// TODO: Populate this with content from the MiOS Units under
		// management.
		return items;
	}

	@Override
	public void addItemChangeListener(ItemsChangeListener listener) {
		logger.debug("addItemChangeListener: added listener {}", listener);
		listeners.add(listener);
	}

	@Override
	public void removeItemChangeListener(ItemsChangeListener listener) {
		logger.debug("removeItemChangeListener: removed listener {}", listener);
		listeners.remove(listener);
	}
	
	/**
	 * Add another instance of an {@link ItemFactory}. Used by Declarative Services.
	 * 
	 * @param factory The {@link ItemFactory} to add.
	 */
	public void addItemFactory(ItemFactory factory) {
		itemFactories.add(factory);
		dispatchBindingsPerItemType(null, factory.getSupportedItemTypes());
	}
	
	/**
	 * Removes the given {@link ItemFactory}. Used by Declarative Services.
	 * 
	 * @param factory The {@link ItemFactory} to remove.
	 */
	public void removeItemFactory(ItemFactory factory) {
		itemFactories.remove(factory);
	}

	/**
	 * Creates a new item of type {@code itemType} by utilizing an appropriate
	 * {@link ItemFactory}.
	 * 
	 * @param itemType
	 *            The type to find the appropriate {@link ItemFactory} for.
	 * @param itemName
	 *            The name of the {@link Item} to create.
	 * 
	 * @return An Item instance of type {@code itemType}.
	 */
	private GenericItem createItemOfType(String itemType, String itemName) {
		if (itemType == null) {
			return null;
		}

		for (ItemFactory factory : itemFactories) {
			GenericItem item = factory.createItem(itemType, itemName);
			if (item != null) {
				logger.trace("Created item '{}' of type '{}'", itemName,
						itemType);
				return item;
			}
		}

		logger.debug("Couldn't find ItemFactory for item '{}' of type '{}'",
				itemName, itemType);
		return null;
	}
}
