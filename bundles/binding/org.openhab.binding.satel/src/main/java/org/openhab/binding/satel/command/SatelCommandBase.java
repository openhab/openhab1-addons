/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.command;

import org.openhab.binding.satel.internal.event.EventDispatcher;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all command classes.
 *
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public abstract class SatelCommandBase extends SatelMessage implements SatelCommand {

    private static final Logger logger = LoggerFactory.getLogger(SatelCommandBase.class);

    /**
     * Used in extended (INT-RS v2.xx) command version.
     */
    protected static final byte[] EXTENDED_CMD_PAYLOAD = { 0x00 };

    private static final byte COMMAND_RESULT_CODE = (byte) 0xef;

    private volatile State state = State.NEW;
    protected SatelMessage response;

    /**
     * Creates new command basing on command code and extended command flag.
     *
     * @param commandCode
     *            command code
     * @param extended
     *            if <code>true</code> command will be sent as extended (256
     *            zones or outputs)
     */
    public SatelCommandBase(byte commandCode, boolean extended) {
        this(commandCode, extended ? EXTENDED_CMD_PAYLOAD : EMPTY_PAYLOAD);
    }

    /**
     * Creates new instance with specified command code and payload.
     *
     * @param command
     *            command code
     * @param payload
     *            command payload
     */
    public SatelCommandBase(byte commandCode, byte[] payload) {
        super(commandCode, payload);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State getState() {
        return state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setState(State state) {
        synchronized (this) {
            this.state = state;
            this.notifyAll();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SatelMessage getRequest() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handleResponse(EventDispatcher eventDispatcher, SatelMessage response) {
        // if response is valid, store it for future use
        if (response == null) {
            return false;
        }
        // TODO consider logging request and response messages for commands that failed
        if (response.getCommand() == COMMAND_RESULT_CODE) {
            if (!hasCommandSucceeded(response)) {
                return false;
            }
        } else if (!isResponseValid(response)) {
            return false;
        }
        this.response = response;
        return true;
    }

    /**
     * Checks whether given response is valid for the command.
     *
     * @param response message to check
     * @return <code>true</code> if given message is valid response for the command
     */
    protected abstract boolean isResponseValid(SatelMessage response);

    protected static int bcdToInt(byte[] bytes, int offset, int size) {
        int result = 0, digit;
        int byteIdx = offset;
        int digits = size * 2;
        for (int i = 0; i < digits; ++i) {
            if (i % 2 == 0) {
                digit = (bytes[byteIdx] >> 4) & 0x0f;
            } else {
                digit = (bytes[byteIdx]) & 0x0f;
                byteIdx += 1;
            }
            result = result * 10 + digit;
        }
        return result;
    }

    protected static boolean hasCommandSucceeded(SatelMessage response) {
        // validate response message
        if (response.getCommand() != COMMAND_RESULT_CODE) {
            logger.error("Invalid response code: {}", response.getCommand());
            return false;
        }
        if (response.getPayload().length != 1) {
            logger.error("Invalid payload length: {}", response.getPayload().length);
            return false;
        }

        // check result code
        byte responseCode = response.getPayload()[0];
        String errorMsg;

        switch (responseCode) {
            case 0:
                // success
                return true;
            case 0x01:
                errorMsg = "Requesting user code not found";
                break;
            case 0x02:
                errorMsg = "No access";
                break;
            case 0x03:
                errorMsg = "Selected user does not exist";
                break;
            case 0x04:
                errorMsg = "Selected user already exists";
                break;
            case 0x05:
                errorMsg = "Wrong code or code already exists";
                break;
            case 0x06:
                errorMsg = "Telephone code already exists";
                break;
            case 0x07:
                errorMsg = "Changed code is the same";
                break;
            case 0x08:
                errorMsg = "Other error";
                break;
            case 0x11:
                errorMsg = "Cannot arm, but can use force arm";
                break;
            case 0x12:
                errorMsg = "Cannot arm";
                break;
            case (byte) 0xff:
                logger.trace("Command accepted");
                return true;
            default:
                if (responseCode >= 0x80 && responseCode <= 0x8f) {
                    errorMsg = String.format("Other error: %02X", responseCode);
                } else {
                    errorMsg = String.format("Unknown result code: %02X", responseCode);
                }
        }

        logger.error(errorMsg);
        return false;
    }

}
