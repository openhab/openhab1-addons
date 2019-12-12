/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.dscalarm1.internal.model;

import org.openhab.binding.dscalarm1.DSCAlarmBindingConfig;
import org.openhab.binding.dscalarm1.internal.DSCAlarmEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;

/**
 * Abstract class for DSC Alarm devices
 *
 * @author Russell Stephens
 * @since 1.6.0
 */
public abstract class DSCAlarmDevice {

    /**
     * Refresh an openHAB item
     *
     * @param item
     * @param config
     * @param publisher
     * @param state
     * @param description
     */
    public abstract void refreshItem(Item item, DSCAlarmBindingConfig config, EventPublisher publisher, int state, String description);

    /**
     * Update an openHAB item after receiving a DSC Alarm event
     *
     * @param item
     * @param config
     * @param publisher
     * @param event
     */
    public abstract void handleEvent(Item item, DSCAlarmBindingConfig config, EventPublisher publisher, DSCAlarmEvent event);

    /**
     * Update a DSC Alarm Device Property
     *
     * @param item
     * @param config
     * @param publisher
     */
}
