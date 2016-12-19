/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.powerdoglocalapi;

import java.util.List;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and the PowerDog Local API.
 * 
 * @author wuellueb
 * @since 1.9.0
 */
public interface PowerDogLocalApiBindingProvider extends BindingProvider {

    /**
     * Returns the Type of the Item identified by {@code itemName}
     * 
     * @param itemName
     *            the name of the item to find the type for
     * @return the type of the Item identified by {@code itemName}
     */
    Class<? extends Item> getItemType(String itemName);

    /**
     * Return the serverId (used in the config file) for the PowerDog linked to
     * the item
     * 
     * @param itemName
     *            the item for which to find the serverId
     */
    String getServerId(String itemName);

    /**
     * Return the PowerDog Value ID for the item e.g.
     * 'impulsecounter_1234567890'
     * 
     * @param itemName
     *            the item for which to find a ValueID
     */
    String getValueId(String itemName);

    /**
     * Return the parameter 'name' for this item. The variable 'name' is the
     * PowerDog XML parameter used for the item, e.g. 'Current_Value'
     * 
     * @param itemName
     *            the item for which to find the name
     */
    String getName(String itemName);

    /**
     * Returns the refresh interval to use according to <code>itemName</code>.
     * Is used by PowerDog-In-Binding.
     * 
     * @param itemName
     *            the item for which to find a refresh interval
     * 
     * @return the matching refresh interval or <code>null</code> if no matching
     *         refresh interval could be found.
     */
    int getRefreshInterval(String itemName);

    /**
     * Returns all items which are mapped to a PowerDog-In-Binding
     * 
     * @return item which are mapped to a PowerDog-In-Binding
     */
    List<String> getInBindingItemNames();

    /**
     * Returns all items which are mapped to a PowerDog-Out-Binding
     * 
     * @return item which are mapped to a PowerDog-Out-Binding
     */
    List<String> getOutBindingItemNames();
}
