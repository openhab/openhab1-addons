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

import java.util.EventObject;

/**
 * EventClass for InterfaceOneWireDevicePropertyWantsUpdateListener
 *
 * @author Dennis Riegelbauer
 * @since 1.7.0
 *
 */
public class OneWireDevicePropertyWantsUpdateEvent extends EventObject {

    private static final long serialVersionUID = -6971853348646127138L;

    /**
     * Item name
     */
    private String ivItemName = null;

    /**
     * @param pvSource
     * @param pvItemName
     */
    public OneWireDevicePropertyWantsUpdateEvent(Object pvSource, String pvItemName) {
        super(pvSource);
        this.ivItemName = pvItemName;
    }

    /**
     * @return name of the item which wants to be updated
     */
    public String getItemName() {
        return ivItemName;
    }

}
