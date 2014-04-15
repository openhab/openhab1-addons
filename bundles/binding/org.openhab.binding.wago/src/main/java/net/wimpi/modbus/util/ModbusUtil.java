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

package net.wimpi.modbus.util;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.BytesOutputStream;
import net.wimpi.modbus.msg.ModbusMessage;

import java.io.IOException;

/**
 * Helper class that provides utility methods.
 *
 * @author Dieter Wimberger
 * @author John Charlton
 *
 * @version @version@ (@date@)
 */
public final class ModbusUtil {

  private static BytesOutputStream m_ByteOut =
      new BytesOutputStream(Modbus.MAX_MESSAGE_LENGTH);

  /**
   * Converts a <tt>ModbusMessage</tt> instance into
   * a hex encoded string representation.
   *
   * @param msg the message to be converted.
   * @return the converted hex encoded string representation of the message.
   */
  public static final String toHex(ModbusMessage msg) {
    String ret = "-1";
    try {
      synchronized (m_ByteOut) {
        msg.writeTo(m_ByteOut);
        ret = toHex(m_ByteOut.getBuffer(), 0, m_ByteOut.size());
        m_ByteOut.reset();
      }
    } catch (IOException ex) {
    }
    return ret;
  }//toHex

  /**
   * Returns the given byte[] as hex encoded string.
   *
   * @param data a byte[] array.
   * @return a hex encoded String.
   */
  public static final String toHex(byte[] data) {
    return toHex(data, 0, data.length);
  }//toHex

  /**
   * Returns a <tt>String</tt> containing unsigned hexadecimal
   * numbers as digits.
   * The <tt>String</tt> will coontain two hex digit characters
   * for each byte from the passed in <tt>byte[]</tt>.<br>
   * The bytes will be separated by a space character.
   * <p/>
   *
   * @param data the array of bytes to be converted into a hex-string.
   * @param off the offset to start converting from.
   * @param length the number of bytes to be converted.
   *
   * @return	the generated hexadecimal representation as <code>String</code>.
   */
  public static final String toHex(byte[] data, int off, int length) {
    //double size, two bytes (hex range) for one byte
    StringBuffer buf = new StringBuffer(data.length * 2);
    for (int i = off; i < length; i++) {
      //don't forget the second hex digit
      if (((int) data[i] & 0xff) < 0x10) {
        buf.append("0");
      }
      buf.append(Long.toString((int) data[i] & 0xff, 16));
      if (i < data.length - 1) {
        buf.append(" ");
      }
    }
    return buf.toString();
  }//toHex

  /**
   * Returns a <tt>byte[]</tt> containing the given
   * byte as unsigned hexadecimal number digits.
   * <p/>
   *
   * @param i the int to be converted into a hex string.
   * @return the generated hexadecimal representation as <code>byte[]</code>.
   */
  public static final byte[] toHex(int i) {
    StringBuffer buf = new StringBuffer(2);
    //don't forget the second hex digit
    if (((int) i & 0xff) < 0x10) {
      buf.append("0");
    }
    buf.append(Long.toString((int) i & 0xff, 16).toUpperCase());
    return buf.toString().getBytes();
  }//toHex

  /**
   * Converts the register (a 16 bit value) into an unsigned short.
   * The value returned is:
   * <p><pre><code>(((a &amp; 0xff) &lt;&lt; 8) | (b &amp; 0xff))
   * </code></pre>
   * <p/>
   * This conversion has been taken from the documentation of
   * the <tt>DataInput</tt> interface.
   *
   * @param bytes a register as <tt>byte[2]</tt>.
   * @return the unsigned short value as <tt>int</tt>.
   * @see java.io.DataInput
   */
  public static final int registerToUnsignedShort(byte[] bytes) {
    return ((bytes[0] & 0xff) << 8 | (bytes[1] & 0xff));
  }//registerToUnsignedShort

  /**
   * Converts the given unsigned short into a register
   * (2 bytes).
   * The byte values in the register, in the  order
   * shown, are:
   * <p/>
   * <pre><code>
   * (byte)(0xff &amp; (v &gt;&gt; 8))
   * (byte)(0xff &amp; v)
   * </code></pre>
   * <p/>
   * This conversion has been taken from the documentation of
   * the <tt>DataOutput</tt> interface.
   *
   * @param v
   * @return the register as <tt>byte[2]</tt>.
   * @see java.io.DataOutput
   */
  public static final byte[] unsignedShortToRegister(int v) {
    byte[] register = new byte[2];
    register[0] = (byte) (0xff & (v >> 8));
    register[1] = (byte) (0xff & v);
    return register;
  }//unsignedShortToRegister

