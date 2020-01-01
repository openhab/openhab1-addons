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
package org.openhab.binding.ihc.ws;

import java.util.EventListener;
import java.util.EventObject;

import org.openhab.binding.ihc.ws.datatypes.WSControllerState;
import org.openhab.binding.ihc.ws.datatypes.WSResourceValue;

/**
 * This interface defines interface to receive updates from IHC controller.
 *
 * @author Pauli Anttila
 * @since 1.3.0
 */
public interface IhcEventListener extends EventListener {

    /**
     * Event for receive status update from IHC controller.
     * 
     * @param status
     *            Received status update from controller.
     */
    void statusUpdateReceived(EventObject event, WSControllerState status);

    /**
     * Event for receive resource value updates from IHC controller.
     * 
     * @param value
     *            Received value update from controller.
     */
    void resourceValueUpdateReceived(EventObject event, WSResourceValue value);

    /**
     * Event for fatal error on communication to IHC controller.
     * 
     * @param e
     *            IhcException occurred.
     */
    void errorOccured(EventObject event, IhcExecption e);

}
