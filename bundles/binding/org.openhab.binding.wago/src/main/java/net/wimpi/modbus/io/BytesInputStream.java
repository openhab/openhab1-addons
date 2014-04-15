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

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Class implementing a byte array input stream with
 * a DataInput interface.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class BytesInputStream
    extends FastByteArrayInputStream
    implements DataInput {

  DataInputStream m_Din;

  /**
   * Constructs a new <tt>BytesInputStream</tt> instance,
   * with an empty buffer of a given size.
   *
   * @param size the size of the input buffer.
   */
  public BytesInputStream(int size) {
    super(new byte[size]);
    m_Din = new DataInputStream(this);
  }//BytesInputStream

  /**
   * Constructs a new <tt>BytesInputStream</tt> instance,
   * that will read from the given data.
   *
   * @param data a byte array containing data to be read.
   */
  public BytesInputStream(byte[] data) {
    super(data);
    m_Din = new DataInputStream(this);
  }//BytesInputStream

  /**
   * Resets this <tt>BytesInputStream</tt> using the given
   * byte[] as new input buffer.
   *
   * @param data a byte array with data to be read.
   */
  public void reset(byte[] data) {
   // System.out.println("reset(byte[])::count=" + count + " pos=" + pos);
    pos = 0;
    mark = 0;
    buf = data;
    count = data.length;
  }//reset

  /**
   * Resets this <tt>BytesInputStream</tt> using the given
   * byte[] as new input buffer and a given length.
   *
   * @param data a byte array with data to be read.
   * @param length the length of the buffer to be considered.
   */
  public void reset(byte[] data, int length) {
    pos = 0;
    mark = 0;
    count = length;
    buf = data;
    readlimit = -1;
    //System.out.println("reset(byte[],int)::count=" + count + " pos=" + pos);
  }//reset

  /**
   * Resets this <tt>BytesInputStream</tt>  assigning the input buffer
   * a new length.
   *
   * @param length the length of the buffer to be considered.
   */
  public void reset(int length) {
    //System.out.println("reset(int)::count=" + count + " pos=" + pos);
    pos = 0;
    count = length;
  }//reset

  /**
   * Skips the given number of bytes or all bytes till the end
   * of the assigned input buffer length.
   *
   * @param n the number of bytes to be skipped as <tt>int</tt>.
   * @return the number of bytes skipped.
   */
  public int skip(int n) {
    mark(pos);
    pos += n;
    return n;
  }//skip

  /**
   * Returns the reference to the input buffer.
   *
   * @return the reference to the <tt>byte[]</tt> input buffer.
   */
  public byte[] getBuffer() {
    return buf;
  }//getBuffer

  public int getBufferLength() {
    return buf.length;
  }//getBufferLength

  public void readFully(byte b[])
      throws IOException {
    m_Din.readFully(b);
  }//readFully

  public void readFully(byte b[], int off, int len)
      throws IOException {
    m_Din.readFully(b, off, len);
  }//readFully

  public int skipBytes(int n)
      throws IOException {
    return m_Din.skipBytes(n);
  }//skipBytes

  public boolean readBoolean()
      throws IOException {
    return m_Din.readBoolean();
  }//readBoolean

  public byte readByte()
      throws IOException {
    return m_Din.readByte();
  }

  public int readUnsignedByte()
      throws IOException {
    return m_Din.readUnsignedByte();
  }//readUnsignedByte

  public short readShort()
      throws IOException {
    return m_Din.readShort();
  }//readShort

  public int readUnsignedShort()
      throws IOException {
    return m_Din.readUnsignedShort();
  }//readUnsignedShort

  public char readChar()
      throws IOException {
    return m_Din.readChar();
  }//readChar

  public int readInt()
      throws IOException {
    return m_Din.readInt();
  }//readInt

  public long readLong()
      throws IOException {
    return m_Din.readLong();
  }//readLong

  //@commentstart@
  public float readFloat()
      throws IOException {
    return m_Din.readFloat();
  }//readFloat

  public double readDouble()
      throws IOException {
    return m_Din.readDouble();
  }//readDouble
  //@commentend@

  public String readLine()
      throws IOException {
    throw new IOException("Not supported.");
  }//readLine

  public String readUTF()
      throws IOException {
    return m_Din.readUTF();
  }//readUTF

}//class BytesInputStream

