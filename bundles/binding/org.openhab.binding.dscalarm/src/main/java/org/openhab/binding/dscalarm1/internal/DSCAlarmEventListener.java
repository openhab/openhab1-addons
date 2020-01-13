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

import java.util.EventListener;
import java.util.EventObject;

/**
 * DSC Alarm Event Listener interface. Handles incoming DSC Alarm events
 *
 * @author Russell Stephens
 * @since 1.6.0
 */
public interface DSCAlarmEventListener extends EventListener {

    /**
     * Event handler method for incoming DSC Alarm events
     * 
     * @param event.
     */
    void dscAlarmEventRecieved(EventObject event);
}
