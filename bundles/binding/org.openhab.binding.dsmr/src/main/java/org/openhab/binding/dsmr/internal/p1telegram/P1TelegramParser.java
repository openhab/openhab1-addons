/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dsmr.internal.p1telegram;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.openhab.binding.dsmr.internal.messages.OBISMessage;
import org.openhab.binding.dsmr.internal.messages.OBISMsgFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The P1TelegramParser is a class that will read P1-port data create a full P1
 * telegram.
 *
 * This class parses data via the public method parseData. It will parse the
 * received data. If a complete P1 telegram is received the OBIS messages are
 * returned. Otherwise it will continue parsing with the next call op parseData.
 *
 * @author mvolaart
 * @since 1.7.0
 */
public class P1TelegramParser {
    // Logger
    public static final Logger logger = LoggerFactory.getLogger(P1TelegramParser.class);

    // Helper classers
    private OBISMsgFactory factory;

    // Reader state
    private P1TelegramParserState parserState;

    // Current P1Telegram values
    private LinkedList<OBISDataLine> obisDataLines;
    private OBISDataLine currentLine = null;

    /**
     * Creates a new P1TelegramParser
     *
     * @param factory
     *            OBISMsgFactory object
     */
    public P1TelegramParser(OBISMsgFactory factory) {
        this.factory = factory;

        obisDataLines = new LinkedList<OBISDataLine>();
        parserState = new P1TelegramParserState();
    }

    /**
     * Parses data. If parsing is not ready yet nothing will be returned. If
     * parsing fails completely nothing will be returned. If parsing succeeds
     * (partial) the received OBIS messages will be returned.
     *
     * @param data
     *            byte data
     * @param offset
     *            offset tot start in the data buffer
     * @param length
     *            number of bytes to parse
     * @return List of {@link OBISMessage} if a full P1 telegram is received,
     *         empty list if parsing is not ready or failed completely.
     */
    public List<OBISMessage> parseData(byte[] data, int offset, int length) {
        List<OBISMessage> receivedMessages = new LinkedList<OBISMessage>();

        if (logger.isTraceEnabled()) {
            logger.trace("Data: {}, state before parsing: {}", new String(Arrays.copyOfRange(data, offset, length)),
                    parserState);
        }
        for (int i = offset; i < (offset + length); i++) {
            char c = (char) data[i];

            switch (parserState.getState()) {
                case WAIT_FOR_START:
                    if (c == '/') {
                        parserState.setState(P1TelegramParserState.State.STARTED);

                        handleNewP1Telegram();
                    }
                    break;
                case STARTED:
                    parserState.setState(P1TelegramParserState.State.HEADER);
                    break;

                case HEADER:
                    if (c == '\r') {
                        parserState.setState(P1TelegramParserState.State.CRLF);
                    }
                    break;
                case CRLF:
                    if (Character.isWhitespace(c)) {
                        // do nothing
                    } else if (Character.isDigit(c)) {
                        parserState.setState(P1TelegramParserState.State.DATA_OBIS_ID);

                        finishObisLine();
                    } else {
                        handleUnexpectedCharacter(c);

                        parserState.setState(P1TelegramParserState.State.WAIT_FOR_START);
                    }
                    break;
                case DATA_OBIS_ID:
                    if (Character.isWhitespace(c)) {
                        // ignore
                    } else if (Character.isDigit(c) || c == ':' || c == '-' || c == '.' || c == '*') {
                        // do nothing
                    } else if (c == '(') {
                        parserState.setState(P1TelegramParserState.State.DATA_OBIS_VALUE_START);
                    } else if (c == '!') {
                        logger.warn("Unexpected character '!'. Going to state: {}",
                                P1TelegramParserState.State.CRC_VALUE);

                        parserState.setState(P1TelegramParserState.State.CRC_VALUE);
                    } else {
                        handleUnexpectedCharacter(c);

                        parserState.setState(P1TelegramParserState.State.WAIT_FOR_START);
                    }
                    break;
                case DATA_OBIS_VALUE_START:
                    if (c == ')') {
                        handleObisValueReady();

                        parserState.setState(P1TelegramParserState.State.DATA_OBIS_VALUE_END);
                    } else {
                        parserState.setState(P1TelegramParserState.State.DATA_OBIS_VALUE);
                    }
                    break;

                case DATA_OBIS_VALUE:
                    if (c == ')') {
                        handleObisValueReady();

                        parserState.setState(P1TelegramParserState.State.DATA_OBIS_VALUE_END);
                    }
                    break;
                case DATA_OBIS_VALUE_END:
                    if (Character.isWhitespace(c)) {
                        // ignore
                    } else if (Character.isDigit(c)) {
                        finishObisLine();

                        parserState.setState(P1TelegramParserState.State.DATA_OBIS_ID);
                    } else if (c == '(') {
                        parserState.setState(P1TelegramParserState.State.DATA_OBIS_VALUE_START);
                    } else if (c == '!') {
                        parserState.setState(P1TelegramParserState.State.CRC_VALUE);
                    } else {
                        handleUnexpectedCharacter(c);

                        parserState.setState(P1TelegramParserState.State.WAIT_FOR_START);
                    }
                    break;

                case CRC_VALUE:
                    if (c == '\r') {
                        if (parserState.getCrcValue().length() > 0) {
                            // TODO: Handle CRC here
                        }

                        receivedMessages.addAll(handleEndP1Telegram());

                        parserState.setState(P1TelegramParserState.State.WAIT_FOR_START);
                    }
                    break;
            }
            parserState.handleCharacter(c);
        }
        logger.trace("State after parsing: {}", parserState);

        return receivedMessages;
    }

