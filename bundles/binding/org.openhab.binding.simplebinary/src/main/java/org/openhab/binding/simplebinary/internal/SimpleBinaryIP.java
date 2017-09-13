/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.simplebinary.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Calendar;
import java.util.Map;

import org.openhab.binding.simplebinary.internal.SimpleBinaryDeviceState.DeviceStates;
import org.openhab.binding.simplebinary.internal.SimpleBinaryGenericBindingProvider.SimpleBinaryBindingConfig;
import org.openhab.binding.simplebinary.internal.SimpleBinaryGenericBindingProvider.SimpleBinaryInfoBindingConfig;
import org.openhab.binding.simplebinary.internal.SimpleBinaryPortState.PortStates;
import org.openhab.core.events.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IP device class
 *
 * @author Vita Tucek
 * @since 1.9.0
 */
public class SimpleBinaryIP extends SimpleBinaryGenericDevice {

    private static final Logger logger = LoggerFactory.getLogger(SimpleBinaryIP.class);

    /** port */
    public static final int DEFAULT_PORT = 43243;
    private String bindAddress = "";
    private int port = 43243;

    /** server socket instance */
    private AsynchronousServerSocketChannel listener;
    /** connected clients collection */
    private SimpleBinaryIPChannelInfoCollection channels;

    /**
     * Constructor
     *
     * @param deviceName
     * @param ip
     * @param port
     * @param simpleBinaryPoolControl
     */
    public SimpleBinaryIP(String deviceName, String ip, int port, SimpleBinaryPoolControl simpleBinaryPoolControl) {
        super(deviceName, "TCPserver", simpleBinaryPoolControl);

        this.bindAddress = ip;
        this.port = port;
    }

    /**
     * Return bind IP address
     *
     * @return
     */
    protected String getIp() {
        return this.bindAddress;
    }

    /**
     * Return port
     *
     * @return
     */
    protected int getPort() {
        return port;
    }

    @Override
    public void setBindingData(EventPublisher eventPublisher, Map<String, SimpleBinaryBindingConfig> itemsConfig,
            Map<String, SimpleBinaryInfoBindingConfig> itemsInfoConfig,
            Map<String, SimpleBinaryGenericDevice> configuredDevices) {
        super.setBindingData(eventPublisher, itemsConfig, itemsInfoConfig, configuredDevices);

        this.channels = new SimpleBinaryIPChannelInfoCollection(devicesStates, deviceName);
    }

