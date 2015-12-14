/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mystromecopower;

import org.openhab.core.binding.BindingProvider;

/**
 * @author Jordens Christophe
 * @since 1.8.0-SNAPSHOT
 */
public interface MyStromEcoPowerBindingProvider extends BindingProvider {
	/**
	 * Get the name in the configuration file which must correspond to the
	 * device name on the mystrom server.
	 * 
	 * @param itemName
	 *            The item name.
	 * @return The friendly name.
	 */
	public String getMystromFriendlyName(String itemName);

	/**
	 * Get if the item is configured as a switch item.
	 * 
	 * @param itemName
	 *            The item name.
	 * @return true if is a switch item else false.
	 */
	public Boolean getIsSwitch(String itemName);

	/**
	 * Get if the item is configured as a number item.
	 * 
	 * @param itemName
	 *            The item name.
	 * @return true if is a number item else false.
	 */
	public Boolean getIsNumberItem(String itemName);

	/**
	 * Get if the item is configured as a string item.
	 * 
	 * @param itemName
	 *            The item name.
	 * @return true if is a string item else false.
	 */
	public Boolean getIsStringItem(String itemName);
}
