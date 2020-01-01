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
package org.openhab.binding.mcp23017;

import org.openhab.core.binding.BindingProvider;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;

/**
 * @author Diego A. Fliess
 * @since 1.9.0
 */
public interface MCP23017BindingProvider extends BindingProvider {

    /**
     * @return the busAddress
     */
    public int getBusAddress(String itemName);

    /**
     * @return the pin
     */
    public Pin getPin(String itemName);

    /**
     * @return the defaultState
     */
    public PinState getDefaultState(String itemName);

    /**
     * @return the direction
     */
    public PinMode getPinMode(String itemName);
}
