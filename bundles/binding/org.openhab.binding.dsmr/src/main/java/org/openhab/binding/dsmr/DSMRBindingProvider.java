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
package org.openhab.binding.dsmr;

import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and DSMR data.
 *
 * @author M.Volaart
 * @since 1.7.0
 */
public interface DSMRBindingProvider extends BindingProvider {
    /**
     * Returns the DMSR item identifier the binding represents
     * 
     * @param String
     *            openHAB itemName to get the DSMR item ID for
     * 
     * @return String containing the DMSR item ID
     */
    public String getDSMRItemID(String itemName);
}
