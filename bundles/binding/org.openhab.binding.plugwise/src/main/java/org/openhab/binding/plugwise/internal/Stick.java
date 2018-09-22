/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.internal;

import static org.openhab.binding.plugwise.internal.PlugwiseBinding.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.plugwise.PlugwiseCommandType;
import org.openhab.binding.plugwise.protocol.AcknowledgeMessage;
import org.openhab.binding.plugwise.protocol.AnnounceAwakeRequestMessage;
import org.openhab.binding.plugwise.protocol.BroadcastGroupSwitchResponseMessage;
import org.openhab.binding.plugwise.protocol.CalibrationResponseMessage;
import org.openhab.binding.plugwise.protocol.ClockGetResponseMessage;
import org.openhab.binding.plugwise.protocol.InformationResponseMessage;
import org.openhab.binding.plugwise.protocol.InitialiseRequestMessage;
import org.openhab.binding.plugwise.protocol.InitialiseResponseMessage;
import org.openhab.binding.plugwise.protocol.Message;
import org.openhab.binding.plugwise.protocol.MessageType;
import org.openhab.binding.plugwise.protocol.ModuleJoinedNetworkRequestMessage;
import org.openhab.binding.plugwise.protocol.NodeAvailableMessage;
import org.openhab.binding.plugwise.protocol.NodeAvailableResponseMessage;
import org.openhab.binding.plugwise.protocol.PowerBufferResponseMessage;
import org.openhab.binding.plugwise.protocol.PowerInformationResponseMessage;
import org.openhab.binding.plugwise.protocol.RealTimeClockGetResponseMessage;
import org.openhab.binding.plugwise.protocol.RoleCallResponseMessage;
import org.openhab.binding.plugwise.protocol.SenseReportRequestMessage;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * This class represents a Plugwise Stick that is connected to a serial port on the host.
 * This class borrows heavily from the Serial binding for the serial port communication
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class Stick extends PlugwiseDevice implements SerialPortEventListener {

    private static final Logger logger = LoggerFactory.getLogger(Stick.class);

    /** Plugwise protocol header code (hex) */
    private static final String PROTOCOL_HEADER = "\u0005\u0005\u0003\u0003";

    /** Carriage return */
    private static final char CR = '\r';
    /** Line feed */
    private static final char LF = '\n';
    /** Plugwise protocol trailer code (hex) */
    private static final String PROTOCOL_TRAILER = new String(new char[] { CR, LF });

    /** Matches Plugwise responses into the following groups: protocolHeader command sequence payload CRC */
    private static final Pattern RESPONSE_PATTERN = Pattern.compile("(.{4})(\\w{4})(\\w{4})(\\w*?)(\\w{4})");

    // Serial communication fields
    private String port;
    private CommPortIdentifier portId;
    private SerialPort serialPort;
    private WritableByteChannel outputChannel;
    private ByteBuffer readBuffer = ByteBuffer.allocate(maxBufferSize);
    private int previousByte = -1;

    // Queue fields
    private static int maxBufferSize = 1024;
    private final ReentrantLock sentQueueLock = new ReentrantLock();
    private BlockingQueue<Message> sendQueue = new ArrayBlockingQueue<Message>(maxBufferSize, true);
    private BlockingQueue<Message> prioritySendQueue = new ArrayBlockingQueue<Message>(maxBufferSize, true);
    private BlockingQueue<AcknowledgeMessage> acknowledgedQueue = new ArrayBlockingQueue<AcknowledgeMessage>(
            maxBufferSize, true);
    private BlockingQueue<Message> sentQueue = new ArrayBlockingQueue<Message>(maxBufferSize, true);
    private BlockingQueue<Message> receivedQueue = new ArrayBlockingQueue<Message>(maxBufferSize, true);

    // Background threads
    private final Thread sendThread;
    private final Thread processMessageThread;

    // Stick fields
    private boolean initialised = false;
    private final PlugwiseDeviceCache plugwiseDeviceCache = new PlugwiseDeviceCache();
    private PlugwiseBinding binding;
    /** default interval for sending messages on the ZigBee network */
    private int interval = 50;
    /** default maximum number of attempts to send a message */
    private int maxRetries = 1;

    public Stick(String port, PlugwiseBinding binding) {
        super("", PlugwiseDevice.DeviceType.Stick, "stick");
        this.port = port;
        this.binding = binding;
        plugwiseDeviceCache.add(this);

        sendThread = new SendThread(this);
        processMessageThread = new ProcessMessageThread(this);

        try {
            initialize();
        } catch (PlugwiseInitializationException e) {
            logger.error("Failed to initialize Plugwise stick: {}", e.getLocalizedMessage());
            initialised = false;
        }
    }

    protected void addDevice(PlugwiseDevice device) {
        plugwiseDeviceCache.add(device);
    }

    protected PlugwiseDevice getDevice(String id) {
        return plugwiseDeviceCache.get(id);
    }

    protected PlugwiseDevice getDeviceByMAC(String mac) {
        return plugwiseDeviceCache.getByMAC(mac);
    }

    protected PlugwiseDevice getDeviceByName(String name) {
        return plugwiseDeviceCache.getByName(name);
    }

    protected <T> List<T> getDevicesByClass(Class<T> deviceClass) {
        return plugwiseDeviceCache.getByClass(deviceClass);
    }

    public String getPort() {
        return port;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setRetries(int retries) {
        this.maxRetries = retries;
    }

    public boolean isInitialised() {
        return initialised;
    }

    /**
     * Initialize this device and open the serial port
     *
     * @throws PlugwiseInitializationException if port cannot be opened
     */
    @SuppressWarnings("rawtypes")
    private void initialize() throws PlugwiseInitializationException {

        // Flush the deviceCache
        plugwiseDeviceCache.clear();

        // parse ports and if the default port is found, initialized the reader
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
            if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (id.getName().equals(port)) {
                    logger.debug("Serial port '{}' has been found.", port);
                    portId = id;
                }
            }
        }
        if (portId != null) {
            // initialize serial port
            try {
                serialPort = portId.open("openHAB", 2000);
            } catch (PortInUseException e) {
                throw new PlugwiseInitializationException(e);
            }

            try {
                serialPort.addEventListener(this);
            } catch (TooManyListenersException e) {
                throw new PlugwiseInitializationException(e);
            }

            // activate the DATA_AVAILABLE notifier
            serialPort.notifyOnDataAvailable(true);

            try {
                // set port parameters
                serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
            } catch (UnsupportedCommOperationException e) {
                throw new PlugwiseInitializationException(e);
            }

            try {
                // get the output stream
                outputChannel = Channels.newChannel(serialPort.getOutputStream());
            } catch (IOException e) {
                throw new PlugwiseInitializationException(e);
            }
        } else {
            StringBuilder sb = new StringBuilder();
            portList = CommPortIdentifier.getPortIdentifiers();
            while (portList.hasMoreElements()) {
                CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
                if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    sb.append(id.getName() + "\n");
                }
            }
            throw new PlugwiseInitializationException(
                    "Serial port '" + port + "' could not be found. Available ports are:\n" + sb);
        }

        initialised = true;

        // initialise the Stick
        sendMessage(new InitialiseRequestMessage());
    }

    public void startBackgroundThreads() {
        sendThread.start();
        processMessageThread.start();
    }

    /**
     * Close this serial device associated with the Stick
     */
    public void close() {
        stopBackgroundThread(sendThread);
        stopBackgroundThread(processMessageThread);

        serialPort.removeEventListener();
        try {
            IOUtils.closeQuietly(serialPort.getInputStream());
            IOUtils.closeQuietly(serialPort.getOutputStream());
            serialPort.close();
        } catch (IOException e) {
            logger.error("An exception occurred while closing the serial port {} ({})", serialPort, e.getMessage());
        }

        initialised = false;
    }

    private static void stopBackgroundThread(Thread thread) {
        thread.interrupt();

        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
    }

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
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                // we get here if data has been received
                try {
                    // read data from serial device
                    while (serialPort.getInputStream().available() > 0) {
                        int currentByte = serialPort.getInputStream().read();
                        // Plugwise sends ASCII data, but for some unknown reason we sometimes get data with unsigned
                        // byte value >127 which in itself is very strange. We filter these out for the time being
                        if (currentByte < 128) {
                            readBuffer.put((byte) currentByte);
                            if (previousByte == CR && currentByte == LF) {
                                readBuffer.flip();
                                parseAndQueue(readBuffer);
                                readBuffer.clear();
                                previousByte = -1;
                            } else {
                                previousByte = currentByte;
                            }
                        }
                    }
                } catch (IOException e) {
                    logger.debug("Error receiving data on serial port {}: {}", port, e.getMessage());
                }
                break;
        }
    }

    public void sendMessage(Message message) {
        if (message != null && isInitialised()) {
            try {
                logger.debug("Adding to sendQueue: {}", message);
                sendQueue.put(message);
            } catch (InterruptedException e) {
                logger.error("Interrupted while adding to sendQueue: {}", message);
            }
        }
    }

    public void sendPriorityMessage(Message message) {
        if (message != null && isInitialised()) {
            try {
                logger.debug("Adding to prioritySendQueue: {}", message);
                prioritySendQueue.put(message);
            } catch (InterruptedException e) {
                logger.error("Interrupted while adding to prioritySendQueue: {}", message);
            }
        }
    }

    @Override
    public boolean postUpdate(String MAC, PlugwiseCommandType type, Object value) {
        if (MAC != null && type != null && value != null) {
            binding.postUpdate(MAC, type, value);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Parse a buffer into a Message and put it in the appropriate queue for further processing
     *
     * @param readBuffer - the string to parse
     */
    private void parseAndQueue(ByteBuffer readBuffer) {
        if (readBuffer != null) {

            String response = new String(readBuffer.array(), 0, readBuffer.limit());
            response = StringUtils.chomp(response);

            Matcher matcher = RESPONSE_PATTERN.matcher(response);

            if (matcher.matches()) {

                String protocolHeader = matcher.group(1);
                String command = matcher.group(2);
                String sequence = matcher.group(3);
                String payload = matcher.group(4);
                String CRC = matcher.group(5);

                if (protocolHeader.equals(PROTOCOL_HEADER)) {
                    String calculatedCRC = getCRCFromString(command + sequence + payload);
                    if (calculatedCRC.equals(CRC)) {

                        int messageTypeNumber = Integer.parseInt(command, 16);
                        MessageType messageType = MessageType.forValue(messageTypeNumber);
                        int sequenceNumber = Integer.parseInt(sequence, 16);

                        if (messageType == null) {
                            logger.debug("Received unrecognized messageTypeNumber:{} command:{} sequence:{} payload:{}",
                                    messageTypeNumber, command, sequenceNumber, payload);
                            return;
                        }

                        logger.debug("Received message: command:{} sequence:{} payload:{}", messageType, sequenceNumber,
                                payload);

                        Message message = createMessage(messageType, command, sequenceNumber, payload);

                        try {
                            if (message instanceof AcknowledgeMessage && !((AcknowledgeMessage) message).isExtended()) {
                                logger.debug("Adding to acknowledgedQueue: {}", message);
                                acknowledgedQueue.put((AcknowledgeMessage) message);
                            } else {
                                logger.debug("Adding to receivedQueue: {}", message);
                                receivedQueue.put(message);
                            }
                        } catch (InterruptedException e) {
                            Thread.interrupted();
                        }
                    } else {
                        logger.error("Plugwise protocol CRC error: {} does not match {} in message", calculatedCRC,
                                CRC);
                    }
                } else {
                    logger.debug("Plugwise protocol header error: {} in message {}", protocolHeader, response);
                }
            } else {
                if (!response.contains("APSRequestNodeInfo")) {
                    logger.error("Plugwise protocol message error: {}", response);
                }
            }
        }
    }

    private Message createMessage(MessageType messageType, String command, int sequenceNumber, String payLoad) {
        switch (messageType) {
            case ACKNOWLEDGEMENT:
                return new AcknowledgeMessage(sequenceNumber, payLoad);
            case NODE_AVAILABLE:
                return new NodeAvailableMessage(sequenceNumber, payLoad);
            case INITIALISE_RESPONSE:
                return new InitialiseResponseMessage(sequenceNumber, payLoad);
            case DEVICE_ROLECALL_RESPONSE:
                return new RoleCallResponseMessage(sequenceNumber, payLoad);
            case DEVICE_CALIBRATION_RESPONSE:
                return new CalibrationResponseMessage(sequenceNumber, payLoad);
            case DEVICE_INFORMATION_RESPONSE:
                return new InformationResponseMessage(sequenceNumber, payLoad);
            case REALTIMECLOCK_GET_RESPONSE:
                return new RealTimeClockGetResponseMessage(sequenceNumber, payLoad);
            case CLOCK_GET_RESPONSE:
                return new ClockGetResponseMessage(sequenceNumber, payLoad);
            case POWER_BUFFER_RESPONSE:
                return new PowerBufferResponseMessage(sequenceNumber, payLoad);
            case POWER_INFORMATION_RESPONSE:
                return new PowerInformationResponseMessage(sequenceNumber, payLoad);
            case ANNOUNCE_AWAKE_REQUEST:
                return new AnnounceAwakeRequestMessage(sequenceNumber, payLoad);
            case BROADCAST_GROUP_SWITCH_RESPONSE:
                return new BroadcastGroupSwitchResponseMessage(sequenceNumber, payLoad);
            case MODULE_JOINED_NETWORK_REQUEST:
                return new ModuleJoinedNetworkRequestMessage(sequenceNumber, payLoad);
            case SENSE_REPORT_REQUEST:
                return new SenseReportRequestMessage(sequenceNumber, payLoad);
            default:
                logger.debug("Received unrecognized command: {}", command);
                return null;
        }
    }

    @Override
    public boolean processMessage(Message message) {

        if (message != null) {
            // deal with the messages that are destined to a very specific plugwise device, and only if we already have
            // a reference to them
            switch (message.getType()) {

                case ACKNOWLEDGEMENT:
                    if (((AcknowledgeMessage) message).isExtended()) {

                        String cpMAC = ((AcknowledgeMessage) message).getCirclePlusMAC();
                        switch (((AcknowledgeMessage) message).getExtensionCode()) {

                            case CIRCLEPLUS:
                                CirclePlus cp = (CirclePlus) getDeviceByMAC(cpMAC);
                                if (!cpMAC.equals("") && cp == null) {
                                    cp = new CirclePlus(cpMAC, this, cpMAC);
                                    plugwiseDeviceCache.add(cp);
                                    logger.debug("Added a CirclePlus with MAC {} to the cache", cp.getMAC());
                                }

                                if (cp != null) {
                                    cp.updateInformation();
                                    cp.calibrate();
                                    cp.setClock();
                                    // initiate a "role call" request in the network
                                    cp.roleCall(0);
                                }
                                break;
                            case TIMEOUT:

                                // Get the original request message from sentQueue and resend it
                                logger.error("Received timeout ack for: {}", message);

                                Message sentMessage = null;
                                sentQueueLock.lock();
                                try {
                                    Iterator<Message> messageIterator = sentQueue.iterator();
                                    while (messageIterator.hasNext()) {
                                        sentMessage = messageIterator.next();
                                        if (sentMessage.getSequenceNumber() == message.getSequenceNumber()) {
                                            logger.debug("Timeout: removing from the sentQueue: {}", sentMessage);
                                            sentQueue.remove(sentMessage);
                                            break;
                                        }
                                    }
                                } finally {
                                    sentQueueLock.unlock();
                                }

                                if (sentMessage != null) {
                                    sentMessage.setSequenceNumber(0);
                                    sendMessage(sentMessage);
                                }

                                return false;

                            case ON:
                                // Protocol Reverse Engineering: We have to decide whether we trust the ACK messages
                                // sent back to the Stick or not.
                                // If we do, then uncomment this line. If not, we will rely on a formal
                                // DEVICE_INFORMATION_REQUEST to get
                                // the real state of the Circle(+)
                                // postUpdate(((AcknowledgeMessage)message).getExtendedMAC(),
                                // PlugwiseCommandType.CURRENTSTATE, ((AcknowledgeMessage)message).isOn());

                                break;

                            case OFF:
                                // Protocol Reverse Engineering: : Idem as in ON
                                // postUpdate(((AcknowledgeMessage)message).getExtendedMAC(),
                                // PlugwiseCommandType.CURRENTSTATE, ((AcknowledgeMessage)message).isOff());

                                break;

                            default:
                                logger.debug("Plugwise Unknown Acknowledgement message Extension");
                                break;

                        }

                    }
                    return true;

                case INITIALISE_RESPONSE:
                    MAC = ((InitialiseResponseMessage) message).getMAC();
                    initialised = true;

                    // is the network online?
                    if (((InitialiseResponseMessage) message).isOnline()) {

                        String cpMAC = ((InitialiseResponseMessage) message).getCirclePlusMAC();
                        CirclePlus cp = (CirclePlus) getDeviceByMAC(cpMAC);
                        if (!cpMAC.equals("") && cp == null) {
                            cp = new CirclePlus(cpMAC, this, cpMAC);
                            plugwiseDeviceCache.add(cp);
                            logger.debug("Added a CirclePlus with MAC {} to the cache", cp.getMAC());
                        }

                        if (cp != null) {
                            cp.updateInformation();
                            cp.calibrate();
                            cp.setClock();
                            // initiate a "role call" request in the network
                            cp.roleCall(0);
                        }
                    } else {
                        logger.debug("The network is not online. nothing to do here");
                    }
                    return true;

                case NODE_AVAILABLE:
                    String node = ((NodeAvailableMessage) message).getMAC();

                    Circle someCircle = (Circle) getDeviceByMAC(node);
                    if (someCircle == null) {
                        Circle newCircle = new Circle(node, this, node);
                        plugwiseDeviceCache.add(newCircle);

                        // confirm to the new node that it is added to the network
                        NodeAvailableResponseMessage response = new NodeAvailableResponseMessage(true, node);
                        sendMessage(response);

                        newCircle.updateInformation();
                        newCircle.calibrate();
                    }

                    return true;

                default:
                    return super.processMessage(message);
            }
        }
        return false;
    }

    private String getCRCFromString(String buffer) {

        int crc = 0x0000;
        int polynomial = 0x1021; // 0001 0000 0010 0001 (0, 5, 12)

        byte[] bytes = new byte[0];
        try {
            bytes = buffer.getBytes("ASCII");
        } catch (UnsupportedEncodingException e) {
            logger.debug("Could not fetch ASCII bytes from String ", buffer);
        }

        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }

        crc &= 0xffff;

        return (String.format("%04X", crc).toUpperCase());
    }

    private static class SendThread extends Thread {
        private final Stick stick;

        public SendThread(Stick stick) {
            super("Plugwise SendThread");
            this.stick = stick;
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Message message = stick.prioritySendQueue.poll();

                    if (message == null) {
                        message = stick.sendQueue.poll(100, TimeUnit.MILLISECONDS);
                    }

                    if (message == null) {
                        continue;
                    }

                    sendMessage(message);
                    sleep(stick.interval);
                } catch (InterruptedException e) {
                    // That's our signal to stop
                    break;
                } catch (Exception e) {
                    logger.error("SendJob: unexpected error", e);
                }
            }
        }

        private void sendMessage(Message message) throws InterruptedException {
            if (message.getAttempts() < stick.maxRetries) {
                message.increaseAttempts();

                String messageHexString = message.toHexString();
                String packetString = PROTOCOL_HEADER + messageHexString + PROTOCOL_TRAILER;
                ByteBuffer bytebuffer = ByteBuffer.allocate(packetString.length());
                bytebuffer.put(packetString.getBytes());
                bytebuffer.rewind();

                try {
                    logger.debug("Sending: {} as {}", message, messageHexString);
                    stick.outputChannel.write(bytebuffer);
                } catch (IOException e) {
                    logger.error("Error writing '{}' to serial port {}: {}", packetString, stick.port, e.getMessage());
                    return;
                }

                // Poll the acknowledgement message for at most 1 second, normally it is received within 75ms
                AcknowledgeMessage ack = stick.acknowledgedQueue.poll(1, TimeUnit.SECONDS);
                logger.debug("Removing from acknowledgedQueue: {}", ack);

                if (ack == null) {
                    logger.error("Error sending: No ACK received after 1 second: {}", packetString);
                } else if (!ack.isSuccess()) {
                    if (ack.isError()) {
                        logger.error("Error sending: Negative ACK: {}", packetString);
                    }
                } else {
                    // update the sent message with the new sequence number
                    message.setSequenceNumber(ack.getSequenceNumber());

                    // place the sent message in the sent Q
                    logger.debug("Adding to sentQueue: {}", message);
                    stick.sentQueueLock.lock();
                    try {
                        if (stick.sentQueue.size() == maxBufferSize) {
                            // For some @#$@#$ reason plugwise devices, or the Stick, does not send responses
                            // to Requests. They clog the sentQueue. Let's flush some part of the queue
                            Message someMessage = stick.sentQueue.poll();
                            logger.debug("Flushing from sentQueue: {}", someMessage);

                        }
                        stick.sentQueue.put(message);
                    } finally {
                        stick.sentQueueLock.unlock();
                    }
                }
            } else {
                // max attempts reached
                // we give up, and to a network reset
                logger.error(
                        "Giving finally up on Plugwise protocol data unit after attempts: {} MAC:{} command:{} sequence:{} payload:{}",
                        message.getAttempts(), message.getMAC(), message.getType(), message.getSequenceNumber(),
                        message.getPayLoad());
            }
        }
    }

    private static class ProcessMessageThread extends Thread {
        private Stick stick;

        public ProcessMessageThread(Stick stick) {
            super("Plugwise ProcessMessageThread");
            this.stick = stick;
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Message message = stick.receivedQueue.take();
                    processMessage(message);
                } catch (InterruptedException e) {
                    // That's our signal to stop
                    break;
                } catch (Exception e) {
                    logger.error("SendJob: unexpected error", e);
                }
            }
        }

        private void processMessage(Message message) {
            PlugwiseDevice target = stick.getDeviceByMAC(message.getMAC());
            boolean result = false;

            if (target != null) {
                result = target.processMessage(message);
            } else {
                // if we cannot find the target MAC for this message, we let the stick deal with it
                result = stick.processMessage(message);
            }

            // after processing the response to a message, we remove any reference to the original request
            // stored in the sentQueue
            // WARNING: We assume that each request sent out can only be followed bye EXACTLY ONE response - so
            // far it seems that the PW protocol is operating in that way

            if (result) {
                stick.sentQueueLock.lock();
                try {
                    Iterator<Message> messageIterator = stick.sentQueue.iterator();
                    while (messageIterator.hasNext()) {
                        Message sentMessage = messageIterator.next();
                        if (sentMessage.getSequenceNumber() == message.getSequenceNumber()) {
                            logger.debug("Removing from sentQueue: {}", sentMessage);
                            stick.sentQueue.remove(sentMessage);
                            break;
                        }
                    }
                } finally {
                    stick.sentQueueLock.unlock();
                }
            }
        }
    }

    public static abstract class AbstractPlugwiseDeviceJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {

            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            Stick theStick = (Stick) dataMap.get(STICK_JOB_DATA_KEY);

            if (theStick.isInitialised()) {
                String MAC = (String) dataMap.get(MAC_JOB_DATA_KEY);
                PlugwiseDevice device = theStick.getDeviceByMAC(MAC);
                if (device != null) {
                    executeDeviceJob(device);
                }
            }
        }

        abstract protected void executeDeviceJob(PlugwiseDevice device);
    }

    public static class PowerInformationJob extends AbstractPlugwiseDeviceJob {
        @Override
        protected void executeDeviceJob(PlugwiseDevice device) {
            if (device instanceof Circle) {
                ((Circle) device).updateCurrentEnergy();
            }
        }
    }

    public static class PowerBufferJob extends AbstractPlugwiseDeviceJob {
        @Override
        protected void executeDeviceJob(PlugwiseDevice device) {
            if (device instanceof Circle) {
                ((Circle) device).updateEnergy(false);
            }
        }
    }

    public static class ClockJob extends AbstractPlugwiseDeviceJob {
        @Override
        protected void executeDeviceJob(PlugwiseDevice device) {
            if (device instanceof Circle) {
                ((Circle) device).updateSystemClock();
            }
        }
    }

    public static class RealTimeClockJob extends AbstractPlugwiseDeviceJob {
        @Override
        protected void executeDeviceJob(PlugwiseDevice device) {
            if (device instanceof CirclePlus) {
                ((CirclePlus) device).updateRealTimeClock();
            }
        }
    }

    public static class InformationJob extends AbstractPlugwiseDeviceJob {
        @Override
        protected void executeDeviceJob(PlugwiseDevice device) {
            if (device instanceof Circle) {
                ((Circle) device).updateInformation();
            }
        }
    }
}
