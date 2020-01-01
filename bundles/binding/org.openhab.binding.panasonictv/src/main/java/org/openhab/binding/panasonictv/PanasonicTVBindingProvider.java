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
package org.openhab.binding.panasonictv;

import org.openhab.core.binding.BindingProvider;

/**
 * This interface defines a method for retrieving the binding configuration by an item name.
 *
 * @author Andre Heuer
 * @since 1.7.0
 */
public interface PanasonicTVBindingProvider extends BindingProvider {

    /**
     * This method returns the item configuration based on the item name
     * 
     * @param item The name of the item
     * @return Item/Binding configuration for the given item
     */
    PanasonicTVBindingConfig getBindingConfigForItem(String item);
}
