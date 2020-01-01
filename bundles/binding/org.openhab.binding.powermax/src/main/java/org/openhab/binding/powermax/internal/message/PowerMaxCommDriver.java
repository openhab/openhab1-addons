/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.powermax.internal.message;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.powermax.internal.connector.PowerMaxConnector;
import org.openhab.binding.powermax.internal.connector.PowerMaxEventListener;
import org.openhab.binding.powermax.internal.connector.PowerMaxSerialConnector;
import org.openhab.binding.powermax.internal.connector.PowerMaxTcpConnector;
import org.openhab.binding.powermax.internal.state.PowerMaxPanelSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that manages the communication with the Visonic alarm system
 *
 * Visonic does not provide a specification of the RS232 protocol and, thus,
 * the binding uses the available protocol specification given at the ​domoticaforum
 * http://www.domoticaforum.eu/viewtopic.php?f=68&t=6581
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxCommDriver {

    private static final Logger logger = LoggerFactory.getLogger(PowerMaxCommDriver.class);

    private static final int DEFAULT_TCP_PORT = 80;
    private static final int TCP_CONNECTION_TIMEOUT = 5000;
    private static final int DEFAULT_BAUD_RATE = 9600;
    private static final int WAITING_DELAY_FOR_RESPONSE = 750;

    /** The unique instance of this class */
    private static PowerMaxCommDriver theCommDriver = null;

    /** The serial or TCP connecter used to communicate with the PowerMax alarm system */
    private PowerMaxConnector connector = null;

    /** The last message sent to the the PowerMax alarm system */
    private PowerMaxBaseMessage lastSendMsg = null;

    /** The message queue of messages to be sent to the the PowerMax alarm system */
    private ConcurrentLinkedQueue<PowerMaxBaseMessage> msgQueue = new ConcurrentLinkedQueue<PowerMaxBaseMessage>();

    private boolean downloadRunning = false;

    /** The time in milliseconds used to set time and date */
    private Long syncTimeCheck = null;

    /**
     * Constructor for Serial Connection
     *
     * @param sPort
     *            the serial port name
     */
    private PowerMaxCommDriver(String sPort) {
        String serialPort = StringUtils.isNotBlank(sPort) ? sPort : null;
        if (serialPort != null) {
            connector = new PowerMaxSerialConnector(serialPort, DEFAULT_BAUD_RATE);
        } else {
            connector = null;
        }
    }

    /**
     * Constructor for TCP connection
     *
     * @param ip
     *            the IP address
     * @param port
     *            TCP port number; default port is used if value <= 0
     */
    private PowerMaxCommDriver(String ip, int port) {
        String ipAddress = StringUtils.isNotBlank(ip) ? ip : null;
        int tcpPort = (port > 0) ? port : DEFAULT_TCP_PORT;
        if (ipAddress != null) {
            connector = new PowerMaxTcpConnector(ipAddress, tcpPort, TCP_CONNECTION_TIMEOUT);
        } else {
            connector = null;
        }
    }

    /**
     * Get the communication driver in charge of the communication with the PowerMax alarm system
     *
     * @return the unique instance of class PowerMaxCommDriver
     */
    public static PowerMaxCommDriver getTheCommDriver() {
        return theCommDriver;
    }

    /**
     * Initialize a new communication driver in charge of the communication with the PowerMax alarm system
     *
     * @param sPort
     *            the serial port name
     * @param ip
     *            the IP address
     * @param port
     *            TCP port number; default port is used if value <= 0
     */
    public static void initTheCommDriver(String sPort, String ip, int port) {
        if (sPort != null) {
            theCommDriver = new PowerMaxCommDriver(sPort);
        } else if (ip != null) {
            theCommDriver = new PowerMaxCommDriver(ip, port);
        } else {
            theCommDriver = null;
        }
    }

    /**
     * Add event listener
     *
     * @param listener
     *            the listener to be added
     */
    public synchronized void addEventListener(PowerMaxEventListener listener) {
        if (connector != null) {
            connector.addEventListener(listener);
        }
    }

    /**
     * Remove event listener
     *
     * @param listener
     *            the listener to be removed
     */
    public synchronized void removeEventListener(PowerMaxEventListener listener) {
        if (connector != null) {
            connector.removeEventListener(listener);
        }
    }

    /**
     * Connect to the PowerMax alarm system
     *
     * @return true if connected or false if not
     */
    public boolean open() {
        if (connector != null) {
            connector.open();
        }
        lastSendMsg = null;
        return isConnected();
    }

    /**
     * Close the connection to the PowerMax alarm system.
     *
     * @return true if connected or false if not
     */
    public boolean close() {
        if (connector != null) {
            connector.close();
        }
        downloadRunning = false;
        return isConnected();
    }

    /**
     * @return true if connected to the PowerMax alarm system or false if not
     */
    public boolean isConnected() {
        return (connector != null) && connector.isConnected();
    }

    /**
     * @return the last message sent to the PowerMax alarm system
     */
    public synchronized PowerMaxBaseMessage getLastSendMsg() {
        return lastSendMsg;
    }

    /**
     * @return the time in milliseconds used to set time and date
     */
    public Long getSyncTimeCheck() {
        return syncTimeCheck;
    }

    /**
     * Compute the CRC of a message
     *
     * @param data
     *            the buffer containing the message
     * @param len
     *            the size of the message in the buffer
     *
     * @return the computed CRC
     */
    public static byte computeCRC(byte[] data, int len) {
        long checksum = 0;
        for (int i = 1; i < (len - 2); i++) {
            checksum = checksum + (data[i] & 0x000000FF);
        }
        checksum = 0xFF - (checksum % 0xFF);
        if (checksum == 0xFF) {
            checksum = 0;
        }
        return (byte) checksum;
    }

    /**
     * Send an ACK for a received message
     *
     * @param msg
     *            the received message object
     * @param ackType
     *            the type of ACK to be sent
     *
     * @return true if the ACK was sent or false if not
     */
    public synchronized boolean sendAck(PowerMaxBaseMessage msg, byte ackType) {
        int code = msg.getCode();
        byte[] rawData = msg.getRawData();
        byte[] ackData;
        if ((code >= 0x80) || ((code < 0x10) && (rawData[rawData.length - 3] == 0x43))) {
            ackData = new byte[] { 0x0D, ackType, 0x43, 0x00, 0x0A };
        } else {
            ackData = new byte[] { 0x0D, ackType, 0x00, 0x0A };
        }

        if (logger.isDebugEnabled()) {
            logger.debug("sendAck(): sending message {}", DatatypeConverter.printHexBinary(ackData));
        }
        boolean done = sendMessage(ackData);
        if (!done) {
            logger.debug("sendAck(): failed");
        }
        return done;
    }

    /**
     * Send a message to the PowerMax alarm panel to change arm mode
     *
     * @param armMode
     *            the arm mode. Allowed values are: Disarmed, Stay, Armed,
     *            StayInstant, ArmedInstant, Night, NightInstant
     * @param pinCode
     *            the PIN code. A string of 4 characters is expected
     *
     * @return true if the message was sent or false if not
     */
    public boolean requestArmMode(String armMode, String pinCode) {
        logger.debug("requestArmMode(): armMode = {}", armMode);

        boolean done = false;

        HashMap<String, Byte> codes = new HashMap<String, Byte>();
        codes.put("Disarmed", (byte) 0x00);
        codes.put("Stay", (byte) 0x04);
        codes.put("Armed", (byte) 0x05);
        codes.put("StayInstant", (byte) 0x14);
        codes.put("ArmedInstant", (byte) 0x15);
        codes.put("Night", (byte) 0x04);
        codes.put("NightInstant", (byte) 0x14);

        Byte code = codes.get(armMode);
        if (code == null) {
            logger.warn("PowerMax alarm binding: invalid requested arm mode: {}", armMode);
        } else if ((pinCode == null) || (pinCode.length() != 4)) {
            logger.warn("PowerMax alarm binding: requested arm mode rejected due to invalid PIN code: {}", armMode);
        } else {
            try {
                byte[] dynPart = new byte[3];
                dynPart[0] = code;
                dynPart[1] = (byte) Integer.parseInt(pinCode.substring(0, 2), 16);
                dynPart[2] = (byte) Integer.parseInt(pinCode.substring(2, 4), 16);

                done = sendMessage(new PowerMaxBaseMessage(PowerMaxSendType.ARM, dynPart), false, 0);
            } catch (NumberFormatException e) {
                logger.warn("PowerMax alarm binding: requested arm mode rejected due to invalid PIN code: {}", armMode);
            }
        }
        return done;
    }

    /**
     * Send a message to the PowerMax alarm panel to change PGM or X10 zone state
     *
     * @param action
     *            the requested action. Allowed values are: OFF, ON, DIM, BRIGHT
     * @param device
     *            the X10 device number. null is expected for PGM
     *
     * @return true if the message was sent or false if not
     */
    public boolean sendPGMX10(String action, Byte device) {
        logger.debug("sendPGMX10(): action = {}, device = {}", action, device);

        boolean done = false;

        HashMap<String, Byte> codes = new HashMap<String, Byte>();
        codes.put("OFF", (byte) 0x00);
        codes.put("ON", (byte) 0x01);
        codes.put("DIM", (byte) 0x0A);
        codes.put("BRIGHT", (byte) 0x0B);

        Byte code = codes.get(action);
        if (code == null) {
            logger.warn("PowerMax alarm binding: invalid PGM/X10 command: {}", action);
        } else if ((device != null)
                && ((device < 1) || (device >= PowerMaxPanelSettings.getThePanelSettings().getNbPGMX10Devices()))) {
            logger.warn("PowerMax alarm binding: invalid X10 device id: {}", device);
        } else {
            int val = (device == null) ? 1 : (1 << device);
            byte[] dynPart = new byte[3];
            dynPart[0] = code;
            dynPart[1] = (byte) (val & 0x000000FF);
            dynPart[2] = (byte) (val >> 8);

            done = sendMessage(new PowerMaxBaseMessage(PowerMaxSendType.X10PGM, dynPart), false, 0);
        }
        return done;
    }

    /**
     * Send a message to the PowerMax alarm panel to bypass a zone or to not bypass a zone
     *
     * @param bypass
     *            true to bypass the zone; false to not bypass the zone
     * @param zone
     *            the zone number (first zone is number 1)
     * @param pinCode
     *            the PIN code. A string of 4 characters is expected
     *
     * @return true if the message was sent or false if not
     */
    public boolean sendZoneBypass(boolean bypass, byte zone, String pinCode) {
        logger.debug("sendZoneBypass(): bypass = {}, zone = {}", bypass ? "true" : "false", zone);

        boolean done = false;

        if ((pinCode == null) || (pinCode.length() != 4)) {
            logger.warn("PowerMax alarm binding: zone bypass rejected due to invalid PIN code");
        } else if ((zone < 1) || (zone > PowerMaxPanelSettings.getThePanelSettings().getNbZones())) {
            logger.warn("PowerMax alarm binding: invalid zone number: {}", zone);
        } else {
            try {
                int val = (1 << (zone - 1));

                byte[] dynPart = new byte[10];
                dynPart[0] = (byte) Integer.parseInt(pinCode.substring(0, 2), 16);
                dynPart[1] = (byte) Integer.parseInt(pinCode.substring(2, 4), 16);
                int i;
                for (i = 2; i < 10; i++) {
                    dynPart[i] = 0;
                }
                i = bypass ? 2 : 6;
                dynPart[i++] = (byte) (val & 0x000000FF);
                dynPart[i++] = (byte) ((val >> 8) & 0x000000FF);
                dynPart[i++] = (byte) ((val >> 16) & 0x000000FF);
                dynPart[i++] = (byte) ((val >> 24) & 0x000000FF);

                done = sendMessage(new PowerMaxBaseMessage(PowerMaxSendType.BYPASS, dynPart), false, 0);
                if (done) {
                    done = sendMessage(new PowerMaxBaseMessage(PowerMaxSendType.BYPASSTAT), false, 0);
                }
            } catch (NumberFormatException e) {
                logger.warn("PowerMax alarm binding: zone bypass rejected due to invalid PIN code");
            }
        }
        return done;
    }

    /**
     * Send a message to set the alarm time and date using the system time and date
     *
     * @return true if the message was sent or false if not
     */
    public boolean sendSetTime() {
        logger.debug("sendSetTime()");

        boolean done = false;

        GregorianCalendar cal = new GregorianCalendar();
        if (cal.get(Calendar.YEAR) >= 2000) {
            logger.debug(String.format("sendSetTime(): sync time %02d/%02d/%04d %02d:%02d:%02d",
                    cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR),
                    cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)));

            byte[] dynPart = new byte[6];
            dynPart[0] = (byte) cal.get(Calendar.SECOND);
            dynPart[1] = (byte) cal.get(Calendar.MINUTE);
            dynPart[2] = (byte) cal.get(Calendar.HOUR_OF_DAY);
            dynPart[3] = (byte) cal.get(Calendar.DAY_OF_MONTH);
            dynPart[4] = (byte) (cal.get(Calendar.MONTH) + 1);
            dynPart[5] = (byte) (cal.get(Calendar.YEAR) - 2000);

            done = sendMessage(new PowerMaxBaseMessage(PowerMaxSendType.SETTIME, dynPart), false, 0);

            cal.set(Calendar.MILLISECOND, 0);
            syncTimeCheck = cal.getTimeInMillis();
        } else {
            logger.warn(
                    "PowerMax alarm binding: time not synchronized; please correct the date/time of your openHAB server");
            syncTimeCheck = null;
        }
        return done;
    }

    /**
     * Send a message to the PowerMax alarm panel to get all the event logs
     *
     * @param pinCode
     *            the PIN code. A string of 4 characters is expected
     *
     * @return true if the message was sent or false if not
     */
    public boolean requestEventLog(String pinCode) {
        logger.debug("requestEventLog()");

        boolean done = false;

        if ((pinCode == null) || (pinCode.length() != 4)) {
            logger.warn("PowerMax alarm binding: requested event log rejected due to invalid PIN code");
        } else {
            try {
                byte[] dynPart = new byte[3];
                dynPart[0] = (byte) Integer.parseInt(pinCode.substring(0, 2), 16);
                dynPart[1] = (byte) Integer.parseInt(pinCode.substring(2, 4), 16);

                done = sendMessage(new PowerMaxBaseMessage(PowerMaxSendType.EVENTLOG, dynPart), false, 0);
            } catch (NumberFormatException e) {
                logger.warn("PowerMax alarm binding: requested event log rejected due to invalid PIN code");
            }
        }
        return done;
    }

    /**
     * Start downloading panel setup
     *
     * @return true if the message was sent or the sending is delayed; false in other cases
     */
    public synchronized boolean startDownload() {
        if (downloadRunning) {
            logger.info("PowerMax alarm binding: download not started as one is in progress");
            return false;
        } else {
            downloadRunning = true;
            return sendMessage(PowerMaxSendType.DOWNLOAD);
        }
    }

    /**
     * Act the exit of the panel setup
     */
    public synchronized void exitDownload() {
        downloadRunning = false;
    }

    /**
     * Send a ENROLL message
     *
     * @return true if the message was sent or the sending is delayed; false in other cases
     */
    public boolean enrollPowerlink() {
        return sendMessage(new PowerMaxBaseMessage(PowerMaxSendType.ENROLL), true, 0);
    }

    /**
     * Send a message or delay the sending if time frame for receiving response is not ended
     *
     * @param msgType
     *            the message type to be sent
     *
     * @return true if the message was sent or the sending is delayed; false in other cases
     */
    public boolean sendMessage(PowerMaxSendType msgType) {
        return sendMessage(new PowerMaxBaseMessage(msgType), false, 0);
    }

    /**
     * Delay the sending of a message
     *
     * @param msgType
     *            the message type to be sent
     *
     * @param waitTime
     *            the delay in seconds to wait
     *
     * @return true if the sending is delayed; false in other cases
     */
    public boolean sendMessageLater(PowerMaxSendType msgType, int waitTime) {
        return sendMessage(new PowerMaxBaseMessage(msgType), false, waitTime);
    }

    /**
     * Send a message or delay the sending if time frame for receiving response is not ended
     *
     * @param msg
     *            the message to be sent
     * @param immediate
     *            true if the message has to be send without considering timing
     * @param waitTime
     *            the delay in seconds to wait
     *
     * @return true if the message was sent or the sending is delayed; false in other cases
     */
    private synchronized boolean sendMessage(PowerMaxBaseMessage msg, boolean immediate, int waitTime) {

        if ((waitTime > 0) && (msg != null)) {
            logger.debug("sendMessage(): delay ({} s) sending message (type {})", waitTime,
                    msg.getSendType().toString());
            Timer timer = new Timer();
            // Don't queue the message
            timer.schedule(new DelayedSendTask(msg), waitTime * 1000);
            return true;
        }

        if (msg == null) {
            msg = msgQueue.peek();
            if (msg == null) {
                logger.debug("sendMessage(): nothing to send");
                return false;
            }
        }

        // Delay sending if time frame for receiving response is not ended
        long delay = WAITING_DELAY_FOR_RESPONSE - (System.currentTimeMillis() - connector.getWaitingForResponse());

        PowerMaxBaseMessage msgToSend = msg;

        if (!immediate) {
            msgToSend = msgQueue.peek();
            if (msgToSend != msg) {
                logger.debug("sendMessage(): add message in queue (type {})", msg.getSendType().toString());
                msgQueue.offer(msg);
                msgToSend = msgQueue.peek();
            }
            if ((msgToSend != msg) && (delay > 0)) {
                return true;
            } else if ((msgToSend == msg) && (delay > 0)) {
                if (delay < 100) {
                    delay = 100;
                }
                logger.debug("sendMessage(): delay ({} ms) sending message (type {})", delay,
                        msgToSend.getSendType().toString());
                Timer timer = new Timer();
                timer.schedule(new DelayedSendTask(null), delay);
                return true;
            } else {
                msgToSend = msgQueue.poll();
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("sendMessage(): sending {} message {}", msgToSend.getSendType().toString(),
                    DatatypeConverter.printHexBinary(msgToSend.getRawData()));
        }
        boolean done = sendMessage(msgToSend.getRawData());
        if (done) {
            lastSendMsg = msgToSend;
            connector.setWaitingForResponse(System.currentTimeMillis());

            if (!immediate && (msgQueue.peek() != null)) {
                logger.debug("sendMessage(): delay sending next message (type {})",
                        msgQueue.peek().getSendType().toString());
                Timer timer = new Timer();
                timer.schedule(new DelayedSendTask(null), WAITING_DELAY_FOR_RESPONSE);
            }
        } else {
            logger.debug("sendMessage(): failed");
        }

        return done;
    }

    /**
     * Send a message to the PowerMax alarm panel
     *
     * @param data
     *            the data buffer containing the message to be sent
     *
     * @return true if the message was sent or false if not
     */
    private boolean sendMessage(byte[] data) {
        boolean done = false;
        if (isConnected()) {
            data[data.length - 2] = computeCRC(data, data.length);
            connector.sendMessage(data);
            done = connector.isConnected();
        } else {
            logger.debug("sendMessage(): aborted (not connected)");
        }
        return done;
    }

    /**
     * A class used to delay the sending of a message
     */
    private class DelayedSendTask extends TimerTask {

        private PowerMaxBaseMessage msg;

        private DelayedSendTask(PowerMaxBaseMessage msg) {
            super();
            this.msg = msg;
        }

        @Override
        public void run() {
            logger.debug("Time to send next message");
            sendMessage(msg, false, 0);
        }

    }

}
