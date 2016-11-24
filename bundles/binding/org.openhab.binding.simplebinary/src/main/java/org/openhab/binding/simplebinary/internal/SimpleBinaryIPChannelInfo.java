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
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Channel info
 *
 * @author Vita Tucek
 * @since 1.9.0
 */
public class SimpleBinaryIPChannelInfo {
    private static final Logger logger = LoggerFactory.getLogger(SimpleBinaryIPChannelInfo.class);

    private AsynchronousSocketChannel channel = null;
    private SimpleBinaryByteBuffer buffer = null;
    private final SimpleBinaryIPChannelInfoCollection collection;
    private InetSocketAddress address = null;
    private ByteBuffer writeBuffer = null;
    private SimpleBinaryItemData lastSentData = null;

    private int configuredDeviceID = -1;
    private int receivedDeviceID = -1;
    private String configuredDeviceIP = "";

    private final boolean isIpLocked;

    /** timer measuring answer timeout */
    protected Timer timer = new Timer();
    protected TimeoutTask timeoutTask = null;
    /** flag waiting */
    protected AtomicBoolean waitingForAnswer = new AtomicBoolean(false);

    private SimpleBinaryIRequestTimeouted requestTimeouted;

    /**
     * Constructor to add new channel after connect
     *
     * @param channel
     * @param buffer
     * @param collection
     * @param timeoutEvent
     */
    public SimpleBinaryIPChannelInfo(AsynchronousSocketChannel channel, ByteBuffer buffer,
            SimpleBinaryIPChannelInfoCollection collection, SimpleBinaryIRequestTimeouted timeoutEvent) {

        this.collection = collection;
        this.requestTimeouted = timeoutEvent;

        assignChannel(channel, buffer, timeoutEvent);

        // set address for use when device reconnected
        // this.configuredDeviceIP = getIp();

        this.isIpLocked = false;
    }

    /**
     * Constructor to add configuration record
     *
     * @param deviceID
     * @param ipAddress
     * @param isIpLocked
     * @param collection
     */
    public SimpleBinaryIPChannelInfo(int deviceID, String ipAddress, boolean isIpLocked,
            SimpleBinaryIPChannelInfoCollection collection) {
        this.collection = collection;

        this.configuredDeviceID = deviceID;
        this.configuredDeviceIP = ipAddress;

        this.isIpLocked = isIpLocked;
    }

    public void assignChannel(AsynchronousSocketChannel channel, ByteBuffer buffer,
            SimpleBinaryIRequestTimeouted timeoutEvent) {
        this.channel = channel;
        this.buffer = new SimpleBinaryByteBuffer(buffer);
        this.requestTimeouted = timeoutEvent;

        // get connected channel address
        address = retrieveAddress(this.channel);
    }

    public static InetSocketAddress retrieveAddress(AsynchronousSocketChannel channel) {
        SocketAddress a = null;

        try {
            a = channel.getRemoteAddress();
        } catch (IOException e) {
            logger.error("retrieveAddress channel exception: {}", e.getMessage());
        }

        if (a != null && (a instanceof InetSocketAddress)) {
            return ((InetSocketAddress) a);
        }

        return null;
    }

    public String getHostName() {
        if (address != null) {
            return address.getHostName();
        } else {
            return "";
        }
    }

    public String getIp() {
        if (address != null) {
            return address.getAddress().getHostAddress();
        } else {
            return configuredDeviceIP;
        }
    }

    public String getIpConfigured() {
        return configuredDeviceIP;
    }

    public boolean hasIpConfigured() {
        return configuredDeviceIP.length() > 0;
    }

    public boolean hasIdConfigured() {
        return configuredDeviceID != -1;
    }

    public String getIpReceived() {
        if (address != null) {
            return address.getAddress().getHostAddress();
        } else {
            return null;
        }
    }

    public int getPort() {
        if (address != null) {
            return address.getPort();
        } else {
            return -1;
        }
    }

    public int getDeviceId() {
        if (receivedDeviceID >= 0) {
            return receivedDeviceID;
        }

        return configuredDeviceID;
    }

    public int getDeviceIdConfigured() {
        return configuredDeviceID;
    }

    public int getDeviceIdReceived() {
        return receivedDeviceID;
    }

    public boolean isDeviceIdAlreadyReceived() {
        return receivedDeviceID != -1;
    }

    public SimpleBinaryByteBuffer getBuffer() {
        return buffer;
    }

    public AsynchronousSocketChannel getChannel() {
        return channel;
    }

    public SimpleBinaryIPChannelInfoCollection getCollection() {
        return collection;
    }

    public void remove() {
        collection.remove(this);
    }

    /**
     * Mark channel as closed
     */
    public void closed() {
        clearWaitingForAnswer();

        channel = null;
        buffer = null;
        writeBuffer = null;
        requestTimeouted = null;
        // TODO:
    }

