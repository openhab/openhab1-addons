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
package org.openhab.binding.fritzboxtr064.internal;

import java.util.Set;

/***
 * Represents a item mapping. An item mapping is a collection of all parameters
 * based on the config string of the item. config string is like the key of an item mapping.
 * Item Mappings must be created manually to support desired fbox tr064 functions.
 * Since the FritzBox SOAP services typically return several values in one request,
 * an item mapping can map a single {@link #getReadServiceCommand() service command} to
 * multiple {@link #getItemCommands() item commands}. This is used to fetch all configured
 * items of a service command in a single call.
 *
 * @author gitbock
 *
 */
public interface ItemMap {
    /**
     * @return Names of the item commands which are provided by the response to the
     *         {@link #getItemArgumentName(String) SOAP service command} of this map.
     */
    Set<String> getItemCommands();

    /**
     * Get the name of the XML element in the TR064 service call which contains the value of the given command.
     * For writing calls, this is an element in the service request, for reading calls in the service response.
     *
     * @return Name of the XML element for the item value.
     */
    String getItemArgumentName(String itemCommand);

    String getServiceId();

    String getReadServiceCommand();

    SoapValueParser getSoapValueParser();
}
