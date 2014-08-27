/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ehealth;

import org.openhab.binding.ehealth.protocol.EHealthSensorPropertyName;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Libelium eHealth items.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Thomas Eichstaedt-Engelen
 * @since 1.6.0
 */
public interface EHealthBindingProvider extends BindingProvider {

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName
	 *            the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);

	/**
	 * Returns the name of the Sensor Property identified by {@code itemName}
	 * 
	 * @param itemName the name of the Sensor Property
	 * @return the name of the Sensor Property identified by {@code itemName}
	 */
	EHealthSensorPropertyName getSensorPropertyName(String itemName);

}
