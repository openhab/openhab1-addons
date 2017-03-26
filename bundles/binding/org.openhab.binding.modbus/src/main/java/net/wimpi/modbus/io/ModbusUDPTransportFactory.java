package net.wimpi.modbus.io;

import net.wimpi.modbus.net.UDPTerminal;

public interface ModbusUDPTransportFactory {
    public ModbusTransport create(UDPTerminal terminal);
}