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
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.msg.ExceptionResponse;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;
import net.wimpi.modbus.net.UDPMasterConnection;
import net.wimpi.modbus.net.UDPTerminal;
import net.wimpi.modbus.util.AtomicCounter;
import net.wimpi.modbus.util.Mutex;

/**
 * Class implementing the <tt>ModbusTransaction</tt>
 * interface for the UDP transport mechanism.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class ModbusUDPTransaction
    implements ModbusTransaction {

  //class attributes
  private static AtomicCounter c_TransactionID =
      new AtomicCounter(Modbus.DEFAULT_TRANSACTION_ID);


  //instance attributes and associations
  private UDPTerminal m_Terminal;
  private ModbusTransport m_IO;
  private ModbusRequest m_Request;
  private ModbusResponse m_Response;
  private boolean m_ValidityCheck =
      Modbus.DEFAULT_VALIDITYCHECK;
  private int m_Retries = Modbus.DEFAULT_RETRIES;
  private int m_RetryCounter = 0;

  private Mutex m_TransactionLock = new Mutex();

  /**
   * Constructs a new <tt>ModbusUDPTransaction</tt>
   * instance.
   */
  public ModbusUDPTransaction() {
  }//constructor

  /**
   * Constructs a new <tt>ModbusUDPTransaction</tt>
   * instance with a given <tt>ModbusRequest</tt> to
   * be send when the transaction is executed.
   * <p/>
   *
   * @param request a <tt>ModbusRequest</tt> instance.
   */
  public ModbusUDPTransaction(ModbusRequest request) {
    setRequest(request);
  }//constructor

  /**
   * Constructs a new <tt>ModbusUDPTransaction</tt>
   * instance with a given <tt>UDPTerminal</tt> to
   * be used for transactions.
   * <p/>
   *
   * @param terminal a <tt>UDPTerminal</tt> instance.
   */
  public ModbusUDPTransaction(UDPTerminal terminal) {
    setTerminal(terminal);
  }//constructor

  /**
   * Constructs a new <tt>ModbusUDPTransaction</tt>
   * instance with a given <tt>ModbusUDPConnection</tt>
   * to be used for transactions.
   * <p/>
   *
   * @param con a <tt>ModbusUDPConnection</tt> instance.
   */
  public ModbusUDPTransaction(UDPMasterConnection con) {
    setTerminal(con.getTerminal());
  }//constructor

  /**
   * Sets the terminal on which this <tt>ModbusTransaction</tt>
   * should be executed.<p>
   *
   * @param terminal a <tt>UDPSlaveTerminal</tt>.
   */
  public void setTerminal(UDPTerminal terminal) {
    m_Terminal = terminal;
    if (terminal.isActive()) {
      m_IO = terminal.getModbusTransport();
    }
  }//setConnection

  public void setRequest(ModbusRequest req) {
    m_Request = req;
    //m_Response = req.getResponse();
  }//setRequest

  public ModbusRequest getRequest() {
    return m_Request;
  }//getRequest

  public ModbusResponse getResponse() {
    return m_Response;
  }//getResponse

  public int getTransactionID() {
    return c_TransactionID.get();
  }//getTransactionID

  public void setCheckingValidity(boolean b) {
    m_ValidityCheck = b;
  }//setCheckingValidity

  public boolean isCheckingValidity() {
    return m_ValidityCheck;
  }//isCheckingValidity


  public int getRetries() {
    return m_Retries;
  }//getRetries

  public void setRetries(int num) {
    m_Retries = num;
  }//setRetries

  public void execute() throws ModbusIOException,
      ModbusSlaveException,
      ModbusException {

    //1. assert executeability
    assertExecutable();

    try {
      //2. Lock transaction
      /**
       * Note: The way this explicit synchronization is implemented at the moment,
       * there is no ordering of pending threads. The Mutex will simply call notify()
       * and the JVM will handle the rest.
       */
      m_TransactionLock.acquire();

      //3. open the connection if not connected
      if (!m_Terminal.isActive()) {
        try {
          m_Terminal.activate();
          m_IO = m_Terminal.getModbusTransport();
        } catch (Exception ex) {
          throw new ModbusIOException("Activation failed.");

        }
      }

      //3. Retry transaction m_Retries times, in case of
      //I/O Exception problems.
      m_RetryCounter = 0;

      while (m_RetryCounter <= m_Retries) {
        try {
          //toggle the id
          m_Request.setTransactionID(c_TransactionID.increment());
          //3. write request, and read response,
          //   while holding the lock on the IO object
          synchronized (m_IO) {
            //write request message
            m_IO.writeMessage(m_Request);
            //read response message
            m_Response = m_IO.readResponse();
            break;
          }
        } catch (ModbusIOException ex) {
          m_RetryCounter++;
          continue;
        }
      }

      //4. deal with "application level" exceptions
      if (m_Response instanceof ExceptionResponse) {
        throw new ModbusSlaveException(
            ((ExceptionResponse) m_Response).getExceptionCode()
        );
      }

      if (isCheckingValidity()) {
        checkValidity();
      }
    } catch (InterruptedException ex) {
      throw new ModbusIOException("Thread acquiring lock was interrupted.");
    } finally {
      m_TransactionLock.release();
    }

  }//execute

  /**
   * Asserts if this <tt>ModbusTCPTransaction</tt> is
   * executable.
   *
   * @throws ModbusException if this transaction cannot be
   *                         asserted as executable.
   */
  private void assertExecutable()
      throws ModbusException {
    if (m_Request == null ||
        m_Terminal == null) {
      throw new ModbusException(
          "Assertion failed, transaction not executable"
      );
    }
  }//assertExecuteable

  /**
   * Checks the validity of the transaction, by
   * checking if the values of the response correspond
   * to the values of the request.
   * Use an override to provide some checks, this method will only return.
   *
   * @throws ModbusException if this transaction has not been valid.
   */
  protected void checkValidity() throws ModbusException {
  }//checkValidity

}//class ModbusUDPTransaction
