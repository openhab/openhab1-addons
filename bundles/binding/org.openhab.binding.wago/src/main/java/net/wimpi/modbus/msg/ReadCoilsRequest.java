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

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.procimg.DigitalOut;
import net.wimpi.modbus.procimg.IllegalAddressException;
import net.wimpi.modbus.procimg.ProcessImage;

/**
 * Class implementing a <tt>ReadCoilsRequest</tt>.
 * The implementation directly correlates with the class 1
 * function <i>read coils (FC 1)</i>. It encapsulates
 * the corresponding request message.
 * <p>
 * Coils are understood as bits that can be manipulated
 * (i.e. set or unset).
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public final class ReadCoilsRequest
    extends ModbusRequest {

  //instance attributes
  private int m_Reference;
  private int m_BitCount;

  /**
   * Constructs a new <tt>ReadCoilsRequest</tt>
   * instance.
   */
  public ReadCoilsRequest() {
    super();
    setFunctionCode(Modbus.READ_COILS);
    //4 bytes (unit id and function code is excluded)
    setDataLength(4);
  }//constructor

  /**
   * Constructs a new <tt>ReadCoilsRequest</tt>
   * instance with a given reference and count of coils
   * (i.e. bits) to be read.
   * <p>
   * @param ref the reference number of the register
   *        to read from.
   * @param count the number of bits to be read.
   */
  public ReadCoilsRequest(int ref, int count) {
    super();
    setFunctionCode(Modbus.READ_COILS);
    //4 bytes (unit id and function code is excluded)
    setDataLength(4);
    setReference(ref);
    setBitCount(count);
  }//constructor

  public ModbusResponse createResponse() {
    ReadCoilsResponse response = null;
    DigitalOut[] douts = null;

    //1. get process image
    ProcessImage procimg = ModbusCoupler.getReference().getProcessImage();
    //2. get coil range
    try {
      douts = procimg.getDigitalOutRange(this.getReference(), this.getBitCount());
    } catch (IllegalAddressException iaex) {
      return createExceptionResponse(Modbus.ILLEGAL_ADDRESS_EXCEPTION);
    }
    response = new ReadCoilsResponse(douts.length);

    //transfer header data
    if (!isHeadless()) {
      response.setTransactionID(this.getTransactionID());
      response.setProtocolID(this.getProtocolID());
    } else {
      response.setHeadless();
    }
    response.setUnitID(this.getUnitID());
    response.setFunctionCode(this.getFunctionCode());

    for (int i = 0; i < douts.length; i++) {
      response.setCoilStatus(i, douts[i].isSet());
    }
    return response;
  }//createResponse

  /**
   * Sets the reference of the register to start reading
   * from with this <tt>ReadCoilsRequest</tt>.
   * <p>
   * @param ref the reference of the register
   *        to start reading from.
   */
  public void setReference(int ref) {
    m_Reference = ref;
    //setChanged(true);
  }//setReference

  /**
   * Returns the reference of the register to to start
   * reading from with this <tt>ReadCoilsRequest</tt>.
   * <p>
   * @return the reference of the register
   *        to start reading from as <tt>int</tt>.
   */
  public int getReference() {
    return m_Reference;
  }//getReference

  /**
   * Sets the number of bits (i.e. coils) to be read with
   * this <tt>ReadCoilsRequest</tt>.
   * <p>
   * @param count the number of bits to be read.
   */
  public void setBitCount(int count) {
    if(count > Modbus.MAX_BITS) {
      throw new IllegalArgumentException("Maximum bitcount exceeded.");
    } else {
      m_BitCount = count;
    }
  }//setBitCount

  /**
   * Returns the number of bits (i.e. coils) to be
   * read with this <tt>ReadCoilsRequest</tt>.
   * <p>
   * @return the number of bits to be read.
   */
  public int getBitCount() {
    return m_BitCount;
  }//getBitCount

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

}//class ReadCoilsRequest
