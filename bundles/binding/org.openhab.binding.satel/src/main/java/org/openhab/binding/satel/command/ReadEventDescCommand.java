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
package org.openhab.binding.satel.command;

import java.io.UnsupportedEncodingException;

import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command class for command that reads description for specific event
 * code.
 *
 * @author Krzysztof Goworek
 * @since 1.9.0
 */
public class ReadEventDescCommand extends SatelCommandBase {

    private static final Logger logger = LoggerFactory.getLogger(ReadEventDescCommand.class);

    public static final byte COMMAND_CODE = (byte) 0x8f;

    /**
     * Creates new command class instance to read description for given parameters.
     *
     * @param eventCode event code
     * @param restore <code>true</code> if this is restoration
     * @param longDescription <code>true</code> for long description, <code>false</code> for short one
     */
    public ReadEventDescCommand(int eventCode, boolean restore, boolean longDescription) {
        super(COMMAND_CODE, buildPayload(eventCode, restore, longDescription));
    }

    private static byte[] buildPayload(int eventCode, boolean restore, boolean longDescription) {
        int firstByte = 0;
        if (restore) {
            firstByte |= 0x04;
        }
        if (longDescription) {
            firstByte |= 0x80;
        }
        firstByte |= ((eventCode >> 8) & 0x03);
        return new byte[] { (byte) firstByte, (byte) (eventCode & 0xff) };
    }

    /**
     * Returns type of requested description, either long or short.
     *
     * @return <code>true</code> if long description has been requested
     */
    public boolean isLongDescription() {
        return (getRequest().getPayload()[0] & 0x80) != 0;
    }

    /**
     * Returns text of the description decoded using given encoding.
     * Encoding depends on firmware language and must be specified in the binding configuration.
     *
     * @param encoding encoding for the text
     * @return text of the description
     * @throws UnsupportedEncodingException if provided encoding is not supported
     */
    public String getText(String encoding) throws UnsupportedEncodingException {
        int length = isLongDescription() ? 46 : 16;
        return new String(response.getPayload(), 5, length, encoding).trim();
    }

    /**
     * Returns kind of description, either short or long, depending on the request.
     *
     * @return kind of description
     */
    public int getKind() {
        if (isLongDescription()) {
            return response.getPayload()[2] & 0xff;
        } else {
            return ((response.getPayload()[3] & 0xff) << 8) + (response.getPayload()[4] & 0xff);
        }
    }

    @Override
    protected boolean isResponseValid(SatelMessage response) {
        // validate response
        if (response.getCommand() != COMMAND_CODE) {
            logger.error("Invalid response code: {}", response.getCommand());
            return false;
        }
        int properLength = isLongDescription() ? 51 : 21;
        if (response.getPayload().length != properLength) {
            logger.error("Invalid payload length: {}", response.getPayload().length);
            return false;
        }
        return true;
    }

}
