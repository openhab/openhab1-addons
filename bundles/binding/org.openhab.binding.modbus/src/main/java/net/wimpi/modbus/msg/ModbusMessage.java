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

package net.wimpi.modbus.msg;

import net.wimpi.modbus.io.Transportable;

/**
 * Interface defining a Modbus Message.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public interface ModbusMessage
    extends Transportable {

  /**
   * Sets the flag that marks this <tt>ModbusMessage</tt> as headless
   * (for serial transport).
   */
  public void setHeadless();

  /**
   * Returns the transaction identifier of this
   * <tt>ModbusMessage</tt> as <tt>int</tt>.<br>
   * The identifier is a 2-byte (short) non negative
   * integer value valid in the range of 0-65535.
   * <p>
   * @return the transaction identifier as <tt>int</tt>.
   */
  public int getTransactionID();

  /**
   * Returns the protocol identifier of this
   * <tt>ModbusMessage</tt> as <tt>int</tt>.<br>
   * The identifier is a 2-byte (short) non negative
   * integer value valid in the range of 0-65535.
   * <p>
   * @return the protocol identifier as <tt>int</tt>.
   */
  public int getProtocolID();

  /**
   * Returns the length of the data appended
   * after the protocol header.
   * <p>
   * @return the data length as <tt>int</tt>.
   */
  public int getDataLength();

  /**
   * Returns the unit identifier of  this
   * <tt>ModbusMessage</tt> as <tt>int</tt>.<br>
   * The identifier is a 1-byte non negative
   * integer value valid in the range of 0-255.
   * <p>
   * @return the unit identifier as <tt>int</tt>.
   */
  public int getUnitID();

  /**
   * Returns the function code of this
   * <tt>ModbusMessage</tt> as <tt>int</tt>.<br>
   * The function code is a 1-byte non negative
   * integer value valid in the range of 0-127.<br>
   * Function codes are ordered in conformance
   * classes their values are specified in
   * <tt>net.wimpi.modbus.Modbus</tt>.
   * <p>
   * @return the function code as <tt>int</tt>.
   *
   * @see net.wimpi.modbus.Modbus
   */
  public int getFunctionCode();

  /**
   * Returns the <i>raw</i> message as <tt>String</tt>
   * containing a hexadecimal series of bytes.
   * <br>
   * This method is specially for debugging purposes,
   * allowing to log the communication in a manner used
   * in the specification document.
   * <p>
   * @return the <i>raw</i> message as <tt>String</tt>
   *         containing a hexadecimal series of bytes.
   *
   */
  public String getHexMessage();

}//interface ModbusMessage
