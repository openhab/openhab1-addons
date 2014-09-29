/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public void updateItem(Item item, OmniLinkBindingConfig config,
			EventPublisher publisher) {
		// nothing to update

	}

}
