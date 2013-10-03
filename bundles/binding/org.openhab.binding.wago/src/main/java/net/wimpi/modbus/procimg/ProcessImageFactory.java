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
 * Interface defining the factory methods for
 * the process image and it's elements.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public interface ProcessImageFactory {

  /**
   * Returns a new ProcessImageImplementation instance.
   *
   * @return a ProcessImageImplementation instance.
   */
  public ProcessImageImplementation createProcessImageImplementation();

  /**
   * Returns a new DigitalIn instance.
   *
   * @return a DigitalIn instance.
   */
  public DigitalIn createDigitalIn();

  /**
   * Returns a new DigitalIn instance with the given state.
   *
   * @param state true if set, false otherwise.
   * @return a DigitalIn instance.
   */
  public DigitalIn createDigitalIn(boolean state);

  /**
   * Returns a new DigitalOut instance.
   *
   * @return a DigitalOut instance.
   */
  public DigitalOut createDigitalOut();

  /**
   * Returns a new DigitalOut instance with the
   * given state.
   *
   * @param b true if set, false otherwise.
   * @return a DigitalOut instance.
   */
  public DigitalOut createDigitalOut(boolean b);

  /**
   * Returns a new InputRegister instance.
   *
   * @return an InputRegister instance.
   */
  public InputRegister createInputRegister();

  /**
   * Returns a new InputRegister instance with a
   * given value.
   *
   * @param b1 the first <tt>byte</tt>.
   * @param b2 the second <tt>byte</tt>.
   * @return an InputRegister instance.
   */
  public InputRegister createInputRegister(byte b1, byte b2);

  /**
   * Creates a new Register instance.
   *
   * @return a Register instance.
   */
  public Register createRegister();

  /**
   * Returns a new Register instance with a
   * given value.
   *
   * @param b1 the first <tt>byte</tt>.
   * @param b2 the second <tt>byte</tt>.
   * @return a Register instance.
   */
   public Register createRegister(byte b1, byte b2);

}//interface ProcessImageFactory