  /**
   * Converts the given register (16-bit value) into
   * a <tt>short</tt>.
   * The value returned is:
   * <p/>
   * <pre><code>
   * (short)((a &lt;&lt; 8) | (b &amp; 0xff))
   * </code></pre>
   * <p/>
   * This conversion has been taken from the documentation of
   * the <tt>DataInput</tt> interface.
   *
   * @param bytes bytes a register as <tt>byte[2]</tt>.
   * @return the signed short as <tt>short</tt>.
   */
  public static final short registerToShort(byte[] bytes) {
    return (short) ((bytes[0] << 8) | (bytes[1] & 0xff));
  }//registerToShort


  /**
   * Converts the register (16-bit value) at the given index
   * into a <tt>short</tt>.
   * The value returned is:
   * <p/>
   * <pre><code>
   * (short)((a &lt;&lt; 8) | (b &amp; 0xff))
   * </code></pre>
   * <p/>
   * This conversion has been taken from the documentation of
   * the <tt>DataInput</tt> interface.
   *
   * @param bytes a <tt>byte[]</tt> containing a short value.
   * @param idx an offset into the given byte[].
   * @return the signed short as <tt>short</tt>.
   */
  public static final short registerToShort(byte[] bytes, int idx) {
    return (short) ((bytes[idx] << 8) | (bytes[idx + 1] & 0xff));
  }//registerToShort

  /**
   * Converts the given <tt>short</tt> into a register
   * (2 bytes).
   * The byte values in the register, in the  order
   * shown, are:
   * <p/>
   * <pre><code>
   * (byte)(0xff &amp; (v &gt;&gt; 8))
   * (byte)(0xff &amp; v)
   * </code></pre>
   *
   * @param s
   * @return a register containing the given short value.
   */
  public static final byte[] shortToRegister(short s) {
    byte[] register = new byte[2];
    register[0] = (byte) (0xff & (s >> 8));
    register[1] = (byte) (0xff & s);
    return register;
  }//shortToRegister

  /**
   * Converts a byte[4] binary int value to a primitive int.<br>
   * The value returned is:
   * <p><pre>
   * <code>
   * (((a &amp; 0xff) &lt;&lt; 24) | ((b &amp; 0xff) &lt;&lt; 16) |
   * &#32;((c &amp; 0xff) &lt;&lt; 8) | (d &amp; 0xff))
   * </code></pre>
   *
   * @param bytes registers as <tt>byte[4]</tt>.
   * @return the integer contained in the given register bytes.
   */
  public static final int registersToInt(byte[] bytes) {
    return (
        ((bytes[0] & 0xff) << 24) |
        ((bytes[1] & 0xff) << 16) |
        ((bytes[2] & 0xff) << 8) |
        (bytes[3] & 0xff)
        );
  }//registersToInt

  /**
   * Converts an int value to a byte[4] array.
   *
   * @param v the value to be converted.
   * @return a byte[4] containing the value.
   */
  public static final byte[] intToRegisters(int v) {
    byte[] registers = new byte[4];
    registers[0] = (byte) (0xff & (v >> 24));
    registers[1] = (byte) (0xff & (v >> 16));
    registers[2] = (byte) (0xff & (v >> 8));
    registers[3] = (byte) (0xff & v);
    return registers;
  }//intToRegisters

  /**
   * Converts a byte[8] binary long value into a long
   * primitive.
   *
   * @param bytes a byte[8] containing a long value.
   * @return a long value.
   */
  public static final long registersToLong(byte[] bytes) {
    return (
        (((long) (bytes[0] & 0xff) << 56) |
        ((long) (bytes[1] & 0xff) << 48) |
        ((long) (bytes[2] & 0xff) << 40) |
        ((long) (bytes[3] & 0xff) << 32) |
        ((long) (bytes[4] & 0xff) << 24) |
        ((long) (bytes[5] & 0xff) << 16) |
        ((long) (bytes[6] & 0xff) << 8) |
        ((long) (bytes[7] & 0xff)))
        );
  }//registersToLong

  /**
   * Converts a long value to a byte[8].
   *
   * @param v the value to be converted.
   * @return a byte[8] containing the long value.
   */
  public static final byte[] longToRegisters(long v) {
    byte[] registers = new byte[8];
    registers[0] = (byte) (0xff & (v >> 56));
    registers[1] = (byte) (0xff & (v >> 48));
    registers[2] = (byte) (0xff & (v >> 40));
    registers[3] = (byte) (0xff & (v >> 32));
    registers[4] = (byte) (0xff & (v >> 24));
    registers[5] = (byte) (0xff & (v >> 16));
    registers[6] = (byte) (0xff & (v >> 8));
    registers[7] = (byte) (0xff & v);
    return registers;
  }//longToRegisters

