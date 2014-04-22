/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic;

import java.util.List;

import org.openhab.binding.homematic.internal.config.binding.HomematicBindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * The interface to implement to provide a binding for Homematic.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public interface HomematicBindingProvider extends BindingProvider {

	/**
	 * Returns a list of items for the specified bindingConfig.
	 */
	public List<Item> getItemsFor(HomematicBindingConfig bindingConfig);

	/**
	 * Returns the item object by itemName.
	 */
	public Item getItem(String itemName);

	/**
	 * Returns the bindingConfig by itemName.
	 */
	public HomematicBindingConfig getBindingFor(String itemName);
}
