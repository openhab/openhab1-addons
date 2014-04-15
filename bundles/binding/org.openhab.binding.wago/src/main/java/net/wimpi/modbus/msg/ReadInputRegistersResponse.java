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

import net.wimpi.modbus.procimg.InputRegister;
import net.wimpi.modbus.procimg.ProcessImageFactory;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.Modbus;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Class implementing a <tt>ReadInputRegistersRequest</tt>.
 * The implementation directly correlates with the class 0
 * function <i>read multiple registers (FC 4)</i>. It
 * encapsulates the corresponding response message.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public final class ReadInputRegistersResponse
    extends ModbusResponse {

  //instance attributes
  private int m_ByteCount;
  //private int[] m_RegisterValues;
  private InputRegister[] m_Registers;

  /**
   * Constructs a new <tt>ReadInputRegistersResponse</tt>
   * instance.
   */
  public ReadInputRegistersResponse() {
    super();
    setFunctionCode(Modbus.READ_INPUT_REGISTERS);
  }//constructor


  /**
   * Constructs a new <tt>ReadInputRegistersResponse</tt>
   * instance.
   *
   * @param registers the InputRegister[] holding response input registers.
   */
  public ReadInputRegistersResponse(InputRegister[] registers) {
    super();
    setFunctionCode(Modbus.READ_INPUT_REGISTERS);
    m_ByteCount = registers.length * 2;
    m_Registers = registers;
    //set correct data length excluding unit id and fc
    setDataLength(m_ByteCount + 1);
  }//constructor


  /**
   * Returns the number of bytes that have been read.
   * <p/>
   *
   * @return the number of bytes that have been read
   *         as <tt>int</tt>.
   */
  public int getByteCount() {
    return m_ByteCount;
  }//getByteCount

  /**
   * Returns the number of words that have been read.
   * The returned value should be twice as much as
   * the byte count of the response.
   * <p/>
   *
   * @return the number of words that have been read
   *         as <tt>int</tt>.
   */
  public int getWordCount() {
    return m_ByteCount / 2;
  }//getWordCount

  /**
   * Sets the number of bytes that have been returned.
   * <p/>
   *
   * @param count the number of bytes as <tt>int</tt>.
   */
  private void setByteCount(int count) {
    m_ByteCount = count;
  }//setByteCount

  /**
   * Returns the <tt>InputRegister</tt> at
   * the given position (relative to the reference
   * used in the request).
   * <p/>
   *
   * @param index the relative index of the <tt>InputRegister</tt>.
   * @return the register as <tt>InputRegister</tt>.
   * @throws IndexOutOfBoundsException if
   *                                   the index is out of bounds.
   */
  public InputRegister getRegister(int index)
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
   * used in the request) interpreted as usigned
   * short.
   * <p/>
   *
   * @param index the relative index of the register
   *              for which the value should be retrieved.
   * @return the value as <tt>int</tt>.
   * @throws IndexOutOfBoundsException if
   *                                   the index is out of bounds.
   */
  public int getRegisterValue(int index)
      throws IndexOutOfBoundsException {

    if (index >= getWordCount()) {
      throw new IndexOutOfBoundsException();
    } else {
      return m_Registers[index].toUnsignedShort();
    }
  }//getRegisterValue

  /**
   * Returns a reference to the array of input
   * registers read.
   *
   * @return a <tt>InputRegister[]</tt> instance.
   */
  public InputRegister[] getRegisters() {
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

    InputRegister[] registers = new InputRegister[getWordCount()];
    ProcessImageFactory pimf = ModbusCoupler.getReference().getProcessImageFactory();
    for (int k = 0; k < getWordCount(); k++) {
      registers[k] = pimf.createInputRegister(din.readByte(), din.readByte());
    }
    m_Registers = registers;
    //update data length
    setDataLength(getByteCount() + 1);
  }//readData

}//class ReadInputRegistersResponse
