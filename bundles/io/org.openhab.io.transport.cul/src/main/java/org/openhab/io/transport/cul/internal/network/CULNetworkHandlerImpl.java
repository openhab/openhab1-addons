/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.cul.internal.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.StringUtils;
import org.openhab.io.transport.cul.CULDeviceException;
import org.openhab.io.transport.cul.internal.AbstractCULHandler;
import org.openhab.io.transport.cul.internal.CULConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation for culfw based devices which communicate via network port
 * (CUN for example).
 *
 * @author Markus Heberling
 * @author Till Klocke
 * @since 1.5.0
 */
public class CULNetworkHandlerImpl extends AbstractCULHandler<CULNetworkConfig> implements Runnable {

    private static final int CUN_DEFAULT_PORT = 2323;

    protected static final Logger logger = LoggerFactory.getLogger(CULNetworkHandlerImpl.class);
    private static final long INITIAL_RECONNECT_INTERVAL = 500; // 500 ms.
    private static final long MAXIMUM_RECONNECT_INTERVAL = 30000; // 30 sec.
    private static final int READ_BUFFER_SIZE = 2048;
    private static final int WRITE_BUFFER_SIZE = 2048;

    private long reconnectInterval = INITIAL_RECONNECT_INTERVAL;

    private ByteBuffer readBuf = ByteBuffer.allocateDirect(READ_BUFFER_SIZE);
    private ByteBuffer writeBuf = ByteBuffer.allocateDirect(WRITE_BUFFER_SIZE);

    private final Thread thread = new Thread(this);
    private SocketAddress address;

    private Selector selector;
    private SocketChannel channel;

    private final AtomicBoolean connected = new AtomicBoolean(false);

    private StringBuffer commandBuffer = new StringBuffer();
    // Encoding for text based CUN protocol
    private Charset cs = Charset.forName("ASCII");

    /**
     * Constructor including property map for specific configuration. Just for
     * compatibility with CulSerialHandlerImpl
     *
     * @param config config for the handler
     */
    public CULNetworkHandlerImpl(CULConfig config) {
        super((CULNetworkConfig) config);
    }

