/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.iec6205621meter;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author Peter Kreutzer
 * @author Günter Speckhofer
 * @since 1.5.0
 */
public interface Iec6205621MeterBindingProvider extends BindingProvider {

	/**
	 * Returns the configured obis for the given <code>itemName</code>. If no
	 * obis has been configured or the itemName is unknown,
	 * <code>null<code> is returned
	 * 
	 * @param itemName
	 *            the item to find the obis for
	 * @return the configured obis or <code>null<code> if nothing is configured
	 *         or the itemName is unknown
	 */
	public String getObis(String itemName);

	/**
	 * Returns the configured meter name for the given <code>itemName</code>. If
	 * no meter name has been configured or the itemName is unknown,
	 * <code>null<code> is returned
	 * 
	 * @param itemName
	 *            the item to find the meter name for
	 * @return the configured meter name or <code>null<code> if nothing is
	 *         configured or the itemName is unknown
	 */
	public String getMeterName(String itemName);

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName
	 *            the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);

}
