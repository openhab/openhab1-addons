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

import java.util.EventListener;
import java.util.EventObject;

/**
 * Interface for receiving status updates from Primare
 *
 * @author Pauli Anttila, Veli-Pekka Juslin
 * @since 1.7.0
 */
public interface PrimareEventListener extends EventListener {

    /**
     * Receive status update from Primare device
     * 
     * @param data
     *            Received data.
     */
    void statusUpdateReceived(EventObject event, String deviceId, byte[] data);

}
