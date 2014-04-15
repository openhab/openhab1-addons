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

import java.net.InetAddress;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.io.ModbusUDPTransport;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;

/**
 * Class that implements a ModbusUDPListener.<br>
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class ModbusUDPListener {

  private UDPSlaveTerminal m_Terminal;
  private ModbusUDPHandler m_Handler;
  private Thread m_HandlerThread;
  private int m_Port = Modbus.DEFAULT_PORT;
  private boolean m_Listening;
  private InetAddress m_Interface;

  /**
   * Constructs a new ModbusUDPListener instance.
   */
  public ModbusUDPListener() {
  }//ModbusUDPListener

  /**
   * Create a new <tt>ModbusUDPListener</tt> instance
   * listening to the given interface address.
   *
   * @param ifc an <tt>InetAddress</tt> instance.
   */
  public ModbusUDPListener(InetAddress ifc) {
    m_Interface = ifc;
  }//ModbusUDPListener

  /**
   * Returns the number of the port this <tt>ModbusUDPListener</tt>
   * is listening to.
   *
   * @return the number of the IP port as <tt>int</tt>.
   */
  public int getPort() {
    return m_Port;
  }//getPort

  /**
   * Sets the number of the port this <tt>ModbusUDPListener</tt>
   * is listening to.
   *
   * @param port the number of the IP port as <tt>int</tt>.
   */
  public void setPort(int port) {
    m_Port = ((port>0)? port : Modbus.DEFAULT_PORT);
  }//setPort

  /**
   * Starts this <tt>ModbusUDPListener</tt>.
   */
  public void start() {
    //start listening
    try {
      if(m_Interface == null) {
        m_Terminal = new UDPSlaveTerminal(InetAddress.getLocalHost());
      } else {
        m_Terminal = new UDPSlaveTerminal(m_Interface);
      }
      m_Terminal.setLocalPort(m_Port);
      m_Terminal.activate();

      m_Handler = new ModbusUDPHandler(m_Terminal.getModbusTransport());
      m_HandlerThread = new Thread(m_Handler);
      m_HandlerThread.start();

    } catch (Exception e) {
      //FIXME: this is a major failure, how do we handle this
    }
    m_Listening = true;
  }//start

  /**
   * Stops this <tt>ModbusUDPListener</tt>.
   */
  public void stop() {
    //stop listening
    m_Terminal.deactivate();
    m_Handler.stop();
    m_Listening = false;
  }//stop

  /**
   * Tests if this <tt>ModbusTCPListener</tt> is listening
   * and accepting incoming connections.
   *
   * @return true if listening (and accepting incoming connections),
   *          false otherwise.
   */
  public boolean isListening() {
    return m_Listening;
  }//isListening

  class ModbusUDPHandler
      implements Runnable {

    private ModbusUDPTransport m_Transport;
    private boolean m_Continue = true;

    public ModbusUDPHandler(ModbusUDPTransport transport) {
      m_Transport = transport;
    }//constructor

    public void run() {
      try {
        do {
          //1. read the request
          ModbusRequest request = m_Transport.readRequest();
          //System.out.println("Request:" + request.getHexMessage());
          ModbusResponse response = null;

          //test if Process image exists
          if (ModbusCoupler.getReference().getProcessImage() == null) {
            response =
                request.createExceptionResponse(Modbus.ILLEGAL_FUNCTION_EXCEPTION);
          } else {
            response = request.createResponse();
          }
          /*DEBUG*/
          if (Modbus.debug) System.out.println("Request:" + request.getHexMessage());
          if (Modbus.debug) System.out.println("Response:" + response.getHexMessage());

          //System.out.println("Response:" + response.getHexMessage());
          m_Transport.writeMessage(response);
        } while (m_Continue);
      } catch (ModbusIOException ex) {
        if (!ex.isEOF()) {
          //other troubles, output for debug
          ex.printStackTrace();
        }
      } finally {
        try {
          m_Terminal.deactivate();
        } catch (Exception ex) {
          //ignore
        }
      }
    }//run

    public void stop() {
      m_Continue = false;
    }//stop

  }//inner class ModbusUDPHandler

}//class ModbusUDPListener
