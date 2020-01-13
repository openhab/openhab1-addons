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
package org.openhab.binding.opensprinkler;

import org.openhab.core.binding.BindingProvider;

/**
 * A custom binding provide for OpenSprinkler, to allow for configuration to
 * specify a station number for a given switch.
 *
 * @author Jonathan Giles (http://www.jonathangiles.net)
 * @since 1.3.0
 */
public interface OpenSprinklerBindingProvider extends BindingProvider {

    /**
     * Returns the station number specified for item {@code itemName}.
     */
    public int getStationNumber(String itemName);

    /**
     * Returns the command value for item {@code itemName}.
     */
    public String getCommand(String itemName);

}
