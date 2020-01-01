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
package org.openhab.binding.heatmiser.internal;

import java.util.EventObject;

/**
 * The listener interface for receiving data from Heatmiser connector.
 *
 * @author Chris Jackson
 * @since 1.4.0
 */
public class HeatmiserResponseEvent extends EventObject {

    private static final long serialVersionUID = 3821740012020068392L;

    public HeatmiserResponseEvent(Object source) {
        super(source);
    }

    /**
     * Invoked when data message is received from RFXCOM controller.
     * 
     * @param packet
     *            Data from controller.
     * 
     */
    public void DataReceivedEvent(byte[] packet) {
    }
}
