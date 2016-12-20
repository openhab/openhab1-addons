/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.km200;

import java.util.HashMap;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can map openHAB items to
 * KM200 binding types.
 *
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 *
 * @author Markus Eckhardt
 * @since 1.9.0
 */
public interface KM200BindingProvider extends BindingProvider {

    /** binds the date_time service to an item */
    static final public String DATE_TIME = "date_time";
    static final public String SYS_BRAND = "sys_brand";
    static final public String SYS_TYPE = "sys_type";
    static final public String SYS_STATE = "sys_state";
    static final public String VER_FIRMWARE = "ver_firmware";
    static final public String VER_HARDWARE = "ver_hardware";

    /** binds the date_time service to an item */
    static final public String DIRECT_SERVICE = "service";

    static final public String[] TYPES = { DATE_TIME, DIRECT_SERVICE };

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
     * Returns the service for an item name
     *
     * @param itemName
     *            the name of the item
     * @return the items binding service
     */
    String getService(String itemName);

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

    /**
     * Returns the parameters for this service
     *
     * @param itemName
     *            the name of the item
     * @return HashMap with parameters
     */
    HashMap<String, String> getParameter(String itemName);

}
