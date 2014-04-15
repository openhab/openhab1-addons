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
 * The default ProcessImageFactory.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class DefaultProcessImageFactory
    implements ProcessImageFactory {

  /**
   * Returns a new SimpleProcessImage instance.
   *
   * @return a SimpleProcessImage instance.
   */
  public ProcessImageImplementation createProcessImageImplementation() {
    return new SimpleProcessImage();
  }//createProcessImageImplementation

  /**
   * Returns a new SimpleDigitalIn instance.
   *
   * @return a SimpleDigitalIn instance.
   */
  public DigitalIn createDigitalIn() {
    return new SimpleDigitalIn();
  }//createDigitalIn

  /**
   * Returns a new DigitalIn instance with the given state.
   *
   * @param state true if set, false otherwise.
   * @return a SimpleDigitalIn instance.
   */
  public DigitalIn createDigitalIn(boolean state) {
    return new SimpleDigitalIn(state);
  }//createDigitalIn

  /**
   * Returns a new SimpleDigitalOut instance.
   *
   * @return a SimpleDigitalOut instance.
   */
  public DigitalOut createDigitalOut() {
    return new SimpleDigitalOut();
  }//createDigitalOut

  /**
   * Returns a new DigitalOut instance with the
   * given state.
   *
   * @param b true if set, false otherwise.
   * @return a SimpleDigitalOut instance.
   */
  public DigitalOut createDigitalOut(boolean b) {
    return new SimpleDigitalOut(b);
  }//createDigitalOut

  /**
   * Returns a new SimpleInputRegister instance.
   *
   * @return a SimpleInputRegister instance.
   */
  public InputRegister createInputRegister() {
    return new SimpleInputRegister();
  }//createSimpleInputRegister

  /**
   * Returns a new InputRegister instance with a
   * given value.
   *
   * @param b1 the first <tt>byte</tt>.
   * @param b2 the second <tt>byte</tt>.
   * @return an InputRegister instance.
   */
  public InputRegister createInputRegister(byte b1, byte b2) {
    return new SimpleInputRegister(b1, b2);
  }//createInputRegister

  /**
   * Creates a new SimpleRegister instance.
   *
   * @return a SimpleRegister instance.
   */
  public Register createRegister() {
    return new SimpleRegister();
  }//createRegister

  /**
   * Returns a new Register instance with a
   * given value.
   *
   * @param b1 the first <tt>byte</tt>.
   * @param b2 the second <tt>byte</tt>.
   * @return a Register instance.
   */
  public Register createRegister(byte b1, byte b2) {
    return new SimpleRegister(b1, b2);
  }//createRegister

}//class DefaultProcessImageFactory
