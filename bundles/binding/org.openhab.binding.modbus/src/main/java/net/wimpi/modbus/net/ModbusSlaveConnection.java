/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
