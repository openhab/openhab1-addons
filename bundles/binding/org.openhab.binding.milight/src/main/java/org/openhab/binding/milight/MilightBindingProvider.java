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
package org.openhab.binding.milight;

import org.openhab.binding.milight.internal.MilightBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * The interface to implement for classes that want to contribute binding configurations for Milight.
 *
 * @author Hans-Joerg Merk
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public interface MilightBindingProvider extends BindingProvider {

    /**
     * Returns the configuration for the item with the given name.
     * 
     * @param itemName
     * @return The configuration if there is an item with the given name, null
     *         otherwise.
     */
    public MilightBindingConfig getItemConfig(String itemName);

}