    /**
     * Check if port is opened
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        return connected;
    }

    /**
     * Open socket
     *
     * @see org.openhab.binding.simplebinary.internal.SimpleBinaryIDevice#open()
     */
    @Override
    public Boolean open() {
        if (logger.isDebugEnabled()) {
            logger.debug("{} - Start listening", this.toString());
        }

        portState.setState(PortStates.CLOSED);
        // clear device states
        devicesStates.clear();
        // set initial state for configured devices
        devicesStates.setStateToAllConfiguredDevices(this.deviceName, DeviceStates.NOT_RESPONDING);
        // reset connected state
        connected = false;
        // setWaitingForAnswer(false);

        try {
            // AsynchronousChannelGroup threadGroup = AsynchronousChannelGroup.withThreadPool(executor);
            listener = AsynchronousServerSocketChannel.open();
            listener.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            InetSocketAddress hostAddress;

            if (this.getIp().length() > 0) {
                hostAddress = new InetSocketAddress(getIp(), getPort());
            } else {
                hostAddress = new InetSocketAddress(this.port);
            }

            // boolean running = true;

            listener.bind(hostAddress);

            logger.debug("{} - Listener local address={}", toString(), listener.getLocalAddress().toString());

            listener.accept(channels,
                    new CompletionHandler<AsynchronousSocketChannel, SimpleBinaryIPChannelInfoCollection>() {
                        @Override
                        public void completed(AsynchronousSocketChannel channel,
                                SimpleBinaryIPChannelInfoCollection a) {
                            // get ready for next connection
                            listener.accept(a, this);
                            // allocate receive buffer
                            final ByteBuffer buffer = ByteBuffer.allocateDirect(256);
                            // insert client into list
                            SimpleBinaryIPChannelInfo chInfo = a.addChannel(channel, buffer,
                                    new SimpleBinaryIRequestTimeouted() {

                                        @Override
                                        public void timeoutEvent(SimpleBinaryIPChannelInfo chInfo) {

                                            logger.warn("TCP server - Device{} - Receiving data timeouted. Thread={}",
                                                    chInfo.getDeviceId(), Thread.currentThread().getId());

                                            closeChannel(chInfo);
                                        }
                                    });
                            // channel is ready to write
                            chInfo.writeReady.set(true);

                            logger.info("TCP server - New device connected. IP={}. Thread={}", chInfo.getIp(),
                                    Thread.currentThread().getId());

                            // callback read
                            channel.read(buffer, chInfo, new CompletionHandler<Integer, SimpleBinaryIPChannelInfo>() {
                                @Override
                                public void completed(Integer result, final SimpleBinaryIPChannelInfo chInfo) {

                                    if (logger.isDebugEnabled()) {
                                        logger.debug("TCPserver - Channel {} - read. I/O result = {}", chInfo.getIp(),
                                                result);
                                    }

                                    if (result < 0) {
                                        if (logger.isDebugEnabled()) {
                                            logger.debug("TCPserver - Channel {} disconnected", chInfo.getIp());
                                        }

                                        closeChannel(chInfo);

                                        return;
                                    }

                                    if (chInfo.isIpLocked()) {
                                        // ipLocked - get device ID from configuration
                                        int forcedId = chInfo.getDeviceIdConfigured();

                                        logger.info(
                                                "TCPserver - Channel {} is marked as 'Locked'. ID would be forced to {}",
                                                chInfo.getIp(), forcedId);
                                    }

                                    // data processing
                                    SimpleBinaryByteBuffer inBuffer = chInfo.getBuffer();

                                    if (logger.isDebugEnabled()) {
                                        logger.debug("TCPserver - Channel {} - received data buffer size = {}",
                                                chInfo.getIp(), inBuffer.position());
                                    }

                                    while (inBuffer.position() > 3) {

                                        // verify device first
                                        if (!chInfo.isDeviceIdAlreadyReceived()) {
                                            SimpleBinaryMessage r = verifyDataOnly(inBuffer);

                                            if (r != null) {
                                                // Device can be configured in 'tcpserverclientlist'
                                                // * marked as 'IP locked' -> on this IP is my device ID doesn't matter
                                                // * /wo 'IP lock' -> device with this ID must be connected from this IP
                                                // Device is not configured in 'tcpserverclientlist'
                                                // * is not in conflict with configured / connected devices -> can be
                                                // serviced

                                                if (logger.isDebugEnabled()) {
                                                    logger.debug(
                                                            "TCPserver - Channel {} - Starting assign ID process. From device ID={} received",
                                                            chInfo.getIp(), r.deviceId);
                                                }

                                                if (!chInfo.isIpLocked()) {
                                                    // device is presented in configuration
                                                    if (chInfo.hasIdConfigured()) {
                                                        if (logger.isDebugEnabled()) {
                                                            logger.debug(
                                                                    "TCPserver - Channel {} - device from this IP is configured with ID={}",
                                                                    chInfo.getIp(), chInfo.getDeviceIdConfigured());
                                                        }
                                                        // received ID is not equal configured
                                                        if (chInfo.getDeviceIdConfigured() != r.deviceId) {
                                                            logger.error(
                                                                    "TCPserver - Device with IP {} has mismatch between configured(={}) and connected(={}) device ID's. "
                                                                            + "Remove it from configuration, change device ID or mark it in configuration as locked.",
                                                                    chInfo.getIp(), chInfo.getDeviceIdConfigured(),
                                                                    r.deviceId);

                                                            // send info to device
                                                            sendDataOut(SimpleBinaryProtocol.compileDenyDataFrame(
                                                                    r.deviceId, (byte) 0x1), chInfo);
                                                            // close channel
                                                            closeChannel(chInfo);

                                                            return;
                                                            // ID equals but IP mismatch
                                                        }
                                                        chInfo.assignDeviceId();

                                                        // assign ID for non-configured device
                                                    } else if (!chInfo.assignDeviceId(r.deviceId)) {
                                                        logger.error("TCPserver - DeviceID {} will be ignored.",
                                                                r.deviceId);
                                                        // send info to device
                                                        sendDataOut(SimpleBinaryProtocol
                                                                .compileDenyDataFrame(r.deviceId, (byte) 0x2), chInfo);
                                                        closeChannel(chInfo);
                                                        return;
                                                    } else if (logger.isDebugEnabled()) {
                                                        logger.debug(
                                                                "TCPserver - Channel {} - no configuration found. Assign ID process finished",
                                                                chInfo.getIp());
                                                    }
                                                } else {
                                                    if (logger.isDebugEnabled()) {
                                                        logger.debug(
                                                                "TCPserver - Channel {} - device is configured as IP locked",
                                                                chInfo.getIp());
                                                    }
                                                    chInfo.assignDeviceId();
                                                    if (chInfo.hasIdMismatch()) {
                                                        logger.info(
                                                                "TCPserver - Device with IP {} has mismatch between configured and connected device ID's.",
                                                                chInfo.getIp());
                                                    }
                                                }

                                                // send Welcome response
                                                sendDataOut(SimpleBinaryProtocol.compileWelcomeDataFrame(
                                                        r.getDeviceId(), chInfo.getDeviceId()), chInfo);
                                            } else {
                                                logger.error(
                                                        "TCPserver - Channel {} - device will be ignored. Non valid packet.",
                                                        chInfo.getIp());
                                                closeChannel(chInfo);
                                                return;
                                            }
                                        }

                                        // look what is in income raw data
                                        int r = chInfo.isIpLocked()
                                                ? processData(inBuffer, chInfo.getLastSentData(),
                                                        new Byte((byte) chInfo.getDeviceId()))
                                                : processData(inBuffer, chInfo.getLastSentData());

                                        if (r >= 0 || r == ProcessDataResult.INVALID_CRC
                                                || r == ProcessDataResult.BAD_CONFIG
                                                || r == ProcessDataResult.NO_VALID_ADDRESS
                                                || r == ProcessDataResult.UNKNOWN_MESSAGE) {
                                            // waiting for answer?
                                            if (chInfo.waitingForAnswer.get()) {
                                                // stop block sent
                                                chInfo.clearWaitingForAnswer();
                                            }
                                        } else if (r == ProcessDataResult.DATA_NOT_COMPLETED
                                                || r == ProcessDataResult.PROCESSING_ERROR) {
                                            break;
                                        }
                                    }

                                    if (logger.isDebugEnabled()) {
                                        logger.debug("TCPserver - Channel {}/{} - read finished", chInfo.getDeviceId(),
                                                chInfo.getIp());
                                    }

                                    // ready for new data
                                    chInfo.getChannel().read(buffer, chInfo, this);

                                    if (chInfo.isDeviceIdAlreadyReceived() || chInfo.isIpLocked()) {
                                        processCommandQueue(chInfo.getDeviceId());
                                    }
                                }

                                @Override
                                public void failed(Throwable t, SimpleBinaryIPChannelInfo chInfo) {
                                    if (t instanceof AsynchronousCloseException) {
                                        logger.debug("TCPserver - " + t.toString());
                                    } else {
                                        logger.warn("TCPserver - read exception: " + t.toString());
                                        closeChannel(chInfo);
                                    }
                                }
                            });
                            /*
                             * // "Hi" message to obtain device ID was not delivered?
                             * if (chInfo.getDeviceId() < 0) {
                             * logger.warn(
                             * "TCPserver - Channel IP={} - device does not have assigned ID. Configure device ID in binding configuration or implement sendHi() message into device. (sendHi must be called when device is connected) "
                             * ,
                             * chInfo.getIp());
                             * } else {
                             * // look for data to send
                             * processCommandQueue(chInfo.getDeviceId());
                             * }
                             */
                        }

                        @Override
                        public void failed(Throwable e, SimpleBinaryIPChannelInfoCollection a) {
                            StringWriter sw = new StringWriter();
                            PrintWriter pw = new PrintWriter(sw);
                            e.printStackTrace(pw);
                            logger.error("TCPserver - connection failed. " + sw.toString());
                        }
                    });

        } catch (UnknownHostException ex) {
            portState.setState(PortStates.NOT_AVAILABLE);

            logger.error("{} - address error", this.toString());

            return false;
        } catch (IOException ex) {
            portState.setState(PortStates.NOT_AVAILABLE);

            logger.error("{} - socket error: {}", this.toString(), ex.getMessage());

            return false;

        } catch (Exception ex) {
            portState.setState(PortStates.NOT_AVAILABLE);

            logger.error("{} - socket error: {}", this.toString(), ex.getMessage());

            return false;
        }

        if (logger.isInfoEnabled()) {
            logger.info("{} - channel listen for incoming connections", this.toString());
        }

        portState.setState(PortStates.LISTENING);
        connected = true;

        return true;
    }

