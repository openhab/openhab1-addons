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
package org.openhab.binding.energenie;

import org.openhab.binding.energenie.internal.EnergenieBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Hans-Jörg Merk
 * @since 1.6.0
 */
public interface EnergenieBindingProvider extends BindingProvider {

    /**
     * Returns the configuration for the item with the given name.
     * 
     * @param itemName
     * @return the configuration if there is an item with the given name, null otherwise.
     */

    public EnergenieBindingConfig getItemConfig(String itemName);

}
