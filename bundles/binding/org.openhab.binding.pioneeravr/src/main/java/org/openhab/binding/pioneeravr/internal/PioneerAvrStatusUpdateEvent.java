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
package org.openhab.binding.pioneeravr.internal;

import java.util.EventObject;

/**
 * The listener interface for receiving status updates from Pioneer AVR receiver.
 *
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class PioneerAvrStatusUpdateEvent extends EventObject {

    private static final long serialVersionUID = -2256210413245865703L;

    public PioneerAvrStatusUpdateEvent(Object source) {
        super(source);
    }

    /**
     * Invoked when received status updates from pioneerav receiver.
     * 
     * @param data
     *            Data from receiver.
     * 
     */
    public void StatusUpdateEventReceived(String ip, String data) {
    }

}