    /**
     * Close socket
     *
     * @see org.openhab.binding.simplebinary.internal.SimpleBinaryIDevice#close()
     */
    @Override
    public void close() {
        if (listener != null) {
            try {
                listener.close();
            } catch (IOException e) {
                logger.error("{} close - IO error: {}", this.toString(), e.getMessage());
            }
            listener = null;
        }

        portState.setState(PortStates.CLOSED);
        devicesStates.setStateToAllConfiguredDevices(this.deviceName, DeviceStates.NOT_RESPONDING);
        connected = false;
    }

    /*
     * Can send if specific device does not wait for answer or is ready to write.
     */
    @Override
    protected boolean canSend(int devId) {
        for (SimpleBinaryIPChannelInfo c : channels) {
            if (c.getDeviceId() == devId) {
                return !(c.waitingForAnswer.get() || c.getChannel() == null || !c.getChannel().isOpen()
                        || !c.writeReady.get());
            }
        }

        return false;
    }

    /**
     * Write data into device stream
     *
     * @param data
     *            Item data with compiled packet
     */
    @Override
    protected boolean sendDataOut(SimpleBinaryItemData data) {

        return sendDataOut(data, channels.getById(data.getDeviceId()));
    }

    /**
     * Write data into device stream
     *
     * @param data
     *            Item data with compiled packet
     */
    protected boolean sendDataOut(SimpleBinaryItemData data, SimpleBinaryIPChannelInfo chInfo) {
        if (logger.isDebugEnabled()) {
            logger.debug("{} - Try to send data to device {} - {} bytes", this.toString(), data.getDeviceId(),
                    data.getData().length);
        }

        if (chInfo == null) {
            logger.warn("{} - Device {}: No channel found.", this.toString(), data.getDeviceId());
            return false;
        }

        if (chInfo.getChannel() == null) {
            logger.warn("{} - Device {}: Channel not ready.", this.toString(), data.getDeviceId());
            return false;
        }

        if (!chInfo.writeReady.compareAndSet(true, false)) {
            logger.warn("{} - Device {}: Write into channel is not ready.", this.toString(), data.getDeviceId());
            return false;
        }

        if (!chInfo.compareAndSetWaitingForAnswer()) {
            // if (logger.isDebugEnabled()) {
            logger.info("{} - Sending data to device {} discarted. Another send/wait is processed.", this.toString(),
                    data.getDeviceId());
            // }
            return false;
        } else {
            // write string to tcp channel

            ByteBuffer buffer = ByteBuffer.allocate(data.getData().length);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.put(data.getData());
            buffer.flip();

            chInfo.setWriteBuffer(buffer);
            chInfo.setLastSentData(data);

            // write into device
            chInfo.getChannel().write(buffer, chInfo, new CompletionHandler<Integer, SimpleBinaryIPChannelInfo>() {
                @Override
                public void completed(Integer result, final SimpleBinaryIPChannelInfo chInfo) {

                    if (result < 0) {
                        // if (logger.isDebugEnabled()) {
                        logger.info("TCPserver -  Channel {} disconnected", chInfo.getIp());
                        // }

                        closeChannel(chInfo);

                        return;
                    }

                    if (chInfo == null) {
                        return;
                    }

                    if (chInfo.getWriteBuffer().remaining() > 0) {
                        logger.info("TCPserver - Device {}/{} - Write rest {}/{}", chInfo.getDeviceId(), chInfo.getIp(),
                                chInfo.getWriteBuffer().remaining(), chInfo.getWriteBuffer().limit());
                        chInfo.getChannel().write(chInfo.getWriteBuffer(), chInfo, this);
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("TCPserver - Device {}/{} - Write finished", chInfo.getDeviceId(),
                                    chInfo.getIp());
                        }
                        chInfo.clearWriteBuffer();

                        chInfo.writeReady.set(true);
                    }
                }

                @Override
                public void failed(Throwable t, SimpleBinaryIPChannelInfo chInfo) {
                    logger.warn("TCPserver - " + t.toString());

                    closeChannel(chInfo);
                }
            });
        }

