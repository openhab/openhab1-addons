/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.http;

import java.util.List;
import java.util.Properties;

import org.openhab.core.binding.BindingProvider;
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
