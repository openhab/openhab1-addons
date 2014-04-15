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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.procimg.DigitalOut;
import net.wimpi.modbus.procimg.IllegalAddressException;
import net.wimpi.modbus.procimg.ProcessImage;

/**
 * Class implementing a <tt>WriteCoilRequest</tt>.
 * The implementation directly correlates with the class 0
 * function <i>write coil (FC 5)</i>. It
 * encapsulates the corresponding request message.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public final class WriteCoilRequest
    extends ModbusRequest {

  //instance attributes
  private int m_Reference;
  private boolean m_Coil;

  /**
   * Constructs a new <tt>WriteCoilRequest</tt>
   * instance.
   */
  public WriteCoilRequest() {
    super();
    setFunctionCode(Modbus.WRITE_COIL);
    //4 bytes (unit id and function code is excluded)
    setDataLength(4);
  }//constructor

  /**
   * Constructs a new <tt>WriteCoilRequest</tt>
   * instance with a given reference and state to
   * be written.
   * <p>
   * @param ref the reference number of the register
   *        to read from.
   * @param b true if the coil should be set of
   *        false if it should be unset.
   */
  public WriteCoilRequest(int ref, boolean b) {
    super();
    setFunctionCode(Modbus.WRITE_COIL);
    //4 bytes (unit id and function code is excluded)
    setDataLength(4);
    setReference(ref);
    setCoil(b);
  }//constructor
  
  public ModbusResponse createResponse() {
    WriteCoilResponse response = null;
    DigitalOut dout = null;

    //1. get process image
    ProcessImage procimg = ModbusCoupler.getReference().getProcessImage();
    //2. get coil
    try {
      dout = procimg.getDigitalOut(this.getReference());
      //3. set coil
      dout.set(this.getCoil());
      //if(Modbus.debug) System.out.println("set coil ref="+this.getReference()+" state=" + this.getCoil());
    } catch (IllegalAddressException iaex) {
      return createExceptionResponse(Modbus.ILLEGAL_ADDRESS_EXCEPTION);
    }
    response = new WriteCoilResponse(this.getReference(), dout.isSet());
    //transfer header data
    if (!isHeadless()) {
      response.setTransactionID(this.getTransactionID());
      response.setProtocolID(this.getProtocolID());
    } else {
      response.setHeadless();
    }
    response.setUnitID(this.getUnitID());
    response.setFunctionCode(this.getFunctionCode());
    return response;
  }//createResponse

  /**
   * Sets the reference of the register of the coil
   * that should be written to with this
   * <tt>ReadCoilsRequest</tt>.
   * <p>
   * @param ref the reference of the coil's register.
   */
  public void setReference(int ref) {
    m_Reference = ref;
    //setChanged(true);
  }//setReference

  /**
   * Returns the reference of the register of the coil
   * that should be written to with this
   * <tt>ReadCoilsRequest</tt>.
   * <p>
   * @return the reference of the coil's register.
   */
  public int getReference() {
    return m_Reference;
  }//getReference


  /**
   * Sets the state that should be written
   * with this <tt>WriteCoilRequest</tt>.
   * <p>
   * @param b true if the coil should be set of
   *        false if it should be unset.
   */
  public void setCoil(boolean b) {
    m_Coil = b;
    //setChanged(true);
  }//setCoil

  /**
   * Returns the state that should be written
   * with this <tt>WriteCoilRequest</tt>.
   * <p>
   * @return true if the coil should be set of
   *        false if it should be unset.
   */
  public boolean getCoil() {
    return m_Coil;
  }//getCoil


  public void writeData(DataOutput dout)
      throws IOException {
    dout.writeShort(m_Reference);
    if (m_Coil) {
      dout.write(Modbus.COIL_ON_BYTES, 0, 2);
    } else {
      dout.write(Modbus.COIL_OFF_BYTES, 0, 2);
    }
  }

  public void readData(DataInput din)
      throws IOException {
    m_Reference = din.readUnsignedShort();
    if (din.readByte() == Modbus.COIL_ON) {
      m_Coil = true;
    } else {
      m_Coil = false;
    }
    //skip last byte
    din.readByte();
  }//readData

}//class WriteCoilRequest
