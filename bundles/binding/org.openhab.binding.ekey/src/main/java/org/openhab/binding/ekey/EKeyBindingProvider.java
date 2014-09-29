/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekey;

import org.openhab.binding.ekey.internal.EKeyBindingConfig.InterestType;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and eKey data
 * 
 * It provides the type of information that an item is interested in
 * and the datatype of the item itself
 * @author Paul Schlagitweit
 * @since 1.5.0
 */
public interface EKeyBindingProvider extends BindingProvider {
	
	/**
	 * Gets the interest that a specific item subscribed
	 * @param itemName
	 * @return
	 */
	public InterestType getItemInterest(String itemName);
	
	/**
	 * Determines whether the item is a NumberItem or a StringItem
	 * @param itemName
	 * @return
	 */
	Class<? extends Item> getItemType(String itemName);
	
	
	
	
}
