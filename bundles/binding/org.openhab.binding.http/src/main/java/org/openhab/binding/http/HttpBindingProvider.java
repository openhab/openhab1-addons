/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.http;

import java.util.List;
import java.util.Properties;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;


/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and HTTP items.
 * 
 * Implementing classes should register themselves as a service in order to be 
 * taken into account.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.6.0
 */
public interface HttpBindingProvider extends BindingProvider {

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);
	
	/**
	 * Returns the httpMethod to use according to <code>itemName</code> and
	 * <code>command</code>. Is used by HTTP-Out-Binding.
	 *  
	 * @param itemName the item for which to find a httpMethod
	 * @param command the openHAB command for which to find a configuration
	 * 
	 * @return the matching httpMethod or <code>null</code> if no matching
	 * httpMethod could be found.
	 */
	String getHttpMethod(String itemName, Command command);	
	
	/**
	 * Returns the url to use according to <code>itemName</code> and
	 * <code>command</code>. Is used by HTTP-Out-Binding.
	 *  
	 * @param itemName the item for which to find a url
	 * @param command the openHAB command for which to find a configuration
	 * 
	 * @return the matching url or <code>null</code> if no matching
	 * url could be found.
	 */
	String getUrl(String itemName, Command command);
	
	/**
	 * Returns HTTP headers to use according to <code>itemName</code> and
	 * <code>command</code>. Is used by HTTP-Out-Binding.
	 *  
	 * @param itemName the item for which to find a url
	 * @param command the openHAB command for which to find a configuration
	 * 
	 * @return the matching headers or <code>null</code> if no matching
	 * headers could be found.
	 */
	Properties getHttpHeaders(String itemName, Command command);

	/**
	 * Returns the url to use according to <code>itemName</code> and
	 * <code>command</code>. Is used by HTTP-In-Binding.
	 *  
	 * @param itemName the item for which to find a url
	 * @param command the openHAB command for which to find a configuration
	 * 
	 * @return the matching url or <code>null</code> if no matching
	 * url could be found.
	 */
	String getUrl(String itemName);
	
	/**
	 * Returns HTTP headers to use according to <code>itemName</code>. It is 
	 * used by HTTP-In-Binding
	 * 
	 * @param itemName the item for which to find headers
	 * @return the matching HTTP headers
	 */
	Properties getHttpHeaders(String itemName);
	
	/**
	 * Returns the refresh interval to use according to <code>itemName</code>.
	 * Is used by HTTP-In-Binding.
	 *  
	 * @param itemName the item for which to find a refresh interval
	 * @param command the openHAB command for which to find a configuration
	 * 
	 * @return the matching refresh interval or <code>null</code> if no matching
	 * refresh interval could be found.
	 */
	int getRefreshInterval(String itemName);
	
	/**
	 * Returns the transformation rule to use according to <code>itemName</code>.
	 * Is used by HTTP-In-Binding.
	 * 
	 * @param itemName the item for which to find a transformation rule
	 * @param command the openHAB command for which to find a configuration
	 * 
	 * @return the matching transformation rule or <code>null</code> if no matching
	 * transformation rule could be found.
	 */
	String getTransformation(String itemName);
	
	/**
	 * Returns all items which are mapped to a HTTP-In-Binding
	 * @return item which are mapped to a HTTP-In-Binding
	 */
	List<String> getInBindingItemNames();
	
}
