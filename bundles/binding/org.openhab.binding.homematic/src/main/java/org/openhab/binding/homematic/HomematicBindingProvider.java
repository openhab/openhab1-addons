/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic;

import org.openhab.binding.homematic.internal.config.AdminItem;
import org.openhab.binding.homematic.internal.config.HomematicParameterAddress;
import org.openhab.binding.homematic.internal.converter.state.StateConverter;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * The interface to implement to provide a binding for Homematic.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public interface HomematicBindingProvider extends BindingProvider {

    /**
     * @return the parameter address to the given <code>itemName</code>
     */
    HomematicParameterAddress getParameterAddress(String itemName);

    /**
     * @return the admin item to the given <code>itemName</code>
     */
    AdminItem getAdminItem(String itemName);

    /**
     * @return true if the item is an ADMIN item
     */
    boolean isAdminItem(String itemName);

    /**
     * @param itemName
     *            The name of the item
     * @return The item with the given name
     */
    Item getItem(String itemName);

    /**
     * Returns the configured converter class if a custom converter is specified in the config line. Otherwise it returns null.
     * 
     * @param itemName
     *            The name of the item
     * @return The converter class or null
     */
    <CONVERTER extends StateConverter<?, ?>> Class<CONVERTER> getConverter(String itemName);

}
