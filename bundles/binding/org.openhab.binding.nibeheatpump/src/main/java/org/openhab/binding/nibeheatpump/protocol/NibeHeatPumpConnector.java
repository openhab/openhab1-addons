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
package org.openhab.binding.nibeheatpump.protocol;

import org.openhab.binding.nibeheatpump.internal.NibeHeatPumpException;

/**
 * Base class for Nibe heat pump communication.
 *
 * @author Pauli Anttila
 * @since 1.3.0
 */
public abstract class NibeHeatPumpConnector {

    /**
     * Procedure for connect to heat pump.
     * 
     * @throws NibeHeatPumpException
     */
    public abstract void connect() throws NibeHeatPumpException;

    /**
     * Procedure for disconnect from heat pump.
     * 
     * @throws NibeHeatPumpException
     */
    public abstract void disconnect() throws NibeHeatPumpException;

    /**
     * Procedure for receiving datagram from heat pump.
     * 
     * @throws NibeHeatPumpException
     */
    public abstract byte[] receiveDatagram() throws NibeHeatPumpException;

}
