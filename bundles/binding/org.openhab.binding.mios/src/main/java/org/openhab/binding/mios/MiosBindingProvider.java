/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios;

import java.util.List;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * Binding provider interface. Defines how to get properties from a binding
 * configuration.
 * 
 * @author Mark Clark
 * @since 1.6.0
 */
public interface MiosBindingProvider extends BindingProvider {
	String getMiosUnitName(String itemName);

	String getProperty(String itemName);

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);

	public List<String> getItemsForProperty(String property);
}
