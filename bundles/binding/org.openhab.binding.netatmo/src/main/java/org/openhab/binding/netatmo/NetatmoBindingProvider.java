/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo;

import org.openhab.core.binding.BindingProvider;
import org.openhab.binding.netatmo.internal.NetatmoMeasureType;
import org.openhab.binding.netatmo.internal.NetatmoScale;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Netatmo items.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Andreas Brenk
 * @author Rob Nielsen
 * @since 1.4.0
 */
public interface NetatmoBindingProvider extends BindingProvider {

	/**
	 * Returns an Id of the user the OAuth credentials do refer to.
	 *  
	 * @param itemName
	 * @return
	 */
	String getUserid(String itemName);

	/**
	 * Queries the Netatmo device id of the given {@code itemName}.
	 * 
	 * @param itemName
	 *            the itemName to query
	 * @return the Netatmo device id of the Item identified by {@code itemName}
	 *         if it has a Netatmo binding, <code>null</code> otherwise
	 */
	String getDeviceId(String itemName);

	/**
	 * Queries the Netatmo measure of the given {@code itemName}.
	 * 
	 * @param itemName
	 *            the itemName to query
	 * @return the Netatmo measure of the Item identified by {@code itemName} if
	 *         it has a Netatmo binding, <code>null</code> otherwise
	 */
	NetatmoMeasureType getMeasureType(String itemName);

	/**
	 * Queries the Netatmo module of the given {@code itemName}.
	 * 
	 * @param itemName
	 *            the itemName to query
	 * @return the Netatmo module id if the item has a Netatmo binding and
	 *         specifies a module (i.e. is not a device binding),
	 *         <code>null</code> otherwise
	 */
	String getModuleId(String itemName);

	/**
	 * Returns the scale to use when querying the Netatmo measure of the given
	 * {@code itemName}.
	 *
	 * @param itemName
	 * @return the Netatmo scale of the Item identified by {@code itemName} if
	 *         it has a Netatmo binding, <code>null</code> otherwise
	 */
	NetatmoScale getNetatmoScale(String itemName);
}
