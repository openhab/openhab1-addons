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
import org.openhab.core.items.ItemRegistry;

/**
 * openHAB Binding Provider interface for MiOS Devices.
 * 
 * Defines how to get properties from a MiOS-specific binding configuration.
 * 
 * @author Mark Clark
 * @since 1.6.0
 */
public interface MiosBindingProvider extends BindingProvider {
	/**
	 * Gets the MiOS Unit for the Item {@code itemName}.
	 * 
	 * @param itemName
	 *            the name of the Item.
	 * 
	 * @return the name of the MiOS Unit associated with the the Item
	 *         {@code itemName}
	 */
	String getMiosUnitName(String itemName);

	/**
	 * Gets the Binding property string for the Item {@code itemName}.
	 * 
	 * @param itemName
	 *            the name of the Item.
	 * 
	 * @return the property string component for the Binding on the Item
	 *         {@code itemName}
	 */
	String getProperty(String itemName);

	public List<String> getItemsForProperty(String property);

	/**
	 * Gets the ItemRegistry (catalog) used by this BindingProvider.
	 * 
	 * The {@code ItemRegistry} is injected into the {@code MiosBindingProvider}
	 * through OSGi configuration. This method provider read-only access to the
	 * {@code ItemRegistry}, which is the catalog of Items in use by this
	 * system.
	 * 
	 * @return the ItemRegistry associated with this BindingProvider.
	 */
	public ItemRegistry getItemRegistry();
}
