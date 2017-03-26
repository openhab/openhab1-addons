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

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.net.ModbusSlaveConnection;

/**
 *
 * @author hg8496
 */
public abstract class ModbusIPSlave extends ModbusSlave {

    public ModbusIPSlave(String slave, KeyedObjectPool<ModbusSlaveEndpoint, ModbusSlaveConnection> connectionPool) {
        super(slave, connectionPool);
        updateEndpoint();
    }

    /** host address */
    protected String host;
    /** connection port. Default 502 */
    protected int port = Modbus.DEFAULT_PORT;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
        updateEndpoint();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
        updateEndpoint();
    }

    protected abstract void updateEndpoint();

}
