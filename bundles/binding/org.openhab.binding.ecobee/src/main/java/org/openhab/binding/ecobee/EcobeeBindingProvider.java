/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee;

import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Ecobee items.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author John Cocula
 * @since 1.6.0
 */
public interface EcobeeBindingProvider extends BindingProvider {

	/**
	 * Returns an Id of the user the settings refer to.
	 *  
	 * @param itemName
	 *            the itemName to query
	 * @return the ID of the user the settings refer to.
	 */
	String getUserid(String itemName);

	/**
	 * Queries the Ecobee thermostat identifier of the given {@code itemName}.
	 * 
	 * @param itemName
	 *            the itemName to query
	 * @return the Ecobee thermostat identifier of the Item identified by {@code itemName}
	 *         if it has an Ecobee binding, <code>null</code> otherwise
	 */
	String getThermostatIdentifier(String itemName);
	
	/**
	 * Queries the Ecobee property of the given {@code itemName}.
	 * 
	 * @param itemName
	 *            the itemName to query
	 * @return the Ecobee property of the Item identified by {@code itemName} if
	 *         it has an Ecobee binding, <code>null</code> otherwise
	 */
	String getProperty(String itemName);
	
	/**
	 * Queries whether this item can be read from the Ecobee API, for the
	 * given {@code itemName}.
	 * 
	 * @param itemName
	 *            the itemName to query
	 * @return <code>true</code> if this property can be read from the Ecobee API.
	 */
	boolean isInBound(String itemName);
	
	/**
	 * Queries whether this item can be written to the Ecobee API, for the
	 * given {@code itemName}.
	 * 
	 * @param itemName
	 *            the itemName to query
	 * @return <code>true</code> if this property can be written to the Ecobee API.
	 */
	boolean isOutBound(String itemName);
}
