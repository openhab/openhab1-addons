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
package org.openhab.binding.plugwise;

import java.util.List;
import java.util.Set;

import org.openhab.binding.plugwise.internal.PlugwiseGenericBindingProvider.PlugwiseBindingConfigElement;
import org.openhab.core.autoupdate.AutoUpdateBindingProvider;
import org.openhab.core.types.Command;

/**
 * Interface of the Plugwise Binding Provider
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public interface PlugwiseBindingProvider extends AutoUpdateBindingProvider {
	
	/**
	 * Returns a <code>List</code> of matching Plugwise ids (associated to <code>itemName</code>.
	 *
	 * @param itemName the item for which to find a Plugwise id
	 * @return a List of matching Plugwise ids or <code>null</code> if no matching Plugwise id
	 * could be found.
	 */
	public List<String> getPlugwiseID(String itemName);
	
	/**
	 * Returns the matching Plugwise id (associated to <code>itemName</code> and aCommand).
	 *
	 * @param itemName the item for which to find a Plugwise id
	 * @param aCommand the a command
	 * @return a List of matching Plugwise ids or <code>null</code> if no matching Plugwise id
	 * could be found.
	 */
	public String getPlugwiseID(String itemName, Command aCommand);

	/**
	 * Returns the matching Plugwise command (associated to <code>itemName</code> and aCommand).
	 *
	 * @param PlugwiseID the plugwise id
	 * @param type the type
	 * @return a List of matching Plugwise ids or <code>null</code> if no matching Plugwise id
	 * could be found.
	 */
	
	public Set<String> getItemNames(String PlugwiseID, PlugwiseCommandType type);

	/**
	 * Gets the interval list. the Interval list is a list of Plugwise Configuration elements that is used to schedule 
	 * Quartz jobs to poll variables. If a certain Job.class (see PlugwiseCommandType) is required multiple times for a 
	 * given Plugwise MAC address, then the only the element with the smallest interval time will be retained. The Items
	 * that depend on this Job.class to get their values/updates, and that do have a larger polling interval, will in reality be polled more frequently.
	 *
	 * @return the interval list
	 */
	public List<PlugwiseBindingConfigElement> getIntervalList();

	/**
	 * Gets the commands by type.
	 *
	 * @param itemName the item name
	 * @param class1 the class1
	 * @return the commands by type
	 */
	public List<Command> getCommandsByType(String itemName, Class<? extends Command> class1);

	/**
	 * Gets the corresponding plugwise command type for a given Item and Command
	 *
	 * @param itemName the item name
	 * @param someCommand the command
	 * @return the plugwise command type
	 */
	public PlugwiseCommandType getPlugwiseCommandType(String itemName,
			Command someCommand);

	/**
	 * Gets all the openHAB commands for the given Item
	 *
	 * @param itemName the item name
	 * @return the list of Commands
	 */
	public List<Command> getAllCommands(String itemName);
	

}