  /**
   * Converts a byte[4] binary float value to a float primitive.
   *
   * @param bytes the byte[4] containing the float value.
   * @return a float value.
   */
  public static final float registersToFloat(byte[] bytes) {
    return Float.intBitsToFloat((
        ((bytes[0] & 0xff) << 24) |
        ((bytes[1] & 0xff) << 16) |
        ((bytes[2] & 0xff) << 8) |
        (bytes[3] & 0xff)
        ));
  }//registersToFloat

  /**
   * Converts a float value to a byte[4] binary float value.
   *
   * @param f the float to be converted.
   * @return a byte[4] containing the float value.
   */
  public static final byte[] floatToRegisters(float f) {
    return intToRegisters(Float.floatToIntBits(f));
  }//floatToRegisters

  /**
   * Converts a byte[8] binary double value into a double primitive.
   *
   * @param bytes a byte[8] to be converted.
   * @return a double value.
   */
  public static final double registersToDouble(byte[] bytes) {
    return Double.longBitsToDouble((
        (((long) (bytes[0] & 0xff) << 56) |
        ((long) (bytes[1] & 0xff) << 48) |
        ((long) (bytes[2] & 0xff) << 40) |
        ((long) (bytes[3] & 0xff) << 32) |
        ((long) (bytes[4] & 0xff) << 24) |
        ((long) (bytes[5] & 0xff) << 16) |
        ((long) (bytes[6] & 0xff) << 8) |
        ((long) (bytes[7] & 0xff)))
        ));
  }//registersToDouble

  /**
   * Converts a double value to a byte[8].
   *
   * @param d the double to be converted.
   * @return a byte[8].
   */
  public static final byte[] doubleToRegisters(double d) {
    return longToRegisters(Double.doubleToLongBits(d));
  }//doubleToRegisters

  /**
   * Converts an unsigned byte to an integer.
   *
   * @param b the byte to be converted.
   * @return an integer containing the unsigned byte value.
   */
  public static final int unsignedByteToInt(byte b) {
    return (int) b & 0xFF;
  }//unsignedByteToInt

  /**
   * Returns the broadcast address for the subnet of the host the code
   * is executed on.
   *
   * @return the broadcast address as <tt>InetAddress</tt>.
   *         <p/>
   *         public static final InetAddress getBroadcastAddress() {
   *         byte[] addr = new byte[4];
   *         try {
   *         addr = InetAddress.getLocalHost().getAddress();
   *         addr[3] = -1;
   *         return getAddressFromBytes(addr);
   *         } catch (Exception ex) {
   *         ex.printStackTrace();
   *         return null;
   *         }
   *         }//getBroadcastAddress
   */

  /*
  public static final InetAddress getAddressFromBytes(byte[] addr) throws Exception {
    StringBuffer sbuf = new StringBuffer();
    for (int i = 0; i < addr.length; i++) {
      if (addr[i] < 0) {
        sbuf.append(256 + addr[i]);
      } else {
        sbuf.append(addr[i]);
      }
      if (i < (addr.length - 1)) {
        sbuf.append('.');
      }
    }
    //DEBUG:System.out.println(sbuf.toString());
    return InetAddress.getByName(sbuf.toString());
  }//getAddressFromBytes
  */

  //TODO: John description.
  /**
   * Returs the low byte of an integer word.
   *
   * @param wd
   * @return the low byte.
   */
  public static final byte lowByte(int wd) {
    return (new Integer(0xff & wd).byteValue());
  }// lowByte

  //TODO: John description.
  /**
   *
   * @param wd
   * @return the hi byte.
   */
  public static final byte hiByte(int wd) {
    return (new Integer(0xff & (wd >> 8)).byteValue());
  }// hiByte

   //TODO: John description.
  /**
   *
   * @param hibyte
   * @param lowbyte
   * @return a word.
   */
  public static final int makeWord(int hibyte, int lowbyte) {
    int hi = 0xFF & hibyte;
    int low = 0xFF & lowbyte;
    return ((hi << 8) | low);
  }// makeWord

  public static final int[] calculateCRC(byte[] data, int offset, int len) {

    int[] crc = {0xFF, 0xFF};
    int nextByte = 0;
    int uIndex; /* will index into CRC lookup*/ /* table */
    /* pass through message buffer */
    for (int i = offset; i < len && i < data.length; i++) {
      nextByte = 0xFF & ((int) data[i]);
      uIndex = crc[0] ^ nextByte; //*puchMsg++; /* calculate the CRC */
      crc[0] = crc[1] ^ auchCRCHi[uIndex];
      crc[1] = auchCRCLo[uIndex];
    }

    return crc;
  }//calculateCRC

