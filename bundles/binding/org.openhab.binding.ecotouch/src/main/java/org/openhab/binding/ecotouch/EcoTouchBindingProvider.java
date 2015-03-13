/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecotouch;

import org.openhab.binding.ecotouch.EcoTouchTags;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Sebastian Held <sebastian.held@gmx.de>
 * @since 1.5.0
 */
public interface EcoTouchBindingProvider extends BindingProvider {

	/**
	 * Provides an array of all item names of this provider for a given binding
	 * type
	 * 
	 * @param bindingType
	 *            the binding type of the items
	 * @return an array of all item names of this provider for the given binding
	 *         type
	 */
	public String[] getItemNamesForType(EcoTouchTags bindingType);

	/**
	 * Returns the type for an specific item.
	 * 
	 * @param itemName
	 *            the name of the item (set inside a *.items config file)
	 * @return type of this binding
	 */
	public EcoTouchTags getTypeForItemName(String itemName);

	/**
	 * Provides an array of all active tag names of this provider
	 * 
	 * @return an array of all active tag names of this provider
	 */
	public String[] getActiveTags();

	/**
	 * Provides an array of all active items of this provider
	 * 
	 * @return an array of all active items of this provider
	 */
	public EcoTouchTags[] getActiveItems();
}
