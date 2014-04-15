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

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.DataInput;

/**
 * Interface implementing a non word data handler for the
 * read/write multiple register commands (class 0).
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public interface NonWordDataHandler {

  /**
   * Returns the intermediate raw non-word data.
   *
   * @return the raw data as <tt>byte[]</tt>.
   */
  public byte[] getData();

  /**
   * Reads the non-word raw data based on an arbitrary
   * implemented structure.
   *
   * @param in the <tt>DataInput</tt> to read from.
   * @param reference to specify the offset as <tt>int</tt>.
   * @param count to sepcify the amount of bytes as <tt>int</tt>.
   *
   * @throws IOException if I/O fails.
   * @throws EOFException if the stream ends before all data is read.
   */
  public void readData(DataInput in, int reference, int count)
      throws IOException, EOFException;

  /**
   * Returns the word count of the data.
   * Note that this should be the length of the byte
   * array divided by two.
   *
   * @return the number of words the data consists of.
   */
  public int getWordCount();

  /**
   * Commits the data if it has been read into an intermediate
   * repository.
   * This method is called by a <tt>WriteMultipleRegistersRequest</tt>
   * instance when finished with reading, for creating a response.
   *
   * @return -1 if the commit was successful, a Modbus exception code
   *         valid for the read/write multiple registers commands
   *         otherwise.
   */
  public int commitUpdate();

  /**
   * Prepares the raw data, putting it together from a
   * backing data store.
   * This method is called by a <tt>ReadMultipleRegistersRequest</tt>
   * instance when finshed with reading, for creating a response.
   *
   * @param reference to specify the offset as <tt>int</tt>.
   * @param count to sepcify the amount of bytes as <tt>int</tt>.
   */
  public void prepareData(int reference, int count);

}//NonWordDataHandler
