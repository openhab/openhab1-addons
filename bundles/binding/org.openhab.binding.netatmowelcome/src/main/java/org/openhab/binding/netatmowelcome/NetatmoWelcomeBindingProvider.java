/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmowelcome;

import org.openhab.core.binding.BindingProvider;

/**
 * @author Ing. Peter Weiss
 * @since 1.8.0
 */
public interface NetatmoWelcomeBindingProvider extends BindingProvider {


	/**
	 * Returns an Id of the user the OAuth credentials do refer to.
	 *  
	 * @param itemName
	 * @return
	 */
	String getUserid(String itemName);

	/**
	 * Queries the NetatmoWelcome home of the given {@code itemName}.
	 * 
	 * @param itemName
	 *            the itemName to query
	 * @return the NetatmoWelcome HomeId of the Item identified by {@code itemName}
	 *         if it has a NetatmoWelcome binding, <code>null</code> otherwise
	 */
	String getHomeId(String itemName);

	/**
	 * Queries the NetatmoWelcome Person of the given {@code itemName}.
	 * 
	 * @param itemName
	 *            the itemName to query
	 * @return the NetatmoWelcome PersonId of the Item identified by {@code itemName} if
	 *         it has a NetatmoWelcome binding, <code>null</code> otherwise
	 */
	String getPersonId(String itemName);

	/**
	 * Queries the NetatmoWelcome Attribute of the given {@code itemName}.
	 * 
	 * @param itemName
	 *            the itemName to query
	 * @return the NetatmoWelcome Attribute of the Item identified by {@code itemName} if
	 *         it has a NetatmoWelcome binding, <code>null</code> otherwise
	 */
	String getAttribute(String itemName);

	/**
	 * Queries the NetatmoWelcome Camera of the given {@code itemName}.
	 * 
	 * @param itemName
	 *            the itemName to query
	 * @return the NetatmoWelcome CameraId of the Item identified by {@code itemName} if
	 *         it has a NetatmoWelcome binding, <code>null</code> otherwise
	 */
	 String getCameraId(String itemName);


}
