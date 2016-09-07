package net.wimpi.modbus.net;

import java.net.InetAddress;
import java.net.Socket;

public interface UDPSlaveTerminalFactory {
  public UDPSlaveTerminal create(InetAddress interfac, int port);
}