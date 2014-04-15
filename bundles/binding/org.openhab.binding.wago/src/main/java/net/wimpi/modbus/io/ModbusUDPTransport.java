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

package net.wimpi.modbus.io;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InterruptedIOException;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.msg.ModbusMessage;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;
import net.wimpi.modbus.net.UDPTerminal;

/**
 * Class that implements the Modbus UDP transport
 * flavor.
 *
 * @author Dieter Wimberger
 * @version 1.0 (29/04/2002)
 */
public class ModbusUDPTransport
    implements ModbusTransport {

  //instance attributes
  private UDPTerminal m_Terminal;
  private BytesOutputStream m_ByteOut;
  private BytesInputStream m_ByteIn;

  /**
   * Constructs a new <tt>ModbusTransport</tt> instance,
   * for a given <tt>UDPTerminal</tt>.
   * <p>
   * @param terminal the <tt>UDPTerminal</tt> used for message transport.
   */
  public ModbusUDPTransport(UDPTerminal terminal) {
    m_Terminal = terminal;
    m_ByteOut = new BytesOutputStream(Modbus.MAX_MESSAGE_LENGTH);
    m_ByteIn = new BytesInputStream(Modbus.MAX_MESSAGE_LENGTH);
  }//constructor


  public void close()
      throws IOException {
    //?
  }//close

  public void writeMessage(ModbusMessage msg)
      throws ModbusIOException {
    try {
      synchronized (m_ByteOut) {
        m_ByteOut.reset();
        msg.writeTo((DataOutput) m_ByteOut);
        m_Terminal.sendMessage(m_ByteOut.toByteArray());
      }
    } catch (Exception ex) {
      throw new ModbusIOException("I/O exception - failed to write.");
    }
  }//write

  public ModbusRequest readRequest()
      throws ModbusIOException {
    try {
      ModbusRequest req = null;
      synchronized (m_ByteIn) {
        m_ByteIn.reset(m_Terminal.receiveMessage());
        m_ByteIn.skip(7);
        int functionCode = m_ByteIn.readUnsignedByte();
        m_ByteIn.reset();
        req = ModbusRequest.createModbusRequest(functionCode);
        req.readFrom(m_ByteIn);
      }
      return req;
    } catch (Exception ex) {
      throw new ModbusIOException("I/O exception - failed to read.");
    }
  }//readRequest

  public ModbusResponse readResponse()
      throws ModbusIOException {

    try {
      ModbusResponse res = null;
      synchronized (m_ByteIn) {
        m_ByteIn.reset(m_Terminal.receiveMessage());
        m_ByteIn.skip(7);
        int functionCode = m_ByteIn.readUnsignedByte();
        m_ByteIn.reset();
        res = ModbusResponse.createModbusResponse(functionCode);
        res.readFrom(m_ByteIn);
      }
      return res;
    } catch (InterruptedIOException ioex) {
      throw new ModbusIOException("Socket timed out.");
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new ModbusIOException("I/O exception - failed to read.");
    }
  }//readResponse

}//class ModbusUDPTransport
