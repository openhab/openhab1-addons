/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.powermax.internal.connector;

import java.util.EventObject;

import org.openhab.binding.powermax.internal.message.PowerMaxBaseMessage;

/**
 * Event for messages received from the Visonic alarm panel
 *
 * @author lolodomo
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
