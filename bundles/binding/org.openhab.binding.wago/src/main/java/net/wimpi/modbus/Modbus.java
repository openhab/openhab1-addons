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

package net.wimpi.modbus;

/**
 * Interface defining all constants related to the
 * Modbus protocol.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public interface Modbus {


  /**
   * JVM flag for debug mode. Can be set passing the system property
   * net.wimpi.modbus.debug=false|true (-D flag to the jvm).
   */
  public static final boolean debug = "true".equals(System.getProperty("net.wimpi.modbus.debug"));
  
  /**
   * Defines the class 0 function code
   * for <tt>read multiple registers</tt>.
   */
  public static final int READ_MULTIPLE_REGISTERS = 3;

  /**
   * Defines the class 0 function code
   * for <tt>write multiple registers</tt>.
   */
  public static final int WRITE_MULTIPLE_REGISTERS = 16;

  /**
   * Defines the class 1 function code
   * for <tt>read coils</tt>.
   */
  public static final int READ_COILS = 1;

  /**
   * Defines a class 1 function code
   * for <tt>read input discretes</tt>.
   */
  public static final int READ_INPUT_DISCRETES = 2;

  /**
   * Defines a class 1 function code
   * for <tt>read input registers</tt>.
   */
  public static final int READ_INPUT_REGISTERS = 4;

  /**
   * Defines a class 1 function code
   * for <tt>write coil</tt>.
   */
  public static final int WRITE_COIL = 5;

  /**
   * Defines a standard function code
   * for <tt>write multiple coils</tt>.
   */
  public static final int WRITE_MULTIPLE_COILS = 15;

  /**
   * Defines a class 1 function code
   * for <tt>write single register</tt>.
   */
  public static final int WRITE_SINGLE_REGISTER = 6;

  /**
   * Defines the byte representation of the coil state <b>on</b>.
   */
  public static final int COIL_ON = (byte) 255;

  /**
   * Defines the byte representation of the coil state <b>pos</b>.
   */
  public static final int COIL_OFF = 0;

  /**
   * Defines the word representation of the coil state <b>on</b>.
   */
  public static final byte[] COIL_ON_BYTES = {(byte) COIL_ON, (byte) COIL_OFF};

  /**
   * Defines the word representation of the coil state <b>pos</b>.
   */
  public static final byte[] COIL_OFF_BYTES = {(byte) COIL_OFF, (byte) COIL_OFF};

  /**
   * Defines the maximum number of bits in multiple read/write
   * of input discretes or coils (<b>2000</b>).
   */
  public static final int MAX_BITS = 2000;

  /**
   * Defines the Modbus slave exception offset that is added to the
   * function code, to flag an exception.
   */
  public static final int EXCEPTION_OFFSET = 128;			//the last valid function code is 127

  /**
   * Defines the Modbus slave exception type <tt>illegal function</tt>.
   * This exception code is returned if the slave:
   * <ul>
   *   <li>does not implement the function code <b>or</b></li>
   *   <li>is not in a state that allows it to process the function</li>
   * </ul>
   */
  public static final int ILLEGAL_FUNCTION_EXCEPTION = 1;

  /**
   * Defines the Modbus slave exception type <tt>illegal data address</tt>.
   * This exception code is returned if the reference:
   * <ul>
   *   <li>does not exist on the slave <b>or</b></li>
   *   <li>the combination of reference and length exceeds the bounds
   *       of the existing registers.
   *   </li>
   * </ul>
   */
  public static final int ILLEGAL_ADDRESS_EXCEPTION = 2;

  /**
   * Defines the Modbus slave exception type <tt>illegal data value</tt>.
   * This exception code indicates a fault in the structure of the data values
   * of a complex request, such as an incorrect implied length.<br>
   * <b>This code does not indicate a problem with application specific validity
   * of the value.</b>
   */
  public static final int ILLEGAL_VALUE_EXCEPTION = 3;


  /**
   * Defines the default port number of Modbus
   * (=<tt>502</tt>).
   */
  public static final int DEFAULT_PORT = 502;

  /**
   * Defines the maximum message length in bytes
   * (=<tt>256</tt>).
   */
  public static final int MAX_MESSAGE_LENGTH = 256;

  /**
   * Defines the default transaction identifier (=<tt>0</tt>).
   */
  public static final int DEFAULT_TRANSACTION_ID = 0;

  /**
   * Defines the default protocol identifier (=<tt>0</tt>).
   */
  public static final int DEFAULT_PROTOCOL_ID = 0;

  /**
   * Defines the default unit identifier (=<tt>0</tt>).
   */
  public static final int DEFAULT_UNIT_ID = 0;

  /**
   * Defines the default setting for validity checking
   * in transactions (=<tt>true</tt>).
   */
  public static final boolean DEFAULT_VALIDITYCHECK = true;

  /**
   * Defines the default setting for I/O operation timeouts
   * in milliseconds (=<tt>3000</tt>).
   */
  public static final int DEFAULT_TIMEOUT = 3000;

  /**
   * Defines the default reconnecting setting for
   * transactions (=<tt>false</tt>).
   */
  public static final boolean DEFAULT_RECONNECTING = false;

  /**
   * Defines the default amount of retires for opening
   * a connection (=<tt>3</tt>).
   */
  public static final int DEFAULT_RETRIES = 3;

  /**
   * Defines the default number of msec to delay before transmission
   * (=<tt>50</tt>).
   */
  public static final int DEFAULT_TRANSMIT_DELAY = 0;

  /**
   * Defines the maximum value of the transaction identifier.
   */
  public static final int MAX_TRANSACTION_ID = (Short.MAX_VALUE * 2) - 1;


  /**
   * Defines the serial encoding "ASCII".
   */
  public static final String SERIAL_ENCODING_ASCII = "ascii";

  /**
   * Defines the serial encoding "RTU".
   */
  public static final String SERIAL_ENCODING_RTU = "rtu";

  /**
   * Defines the serial encoding "BIN".
   */
  public static final String SERIAL_ENCODING_BIN = "bin";

  /**
   * Defines the default serial encoding (ASCII).
   */
  public static final String DEFAULT_SERIAL_ENCODING = SERIAL_ENCODING_ASCII;

}//class Modbus

