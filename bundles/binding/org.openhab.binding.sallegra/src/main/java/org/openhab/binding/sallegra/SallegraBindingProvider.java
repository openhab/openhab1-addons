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
package org.openhab.binding.sallegra;

import org.openhab.binding.sallegra.internal.SallegraBindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author Benjamin Marty (Developed on behalf of Satelco.ch)
 * @since 1.8.0
 */
public interface SallegraBindingProvider extends BindingProvider {
    /**
     * Lookup method for binding configs
     * 
     * @param itemName
     *            name of the item
     * @return the binding config for the item with the name itemName
     */
    public SallegraBindingConfig getBindingConfigFor(String itemName);

    /**
     * Returns the class for the item
     * 
     * @param name
     *            name of the item
     * @return the class of the item with the name itemName
     */
    public Class<? extends Item> getItemType(String name);
}
