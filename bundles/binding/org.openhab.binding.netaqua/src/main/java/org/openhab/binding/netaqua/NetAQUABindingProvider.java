/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netaqua;

import org.openhab.core.binding.BindingProvider;
import org.openhab.binding.netaqua.internal.NetAQUAItemType;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Netatmo items.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Markus Fritze
 * @since 1.7.0
 */
public interface NetAQUABindingProvider extends BindingProvider {

	/**
	 * Queries the netAQUA property of the given {@code itemName}.
	 * 
	 * @param itemName
	 *            the itemName to query
	 * @return the NetAQUAItemType of the Item identified by {@code itemName} if
	 *         it has an NetAQUA binding, <code>null</code> otherwise
	 */
	NetAQUAItemType getItemType(String itemName);

	/**
	 * Queries the netAQUA property of the given {@code itemName}.
	 * 
	 * @param itemName
	 *            the itemName to query
	 * @return the NetAQUAItemType of the Item identified by {@code itemName} if
	 *         it has an NetAQUA binding, <code>null</code> otherwise
	 */
	Integer getItemIndex(String itemName);

	/**
	 * Queries the netAQUA property of the given {@code itemName}.
	 * 
	 * @param itemName
	 *            the itemName to query
	 * @return the NetAQUAItemType of the Item identified by {@code itemName} if
	 *         it has an NetAQUA binding, <code>null</code> otherwise
	 */
	Integer getItemIndexB(String itemName);

}
