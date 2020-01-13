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
package org.openhab.binding.insteonplm;

import org.openhab.core.binding.BindingProvider;

/**
 * Binding provider interface. Defines the methods to interact with the binding provider.
 *
 * @author Bernd Pfrommer
 * @since 1.5.0
 */
public interface InsteonPLMBindingProvider extends BindingProvider {
    /**
     * Returns the binding configuration for the item with
     * this name.
     *
     * @param itemName the name to get the binding configuration for.
     * @return the binding configuration.
     */
    public InsteonPLMBindingConfig getInsteonPLMBindingConfig(String itemName);
}
