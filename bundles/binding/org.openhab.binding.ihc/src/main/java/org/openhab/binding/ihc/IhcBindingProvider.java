/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and IHC / ELKO LS resource items.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Pauli Anttila
 * @since 1.1.0
 */
public interface IhcBindingProvider extends BindingProvider {

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);
	
	/**
	 * Returns the resource id to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a resource id.
	 * 
	 * @return the corresponding Resource Id to the given <code>itemName</code>.
	 */
	public int getResourceId(String itemName);

	/**
	 * Returns the refresh interval to the given <code>itemName</code>. Is used
	 * by IHC-In-Binding.
	 * 
	 * @param itemName
	 *            the item for which to find a refresh interval.
	 * 
	 * @return the matching refresh interval or <code>null</code> if no
	 *         matching. refresh interval could be found.
	 */
	public int getRefreshInterval(String itemName);

	/**
	 * Returns item binding mode to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a binding mode.
	 * 
	 * @return true if item is out binding only.
	 */
	public boolean isOutBindingOnly(String itemName);

}
