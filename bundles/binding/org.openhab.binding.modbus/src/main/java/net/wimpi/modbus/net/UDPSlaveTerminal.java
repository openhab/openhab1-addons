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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusTransport;
import net.wimpi.modbus.io.ModbusUDPTransport;
import net.wimpi.modbus.io.ModbusUDPTransport;
import net.wimpi.modbus.io.ModbusUDPTransportFactory;
import net.wimpi.modbus.util.LinkedQueue;
import net.wimpi.modbus.util.LinkedQueue;
import net.wimpi.modbus.util.ModbusUtil;
import net.wimpi.modbus.util.ModbusUtil;

/**
 * Class implementing a <tt>UDPSlaveTerminal</tt>.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class UDPSlaveTerminal implements UDPTerminal {
    public static class ModbusUDPTransportFactoryImpl implements ModbusUDPTransportFactory {

        @Override
        public ModbusTransport create(UDPTerminal terminal) {
            return new ModbusUDPTransport(terminal);
        }

    }

    private static final Logger logger = LoggerFactory.getLogger(UDPSlaveTerminal.class);
    public static final int DEFAULT_DEACTIVATION_WAIT_MILLIS = 100;

    // instance attributes
    private DatagramSocket m_Socket;
    private int m_Timeout = Modbus.DEFAULT_TIMEOUT;
    private boolean m_Active;
    protected InetAddress m_LocalAddress;
    private int m_LocalPort = Modbus.DEFAULT_PORT;
    protected ModbusTransport m_ModbusTransport;
    private int m_Retries = Modbus.DEFAULT_RETRIES;

    private LinkedQueue m_SendQueue;
    private LinkedQueue m_ReceiveQueue;
    private PacketSender m_PacketSender;
    private PacketReceiver m_PacketReceiver;
    private Thread m_Receiver;
    private Thread m_Sender;

    protected Hashtable m_Requests;

    private ModbusUDPTransportFactory m_TransportFactory;
    /**
     * Time to wait for Threads to close when deactivate is called.
     * Note that often no time is enough since threads might be waiting for data.
     */
    private int m_DeactivationWaitMillis = 100;

    public UDPSlaveTerminal() {
        this(null);
    }// constructor

    public UDPSlaveTerminal(InetAddress localaddress) {
        this(localaddress, new ModbusUDPTransportFactoryImpl(), DEFAULT_DEACTIVATION_WAIT_MILLIS);
    }

    public UDPSlaveTerminal(InetAddress localaddress, ModbusUDPTransportFactory transportFactory,
            int deactivationWaitMillis) {
        m_LocalAddress = localaddress;
        m_TransportFactory = transportFactory;
        m_DeactivationWaitMillis = deactivationWaitMillis;
        m_SendQueue = new LinkedQueue();
        m_ReceiveQueue = new LinkedQueue();
        // m_Requests = new Hashtable(342, 0.75F);
        m_Requests = new Hashtable(342);
    }// constructor

    @Override
    public InetAddress getLocalAddress() {
        return m_LocalAddress;
    }// getLocalAddress

    @Override
    public int getLocalPort() {
        return m_LocalPort;
    }// getLocalPort

    public void setLocalPort(int port) {
        m_LocalPort = port;
    }// setLocalPort

    /**
     * Tests if this <tt>UDPSlaveTerminal</tt> is active.
     *
     * @return <tt>true</tt> if active, <tt>false</tt> otherwise.
     */
    @Override
    public boolean isActive() {
        return m_Active;
    }// isActive

    /**
     * Activate this <tt>UDPTerminal</tt>.
     *
     * @throws Exception if there is a network failure.
     */
    @Override
    public synchronized void activate() throws Exception {
        if (!isActive()) {
            logger.debug("UDPSlaveTerminal::activate()");
            if (m_Socket == null) {
                if (m_LocalAddress != null && m_LocalPort != -1) {
                    m_Socket = new DatagramSocket(m_LocalPort, m_LocalAddress);
                } else {
                    m_Socket = new DatagramSocket();
                    m_LocalPort = m_Socket.getLocalPort();
                    m_LocalAddress = m_Socket.getLocalAddress();
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("UDPSlaveTerminal::haveSocket():{}", m_Socket.toString());
                logger.debug("UDPSlaveTerminal::addr=:{}:port={}", m_LocalAddress.toString(), m_LocalPort);
            }

            m_Socket.setReceiveBufferSize(1024);
            m_Socket.setSendBufferSize(1024);
            m_PacketReceiver = new PacketReceiver();
            m_Receiver = new Thread(m_PacketReceiver);
            m_Receiver.start();
            logger.debug("UDPSlaveTerminal::receiver started()");
            m_PacketSender = new PacketSender();
            m_Sender = new Thread(m_PacketSender);
            m_Sender.start();
            logger.debug("UDPSlaveTerminal::sender started()");
            m_ModbusTransport = m_TransportFactory.create(this);
            logger.debug("UDPSlaveTerminal::transport created");
            m_Active = true;
        }
        logger.info("UDPSlaveTerminal::activated");
    }// activate

    /**
     * Deactivates this <tt>UDPSlaveTerminal</tt>.
     */
    @Override
    public void deactivate() {
        try {
            if (m_Active) {
                // 1. stop receiver
                m_PacketReceiver.stop();
                m_Receiver.join(m_DeactivationWaitMillis);
                m_Receiver.interrupt();
                // 2. stop sender gracefully
                m_PacketSender.stop();
                m_Sender.join(m_DeactivationWaitMillis);
                m_Sender.interrupt();
                // 3. close socket
                m_Socket.close();
                m_ModbusTransport = null;
                m_Active = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }// deactivate

    /**
     * Returns the <tt>ModbusTransport</tt> associated with this
     * <tt>TCPMasterConnection</tt>.
     *
     * @return the connection's <tt>ModbusTransport</tt>.
     */
    @Override
    public ModbusTransport getModbusTransport() {
        return m_ModbusTransport;
    }// getModbusTransport

    protected boolean hasResponse() {
        return !m_ReceiveQueue.isEmpty();
    }// hasResponse

    /**
     * Returns the timeout for this <tt>UDPSlaveTerminal</tt>.
     *
     * @return the timeout as <tt>int</tt>.
     *
     *         public int getReceiveTimeout() {
     *         return m_Timeout;
     *         }//getReceiveTimeout
     *
     *         /**
     *         Sets the timeout for this <tt>UDPSlaveTerminal</tt>.
     *
     * @param timeout the timeout as <tt>int</tt>.
     *
     *            public void setReceiveTimeout(int timeout) {
     *            m_Timeout = timeout;
     *            try {
     *            m_Socket.setSoTimeout(m_Timeout);
     *            } catch (IOException ex) {
     *            ex.printStackTrace();
     *            //handle?
     *            }
     *            }//setReceiveTimeout
     */
    /**
     * Returns the socket of this <tt>UDPSlaveTerminal</tt>.
     *
     * @return the socket as <tt>DatagramSocket</tt>.
     */
    public DatagramSocket getSocket() {
        return m_Socket;
    }// getSocket

    /**
     * Sets the socket of this <tt>UDPTerminal</tt>.
     *
     * @param sock the <tt>DatagramSocket</tt> for this terminal.
     */
    protected void setSocket(DatagramSocket sock) {
        m_Socket = sock;
    }// setSocket

    @Override
    public void sendMessage(byte[] msg) throws Exception {
        m_SendQueue.put(msg);
    }// sendPackage

    @Override
    public byte[] receiveMessage() throws Exception {
        return (byte[]) m_ReceiveQueue.take();
    }// receiveMessage

    class PacketSender implements Runnable {

        private boolean m_Continue;

        public PacketSender() {
            m_Continue = true;
        }// constructor

        @Override
        public void run() {
            do {
                try {
                    // 1. pickup the message and corresponding request
                    byte[] message = (byte[]) m_SendQueue.take();
                    DatagramPacket req = (DatagramPacket) m_Requests
                            .remove(new Integer(ModbusUtil.registersToInt(message)));
                    // 2. create new Package with corresponding address and port
                    DatagramPacket res = new DatagramPacket(message, message.length, req.getAddress(), req.getPort());
                    m_Socket.send(res);
                    logger.trace("Sent package from queue");
                } catch (Exception ex) {
                    DEBUG: ex.printStackTrace();
                }
            } while (m_Continue || !m_SendQueue.isEmpty());
        }// run

        public void stop() {
            m_Continue = false;
        }// stop

    }// PacketSender

    class PacketReceiver implements Runnable {

        private boolean m_Continue;

        public PacketReceiver() {
            m_Continue = true;
        }// constructor

        @Override
        public void run() {
            do {
                try {
                    // 1. Prepare buffer and receive package
                    byte[] buffer = new byte[256];// max size
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    m_Socket.receive(packet);
                    // 2. Extract TID and remember request
                    Integer tid = new Integer(ModbusUtil.registersToInt(buffer));
                    m_Requests.put(tid, packet);
                    // 3. place the data buffer in the queue
                    m_ReceiveQueue.put(buffer);
                    logger.trace("Received package placed in queue");
                } catch (Exception ex) {
                    DEBUG: ex.printStackTrace();
                }
            } while (m_Continue);
        }// run

        public void stop() {
            m_Continue = false;
        }// stop

    }// PacketReceiver

}// class UDPTerminal
