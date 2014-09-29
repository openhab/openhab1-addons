/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave;

import org.openhab.core.autoupdate.AutoUpdateBindingProvider;
import org.openhab.core.items.Item;

/**
 * Binding provider interface. Defines the methods
 * to interact with the binding provider.
 * @author Victor Belov
 * @since 1.3.0
 */
public interface ZWaveBindingProvider extends AutoUpdateBindingProvider {
	/**
	 * Returns the binding configuration for the item with
	 * this name.
	 * @param itemName the name to get the binding configuration for.
	 * @return the binding configuration.
	 */
	public ZWaveBindingConfig getZwaveBindingConfig(String itemName);
	
	/**
	 * Returns the {@link Item} with the specified item name. Returns null
	 * if the item was not found.
	 * @param itemName the name of the item.
	 * @return the item.
	 */
	public Item getItem(String itemName);
}
