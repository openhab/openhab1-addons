package org.openhab.binding.modbus.internal;

public interface ModbusSlaveConnection {
	public boolean connect();
	public void resetConnection();
	public boolean isConnected();

}
