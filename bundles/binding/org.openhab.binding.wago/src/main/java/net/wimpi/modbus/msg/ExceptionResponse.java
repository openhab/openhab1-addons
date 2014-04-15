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

/**
 * Provides the a<tt>ModbusResponse</tt>
 * implementation that represents a Modbus exception.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class ExceptionResponse
    extends ModbusResponse {

  //instance attributes
  private int m_ExceptionCode = -1;

  /**
   * Constructs a new <tt>ExceptionResponse</tt> instance.
   */
  public ExceptionResponse() {
    //exception code, unitid and function code not counted.
    setDataLength(1);
  }//constructor

  /**
   * Constructs a new <tt>ExceptionResponse</tt> instance with
   * a given function code. Adds the exception offset automatically.
   *
   * @param fc the function code as <tt>int</tt>.
   */
  public ExceptionResponse(int fc) {
    //unitid and function code not counted.
    setDataLength(1);
    setFunctionCode(fc + Modbus.EXCEPTION_OFFSET);
  }//constructor

  /**
   * Constructs a new <tt>ExceptionResponse</tt> instance with
   * a given function code and an exception code. The function
   * code will be automatically increased with the exception offset.
   *
   *
   * @param fc the function code as <tt>int</tt>.
   * @param exc the exception code as <tt>int</tt>.
   */
  public ExceptionResponse(int fc, int exc) {
    //exception code, unitid and function code not counted.
    setDataLength(1);
    setFunctionCode(fc + Modbus.EXCEPTION_OFFSET);
    m_ExceptionCode = exc;
  }//constructor

  /**
   * Returns the Modbus exception code of this
   * <tt>ExceptionResponse</tt>.
   * <p>
   * @return the exception code as <tt>int</tt>.
   */
  public int getExceptionCode() {
    return m_ExceptionCode;
  }//getExceptionCode

  public void writeData(DataOutput dout)
      throws IOException {
    dout.writeByte(getExceptionCode());
  }//writeData

  public void readData(DataInput din)
      throws IOException {
    m_ExceptionCode = din.readUnsignedByte();
  }//readData

}//ExceptionResponse
