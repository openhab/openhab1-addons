/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import org.apache.commons.pool2.KeyedObjectPool;
import org.openhab.binding.modbus.internal.pooling.ModbusSlaveEndpoint;
import org.openhab.binding.modbus.internal.pooling.ModbusTCPSlaveEndpoint;

import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.net.ModbusSlaveConnection;
import net.wimpi.modbus.net.TCPMasterConnection;

/**
 * ModbusSlave class instantiates physical Modbus slave.
 * It is responsible for polling data from physical device using TCPConnection.
 * It is also responsible for updating physical devices according to OpenHAB commands
 *
 * @author Dmitry Krasnov
 * @since 1.1.0
 */
public class ModbusTcpSlave extends ModbusIPSlave {

    public ModbusTcpSlave(String slave, KeyedObjectPool<ModbusSlaveEndpoint, ModbusSlaveConnection> connectionPool) {
        super(slave, connectionPool);
        transaction = new ModbusTCPTransaction();
        ((ModbusTCPTransaction) transaction).setReconnecting(false);
    }

    /**
     * Performs physical write to device when slave type is "holding" using Modbus FC06 function
     *
     * @param command command received from OpenHAB
     * @param readRegister reference to the register that stores current value
     * @param writeRegister register reference to write data to
     */

    @Override
    protected ModbusSlaveConnection getConnection(ModbusSlaveEndpoint endpoint) {
        ModbusSlaveConnection connection = super.getConnection(endpoint);
        if (connection == null) {
            return null;
        }
        if (!(connection instanceof TCPMasterConnection)) {
            // should not happen
            throw new IllegalStateException("Should not happen: wrong connection type for slave " + name);
        }
        ((ModbusTCPTransaction) transaction).setConnection((TCPMasterConnection) connection);
        return connection;
    }

    @Override
    protected void updateEndpoint() {
        endpoint = new ModbusTCPSlaveEndpoint(getHost(), getPort());
    }

}
