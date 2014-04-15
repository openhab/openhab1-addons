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

/**
 * Class implementing a <tt>WriteMultipleRegistersResponse</tt>.
 * The implementation directly correlates with the class 0
 * function <i>read multiple registers (FC 16)</i>. It encapsulates
 * the corresponding response message.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public final class WriteMultipleRegistersResponse
    extends ModbusResponse {

  //instance attributes
  private int m_WordCount;
  private int m_Reference;

  /**
   * Constructs a new <tt>WriteMultipleRegistersResponse</tt>
   * instance.
   */
  public WriteMultipleRegistersResponse() {
    super();
  }//constructor

  /**
   * Constructs a new <tt>WriteMultipleRegistersResponse</tt>
   * instance.
   *
   * @param reference the offset to start reading from.
   * @param wordcount the number of words (registers) to be read.
   */
  public WriteMultipleRegistersResponse(int reference, int wordcount) {
    super();
    m_Reference = reference;
    m_WordCount = wordcount;
    setDataLength(4);
  }//constructor

  /**
   * Sets the reference of the register to start writing to
   * with this <tt>WriteMultipleRegistersResponse</tt>.
   * <p>
   * @param ref the reference of the register
   *        to start writing to as <tt>int</tt>.
   */
  private void setReference(int ref) {
    m_Reference = ref;
  }//setReference

  /**
   * Returns the reference of the register to start
   * writing to with this
   * <tt>WriteMultipleRegistersResponse</tt>.
   * <p>
   * @return the reference of the register
   *        to start writing to as <tt>int</tt>.
   */
  public int getReference() {
    return m_Reference;
  }//getReference

  /**
   * Returns the number of bytes that have been written.
   * <p>
   * @return the number of bytes that have been read
   *         as <tt>int</tt>.
   */
  public int getByteCount() {
    return m_WordCount * 2;
  }//getByteCount

  /**
   * Returns the number of words that have been read.
   * The returned value should be half of
   * the byte count of the response.
   * <p>
   * @return the number of words that have been read
   *         as <tt>int</tt>.
   */
  public int getWordCount() {
    return m_WordCount;
  }//getWordCount

  /**
   * Sets the number of words that have been returned.
   * <p>
   * @param count the number of words as <tt>int</tt>.
   */
  private void setWordCount(int count) {
    m_WordCount = count;
  }//setWordCount

  public void writeData(DataOutput dout)
      throws IOException {
    dout.writeShort(m_Reference);
    dout.writeShort(getWordCount());
  }//writeData

  public void readData(DataInput din)
      throws IOException {

    setReference(din.readUnsignedShort());
    setWordCount(din.readUnsignedShort());
    //NOTE: register values are not echoed

    //update data length
    setDataLength(4);
  }//readData

}//class WriteMultipleRegistersResponse
