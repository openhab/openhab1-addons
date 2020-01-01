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

import com.digitaldan.jomnilinkII.MessageTypes.properties.ButtonProperties;

/**
 * Buttons are macros on a omni syste,
 *
 * @author Dan Cunningham
 * @since 1.5.0
 */
public class Button extends OmnilinkDevice {

    private ButtonProperties properties;

    public Button(ButtonProperties properties) {
        super();
        this.properties = properties;
    }

    @Override
    public ButtonProperties getProperties() {
        return properties;
    }

    @Override
    public void updateItem(Item item, OmniLinkBindingConfig config, EventPublisher publisher) {
        // nothing to update

    }

}
