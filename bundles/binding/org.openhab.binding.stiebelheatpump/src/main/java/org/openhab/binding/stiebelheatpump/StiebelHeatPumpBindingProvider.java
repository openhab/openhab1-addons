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
package org.openhab.binding.stiebelheatpump;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/* This interface is implemented by classes that can provide mapping information
 * between openHAB items and Stiebel heat pump  items.
 *
 * @author Peter Kreutzer
 * @since 1.5.0
 */
public interface StiebelHeatPumpBindingProvider extends BindingProvider {

    /**
     * Returns the configured parameter for the given <code>itemName</code>. If
     * no parameter has been configured or the itemName is unknown,
     * <code>null<code> is returned
     * 
     * @param itemName
     *            the item to find the parameter for
     * @return the configured parameter or <code>null<code> if nothing is
     *         configured or the itemName is unknown
     */
    public String getParameter(String itemName);

    /**
     * Returns the Type of the Item identified by {@code itemName}
     * 
     * @param itemName
     *            the name of the item to find the type for
     * @return the type of the Item identified by {@code itemName}
     */
    Class<? extends Item> getItemType(String itemName);
}
