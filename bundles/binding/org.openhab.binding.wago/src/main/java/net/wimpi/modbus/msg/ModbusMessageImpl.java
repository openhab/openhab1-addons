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
import net.wimpi.modbus.util.ModbusUtil;

/**
 * Abstract class implementing a <tt>ModbusMessage</tt>.
 * This class provides specialised implementations with
 * the functionality they have in common.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public abstract class ModbusMessageImpl
    implements ModbusMessage {

  //instance attributes
  private int m_TransactionID = Modbus.DEFAULT_TRANSACTION_ID;
  private int m_ProtocolID = Modbus.DEFAULT_PROTOCOL_ID;
  private int m_DataLength;
  private int m_UnitID = Modbus.DEFAULT_UNIT_ID;
  private int m_FunctionCode;
  private boolean m_Headless = false;       //flag for headerless (serial) transport

  /*** Header ******************************************/

  /**
   * Tests if this message instance is headless.
   *
   * @return true if headless, false otherwise.
   */
  public boolean isHeadless() {
    return m_Headless;
  }//isHeadless

  public void setHeadless() {
    m_Headless = true;
  }//setHeadless

  /**
   * Sets the headless flag of this message.
   *
   * @param b true if headless, false otherwise.
   */
  protected void setHeadless(boolean b) {
    m_Headless = b;
  }//setHeadless

  public int getTransactionID() {
    return m_TransactionID;
  }//getTransactionID

  /**
   * Sets the transaction identifier of this
   * <tt>ModbusMessage</tt>.<p>
   * The identifier should be a 2-byte (short) non negative
   * integer value valid in the range of 0-65535.<br>
   * <p>
   * @param tid the transaction identifier as <tt>int</tt>.
   */
  public void setTransactionID(int tid) {
    m_TransactionID = tid;
    //setChanged(true);
  }//setTransactionID

  public int getProtocolID() {
    return m_ProtocolID;
  }//getProtocolID

  /**
   * Sets the protocol identifier of this
   * <tt>ModbusMessage</tt>.<p>
   * The identifier should be a 2-byte (short) non negative
   * integer value valid in the range of 0-65535.<br>
   * <p>
   * @param pid the protocol identifier as <tt>int</tt>.
   */
  public void setProtocolID(int pid) {
    m_ProtocolID = pid;
    //setChanged(true);
  }//setProtocolID

  public int getDataLength() {
    return m_DataLength;
  }//getDataLength

  /**
   * Sets the length of the data appended
   * after the protocol header.<p>
   * Note that this library, a bit in contrast to the
   * specification, counts the unit identifier and the
   * function code to the header, because it is part
   * of each and every message. Thus this message will
   * append two (2) to the passed in integer value.
   * <p>
   * @param length the data length as <tt>int</tt>.
   */
  public void setDataLength(int length) {
    //should be below 255, check!
    m_DataLength = length + 2;
  }//setData

  public int getUnitID() {
    return m_UnitID;
  }//getUnitID

  /**
   * Sets the unit identifier of  this
   * <tt>ModbusMessage</tt>.<br>
   * The identifier should be a 1-byte non negative
   * integer value valid in the range of 0-255.
   *
   * @param num the unit identifier number to be set.
   */
  public void setUnitID(int num) {
    m_UnitID = num;
    //setChanged(true);
  }//setUnitID

  public int getFunctionCode() {
    return m_FunctionCode;
  }//getFunctionCode

  /**
   * Sets the function code of this <tt>ModbusMessage</tt>.<br>
   * The function code should be a 1-byte non negative
   * integer value valid in the range of 0-127.<br>
   * Function codes are ordered in conformance
   * classes their values are specified in
   * <tt>net.wimpi.modbus.Modbus</tt>.
   *
   * @param code the code of the function to be set.
   * @see net.wimpi.modbus.Modbus
   */
  protected void setFunctionCode(int code) {
    m_FunctionCode = code;
    //setChanged(true);
  }//setFunctionCode


  /*** Data ********************************************/

  /*** Transportable ***********************************/

  /**
   * Writes this message to the given <tt>DataOutput</tt>.
   *
   * @param dout a <tt>DataOutput</tt> instance.
   * @throws IOException if an I/O related error occurs.
   */
  public void writeTo(DataOutput dout)
      throws IOException {

    if (!isHeadless()) {
      dout.writeShort(getTransactionID());
      dout.writeShort(getProtocolID());
      dout.writeShort(getDataLength());
    }
    dout.writeByte(getUnitID());
    dout.writeByte(getFunctionCode());
    writeData(dout);
  }//writeTo

  /**
   * Writes the subclass specific data to the given DataOutput.
   *
   * @param dout the DataOutput to be written to.
   * @throws IOException if an I/O related error occurs.
   */
  public abstract void writeData(DataOutput dout)
      throws IOException;

  public void readFrom(DataInput din)
      throws IOException {
    if (!isHeadless()) {
      setTransactionID(din.readUnsignedShort());
      setProtocolID(din.readUnsignedShort());
      m_DataLength = din.readUnsignedShort();
    }
    setUnitID(din.readUnsignedByte());
    setFunctionCode(din.readUnsignedByte());
    readData(din);
  }//readFrom

  /**
   * Reads the subclass specific data from the given DataInput instance.
   *
   * @param din the DataInput to read from.
   * @throws IOException if an I/O related error occurs.
   */
  public abstract void readData(DataInput din)
      throws IOException;

  public int getOutputLength() {
    int l = 2 + getDataLength();
    if (!isHeadless()) {
      l = l + 6;
    }
    return l;
  }//getOutputLength

  /*** END Transportable *******************************/

  /**
   * Returns the this message as hexadecimal string.
   *
   * @return the message as hex encoded string.
   */
  public String getHexMessage() {
    return ModbusUtil.toHex(this);
  }//getHexMessage

}//class ModbusMessageImpl
