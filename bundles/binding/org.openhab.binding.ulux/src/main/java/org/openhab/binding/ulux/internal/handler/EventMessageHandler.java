/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.handler;

import static org.openhab.binding.ulux.UluxBindingConfigType.KEY;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.internal.ump.UluxMessageDatagram;
import org.openhab.binding.ulux.internal.ump.messages.EventMessage;
import org.openhab.binding.ulux.internal.ump.messages.EventMessage.Key;
import org.openhab.core.library.types.OnOffType;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
final class EventMessageHandler extends AbstractMessageHandler<EventMessage> {

	@Override
	public void handleMessage(EventMessage message, UluxMessageDatagram response) {
		final Map<Key, OnOffType> keyStates = new EnumMap<Key, OnOffType>(Key.class);
		keyStates.put(Key.KEY_1, message.isKeyPressed(Key.KEY_1) ? OnOffType.ON : OnOffType.OFF);
		keyStates.put(Key.KEY_2, message.isKeyPressed(Key.KEY_2) ? OnOffType.ON : OnOffType.OFF);
		keyStates.put(Key.KEY_3, message.isKeyPressed(Key.KEY_3) ? OnOffType.ON : OnOffType.OFF);
		keyStates.put(Key.KEY_4, message.isKeyPressed(Key.KEY_4) ? OnOffType.ON : OnOffType.OFF);

		for (Entry<String, UluxBindingConfig> entry : getBindingConfigs(KEY).entrySet()) {
			final String itemName = entry.getKey();
			final UluxBindingConfig config = entry.getValue();

			this.eventPublisher.postUpdate(itemName, keyStates.get(config.getKey()));
		}
	}

}
