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
package org.openhab.binding.zwave.internal.protocol;

import org.openhab.binding.zwave.internal.protocol.event.ZWaveEvent;

/**
 * ZWave Event Listener interface. Classes that implement this interface
 * need to be able to handle incoming ZWaveEvent events.
 *
 * @author Brian Crosby
 * @since 1.3.0
 */
public interface ZWaveEventListener {

    /**
     * Event handler method for incoming Z-Wave events.
     *
     * @param event the incoming Z-Wave event.
     */
    void ZWaveIncomingEvent(ZWaveEvent event);
}
