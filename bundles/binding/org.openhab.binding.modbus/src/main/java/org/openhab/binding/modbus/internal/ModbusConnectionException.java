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
