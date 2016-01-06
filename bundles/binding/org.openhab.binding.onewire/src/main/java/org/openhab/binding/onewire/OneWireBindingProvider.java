/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire;

import java.util.Map;

import org.openhab.binding.onewire.internal.OneWireBindingConfig;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information between openHAB items and OneWire
 * devices and their properties.
 * 
 * Implementing classes should register themselves as a service in order to be taken into account.
 * 
 * @author Thomas.Eichstaedt-Engelen, Dennis Riegelbauer
 * @since 0.6.0
 */
public interface OneWireBindingProvider extends BindingProvider {

	/**
	 * @return the corresponding InterfaceAbstractOneWireBindingConfig to the given <code>pvItemName</code>
	 */
	public OneWireBindingConfig getBindingConfig(String pvItemName);

	/**
	 * @return all BindingConfig
	 */
	public Map<String, BindingConfig> getBindingConfigs();

	/**
	 * Get an item by its name
	 * 
	 * @param pvItemName
	 * @return the item corresponding to the given <code>pvItemName</code>
	 */
	public Item getItem(String pvItemName);

}
