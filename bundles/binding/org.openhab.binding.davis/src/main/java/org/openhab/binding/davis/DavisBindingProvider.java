/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.davis;

import java.util.List;

import org.openhab.core.binding.BindingProvider;

/**
 * @author Trathnigg Thomas
 * @since 1.6.0
 */
public interface DavisBindingProvider extends BindingProvider {

	/**
	 * @return all configured binding keys
	 */
	List<String> getConfiguredKeys();

	/**
	 * @param key
	 * @return all items which are configured with this command key
	 */
	List<String> getItemNamesForKey(String key);

	/**
	 * @param itemName
	 * @return configured commandKey for this item
	 */
	String getConfiguredKeyForItem(String itemName);
	
}
