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
 * Interface defining a digital output.
 * <p>
 * In Modbus terms this represents a
 * coil, which is read-write from slave and
 * master or device side.<br>
 * Therefor implementations have to be carefully
 * designed for concurrency.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public interface DigitalOut {

  /**
   * Tests if this <tt>DigitalOut</tt> is set.
   * <p>
   *
   * @return true if set, false otherwise.
   */
  public boolean isSet();

  /**
   * Sets the state of this <tt>DigitalOut</tt>.
   * <p>
   *
   * @param b true if to be set, false otherwise.
   */
  public void set(boolean b);

}//DigitalOut
