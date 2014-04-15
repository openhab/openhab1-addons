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

import net.wimpi.modbus.Modbus;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Class implementing a <tt>WriteMultipleCoilsResponse</tt>.
 * The implementation directly correlates with the class 1
 * function <i>read coils (FC 15)</i>. It encapsulates
 * the corresponding response message.
 * <p>
 * Coils are understood as bits that can be manipulated
 * (i.e. set or unset).
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public final class WriteMultipleCoilsResponse
    extends ModbusResponse {

  //instance attributes
  private int m_Reference;
  private int m_BitCount;

  /**
   * Constructs a new <tt>WriteMultipleCoilsResponse</tt>
   * instance.
   */
  public WriteMultipleCoilsResponse() {
    super();
    setFunctionCode(Modbus.WRITE_MULTIPLE_COILS);
    setDataLength(4);
  }//constructor(int)

  /**
   * Constructs a new <tt>WriteMultipleCoilsResponse</tt>
   * instance with a given count of coils (i.e. bits).
   * <b>
   * @param ref the offset to begin writing from.
   * @param count the number of bits to be read.
   */
  public WriteMultipleCoilsResponse(int ref,int count) {
    super();
    setFunctionCode(Modbus.WRITE_MULTIPLE_COILS);
    setDataLength(4);
    m_Reference = ref;
    m_BitCount = count;
  }//constructor(int)

  /**
   * Returns the reference of the register to to start
   * reading from with this <tt>WriteMultipleCoilsRequest</tt>.
   * <p>
   * @return the reference of the register
   *        to start reading from as <tt>int</tt>.
   */
  public int getReference() {
    return m_Reference;
  }//getReference

  /**
   * Returns the number of bits (i.e. coils)
   * read with the request.
   * <p>
   * @return the number of bits that have been read.
   */
  public int getBitCount() {
    return m_BitCount;
  }//getBitCount

  /**
   * Sets the number of bits (i.e. coils)
   * that will be in a response.
   *
   * @param count the number of bits in the response.
   */
  public void setBitCount(int count) {
    m_BitCount = count;
  }//setBitCount

  public void writeData(DataOutput dout)
      throws IOException {

    dout.writeShort(m_Reference);
    dout.writeShort(m_BitCount);
  }//writeData

  public void readData(DataInput din)
      throws IOException {

    m_Reference = din.readUnsignedShort();
    m_BitCount = din.readUnsignedShort();
  }//readData

}//class ReadCoilsResponse
