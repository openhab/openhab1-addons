/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.freeswitch;

import org.openhab.binding.freeswitch.internal.FreeswitchBindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * Users of this class can map openHAB items to Freeswitch binding types
 * @author Dan Cunningham
 * @since 1.4.0
 */
public interface FreeswitchBindingProvider extends BindingProvider {
	/**
	 * returns the item with the given item name
	 * @param itemName
	 * @return
	 */
	public Item getItem(String itemName);
	/**
	 * return the binding config for an item 
	 * @param itemName
	 * @return
	 */
	public FreeswitchBindingConfig getFreeswitchBindingConfig(String itemName);

}
