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
package org.openhab.binding.myq;

import java.util.List;

import org.openhab.binding.myq.internal.MyqBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Chamberlain Garage Door Openers.
 *
 * I used the Hue binding as my example so most of the code is the same
 *
 * @author Scott Hanson
 * @since 1.8.0
 */
public interface MyqBindingProvider extends BindingProvider {

    /**
     * Returns the configuration for the item with the given name.
     * 
     * @param itemName
     * @return The configuration if there is an item with the given name, null
     *         otherwise.
     */
    public MyqBindingConfig getItemConfig(String itemName);

    /**
     * Returns a list of item names
     * 
     * @return List of item names mapped to a Chamberlain myQ Binding
     */
    public List<String> getInBindingItemNames();
}
