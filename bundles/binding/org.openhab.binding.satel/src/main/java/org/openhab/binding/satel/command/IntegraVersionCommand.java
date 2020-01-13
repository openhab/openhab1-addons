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

import org.openhab.binding.satel.internal.event.EventDispatcher;
import org.openhab.binding.satel.internal.event.IntegraVersionEvent;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command class for command that returns Integra version and type.
 *
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class IntegraVersionCommand extends SatelCommandBase {

    private static final Logger logger = LoggerFactory.getLogger(IntegraVersionCommand.class);

    public static final byte COMMAND_CODE = 0x7e;

    /**
     * Creates new command class instance.
     */
    public IntegraVersionCommand() {
        super(COMMAND_CODE, false);
    }

    /**
     * @return Integra firmware version and release date
     */
    public String getVersion() {
        // build version string
        String verStr = new String(response.getPayload(), 1, 1) + "." + new String(response.getPayload(), 2, 2) + " "
                + new String(response.getPayload(), 4, 4) + "-" + new String(response.getPayload(), 8, 2) + "-"
                + new String(response.getPayload(), 10, 2);
        return verStr;
    }

    /**
     * @return Integra type
     */
    public byte getType() {
        return response.getPayload()[0];
    }

    /**
     * @return firmware language
     */
    public byte getLanguage() {
        return response.getPayload()[12];
    }

    /**
     * @return <code>true</code> if alarm settings are stored in flash memory
     */
    public boolean areSettingsInFlash() {
        return response.getPayload()[13] == (byte) 0xFF;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handleResponse(EventDispatcher eventDispatcher, SatelMessage response) {
        if (super.handleResponse(eventDispatcher, response)) {
            // dispatch version event
            eventDispatcher.dispatchEvent(
                    new IntegraVersionEvent(getType(), getVersion(), getLanguage(), areSettingsInFlash()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean isResponseValid(SatelMessage response) {
        // validate response
        if (response.getCommand() != COMMAND_CODE) {
            logger.error("Invalid response code: {}", response.getCommand());
            return false;
        }
        if (response.getPayload().length != 14) {
            logger.error("Invalid payload length: {}", response.getPayload().length);
            return false;
        }
        return true;
    }

}
