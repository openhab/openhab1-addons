package net.wimpi.modbus.net;

import java.net.InetAddress;

public interface UDPSlaveTerminalFactory {
    public UDPSlaveTerminal create(InetAddress interfac, int port);
}