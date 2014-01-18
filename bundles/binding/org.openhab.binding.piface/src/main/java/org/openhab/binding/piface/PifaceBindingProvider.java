/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.piface;

import java.util.List;

import org.openhab.binding.piface.internal.PifaceBindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and PiFace items.
 * 
 * Implementing classes should register themselves as a service in order to be 
 * taken into account.
 * 
 * @author Ben Jones
 * @since 1.3.0
 */
public interface PifaceBindingProvider extends BindingProvider {

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);

	/**
	 * Returns the binding config details associated with an <code>itemName</code>
	 * or <code>null</code> if it could not be found.
	 * 
	 */
	PifaceBindingConfig getPifaceBindingConfig(String itemName);
	
	
	/**
	 * Returns the list of <code>itemNames</code> associated with the
	 * specified pin (id, type, and number)
	 *
	 */
	List<String> getItemNames(String pifaceId, PifaceBindingConfig.BindingType bindingType, int pinNumber);
}
