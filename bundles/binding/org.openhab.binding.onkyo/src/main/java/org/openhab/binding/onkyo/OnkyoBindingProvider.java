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
package org.openhab.binding.onkyo;

import java.util.HashMap;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Onkyo devices.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public interface OnkyoBindingProvider extends BindingProvider {
	
	/**
	 * Returns the Type of the Item identified by {@code itemName}
	 * 
	 * @param itemName the name of the item to find the type for
	 * @return the type of the Item identified by {@code itemName}
	 */
	Class<? extends Item> getItemType(String itemName);

	/**
	 * Returns the command to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a command.
	 * @param command
	 *            the openHAB command for which to find a device command
	 * 
	 * @return the corresponding command or <code>null</code> if no matching
	 *         device id could be found.
	 */
	public String getDeviceCommand(String itemName, String command);

	/**
	 * Returns the init command to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a command.
	 * 
	 * @return the corresponding init command or <code>null</code> if no init
	 *         command is defined.
	 */
  public String getItemInitCommand(String itemName);
	
	/**
	 * Returns all commands to the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a command.
	 * 
	 * @return the corresponding list of commands or <code>null</code> if no commands
	 *         could be found.
	 */
  public HashMap<String, String> getDeviceCommands(String itemName);
}
