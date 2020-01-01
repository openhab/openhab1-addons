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
package org.openhab.binding.weather.internal.utils;

import org.openhab.binding.weather.WeatherBindingProvider;
import org.openhab.binding.weather.internal.common.WeatherContext;
import org.openhab.binding.weather.internal.common.binding.WeatherBindingConfig;

/**
 * Iterates through all weather items.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class ItemIterator {
    protected WeatherContext context = WeatherContext.getInstance();

    /**
     * Iterates through all items and calls the callback.
     */
    public void iterate(ItemIteratorCallback callback) {
        for (WeatherBindingProvider provider : context.getProviders()) {
            for (String itemName : provider.getItemNames()) {
                WeatherBindingConfig bindingConfig = provider.getBindingFor(itemName);
                callback.next(bindingConfig, itemName);
            }
        }
    }

    /**
     * This callback interface is executed for every item in all providers.
     */
    public interface ItemIteratorCallback {
        public void next(WeatherBindingConfig bindingConfig, String itemName);
    }

}