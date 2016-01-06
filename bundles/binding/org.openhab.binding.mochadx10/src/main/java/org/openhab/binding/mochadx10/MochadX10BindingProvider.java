/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mochadx10;

import java.util.List;

import org.openhab.binding.mochadx10.internal.MochadX10BindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and the Mochad X10 Daemon 
 * (http://sourceforge.net/projects/mochad/).
 * 
 * @author Jack Sleuters
 * @since 1.7.0
 */
public interface MochadX10BindingProvider extends BindingProvider {

	/**
	 * Returns the configuration for the item with the given name.
	 * 
	 * @param itemName
	 * @return The configuration if there is an item with the given name, null
	 *         otherwise.
	 */
	public MochadX10BindingConfig getItemConfig(String itemName);

	/**
	 * Returns a list of item names
	 * 
	 * @return List of item names mapped to a Hue-Binding
	 */
	public List<String> getInBindingItemNames();
}
