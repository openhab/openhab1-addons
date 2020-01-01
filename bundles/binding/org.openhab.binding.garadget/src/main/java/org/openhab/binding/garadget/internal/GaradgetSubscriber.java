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

import java.util.ArrayList;
import java.util.List;

import org.openhab.core.items.Item;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class parses and holds the device and variable names when calling the device over the REST API.
 *
 * @author John Cocula
 * @since 1.9.0
 */
public class GaradgetSubscriber {

    private final String deviceId;
    private final String varName;
    private List<Class<? extends State>> acceptedDataTypes;

    /**
     * Construct a Garadget subscriber to receive updates from device fields and variables.
     *
     * @param item
     *            The item to which state updates will be posted
     * @param configuration
     *            The string version of the device ID and variable or field name
     * @throws BindingConfigParseException
     *             if the string representation is incorrectly formatted
     */
    public GaradgetSubscriber(Item item, String configuration) throws BindingConfigParseException {
        acceptedDataTypes = new ArrayList<Class<? extends State>>(item.getAcceptedDataTypes());

        String[] parts = configuration.split("#");
        if (parts.length != 2 || isEmpty(parts[0]) || isEmpty(parts[1])) {
            throw new BindingConfigParseException("Invalid binding part: " + configuration);
        }
        deviceId = parts[0];
        varName = parts[1];
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getVarName() {
        return varName;
    }

    /**
     * The data types that are accepted by the receiving {@link Item}
     *
     * @return
     *         the accepted data types list
     */
    public List<Class<? extends State>> getAcceptedDataTypes() {
        return acceptedDataTypes;
    }
}