  public static final int calculateLRC(byte[] data, int off, int len) {
    int lrc = 0;
    for (int i = off; i < len; i++) {
      lrc += (int) data[i] & 0xff; //calculate with unsigned bytes
    }
    lrc = (lrc ^ 0xff) + 1;      // two's complement
    return (int) ((byte) lrc) & 0xff;
  }//calculateLRC


  /* Table of CRC values for high-order byte */
  private final static short[] auchCRCHi = {
    0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
    0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
    0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
    0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
    0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
    0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
    0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
    0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
    0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
    0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
    0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
    0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
    0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
    0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
    0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
    0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
    0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
    0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
    0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
    0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
    0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
    0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
    0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
    0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
    0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
    0x80, 0x41, 0x00, 0xC1, 0x81, 0x40
  };

  /* Table of CRC values for low-order byte */
  private final static short[] auchCRCLo = {
    0x00, 0xC0, 0xC1, 0x01, 0xC3, 0x03, 0x02, 0xC2, 0xC6, 0x06,
    0x07, 0xC7, 0x05, 0xC5, 0xC4, 0x04, 0xCC, 0x0C, 0x0D, 0xCD,
    0x0F, 0xCF, 0xCE, 0x0E, 0x0A, 0xCA, 0xCB, 0x0B, 0xC9, 0x09,
    0x08, 0xC8, 0xD8, 0x18, 0x19, 0xD9, 0x1B, 0xDB, 0xDA, 0x1A,
    0x1E, 0xDE, 0xDF, 0x1F, 0xDD, 0x1D, 0x1C, 0xDC, 0x14, 0xD4,
    0xD5, 0x15, 0xD7, 0x17, 0x16, 0xD6, 0xD2, 0x12, 0x13, 0xD3,
    0x11, 0xD1, 0xD0, 0x10, 0xF0, 0x30, 0x31, 0xF1, 0x33, 0xF3,
    0xF2, 0x32, 0x36, 0xF6, 0xF7, 0x37, 0xF5, 0x35, 0x34, 0xF4,
    0x3C, 0xFC, 0xFD, 0x3D, 0xFF, 0x3F, 0x3E, 0xFE, 0xFA, 0x3A,
    0x3B, 0xFB, 0x39, 0xF9, 0xF8, 0x38, 0x28, 0xE8, 0xE9, 0x29,
    0xEB, 0x2B, 0x2A, 0xEA, 0xEE, 0x2E, 0x2F, 0xEF, 0x2D, 0xED,
    0xEC, 0x2C, 0xE4, 0x24, 0x25, 0xE5, 0x27, 0xE7, 0xE6, 0x26,
    0x22, 0xE2, 0xE3, 0x23, 0xE1, 0x21, 0x20, 0xE0, 0xA0, 0x60,
    0x61, 0xA1, 0x63, 0xA3, 0xA2, 0x62, 0x66, 0xA6, 0xA7, 0x67,
    0xA5, 0x65, 0x64, 0xA4, 0x6C, 0xAC, 0xAD, 0x6D, 0xAF, 0x6F,
    0x6E, 0xAE, 0xAA, 0x6A, 0x6B, 0xAB, 0x69, 0xA9, 0xA8, 0x68,
    0x78, 0xB8, 0xB9, 0x79, 0xBB, 0x7B, 0x7A, 0xBA, 0xBE, 0x7E,
    0x7F, 0xBF, 0x7D, 0xBD, 0xBC, 0x7C, 0xB4, 0x74, 0x75, 0xB5,
    0x77, 0xB7, 0xB6, 0x76, 0x72, 0xB2, 0xB3, 0x73, 0xB1, 0x71,
    0x70, 0xB0, 0x50, 0x90, 0x91, 0x51, 0x93, 0x53, 0x52, 0x92,
    0x96, 0x56, 0x57, 0x97, 0x55, 0x95, 0x94, 0x54, 0x9C, 0x5C,
    0x5D, 0x9D, 0x5F, 0x9F, 0x9E, 0x5E, 0x5A, 0x9A, 0x9B, 0x5B,
    0x99, 0x59, 0x58, 0x98, 0x88, 0x48, 0x49, 0x89, 0x4B, 0x8B,
    0x8A, 0x4A, 0x4E, 0x8E, 0x8F, 0x4F, 0x8D, 0x4D, 0x4C, 0x8C,
    0x44, 0x84, 0x85, 0x45, 0x87, 0x47, 0x46, 0x86, 0x82, 0x42,
    0x43, 0x83, 0x41, 0x81, 0x80, 0x40
  };

}//class ModBusUtil
