/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.frontiersiliconradio;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * Binding provider for internet radios based on frontier silicon chipset. Binding configuration is:
 * "frontiersiliconradio=[deviceId]:[Property]", e.g. "frontiersiliconradio=kitchen:POWER"
 * 
 * @author Rainer Ostendorf
 * @since 1.7.0
 */
public interface FrontierSiliconRadioBindingProvider extends BindingProvider {

	// return the property the item is bound to (e.g. "POWER")
	String getProperty(String itemName);

	// get the device id of the radio (e.g. "livingroom-radio")
	String getDeviceID(String itemName);

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName
	 *            the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);
}
