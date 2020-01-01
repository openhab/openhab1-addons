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
package org.openhab.binding.gc100ir;

import org.openhab.core.binding.BindingProvider;

/**
 * Binding provider interface. Defines how to get properties from a binding
 * configuration.
 *
 * @author Parikshit Thakur & Team
 * @since 1.9.0
 */
public interface GC100IRBindingProvider extends BindingProvider {
    /**
     * Gets the Global Cache IR Instance Name associated with a given <code>itemname</code>.
     * 
     * @param itemname The item name associated with an instance of a Global Cache IR.
     * @return String format of the Global Cache Instance Name
     */
    String getGC100InstanceName(String itemname);

    /**
     * Returns the numeric value associated with the module port number on your global cache IR
     * 
     * @param itemname The item name to get the port number for
     * @return Integer representing the module number for the global cache IR instance
     */
    int getGC100Module(String itemname);

    /**
     * Returns the connector number associated with the global cache IR instance
     * 
     * @param itemname The item name to get the connector number for
     * @return Integer representing the connector to use
     */
    int getGC100Connector(String itemname);

    /**
     * A Global Cache formatted code string for a given <code>itemname</code>
     * 
     * @param itemname The item name to get the associated Global Cache formatted string for
     * @return A String representation of Global Cache formatted code
     */
    String getCode(String itemname);
}
