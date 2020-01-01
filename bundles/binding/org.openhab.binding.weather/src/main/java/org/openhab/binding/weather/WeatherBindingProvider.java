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
package org.openhab.binding.weather;

import org.openhab.binding.weather.internal.common.binding.WeatherBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * The interface to implement to provide a binding for Weather.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public interface WeatherBindingProvider extends BindingProvider {

    /**
     * Returns the WeatherBindingConfig for an item by name.
     */
    public WeatherBindingConfig getBindingFor(String itemName);

    /**
     * Returns true, if a binding for the specified locationId is available.
     */
    public boolean hasBinding(String locationId);

}
