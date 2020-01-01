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
package net.wimpi.modbus.net;

public interface ModbusSlaveConnection {
    /**
     * Connects the connection to the endpoint
     *
     * @return whether connection was successfull
     * @throws Exception on any connection errors
     */
    public boolean connect() throws Exception;

    /**
     * Close connection and free associated resources
     */
    public void resetConnection();

    /**
     *
     * @return whether connection is now fully connected
     */
    public boolean isConnected();

}
