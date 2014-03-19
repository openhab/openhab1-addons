/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.epsonprojector;

import java.util.List;

import org.openhab.binding.epsonprojector.internal.EpsonProjectorCommandType;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Epson projector items.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public interface EpsonProjectorBindingProvider extends BindingProvider {

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName
	 *            the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);

	/**
	 * Returns the device id to execute according to <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a command
	 * 
	 * @return the matching device id or <code>null</code> if no matching device
	 *         id could be found.
	 */
	String getDeviceId(String itemName);

	/**
	 * Returns the command type to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a unit code.
	 * 
	 * @return the corresponding command type to the given <code>itemName</code>
	 *         .
	 */
	EpsonProjectorCommandType getCommandType(String itemName);

	/**
	 * Returns the refresh interval to use according to <code>itemName</code>.
	 * Is used by In-Binding.
	 * 
	 * @param itemName
	 *            the item for which to find a refresh interval
	 * 
	 * @return the matching refresh interval or <code>null</code> if no matching
	 *         refresh interval could be found.
	 */
	int getRefreshInterval(String itemName);

	/**
	 * Check if <code>itemName</code> is In-binding.
	 * 
	 * @param itemName
	 *            the item for which to find binding direction
	 * 
	 * @return true if binding is In-binding.
	 */
	boolean isInBinding(String itemName);

	/**
	 * Returns all items which are mapped to a Epson-In-Binding
	 * 
	 * @return item which are mapped to a Epson-In-Binding
	 */
	List<String> getInBindingItemNames();
	
	/**
	 * Check if <code>itemName</code> is Out-binding.
	 * 
	 * @param itemName
	 *            the item for which to find binding direction
	 * 
	 * @return true if binding is Out-binding.
	 */
	boolean isOutBinding(String itemName);

	/**
	 * Check if <code>itemName</code> needs to be refreshed only when projector
	 * power is ON.
	 * 
	 * @param itemName
	 *            the item to test
	 * 
	 * @return true if needs to be refreshed only when projector Power is ON.
	 */
	boolean refreshOnlyWhenPowerOn(String itemName);

}
