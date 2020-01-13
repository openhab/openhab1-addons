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
package org.openhab.binding.enphaseenergy;

import org.openhab.binding.enphaseenergy.internal.EnphaseenergyItemType;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Netatmo items.
 *
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 *
 * @author Markus Fritze
 * @since 1.7.0
 */
public interface EnphaseenergyBindingProvider extends BindingProvider {

    /**
     * Queries the Netatmo measure of the given {@code itemName}.
     * 
     * @param itemName
     *            the itemName to query
     * @return the EnphaseenergyItemType of the Item identified by {@code itemName} if
     *         it has an Enphaseenergy binding, <code>null</code> otherwise
     */
    EnphaseenergyItemType getItemType(String itemName);

    /**
     * Queries the system ID of the given {@code itemName}.
     * 
     * @param itemName
     *            the itemName to query
     * @return the system id if the item has an Enphaseenergy binding
     *         <code>null</code> otherwise
     */
    Integer getSystemId(String itemName);

}
