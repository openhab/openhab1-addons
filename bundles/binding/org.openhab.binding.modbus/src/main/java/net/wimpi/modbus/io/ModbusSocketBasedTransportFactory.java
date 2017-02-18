package net.wimpi.modbus.io;

import java.net.Socket;

public interface ModbusSocketBasedTransportFactory {
  public ModbusTransport create(Socket socket);
}