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

import java.util.EventListener;
import java.util.EventObject;

/**
 * This interface defines an interface to receive data from the Heatmiser thermostats.
 *
 * @author Chris Jackson
 * @since 1.4.0
 */
public interface HeatmiserEventListener extends EventListener {

    /**
     * Receive data from the Heatmiser interface.
     * 
     * @param data
     */
    void packetReceived(EventObject event, byte[] data);

}
