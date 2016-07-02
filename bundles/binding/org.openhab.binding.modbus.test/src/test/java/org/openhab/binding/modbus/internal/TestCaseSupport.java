/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.io.File;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.commons.lang.NotImplementedException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;

import gnu.io.SerialPort;
import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.io.ModbusTransport;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.net.ModbusSerialListener;
import net.wimpi.modbus.net.ModbusTCPListener;
import net.wimpi.modbus.net.ModbusUDPListener;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.net.SerialConnectionFactory;
import net.wimpi.modbus.net.TCPSlaveConnection;
import net.wimpi.modbus.net.TCPSlaveConnection.ModbusTCPTransportFactory;
import net.wimpi.modbus.net.TCPSlaveConnectionFactory;
import net.wimpi.modbus.net.UDPSlaveTerminal;
import net.wimpi.modbus.net.UDPSlaveTerminal.ModbusUDPTransportFactoryImpl;
import net.wimpi.modbus.net.UDPSlaveTerminalFactory;
import net.wimpi.modbus.net.UDPTerminal;
import net.wimpi.modbus.procimg.SimpleProcessImage;
import net.wimpi.modbus.util.AtomicCounter;
import net.wimpi.modbus.util.SerialParameters;

public class TestCaseSupport {

    public enum ServerType {
        TCP,
        UDP,
        SERIAL
    }

    /**
     * Servers to test
     * Serial is system dependent
     */
    public static final ServerType[] TEST_SERVERS = new ServerType[] { ServerType.TCP
            // ServerType.UDP,
            // ServerType.SERIAL
    };

    // One can perhaps test SERIAL with https://github.com/freemed/tty0tty
    // and using those virtual ports? Not the same thing as real serial device of course
    private static String SERIAL_SERVER_PORT = "/dev/pts/7";
    private static String SERIAL_CLIENT_PORT = "/dev/pts/8";

    private static SerialParameters SERIAL_PARAMETERS_CLIENT = new SerialParameters(SERIAL_CLIENT_PORT, 115200,
            SerialPort.FLOWCONTROL_NONE, SerialPort.FLOWCONTROL_NONE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
            SerialPort.PARITY_NONE, Modbus.SERIAL_ENCODING_ASCII, false, 1000);

