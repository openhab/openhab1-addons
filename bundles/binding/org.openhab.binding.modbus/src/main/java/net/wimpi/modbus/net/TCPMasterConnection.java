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

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusTCPTransport;
import net.wimpi.modbus.io.ModbusTransport;

/**
 * Class that implements a TCPMasterConnection.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class TCPMasterConnection {

  //instance attributes
  private Socket m_Socket;
  private int m_Timeout = Modbus.DEFAULT_TIMEOUT;
  private boolean m_Connected;

  private InetAddress m_Address;
  private int m_Port = Modbus.DEFAULT_PORT;

  //private int m_Retries = Modbus.DEFAULT_RETRIES;
  private ModbusTCPTransport m_ModbusTransport;

  /**
   * Constructs a <tt>TCPMasterConnection</tt> instance
   * with a given destination address.
   *
   * @param adr the destination <tt>InetAddress</tt>.
   */
  public TCPMasterConnection(InetAddress adr) {
    m_Address = adr;
  }//constructor

  /**
   * Opens this <tt>TCPMasterConnection</tt>.
   *
   * @throws Exception if there is a network failure.
   */
  public synchronized void connect()
      throws Exception {
    if(!m_Connected) {
      if(Modbus.debug) System.out.println("connect()");
      m_Socket = new Socket(m_Address, m_Port);
      setTimeout(m_Timeout);
      prepareTransport();
      m_Connected = true;
    }
  }//connect

  /**
   * Closes this <tt>TCPMasterConnection</tt>.
   */
  public void close() {
    if(m_Connected) {
      try {
        m_ModbusTransport.close();
      } catch (IOException ex) {
        if(Modbus.debug) System.out.println("close()");
      }
      m_Connected = false;
    }
  }//close

  /**
   * Returns the <tt>ModbusTransport</tt> associated with this
   * <tt>TCPMasterConnection</tt>.
   *
   * @return the connection's <tt>ModbusTransport</tt>.
   */
  public ModbusTransport getModbusTransport() {
    return m_ModbusTransport;
  }//getModbusTransport

  /**
   * Prepares the associated <tt>ModbusTransport</tt> of this
   * <tt>TCPMasterConnection</tt> for use.
   *
   * @throws IOException if an I/O related error occurs.
   */
  private void prepareTransport() throws IOException {
    if (m_ModbusTransport == null) {
      m_ModbusTransport = new ModbusTCPTransport(m_Socket);
    } else {
      m_ModbusTransport.setSocket(m_Socket);
    }
  }//prepareIO

  /**
   * Returns the timeout for this <tt>TCPMasterConnection</tt>.
   *
   * @return the timeout as <tt>int</tt>.
   */
  public int getTimeout() {
    return m_Timeout;
  }//getReceiveTimeout

  /**
   * Sets the timeout for this <tt>TCPMasterConnection</tt>.
   *
   * @param timeout the timeout as <tt>int</tt>.
   */
  public void setTimeout(int timeout) {
    m_Timeout = timeout;
    if(m_Socket != null) {
      try {
        m_Socket.setSoTimeout(m_Timeout);
      } catch (IOException ex) {
        //handle?
      } 
    }
  }//setReceiveTimeout

  /**
   * Returns the destination port of this
   * <tt>TCPMasterConnection</tt>.
   *
   * @return the port number as <tt>int</tt>.
   */
  public int getPort() {
    return m_Port;
  }//getPort

  /**
   * Sets the destination port of this
   * <tt>TCPMasterConnection</tt>.
   * The default is defined as <tt>Modbus.DEFAULT_PORT</tt>.
   *
   * @param port the port number as <tt>int</tt>.
   */
  public void setPort(int port) {
    m_Port = port;
  }//setPort

  /**
   * Returns the destination <tt>InetAddress</tt> of this
   * <tt>TCPMasterConnection</tt>.
   *
   * @return the destination address as <tt>InetAddress</tt>.
   */
  public InetAddress getAddress() {
    return m_Address;
  }//getAddress

  /**
   * Sets the destination <tt>InetAddress</tt> of this
   * <tt>TCPMasterConnection</tt>.
   *
   * @param adr the destination address as <tt>InetAddress</tt>.
   */
  public void setAddress(InetAddress adr) {
    m_Address = adr;
  }//setAddress

  /**
   * Tests if this <tt>TCPMasterConnection</tt> is connected.
   *
   * @return <tt>true</tt> if connected, <tt>false</tt> otherwise.
   */
  public boolean isConnected() {
    return m_Connected;
  }//isConnected


}//class TCPMasterConnection
