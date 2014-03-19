/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;



/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and OneWire items (sensors).
 * 
 * Implementing classes should register themselves as a service in order to be 
 * taken into account.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.6.0
 */
public interface OneWireBindingProvider extends BindingProvider {

	/**
	 * @return the corresponding sensorId to the given <code>itemName</code>
	 */
	public String getSensorId(String itemName);
	
	/**
	 * @return the corresponding unitId of the given <code>itemName</code>
	 */
	public String getUnitId(String itemName);

	public Item getItem(String itemName);
	
}
