/***
 * Copyright 2002-2010 jamod development team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***/

package net.wimpi.modbus.net;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusTransport;

import java.net.InetAddress;

/**
 * Class that implements a UDPMasterConnection.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class UDPMasterConnection {

  //instance attributes
  private UDPMasterTerminal m_Terminal;
  private int m_Timeout = Modbus.DEFAULT_TIMEOUT;
  private boolean m_Connected;

  private InetAddress m_Address;
  private int m_Port = Modbus.DEFAULT_PORT;

  /**
   * Constructs a <tt>UDPMasterConnection</tt> instance
   * with a given destination address.
   *
   * @param adr the destination <tt>InetAddress</tt>.
   */
  public UDPMasterConnection(InetAddress adr) {
    m_Address = adr;
  }//constructor

  /**
   * Opens this <tt>UDPMasterConnection</tt>.
   *
   * @throws Exception if there is a network failure.
   */
  public synchronized void connect()
      throws Exception {
    if (!m_Connected) {
      m_Terminal = new UDPMasterTerminal();
      m_Terminal.setLocalAddress(InetAddress.getLocalHost());
      m_Terminal.setLocalPort(5000);
      m_Terminal.setRemoteAddress(m_Address);
      m_Terminal.setRemotePort(m_Port);
      m_Terminal.setTimeout(m_Timeout);
      m_Terminal.activate();
      m_Connected = true;
    }
  }//connect

  /**
   * Closes this <tt>UDPMasterConnection</tt>.
   */
  public void close() {
    if (m_Connected) {
      try {
        m_Terminal.deactivate();
      } catch (Exception ex) {
        if (Modbus.debug) ex.printStackTrace();
      }
      m_Connected = false;
    }
  }//close

  /**
   * Returns the <tt>ModbusTransport</tt> associated with this
   * <tt>UDPMasterConnection</tt>.
   *
   * @return the connection's <tt>ModbusTransport</tt>.
   */
  public ModbusTransport getModbusTransport() {
    return m_Terminal.getModbusTransport();
  }//getModbusTransport

  /**
   * Returns the terminal used for handling the package traffic.
   *
   * @return a <tt>UDPTerminal</tt> instance.
   */
  public UDPTerminal getTerminal() {
    return m_Terminal;
  }//getTerminal

  /**
   * Returns the timeout for this <tt>UDPMasterConnection</tt>.
   *
   * @return the timeout as <tt>int</tt>.
   */
  public int getTimeout() {
    return m_Timeout;
  }//getReceiveTimeout

  /**
   * Sets the timeout for this <tt>UDPMasterConnection</tt>.
   *
   * @param timeout the timeout as <tt>int</tt>.
   */
  public void setTimeout(int timeout) {
    m_Timeout = timeout;
    m_Terminal.setTimeout(timeout);
  }//setReceiveTimeout

  /**
   * Returns the destination port of this
   * <tt>UDPMasterConnection</tt>.
   *
   * @return the port number as <tt>int</tt>.
   */
  public int getPort() {
    return m_Port;
  }//getPort

  /**
   * Sets the destination port of this
   * <tt>UDPMasterConnection</tt>.
   * The default is defined as <tt>Modbus.DEFAULT_PORT</tt>.
   *
   * @param port the port number as <tt>int</tt>.
   */
  public void setPort(int port) {
    m_Port = port;
  }//setPort

  /**
   * Returns the destination <tt>InetAddress</tt> of this
   * <tt>UDPMasterConnection</tt>.
   *
   * @return the destination address as <tt>InetAddress</tt>.
   */
  public InetAddress getAddress() {
    return m_Address;
  }//getAddress

  /**
   * Sets the destination <tt>InetAddress</tt> of this
   * <tt>UDPMasterConnection</tt>.
   *
   * @param adr the destination address as <tt>InetAddress</tt>.
   */
  public void setAddress(InetAddress adr) {
    m_Address = adr;
  }//setAddress

  /**
   * Tests if this <tt>UDPMasterConnection</tt> is connected.
   *
   * @return <tt>true</tt> if connected, <tt>false</tt> otherwise.
   */
  public boolean isConnected() {
    return m_Connected;
  }//isConnected

}//class UDPMasterConnection
