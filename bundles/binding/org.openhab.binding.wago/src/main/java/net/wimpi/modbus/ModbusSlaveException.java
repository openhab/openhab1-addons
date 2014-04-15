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
 * Class that implements a <tt>ModbusSlaveException</tt>.
 * Instances of this exception are thrown when
 * the slave returns a Modbus exception.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class ModbusSlaveException
    extends ModbusException {

  //instance attributes
  private int m_Type = -1;

  /**
   * Constructs a new <tt>ModbusSlaveException</tt>
   * instance with the given type.<br>
   * Types are defined according to the protocol
   * specification in <tt>net.wimpi.modbus.Modbus</tt>.
   * <p>
   * @param TYPE the type of exception that occured.
   *
   * @see net.wimpi.modbus.Modbus
   */
  public ModbusSlaveException(int TYPE) {
    super();
    m_Type = TYPE;
  }//constructor

  /**
   * Returns the type of this <tt>ModbusSlaveException</tt>.
   * <br>
   * Types are defined according to the protocol
   * specification in <tt>net.wimpi.modbus.Modbus</tt>.
   * <p>
   * @return the type of this <tt>ModbusSlaveException</tt>.
   *
   * @see net.wimpi.modbus.Modbus
   */
  public int getType() {
    return m_Type;
  }//getType

  /**
   * Tests if this <tt>ModbusSlaveException</tt>
   * is of a given type.
   * <br>
   * Types are defined according to the protocol
   * specification in <tt>net.wimpi.modbus.Modbus</tt>.
   * <p>
   * @param TYPE the type to test this
   *        <tt>ModbusSlaveException</tt> type against.
   *
   * @return true if this <tt>ModbusSlaveException</tt>
   *         is of the given type, false otherwise.
   *
   * @see net.wimpi.modbus.Modbus
   */
  public boolean isType(int TYPE) {
    return (TYPE == m_Type);
  }//isType

  public String getMessage() {
    return "Error Code = " + m_Type;
  }//getMessage

}//ModbusSlaveException
