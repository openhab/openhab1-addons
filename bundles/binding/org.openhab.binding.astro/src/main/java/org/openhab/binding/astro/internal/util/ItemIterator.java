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
package org.openhab.binding.astro.internal.util;

import org.openhab.binding.astro.AstroBindingProvider;
import org.openhab.binding.astro.internal.common.AstroContext;
import org.openhab.binding.astro.internal.config.AstroBindingConfig;
import org.openhab.core.items.Item;

/**
 * Iterates through all astro items.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class ItemIterator {
    protected AstroContext context = AstroContext.getInstance();

    /**
     * Iterates through all items and calls the callback.
     */
    public void iterate(ItemIteratorCallback callback) {
        for (AstroBindingProvider provider : context.getProviders()) {
            for (String itemName : provider.getItemNames()) {
                AstroBindingConfig bindingConfig = provider.getBindingFor(itemName);
                Item item = provider.getItem(itemName);
                callback.next(bindingConfig, item);
            }
        }
    }

    /**
     * This callback interface is executed for every item in all providers.
     */
    public interface ItemIteratorCallback {
        public void next(AstroBindingConfig bindingConfig, Item item);
    }

}