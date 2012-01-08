/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.vdr;

import java.util.List;

import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and VDR items.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Wolfgang Willinghoefer
 * @since 0.9.0
 */
public interface VDRBindingProvider extends BindingProvider {

	/**
	 * Returns a <code>List</code> of matching VDR ids (associated to <code>itemName</code>
	 * 
	 * @param itemName
	 *            the item for which to find a VDR id
	 * 
	 * @return a List of matching VDR ids or <code>null</code> if no matching VDR id
	 *         could be found.
	 */
	public List<String> getVDRId(String itemName);

	/**
	 * Returns a <code>List</code> of VDR commands (associated to <code>itemName</code>
	 * 
	 * @param itemName
	 *            the item for which to find VDR commands
	 * 
	 * @return a List of matching VDR commands or <code>null</code> if no matching VDR
	 *         command could be found.
	 */
	public List<String> getVDRCommand(String itemName);

	/**
	 * Returns associated item to <code>vdrId</code> and <code>vdrCommand</code> 
	 * 
	 * @param vdrId the id of the vdr for which items should be returned
	 * @param vdrCommand the vdr command for which items should be returned
	 * 
	 * @return the name of the item which is associated to <code>vdrId</code>
	 * and <code>vdrComannd</code>
	 */
	public String getBindingItemName(String vdrId, VDRCommandType vdrCommand);

}
