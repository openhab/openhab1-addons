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
package org.openhab.binding.onewire.internal.listener;

import java.util.EventListener;

/**
 * This Interface definies a Listener for Items which wanted to be updated
 *
 * @author Dennis Riegelbauer
 * @since 1.7.0
 *
 */
public interface OneWireDevicePropertyWantsUpdateListener extends EventListener {

    /**
     * This method must be implemenented by the classes, which implements the Listener
     * 
     * @param wantsUpdateEvent
     */
    public void devicePropertyWantsUpdate(OneWireDevicePropertyWantsUpdateEvent wantsUpdateEvent);

}
