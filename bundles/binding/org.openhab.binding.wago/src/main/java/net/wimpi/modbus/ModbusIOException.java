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

package net.wimpi.modbus;

/**
 * Class that implements a <tt>ModbusIOException</tt>.
 * Instances of this exception are thrown when
 * errors in the I/O occur.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class ModbusIOException
    extends ModbusException {

  private boolean m_EOF = false;

  /**
   * Constructs a new <tt>ModbusIOException</tt>
   * instance.
   */
  public ModbusIOException() {
  }//constructor

  /**
   * Constructs a new <tt>ModbusIOException</tt>
   * instance with the given message.
   * <p>
   * @param message the message describing this
   *        <tt>ModbusIOException</tt>.
   */
  public ModbusIOException(String message) {
    super(message);
  }//constructor(String)

  /**
   * Constructs a new <tt>ModbusIOException</tt>
   * instance.
   *
   * @param b true if caused by end of stream, false otherwise.
   */
  public ModbusIOException(boolean b) {
    m_EOF = b;
  }//constructor

  /**
   * Constructs a new <tt>ModbusIOException</tt>
   * instance with the given message.
   * <p>
   * @param message the message describing this
   *        <tt>ModbusIOException</tt>.
   * @param b true if caused by end of stream, false otherwise.
   */
  public ModbusIOException(String message, boolean b) {
    super(message);
    m_EOF = b;
  }//constructor(String)


  /**
   * Tests if this <tt>ModbusIOException</tt>
   * is caused by an end of the stream.
   * <p>
   * @return true if stream ended, false otherwise.
   */
  public boolean isEOF() {
    return m_EOF;
  }//isEOF

  /**
   * Sets the flag that determines whether this
   * <tt>ModbusIOException</tt> was caused by
   * an end of the stream.
   * <p>
   * @param b true if stream ended, false otherwise.
   */
  public void setEOF(boolean b) {
    m_EOF = b;
  }//setEOF

}//ModbusIOException