        return true;
    }

    private void closeChannel(final SimpleBinaryIPChannelInfo chInfo) {
        try {
            if (chInfo.getChannel() != null && chInfo.getChannel().isOpen()) {
                chInfo.getChannel().close();
            }
        } catch (Exception e) {
            logger.error("{} - Device {}/{} channel close exception: {}", toString(), chInfo.getDeviceId(),
                    chInfo.getIp(), e.getMessage());
        } finally {
            chInfo.closed();
            logger.info("{} - Device {}/{} was disconnected", toString(), chInfo.getDeviceId(), chInfo.getIp());
        }

        devicesStates.setDeviceState(deviceName, chInfo.getDeviceId(), DeviceStates.NOT_RESPONDING);
    }

    public void addDevice(String deviceID, String ipAddress, boolean isIpLocked) {
        if (logger.isDebugEnabled()) {
            logger.debug("{}: Adding TCP client configuration: {}-{}", toString(), Integer.parseInt(deviceID),
                    ipAddress);
        }
        if (channels != null) {
            channels.addConfiguredChannel(Integer.parseInt(deviceID), ipAddress, isIpLocked);
        }
    }

    /**
     * Check new data for all connected devices
     *
     */
    @Override
    public void checkNewData() {
        super.checkNewData();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.binding.simplebinary.internal.SimpleBinaryGenericDevice#checkConnectionTimeout()
     */
    @Override
    public void checkConnectionTimeout() {
        if (devicesStates == null || channels == null || channels.size() == 0) {
            return;
        }

        Calendar limitTime = Calendar.getInstance();
        limitTime.add(Calendar.SECOND, -60);

        for (Map.Entry<Integer, SimpleBinaryDeviceState> device : devicesStates.entrySet()) {
            Calendar c = device.getValue().getLastCommunication();
            if (c == null || c.after(limitTime)) {
                continue;
            }

            SimpleBinaryIPChannelInfo chInfo = channels.getById(device.getKey());
            if (chInfo != null && chInfo.getChannel() != null && chInfo.getChannel().isOpen()) {
                logger.debug("{} - Device {}/{} timeouted. Channel will be closed.", toString(), chInfo.getDeviceId(),
                        chInfo.getIp());
                closeChannel(chInfo);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.binding.simplebinary.internal.SimpleBinaryGenericDevice#toString()
     */
    @Override
    public String toString() {
        return deviceID + ":" + getPort();
    }
}
