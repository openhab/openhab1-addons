/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
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
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;

/**
 * Interface of the Plugwise Binding Provider
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public interface PlugwiseBindingProvider extends BindingProvider {

    /**
     * Returns a <code>List</code> of matching Plugwise IDs (associated to <code>itemName</code>.
     *
     * @param itemName the item for which to find a Plugwise ID
     * @return a List of matching Plugwise ids or <code>null</code> if no matching Plugwise ID
     *         could be found.
     */
    public List<String> getPlugwiseID(String itemName);

    /**
     * Returns the matching Plugwise ID (associated to <code>itemName</code> and aCommand).
     *
     * @param itemName the item for which to find a Plugwise ID (the device MAC or name)
     * @param command  the a command
     * @return a List of matching Plugwise IDs or <code>null</code> if no matching Plugwise ID
     *         could be found.
     */
    public String getPlugwiseID(String itemName, Command command);

    /**
     * Returns the matching Plugwise command (associated to <code>itemName</code> and aCommand).
     *
     * @param plugwiseID the Plugwise ID (the device MAC or name)
     * @param type       the type
     * @return a List of matching Plugwise IDs or <code>null</code> if no matching Plugwise ID
     *         could be found.
     */

    public Set<String> getItemNames(String plugwiseID, PlugwiseCommandType type);

    /**
     * Gets the interval list. the Interval list is a list of Plugwise Configuration elements that is used to schedule
     * Quartz jobs to poll variables. If a certain Job.class (see PlugwiseCommandType) is required multiple times for a
     * given Plugwise MAC address, then the only the element with the smallest interval time will be retained. The Items
     * that depend on this Job.class to get their values/updates, and that do have a larger polling interval, will in
     * reality be polled more frequently.
     *
     * @return the interval list
     */
    public List<PlugwiseBindingConfigElement> getIntervalList();

    /**
     * Gets the commands by type.
     *
     * @param itemName     the item name
     * @param commandClass the command class
     * @return the commands by type
     */
    public List<Command> getCommandsByType(String itemName, Class<? extends Command> commandClass);

    /**
     * Gets the corresponding plugwise command type for a given Item and Command
     *
     * @param itemName the item name
     * @param command  the command
     * @return the plugwise command type
     */
    public PlugwiseCommandType getPlugwiseCommandType(String itemName, Command command);

    /**
     * Gets all the openHAB commands for the given Item
     *
     * @param itemName the item name
     * @return the list of Commands
     */
    public List<Command> getAllCommands(String itemName);

}
