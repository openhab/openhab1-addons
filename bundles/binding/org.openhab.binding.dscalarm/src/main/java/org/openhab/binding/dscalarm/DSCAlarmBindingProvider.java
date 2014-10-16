/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dscalarm;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * Binding provider interface for a DSC Alarm system
 * 
 * @author Russell Stephens
 * @since 1.6.0
 */
public interface DSCAlarmBindingProvider extends BindingProvider {

	/**
	 * Returns the binding configuration for the item with this name
	 * 
	 * @param itemName
	 * @return the binding configuration.
	 */
	public DSCAlarmBindingConfig getDSCAlarmBindingConfig(String itemName);

	/**
	 * Returns the {@link Item} with the specified item name
	 * 
	 * @param itemName
	 * @return item
	 */
	public Item getItem(String itemName);
}
