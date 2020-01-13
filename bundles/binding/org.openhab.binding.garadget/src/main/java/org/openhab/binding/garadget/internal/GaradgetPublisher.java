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
package org.openhab.binding.garadget.internal;

import static org.apache.commons.lang.StringUtils.isEmpty;

import org.openhab.core.items.Item;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class parses and holds the device and function names when calling the device over the REST API.
 *
 * @author John Cocula
 * @since 1.9.0
 */
public class GaradgetPublisher {
    private final String deviceId;
    private final String funcName;

    /**
     * Construct a GaradgetPublisher.
     *
     * @param item
     *            The @{link Item} to which commands are sent for publishing to Garadget
     * @param configuration
     *            The string version of the device ID and function name
     * @throws BindingConfigParseException
     *             if the string format is incorrect
     */
    public GaradgetPublisher(Item item, String configuration) throws BindingConfigParseException {
        String[] parts = configuration.split("#");
        if (parts.length != 2 || isEmpty(parts[0]) || isEmpty(parts[1])) {
            throw new BindingConfigParseException("Invalid binding part: " + configuration);
        }
        deviceId = parts[0];
        funcName = parts[1];
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getFuncName() {
        return funcName;
    }
}
