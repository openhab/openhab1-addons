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
							logger.warn("InstantiationException on ", e.getMessage()); // Should never happen
						} catch (IllegalAccessException e) {
							logger.warn("IllegalAccessException on ", e.getMessage()); // Should never happen
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
