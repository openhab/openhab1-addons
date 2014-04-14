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

import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleRegister;
import net.wimpi.modbus.procimg.ProcessImageFactory;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.Modbus;

/**
 * Class implementing a <tt>ReadMultipleRegistersResponse</tt>.
 * The implementation directly correlates with the class 0
 * function <i>read multiple registers (FC 3)</i>. It encapsulates
 * the corresponding response message.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public final class ReadMultipleRegistersResponse
    extends ModbusResponse {

  //instance attributes
  private int m_ByteCount;
  private Register[] m_Registers;

  /**
   * Constructs a new <tt>ReadMultipleRegistersResponse</tt>
   * instance.
   */
  public ReadMultipleRegistersResponse() {
    super();
    setFunctionCode(Modbus.READ_MULTIPLE_REGISTERS);
  }//constructor

  /**
   * Constructs a new <tt>ReadInputRegistersResponse</tt>
   * instance.
   *
   * @param registers the Register[] holding response registers.
   */
  public ReadMultipleRegistersResponse(Register[] registers) {
    super();
    m_Registers = registers;
    m_ByteCount = registers.length * 2;
    setFunctionCode(Modbus.READ_MULTIPLE_REGISTERS);
    //set correct data length excluding unit id and fc
    setDataLength(m_ByteCount + 1);
  }//constructor


  /**
   * Returns the number of bytes that have been read.
   * <p>
   * @return the number of bytes that have been read
   *         as <tt>int</tt>.
   */
  public int getByteCount() {
    return m_ByteCount;
  }//getByteCount

  /**
   * Returns the number of words that have been read.
   * The returned value should be half of the
   * the byte count of this
   * <tt>ReadMultipleRegistersResponse</tt>.
   * <p>
   * @return the number of words that have been read
   *         as <tt>int</tt>.
   */
  public int getWordCount() {
    return m_ByteCount / 2;
  }//getWordCount

  /**
   * Sets the number of bytes that have been returned.
   * <p>
   * @param count the number of bytes as <tt>int</tt>.
   */
  private void setByteCount(int count) {
    m_ByteCount = count;
  }//setByteCount

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
   * Returns a reference to the array of registers
   * read.
   *
   * @return a <tt>Register[]</tt> instance.
   */
  public Register[] getRegisters() {
    return m_Registers;
  }//getRegisters

  public void writeData(DataOutput dout)
      throws IOException {
    dout.writeByte(m_ByteCount);
    for (int k = 0; k < getWordCount(); k++) {
      dout.write(m_Registers[k].toBytes());
    }
  }//writeData

  public void readData(DataInput din)
      throws IOException {
    setByteCount(din.readUnsignedByte());

    m_Registers = new Register[getWordCount()];
    ProcessImageFactory pimf = ModbusCoupler.getReference().getProcessImageFactory();

    for (int k = 0; k < getWordCount(); k++) {
      m_Registers[k] = pimf.createRegister(din.readByte(), din.readByte());
    }

    //update data length
    setDataLength(getByteCount() + 1);
  }//readData


}//class ReadMultipleRegistersResponse
