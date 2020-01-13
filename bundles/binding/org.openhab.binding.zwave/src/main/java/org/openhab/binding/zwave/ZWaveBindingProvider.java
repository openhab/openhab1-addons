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
package org.openhab.binding.zwave;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * Binding provider interface. Defines the methods
 * to interact with the binding provider.
 *
 * @author Victor Belov
 * @since 1.3.0
 */
public interface ZWaveBindingProvider extends BindingProvider {
    /**
     * Returns the binding configuration for the item with
     * this name.
     *
     * @param itemName the name to get the binding configuration for.
     * @return the binding configuration.
     */
    public ZWaveBindingConfig getZwaveBindingConfig(String itemName);

    /**
     * Returns the {@link Item} with the specified item name. Returns null
     * if the item was not found.
     *
     * @param itemName the name of the item.
     * @return the item.
     */
    public Item getItem(String itemName);
}
