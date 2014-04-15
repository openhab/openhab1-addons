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

package net.wimpi.modbus.io;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Class implementing a byte array output stream with
 * a DataInput interface.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class BytesOutputStream
    extends FastByteArrayOutputStream
    implements DataOutput {

  private DataOutputStream m_Dout;

 /**
  * Constructs a new <tt>BytesOutputStream</tt> instance with
   * a new output buffer of the given size.
  *
  * @param size the size of the output buffer as <tt>int</tt>.
  */
  public BytesOutputStream(int size) {
    super(size);
    m_Dout = new DataOutputStream(this);
  }//BytesOutputStream

  /**
   * Constructs a new <tt>BytesOutputStream</tt> instance with
   * a given output buffer.
   *
   * @param buffer the output buffer as <tt>byte[]</tt>.
   */
  public BytesOutputStream(byte[] buffer) {
    buf = buffer;
    count = 0;
    m_Dout = new DataOutputStream(this);
  }//BytesOutputStream

  /**
   * Returns the reference to the output buffer.
   *
   * @return the reference to the <tt>byte[]</tt> output buffer.
   */
  public byte[] getBuffer() {
    return buf;
  }//getBuffer

  public void reset() {
    count = 0;
  }//reset

  public void writeBoolean(boolean v)
      throws IOException {
    m_Dout.writeBoolean(v);
  }//writeBoolean

  public void writeByte(int v)
      throws IOException {
    m_Dout.writeByte(v);
  }//writeByte

  public void writeShort(int v)
      throws IOException {
    m_Dout.writeShort(v);
  }//writeShort

  public void writeChar(int v)
      throws IOException {
    m_Dout.writeChar(v);
  }//writeChar

  public void writeInt(int v)
      throws IOException {
    m_Dout.writeInt(v);
  }//writeInt

  public void writeLong(long v)
      throws IOException {
    m_Dout.writeLong(v);
  }//writeLong

  //@commentstart@
  public void writeFloat(float v)
      throws IOException {
    m_Dout.writeFloat(v);
  }//writeFloat

  public void writeDouble(double v)
      throws IOException {
    m_Dout.writeDouble(v);
  }//writeDouble
  //@commentend@

  public void writeBytes(String s)
      throws IOException {
    int len = s.length();
    for (int i = 0; i < len; i++) {
      this.write((byte) s.charAt(i));
    }
  }//writeBytes

  public void writeChars(String s)
      throws IOException {
    m_Dout.writeChars(s);
  }//writeChars

  public void writeUTF(String str)
      throws IOException {
    m_Dout.writeUTF(str);
  }//writeUTF

}//class BytesOutputStream
