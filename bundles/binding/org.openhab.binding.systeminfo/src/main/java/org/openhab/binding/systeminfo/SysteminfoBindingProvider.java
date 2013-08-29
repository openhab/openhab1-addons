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
