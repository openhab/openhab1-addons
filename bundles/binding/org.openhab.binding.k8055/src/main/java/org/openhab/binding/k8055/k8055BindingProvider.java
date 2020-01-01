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
package org.openhab.binding.k8055;

import org.openhab.binding.k8055.internal.k8055GenericBindingProvider.k8055BindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Anthony Green
 * @since 1.5.0
 */
public interface k8055BindingProvider extends BindingProvider {
    /**
     * Returns the configuration for the item with the given name.
     * 
     * @param itemName
     * @return The configuration if there is an item with the given name, null
     *         otherwise.
     */
    public k8055BindingConfig getItemConfig(String itemName);
}
