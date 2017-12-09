/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smarthomatic.internal;

import java.util.StringTokenizer;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * these class represents the basestation and the communication between openhab
 * and the smarthomatic basestation
 * {@link https://www.smarthomatic.org/devices/base_station.html}
 *
 * @author mcjobo
 * @author arohde
 * @since 1.9.0
 */
public class BaseStation implements SerialEventWorker {

    private static final Logger logger = LoggerFactory.getLogger(BaseStation.class);

    private SerialDevice serialDevice;

    private String[] versionInfo;

    private SerialEventWorker bindingEventWorker;

    /**
     * public constructor for BaseStation
     * 
     * @param port
     *            of the serial line uses eg com4 or /dev/ttyUSB0
     * @param baud
     *            rate used eg 19200
     * @param sew
     *            used event worker
     */
    public BaseStation(String port, int baud, SerialEventWorker sew) {
        bindingEventWorker = sew;
        serialDevice = new SerialDevice(port, baud);
        serialDevice.setEventWorker(this);
        try {
            serialDevice.initialize();
        } catch (InitializationException e) {
            logger.error("Port cannot be opened. Port: {}", port);
            logger.error("initialization exception", e);
        }
    }

    /**
     * closes the serial port
     * 
     */
    public void closeSerialPort() {
        serialDevice.close();
    }

    private String genHexString(int deviceID, int length) {
        String s = Integer.toHexString(deviceID);
        int len = s.length();
        for (int i = len; i < length; i++) {
            s = "0" + s;
        }
        return s;
    }

    private String getToggleTime(int toggleTime, boolean on) {
        // shift left the on bit, so that the on-bit stands on position 17 from
        // right
        long temp = (on ? 1 : 0) << 16;
        // patch toggletime into the last 16 bit and be sure to prevent to
        // override on-bit
        temp |= (toggleTime & 0x0001FFFF);
        // shift temp to left by 15 bits (the on bit should stand at msb
        // position)
        temp <<= 15;

        // write hex representation of temp to s
        String s = Long.toHexString(temp);

        // fill String with leading zeros until it's minimum 2 chars long
        for (int i = s.length(); i < 2; i++) {
            s = "0" + s;
        }

        return s;
    }

    public String prepareCommand(int deviceID, int messageGroupId, int messageId, int toggleTime, Command command) {
        String cmd = "";
        String messageData = "";
        // GPIO Digital Port Message
        if (messageGroupId == 1 && messageId == 1 && command instanceof DecimalType) {
            int setting = ((DecimalType) command).intValue();
            // message data length = 8 bits, no need to shift
            messageData = genHexString(setting, 2);
        } else if (messageGroupId == 1 && messageId == 5 && command instanceof DecimalType) {
            // GPIO Digital Pin Message
            int setting = ((DecimalType) command).intValue();
            messageData = genHexString(setting, 1);
        } else if (messageGroupId == 1 && messageId == 6 && command instanceof DecimalType) {
            // GPIO Digital Pin Timeout Message
            int setting = ((DecimalType) command).intValue();
            setting = setting << 4; // message data length = 20 bits
            messageData = genHexString(setting, 5);
        } else if (messageGroupId == 60 && messageId == 1 && command instanceof DecimalType) {
            // Dimmer Brightness Message
            int brightness = ((DecimalType) command).intValue();
            brightness = brightness << 1;
            messageData = Integer.toHexString(brightness);
        } else if (messageGroupId == 60 && messageId == 2 && command instanceof DecimalType) {
            // Dimmer Animation Message
            int setting = ((DecimalType) command).intValue();
            messageData = genHexString(setting, 8);
        } else if (messageGroupId == 60 && messageId == 10) {
            // RGB Dimmer Color Message
            if (command instanceof HSBType) {
                ShcColor translateColor = translateColor((HSBType) command);
                messageData = translateColor.toString();
            } else if (command instanceof DecimalType) {
                int color = ((DecimalType) command).intValue();
                color = color << 2; // 6 bits
                messageData = genHexString(color, 2);
            }
        } else if (messageGroupId == 60 && messageId == 11 && command instanceof DecimalType) {
            // Dimmer Color Animation Message
            int setting = ((DecimalType) command).intValue();
            /*
             * TODO: Right now only two time-color pairs are supported in this
             * implemenation. It is unclear to me how to handle 115 bits that
             * are required for the complete message consisting of 10 time-color
             * pairs which gives an integer the size of 2^115
             */
            setting = setting << 5;
            messageData = Integer.toHexString(setting);
        }
        if (!"".equals(messageData)) {
            cmd = "s0002" + genHexString(deviceID, 4) + genHexString(messageGroupId, 2) + genHexString(messageId, 2)
                    + messageData;
        }
        return cmd;
    }

    /**
     * sends a command to the smarthomatic basestation with the given parameters
     * 
     * @param deviceID
     * @param messageGroupId
     * @param messageId
     * @param toggleTime
     * @param command
     */
    public void sendCommand(int deviceID, int messageGroupId, int messageId, int toggleTime, Command command) {
        String cmd = prepareCommand(deviceID, messageGroupId, messageId, toggleTime, command);
        if (cmd != "") {
            logger.debug("send to serial port: {}", cmd);
            serialDevice.writeString(cmd + "\r");
        }
    }

    private ShcColor translateColor(HSBType color) {
        int red = color.getRed().intValue() * 16 / 100;
        int green = color.getGreen().intValue() * 16 / 100;
        int blue = color.getBlue().intValue() * 16 / 100;
        ShcColor result = new ShcColor((byte) red, (byte) green, (byte) blue);
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * handles new events send from the smarthomatic basestation to openhab
     */
    @Override
    public void eventOccured(String message) {

        // normally there comes only one line to the baseStation
        // except when station is restarted

        // Filter out the lines that contain garbage data
        if (message.contains("(CRC wrong after decryption)")) {
            logger.debug("BaseStation eventOccurred: CRC wrong after decryption");
            return;
        }

        if (message.contains("Base Station")) {
            StringTokenizer strTok = new StringTokenizer(message, "\n");
            String data = null;
            versionInfo = new String[strTok.countTokens()];
            logger.debug("BaseStation eventOccurred - initial message ( {} )", strTok.countTokens());
            int i = 0;
            while (strTok.hasMoreTokens()) {
                versionInfo[i] = strTok.nextToken();
                logger.debug("<BaseStation>[ {} ]: {}", i, versionInfo[i]);
                i++;
            }
        } else {
            String logResult = message.replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r").substring(0, 40);

            logger.debug("BaseStation eventOccurred - giving to Binding {}", logResult);
            if (bindingEventWorker != null) {
                bindingEventWorker.eventOccured(message);
            }
        }
    }

    /**
     * prints the uses device
     * 
     */
    @Override
    public String toString() {
        return "BaseStation listening on port " + serialDevice.getPort();
    }

    /**
     * private Class that holds the the color parts (means the red, green and
     * blue part) of a color
     * 
     * @author jbolay
     * @since 1.9.0
     */
    private class ShcColor {
        byte red;
        byte green;
        byte blue;

        public ShcColor(byte red, byte green, byte blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            logger.debug("red: {}, green: {}, blue: {}", red, green, blue);
        }

        @Override
        public String toString() {
            // calculating the translation from rgb values to SHC color table
            int colorNumber = (((red - 1) / 4) * 16) + (((green - 1) / 4) * 4) + ((blue - 1) / 4);
            colorNumber = colorNumber << 2;
            String hexString = Integer.toHexString(colorNumber);
            while (hexString.length() < 2) {
                hexString = "0" + hexString;
            }
            return hexString;
        }
    }

}
