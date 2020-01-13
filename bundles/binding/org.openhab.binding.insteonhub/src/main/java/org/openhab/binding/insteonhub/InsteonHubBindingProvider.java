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
package org.openhab.binding.insteonhub;

import java.util.Set;

import org.openhab.binding.insteonhub.internal.InsteonHubBindingConfig;
import org.openhab.binding.insteonhub.internal.InsteonHubBindingDeviceInfo;
import org.openhab.core.binding.BindingProvider;

/**
 * Insteon Hub BindingProvider interface
 *
 * @author Eric Thill
 * @since 1.4.0
 */
public interface InsteonHubBindingProvider extends BindingProvider {

    /**
     * Returns the openhab Item Configuration identified by {@code itemName}.
     * 
     * @param itemName
     *            the name of the Item.
     * @return The Item Configuration identified by {@code itemName}
     * 
     */
    public InsteonHubBindingConfig getItemConfig(String itemName);

    /**
     * Get all hubId+devices
     * 
     * return Set of devices
     */
    public Set<InsteonHubBindingDeviceInfo> getConfiguredDevices();

    /**
     * Returns the openhab Item Names configured for {@code deviceInfo}
     * 
     * @param deviceInfo
     *            device info (hubId+deviceId)
     *            return Set of Item Names
     */
    public Set<String> getDeviceItemNames(InsteonHubBindingDeviceInfo deviceInfo);

}
