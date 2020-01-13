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
package org.openhab.binding.swegonventilation;

import org.openhab.binding.swegonventilation.internal.SwegonVentilationCommandType;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Swegon ventilation system items.
 *
 * @author Pauli Anttila
 * @since 1.4.0
 */
public interface SwegonVentilationBindingProvider extends BindingProvider {

    /**
     * Returns the Type of the Item identified by {@code itemName}
     * 
     * @param itemName
     *            the name of the item to find the type for
     * @return the type of the Item identified by {@code itemName}
     */
    Class<? extends Item> getItemType(String itemName);

    /**
     * Returns the command type to the given <code>itemName</code>.
     * 
     * @param itemName
     *            the item for which to find a command type.
     * 
     * @return the corresponding command type to the given <code>itemName</code>
     *         .
     */
    public SwegonVentilationCommandType getCommandType(String itemName);

}
