/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import net.wimpi.modbus.Modbus;

/**
 *
 * @author hg8496
 */
public abstract class ModbusIPSlave extends ModbusSlave {
    
    /** host address */
    protected String host;
    /** connection port. Default 502 */
    protected int port = Modbus.DEFAULT_PORT;

    public ModbusIPSlave(String slave) {
        super(slave);
    }

    String getHost() {
        return host;
    }

    void setHost(String host) {
        this.host = host;
    }

    int getPort() {
        return port;
    }

    void setPort(int port) {
        this.port = port;
    }
    
}
