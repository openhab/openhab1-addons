/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzbox;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can map openHAB items to
 * FritzBox binding types.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Kai Kreuzer
 * @since 0.7.0
 */
public interface FritzboxBindingProvider extends BindingProvider {

	/** binds incoming calls to an item */
	static final public String TYPE_INBOUND = "inbound";

	/** binds outgoing calls to an item */
	static final public String TYPE_OUTBOUND = "outbound";

	/** binds active (i.e. connected) calls to an item */
	static final public String TYPE_ACTIVE = "active";

	/** binds wlan state to an item */
	static final public String TYPE_WLAN = "wlan";

	/** binds dect state to an item */
	static final public String TYPE_DECT = "dect";

	/** binds tam# state to an item */
	static final public String TYPE_TAM = "tam";

	/** binds a user definde query to an item - little trick to validate item 'query' */
	static final public String TYPE_QUERY = "que";

	/** binds a user definde command to an item */
	static final public String TYPE_COMMAND = "cmd";

	static final public String[] TYPES = { TYPE_INBOUND, TYPE_OUTBOUND,
			TYPE_ACTIVE, TYPE_WLAN, TYPE_DECT, TYPE_TAM, TYPE_QUERY,
			TYPE_COMMAND };

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName
	 *            the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);

	/**
	 * Returns the binding type for an item name
	 * 
	 * @param itemName
	 *            the name of the item
	 * @return the items binding type
	 */
	String getType(String itemName);

	/**
	 * Provides an array of all item names of this provider for a given binding
	 * type
	 * 
	 * @param bindingType
	 *            the binding type of the items
	 * @return an array of all item names of this provider for the given binding
	 *         type
	 */
	String[] getItemNamesForType(String bindingType);

}
