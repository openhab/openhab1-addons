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
package org.openhab.binding.autelis;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 *
 * Provides a mapping between openHAB {@link Item}s and a Autelis Pool controller.
 * 
 * @author Dan Cunningham
 * @since 1.7.0
 */
public interface AutelisBindingProvider extends BindingProvider {

    /**
     * Returns an {@link Item} for a given item name
     * 
     * @param itemName
     * @return {@link Item} for the given item name
     */
    public Item getItem(String itemName);

    /**
     * Returns the binding configuration for an item as a {@link String}
     * 
     * @param itemName
     * @return {@link String}
     */
    public String getAutelisBindingConfigString(String itemName);
}
