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
package org.openhab.io.gpio;

import java.io.IOException;

/**
 * Base interface for interacting with GPIO subsystem. Implementation
 * class should be dynamically registered as OSGi service in bundle
 * activator code if underlying platform is one of the supported.
 *
 * @author Dancho Penev
 * @since 1.5.0
 */
public interface GPIO {

    /**
     * Creates and initializes backend object representing GPIO pin.
     * Further pin manipulations are made using methods exposed by
     * <code>GPIOPin</code> interface.
     * 
     * @param pinNumber platform specific pin number
     * @return object representing the GPIO pin
     * @throws IOException in case of inability to initialize the pin
     */
    public GPIOPin reservePin(Integer pinNumber) throws IOException;

    /**
     * Creates and initializes backend object representing GPIO pin.
     * Further pin manipulations are made using methods exposed by
     * <code>GPIOPin</code> interface.
     * 
     * @param pinNumber platform specific pin number
     * @param force force reservation of pin
     * 
     * @return object representing the GPIO pin
     * @throws IOException in case of inability to initialize the pin
     */
    public GPIOPin reservePin(Integer pinNumber, boolean force) throws IOException;

    /**
     * Uninitializes backend object and free used resources. Further
     * using of this pin object is invalid.
     * 
     * @param pin object representing already initialized GPIO pin
     * @throws IOException in case of inability to uninitialize the pin
     */
    public void releasePin(GPIOPin pin) throws IOException;

    /**
     * Query default debounce interval.
     * 
     * @return current default debounce interval
     */
    public long getDefaultDebounceInterval();
}
