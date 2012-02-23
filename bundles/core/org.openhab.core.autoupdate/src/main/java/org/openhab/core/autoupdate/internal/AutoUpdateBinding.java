/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.core.autoupdate.internal;

import org.openhab.core.autoupdate.AutoUpdateBindingProvider;
import org.openhab.core.events.AbstractEventSubscriberBinding;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * <p>The AutoUpdate-Binding is no 'normal' binding as it doesn't connect any hardware
 * to openHAB. In fact it takes care of updating the State of an item with respect
 * to the received command automatically or not. By default the State is getting
 * updated automatically which is desired behavior in most of the cases. However
 * it could be useful to disable this default behavior.</p>
 * <p>For example when implementing validation steps before changing a State one
 * needs to control the State update oneself.</p>
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.1
 */
public class AutoUpdateBinding extends AbstractEventSubscriberBinding<AutoUpdateBindingProvider> {

	private static final Logger logger = LoggerFactory.getLogger(AutoUpdateBinding.class);
	
	protected ItemRegistry itemRegistry;
	
	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = null;
	}
	

	/**
	 * <p>Iterates through all registered {@link AutoUpdateBindingProvider}s and
	 * checks whether an autoupdate configuration is available for <code>itemName</code>.</p>
	 * 
	 * <p>If there are more then one {@link AutoUpdateBindingProvider}s providing
	 * a configuration the results are combined by a logical <em>OR</em>. If no
	 * configuration is provided at all the autoupdate defaults to <code>true</code>
	 * and an update is posted for the corresponding {@link State}.</p> 
	 * 
	 * @param itemName the item for which to find an autoupdate configuration
	 * @param command the command being received and posted as {@link State}
	 * update if <code>command</code> is instance of {@link State} as well.
	 */
	@Override
	public void receiveCommand(String itemName, Command command) {
		Boolean autoUpdate = null;
		for (AutoUpdateBindingProvider provider : providers) {
			Boolean au = provider.autoUpdate(itemName);
			if (au != null) {
				autoUpdate = au;
				if (Boolean.TRUE.equals(autoUpdate)) {
					break;
				}
			}
		}
		
		// we didn't find any autoupdate configuration, so apply the default now
		if (autoUpdate == null) {
			autoUpdate = Boolean.TRUE;
		}
		
		if (autoUpdate && command instanceof State) {
			postUpdate(itemName, (State) command);
		} else {
			logger.trace("Item '{}' is not configured to update its state automatically.", itemName);
		}
	}

	private void postUpdate(String itemName, State newStatus) {
		if (itemRegistry != null) {
			try {
				GenericItem item = (GenericItem) itemRegistry.getItem(itemName);
				if (item.getAcceptedDataTypes().contains(newStatus.getClass())) {
					item.setState(newStatus);
					logger.trace("Received update for item {}: {}", itemName, newStatus.toString());
				} else {
					logger.debug("Received update of a not accepted type ({}) for item {}", newStatus.getClass().getSimpleName(), itemName);
				}
			} catch (ItemNotFoundException e) {
				logger.debug("Received update for non-existing item: {}", e.getMessage());
			} catch (ItemNotUniqueException e) {
				logger.debug("Received update for a not uniquely identifiable item: {}", e.getMessage());
			}
		}
	}
	
	
}
