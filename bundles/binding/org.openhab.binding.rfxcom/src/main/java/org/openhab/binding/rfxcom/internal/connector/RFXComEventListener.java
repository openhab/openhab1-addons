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
package org.openhab.binding.rfxcom.internal.connector;

import java.util.EventListener;
import java.util.EventObject;

/**
 * This interface defines interface to receive data from RFXCOM controller.
 *
 * @author Pauli Anttila
 * @since 1.2.0
 */
public interface RFXComEventListener extends EventListener {

    /**
     * Procedure for receive raw data from RFXCOM controller.
     * 
     * @param data
     *            Received raw data.
     */
    void packetReceived(EventObject event, byte[] data);

}
