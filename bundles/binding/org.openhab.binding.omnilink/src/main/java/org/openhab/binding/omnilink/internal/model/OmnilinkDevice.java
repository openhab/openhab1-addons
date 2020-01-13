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
package org.openhab.binding.omnilink.internal.model;

import org.openhab.binding.omnilink.internal.OmniLinkBindingConfig;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;

import com.digitaldan.jomnilinkII.MessageTypes.ObjectProperties;

/**
 * Base class for omni devices
 *
 * @author Dan Cunningham
 * @since 1.5.0
 */
public abstract class OmnilinkDevice {

    /**
     * Return the Object properties for a given device
     * 
     * @return
     */
    public abstract ObjectProperties getProperties();

    /**
     * Update a openhab item for this device.
     * 
     * @param item
     * @param config
     * @param publisher
     */
    public abstract void updateItem(Item item, OmniLinkBindingConfig config, EventPublisher publisher);
}
