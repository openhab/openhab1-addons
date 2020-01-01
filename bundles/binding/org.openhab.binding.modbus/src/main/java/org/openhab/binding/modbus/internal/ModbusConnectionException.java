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
package org.openhab.binding.modbus.internal;

import org.openhab.binding.modbus.internal.pooling.ModbusSlaveEndpoint;

@SuppressWarnings("serial")
public class ModbusConnectionException extends Exception {

    private ModbusSlaveEndpoint endpoint;

    public ModbusConnectionException(ModbusSlaveEndpoint endpoint) {
        this.endpoint = endpoint;

    }

    public ModbusSlaveEndpoint getEndpoint() {
        return endpoint;
    }

}
