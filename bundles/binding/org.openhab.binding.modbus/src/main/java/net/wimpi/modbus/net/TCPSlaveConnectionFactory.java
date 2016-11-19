package net.wimpi.modbus.net;

import java.net.Socket;

public interface TCPSlaveConnectionFactory {
  public TCPSlaveConnection create(Socket socket);
}