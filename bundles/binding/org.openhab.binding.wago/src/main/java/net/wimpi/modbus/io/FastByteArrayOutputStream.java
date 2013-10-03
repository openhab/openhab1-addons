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


import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * This class is a clean room implementation
 * of the ByteArrayOutputStream, with enhancements for
 * speed (no synchronization for example).
 * <p/>
 * The idea for such an implementation was originally
 * obtained from Berkeley DB JE, however, this represents a
 * clean-room implementation that is <em>NOT</em> derived
 * from their implementation for license reasons and differs
 * in implementation considerably. For compatibility reasons
 * we have tried to conserve the interface as much as possible.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class FastByteArrayOutputStream
    extends OutputStream {

  protected int count;
  protected byte[] buf;

  /**
   * Create a new <tt>FastByteArrayOutputStream</tt>.
   * The default value {@link #DEFAULT_SIZE} will be used.
   */
  public FastByteArrayOutputStream() {
    buf = new byte[DEFAULT_SIZE];
  }//constructor

  /**
   * Create a new <tt>FastByteArrayOutputStream</tt> with a given
   * initial buffer size.
   *
   * @param bufferSize the initial size of the buffer as <tt>int</tt>.
   */
  public FastByteArrayOutputStream(int bufferSize) {
    buf = new byte[bufferSize];
  }//constructor

  /**
   * Create a new <tt>FastByteArrayOutputStream</tt> with a given
   * initial buffer.
   *
   * @param buf the buffer as <tt>byte[]</tt>.
   */
  public FastByteArrayOutputStream(byte[] buf) {
    this.buf = buf;
  }//constructor

  /**
   * Closing a <tt>FastByteArrayOutputStream</tt> has no effect.
   * The methods in this class can be called after the stream has
   * been closed without generating an IOException.
   */
  public void close() {
  }//close

  /**
   * Resets the count of this <tt>FastByteArrayOutputStream</tt>
   * to zero, so that all currently accumulated output in the
   * ouput stream is discarded when overwritten.
   */
  public void reset() {
    count = 0;
  }//reset


  /**
   * Returns the current number of bytes in the buffer of this
   * <tt>FastByteArrayOutputStream</tt>.
   *
   * @return the amount of bytes in the buffer.
   */
  public int size() {
    return count;
  }//size

  /**
   * Returns a newly allocated <tt>byte[]</tt> with the actual content
   * of the buffer of this <tt>FastByteArrayOutputStream</tt>.
   *
   * @return the current contents of this output stream, as a <tt>byte[]</tt>.
   * @see #size()
   */
  public byte[] toByteArray() {
    byte[] buf = new byte[count];
    System.arraycopy(this.buf, 0, buf, 0, count);
    return buf;
  }//toByteArray

  /**
   * Converts the buffer's contents into a string, translating bytes into
   * characters according to the platform's default character encoding.
   *
   * @return String translated from the buffer's contents.
   */
  public String toString() {
    return new String(buf, 0, count);
  }//toString

  /**
   * Converts the buffer's contents into a string, translating bytes into
   * characters according to the specified character encoding.
   *
   * @param enc a character-encoding name.
   * @return String translated from the buffer's contents.
   * @throws UnsupportedEncodingException If the named encoding is not supported.
   */
  public String toString(String enc)
      throws UnsupportedEncodingException {
    return new String(buf, 0, count, enc);
  }//toString

  /**
   * Writes len bytes from the specified byte array starting at offset off
   * to this <tt>FastByteArrayOutputStream</tt>.
   *
   * @param b   the data.
   * @param off the start offset in the data.
   * @param len the number of bytes to write.
   */
  public void write(byte[] b, int off, int len) {
    ensureCapacity(count + len);
    System.arraycopy(b, off, buf, count, len);
    count += len;
  }//write


  /**
   * Writes the specified byte to this byte array output stream.
   *
   * @param b the byte to be written.
   */
  public void write(int b) {
    ensureCapacity(count + 1);
    buf[count++] = (byte) b;
  }//write

  /**
   * Writes the complete contents of this <tt>FastByteArrayOutputStream</tt> to
   * the specified output stream argument.
   *
   * @param out the output stream to which to write the data.
   * @throws IOException if an I/O error occurs.
   */
  public void writeTo(OutputStream out)
      throws IOException {
    out.write(buf, 0, count);
  }//writeTo

  /**
   * Convenience method that writes all bytes from the specified byte
   * array to this <tt>FastByteArrayOutputStream</tt>.
   *
   * @param buf
   * @throws IOException
   */
  public void write(byte[] buf) throws IOException {
    write(buf, 0, buf.length);
  }//write

  /**
   * Increases the capacity of this <tt>FastByteArrayOutputStream</tt>s buffer,
   * if necessary, to ensure that it can hold at least the number of
   * bytes specified by the minimum capacity argument.
   *
   * @param minCapacity the desired minimum capacity.
   */
  public final void ensureCapacity(int minCapacity) {
    if (minCapacity < buf.length) {
      return;
    } else {
      byte[] newbuf = new byte[minCapacity];
      System.arraycopy(buf, 0, newbuf, 0, count);
      buf = newbuf;
    }
  }//ensureCapacity

  /**
   * Copies the content of this <tt>FastByteArrayOutputStream</tt>
   * into the given byte array, starting from the given offset.
   *
   * @param b  the buffer to hold a copy of the data.
   * @param offset the offset at which to start copying.
   */
  public void toByteArray(byte[] b, int offset) {
    if(offset >= b.length) {
      throw new IndexOutOfBoundsException();
    }
    int len = count-offset;
    if(len > b.length) {
      System.arraycopy(buf, offset, b, offset, b.length); //just copy what is there
    } else {
      System.arraycopy(buf, offset, b, offset, len);
    }
  }//toByteArray

  /**
   * Returns a reference to the buffer of this
   * <tt>FastByteArrayOutputStream</tt>.
   *
   * @return the buffer.
   */
  public byte[] getBuffer() {
    return buf;
  }//getBuffer


  public static final int DEFAULT_SIZE = 512;

}//class FastByteArrayOutputStream
