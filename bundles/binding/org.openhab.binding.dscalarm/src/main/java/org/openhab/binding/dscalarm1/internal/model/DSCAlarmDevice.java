/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
