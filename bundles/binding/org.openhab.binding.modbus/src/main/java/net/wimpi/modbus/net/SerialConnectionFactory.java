package net.wimpi.modbus.net;

import net.wimpi.modbus.util.SerialParameters;

public interface SerialConnectionFactory {
  public SerialConnection create(SerialParameters parameters);
}