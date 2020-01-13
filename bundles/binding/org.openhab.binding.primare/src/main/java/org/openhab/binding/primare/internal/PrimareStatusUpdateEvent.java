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
package org.openhab.binding.primare.internal;

import java.util.EventObject;

/**
 * Listener interface for receiving status updates from Primare receiver.
 *
 * @author Pauli Anttila, Veli-Pekka Juslin
 * @since 1.7.0
 */
public class PrimareStatusUpdateEvent extends EventObject {

    private static final long serialVersionUID = -7109660432028125252L;

    public PrimareStatusUpdateEvent(Object source) {
        super(source);
    }

    /**
     * Invoked when received status updates from Primare receiver.
     * 
     * @param data
     *            Data from receiver.
     * 
     */
    public void StatusUpdateEventReceived(String ip, String data) {
    }

}
