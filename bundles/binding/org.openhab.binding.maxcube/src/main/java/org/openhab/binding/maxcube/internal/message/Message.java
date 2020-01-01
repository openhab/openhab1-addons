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
package org.openhab.binding.maxcube.internal.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base message received by the MAX!Cube protocol.
 * 
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public abstract class Message {

    public static final String DELIMETER = ",";
    protected final static Logger logger = LoggerFactory.getLogger(Device.class);

    private String raw = null;

    public Message(String raw) {
        this.raw = raw;
    }

    public abstract void debug(Logger logger);

    public abstract MessageType getType();

    protected final String getPayload() {
        return raw.substring(2, raw.length());
    }
}
