/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.comfoair;

import java.util.List;

import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and ComfoAir items.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Holger Hees
 * @since 1.3.0
 */
public interface ComfoAirBindingProvider extends BindingProvider {

	/**
	 * @return all configured binding keys
	 */
	List<String> getConfiguredKeys();

	/**
	 * @param key
	 * @return all items which are configured with this command key
	 */
	List<String> getItemNamesForCommandKey(String key);

	/**
	 * @param itemName
	 * @return configured commandKey for this item
	 */
	String getConfiguredKeyForItem(String itemName);
	
}
