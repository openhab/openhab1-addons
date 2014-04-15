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
 * Interface defining a digital input.
 * <p>
 * In Modbus terms this represents an
 * input discrete, it is read only from
 * the slave side.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public interface DigitalIn {

  /**
   * Tests if this <tt>DigitalIn</tt> is set.
   * <p>
   *
   * @return true if set, false otherwise.
   */
  public boolean isSet();
  
}//DigitalIn
