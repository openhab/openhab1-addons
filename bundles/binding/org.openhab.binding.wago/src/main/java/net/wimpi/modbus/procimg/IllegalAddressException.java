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
 * Class implementing an <tt>IllegalAddressException</tt>.
 * This exception is thrown when a non-existant spot in
 * the process image was addressed.<p>
 * Note that this is a runtime exception, as it is similar
 * to the <tt>IndexOutOfBoundsException</tt>
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class IllegalAddressException
    extends RuntimeException {

  /**
   * Constructs a new <tt>IllegalAddressException</tt>.
   */
  public IllegalAddressException() {
  }//constructor()

  /**
   * Constructs a new <tt>IllegalAddressException</tt>
   * with the given message.
   *
   * @param message a message as <tt>String</tt>.
   */
  public IllegalAddressException(String message) {
    super(message);
  }//constructor(String)

}//class IllegalAddressException
