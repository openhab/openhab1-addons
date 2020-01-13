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
package org.openhab.binding.wr3223;

import org.openhab.core.binding.BindingProvider;

/**
 * @author Michael Fraefel
 * @since 1.10.0
 */
public interface WR3223BindingProvider extends BindingProvider {

    /**
     * Provides an array of all item names of this provider for a given binding type
     *
     * @param bindingType the binding type of the items
     * @return an array of all item names of this provider for the given binding type
     */
    public String[] getItemNamesForType(WR3223CommandType bindingType);

    /**
     * Get the WR3223 command type for a given itemName.
     *
     * @param itemName
     * @return
     */
    public WR3223CommandType getWR3223CommandTypeForItemName(String itemName);

}
