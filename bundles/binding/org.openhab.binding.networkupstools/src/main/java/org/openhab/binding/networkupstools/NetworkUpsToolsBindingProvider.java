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
package org.openhab.binding.networkupstools;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and NetworkUpsTools items.
 *
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 *
 * @author jaroslawmazgaj
 * @since 1.7.0
 */
public interface NetworkUpsToolsBindingProvider extends BindingProvider {
    /**
     * Returns the Type of the Item identified by {@code itemName}
     * 
     * @param itemName the name of the item to find the type for
     * @return the type of the Item identified by {@code itemName}
     */
    public Class<? extends Item> getItemType(String itemName);

    /**
     * Returns the UPS name identified by {@code itemName}
     * 
     * @param itemName the name of the item to find the type for
     * @return the UPS name identified by {@code itemName}
     */
    public String getUps(String itemName);

    /**
     * Returns the UPS property identified by {@code itemName}
     * 
     * @param itemName the name of the item to find the type for
     * @return the UPS property identified by {@code itemName}
     */
    public String getProperty(String itemName);

}