    private static SerialParameters SERIAL_PARAMETERS_SERVER = new SerialParameters(SERIAL_SERVER_PORT,
            SERIAL_PARAMETERS_CLIENT.getBaudRate(), SERIAL_PARAMETERS_CLIENT.getFlowControlIn(),
            SERIAL_PARAMETERS_CLIENT.getFlowControlOut(), SERIAL_PARAMETERS_CLIENT.getDatabits(),
            SERIAL_PARAMETERS_CLIENT.getStopbits(), SERIAL_PARAMETERS_CLIENT.getParity(),
            SERIAL_PARAMETERS_CLIENT.getEncoding(), SERIAL_PARAMETERS_CLIENT.isEcho(), 1000);

    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
        System.setProperty("gnu.io.rxtx.SerialPorts", SERIAL_SERVER_PORT + File.pathSeparator + SERIAL_CLIENT_PORT);
    }

    /**
     * Max time to wait for connections/requests from client
     */
    protected static final int MAX_WAIT_REQUESTS_MILLIS = 1000;

    /**
     * The server runs in single thread, only one connection is accepted at a time.
     * This makes the tests as strict as possible -- connection must be closed.
     */
    private static final int SERVER_THREADS = 1;
    // "infinity", we execute manually manually
    protected static long REFRESH_INTERVAL = 1000000L;
    protected static String SLAVE_NAME = "slave1";
    protected static String SLAVE2_NAME = "slave2";
    protected static int SLAVE_UNIT_ID = 1;

    private static AtomicCounter udpServerIndex = new AtomicCounter(0);

    @Mock
    protected EventPublisher eventPublisher;

    @Spy
    protected TCPSlaveConnectionFactory tcpConnectionFactory = new TCPSlaveConnectionFactoryImpl();

    @Spy
    protected UDPSlaveTerminalFactory udpTerminalFactory = new UDPSlaveTerminalFactoryImpl();

    @Spy
    protected SerialConnectionFactory serialConnectionFactory = new SerialConnectionFactoryImpl();

    protected ResultCaptor<ModbusRequest> modbustRequestCaptor;

    protected ModbusBinding binding;
    protected ModbusTCPListener tcpListener;
    protected ModbusUDPListener udpListener;
    protected ModbusSerialListener serialListener;
    protected SimpleProcessImage spi;
    protected int tcpModbusPort = -1;
    protected int udpModbusPort = -1;
    protected ServerType serverType = ServerType.TCP;
    protected long artificialServerWait = 0;

    private Thread serialServerThread = new Thread("ModbusBindingTestsSerialServer") {
        @Override
        public void run() {
            serialListener = new ModbusSerialListener(SERIAL_PARAMETERS_SERVER);
        };
    };

    protected static InetAddress localAddress() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }

    private static Dictionary<String, Object> addSlave(Dictionary<String, Object> config, String protocol,
            String connection, String slaveName, String type, String valuetype, int slaveId, int start, int length) {
        /**
         * Add a modbus slave to config
         */
        config.put(String.format("%s.%s.connection", protocol, slaveName), connection);
        config.put(String.format("%s.%s.id", protocol, slaveName), String.valueOf(slaveId));
        config.put(String.format("%s.%s.type", protocol, slaveName), type);
        if (valuetype != null) {
            config.put(String.format("%s.%s.valuetype", protocol, slaveName), valuetype);
        }
        config.put(String.format("%s.%s.start", protocol, slaveName), String.valueOf(start));
        config.put(String.format("%s.%s.length", protocol, slaveName), String.valueOf(length));
        return config;
    }

    /**
     * Add slave to config using configured serverType
     */
    protected Dictionary<String, Object> addSlave(Dictionary<String, Object> config, String slaveName, String type,
            String valuetype, int start, int length) throws UnknownHostException {
        String protocol = null;
        String connection;
        if (ServerType.TCP.equals(serverType)) {
            int port = tcpModbusPort;
            protocol = "tcp";
            connection = String.format("%s:%d", localAddress().getHostAddress(), port);
        } else if (ServerType.UDP.equals(serverType)) {
            int port = udpModbusPort;
            protocol = "udp";
            connection = String.format("%s:%d", localAddress().getHostAddress(), port);
        } else if (ServerType.SERIAL.equals(serverType)) {
            protocol = "serial";

            connection = String.format("%s:%d:%d:%s:%s:%s", SERIAL_PARAMETERS_CLIENT.getPortName(),
                    SERIAL_PARAMETERS_CLIENT.getBaudRate(), SERIAL_PARAMETERS_CLIENT.getDatabits(),
                    SERIAL_PARAMETERS_CLIENT.getParityString(), SERIAL_PARAMETERS_CLIENT.getStopbitsString(),
                    SERIAL_PARAMETERS_CLIENT.getEncoding());
        } else {
            throw new NotImplementedException();
        }

        return addSlave(config, protocol, connection, slaveName, type, valuetype, 1, start, length);
    }

    protected static Dictionary<String, Object> newLongPollBindingConfig() {
        Dictionary<String, Object> config = new Hashtable<String, Object>();
        config.put("poll", String.valueOf(REFRESH_INTERVAL));
        return config;
    }

    protected void configureSwitchItemBinding(int items, String slaveName, int itemOffset, String itemPrefix,
            State initialState) throws BindingConfigParseException {
        Assert.assertEquals(REFRESH_INTERVAL, binding.getRefreshInterval());
        final ModbusGenericBindingProvider provider = new ModbusGenericBindingProvider();
        for (int itemIndex = itemOffset; itemIndex < items + itemOffset; itemIndex++) {
            SwitchItem item = new SwitchItem(String.format("%sItem%d", itemPrefix, itemIndex + 1));
            if (initialState != null) {
                item.setState(initialState);
            }
            provider.processBindingConfiguration("test.items", item, String.format("%s:%d", slaveName, itemIndex));
        }
        binding.setEventPublisher(eventPublisher);
        binding.addBindingProvider(provider);
    }

    protected void configureSwitchItemBinding(int items, String slaveName, int itemOffset)
            throws BindingConfigParseException {
        configureSwitchItemBinding(items, slaveName, itemOffset, "", null);
    }

    protected void configureNumberItemBinding(int items, String slaveName, int itemOffset, String itemPrefix,
            State initialState) throws BindingConfigParseException {
        Assert.assertEquals(REFRESH_INTERVAL, binding.getRefreshInterval());
        final ModbusGenericBindingProvider provider = new ModbusGenericBindingProvider();
        for (int itemIndex = itemOffset; itemIndex < items + itemOffset; itemIndex++) {
            NumberItem item = new NumberItem(String.format("%sItem%d", itemPrefix, itemIndex + 1));
            if (initialState != null) {
                item.setState(initialState);
            }
            provider.processBindingConfiguration("test.items", item, String.format("%s:%d", slaveName, itemIndex));
        }
        binding.setEventPublisher(eventPublisher);
        binding.addBindingProvider(provider);
    }

    protected void configureNumberItemBinding(int items, String slaveName, int itemOffset)
            throws BindingConfigParseException {
        configureNumberItemBinding(items, slaveName, itemOffset, "", null);
    }

    protected void verifyBitItems(String expectedBits, int itemOffset, String itemPrefix) {
        for (int bitIndex = 0; bitIndex < expectedBits.length(); bitIndex++) {
            char bit = expectedBits.charAt(bitIndex);
            State state;
            if (bit == '0') {
                state = OnOffType.OFF;
            } else if (bit == '1') {
                state = OnOffType.ON;
            } else {
                throw new RuntimeException("invalid testdata");
            }
            verify(eventPublisher).postUpdate(String.format("%sItem%d", itemPrefix, bitIndex + itemOffset + 1), state);
        }
    }

    protected void verifyBitItems(String expectedBits, int itemOffset) {
        verifyBitItems(expectedBits, itemOffset, "");
    }

    protected void verifyBitItems(String expectedBits) {
        verifyBitItems(expectedBits, 0, "");
    }

    @Before
    public void setUp() throws Exception {
        modbustRequestCaptor = new ResultCaptor<ModbusRequest>(artificialServerWait);
        MockitoAnnotations.initMocks(this);
        startServer();
    }

    @After
    public void tearDown() {
        stopServer();
    }

    protected void waitForRequests(int expectedRequestCount) {
        int sleepMillis = 10;
        int waited = 0;
        AssertionError lastError = new AssertionError("Connections not established in time!");
        while (waited < MAX_WAIT_REQUESTS_MILLIS) {
            try {
                assertThat(modbustRequestCaptor.getAllReturnValues().size(), is(equalTo(expectedRequestCount)));
            } catch (AssertionError e) {
                lastError = e;
                try {
                    Thread.sleep(sleepMillis);
                    waited += sleepMillis;
                } catch (InterruptedException e1) {
                    throw new AssertionError("test interrupted");
                }
                continue;
            }
            // OK!
            return;
        }
        // Requests not established in time
        throw lastError;
    }

    protected void waitForConnectionsReceived(int expectedConnections) {
        int sleepMillis = 10;
        int waited = 0;
        AssertionError lastError = new AssertionError("Connections not established in time!");
        while (waited < MAX_WAIT_REQUESTS_MILLIS) {
            try {
                if (ServerType.TCP.equals(serverType)) {
                    verify(tcpConnectionFactory, times(expectedConnections)).create(any(Socket.class));
                } else if (ServerType.UDP.equals(serverType)) {
                    // No-op
                    // verify(udpTerminalFactory, times(expectedConnections)).create(any(InetAddress.class),
                    // any(Integer.class));
                } else if (ServerType.SERIAL.equals(serverType)) {
                    // No-op
                } else {
                    throw new NotImplementedException();
                }
            } catch (AssertionError e) {
                lastError = e;
                try {
                    Thread.sleep(sleepMillis);
                    waited += sleepMillis;
                } catch (InterruptedException e1) {
                    throw new AssertionError("test interrupted");
                }
                continue;
            }
            // OK!
            return;
        }
        System.err.println("Connections not established in time!");
        throw lastError;
    }

    private void startServer() throws UnknownHostException, InterruptedException {
        spi = new SimpleProcessImage();
        ModbusCoupler.getReference().setProcessImage(spi);
        ModbusCoupler.getReference().setMaster(false);
        ModbusCoupler.getReference().setUnitID(SLAVE_UNIT_ID);

        if (ServerType.TCP.equals(serverType)) {
            startTCPServer();
        } else if (ServerType.UDP.equals(serverType)) {
            startUDPServer();
        } else if (ServerType.SERIAL.equals(serverType)) {
            startSerialServer();
        } else {
            throw new NotImplementedException();
        }
    }

    private void stopServer() {
        if (ServerType.TCP.equals(serverType)) {
            tcpListener.stop();
        } else if (ServerType.UDP.equals(serverType)) {
            udpListener.stop();
            System.err.println(udpModbusPort);
        } else if (ServerType.SERIAL.equals(serverType)) {
            try {
                serialServerThread.join(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            serialServerThread.interrupt();
        } else {
            throw new NotImplementedException();
        }
    }

    private void startUDPServer() throws UnknownHostException, InterruptedException {
        udpListener = new ModbusUDPListener(localAddress(), udpTerminalFactory);
        for (int portCandidate = 10000 + udpServerIndex.increment(); portCandidate < 20000; portCandidate++) {
            try {
                DatagramSocket socket = new DatagramSocket(portCandidate);
                socket.close();
                udpListener.setPort(portCandidate);
                break;
            } catch (SocketException e) {
                continue;
            }
        }

        udpListener.start();
        waitForUDPServerStartup();
        Assert.assertNotSame(-1, udpModbusPort);
        Assert.assertNotSame(0, udpModbusPort);
    }

    private void waitForUDPServerStartup() throws InterruptedException {
        // Query server port. It seems to take time (probably due to thread starting)
        int sleep_millis = 5;
        int total_try_millis = 10000; // 10sec
        for (int tries = 0; tries < Math.max(1, total_try_millis / sleep_millis); tries++) {
            udpModbusPort = udpListener.getLocalPort();
            if (udpModbusPort != -1) {
                break;
            }
            Thread.sleep(sleep_millis);
        }
    }

    private void startTCPServer() throws UnknownHostException, InterruptedException {
        // Serve single user at a time
        tcpListener = new ModbusTCPListener(SERVER_THREADS, localAddress(), tcpConnectionFactory);
        // Use any open port
        tcpListener.setPort(0);
        tcpListener.start();
        // Query server port. It seems to take time (probably due to thread starting)
        waitForTCPServerStartup();
        Assert.assertNotSame(-1, tcpModbusPort);
        Assert.assertNotSame(0, tcpModbusPort);
    }

    private void waitForTCPServerStartup() throws InterruptedException {
        int sleep_millis = 5;
        int total_try_millis = 10000; // 10sec
        for (int tries = 0; tries < Math.max(1, total_try_millis / sleep_millis); tries++) {
            tcpModbusPort = tcpListener.getLocalPort();
            if (tcpModbusPort != -1) {
                break;
            }
            Thread.sleep(sleep_millis);
        }
    }

    private void startSerialServer() throws UnknownHostException, InterruptedException {
        serialServerThread.start();
        Thread.sleep(1000);
    }

    /**
     * Transport factory that spies the created transport items
     */
    public class SpyingModbusTCPTransportFactory extends ModbusTCPTransportFactory {

        @Override
        public ModbusTransport create(Socket socket) {
            ModbusTransport transport = spy(super.create(socket));
            // Capture requests produced by our server transport
            try {
                doAnswer(modbustRequestCaptor).when(transport).readRequest();
            } catch (ModbusIOException e) {
                throw new RuntimeException(e);
            }
            return transport;
        }
    }

    public class SpyingModbusUDPTransportFactory extends ModbusUDPTransportFactoryImpl {

        @Override
        public ModbusTransport create(UDPTerminal terminal) {
            ModbusTransport transport = spy(super.create(terminal));
            // Capture requests produced by our server transport
            try {
                doAnswer(modbustRequestCaptor).when(transport).readRequest();
            } catch (ModbusIOException e) {
                throw new RuntimeException(e);
            }
            return transport;
        }
    }

    public class TCPSlaveConnectionFactoryImpl implements TCPSlaveConnectionFactory {

        @Override
        public TCPSlaveConnection create(Socket socket) {
            return new TCPSlaveConnection(socket, new SpyingModbusTCPTransportFactory());
        }

    }

    public class UDPSlaveTerminalFactoryImpl implements UDPSlaveTerminalFactory {

        @Override
        public UDPSlaveTerminal create(InetAddress interfac, int port) {
            UDPSlaveTerminal terminal = new UDPSlaveTerminal(interfac, new SpyingModbusUDPTransportFactory(), 1);
            terminal.setLocalPort(port);
            return terminal;
        }

    }

    public class SerialConnectionFactoryImpl implements SerialConnectionFactory {
        @Override
        public SerialConnection create(SerialParameters parameters) {
            SerialConnection serialConnection = new SerialConnection(parameters) {
                @Override
                public ModbusTransport getModbusTransport() {
                    ModbusTransport transport = spy(super.getModbusTransport());
                    try {
                        doAnswer(modbustRequestCaptor).when(transport).readRequest();
                    } catch (ModbusIOException e) {
                        throw new RuntimeException(e);
                    }
                    return transport;
                }
            };
            return serialConnection;
        }
    }
}