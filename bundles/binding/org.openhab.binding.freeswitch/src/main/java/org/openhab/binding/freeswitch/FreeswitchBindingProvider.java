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
package org.openhab.binding.freeswitch;

import org.openhab.binding.freeswitch.internal.FreeswitchBindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * Users of this class can map openHAB items to Freeswitch binding types
 * 
 * @author Dan Cunningham
 * @since 1.4.0
 */
public interface FreeswitchBindingProvider extends BindingProvider {
    /**
     * returns the item with the given item name
     * 
     * @param itemName
     * @return
     */
    public Item getItem(String itemName);

    /**
     * return the binding config for an item
     * 
     * @param itemName
     * @return
     */
    public FreeswitchBindingConfig getFreeswitchBindingConfig(String itemName);

}
