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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.wimpi.modbus.util.BitVector;
import net.wimpi.modbus.Modbus;

/**
 * Class implementing a <tt>ReadCoilsResponse</tt>.
 * The implementation directly correlates with the class 1
 * function <i>read coils (FC 1)</i>. It encapsulates
 * the corresponding response message.
 * <p>
 * Coils are understood as bits that can be manipulated
 * (i.e. set or unset).
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public final class ReadCoilsResponse
    extends ModbusResponse {

  //instance attributes
  private BitVector m_Coils;

  /**
   * Constructs a new <tt>ReadCoilsResponse</tt>
   * instance.
   */
  public ReadCoilsResponse() {
    super();
    setFunctionCode(Modbus.READ_COILS);
  }//constructor(int)


  /**
   * Constructs a new <tt>ReadCoilsResponse</tt>
   * instance with a given count of coils (i.e. bits).
   * <b>
   * @param count the number of bits to be read.
   */
  public ReadCoilsResponse(int count) {
    super();
    m_Coils = new BitVector(count);
    setFunctionCode(Modbus.READ_COILS);
    setDataLength(m_Coils.byteSize() + 1);
  }//constructor(int)

  /**
   * Returns the number of bits (i.e. coils)
   * read with the request.
   * <p>
   * @return the number of bits that have been read.
   */
  public int getBitCount() {
    if(m_Coils == null) {
      return 0;
    } else{
      return m_Coils.size();
    }
  }//getBitCount


  /**
   * Returns the <tt>BitVector</tt> that stores
   * the collection of bits that have been read.
   * <p>
   * @return the <tt>BitVector</tt> holding the
   *         bits that have been read.
   */
  public BitVector getCoils() {
    return m_Coils;
  }//getCoils

  /**
   * Convenience method that returns the state
   * of the bit at the given index.
   * <p>
   * @param index the index of the coil for which
   *        the status should be returned.
   *
   * @return true if set, false otherwise.
   *
   * @throws IndexOutOfBoundsException if the
   *         index is out of bounds
   */
  public boolean getCoilStatus(int index)
      throws IndexOutOfBoundsException {

    return m_Coils.getBit(index);
  }//getCoilStatus

  /**
   * Sets the status of the given coil.
   *
   * @param index the index of the coil to be set.
   * @param b true if to be set, false for reset.
   */
  public void setCoilStatus(int index, boolean b) {
    m_Coils.setBit(index, b);
  }//setCoilStatus

  public void writeData(DataOutput dout)
      throws IOException {
    dout.writeByte(m_Coils.byteSize());
    dout.write(m_Coils.getBytes(), 0, m_Coils.byteSize());
  }//writeData

  public void readData(DataInput din)
      throws IOException {
    int count = din.readUnsignedByte();
    byte[] data = new byte[count];
    for (int k = 0; k < count; k++) {
      data[k] = din.readByte();
    }
    //decode bytes into bitvector
    m_Coils = BitVector.createBitVector(data);
    //update data length
    setDataLength(count + 1);
  }//readData

}//class ReadCoilsResponse
