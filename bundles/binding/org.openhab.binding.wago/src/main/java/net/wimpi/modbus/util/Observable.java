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

import java.util.Vector;

/**
 * A cleanroom implementation of the Observable pattern.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class Observable {

  private Vector m_Observers;

  /**
   * Constructs a new Observable instance.
   */
  public Observable() {
    m_Observers = new Vector(10);
  }//constructor

  public int getObserverCount() {
    synchronized (m_Observers) {
      return m_Observers.size();
    }
  }//getObserverCount

  /**
   * Adds an observer instance if it is not already in the
   * set of observers for this <tt>Observable</tt>.
   *
   * @param o an observer instance to be added.
   */
  public void addObserver(Observer o) {
    synchronized (m_Observers) {
      if (!m_Observers.contains(o)) {
        m_Observers.addElement(o);

      }
    }
  }//addObserver

  /**
   * Removes an observer instance from the set of observers
   * of this <tt>Observable</tt>.
   *
   * @param o an observer instance to be removed.
   */
  public void removeObserver(Observer o) {
    synchronized (m_Observers) {
      m_Observers.removeElement(o);
    }
  }//removeObserver

  /**
   * Removes all observer instances from the set of observers
   * of this <tt>Observable</tt>.
   */
  public void removeObservers() {
    synchronized (m_Observers) {
      m_Observers.removeAllElements();
    }
  }//removeObservers

  /**
   * Notifies all observer instances in the set of observers
   * of this <tt>Observable</tt>.
   *
   * @param arg an arbitrary argument to be passed.
   */
  public void notifyObservers(Object arg) {
    synchronized (m_Observers) {
      for (int i = 0; i < m_Observers.size(); i++) {
        ((Observer) m_Observers.elementAt(i)).update(this, arg);
      }
    }
  }//notifyObservers

}//class Observable
