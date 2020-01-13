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
package org.openhab.binding.garadget;

import org.openhab.binding.garadget.internal.GaradgetBindingConfig;
import org.openhab.core.autoupdate.AutoUpdateBindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Garadget devices.
 *
 * @author John Cocula
 * @since 1.9.0
 */
public interface GaradgetBindingProvider extends AutoUpdateBindingProvider {

    /**
     * Returns the binding configuration for the item with the given name.
     *
     * @param itemName
     * @return The configuration if there is an item with the given name, null
     *         otherwise.
     */
    public GaradgetBindingConfig getItemBindingConfig(String itemName);
}
