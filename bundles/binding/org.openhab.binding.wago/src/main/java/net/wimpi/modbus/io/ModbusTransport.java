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

import java.io.IOException;

import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.msg.ModbusMessage;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;

/**
 * Interface defining the I/O mechanisms for
 * <tt>ModbusMessage</tt> instances.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public interface ModbusTransport {

  /**
   * Closes the raw input and output streams of
   * this <tt>ModbusTransport</tt>.
   * <p>
   * @throws IOException if a stream
   *         cannot be closed properly.
   */
  public void close() throws IOException;

  /**
   * Writes a <tt<ModbusMessage</tt> to the
   * output stream of this <tt>ModbusTransport</tt>.
   * <p>
   * @param msg a <tt>ModbusMessage</tt>.
   * @throws ModbusIOException data cannot be
   *         written properly to the raw output stream of
   *         this <tt>ModbusTransport</tt>.
   */
  public void writeMessage(ModbusMessage msg) throws ModbusIOException;

  /**
   * Reads a <tt>ModbusRequest</tt> from the
   * input stream of this <tt>ModbusTransport<tt>.
   * <p>
   * @return req the <tt>ModbusRequest</tt> read from the underlying stream.
   * @throws ModbusIOException data cannot be
   *         read properly from the raw input stream of
   *         this <tt>ModbusTransport</tt>.
   */
  public ModbusRequest readRequest() throws ModbusIOException;

  /**
   * Reads a <tt>ModbusResponse</tt> from the
   * input stream of this <tt>ModbusTransport<tt>.
   * <p>
   * @return res the <tt>ModbusResponse</tt> read from the underlying stream.
   * @throws ModbusIOException data cannot be
   *         read properly from the raw input stream of
   *         this <tt>ModbusTransport</tt>.
   */
  public ModbusResponse readResponse() throws ModbusIOException;

}//class ModbusTransport
