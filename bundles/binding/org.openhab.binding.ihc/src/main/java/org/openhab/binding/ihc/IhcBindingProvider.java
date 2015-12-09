/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and IHC / ELKO LS resource items.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Pauli Anttila
 * @author Simon Merschjohann
 * @since 1.1.0
 */
public interface IhcBindingProvider extends BindingProvider {

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName
	 *            the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);

	/**
	 * Returns the resource id of the associated In-Binding to the given
	 * <code>itemName</code>
	 * 
	 * @param itemName
	 *            the item for which to find a resource id.
	 * @return the corresponding Resource Id of the In-Binding to the given
	 *         <code>itemName</code>.
	 */
	public int getResourceIdForInBinding(String itemName);

	/**
	 * Returns the resource id to the given <code>itemName</code> and
	 * <code>cmd</code>
	 * 
	 * @param itemName
	 *            the item for which to find a resource id.
	 * @param cmd
	 *            the wanted command, choose null for wildcard.
	 * 
	 * @return the corresponding Resource Id to the given <code>itemName</code>.
	 */
	public int getResourceId(String itemName, Command cmd);

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
	 * Returns item binding mode to the given <code>itemName</code> and
	 * <code>resourceId</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a binding mode.
	 * 
	 * @param resourceId
	 *            the resourceId to be checked.
	 * 
	 * @return true if given resource is an out binding.
	 */
	public boolean isOutBinding(String itemName, int resourceId);

	/**
	 * Returns true if there exists an In-Binding ( "<0x1234" or "0x1234" ) for
	 * the given Item
	 * 
	 * @param itemName
	 * @return true if item has in binding
	 */
	public boolean hasInBinding(String itemName);

	/**
	 * Returns the value which is configured for the given item and the command
	 * 
	 * @param itemName
	 * @param cmd
	 * @return
	 */
	public Integer getValue(String itemName, Command cmd);
}
