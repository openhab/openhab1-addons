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
package org.openhab.binding.plex;

import org.openhab.binding.plex.internal.PlexBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Plex properties.
 *
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public interface PlexBindingProvider extends BindingProvider {

    /**
     * Get binding config by machine ID and property
     * 
     * @param machineIdentifier Plex machine ID
     * @param property Name of the property
     * @return The binding config for parameters or null if not found
     */
    public PlexBindingConfig getConfig(String machineIdentifier, String property);

    /**
     * Get binding config by itemName
     * 
     * @param itemName Name of the item in openHAB
     * @return The binding config for itemName or null if not found
     */
    public PlexBindingConfig getConfig(String itemName);

}
