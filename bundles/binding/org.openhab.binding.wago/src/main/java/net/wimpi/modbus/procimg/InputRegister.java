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

package net.wimpi.modbus.procimg;

/**
 * Interface defining an input register.
 * <p>
 * This register is read only from the slave side.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public interface InputRegister {

  /**
   * Returns the value of this <tt>InputRegister</tt>.
   * The value is stored as <tt>int</tt> but should be
   * treated like a 16-bit word.
   *
   * @return the value as <tt>int</tt>.
   */
  public int getValue();

  /**
   * Returns the content of this <tt>Register</tt> as
   * unsigned 16-bit value (unsigned short).
   *
   * @return the content as unsigned short (<tt>int</tt>).
   */
  public int toUnsignedShort();

  /**
   * Returns the content of this <tt>Register</tt> as
   * signed 16-bit value (short).
   *
   * @return the content as <tt>short</tt>.
   */
  public short toShort();

  /**
   * Returns the content of this <tt>Register</tt>
   * as bytes.
   *
   * @return a <tt>byte[]</tt> with length 2.
   */
  public byte[] toBytes();

}//interface InputRegister
