/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.autoupdate.internal;

import org.openhab.core.autoupdate.AutoUpdateBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.ItemNotFoundException;
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
public class AutoUpdateBinding extends AbstractBinding<AutoUpdateBindingProvider> {

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
			logger.trace("Won't update item '{}' as it is not configured to update its state automatically.", itemName);
		}
	}

	private void postUpdate(String itemName, State newStatus) {
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
	
}
