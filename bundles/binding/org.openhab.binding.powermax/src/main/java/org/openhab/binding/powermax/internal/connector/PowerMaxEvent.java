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
package org.openhab.binding.powermax.internal.connector;

import java.util.EventObject;

import org.openhab.binding.powermax.internal.message.PowerMaxBaseMessage;

/**
 * Event for messages received from the Visonic alarm panel
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    private PowerMaxBaseMessage powerMaxMessage;

    /**
     * Constructor
     *
     * @param source
     * @param powerMaxMessage
     *            the message object built from the received message
     */
    public PowerMaxEvent(Object source, PowerMaxBaseMessage powerMaxMessage) {
        super(source);
        this.powerMaxMessage = powerMaxMessage;
    }

    /**
     * Returns the message object build from the received message
     *
     * @return powerMaxMessage: the message object built from the received message
     */
    public PowerMaxBaseMessage getPowerMaxMessage() {
        return powerMaxMessage;
    }

}
