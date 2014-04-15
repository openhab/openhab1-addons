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

import net.wimpi.modbus.io.ModbusUDPTransport;

/**
 * Interface defining a <tt>UDPTerminal</tt>.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public interface UDPTerminal {

  /**
   * Returns the local address of this <tt>UDPTerminal</tt>.
   *
   * @return an <tt>InetAddress</tt> instance.
   */
  public InetAddress getLocalAddress();

  /**
   * Returns the local port of this <tt>UDPTerminal</tt>.
   *
   * @return the local port as <tt>int</tt>.
   */
  public int getLocalPort();

  /**
   * Tests if this <tt>UDPTerminal</tt> is active.
   *
   * @return <tt>true</tt> if active, <tt>false</tt> otherwise.
   */
  public boolean isActive();

  /**
   * Activate this <tt>UDPTerminal</tt>.
   *
   * @throws java.lang.Exception if there is a network failure.
   */
  public void activate() throws Exception;

  /**
   * Deactivates this <tt>UDPTerminal</tt>.
   */
  public void deactivate();

  /**
   * Returns the <tt>ModbusTransport</tt> associated with this
   * <tt>UDPTerminal</tt>.
   *
   * @return a <tt>ModbusTransport</tt> instance.
   */
  public ModbusUDPTransport getModbusTransport();

  /**
   * Sends the given message.
   *
   * @param msg the message as <tt>byte[]</tt>.
   * @throws Exception if sending the message fails.
   */
  public void sendMessage(byte[] msg) throws Exception;

  /**
   * Receives and returns a message.
   *
   * @return the message as a newly allocated <tt>byte[]</tt>.
   * @throws Exception if receiving a message fails.
   */
  public byte[] receiveMessage() throws Exception;

}//interface UDPTerminal
