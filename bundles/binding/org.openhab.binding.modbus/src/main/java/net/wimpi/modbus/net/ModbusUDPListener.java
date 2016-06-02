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

package net.wimpi.modbus.net;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.io.ModbusTransport;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;

/**
 * Class that implements a ModbusUDPListener.<br>
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class ModbusUDPListener {
    private static final Logger logger = LoggerFactory.getLogger(ModbusUDPListener.class);

    private UDPSlaveTerminal m_Terminal;
    private ModbusUDPHandler m_Handler;
    private Thread m_HandlerThread;
    private int m_Port = Modbus.DEFAULT_PORT;
    private boolean m_Listening;
    private InetAddress m_Interface;

    private UDPSlaveTerminalFactory m_TerminalFactory;

    /**
     * Constructs a new ModbusUDPListener instance.
     */
    public ModbusUDPListener() {
        this(null);
    }// ModbusUDPListener

    /**
     * Create a new <tt>ModbusUDPListener</tt> instance
     * listening to the given interface address.
     *
     * @param ifc an <tt>InetAddress</tt> instance.
     */
    public ModbusUDPListener(InetAddress ifc) {
        this(ifc, new UDPSlaveTerminalFactory() {

            @Override
            public UDPSlaveTerminal create(InetAddress interfac, int port) {
                UDPSlaveTerminal terminal = new UDPSlaveTerminal(interfac);
                terminal.setLocalPort(port);
                return terminal;
            }
        });
    }// ModbusUDPListener

    public ModbusUDPListener(InetAddress ifc, UDPSlaveTerminalFactory terminalFactory) {
        m_Interface = ifc;
        this.m_TerminalFactory = terminalFactory;
    }

    /**
     * Returns the number of the port this <tt>ModbusUDPListener</tt>
     * is listening to.
     *
     * @return the number of the IP port as <tt>int</tt>.
     */
    public int getPort() {
        return m_Port;
    }// getPort

    /**
     * Sets the number of the port this <tt>ModbusUDPListener</tt>
     * is listening to.
     *
     * @param port the number of the IP port as <tt>int</tt>.
     */
    public void setPort(int port) {
        m_Port = ((port > 0) ? port : Modbus.DEFAULT_PORT);
    }// setPort

    /**
     * Starts this <tt>ModbusUDPListener</tt>.
     */
    public void start() {
        // start listening
        try {
            m_Terminal = m_TerminalFactory.create(m_Interface == null ? InetAddress.getLocalHost() : m_Interface,
                    m_Port);
            m_Terminal.setLocalPort(m_Port);
            m_Terminal.activate();

            m_Handler = new ModbusUDPHandler(m_Terminal.getModbusTransport());
            m_HandlerThread = new Thread(m_Handler);
            m_HandlerThread.start();

        } catch (Exception e) {
            // FIXME: this is a major failure, how do we handle this
        }
        m_Listening = true;
    }// start

    /**
     * Stops this <tt>ModbusUDPListener</tt>.
     */
    public void stop() {
        // stop listening
        m_Terminal.deactivate();
        m_Handler.stop();
        m_Listening = false;
    }// stop

    /**
     * Tests if this <tt>ModbusTCPListener</tt> is listening
     * and accepting incoming connections.
     *
     * @return true if listening (and accepting incoming connections),
     *         false otherwise.
     */
    public boolean isListening() {
        return m_Listening;
    }// isListening

    class ModbusUDPHandler implements Runnable {

        private ModbusTransport m_Transport;
        private boolean m_Continue = true;

        public ModbusUDPHandler(ModbusTransport transport) {
            m_Transport = transport;
        }// constructor

        @Override
        public void run() {
            try {
                do {
                    // 1. read the request
                    ModbusRequest request = m_Transport.readRequest();
                    logger.trace("Request: {}", request.getHexMessage());
                    ModbusResponse response = null;

                    // test if Process image exists
                    if (ModbusCoupler.getReference().getProcessImage() == null) {
                        response = request.createExceptionResponse(Modbus.ILLEGAL_FUNCTION_EXCEPTION);
                    } else {
                        response = request.createResponse();
                    }
                    logger.debug("Request: {}", request.getHexMessage());
                    logger.debug("Response: {}", response.getHexMessage());

                    m_Transport.writeMessage(response);
                } while (m_Continue);
            } catch (ModbusIOException ex) {
                if (!ex.isEOF()) {
                    // other troubles, output for debug
                    ex.printStackTrace();
                }
            } finally {
                try {
                    m_Terminal.deactivate();
                } catch (Exception ex) {
                    // ignore
                }
            }
        }// run

        public void stop() {
            m_Continue = false;
        }// stop

    }// inner class ModbusUDPHandler

    public int getLocalPort() {
        if (m_Terminal == null) {
            return -1;
        }
        return m_Terminal.getLocalPort();
    }

}// class ModbusUDPListener