    @Override
    protected void openHardware() throws CULDeviceException {
        String deviceName = config.getDeviceAddress();
        logger.debug("Trying to open CUN with deviceName {}", deviceName);
        URI uri;
        try {
            uri = new URI("cul://" + deviceName);
            String host = uri.getHost();
            int port = uri.getPort() == -1 ? CUN_DEFAULT_PORT : uri.getPort();

            if (uri.getHost() == null || uri.getPort() == -1) {
                throw new CULDeviceException("Could not parse host:port from " + deviceName);
            }
            this.address = new InetSocketAddress(host, port);
        } catch (URISyntaxException e) {
            throw new CULDeviceException("Could not parse host:port from " + deviceName, e);
        }

        thread.start();
        // Only return when we are connected, as soon as this method returns this Handler
        // is considered ready.
        while (!connected.get()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // Ignore
            }
        }
    }

    @Override
    protected void closeHardware() {
        thread.interrupt();
    }

    @Override
    protected void write(String command) {
        ByteBuffer buf = cs.encode(command);
        try {
            send(buf);
        } catch (InterruptedException e) {
            logger.warn("InterruptedException when sending command", e);
        } catch (IOException e) {
            logger.warn("IOException when sending command", e);
        }
    }

    private void send(ByteBuffer buffer) throws InterruptedException, IOException {
        if (!connected.get()) {
            throw new IOException("not connected");
        }
        synchronized (writeBuf) {
            // try direct write of what's in the buffer to free up space
            if (writeBuf.remaining() < buffer.remaining()) {
                writeBuf.flip();
                int bytesOp = 0, bytesTotal = 0;
                while (writeBuf.hasRemaining() && (bytesOp = channel.write(writeBuf)) > 0) {
                    bytesTotal += bytesOp;
                }
                writeBuf.compact();
                logger.debug("Written {} bytes to the network", bytesTotal);
            }

            // if didn't help, wait till some space appears
            if (Thread.currentThread().getId() != thread.getId()) {
                while (writeBuf.remaining() < buffer.remaining()) {
                    writeBuf.wait();
                }
            } else {
                if (writeBuf.remaining() < buffer.remaining()) {
                    throw new IOException("send buffer full"); // TODO: add reallocation or buffers chain
                }
            }
            writeBuf.put(buffer);

            // try direct write to decrease the latency
            writeBuf.flip();
            int bytesOp = 0, bytesTotal = 0;
            while (writeBuf.hasRemaining() && (bytesOp = channel.write(writeBuf)) > 0) {
                bytesTotal += bytesOp;
            }
            writeBuf.compact();
            logger.debug("Written {} bytes to the network", bytesTotal);

            if (writeBuf.hasRemaining()) {
                SelectionKey key = channel.keyFor(selector);
                key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                selector.wakeup();
            }
        }
    }

    private void configureChannel(SocketChannel channel) throws IOException {
        channel.configureBlocking(false);
        channel.socket().setSendBufferSize(0x100000); // 1Mb
        channel.socket().setReceiveBufferSize(0x100000); // 1Mb
        channel.socket().setKeepAlive(true);
        channel.socket().setReuseAddress(true);
        channel.socket().setSoLinger(false, 0);
        channel.socket().setSoTimeout(0);
        channel.socket().setTcpNoDelay(true);
    }

    @Override
    public void run() {
        logger.info("event loop starting");
        try {
            while (!Thread.interrupted()) { // reconnection loop
                try {
                    selector = Selector.open();
                    channel = SocketChannel.open();
                    configureChannel(channel);

                    channel.connect(address);
                    channel.register(selector, SelectionKey.OP_CONNECT);

                    while (!thread.isInterrupted() && channel.isOpen()) { // events multiplexing loop
                        if (selector.select() > 0) {
                            processSelectedKeys(selector.selectedKeys());
                        }
                    }
                } catch (Exception e) {
                    logger.warn("exception", e);
                } finally {
                    connected.set(false);
                    onDisconnected();
                    writeBuf.clear();
                    readBuf.clear();
                    if (channel != null) {
                        channel.close();
                    }
                    if (selector != null) {
                        selector.close();
                    }
                    logger.info("connection closed");
                }

                try {
                    Thread.sleep(reconnectInterval);
                    if (reconnectInterval < MAXIMUM_RECONNECT_INTERVAL) {
                        reconnectInterval *= 2;
                    }
                    logger.info("reconnecting to {}", address);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (Exception e) {
            logger.warn("unrecoverable error", e);
        }

        logger.info("event loop terminated");
    }

    private void onDisconnected() {
        logger.info("Disconnected from {}", address);

    }

    private void processSelectedKeys(Set<SelectionKey> keys) throws Exception {
        Iterator<SelectionKey> itr = keys.iterator();
        while (itr.hasNext()) {
            SelectionKey key = itr.next();
            if (key.isReadable()) {
                processRead(key);
            }
            if (key.isWritable()) {
                processWrite(key);
            }
            if (key.isConnectable()) {
                processConnect(key);
            }
            if (key.isAcceptable()) {
                ;
            }
            itr.remove();
        }
    }

    private void processConnect(SelectionKey key) throws Exception {
        SocketChannel ch = (SocketChannel) key.channel();
        if (ch.finishConnect()) {
            key.interestOps(key.interestOps() ^ SelectionKey.OP_CONNECT);
            key.interestOps(key.interestOps() | SelectionKey.OP_READ);
            reconnectInterval = INITIAL_RECONNECT_INTERVAL;
            connected.set(true);
            onConnected();
        }
    }

    private void onConnected() {
        logger.info("Connected to CUN ({})", address);

    }

    private void processRead(SelectionKey key) throws Exception {
        ReadableByteChannel ch = (ReadableByteChannel) key.channel();

        int bytesOp = 0, bytesTotal = 0;
        while (readBuf.hasRemaining() && (bytesOp = ch.read(readBuf)) > 0) {
            bytesTotal += bytesOp;
        }
        logger.debug("Read {} bytes from network", bytesTotal);

        if (bytesTotal > 0) {
            readBuf.flip();
            onRead(readBuf);
            readBuf.compact();
        } else if (bytesOp == -1) {
            logger.info("peer closed read channel");
            ch.close();
        }
    }

    private void onRead(ByteBuffer readBuf) {
        CharBuffer charBuf = cs.decode(readBuf);
        while (charBuf.hasRemaining()) {
            char currentChar = charBuf.get();
            if (currentChar == '\r' || currentChar == '\n') {
                String command = commandBuffer.toString();
                if (!StringUtils.isEmpty(command)) {
                    processNextLine(command);
                }
                commandBuffer = new StringBuffer();
            } else {
                commandBuffer.append(currentChar);
            }
        }
    }

    private void processWrite(SelectionKey key) throws IOException {
        WritableByteChannel ch = (WritableByteChannel) key.channel();
        synchronized (writeBuf) {
            writeBuf.flip();

            int bytesOp = 0, bytesTotal = 0;
            while (writeBuf.hasRemaining() && (bytesOp = ch.write(writeBuf)) > 0) {
                bytesTotal += bytesOp;
            }
            logger.debug("Written {} bytes to the network", bytesTotal);

            if (writeBuf.remaining() == 0) {
                key.interestOps(key.interestOps() ^ SelectionKey.OP_WRITE);
            }

            if (bytesTotal > 0) {
                writeBuf.notify();
            } else if (bytesOp == -1) {
                logger.info("peer closed write channel");
                ch.close();
            }

            writeBuf.compact();
        }
    }
}
