/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Satel module.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public interface SatelBindingProvider extends BindingProvider {

	/**
	 * Returns the {@link Item} with the specified item name. Returns null if
	 * the item was not found.
	 * 
	 * @param itemName
	 *            the name of the item.
	 * @return the item.
	 */
	Item getItem(String itemName);

	/**
	 * Returns the {@link SatelBindingConfig} for the specified item name.
	 * Returns null if the item was not found.
	 * 
	 * @param itemName
	 *            the name of the item.
	 * @return the binding configuration for the item.
	 */
	SatelBindingConfig getItemConfig(String itemName);
}
