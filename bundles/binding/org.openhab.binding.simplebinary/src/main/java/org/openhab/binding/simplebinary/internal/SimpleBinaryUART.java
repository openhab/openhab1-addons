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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.IOUtils;
import org.openhab.binding.simplebinary.internal.SimpleBinaryDeviceState.DeviceStates;
import org.openhab.binding.simplebinary.internal.SimpleBinaryPortState.PortStates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * Serial device class
 *
 * @author Vita Tucek
 * @since 1.9.0
 */
public class SimpleBinaryUART extends SimpleBinaryGenericDevice implements SerialPortEventListener {

    private static final Logger logger = LoggerFactory.getLogger(SimpleBinaryUART.class);
    /** Timeout for receiving data [ms] **/
    private static final int TIMEOUT = 2000;
    /** Time for data line stabilization after data receive [ms] **/
    private static final long LINE_STABILIZATION_TIME = 0;

    /** port baud rate */
    private int baud = 9600;

    /** port id */
    private CommPortIdentifier portId;
    /** port instance */
    private SerialPort serialPort;
    /** input data stream */
    private InputStream inputStream;
    /** output data stream */
    private OutputStream outputStream;

    /** buffer for incoming data */
    protected final SimpleBinaryByteBuffer inBuffer = new SimpleBinaryByteBuffer(256);
    /** store last sent data */
    protected SimpleBinaryItemData lastSentData = null;
    /** Last data receive time **/
    protected long receiveTime = 0;

    /** Flag indicating RTS signal will be handled */
    private boolean forceRTS;
    /** Flag indicating RTS signal will be handled on inverted logic */
    private boolean invertedRTS;
    /** Variable for count minimal time before reset RTS signal */
    private long sentTimeTicks = 0;

    /** timer measuring answer timeout */
    protected Timer timer = new Timer();
    protected TimerTask timeoutTask = null;

    /** flag waiting */
    protected AtomicBoolean waitingForAnswer = new AtomicBoolean(false);
    /** flag reading **/
    protected AtomicBoolean readingData = new AtomicBoolean(false);

    /**
     * Constructor
     *
     * @param deviceName
     * @param port
     * @param baud
     * @param simpleBinaryPoolControl
     * @param forceRTS
     * @param invertedRTS
     */
    public SimpleBinaryUART(String deviceName, String port, int baud, SimpleBinaryPoolControl simpleBinaryPoolControl,
            boolean forceRTS, boolean invertedRTS) {
        super(deviceName, port, simpleBinaryPoolControl);

        this.baud = baud;
        this.forceRTS = forceRTS;
        this.invertedRTS = invertedRTS;
    }

    /**
     * Return port hardware name
     *
     * @return
     */
    public String getPort() {
        return deviceID;
    }

