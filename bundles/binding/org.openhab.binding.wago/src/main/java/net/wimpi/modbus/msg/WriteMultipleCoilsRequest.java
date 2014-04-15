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
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.procimg.DigitalOut;
import net.wimpi.modbus.procimg.IllegalAddressException;
import net.wimpi.modbus.procimg.ProcessImage;
import net.wimpi.modbus.util.BitVector;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Class implementing a <tt>WriteMultipleCoilsRequest</tt>.
 * The implementation directly correlates with the class 1
 * function <i>write multiple coils (FC 15)</i>. It encapsulates
 * the corresponding request message.
 * <p/>
 * Coils are understood as bits that can be manipulated
 * (i.e. set or unset).
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public final class WriteMultipleCoilsRequest
    extends ModbusRequest {

  //instance attributes
  private int m_Reference;
  private BitVector m_Coils;

  /**
   * Constructs a new <tt>WriteMultipleCoilsRequest</tt>
   * instance.
   */
  public WriteMultipleCoilsRequest() {
    super();
    setFunctionCode(Modbus.WRITE_MULTIPLE_COILS);
    //5 bytes (unit id and function code is excluded)
    setDataLength(5);
  }//constructor

  /**
   * Constructs a new <tt>WriteMultipleCoilsRequest</tt>
   * instance with a given reference and count of coils
   * (i.e. bits) to be written.
   * <p/>
   *
   * @param ref   the index of the first coil to be written.
   * @param count the number of coils to be written.
   */
  public WriteMultipleCoilsRequest(int ref, int count) {
    super();
    setFunctionCode(Modbus.WRITE_MULTIPLE_COILS);
    setReference(ref);
    m_Coils = new BitVector(count);
    setDataLength(m_Coils.byteSize() + 5);
  }//constructor

  /**
   * Constructs a new <tt>WriteMultipleCoilsRequest</tt>
   * instance with given reference and coil status.
   *
   * @param ref the index of the first coil to be written.
   * @param bv the coils to be written.
   */
  public WriteMultipleCoilsRequest(int ref, BitVector bv) {
    super();
    setFunctionCode(Modbus.WRITE_MULTIPLE_COILS);
    setReference(ref);
    m_Coils = bv;
    setDataLength(m_Coils.byteSize() + 5);
  }//constructor

  public ModbusResponse createResponse() {
    WriteMultipleCoilsResponse response = null;
    DigitalOut douts[] = null;

    //1. get process image
    ProcessImage procimg = ModbusCoupler.getReference().getProcessImage();
    //2. get coil range
    try {
      douts = procimg.getDigitalOutRange(m_Reference, m_Coils.size());
      //3. set coils
      for (int i = 0; i < douts.length; i++) {
        douts[i].set(m_Coils.getBit(i));
      }
    } catch (IllegalAddressException iaex) {
      return createExceptionResponse(Modbus.ILLEGAL_ADDRESS_EXCEPTION);
    }
    response = new WriteMultipleCoilsResponse(m_Reference, m_Coils.size());

    //transfer header data
    if (!isHeadless()) {
      response.setTransactionID(this.getTransactionID());
      response.setProtocolID(this.getProtocolID());
    } else {
      response.setHeadless();
    }
    response.setUnitID(this.getUnitID());
    response.setFunctionCode(this.getFunctionCode());

    return response;
  }//createResponse

  /**
   * Sets the reference of the register to start reading
   * from with this <tt>ReadCoilsRequest</tt>.
   * <p/>
   *
   * @param ref the reference of the register
   *            to start reading from.
   */
  public void setReference(int ref) {
    m_Reference = ref;
    //setChanged(true);
  }//setReference

  /**
   * Returns the reference of the register to to start
   * reading from with this <tt>ReadCoilsRequest</tt>.
   * <p/>
   *
   * @return the reference of the register
   *         to start reading from as <tt>int</tt>.
   */
  public int getReference() {
    return m_Reference;
  }//getReference

  /**
   * Returns the number of bits (i.e. input discretes)
   * read with the request.
   * <p/>
   *
   * @return the number of bits that have been read.
   */
  public int getBitCount() {
    if (m_Coils == null) {
      return 0;
    } else {
      return m_Coils.size();
    }
  }//getBitCount

  /**
   * Returns the number of bytes required for packing the
   * coil bits.
   *
   * @return the number of bytes required for packing the coil bits.
   */
  public int getByteCount() {
    return m_Coils.byteSize();
  }//getByteCount

  /**
   * Returns the status of the given coil.
   *
   * @param index the index of the coil to be tested.
   * @return true if set, false otherwise.
   * @throws IndexOutOfBoundsException if the given index is out of bounds.
   */
  public boolean getCoilStatus(int index)
      throws IndexOutOfBoundsException {
    return m_Coils.getBit(index);
  }//getCoilStatus

  /**
   * Sets the coil status of the given coil.
   *
   * @param index the index of the coil to be set/reset.
   * @param b     true if to be set, false for reset.
   * @throws IndexOutOfBoundsException if the given index is out of bounds.
   */
  public void setCoilStatus(int index, boolean b)
      throws IndexOutOfBoundsException {
    m_Coils.setBit(index, b);
  }//setCoilStatus

  /**
   * Returns the <tt>BitVector</tt> instance holding coil
   * status information.
   *
   * @return the coils status as <tt>BitVector</tt> instance.
   */
  public BitVector getCoils() {
    return m_Coils;
  }//getCoils

  /**
   * Sets the <tt>BitVector</tt> instance holding coil
   * status information.
   *
   * @param bv a <tt>BitVector</tt> instance holding coil status info.
   */
  public void setCoils(BitVector bv) {
    m_Coils = bv;
  }//setCoils

  public void writeData(DataOutput dout)
      throws IOException {
    dout.writeShort(m_Reference);
    dout.writeShort(m_Coils.size());
    dout.writeByte(m_Coils.byteSize());
    dout.write(m_Coils.getBytes());
  }//writeData

  public void readData(DataInput din)
      throws IOException {

    m_Reference = din.readUnsignedShort();
    int bitcount = din.readUnsignedShort();
    int count = din.readUnsignedByte();
    byte[] data = new byte[count];
    for (int k = 0; k < count; k++) {
      data[k] = din.readByte();
    }
    //decode bytes into bitvector, sets data and bitcount
    m_Coils = BitVector.createBitVector(data, bitcount);

    //update data length
    setDataLength(count + 5);
  }//readData


}//class WriteMultipleCoilsRequest
