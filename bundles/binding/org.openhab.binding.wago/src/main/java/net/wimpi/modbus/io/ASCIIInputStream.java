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

import net.wimpi.modbus.Modbus;

import java.io.IOException;
import java.io.InputStream;
import java.io.FilterInputStream;

/**
 * Class implementing a specialized <tt>InputStream</tt> which
 * decodes characters read from the raw stream into bytes.
 *
 * Note that the characters denoting start and end of a frame
 * as given by the specification are exceptions;
 * They are translated to the "virtual" FRAME_START and FRAME_END.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 *
 * @see net.wimpi.modbus.io.ModbusASCIITransport#FRAME_START
 * @see net.wimpi.modbus.io.ModbusASCIITransport#FRAME_END
 */
public class ASCIIInputStream
    extends FilterInputStream {

  /**
   * Constructs a new <tt>ASCIIInputStream</tt> instance
   * reading from the given <tt>InputStream</tt>.
   *
   * @param in a base input stream to be wrapped.
   */
  public ASCIIInputStream(InputStream in) {
    super(in);
  }//constructor

  /**
   * Reads a byte from the ASCII encoded stream.
   *
   * @return int the byte read from the stream.
   * @throws IOException if an I/O error occurs.
   */
  public int read() throws IOException {
    StringBuffer sbuf = new StringBuffer(2);
    int ch = in.read();
    if(ch == -1) {
      return -1;
    }
    //System.out.println("Read "+ch+ "="+(char)ch);
    sbuf.append((char) ch);
    if (sbuf.charAt(0) == ':') {
      //System.out.println("FRAME START");
      return ModbusASCIITransport.FRAME_START;
    } else {
      if (sbuf.charAt(0) == '\r') {
        if (in.read() == 10) {
          //System.out.println("FRAME END");
          return ModbusASCIITransport.FRAME_END;
        } else {
          //malformed stream
          throw new IOException("Malformed Stream No Frame Delims");
        }
      } else {
        try {
          sbuf.append((char) in.read());
          //System.out.println("Read byte: " + sbuf.toString().toLowerCase());
          return Integer.parseInt(sbuf.toString().toLowerCase(), 16);
        } catch (NumberFormatException ex) {
          //malformed stream
          if(Modbus.debug) System.out.println(sbuf.toString());
          throw new IOException("Malformed Stream - Wrong Characters");
        }
      }
    }
  }//read

}//class ASCIIInputStream
