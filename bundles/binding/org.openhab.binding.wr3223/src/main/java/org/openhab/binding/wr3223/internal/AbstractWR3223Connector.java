/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wr3223.internal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base implementation for the connector to a wr3223 device.
 *
 * @author Michael Fraefel
 *
 */
public abstract class AbstractWR3223Connector {

    private static final Logger logger = LoggerFactory.getLogger(AbstractWR3223Connector.class);

    /**
     * Start der Nachricht
     */
    private static byte STX = 0x02;
    /**
     * Ende der Nachricht
     */
    private static byte ETX = 0x03;
    /**
     * Ende der Übertragung
     */
    private static byte EOT = 0x04;
    /**
     * Anfrage / Anforderung
     */
    private static byte ENQ = 0x05;
    /**
     * Positive Rückmeldung
     */
    private static byte ACK = 0x06;
    /**
     * Negative Rückmeldung
     */
    private static byte NAK = 0x15;

    /**
     * Data stream to the WR3223
     */
    private DataInputStream inputStream;

    /**
     * Data stream from the WR3223
     */
    private DataOutputStream outputStream;

    /**
     * Connect to the WR3223
     *
     * @param inputStream
     * @param outputStream
     */
    protected void connect(DataInputStream inputStream, DataOutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    /**
     * Close the connection to the WR3223
     *
     * @throws IOException
     */
    protected void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }

    /**
     * Read data from the WR3223 controller
     *
     * @param addr Controller address
     * @param command Command
     * @return The received value.
     * @throws IOException
     */
    public String read(int addr, WR3223Commands command) throws IOException {
        // Remove old data from the input stream
        if (inputStream.available() > 0) {
            inputStream.skipBytes(inputStream.available());
        }

        // Write read message to the controller
        byte[] message = new byte[8];
        message[0] = EOT;
        setControllerAddressToMessage(message, addr, 1);
        setCommandToMessage(message, command, 5);
        message[7] = ENQ;
        outputStream.write(message);
        outputStream.flush();
        if (logger.isDebugEnabled()) {
            logger.debug("Write data: {}", bytesToHexString(message));
        }

        // Read answer from controller
        byte[] answer = readAnswer();
        int chkSum = inputStream.read();
        if (logger.isDebugEnabled()) {
            logger.debug("Read data: {} with checksum {}.", bytesToHexString(answer), Integer.toHexString(chkSum));
        }

        // Check answer
        if (answer[0] == STX && answer[answer.length - 1] == ETX) {
            if (chkSum == buildCheckSum(answer, 1, answer.length - 1)) {
                if (command.name().equals(toString(answer, 1, 2))) {
                    return toString(answer, 3, answer.length - 2);
                } else {
                    logger.error("Wrong command received. Expected {} but got {}.", command.name(),
                            toString(answer, 1, 2));
                }
            } else {
                logger.error("Checksum error. Expected {} but got {}.", chkSum,
                        buildCheckSum(answer, 1, answer.length - 1));
            }
        } else {
            logger.error("Start/end of the controller answer is wrong.");
        }
        return null;

    }

    public boolean write(int addr, WR3223Commands command, String data) throws IOException {
        // Check if the provided data not longer then 6 characters.
        if (data == null || data.length() > 6 || data.length() == 0) {
            throw new IllegalArgumentException("Not valid data format.");
        }

        // Remove old data from the input stream
        if (inputStream.available() > 0) {
            inputStream.skipBytes(inputStream.available());
        }

        // Write command to the controller
        byte[] message = new byte[10 + data.length()];
        message[0] = EOT;
        setControllerAddressToMessage(message, addr, 1);
        message[5] = STX;
        setCommandToMessage(message, command, 6);
        byte[] d = data.getBytes();
        for (int ii = 0; ii < data.length(); ii++) {
            message[8 + ii] = d[ii];
        }
        message[message.length - 2] = ETX;
        byte chkSum = (byte) buildCheckSum(message, 6, message.length - 2);
        message[message.length - 1] = chkSum;
        outputStream.write(message);
        outputStream.flush();
        if (logger.isDebugEnabled()) {
            logger.debug("Write data: {}", bytesToHexString(message));
        }

        // Read controller answer
        if (waitUntilDataAvailable(1, 5000l)) {
            int answer = inputStream.readByte();
            if (logger.isDebugEnabled()) {
                logger.debug("Answer from WR3223 {}.", Integer.toHexString(answer));
            }
            if (answer == ACK) {
                return true;
            }
            logger.error("Command {} with data {} not accepted.", command.name(), data);
        } else {
            logger.error("Timeout. No answer for command {} with data {}.", command.name(), data);
        }
        return false;

    }

