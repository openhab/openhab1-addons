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
package org.openhab.binding.fritzaha;

import org.openhab.binding.fritzaha.internal.hardware.interfaces.FritzahaDevice;
import org.openhab.core.binding.BindingProvider;

/**
 * Interface for FritzAHA binding providers
 *
 * @author Christian Brauers
 * @since 1.3.0
 */
public interface FritzahaBindingProvider extends BindingProvider {
    /**
     * Gets device config corresponding to the item specified.
     * 
     * @param itemName
     *            Name of the item for which to get the device config
     * @return Device config corresponding to item
     */
    public FritzahaDevice getDeviceConfig(String itemName);
}
