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
package org.openhab.binding.primare.internal.protocol.spa20;

import org.openhab.binding.primare.internal.protocol.PrimareSerialConnector;
import org.openhab.binding.primare.internal.protocol.PrimareTCPConnector;

/**
 * Connector for Primare SP31/SP31.7/SPA20/SPA21 serial communication.
 *
 * @author Veli-Pekka Juslin
 * @since 1.7.0
 */
public class PrimareSPA20SerialConnector extends PrimareSerialConnector {

    /**
     * Create a {@link PrimareTCPConnector} capable of communicating
     * with a Primare SP31/SP31.7/SPA20/SPA21 device using a serial port
     *
     * @param deviceId
     *            device id as given in OpenHAB configuration
     * @param serialPortName
     *            serial port name (e.g. /dev/ttyS0)
     *
     * @return Primare Serial connector instance
     */
    public PrimareSPA20SerialConnector(String deviceId, String serialPortName) {
        super(deviceId, serialPortName, new PrimareSPA20MessageFactory(), new PrimareSPA20ResponseFactory());
    }
}
