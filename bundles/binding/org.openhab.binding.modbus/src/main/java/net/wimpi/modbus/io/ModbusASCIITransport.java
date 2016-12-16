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

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.msg.ModbusMessage;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;
import net.wimpi.modbus.util.ModbusUtil;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Class that implements the Modbus/ASCII transport
 * flavor.
 *
 * @author Dieter Wimberger
 * @author John Charlton
 * @version @version@ (@date@)
 */
public class ModbusASCIITransport
  extends ModbusSerialTransport {

  private DataInputStream m_InputStream;     //used to read from
  private ASCIIOutputStream m_OutputStream;   //used to write to

  private byte[] m_InBuffer;
  private BytesInputStream m_ByteIn;         //to read message from
  private BytesOutputStream m_ByteInOut;     //to buffer message to
  private BytesOutputStream m_ByteOut;      //write frames

  /**
   * Constructs a new <tt>MobusASCIITransport</tt> instance.
   */
  public ModbusASCIITransport() {
  }//constructor
  
  public void close() throws IOException {
    m_InputStream.close();
    m_OutputStream.close();
  }//close

  public void writeMessage(ModbusMessage msg)
      throws ModbusIOException {

    try {
      synchronized (m_ByteOut) {
        //write message to byte out
        msg.setHeadless();
        msg.writeTo(m_ByteOut);
        byte[] buf = m_ByteOut.getBuffer();
        int len = m_ByteOut.size();

        //write message
        m_OutputStream.write(FRAME_START);               //FRAMESTART
        m_OutputStream.write(buf, 0, len);                 //PDU
        if(Modbus.debug)
          System.out.println("Writing: " + ModbusUtil.toHex(buf, 0, len));
        m_OutputStream.write(ModbusUtil.calculateLRC(buf, 0, len)); //LRC
        m_OutputStream.write(FRAME_END);                 //FRAMEEND
        m_OutputStream.flush();
        m_ByteOut.reset();
        // clears out the echoed message
        // for RS485
        if (m_Echo) {
          // read back the echoed message
          readEcho(len + 3);
        }
      }
    } catch (Exception ex) {
      throw new ModbusIOException("I/O failed to write" + ex);
    }
  }//writeMessage

  public ModbusRequest readRequest()
      throws ModbusIOException {

    boolean done = false;
    ModbusRequest request = null;

    int in = -1;

    try {
      do {
        //1. Skip to FRAME_START
        while ((in = m_InputStream.read()) != FRAME_START) ;
        //2. Read to FRAME_END
        synchronized (m_InBuffer) {
          m_ByteInOut.reset();
          while ((in = m_InputStream.read()) != FRAME_END) {
            if (in == -1) {
              throw new IOException("readRequest: I/O exception - Serial port timeout.");
            }
            m_ByteInOut.writeByte(in);
          }
          //check LRC
          if (((int)m_InBuffer[m_ByteInOut.size()-1] & 0xff)  !=
              ModbusUtil.calculateLRC(m_InBuffer, 0, m_ByteInOut.size()-1)) {
            continue;
          }
          ;
          m_ByteIn.reset(m_InBuffer, m_ByteInOut.size());
          in = m_ByteIn.readUnsignedByte();
          //check message with this slave unit identifier
          if (in != ModbusCoupler.getReference().getUnitID()) {
            continue;
          }
          in = m_ByteIn.readUnsignedByte();
          //create request
          request = ModbusRequest.createModbusRequest(in);
          request.setHeadless();
          //read message
          m_ByteIn.reset(m_InBuffer, m_ByteInOut.size());
          request.readFrom(m_ByteIn);
        }
        done = true;
      } while (!done);
      return request;
    } catch (Exception ex) {
      if(Modbus.debug) System.out.println(ex.getMessage());
      throw new ModbusIOException("readRequest: I/O exception - failed to read.");
    }

  }//readRequest

  public ModbusResponse readResponse()
      throws ModbusIOException {

    boolean done = false;
    ModbusResponse response = null;
    int in = -1;

    try {
      do {
        //1. Skip to FRAME_START
        while ((in = m_InputStream.read()) != FRAME_START) {
          if (in == -1) {
            throw new IOException("readResponse: I/O exception - Serial port timeout.");
          }
        }
        //2. Read to FRAME_END
        synchronized (m_InBuffer) {
          m_ByteInOut.reset();
          while ((in = m_InputStream.read()) != FRAME_END) {
            if (in == -1) {
              throw new IOException("I/O exception - Serial port timeout.");
            }
            m_ByteInOut.writeByte(in);
          }
          int len = m_ByteInOut.size();
          if (Modbus.debug)
            System.out.println("Received: " +
                               ModbusUtil.toHex(m_InBuffer, 0, len));
          //check LRC
          if (((int)m_InBuffer[len-1] & 0xff) != ModbusUtil.calculateLRC(m_InBuffer, 0, len - 1)) {
            if (Modbus.debug)
             System.out.println("LRC is wrong: received=" +
                 ((int)m_InBuffer[len-1] & 0xff) +
                 " calculated=" + ModbusUtil.calculateLRC(m_InBuffer, 0, len - 1));
            continue;
          }

          m_ByteIn.reset(m_InBuffer, m_ByteInOut.size());
          in = m_ByteIn.readUnsignedByte();
          // JDC: To check slave unit identifier in a response we need to know
          // the slave id in the request.  This is not tracked since slaves
          // only respond when a master request is made and there is only one
          // master.  We are the only master, so we can assume that this
          // response message is from the slave responding to the last request.
//           if (in != ModbusCoupler.getReference().getUnitID()) {
//             continue;
//           }
          in = m_ByteIn.readUnsignedByte();
          //create request
          response = ModbusResponse.createModbusResponse(in);
          response.setHeadless();
          //read message
          m_ByteIn.reset(m_InBuffer, m_ByteInOut.size());
          response.readFrom(m_ByteIn);
        }
        done = true;
      } while (!done);
      return response;
    } catch (Exception ex) {
      if(Modbus.debug) System.out.println(ex.getMessage());
      throw new ModbusIOException("readResponse I/O exception - failed to read.");
    }
  }//readResponse

  /**
   * Prepares the input and output streams of this
   * <tt>ModbusASCIITransport</tt> instance.
   * The raw input stream will be wrapped into a
   * filtered <tt>DataInputStream</tt>.
   *
   * @param in the input stream to be used for reading.
   * @param out the output stream to be used for writing.
   * @throws IOException if an I\O related error occurs.
   */
  public void prepareStreams(InputStream in, OutputStream out) throws IOException {
    m_InputStream = new DataInputStream(new ASCIIInputStream(in));
    m_OutputStream = new ASCIIOutputStream(out);
    m_ByteOut = new BytesOutputStream(Modbus.MAX_MESSAGE_LENGTH);
    m_InBuffer = new byte[Modbus.MAX_MESSAGE_LENGTH];
    m_ByteIn = new BytesInputStream(m_InBuffer);
    m_ByteInOut = new BytesOutputStream(m_InBuffer);
  }//prepareStreams


  /**
   * Defines a virtual number for the FRAME START token (COLON).
   */
  public static final int FRAME_START = 1000;

  /**
   * Defines a virtual number for the FRAME_END token (CR LF).
   */
  public static final int FRAME_END = 2000;

}//class ModbusASCIITransport
