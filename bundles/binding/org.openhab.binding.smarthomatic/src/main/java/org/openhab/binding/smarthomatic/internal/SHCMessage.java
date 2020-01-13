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
package org.openhab.binding.smarthomatic.internal;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.CRC32;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.NotImplementedException;
import org.openhab.binding.smarthomatic.internal.packetData.Array;
import org.openhab.binding.smarthomatic.internal.packetData.BoolValue;
import org.openhab.binding.smarthomatic.internal.packetData.IntValue;
import org.openhab.binding.smarthomatic.internal.packetData.Packet;
import org.openhab.binding.smarthomatic.internal.packetData.Packet.MessageGroup;
import org.openhab.binding.smarthomatic.internal.packetData.Packet.MessageGroup.Message;
import org.openhab.binding.smarthomatic.internal.packetData.UIntValue;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class represents a complete smarthomatic message it also includes the
 * capability of parsing and creating smarthomatic messages for detailed
 * description of smarthomatic messages
 * {@link https://www.smarthomatic.org/basics/communication_protocol.html}
 *
 * @author mcjobo
 * @author arohde
 * @since 1.9.0
 */
public class SHCMessage {

    private static final Logger logger = LoggerFactory.getLogger(SHCMessage.class);

    /**
     * constants
     *
     */

    public static final String DATA_FLAG = "PKT:";
    public static final int DATA_FLAG_SIZE = DATA_FLAG.length();

    public static final String SENDER_ID = "SID";
    public static final String PACKET_COUNTER = "PC";
    public static final String MESSAGE_TYPE = "MT";
    public static final String MESSAGE_GROUP_ID = "MGID";
    public static final String MESSAGE_ID = "MID";
    public static final String MESSAGE_DATA = "MD";

    // MessageGroupIDs
    public static final int GRP_Generic = 0;
    public static final int GRP_GPIO = 1;
    public static final int GRP_Weather = 10;
    public static final int GRP_Environment = 11;
    public static final int GRP_Powerswitch = 20;
    public static final int GRP_Dimmer = 60;

    @SuppressWarnings("unused")
    private String originalMessage;
    private String editedMessage;
    private SHCHeader header;
    private Packet packet;
    public static Charset charset = Charset.forName("UTF-8");
    public static CharsetEncoder encoder = charset.newEncoder();

    /**
     * the smarthomatic header class represents the part of a message that is
     * common to all messages for details of header format see
     * {@link https://www.smarthomatic.org/basics/communication_protocol.html}
     *
     * @author mcjobo
     * @author arohde
     * @since 1.9.0
     */
    public class SHCHeader {
        private int SenderID; // =
                              // tokens[0].substring(tokens[0].indexOf("=")+1);
        private int MessageType; // =
                                 // tokens[2].substring(tokens[2].indexOf("=")+1);
        private long MessageGroupID;// =
                                    // tokens[3].substring(tokens[3].indexOf("=")+1);
        private int MessageID; // =
                               // tokens[4].substring(tokens[4].indexOf("=")+1);
        private byte[] MessageData; // =

        // tokens[5].substring(tokens[5].indexOf("=")+1);

        /**
         * getter for the parsed sender id
         *
         * @return
         */
        public int getSenderID() {
            return SenderID;
        }

        /**
         * getter for the parsed message type
         *
         * @return
         */
        public int getMessageType() {
            return MessageType;
        }

        /**
         * getter for the parsed message group id
         *
         * @return
         */
        public long getMessageGroupID() {
            return MessageGroupID;
        }

        /**
         * getter for the parsed message id
         *
         * @return
         */
        public int getMessageID() {
            return MessageID;
        }

        /**
         * getter for the parsed message data
         *
         * @return
         */
        public byte[] getMessageData() {
            return MessageData;
        }

        /**
         * constructor to create a new smarthomatic header with the given data
         *
         * @param data
         */
        public SHCHeader(String data) {
            StringTokenizer dataTok = new StringTokenizer(data, ";");
            String[] tokens = new String[dataTok.countTokens()];
            Map<String, String> tokensMap = new HashMap<String, String>();
            int i = 0;
            while (dataTok.hasMoreTokens()) {
                String nextToken = dataTok.nextToken();
                tokens[i++] = nextToken;
                String[] split = nextToken.split("=");
                if (split.length == 2) {
                    tokensMap.put(split[0].trim(), split[1].trim());
                }
            }
            String crc = tokens[tokens.length - 1].trim();
            CRC32 crc32 = new CRC32();
            String substring = data.substring(0, data.lastIndexOf(";") + 1);
            crc32.update(substring.getBytes());
            long value = crc32.getValue();
            String decode = Long.toHexString(value);
            decode = fillStringWithLeadingZeros(decode, crc.length());
            if (!crc.equals(decode)) {
                RuntimeException runtimeException = new RuntimeException("SHC Binding CRC Error");
                logger.error("SHC Binding CRC Error calculated CRC: {} found  CRC: {}", runtimeException, decode, crc);
                throw runtimeException;
            }

            SenderID = Integer.parseInt(tokensMap.get(SENDER_ID));
            MessageType = Integer.parseInt(tokensMap.get(MESSAGE_TYPE));
            if (MessageType == 0 || MessageType == 1 || MessageType == 2 || MessageType == 8 || MessageType == 10) {
                MessageGroupID = Integer.parseInt(tokensMap.get(MESSAGE_GROUP_ID));
                MessageID = Integer.parseInt(tokensMap.get(MESSAGE_ID));
            }
            if (MessageType == 1 || MessageType == 2 || MessageType == 8 || MessageType == 10) {
                MessageData = DatatypeConverter.parseHexBinary(tokensMap.get(MESSAGE_DATA));
            }

            logger.trace("BaseStation SenderID:       {}", SenderID);
            logger.trace("BaseStation MessageType:    {}", MessageType);
            logger.trace("BaseStation MessageGroupID: {}", MessageGroupID);
            logger.trace("BaseStation MessageID:      {}", MessageID);
            logger.trace("BaseStation MessageData:    {}", MessageData);
        }
    }

    private String fillStringWithLeadingZeros(String value, int length) {
        while (value.length() < length) {
            value = "0" + value;
        }
        return value;
    }

    /**
     * these part represent the message data of the different smarthomatic
     * messages details of a messages data is described here:
     * {@link https://www.smarthomatic.org/basics/message_catalog.html}
     *
     * @author mcjobo
     * @author arohde
     * @since 1.9.0
     */
    public class SHCData {
        private List<Type> openHABTypes = new ArrayList<Type>();

        public SHCData(SHCHeader header) {
            byte[] data = header.getMessageData();
            MessageGroup group = null;
            Message message = null;
            int messageType = header.getMessageType();
            if (messageType == 8 || messageType == 10) {
                for (MessageGroup messageGroup : packet.getMessageGroup()) {
                    if (messageGroup.getMessageGroupID() == header.getMessageGroupID()) {
                        group = messageGroup;
                        break;
                    }
                }
                for (Message message1 : group.getMessage()) {
                    if (message1.getMessageID() == header.getMessageID()) {
                        message = message1;
                        break;
                    }
                }

                int startBit = 0;
                startBit = getDataValues(startBit, message.getDataValue(), data);
            }
        }

        private int getDataValues(int startBit, List<Object> dataValues, byte[] data) {
            for (Object object : dataValues) {
                if (object instanceof UIntValue) {
                    UIntValue value = (UIntValue) object;
                    Integer result = parseData(data, value.getBits(), startBit, false);
                    openHABTypes.add(new DecimalType(result));
                    startBit += value.getBits();
                } else if (object instanceof IntValue) {
                    IntValue value = (IntValue) object;
                    Integer result = parseData(data, value.getBits(), startBit, true);
                    openHABTypes.add(new DecimalType(result));
                    startBit += value.getBits();
                } else if (object instanceof BoolValue) {
                    Integer value = parseData(data, 1, startBit, false);
                    if (value > 0) {
                        openHABTypes.add(OnOffType.ON);
                    } else {
                        openHABTypes.add(OnOffType.OFF);
                    }
                    startBit += 1;

                } else if (object instanceof Array) {
                    Array value = (Array) object;
                    for (int i = 0; i < value.getLength(); i++) {
                        startBit = getDataValues(startBit, value.getArrayDataValue(), data);
                    }
                } else {
                    throw new NotImplementedException();
                }
            }
            return startBit;
        }

        private Integer parseData(byte[] data, long bits, int startBit, boolean signed) {
            int bits2skip = startBit;
            long bits2add = bits;
            int result = 0;
            // Iterate over bytes and bits
            for (byte b : data) {
                for (int mask = 0x80; mask != 0x00; mask >>= 1) {
                    boolean bitvalue = (b & mask) != 0;
                    // no more bits2add, do nothing
                    if (bits2add < 0) {
                    }
                    // skip until start bit is reached
                    else if (bits2skip > 0) {
                        bits2skip--;
                    } else {
                        if (bitvalue) {
                            result = result + (int) Math.pow(2, bits2add - 1);
                        }
                        bits2add--;
                    }
                }
            }
            if (signed) {
                if (result >= (int) Math.pow(2, bits - 1)) {
                    result = result - (int) Math.pow(2, bits);
                }
            }
            return result;
        }

        /**
         * returns the openhab types found
         *
         * @return
         */
        public List<Type> getOpenHABTypes() {
            return openHABTypes;
        }

        /**
         * string representation of the smarthomatic data
         */
        @Override
        public String toString() {
            String res = "SHCData [";
            for (Type type : openHABTypes) {
                res += type.toString();
            }
            res += "]";
            return res;
        }

    }

    /**
     * constructor to create a new smarthomatic message
     *
     * @param message
     * @param packet
     */
    public SHCMessage(String message, Packet packet) {
        originalMessage = message;
        editedMessage = message.substring(message.indexOf(DATA_FLAG) + DATA_FLAG_SIZE);
        header = new SHCHeader(editedMessage);
        this.packet = packet;
    }

    /**
     * getter to return the parsed header
     *
     * @return
     */
    public SHCHeader getHeader() {
        return header;
    }

    /**
     * getter to return the parsed data object
     *
     * @return
     */
    public SHCData getData() {
        return new SHCData(header);
    }

    /**
     * getter to return the openhab states parsed from the smarthomtic message
     *
     * @param object
     * @return
     */
    public List<Type> openHABStateFromSHCMessage(Item object) {
        return getData().getOpenHABTypes();
    }
}
