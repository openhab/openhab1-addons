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
import java.io.FilterInputStream;

/**
 * Class implementing a specialized <tt>InputStream</tt> which
 * handles binary transmitted messages.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 *
 * @see ModbusBINTransport#FRAME_START
 * @see ModbusBINTransport#FRAME_END
 */
public class BINInputStream
    extends FilterInputStream {

  /**
   * Constructs a new <tt>BINInputStream</tt> instance
   * reading from the given <tt>InputStream</tt>.
   *
   * @param in a base input stream to be wrapped.
   */
  public BINInputStream(InputStream in) {
    super(in);
    if(!in.markSupported()) {
      throw new RuntimeException("Accepts only input streams that support marking.");
    }
  }//constructor


  /**
   * Reads a byte from the BIN encoded stream.
   *
   * @return int the byte read from the stream.
   * @throws java.io.IOException if an I/O error occurs.
   */
  public int read() throws IOException {
    int ch = in.read();
    if(ch == -1) {
      return -1;
    } else if (ch == ModbusBINTransport.FRAME_START_TOKEN) {
      in.mark(1);
      //read next
      ch = in.read();
      if(ch == ModbusBINTransport.FRAME_START_TOKEN) {
        return ch;
      } else {
        in.reset();
        return ModbusBINTransport.FRAME_START;
      }
    } else if(ch == ModbusBINTransport.FRAME_END_TOKEN) {
      in.mark(1);
      //read next
      ch = in.read();
      if(ch == ModbusBINTransport.FRAME_END_TOKEN) {
        return ch;
      } else {
        in.reset();
        return ModbusBINTransport.FRAME_END;
      }
    } else {
      return ch;
    }
  }//read



}//class BINInputStream
