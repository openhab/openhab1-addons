/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public PlugwiseCommandType getPlugwiseCommandType(String itemName, Command someCommand);

	/**
	 * Gets all the openHAB commands for the given Item
	 *
	 * @param itemName the item name
	 * @return the list of Commands
	 */
	public List<Command> getAllCommands(String itemName);
	

}
