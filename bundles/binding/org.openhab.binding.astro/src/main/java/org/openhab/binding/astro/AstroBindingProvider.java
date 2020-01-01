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
package org.openhab.binding.astro;

import org.openhab.binding.astro.internal.config.AstroBindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * The interface to implement to provide a binding for Astro.
 *
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public interface AstroBindingProvider extends BindingProvider {

    /**
     * Returns the AstroBindingConfig for an item by name.
     */
    public AstroBindingConfig getBindingFor(String itemName);

    /**
     * Returns the item object by itemName.
     */
    public Item getItem(String itemName);

    /**
     * Returns true, if the specified binding is available.
     */
    public boolean hasBinding(AstroBindingConfig bindingConfig);
}
