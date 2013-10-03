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

package net.wimpi.modbus.msg;

import net.wimpi.modbus.Modbus;

/**
 * Abstract class implementing a <tt>ModbusRequest</tt>.
 * This class provides specialised implementations with
 * the functionality they have in common.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public abstract class ModbusRequest
    extends ModbusMessageImpl {

  /**
   * Returns the <tt>ModbusResponse</tt> that
   * correlates with this <tt>ModbusRequest</tt>.
   * <p>
   * @return the corresponding <tt>ModbusResponse</tt>.
   *
  public abstract ModbusResponse getResponse();
  */

  /**
   * Returns the <tt>ModbusResponse</tt> that
   * represents the answer to this <tt>ModbusRequest</tt>.
   * <p>
   * The implementation should take care about assembling
   * the reply to this <tt>ModbusRequest</tt>.
   * <p>
   * @return the corresponding <tt>ModbusResponse</tt>.
   */
  public abstract ModbusResponse createResponse();

  /**
   * Factory method for creating exception responses with the
   * given exception code.
   *
   * @param EXCEPTION_CODE the code of the exception.
   * @return a ModbusResponse instance representing the exception
   *         response.
   */
  public ModbusResponse createExceptionResponse(int EXCEPTION_CODE) {
    ExceptionResponse response =
        new ExceptionResponse(this.getFunctionCode(), EXCEPTION_CODE);
    if (!isHeadless()) {
      response.setTransactionID(this.getTransactionID());
      response.setProtocolID(this.getProtocolID());
      response.setUnitID(this.getUnitID());
    } else {
      response.setHeadless();
    }
    return response;
  }//createExceptionResponse

  /**
   * Factory method creating the required specialized <tt>ModbusRequest</tt>
   * instance.
   *
   * @param functionCode the function code of the request as <tt>int</tt>.
   * @return a ModbusRequest instance specific for the given function type.
   */
  public static ModbusRequest createModbusRequest(int functionCode) {
    ModbusRequest request = null;

    switch (functionCode) {
      case Modbus.READ_MULTIPLE_REGISTERS:
        request = new ReadMultipleRegistersRequest();
        break;
      case Modbus.READ_INPUT_DISCRETES:
        request = new ReadInputDiscretesRequest();
        break;
      case Modbus.READ_INPUT_REGISTERS:
        request = new ReadInputRegistersRequest();
        break;
      case Modbus.READ_COILS:
        request = new ReadCoilsRequest();
        break;
      case Modbus.WRITE_MULTIPLE_REGISTERS:
        request = new WriteMultipleRegistersRequest();
        break;
      case Modbus.WRITE_SINGLE_REGISTER:
        request = new WriteSingleRegisterRequest();
        break;
      case Modbus.WRITE_COIL:
        request = new WriteCoilRequest();
        break;
      case Modbus.WRITE_MULTIPLE_COILS:
        request = new WriteMultipleCoilsRequest();
        break;
      default:
        request = new IllegalFunctionRequest(functionCode);
        break;
    }
    return request;
  }//createModbusRequest

}//class ModbusRequest
