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
import net.wimpi.modbus.procimg.IllegalAddressException;
import net.wimpi.modbus.procimg.InputRegister;
import net.wimpi.modbus.procimg.ProcessImage;

/**
 * Class implementing a <tt>ReadInputRegistersRequest</tt>.
 * The implementation directly correlates with the class 0
 * function <i>read multiple registers (FC 4)</i>. It
 * encapsulates the corresponding request message.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public final class ReadInputRegistersRequest
    extends ModbusRequest {

  //instance attributes
  private int m_Reference;
  private int m_WordCount;

  /**
   * Constructs a new <tt>ReadInputRegistersRequest</tt>
   * instance.
   */
  public ReadInputRegistersRequest() {
    super();
    setFunctionCode(Modbus.READ_INPUT_REGISTERS);
    //4 bytes (unit id and function code is excluded)
    setDataLength(4);
  }//constructor

  /**
   * Constructs a new <tt>ReadInputRegistersRequest</tt>
   * instance with a given reference and count of words
   * to be read.
   * <p>
   * @param ref the reference number of the register
   *        to read from.
   * @param count the number of words to be read.
   */
  public ReadInputRegistersRequest(int ref, int count) {
    super();
    setFunctionCode(Modbus.READ_INPUT_REGISTERS);
    //4 bytes (unit id and function code is excluded)
    setDataLength(4);
    setReference(ref);
    setWordCount(count);
  }//constructor
  
  public ModbusResponse createResponse() {
    ReadInputRegistersResponse response = null;
    InputRegister[] inpregs = null;

    //1. get process image
    ProcessImage procimg = ModbusCoupler.getReference().getProcessImage();
    //2. get input registers range
    try {
      inpregs = procimg.getInputRegisterRange(this.getReference(), this.getWordCount());
    } catch (IllegalAddressException iaex) {
      return createExceptionResponse(Modbus.ILLEGAL_ADDRESS_EXCEPTION);
    }
    response = new ReadInputRegistersResponse(inpregs);
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
   * from with this <tt>ReadInputRegistersRequest</tt>.
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
   * reading from with this
   * <tt>ReadInputRegistersRequest</tt>.
   * <p>
   * @return the reference of the register
   *        to start reading from as <tt>int</tt>.
   */
  public int getReference() {
    return m_Reference;
  }//getReference

  /**
   * Sets the number of words to be read with this
   * <tt>ReadInputRegistersRequest</tt>.
   * <p>
   * @param count the number of words to be read.
   */
  public void setWordCount(int count) {
    m_WordCount = count;
    //setChanged(true);
  }//setWordCount

  /**
   * Returns the number of words to be read with this
   * <tt>ReadInputRegistersRequest</tt>.
   * <p>
   * @return the number of words to be read as
   *        <tt>int</tt>.
   */
  public int getWordCount() {
    return m_WordCount;
  }//getWordCount

  public void writeData(DataOutput dout)
      throws IOException {
    dout.writeShort(m_Reference);
    dout.writeShort(m_WordCount);
  }//writeData

  public void readData(DataInput din)
      throws IOException {
    m_Reference = din.readUnsignedShort();
    m_WordCount = din.readUnsignedShort();
  }//readData

}//class ReadInputRegistersRequest