    /**
     * Open serial port
     *
     * @see org.openhab.binding.simplebinary.internal.SimpleBinaryIDevice#open()
     */
    @Override
    public Boolean open() {
        if (logger.isDebugEnabled()) {
            logger.debug("{} - Opening", this.toString());
        }

        portState.setState(PortStates.CLOSED);
        // clear device states
        devicesStates.clear();
        // set initial state for configured devices
        devicesStates.setStateToAllConfiguredDevices(this.deviceName, DeviceStates.UNKNOWN);
        // reset connected state
        connected = false;
        setWaitingForAnswer(false);

        try {
            // get port ID
            portId = CommPortIdentifier.getPortIdentifier(this.deviceID);
        } catch (NoSuchPortException ex) {
            portState.setState(PortStates.NOT_EXIST);

            logger.warn("{} not found", this.toString());
            logger.warn("Available ports: " + getCommPortListString());

            return false;
        }

        if (portId != null) {
            // initialize serial port
            try {
                serialPort = portId.open("openHAB", 2000);
            } catch (PortInUseException e) {
                portState.setState(PortStates.NOT_AVAILABLE);

                logger.error("{} is in use", this.toString());

                this.close();
                return false;
            }

            try {
                inputStream = serialPort.getInputStream();
            } catch (IOException e) {
                logger.error("{} exception: {}", this.toString(), e.getMessage());

                this.close();
                return false;
            }

            try {
                // get the output stream
                outputStream = serialPort.getOutputStream();
            } catch (IOException e) {
                logger.error("{} exception:{}", this.toString(), e.getMessage());

                this.close();
                return false;
            }

            try {
                serialPort.addEventListener(this);
            } catch (TooManyListenersException e) {
                logger.error("{} exception:{}", this.toString(), e.getMessage());

                this.close();
                return false;
            }

            // activate the DATA_AVAILABLE notifier
            serialPort.notifyOnDataAvailable(true);
            if (this.forceRTS) {
                // OUTPUT_BUFFER_EMPTY
                serialPort.notifyOnOutputEmpty(true);
            }

            try {
                // set port parameters
                serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            } catch (UnsupportedCommOperationException e) {
                logger.error("{} exception: {}", this.toString(), e.getMessage());

                this.close();
                return false;
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("{} - opened", this.toString());
        }

        portState.setState(PortStates.LISTENING);
        connected = true;
        return true;
    }

    /**
     * Return list of available port
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    private String getCommPortListString() {
        StringBuilder sb = new StringBuilder();
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
            if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                sb.append(id.getName() + "\n");
            }
        }

        return sb.toString();
    }

    /**
     * Close serial port
     *
     * @see org.openhab.binding.simplebinary.internal.SimpleBinaryIDevice#close()
     */
    @Override
    public void close() {
        serialPort.removeEventListener();
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
        serialPort.close();

        portState.setState(PortStates.CLOSED);
        connected = false;
    }

    /**
     * Reconnect device
     */
    private void reconnect() {
        logger.info("{}: Trying to reconnect", this.toString());

        close();
        open();
    }

    /*
     * Can send if we are not waiting for answer
     */
    @Override
    protected boolean canSend() {
        return !waitingForAnswer.get();
    }

    /*
     * Can send if we are not waiting for answer.
     */
    @Override
    protected boolean canSend(int devId) {
        return canSend();
    }

    /**
     * Write data into device stream
     *
     * @param data
     *            Item data with compiled packet
     */
    @Override
    protected boolean sendDataOut(SimpleBinaryItemData data) {
        if (!this.connected) {
            logger.warn("{} - Port is closed. Unable to send data to device {}.", this.toString(), data.getDeviceId());
            return false;
        }

        // data line stabilization
        if (LINE_STABILIZATION_TIME > 0) {
            while (Math.abs(System.currentTimeMillis() - receiveTime) <= LINE_STABILIZATION_TIME) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {

                }
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("{} - Sending data to device {} with length {} bytes", this.toString(), data.getDeviceId(),
                    data.getData().length);
            logger.debug("{} - data: {}", this.toString(),
                    SimpleBinaryProtocol.arrayToString(data.getData(), data.getData().length));
        }

        if (compareAndSetWaitingForAnswer()) {
            try {
                // set RTS
                if (this.forceRTS) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("{} - RTS set", this.toString());
                    }

                    // calc minimum time for send + currentTime + 0ms
                    sentTimeTicks = System.currentTimeMillis() + (((data.getData().length * 8) + 4) * 1000 / this.baud);

                    serialPort.setRTS(invertedRTS ? false : true);
                }

                // write string to serial port
                outputStream.write(data.getData());
                outputStream.flush();

                setLastSentData(data);
            } catch (Exception e) {
                logger.error("{} - Error while writing", this.toString());

                reconnect();

                return false;
            }

            return true;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("{} - Sending data to device {} discarted. Another send/wait is processed.",
                        this.toString(), data.getDeviceId());
            }

