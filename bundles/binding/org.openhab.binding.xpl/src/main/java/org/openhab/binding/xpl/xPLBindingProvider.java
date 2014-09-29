/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xpl;

import java.util.List;
import org.cdp1802.xpl.xPL_MessageI;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and xPL Network items.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author clinique
 * @since 1.5.0
 */
public interface xPLBindingProvider extends BindingProvider {
	
	/**
	 * Returns a <code>xPLBindingConfig</code> associated to <code>itemName</code>
	 * 
	 * @param itemName
	 *            the item for which to find a configuration
	 * 
	 * @return a matching xPLBindingConfig
	 */
	public xPLBindingConfig getConfig(String itemName);
	
	/**
	 * Returns a <code>List</code> of item names that definition matches the incoming
	 * xPL message
	 * @param theMessage
	 *            an xPL_MessageI
	 * 
	 * @return a List of matching items (empty list if none 
	 *         could be found)
	 */
	public List<String> hasMessage(xPL_MessageI theMessage);
	
	/**
	 * Returns an <code>Item</code> of matching <code>itemName</code>
	 * 
	 * @param itemName
	 *            the item to search for
	 * 
	 * @return the matching item or <code>null</code> if no match
	 *         could be found.
	 */
	public Item getItem(String itemName);
}
