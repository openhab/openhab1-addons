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
import org.openhab.binding.modbus.internal.pooling.ModbusUDPSlaveEndpoint;

import net.wimpi.modbus.io.ModbusUDPTransaction;
import net.wimpi.modbus.net.ModbusSlaveConnection;
import net.wimpi.modbus.net.UDPMasterConnection;

/**
 * ModbusSlave class instantiates physical Modbus slave.
 * It is responsible for polling data from physical device using TCPConnection.
 * It is also responsible for updating physical devices according to OpenHAB commands
 *
 * @author Dmitry Krasnov
 * @since 1.1.0
 */
public class ModbusUdpSlave extends ModbusIPSlave {

    public ModbusUdpSlave(String slave, KeyedObjectPool<ModbusSlaveEndpoint, ModbusSlaveConnection> connectionPool) {
        super(slave, connectionPool);
        transaction = new ModbusUDPTransaction();
    }

    @Override
    protected ModbusSlaveConnection getConnection(ModbusSlaveEndpoint endpoint) {
        ModbusSlaveConnection connection = super.getConnection(endpoint);
        if (connection == null) {
            return null;
        }
        if (!(connection instanceof UDPMasterConnection)) {
            throw new IllegalStateException("Should not happen: wrong connection type for slave " + name);
        }
        ((ModbusUDPTransaction) transaction).setTerminal(((UDPMasterConnection) connection).getTerminal());
        return connection;
    }

    @Override
    protected void updateEndpoint() {
        endpoint = new ModbusUDPSlaveEndpoint(getHost(), getPort());
    }
}
