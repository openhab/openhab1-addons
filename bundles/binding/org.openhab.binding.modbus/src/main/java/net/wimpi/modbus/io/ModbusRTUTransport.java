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
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.msg.ModbusMessage;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;
import net.wimpi.modbus.util.ModbusUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class that implements the ModbusRTU transport
 * flavor.
 *
 * @author John Charlton
 * @author Dieter Wimberger
 *
 * @version @version@ (@date@)
 */
public class ModbusRTUTransport
    extends ModbusSerialTransport {

  private static final Logger logger = LoggerFactory.getLogger(ModbusRTUTransport.class);

  private InputStream m_InputStream;    //wrap into filter input
  private OutputStream m_OutputStream;      //wrap into filter output

  private byte[] m_InBuffer;
  private BytesInputStream m_ByteIn;         //to read message from
  private BytesOutputStream m_ByteInOut;     //to buffer message to
  private BytesOutputStream m_ByteOut;      //write frames
  private byte[] lastRequest = null;

  public void writeMessage(ModbusMessage msg) throws ModbusIOException {
    try {
      int len;
      synchronized (m_ByteOut) {
        // first clear any input from the receive buffer to prepare
        // for the reply since RTU doesn't have message delimiters
        clearInput();
        //write message to byte out
        m_ByteOut.reset();
        msg.setHeadless();
        msg.writeTo(m_ByteOut);
        len = m_ByteOut.size();
        int[] crc = ModbusUtil.calculateCRC(m_ByteOut.getBuffer(), 0, len);
        m_ByteOut.writeByte(crc[0]);
        m_ByteOut.writeByte(crc[1]);
        //write message
        len = m_ByteOut.size();
        byte buf[] = m_ByteOut.getBuffer();
        m_OutputStream.write(buf, 0, len);     //PDU + CRC
        m_OutputStream.flush();
        logger.debug("Sent: {}", ModbusUtil.toHex(buf, 0, len));
        // clears out the echoed message
        // for RS485
        if (m_Echo) {
          readEcho(len);
        }
        lastRequest = new byte[len];
        System.arraycopy(buf, 0, lastRequest, 0, len);
      }

    } catch (Exception ex) {
      throw new ModbusIOException("I/O failed to write");
    }

  }//writeMessage

  //This is required for the slave that is not supported
  public ModbusRequest readRequest() throws ModbusIOException {
    throw new RuntimeException("Operation not supported.");
  } //readRequest

  /**
   * Clear the input if characters are found in the input stream.
   *
   * @throws IOException
   */
  public void clearInput() throws IOException {
    if (m_InputStream.available() > 0) {
      int len = m_InputStream.available();
      byte buf[] = new byte[len];
      m_InputStream.read(buf, 0, len);
      logger.debug("Clear input: {}", ModbusUtil.toHex(buf, 0, len));
    }
  }//cleanInput

  public ModbusResponse readResponse()
      throws ModbusIOException {

    boolean done = false;
    ModbusResponse response = null;
    int dlength = 0;

    try {
      do {
        //1. read to function code, create request and read function specific bytes
        synchronized (m_ByteIn) {
          int uid = m_InputStream.read();
          if (uid != -1) {
            int fc = m_InputStream.read();
            m_ByteInOut.reset();
            m_ByteInOut.writeByte(uid);
            m_ByteInOut.writeByte(fc);

            //create response to acquire length of message
            response = ModbusResponse.createModbusResponse(fc);
            response.setHeadless();

            // With Modbus RTU, there is no end frame.  Either we assume
            // the message is complete as is or we must do function
            // specific processing to know the correct length.  To avoid
            // moving frame timing to the serial input functions, we set the
            // timeout and to message specific parsing to read a response.
            getResponse(fc, m_ByteInOut);
            dlength = m_ByteInOut.size() - 2; // less the crc
            logger.debug("Response: {}", ModbusUtil.toHex(m_ByteInOut.getBuffer(), 0, dlength + 2));

            m_ByteIn.reset(m_InBuffer, dlength);

            //check CRC
            int[] crc = ModbusUtil.calculateCRC(m_InBuffer, 0, dlength); //does not include CRC
            if (ModbusUtil.unsignedByteToInt(m_InBuffer[dlength]) != crc[0]
                || ModbusUtil.unsignedByteToInt(m_InBuffer[dlength + 1]) != crc[1]) {
              throw new IOException("CRC Error in received frame: " + dlength + " bytes: " + ModbusUtil.toHex(m_ByteIn.getBuffer(), 0, dlength));
            }
          } else {
            throw new IOException("Error reading response");
          }

          //read response
          m_ByteIn.reset(m_InBuffer, dlength);
          if (response != null) {
            response.readFrom(m_ByteIn);
          }
          done = true;
        }//synchronized
      } while (!done);
      return response;
    } catch (Exception ex) {
      final String errMsg = "failed to read";
      logger.error("Last request: {}", ModbusUtil.toHex(lastRequest));
      logger.error("{}: {}", errMsg, ex.getMessage());
      throw new ModbusIOException("I/O exception - " + errMsg);
    }
  }//readResponse

  /**
   * Prepares the input and output streams of this
   * <tt>ModbusRTUTransport</tt> instance.
   *
   * @param in the input stream to be read from.
   * @param out the output stream to write to.
   * @throws IOException if an I\O error occurs.
   */
  public void prepareStreams(InputStream in, OutputStream out)
      throws IOException {
    m_InputStream = in;   //new RTUInputStream(in);
    m_OutputStream = out;

    m_ByteOut = new BytesOutputStream(Modbus.MAX_MESSAGE_LENGTH);
    m_InBuffer = new byte[Modbus.MAX_MESSAGE_LENGTH];
    m_ByteIn = new BytesInputStream(m_InBuffer);
    m_ByteInOut = new BytesOutputStream(m_InBuffer);
  } //prepareStreams

  public void close() throws IOException {
    if (m_InputStream != null) {
          m_InputStream.close();
    }
    if (m_OutputStream != null) {
      m_OutputStream.close();
    }
    super.close();
  }//close

  private void getResponse(int fn, BytesOutputStream out)
    throws IOException {
    int bc = -1, bc2 = -1, bcw = -1;
    int inpBytes = 0;
    byte inpBuf[] = new byte[256];

    try {
      switch (fn) {
        case 0x01:
        case 0x02:
        case 0x03:
        case 0x04:
        case 0x0C:
        case 0x11:  // report slave ID version and run/stop state
        case 0x14:  // read log entry (60000 memory reference)
        case 0x15:  // write log entry (60000 memory reference)
        case 0x17:
          // read the byte count;
          bc = m_InputStream.read();
          out.write(bc);
          // now get the specified number of bytes and the 2 CRC bytes
          setReceiveThreshold(bc+2);
          inpBytes = m_InputStream.read(inpBuf, 0, bc+2);
          out.write(inpBuf, 0, inpBytes);
          m_CommPort.disableReceiveThreshold();
          if (inpBytes != bc+2) {
            logger.error("awaited {} bytes, but received {}", (bc+2), inpBytes);
          }
          break;
        case 0x05:
        case 0x06:
        case 0x0B:
        case 0x0F:
        case 0x10:
          // read status: only the CRC remains after address and function code
          setReceiveThreshold(6);
          inpBytes = m_InputStream.read(inpBuf, 0, 6);
          out.write(inpBuf, 0, inpBytes);
          m_CommPort.disableReceiveThreshold();
          break;
        case 0x07:
        case 0x08:
          // read status: only the CRC remains after address and function code
          setReceiveThreshold(3);
          inpBytes = m_InputStream.read(inpBuf, 0, 3);
          out.write(inpBuf, 0, inpBytes);
          m_CommPort.disableReceiveThreshold();
          break;
        case 0x16:
          // eight bytes in addition to the address and function codes
          setReceiveThreshold(8);
          inpBytes = m_InputStream.read(inpBuf, 0, 8);
          out.write(inpBuf, 0, inpBytes);
          m_CommPort.disableReceiveThreshold();
          break;
        case 0x18:
          // read the byte count word
          bc = m_InputStream.read();
          out.write(bc);
          bc2 = m_InputStream.read();
          out.write(bc2);
          bcw = ModbusUtil.makeWord(bc, bc2);
          // now get the specified number of bytes and the 2 CRC bytes
          setReceiveThreshold(bcw+2);
          inpBytes = m_InputStream.read(inpBuf, 0, bcw + 2);
          out.write(inpBuf, 0, inpBytes);
          m_CommPort.disableReceiveThreshold();
          break;
      }
    } catch (IOException e) {
      m_CommPort.disableReceiveThreshold();
      throw new IOException("getResponse serial port exception");
    }
  }//getResponse
  
} //ModbusRTUTransport
