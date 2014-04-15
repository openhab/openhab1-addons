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

/**
 * Provides an atomic integer.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class AtomicCounter {

  private int m_Value;

  /**
   * Constructs a new <tt>AtomicInteger</tt>.
   */
  public AtomicCounter() {
    m_Value = 0;
  }//constructor

  /**
   * Constructs a new <tt>AtomicInteger</tt>
   * with a given initial value.
   *
   * @param value the initial value.
   */
  public AtomicCounter(int value) {
    m_Value = value;
  }//constructor

  /**
   * Increments this <tt>AtomicInteger</tt> by one.
   *
   * @return the resulting value.
   */
  public synchronized int increment() {
    if (m_Value == Modbus.MAX_TRANSACTION_ID) {
      m_Value = 0;
    }
    return ++m_Value;
  }//increment

  /**
   * Returns the value of this <tt>AtomicInteger</tt>.
   * @return the actual value.
   */
  public synchronized int get() {
    return m_Value;
  }//get

}//class AtomicCounter
