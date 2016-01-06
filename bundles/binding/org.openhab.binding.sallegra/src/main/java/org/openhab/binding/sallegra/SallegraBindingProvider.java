/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sallegra;

import org.openhab.binding.sallegra.internal.SallegraBindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author Benjamin Marty (Developed on behalf of Satelco.ch)
 * @since 1.8.0
 */
public interface SallegraBindingProvider extends BindingProvider {
	/**
	 * Lookup method for binding configs
	 * 
	 * @param itemName
	 *            name of the item
	 * @return the binding config for the item with the name itemName
	 */
	public SallegraBindingConfig getBindingConfigFor(String itemName);

	/**
	 * Returns the class for the item
	 * 
	 * @param name
	 *            name of the item
	 * @return the class of the item with the name itemName
	 */
	public Class<? extends Item> getItemType(String name);
}
