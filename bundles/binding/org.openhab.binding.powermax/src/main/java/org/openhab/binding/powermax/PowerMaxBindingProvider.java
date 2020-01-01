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
package org.openhab.binding.powermax;

import org.openhab.core.binding.BindingProvider;

/**
 * Binding provider interface for the powermax binding
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public interface PowerMaxBindingProvider extends BindingProvider {

    /**
     * Returns the binding configuration for the specified item name
     *
     * @param itemName: name of the item
     * @return the binding configuration.
     */
    public PowerMaxBindingConfig getConfig(String itemName);

}
