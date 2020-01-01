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
package org.openhab.binding.insteonplm.internal.message;

/**
 * Interface to receive Insteon messages from the modem.
 *
 * @author Bernd Pfrommer
 * @since 1.5.0
 */
public interface MsgListener {
    /**
     * Invoked whenever a valid message comes in from the modem
     * 
     * @param msg the message received
     * @param fromPort on which port (e.g. '/dev/ttyUSB0') this message arrived
     */
    public abstract void msg(Msg msg, String fromPort);
}
