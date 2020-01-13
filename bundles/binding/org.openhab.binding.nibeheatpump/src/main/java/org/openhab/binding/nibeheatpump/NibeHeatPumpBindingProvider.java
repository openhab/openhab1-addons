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
package org.openhab.binding.nibeheatpump;

import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Nibe heat pump items.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public interface NibeHeatPumpBindingProvider extends BindingProvider {

    /**
     * Returns the item id to the given <code>itemName</code>.
     * 
     * @param itemName
     *            the item for which to find a item id.
     * 
     * @return the corresponding item id to the given <code>itemName</code>.
     */
    public int getItemId(String itemName);

}
