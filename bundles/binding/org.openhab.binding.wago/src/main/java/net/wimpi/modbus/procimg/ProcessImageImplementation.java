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
 * Interface defining implementation specific
 * details of the <tt>ProcessImage</tt>, adding
 * mechanisms for creating and modifying the actual
 * "process image".
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public interface ProcessImageImplementation extends ProcessImage {

  /**
   * Sets a new <tt>DigitalOut</tt> instance at the
   * given reference.
   *
   * @param ref the reference as <tt>int</tt>.
   * @param _do the new <tt>DigitalOut</tt> instance to be
   *        set.
   *
   * @throws IllegalAddressException if the reference is invalid.
   */
  public void setDigitalOut(int ref, DigitalOut _do)
      throws IllegalAddressException;

  /**
   * Adds a new <tt>DigitalOut</tt> instance.
   *
   * @param _do the <tt>DigitalOut</tt> instance to be
   *        added.
   */
  public void addDigitalOut(DigitalOut _do);

  /**
   * Removes a given <tt>DigitalOut</tt> instance.
   *
   * @param _do the <tt>DigitalOut</tt> instance to be
   *        removed.
   */
  public void removeDigitalOut(DigitalOut _do);

  /**
   * Sets a new <tt>DigitalIn</tt> instance at the
   * given reference.
   *
   * @param ref the reference as <tt>int</tt>.
   * @param di the new <tt>DigitalIn</tt> instance to be
   *        set.
   *
   * @throws IllegalAddressException if the reference is invalid.
   */
  public void setDigitalIn(int ref, DigitalIn di)
      throws IllegalAddressException;

  /**
   * Adds a new <tt>DigitalIn</tt> instance.
   *
   * @param di the <tt>DigitalIn</tt> instance to be
   *        added.
   */
  public void addDigitalIn(DigitalIn di);

  /**
   * Removes a given <tt>DigitalIn</tt> instance.
   *
   * @param di the <tt>DigitalIn</tt> instance to be
   *        removed.
   */
  public void removeDigitalIn(DigitalIn di);

  /**
   * Sets a new <tt>InputRegister</tt> instance at the
   * given reference.
   *
   * @param ref the reference as <tt>int</tt>.
   * @param reg the new <tt>InputRegister</tt> instance to be
   *        set.
   *
   * @throws IllegalAddressException if the reference is invalid.
   */
  public void setInputRegister(int ref, InputRegister reg)
      throws IllegalAddressException;

  /**
   * Adds a new <tt>InputRegister</tt> instance.
   *
   * @param reg the <tt>InputRegister</tt> instance to be
   *        added.
   */
  public void addInputRegister(InputRegister reg);

  /**
   * Removes a given <tt>InputRegister</tt> instance.
   *
   * @param reg the <tt>InputRegister</tt> instance to be
   *        removed.
   */
  public void removeInputRegister(InputRegister reg);

  /**
   * Sets a new <tt>Register</tt> instance at the
   * given reference.
   *
   * @param ref the reference as <tt>int</tt>.
   * @param reg the new <tt>Register</tt> instance to be
   *        set.
   *
   * @throws IllegalAddressException if the reference is invalid.
   */
  public void setRegister(int ref, Register reg)
      throws IllegalAddressException;

  /**
   * Adds a new <tt>Register</tt> instance.
   *
   * @param reg the <tt>Register</tt> instance to be
   *        added.
   */
  public void addRegister(Register reg);

  /**
   * Removes a given <tt>Register</tt> instance.
   *
   * @param reg the <tt>Register</tt> instance to be
   *        removed.
   */
  public void removeRegister(Register reg);

  /**
   * Defines the set state (i.e. <b>true</b>) of
   * a digital input or output.
   */
  public static final byte DIG_TRUE = 1;

  /**
   * Defines the unset state (i.e. <b>false</b>) of
   * a digital input or output.
   */
  public static final byte DIG_FALSE = 0;

  /**
   * Defines the invalid state of
   * a digital input or output.
   */
  public static final byte DIG_INVALID = -1;

}//ProcessImageImplementation