    /**
     * Handles the start of a new P1 telegram This method will clear internal
     * state
     */
    private void handleNewP1Telegram() {
        obisDataLines.clear();
        currentLine = null;
    }

    /**
     * Handles the end of a P1 telegram. This method will parse the raw data to
     * a list of {@link OBISMessage}
     */
    private List<OBISMessage> handleEndP1Telegram() {
        finishObisLine();

        List<OBISMessage> obisMessages = new LinkedList<OBISMessage>();

        for (OBISDataLine obisDataLine : obisDataLines) {
            OBISMessage obisMessage = factory.getMessage(obisDataLine.obisId, obisDataLine.obisStringValues);
            logger.debug("Parsed: {}, to: {}", obisDataLine, obisMessage);
            if (obisMessage != null) {
                obisMessages.add(obisMessage);
            } else {
                logger.warn("Failed to parse: {}", obisDataLine);
            }
        }

        return obisMessages;
    }

    /**
     * Handles an unexpected character. The character will be logged and the P1
     * telegram will be finished.
     *
     * @param c
     *            the unexpected character
     */
    private void handleUnexpectedCharacter(char c) {
        logger.warn("Unexpected character '{}' in state: {}. Publishing partial P1Telegram and wait for new P1Telegram",
                c, parserState);

        handleEndP1Telegram();
    }

    /**
     * Handle if a full OBIS value is parsed. This method will store the OBIS
     * value in the current OBISDataLine
     */
    private void handleObisValueReady() {
        if (currentLine == null) {
            currentLine = new OBISDataLine(parserState.getObisId());
        }
        currentLine.obisStringValues.add(parserState.getObisValue());
    }

    /**
     * Handle the end of a OBIS Line (i.e. all values are parsed)
     */
    private void finishObisLine() {
        if (currentLine != null) {
            obisDataLines.add(currentLine);

            currentLine = null;
        }
    }

    /**
     * Class representing a OBISDataLine, e.g. '0-0:96.3.10(1)'
     *
     * @author mvolaart
     * @since 1.7.0
     */
    class OBISDataLine {
        // OBIS identifier
        final String obisId;

        // List of OBIS values
        final LinkedList<String> obisStringValues;

        /**
         * Creates a new OBISDataLine
         *
         * @param obisId
         *            OBISIdentifer
         */
        OBISDataLine(String obisId) {
            this.obisId = obisId;

            obisStringValues = new LinkedList<String>();
        }

        @Override
        public String toString() {
            return "OBISDataLine [OBIS id:" + obisId + ", obis values:" + obisStringValues;
        }
    }
}
