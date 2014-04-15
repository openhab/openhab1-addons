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
 * Interface defining a process image
 * in an object oriented manner.
 * <p>
 * The process image is understood as a shared
 * memory area used form communication between
 * slave and master or device side.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public interface ProcessImage {

  /**
   * Returns a range of <tt>DigitalOut</tt> instances.
   * <p>
   *
   * @param offset the start offset.
   * @param count the amount of <tt>DigitalOut</tt> from the offset.
   *
   * @return an array of <tt>DigitalOut</tt> instances.
   *
   * @throws IllegalAddressException if the range from offset
   *         to offset+count is non existant.
   */
  public DigitalOut[] getDigitalOutRange(int offset, int count)
      throws IllegalAddressException;

  /**
   * Returns the <tt>DigitalOut</tt> instance at the given
   * reference.
   * <p>
   *
   * @param ref the reference.
   *
   * @return the <tt>DigitalOut</tt> instance at the given address.
   *
   * @throws IllegalAddressException if the reference is invalid.
   */
  public DigitalOut getDigitalOut(int ref)
      throws IllegalAddressException;

  /**
   * Returns the number of <tt>DigitalOut</tt> instances
   * in this <tt>ProcessImage</tt>.
   *
   * @return the number of digital outs as <tt>int</tt>.
   */
  public int getDigitalOutCount();

  /**
   * Returns a range of <tt>DigitalIn</tt> instances.
   * <p>
   *
   * @param offset the start offset.
   * @param count the amount of <tt>DigitalIn</tt> from the offset.
   *
   * @return an array of <tt>DigitalIn</tt> instances.
   *
   * @throws IllegalAddressException if the range from offset
   *         to offset+count is non existant.
   */
  public DigitalIn[] getDigitalInRange(int offset, int count)
      throws IllegalAddressException;

  /**
   * Returns the <tt>DigitalIn</tt> instance at the given
   * reference.
   * <p>
   *
   * @param ref the reference.
   *
   * @return the <tt>DigitalIn</tt> instance at the given address.
   *
   * @throws IllegalAddressException if the reference is invalid.
   */
  public DigitalIn getDigitalIn(int ref)
      throws IllegalAddressException;

  /**
   * Returns the number of <tt>DigitalIn</tt> instances
   * in this <tt>ProcessImage</tt>.
   *
   * @return the number of digital ins as <tt>int</tt>.
   */
  public int getDigitalInCount();

  /**
   * Returns a range of <tt>InputRegister</tt> instances.
   * <p>
   *
   * @param offset the start offset.
   * @param count the amount of <tt>InputRegister</tt>
   *        from the offset.
   *
   * @return an array of <tt>InputRegister</tt> instances.
   *
   * @throws IllegalAddressException if the range from offset
   *         to offset+count is non existant.
   */
  public InputRegister[] getInputRegisterRange(int offset, int count)
      throws IllegalAddressException;

  /**
   * Returns the <tt>InputRegister</tt> instance at the given
   * reference.
   * <p>
   *
   * @param ref the reference.
   *
   * @return the <tt>InputRegister</tt> instance at the given address.
   *
   * @throws IllegalAddressException if the reference is invalid.
   */
  public InputRegister getInputRegister(int ref)
      throws IllegalAddressException;


  /**
   * Returns the number of <tt>InputRegister</tt> instances
   * in this <tt>ProcessImage</tt>.
   *
   * @return the number of input registers as <tt>int</tt>.
   */
  public int getInputRegisterCount();

  /**
   * Returns a range of <tt>Register</tt> instances.
   * <p>
   *
   * @param offset the start offset.
   * @param count the amount of <tt>Register</tt> from the offset.
   *
   * @return an array of <tt>Register</tt> instances.
   *
   * @throws IllegalAddressException if the range from offset
   *         to offset+count is non existant.
   */
  public Register[] getRegisterRange(int offset, int count)
      throws IllegalAddressException;

  /**
   * Returns the <tt>Register</tt> instance at the given
   * reference.
   * <p>
   *
   * @param ref the reference.
   *
   * @return the <tt>Register</tt> instance at the given address.
   *
   * @throws IllegalAddressException if the reference is invalid.
   */
  public Register getRegister(int ref)
      throws IllegalAddressException;

  /**
   * Returns the number of <tt>Register</tt> instances
   * in this <tt>ProcessImage</tt>.
   *
   * @return the number of registers as <tt>int</tt>.
   */
  public int getRegisterCount();

}//interface ProcessImage