            return false;
        }
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
     * Serial events
     *
     * @see gnu.io.SerialPortEventListener#serialEvent(gnu.io.SerialPortEvent)
     */
    @Override
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
                break;
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                // reset RTS
                if (this.forceRTS && (serialPort.isRTS() && !invertedRTS || !serialPort.isRTS() && invertedRTS)) {

                    // comply minimum sending time. Added because on ubuntu14.04 event OUTPUT_BUFFER_EMPTY is called
                    // periodically even before some data are send.
                    if (System.currentTimeMillis() > sentTimeTicks) {
                        serialPort.setRTS(invertedRTS);

                        if (logger.isDebugEnabled()) {
                            logger.debug("{} - RTS reset", this.toString());
                        }
                    }
                }
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                readingData.set(true);

                try {
                    while (inputStream.available() > 0) {
                        if (!readIncomingData()) {
                            break;
                        }
                    }

                    // check data
                    if (itemsConfig != null) {
                        // check minimum length
                        while (inBuffer.position() > 3) {
                            // check if received data has valid address (same as sent data)
                            if (lastSentData != null && !checkDeviceID(inBuffer, getLastSentData().getDeviceId())) {
                                logger.error("{} - Address not valid: received/sent={}/{}", this.toString(),
                                        getDeviceID(inBuffer), getLastSentData().getDeviceId());
                                // print details
                                printCommunicationInfo(inBuffer, lastSentData);
                                // clear buffer
                                inBuffer.clear();

                                logger.warn("{} - Address not valid: input buffer cleared", this.toString());

                                // set state
                                devicesStates.setDeviceState(this.deviceName, getLastSentData().getDeviceId(),
                                        DeviceStates.DATA_ERROR);

                                return;
                            }

                            int r = processData(inBuffer, getLastSentData());

                            if (r > 0 || r == ProcessDataResult.INVALID_CRC || r == ProcessDataResult.BAD_CONFIG
                                    || r == ProcessDataResult.NO_VALID_ADDRESS
                                    || r == ProcessDataResult.UNKNOWN_MESSAGE) {
                                // waiting for answer?
                                if (waitingForAnswer.get()) {
                                    // stop block sent
                                    setWaitingForAnswer(false);
                                }
                            } else if (r == ProcessDataResult.DATA_NOT_COMPLETED
                                    || r == ProcessDataResult.PROCESSING_ERROR) {
                                break;
                            }

                            // check for new data
                            while (inputStream.available() > 0) {
                                if (logger.isDebugEnabled()) {
                                    logger.debug("{} - another new data - {}bytes", this.toString(),
                                            inputStream.available());
                                }
                                if (!readIncomingData()) {
                                    break;
                                }
                            }
                        }
                    }
                    if (!waitingForAnswer.get()) {
                        // look for data to send
                        processCommandQueue();
                    }
                } catch (IOException e) {
                    logger.error("{} - Error receiving data: {}", toString(), e.getMessage());
                } catch (Exception ex) {
                    logger.error("{} - Exception receiving data: {}", toString(), ex.toString());
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    ex.printStackTrace(pw);
                    logger.error(sw.toString());
                } finally {
                    readingData.set(false);
                }
                break;
        }
    }

    /**
     * Read data from serial port buffer
     *
     * @throws IOException
     * @throws ModeChangeException
     */
    private boolean readIncomingData() throws IOException, ModeChangeException {

        byte[] readBuffer = new byte[32];
        int bytes = inputStream.read(readBuffer);

        receiveTime = System.currentTimeMillis();

        if (logger.isDebugEnabled()) {
            logger.debug("{} - received: {}", toString(), SimpleBinaryProtocol.arrayToString(readBuffer, bytes));
        }

        if (bytes + inBuffer.position() >= inBuffer.capacity()) {
            logger.error("{} - Buffer overrun", toString());
            return false;
        } else {
            inBuffer.put(readBuffer, 0, bytes);
        }

        return true;
    }

    /**
     * Check new data for all connected devices
     *
     */
    @Override
    public void checkNewData() {
        super.checkNewData();
    }

    @Override
    public String toString() {
        return "Port " + deviceID;
    }

    /**
     * Set waiting task for answer for slave device if waitingForAnswer not set.
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

    /**
     * Set / reset waiting task for answer for slave device
     *
     * @param state
     */
    protected void setWaitingForAnswer(boolean state) {
        waitingForAnswer.set(state);

        if (state) {
            if (timeoutTask != null) {
                timeoutTask.cancel();
            }

            timeoutTask = new TimerTask() {
                @Override
                public void run() {
                    timeoutTask = null;
                    dataTimeouted();
                }
            };

            timer.schedule(timeoutTask, TIMEOUT);
        } else {
            if (timeoutTask != null) {
                timeoutTask.cancel();
                timeoutTask = null;
            }
        }
    }

    /**
     * Method processed after waiting for answer is timeouted
     * @throws
     */
    protected void dataTimeouted() {
        int address = this.getLastSentData().getDeviceId();
        int timeout = 5;

        while (readingData.get() && timeout-- > 0) {
            logger.warn("{} - Device{} - Receiving data timeouted but reading still active. Thread={}", this.toString(),
                    address, Thread.currentThread().getId());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("{} - dataTimeouted() Thread.sleep() error. Thread={}", this.toString(),
                        Thread.currentThread().getId());
            }
        }

        if (!waitingForAnswer.get()) {
            return;
        }

        logger.warn("{} - Device{} - Receiving data timeouted. Thread={}", this.toString(), address,
                Thread.currentThread().getId());

        /*
         * for (Map.Entry<Thread, StackTraceElement[]> t : Thread.getAllStackTraces().entrySet()) {
         * String s = "";
         *
         * for (int i = 0; i < t.getValue().length; i++) {
         * s += "\n" + t.getValue()[i].getFileName() + "|" + t.getValue()[i].getMethodName() + "["
         * + t.getValue()[i].getLineNumber() + "]...";
         * if (i > 10) {
         * break;
         * }
         * }
         * logger.warn("{}-{}...", t.getKey(), s);
         * }
         */
        devicesStates.setDeviceState(this.deviceName, address, DeviceStates.NOT_RESPONDING);

        if (logger.isDebugEnabled()) {
            logger.debug("{} - Input buffer cleared", this.toString());
        }

        inBuffer.clear();

        setWaitingForAnswer(false);
        // new command will be sent if there is any
        processCommandQueue();
    }
}
