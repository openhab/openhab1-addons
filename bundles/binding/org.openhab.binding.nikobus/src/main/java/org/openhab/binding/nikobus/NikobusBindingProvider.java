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
package org.openhab.binding.nikobus;

import java.util.List;

import org.openhab.binding.nikobus.internal.config.AbstractNikobusItemConfig;
import org.openhab.binding.nikobus.internal.core.NikobusModule;
import org.openhab.core.binding.BindingProvider;

/**
 * Binding Provider for Nikobus.
 *
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public interface NikobusBindingProvider extends BindingProvider {

    /**
     * Get the item binding with the given itemName.
     * 
     * @param itemName
     *            name of the item
     * @return NikobusItemBinding or null if none found
     */
    public AbstractNikobusItemConfig getItemConfig(String itemName);

    /**
     * Lookup a module by its' address.
     * 
     * @param name
     *            e.g. 3444-1
     * @return NikobusModule
     */
    public NikobusModule getModule(String moduleAddress);

    /**
     * Get list of all registered modules
     */
    public List<NikobusModule> getAllModules();

}
