/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.systeminfo;

import org.openhab.binding.systeminfo.internal.SysteminfoCommandType;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author Pauli Anttila
 * @since 1.3.0
 */
public interface SysteminfoBindingProvider extends BindingProvider {

	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName
	 *            the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);

	/**
	 * Returns the command type to the given <code>itemName</code>. Command type
	 * specify the system resource. See available commands from
	 * {@link SysteminfoCommandType}.
	 * 
	 * @param itemName
	 *            the item for which to find a unit code.
	 * 
	 * @return the corresponding command type to the given <code>itemName</code>
	 *         .
	 */
	SysteminfoCommandType getCommandType(String itemName);

	/**
	 * Returns the refresh interval to use according to <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a refresh interval
	 * 
	 * @return the matching refresh interval or <code>null</code> if no matching
	 *         refresh interval could be found.
	 */
	int getRefreshInterval(String itemName);

	/**
	 * Returns the target to use according to <code>itemName</code>. When system
	 * command can fetch data from several end points like DiskReads or
	 * ProcessRealMem, the target specify the correct end point.
	 * 
	 * @param itemName
	 *            the item for which to find a refresh interval
	 * 
	 * @return the matching target or <code>null</code> if no matching target
	 *         could be found.
	 */
	String getTarget(String itemName);
	
}
