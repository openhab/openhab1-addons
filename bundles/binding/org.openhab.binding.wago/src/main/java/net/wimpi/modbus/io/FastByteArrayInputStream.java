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
import java.io.InputStream;

/**
 * This class is a clean room implementation
 * of the ByteArrayInputStream, with enhancements for
 * speed (no synchronization for example).
 * <p/>
 * The idea for such an implementation was originally
 * obtained from Berkeley DB JE, however, this represents a
 * clean-room implementation that is <em>NOT</em> derived
 * from their implementation for license reasons and differs
 * in implementation considerably. For compatibility reasons
 * we have tried to conserve the interface as much as possible.
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class FastByteArrayInputStream
    extends InputStream {

  protected int count;
  protected int pos;
  protected int mark;
  protected byte[] buf;
  protected int readlimit = -1;

  /**
   * Creates a new <tt>FastByteArrayInputStream</tt> instance
   * that allows to read from the given byte array.
   *
   * @param buffer the data to be read.
   */
  public FastByteArrayInputStream(byte[] buffer) {
    buf = buffer;
    count = buf.length;
    pos = 0;
    mark = 0;
  }//constructor

  /**
   * Creates a new <tt>FastByteArrayInputStream</tt> instance
   * that allows to read from the given byte array.
   *
   * @param buffer the data to read.
   * @param offset the byte offset at which to begin reading.
   * @param length the number of bytes to read.
   */
  public FastByteArrayInputStream(byte[] buffer, int offset, int length) {
    buf = buffer;
    pos = offset;
    count = length;
  }//constructor

  /**
   * Reads the next byte of data from this input stream. The value byte
   * is returned as an int in the range 0 to 255. If no byte is available
   * because the end of the stream has been reached, the value -1 is returned.
   * <p/>
   * This read method cannot block.
   * </p>
   *
   * @return the next byte of data, or -1 if the end of the stream has been reached.
   * @throws IOException
   */
  public int read() throws IOException {
    if ((pos < count)) {
      return (buf[pos++] & 0xff);
    } else {
      return (-1);
    }
  }//read

  /**
   * Reads up to len bytes of data into an array of bytes from this input stream.
   * If pos equals count, then -1 is returned to indicate end of file.
   * Otherwise, the number k  of bytes read is equal to the smaller of
   * len and count-pos. If k is positive, then bytes buf[pos] through buf[pos+k-1]
   * are copied into b[off] through b[off+k-1] in the manner performed by
   * System.arraycopy. The value k is added into pos  and k is returned.
   *
   * @param toBuf  the buffer into which the data is read.
   * @param offset the start offset of the data.
   * @param length the max number of bytes read.
   * @return the total number of bytes read into the buffer, or -1 if there is no
   *         more data because the end of the stream has been reached.
   * @throws IOException
   */
  public int read(byte[] toBuf, int offset, int length)
      throws IOException {
    int avail = count - pos;

    if (avail <= 0) {
      return -1;
    }
    if (length > avail) {
      length = avail;
    }
    System.arraycopy(buf, pos, toBuf, offset, length);
    pos += length;

    return length;
  }//read

  public int read(byte[] toBuf) throws IOException {
    return read(toBuf, 0, toBuf.length);
  }//read

  /**
   * Skips over and discards n bytes of data from this input stream.
   * The skip method may skip over some smaller number of bytes.
   * The actual number of bytes skipped is returned, or a number <=0
   * if none was skipped.
   * <p/>
   * The maximum number of bytes that can be skipped is defined by
   * <tt>Integer.MAX_VALUE</tt>.
   * </p>
   *
   * @param n the number of bytes to be skipped.
   * @return the actual number of bytes skipped.
   */
  public long skip(long n) {
    int skip = this.count - this.pos - (int) n;
    if (skip > 0) {
      pos += skip;
    }
    return skip;
  }//skip

  /**
   * The close method for this <tt>FastByteArrayInputStream</tt>
   * does nothing.
   */
  public void close() {
    return;
  }//close

  /**
   * Returns the number of bytes that can be read (or skipped over) from this
   * <tt>FastByteArrayInputStream</tt>.
   *
   * @return the number of bytes that can be skipped.
   */
  public int available() {
    return count - pos;
  }//available

  /**
   * Marks the current position in this <tt>FastByteArrayInputStream</tt>.
   * A subsequent call to{@link #reset()} will re-postition this <tt>FastByteArrayInputStream</tt>
   * at the last marked position so that subsequent reads re-read the same bytes.
   *
   * @param limit a read limit that invalidates the mark if passed.
   */
  public void mark(int limit) {
    mark = pos;
    readlimit = limit;
  }//mark

  /**
   * Tests if this <tt>FastByteArrayInputStream</tt>
   * supports the mark and reset methods.
   *
   * @return true if supported, false otherwise.
   */
  public boolean markSupported() {
    return true;
  }//markSupported

  /**
   * Re-positions this stream to the position at
   * the time the mark method was last called this <tt>FastByteArrayInputStream</tt>.
   * @throws IOException if the readlimit was exceeded.
   */
  public void reset() throws IOException {
    if(readlimit <0 || pos > mark+readlimit) {
      pos = mark;
      readlimit = -1;
    } else {
      mark = pos;
      readlimit = -1;
      throw new IOException("Readlimit exceeded.");
    }
  }//reset

  /**
   * Returns the underlying data being read.
   *
   * @return the underlying data.
   */
  public byte[] getBuffer() {
    return buf;
  }//getBuffer

  /**
   * Returns the offset at which data is being read from the buffer.
   *
   * @return the offset at which data is being read.
   */
  public int getPosition() {
    return pos;
  }//getPosition

  /**
   * Returns the size of the buffer being read.
   *
   * @return the size of the buffer.
   */
  public int size() {
    return count;
  }//size

}//class FastByteArrayInputStream
