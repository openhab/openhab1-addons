/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.ddwrt;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can map openHAB items to
 * DD-WRT binding types.
 *
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 *
 * @author Kai Kreuzer
 * @author Markus Eckhardt
 * @since 1.9.0
 */
public interface DDWRTBindingProvider extends BindingProvider {

    /** binds wlan state to an item */
    static final public String TYPE_ROUTER_TYPE = "routertype";

    /** binds wlan state to an item */
    static final public String TYPE_WLAN_24 = "wlan24";

    /** binds wlan state to an item */
    static final public String TYPE_WLAN_50 = "wlan50";

    /** binds guest wlan state to an item */
    static final public String TYPE_WLAN_GUEST = "wlanguest";

    static final public String[] TYPES = { TYPE_ROUTER_TYPE, TYPE_WLAN_24, TYPE_WLAN_50, TYPE_WLAN_GUEST };

    /**
     * Returns the Type of the Item identified by {@code itemName}
     *
     * @param itemName
     *            the name of the item to find the type for
     * @return the type of the Item identified by {@code itemName}
     */
    Class<? extends Item> getItemType(String itemName);

    /**
     * Returns the binding type for an item name
     *
     * @param itemName
     *            the name of the item
     * @return the items binding type
     */
    String getType(String itemName);

    /**
     * Provides an array of all item names of this provider for a given binding
     * type
     *
     * @param bindingType
     *            the binding type of the items
     * @return an array of all item names of this provider for the given binding
     *         type
     */
    String[] getItemNamesForType(String bindingType);

}
