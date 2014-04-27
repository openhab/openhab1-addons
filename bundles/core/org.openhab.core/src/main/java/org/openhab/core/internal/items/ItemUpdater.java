/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.internal.items;

import org.openhab.core.events.AbstractEventSubscriber;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ItemUpdater listens on the event bus and passes any received status update
 * to the item registry.
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public class ItemUpdater extends AbstractEventSubscriber {

	private static final Logger logger = LoggerFactory.getLogger(ItemUpdater.class);
	
	protected ItemRegistry itemRegistry;
	
	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void receiveUpdate(String itemName, State newStatus) {
		if (itemRegistry != null) {
			try {
				GenericItem item = (GenericItem) itemRegistry.getItem(itemName);
				boolean isAccepted = false;
				if (item.getAcceptedDataTypes().contains(newStatus.getClass())) {
					isAccepted = true;
				} else {
					// Look for class hierarchy
					for (Class<? extends State> state : item.getAcceptedDataTypes()) {
						try {
							if (!state.isEnum() && state.newInstance().getClass().isAssignableFrom(newStatus.getClass())) {
								isAccepted = true;
								break;
							}
						} catch (InstantiationException e) {
							logger.warn("InstantiationException on {}", e.getMessage()); // Should never happen
						} catch (IllegalAccessException e) {
							logger.warn("IllegalAccessException on {}", e.getMessage()); // Should never happen
						}
					}
				}				
				if (isAccepted) {
					item.setState(newStatus);
				} else {
					logger.debug("Received update of a not accepted type ("	+ newStatus.getClass().getSimpleName() + ") for item " + itemName);
				}
			} catch (ItemNotFoundException e) {
				logger.debug("Received update for non-existing item: {}", e.getMessage());
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void receiveCommand(String itemName, Command command) {	
		// if the item is a group, we have to pass the command to it as it needs to pass the command to its members
		if(itemRegistry!=null) {
			try {
				Item item = itemRegistry.getItem(itemName);
				if (item instanceof GroupItem) {
					GroupItem groupItem = (GroupItem) item;
					groupItem.send(command);
				}
			} catch (ItemNotFoundException e) {
				logger.debug("Received command for non-existing item: {}", e.getMessage());
			}
		}
	}

}
