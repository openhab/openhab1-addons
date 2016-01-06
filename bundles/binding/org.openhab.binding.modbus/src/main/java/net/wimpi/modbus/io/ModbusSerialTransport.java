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

package net.wimpi.modbus.io;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.msg.ModbusMessage;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;
import net.wimpi.modbus.util.ModbusUtil;

import java.io.IOException;

import gnu.io.CommPort;

import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.UnsupportedCommOperationException;

/**
 * Abstract base class for serial <tt>ModbusTransport</tt>
 * implementations.
 *
 * @author Dieter Wimberger
 * @author John Charlton
 *
 * @version @version@ (@date@)
 */
abstract public class ModbusSerialTransport
    implements ModbusTransport {

  private static final Logger logger = LoggerFactory.getLogger(ModbusSerialTransport.class);
  protected CommPort  m_CommPort;
  protected boolean   m_Echo = false;     // require RS-485 echo processing

  /**
   * <code>prepareStreams</code> prepares the input and output streams of this
   * <tt>ModbusSerialTransport</tt> instance.
   *
   * @param in the input stream to be read from.
   * @param out the output stream to write to.
   * @throws IOException if an I\O error occurs.
   */
  abstract public void prepareStreams(InputStream in, OutputStream out)
      throws IOException;

  /**
   *  <code>readResponse</code> reads a response message from the slave
   *  responding to a master writeMessage request.
   *
   * @return a <code>ModbusResponse</code> value
   * @exception ModbusIOException if an error occurs
   */
  abstract public ModbusResponse readResponse()
      throws ModbusIOException;
  
  /**
   * The <code>readRequest</code> method listens continuously on the serial
   * input stream for master request messages and replies if the request slave
   * ID matches its own set in ModbusCoupler.getUnitID().
   *
   * @return a <code>ModbusRequest</code> value
   * @exception ModbusIOException if an error occurs
   */
  abstract public ModbusRequest readRequest()
    throws ModbusIOException;
  
  /**
   * The <code>writeMessage</code> method writes a modbus serial message to
   * its serial output stream to a specified slave unit ID.
   *
   * @param msg a <code>ModbusMessage</code> value
   * @exception ModbusIOException if an error occurs
   */
  abstract public void writeMessage(ModbusMessage msg)
    throws ModbusIOException;


  /**
   * The <code>close</code> method closes the serial input/output streams.
   *
   * @exception IOException if an error occurs
   */
  public void close() throws IOException {
    if (m_CommPort != null) {
      m_CommPort.close();
    }
  }
  
  /**
   * <code>setCommPort</code> sets the comm port member and prepares the input
   * and output streams to be used for reading from and writing to.
   *
   * @param cp the comm port to read from/write to.
   * @throws IOException if an I/O related error occurs.
   */
  public void setCommPort(CommPort cp) throws IOException {
    // Close existing port, if any
    close();
    m_CommPort = cp;
    if (cp != null) {
      prepareStreams(cp.getInputStream(), cp.getOutputStream());
    }
  }
  
  /**
   * <code>isEcho</code> method returns the output echo state.
   *
   * @return a <code>boolean</code> value
   */
  public boolean isEcho() {
    return m_Echo;
  }//isEcho

  /**
   * <code>setEcho</code> method sets the output echo state.
   *
   * @param b a <code>boolean</code> value
   */
  public void setEcho(boolean b) {
    this.m_Echo = b;
  }//setEcho


  /**
   * Describe <code>setReceiveThreshold</code> method here.
   *
   * @param th an <code>int</code> value
   */
  public void setReceiveThreshold(int th) {
    try {
      m_CommPort.enableReceiveThreshold(th); /* chars */
    } catch (UnsupportedCommOperationException e) {
      logger.error("Failed to setReceiveThreshold: {}", e.getMessage());
    }
  }
  
  /**
   * Describe <code>setReceiveTimeout</code> method here.
   *
   * @param ms an <code>int</code> value
   */
  public void setReceiveTimeout(int ms) {
    try {
      m_CommPort.enableReceiveTimeout(ms); /* milliseconds */
    } catch (UnsupportedCommOperationException e) {
      logger.error("Failed to setReceiveTimeout: {}", e.getMessage());
    }
  }

  /**
   * Reads the own message echo produced in RS485 Echo Mode
   * within the given time frame.
   *
   * @param len is the length of the echo to read.  Timeout will occur if the
   * echo is not received in the time specified in the SerialConnection.
   *
   * @throws IOException if a I/O error occurred.
   */
  public void readEcho(int len) throws IOException {

    byte echoBuf[] = new byte[len];
    setReceiveThreshold(len);
    int echoLen = m_CommPort.getInputStream().read(echoBuf, 0, len);
    
    logger.debug("Echo: {}", ModbusUtil.toHex(echoBuf, 0, echoLen));
    m_CommPort.disableReceiveThreshold();
    if (echoLen != len) {
      final String errMsg = "Echo not received";
      logger.error("Transmit {}", errMsg);
      throw new IOException(errMsg);
    }
  }//readEcho

  
}//interface ModbusSerialTransport
