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
package org.openhab.binding.openenergymonitor.protocol;

import org.openhab.binding.openenergymonitor.internal.OpenEnergyMonitorException;

/**
 * Base class for Open Energy Monitor communication.
 *
 * @author Pauli Anttila
 * @since 1.4.0
 */
public abstract class OpenEnergyMonitorConnector {

    /**
     * Procedure for connect to Open Energy Monitor system.
     * 
     * @throws OpenEnergyMonitorException
     */
    public abstract void connect() throws OpenEnergyMonitorException;

    /**
     * Procedure for disconnect from Open Energy Monitor system.
     * 
     * @throws OpenEnergyMonitorException
     */
    public abstract void disconnect() throws OpenEnergyMonitorException;

    /**
     * Procedure for receiving datagram from Open Energy Monitor devices.
     * 
     * @throws OpenEnergyMonitorException
     */
    public abstract byte[] receiveDatagram() throws OpenEnergyMonitorException;

}
