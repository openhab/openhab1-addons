/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
     *         {@link #getReadDataOutName(String) SOAP service command} of this map.
     */
    Set<String> getItemCommands();

    /**
     * @param itemCommand
     * @return Name of the XML element in the service response which contains the value of the given command.
     */
    String getReadDataOutName(String itemCommand);

    String getServiceId();

    String getReadServiceCommand();

    SoapValueParser getSoapValueParser();
}
