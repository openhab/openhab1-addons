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

import java.util.EventListener;
import java.util.EventObject;

/**
 * PowerMax Alarm Event Listener interface. Handles incoming PowerMax Alarm events
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public interface PowerMaxEventListener extends EventListener {

    /**
     * Event handler method for incoming PowerMax Alarm events
     *
     * @param event
     *            the event object
     */
    public void powerMaxEventReceived(EventObject event);

}
