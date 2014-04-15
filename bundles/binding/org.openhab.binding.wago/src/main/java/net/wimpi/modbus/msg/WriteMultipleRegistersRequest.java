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
import net.wimpi.modbus.io.NonWordDataHandler;
import net.wimpi.modbus.procimg.*;

/**
 * Class implementing a <tt>ReadMultipleRegistersRequest</tt>.
 * The implementation directly correlates with the class 0
 * function <i>write multiple registers (FC 16)</i>. It
 * encapsulates the corresponding request message.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public final class WriteMultipleRegistersRequest
    extends ModbusRequest {

  //instance attributes
  private int m_Reference;
  private Register[] m_Registers;
  private NonWordDataHandler m_NonWordDataHandler = null;

  /**
   * Constructs a new <tt>WriteMultipleRegistersRequest</tt>
   * instance.
   */
  public WriteMultipleRegistersRequest() {
    super();
    setFunctionCode(Modbus.WRITE_MULTIPLE_REGISTERS);
  }//constructor

  /**
   * Constructs a new <tt>WriteMultipleRegistersRequest</tt>
   * instance with a given reference and values to be written.
   * <p>
   * @param ref the reference number of the register
   *        to read from.
   * @param registers the registers to be written.
   */
  public WriteMultipleRegistersRequest(int ref, Register[] registers) {
    super();
    setFunctionCode(Modbus.WRITE_MULTIPLE_REGISTERS);
    setReference(ref);
    setRegisters(registers);
  }//constructor

  public ModbusResponse createResponse() {
    WriteMultipleRegistersResponse response = null;

    if (m_NonWordDataHandler == null) {
      Register[] regs = null;
      //1. get process image
      ProcessImage procimg = ModbusCoupler.getReference().getProcessImage();
      //2. get registers
      try {
        //TODO: realize a setRegisterRange()?
        regs = procimg.getRegisterRange(this.getReference(), this.getWordCount());
        //3. set Register values
        for (int i = 0; i < regs.length; i++) {
          regs[i].setValue(this.getRegister(i).toBytes());
        }
      } catch (IllegalAddressException iaex) {
        return createExceptionResponse(Modbus.ILLEGAL_ADDRESS_EXCEPTION);
      }
      response = new WriteMultipleRegistersResponse(this.getReference(), regs.length);
    } else {
      int result = m_NonWordDataHandler.commitUpdate();
      if (result > 0) {
        return createExceptionResponse(result);
      }
      response = new WriteMultipleRegistersResponse(
          this.getReference(),
          m_NonWordDataHandler.getWordCount()
      );
    }
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
   * Sets the reference of the register to writing to
   * with this <tt>WriteMultipleRegistersRequest</tt>.
   * <p>
   * @param ref the reference of the register
   *        to start writing to as <tt>int</tt>.
   */
  public void setReference(int ref) {
    m_Reference = ref;
  }//setReference

  /**
   * Returns the reference of the register to start
   * writing to with this
   * <tt>WriteMultipleRegistersRequest</tt>.
   * <p>
   * @return the reference of the register
   *        to start writing to as <tt>int</tt>.
   */
  public int getReference() {
    return m_Reference;
  }//getReference

  /**
   * Sets the registers to be written with this
   * <tt>WriteMultipleRegistersRequest</tt>.
   * <p>
   * @param registers the registers to be written
   *        as <tt>Register[]</tt>.
   */
  public void setRegisters(Register[] registers) {
    m_Registers = registers;
    setDataLength(5 + getByteCount()); //update message length in header
  }//setRegisters


  /**
   * Returns the registers to be written with this
   * <tt>WriteMultipleRegistersRequest</tt>.
   * <p>
   * @return the registers to be written as <tt>Register[]</tt>.
   */
  public Register[] getRegisters() {
    return m_Registers;
  }//getRegisters

  /**
   * Returns the <tt>Register</tt> at
   * the given position (relative to the reference
   * used in the request).
   * <p>
   * @param index the relative index of the <tt>Register</tt>.
   *
   * @return the register as <tt>Register</tt>.
   *
   * @throws IndexOutOfBoundsException if
   *         the index is out of bounds.
   */
  public Register getRegister(int index)
      throws IndexOutOfBoundsException {

    if (index >= getWordCount()) {
      throw new IndexOutOfBoundsException();
    } else {
      return m_Registers[index];
    }
  }//getRegister


  /**
   * Returns the value of the register at
   * the given position (relative to the reference
   * used in the request) interpreted as unsigned short.
   * <p>
   * @param index the relative index of the register
   *        for which the value should be retrieved.
   *
   * @return the value as <tt>int</tt>.
   *
   * @throws IndexOutOfBoundsException if
   *         the index is out of bounds.
   */
  public int getRegisterValue(int index)
      throws IndexOutOfBoundsException {
    return m_Registers[index].toUnsignedShort();
  }//getRegisterValue

  /**
   * Returns the number of bytes representing the
   * values to be written.
   * <p>
   * @return the number of bytes to be written
   *         as <tt>int</tt>.
   */
  public int getByteCount() {
    return getWordCount() * 2;
  }//getByteCount

  /**
   * Returns the number of words to be written.
   * <p>
   * @return the number of words to be written
   *         as <tt>int</tt>.
   */
  public int getWordCount() {
    return m_Registers.length;
  }//getWordCount

  /**
   * Sets a non word data handler.
   *
   * @param dhandler a  <tt>NonWordDataHandler</tt> instance.
   */
  public void setNonWordDataHandler(NonWordDataHandler dhandler) {
    m_NonWordDataHandler = dhandler;
    setDataLength(5 + (m_NonWordDataHandler.getWordCount()*2)); 
  }//setNonWordDataHandler

  /**
   * Returns the actual non word data handler.
   *
   * @return the actual <tt>NonWordDataHandler</tt>.
   */
  public NonWordDataHandler getNonWordDataHandler() {
    return m_NonWordDataHandler;
  }//getNonWordDataHandler

  public void writeData(DataOutput dout)
      throws IOException {
    //1. the reference
    dout.writeShort(m_Reference);
    //2. the word count
    dout.writeShort(getWordCount());
    //3. the byte count as byte
    dout.writeByte(getByteCount());
    //4. write values
    if (m_NonWordDataHandler == null) {
      for (int n = 0; n < m_Registers.length; n++) {
        dout.write(m_Registers[n].toBytes());
      }
    } else {
      m_NonWordDataHandler.prepareData(getReference(), getWordCount());
      dout.write(m_NonWordDataHandler.getData());
    }
  }//writeData

  public void readData(DataInput din)
      throws IOException {

    m_Reference = din.readShort();
    //read lengths
    int wc = din.readUnsignedShort();
    int bc = din.readUnsignedByte();

    //read values
    if (m_NonWordDataHandler == null) {
      m_Registers = new Register[wc];
      ProcessImageFactory pimf = ModbusCoupler.getReference().getProcessImageFactory();
      for (int i = 0; i < wc; i++) {
        m_Registers[i] = pimf.createRegister(din.readByte(), din.readByte());
      }
    } else {
      m_NonWordDataHandler.readData(din, m_Reference, wc);
    }
  }//readData

}//class WriteMultipleRegistersRequest
