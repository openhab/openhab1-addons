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
package org.openhab.binding.dscalarm1.internal;

import java.util.EventObject;

import org.openhab.binding.dscalarm1.internal.protocol.APIMessage;

/**
 * Event for Receiving API Messages
 *
 * @author Russell Stephens
 * @since 1.6.0
 */
public class DSCAlarmEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    private APIMessage apiMessage;

    /**
     * Constructor
     * 
     * @param source
     */
    public DSCAlarmEvent(Object source) {
        super(source);
    }

    /**
     * Adds the the received API Message to the event
     * 
     * @param apiMessage
     */
    public void dscAlarmEventMessage(APIMessage apiMessage) {
        this.apiMessage = apiMessage;
    }

    /**
     * Returns the API Message event from the DSC Alarm System
     * 
     * @return apiMessage
     */
    public APIMessage getAPIMessage() {
        return apiMessage;
    }

}
