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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.msg.ExceptionResponse;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.util.AtomicCounter;
import net.wimpi.modbus.util.Mutex;

/**
 * Class implementing the <tt>ModbusTransaction</tt>
 * interface.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class ModbusSerialTransaction implements ModbusTransaction {

    private static final Logger logger = LoggerFactory.getLogger(ModbusSerialTransaction.class);
    // class attributes
    private static AtomicCounter c_TransactionID = new AtomicCounter(Modbus.DEFAULT_TRANSACTION_ID);

    // instance attributes and associations
    private ModbusTransport m_IO;
    private ModbusRequest m_Request;
    private ModbusResponse m_Response;
    private boolean m_ValidityCheck = Modbus.DEFAULT_VALIDITYCHECK;
    private int m_Retries = Modbus.DEFAULT_RETRIES;
    private long m_RetryDelayMillis;
    private int m_TransDelayMS = Modbus.DEFAULT_TRANSMIT_DELAY;
    private SerialConnection m_SerialCon;

    private Mutex m_TransactionLock = new Mutex();

    /**
     * Constructs a new <tt>ModbusSerialTransaction</tt>
     * instance.
     */
    public ModbusSerialTransaction() {
    }// constructor

    /**
     * Constructs a new <tt>ModbusSerialTransaction</tt>
     * instance with a given <tt>ModbusRequest</tt> to
     * be send when the transaction is executed.
     * <p/>
     *
     * @param request a <tt>ModbusRequest</tt> instance.
     */
    public ModbusSerialTransaction(ModbusRequest request) {
        setRequest(request);
    }// constructor

    /**
     * Constructs a new <tt>ModbusSerialTransaction</tt>
     * instance with a given <tt>ModbusRequest</tt> to
     * be send when the transaction is executed.
     * <p/>
     *
     * @param con a <tt>TCPMasterConnection</tt> instance.
     */
    public ModbusSerialTransaction(SerialConnection con) {
        setSerialConnection(con);
    }// constructor

    /**
     * Sets the port on which this <tt>ModbusTransaction</tt>
     * should be executed.
     * <p>
     * <p/>
     *
     * @param con a <tt>SerialConnection</tt>.
     */
    public void setSerialConnection(SerialConnection con) {
        m_SerialCon = con;
        m_IO = con.getModbusTransport();
    }// setConnection

    @Override
    public int getTransactionID() {
        return c_TransactionID.get();
    }// getTransactionID

    @Override
    public void setRequest(ModbusRequest req) {
        m_Request = req;
        // m_Response = req.getResponse();
    }// setRequest

    @Override
    public ModbusRequest getRequest() {
        return m_Request;
    }// getRequest

    @Override
    public ModbusResponse getResponse() {
        return m_Response;
    }// getResponse

    @Override
    public void setCheckingValidity(boolean b) {
        m_ValidityCheck = b;
    }// setCheckingValidity

    @Override
    public boolean isCheckingValidity() {
        return m_ValidityCheck;
    }// isCheckingValidity

    @Override
    public int getRetries() {
        return m_Retries;
    }// getRetries

    @Override
    public void setRetries(int num) {
        m_Retries = num;
    }// setRetries

    /**
     * Get the TransDelayMS value.
     *
     * @return the TransDelayMS value.
     */
    public int getTransDelayMS() {
        return m_TransDelayMS;
    }

    /**
     * Set the TransDelayMS value.
     *
     * @param newTransDelayMS The new TransDelayMS value.
     */
    public void setTransDelayMS(int newTransDelayMS) {
        this.m_TransDelayMS = newTransDelayMS;
    }

    @Override
    public void execute() throws ModbusIOException, ModbusSlaveException, ModbusException {
        // 1. assert executeability
        assertExecutable();

        try {
            // 2. Lock transaction
            /**
             * Note: The way this explicit synchronization is implemented at the moment,
             * there is no ordering of pending threads. The Mutex will simply call notify()
             * and the JVM will handle the rest.
             */
            m_TransactionLock.acquire();

            // 3. write request, and read response,
            // while holding the lock on the IO object
            synchronized (m_IO) {
                int tries = 0;

                do {
                    m_Request.setTransactionID(c_TransactionID.increment());
                    try {
                        if (m_TransDelayMS > 0) {
                            try {
                                Thread.sleep(m_TransDelayMS);
                            } catch (InterruptedException ex) {
                                logger.error("InterruptedException: {}", ex.getMessage());
                            }
                        }

                        // write request message
                        m_IO.writeMessage(m_Request);
                        // read response message
                        m_Response = m_IO.readResponse();
                        break;
                    } catch (ModbusIOException e) {
                        tries++;
                        logger.error(
                                "execute try {}/{} error: {}. Request: {} (unit id {} & transaction {}). Serial parameters: {}",
                                tries, m_Retries, e.getMessage(), m_Request, m_Request.getUnitID(),
                                m_Request.getTransactionID(), m_SerialCon.getParameters());
                        if (tries >= m_Retries) {
                            logger.error(
                                    "execute reached max tries {}, throwing last error: {}. Request: {} (unit id {} & transaction {}). Serial parameters: {}",
                                    m_Retries, e.getMessage(), m_Request, m_Request.getUnitID(),
                                    m_Request.getTransactionID(), m_SerialCon.getParameters());
                            throw e;
                        }
                        Thread.sleep(m_RetryDelayMillis);
                    }
                } while (true);
                if (tries > 0) {
                    logger.info(
                            "execute eventually succeeded with {} re-tries. Request: {} (unit id {} & transaction id {}). Serial parameters: {}",
                            tries, m_Request, m_Request.getUnitID(), m_Request.getTransactionID(),
                            m_SerialCon.getParameters());
                }
            }

            // 4. deal with exceptions
            if (m_Response instanceof ExceptionResponse) {
                throw new ModbusSlaveException(((ExceptionResponse) m_Response).getExceptionCode());
            }

            if (isCheckingValidity()) {
                checkValidity();
            }
        } catch (InterruptedException ex) {
            throw new ModbusIOException("Thread acquiring lock was interrupted.");
        } finally {
            m_TransactionLock.release();
        }
    }// execute

    /**
     * Asserts if this <tt>ModbusTCPTransaction</tt> is
     * executable.
     *
     * @throws ModbusException if the transaction cannot be asserted.
     */
    private void assertExecutable() throws ModbusException {
        if (m_Request == null || m_SerialCon == null) {
            throw new ModbusException("Assertion failed, transaction not executable");
        }
    }// assertExecuteable

    /**
     * Checks the validity of the transaction, by
     * checking if the values of the response correspond
     * to the values of the request.
     *
     * @throws ModbusException if the transaction is not valid.
     */
    protected void checkValidity() throws ModbusException {
    }// checkValidity

    @Override
    public long getRetryDelayMillis() {
        return m_RetryDelayMillis;
    }

    @Override
    public void setRetryDelayMillis(long retryDelayMillis) {
        this.m_RetryDelayMillis = retryDelayMillis;
    }

}// class ModbusSerialTransaction
