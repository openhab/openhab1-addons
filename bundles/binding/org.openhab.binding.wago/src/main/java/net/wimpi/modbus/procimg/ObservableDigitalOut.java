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

import net.wimpi.modbus.util.Observable;


/**
 * Class implementing an observable digital output.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class ObservableDigitalOut
    extends Observable
    implements DigitalOut {

  /**
   * A boolean holding the state of this digital out.
   */
  protected boolean m_Set;

  public boolean isSet() {
    return m_Set;
  }//isSet

  public void set(boolean b) {
    m_Set = b;
    notifyObservers("value");
  }//set

}//class ObservableDigitalIn