    public ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }

    public void setWriteBuffer(ByteBuffer buffer) {
        writeBuffer = buffer;
    }

    public void clearWriteBuffer() {
        writeBuffer = null;
    }

    /**
     * Return last sent message
     *
     */
    public SimpleBinaryItemData getLastSentData() {
        return lastSentData;
    }

    /**
     * Set last sent data
     *
     */
    public void setLastSentData(SimpleBinaryItemData data) {
        lastSentData = data;
    }

    /**
     * Set / reset waiting task for answer for slave device
     *
     * @param state
     */
    private void setWaitingForAnswer(boolean state) {
        waitingForAnswer.set(state);

        if (state) {
            if (timeoutTask != null) {
                timeoutTask.cancel();
            }

            timeoutTask = new TimeoutTask(this, requestTimeouted) {
                @Override
                public void run() {
                    timeoutTask = null;
                    // dataTimeouted
                    if (this.event != null) {
                        this.event.timeoutEvent(this.chInfo);
                    }

                    clearWaitingForAnswer();
                }
            };

            timer.schedule(timeoutTask, TimeoutTask.TIMEOUT);
        } else {
            if (timeoutTask != null) {
                timeoutTask.cancel();
                timeoutTask = null;
            }
        }
    }

    /**
     * Set waiting task for answer for target device if waitingForAnswer not set.
     * Return true if flag is set
     *
     */
    protected boolean compareAndSetWaitingForAnswer() {
        if (waitingForAnswer.compareAndSet(false, true)) {
            setWaitingForAnswer(true);

            return true;
        } else {
            return false;
        }
    }

    protected void clearWaitingForAnswer() {
        setWaitingForAnswer(false);
    }

    public boolean isIpLocked() {
        return isIpLocked;
    }

    /**
     * Assign device id for communication
     *
     * @param devId
     * @return True if successfully assigned false if other device use this id or configured is different
     */
    public boolean assignDeviceId(int devId) {
        receivedDeviceID = devId;

        if (logger.isDebugEnabled()) {
            logger.debug("Device {} (configured IP={},configured ID={}) assigned to ID={}", this.getIpReceived(),
                    this.getIpConfigured(), this.getDeviceIdConfigured(), devId);
        }

        if (isIpLocked) {
            if (logger.isDebugEnabled()) {
                logger.debug("Device {} locked. No assign needed.", this.getIpReceived());
            }
            return true;
        }

        if (collection != null) {

            // --------------- TODO remove this
            if (logger.isDebugEnabled()) {
                logger.debug("collection size={}", collection.size());

                String collectioToString = "";

                for (SimpleBinaryIPChannelInfo i : collection) {
                    if (collectioToString.length() > 0) {
                        collectioToString += ",";
                    }
                    collectioToString += i.getDeviceId() + "/" + i.getIp() + "/"
                            + (i.getChannel() == null ? "no" : (i.getChannel().isOpen() ? "open" : "closed"));
                }

                logger.debug("collection is={}", collectioToString);
            }
            // ----------------------------

            // remove duplicate channels in collection and check for already used IDs
            for (Iterator<SimpleBinaryIPChannelInfo> itr = collection.iterator(); itr.hasNext();) {
                SimpleBinaryIPChannelInfo item = itr.next();

                if (!item.equals(this)) {
                    if (item.getChannel() == null) {
                        if (item.getDeviceIdReceived() == devId
                                || (item.getDeviceIdReceived() == -1 && item.getDeviceIdConfigured() == devId)) {

                            if (item.getDeviceIdConfigured() >= 0) {
                                configuredDeviceID = item.getDeviceIdConfigured();
                            }
                            if (item.getIpConfigured().length() > 0) {
                                configuredDeviceIP = item.getIpConfigured();
                            }

                            if (logger.isDebugEnabled()) {
                                logger.debug("Device {} - ID assigning. Old channel record removed(ID={},IP={})",
                                        this.getIpReceived(), item.getDeviceId(), item.getIp());
                            }

                            itr.remove();
                        }
                    } else {
                        if (item.getDeviceIdReceived() == devId) {
                            if (item.getIp().equals(this.getIp())) {
                                if (item.getDeviceIdConfigured() >= 0) {
                                    configuredDeviceID = item.getDeviceIdConfigured();
                                }
                                if (item.getIpConfigured().length() > 0) {
                                    configuredDeviceIP = item.getIpConfigured();
                                }

                                item.closed();
                                itr.remove();

                                if (logger.isDebugEnabled()) {
                                    logger.debug(
                                            "Device {} - ID assigning. Same channel exist(ID={},IP={}). Channel will be closed and removed",
                                            this.getIpReceived(), item.getDeviceId(), item.getIp());
                                }
                            } else {
                                // already exist device with same ID and is also connected -> problem
                                return false;
                            }
                        } else {
                            if (item.getIp().equals(this.getIp())) {
                                if (logger.isDebugEnabled()) {
                                    logger.warn("Device {} - ID assigning. Channel with same IP exist(ID={},IP={}).",
                                            this.getIpReceived(), item.getDeviceId(), item.getIp());
                                }
                            }
                        }
                    }
                }
            }
        }

        // --------------- TODO remove this
        String collectioToString = "";

        for (SimpleBinaryIPChannelInfo i : collection) {
            if (collectioToString.length() > 0) {
                collectioToString += ",";
            }
            collectioToString += i.getDeviceId() + "/" + i.getIp();
        }

        logger.debug("collection after remove is={}", collectioToString);
        // ----------------------

        return true;
    }

    public boolean hasIpMismatch() {
        if (configuredDeviceIP.length() == 0) {
            return false;
        }

        if (!configuredDeviceIP.equals(getIpReceived())) {
            return true;
        }

        return false;
    }

    public boolean hasIdMismatch() {
        if (configuredDeviceID < 0) {
            return false;
        }

        if (configuredDeviceID != getDeviceIdReceived()) {
            return true;
        }

        return false;
    }

    /**
     * TimerTask for timeout event
     *
     * @author Vita Tucek
     * @since 1.9.0
     *
     */
    class TimeoutTask extends TimerTask {

        /** Timeout for receiving data [ms] **/
        public static final int TIMEOUT = 2000;
        /** Channel info **/
        public final SimpleBinaryIPChannelInfo chInfo;
        /** Event **/
        public final SimpleBinaryIRequestTimeouted event;

        TimeoutTask(SimpleBinaryIPChannelInfo chInfo, SimpleBinaryIRequestTimeouted event) {
            this.chInfo = chInfo;
            this.event = event;
        }

        @Override
        public void run() {

        }
    }
}
