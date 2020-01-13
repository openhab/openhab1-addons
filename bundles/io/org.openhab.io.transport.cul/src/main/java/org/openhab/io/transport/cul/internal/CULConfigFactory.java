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
package org.openhab.io.transport.cul.internal;

import java.util.Dictionary;

import org.openhab.io.transport.cul.CULMode;
import org.osgi.service.cm.ConfigurationException;

/**
 * Configuration factory for cul handlers.
 *
 * @author Patrick Ruckstuhl
 * @since 1.9.0
 */
public interface CULConfigFactory {

    /**
     * Creates a specific CULConfig for a handler implementation.
     *
     * @param deviceType type of the device
     * @param deviceAddress address of the device
     * @param mode cul mode
     * @param config additional configurations.
     * @return CULConfig for the handler implementation
     * @throws ConfigurationException if configuration is not valid.
     */
    CULConfig create(String deviceType, String deviceAddress, CULMode mode, Dictionary<String, ?> config)
            throws ConfigurationException;

}