    /**
     * Wait until the specified timeout of data are available or the timout is reached.
     *
     * @param dataCount
     * @param timeout
     * @throws IOException
     */
    private boolean waitUntilDataAvailable(int dataCount, long timeout) throws IOException {
        try {
            long startTime = System.currentTimeMillis();
            long duration = 0l;
            Thread.sleep(100l);
            while (inputStream.available() < dataCount && duration < timeout) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Wait for answer from WR3223. Timeout in {}ms.", timeout - duration);
                }
                Thread.sleep(500l);
                duration = (System.currentTimeMillis() - startTime);
            }
        } catch (InterruptedException e) {
            logger.error("Waiting for WR3223 was interrupted.", e);
        }
        return inputStream.available() >= dataCount;
    }

    /**
     * Read bytes from input stream until ETX.
     *
     * @return The extracted answer.
     * @throws IOException
     */
    private byte[] readAnswer() throws IOException {
        List<Byte> answerList = new ArrayList<Byte>();
        Byte val;
        do {
            val = inputStream.readByte();
            answerList.add(val);
        } while (val != ETX);
        byte[] answer = ArrayUtils.toPrimitive(answerList.toArray(new Byte[0]));
        return answer;
    }

    /**
     * Set the command to the message.
     *
     * @param message
     * @param command
     * @param offset
     */
    private void setCommandToMessage(byte[] message, WR3223Commands command, int offset) {
        byte[] commandByte = command.name().getBytes();
        message[offset] = commandByte[0];
        message[offset + 1] = commandByte[1];
    }

    /**
     * Set the controller address to the message.
     *
     * @param message
     * @param addr
     * @param offset
     */
    private void setControllerAddressToMessage(byte[] message, int addr, int offset) {
        if (addr > 99) {
            throw new IllegalArgumentException("The address must be between 1 and 99.");
        }
        String addrStr = String.valueOf(addr);
        if (addrStr.length() == 1) {
            addrStr = "0" + addrStr;
        }
        byte[] addrByte = addrStr.getBytes();
        message[offset] = addrByte[0];
        message[offset + 1] = addrByte[0];
        message[offset + 2] = addrByte[1];
        message[offset + 3] = addrByte[1];
    }

    /**
     * Answer to string
     *
     * @param answer as bytes
     * @param start
     * @param end
     * @return the answer as string value.
     */
    private String toString(byte[] answer, int start, int end) {
        String data = "";
        for (int ii = start; ii <= end; ii++) {
            char ch = (char) answer[ii];
            data = data + ch;
        }
        return data;
    }

    /**
     * Build the checksum.
     *
     * @param answer
     * @param start
     * @param end
     * @return checksum of the answer byte array.
     */
    private int buildCheckSum(byte[] answer, int start, int end) {
        int chkSum = answer[start];
        for (int i = start + 1; i <= end; i++) {
            chkSum = chkSum ^ answer[i];
        }
        return chkSum;
    }

    /**
     * Convert the bytes to a hex string for debug messages.
     *
     * @param data
     * @return
     */
    private String bytesToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int ii = 0; ii < data.length; ii++) {
            String asHex = Integer.toHexString(data[ii]);
            if (asHex.length() == 1) {
                sb.append("0");
            }
            sb.append(asHex);
            if (ii + 1 < data.length) {
                sb.append(" ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
