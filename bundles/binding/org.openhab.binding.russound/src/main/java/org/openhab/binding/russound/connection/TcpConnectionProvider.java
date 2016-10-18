package org.openhab.binding.russound.connection;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpConnectionProvider implements ConnectionProvider {

    private static final Logger logger = LoggerFactory.getLogger(TcpConnectionProvider.class);

    /** Connection timeout in milliseconds **/
    private static final int CONNECTION_TIMEOUT = 5000;

    /** Instantiated class IP for the receiver to communicate with. **/
    private String receiverIP = "192.168.1.30";

    /** default port. **/
    public static final int DEFAULT_EISCP_PORT = 7777;

    /** Connection test interval in milliseconds **/
    private static final int CONNECTION_TEST_INTERVAL = 20000;
    /** Socket timeout in milliseconds **/
    private static final int SOCKET_TIMEOUT = CONNECTION_TEST_INTERVAL + 10000;

    /** Instantiated class Port for the receiver to communicate with. **/
    private int receiverPort = DEFAULT_EISCP_PORT;

    private Socket eiscpSocket = null;
    private ObjectOutputStream outStream = null;
    private DataInputStream inStream = null;
    private boolean connected = false;

    public TcpConnectionProvider(String ip, int port) {
        if (StringUtils.isNotBlank(ip)) {
            receiverIP = ip;
        }
        if (port >= 1) {
            receiverPort = port;
        }
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public ObjectOutputStream getOutputStream() {
        return outStream;
    }

    @Override
    public DataInputStream getInputStream() {
        return inStream;
    }

    /**
     * Connects to the receiver by opening a socket connection through the IP
     * and port.
     **/
    @Override
    public boolean connect() {

        if (eiscpSocket == null || !connected || !eiscpSocket.isConnected()) {
            try {
                // Creating a socket to connect to the server
                eiscpSocket = new Socket();
                eiscpSocket.connect(new InetSocketAddress(receiverIP, receiverPort), CONNECTION_TIMEOUT);

                logger.debug("Connected to {} on port {}", receiverIP, receiverPort);

                // Get Input and Output streams
                outStream = new ObjectOutputStream(eiscpSocket.getOutputStream());
                inStream = new DataInputStream(eiscpSocket.getInputStream());

                eiscpSocket.setSoTimeout(SOCKET_TIMEOUT);
                outStream.flush();
                connected = true;

            } catch (UnknownHostException unknownHost) {
                logger.error("You are trying to connect to an unknown host!", unknownHost);
            } catch (IOException ioException) {
                logger.error("Can't connect: " + ioException.getMessage());
            }
        }

        return connected;
    }

    @Override
    public void disconnect() throws IOException {
        if (inStream != null) {
            inStream.close();
            inStream = null;
            logger.debug("closed input stream!");
        }
        if (outStream != null) {
            try {
                outStream.close();
                logger.debug("closed output stream!");
            } catch (SocketException e) {
                logger.debug("Received socket exception, assumming already closed", e);
            }
            outStream = null;
        }
        if (eiscpSocket != null) {
            eiscpSocket.close();
            eiscpSocket = null;
            logger.debug("closed socket!");
        }

    }
}
