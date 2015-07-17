/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.handler.messages;

import static org.openhab.binding.ulux.UluxBindingConfigType.LED;

import java.util.Map.Entry;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.internal.ump.UluxMessageDatagram;
import org.openhab.binding.ulux.internal.ump.messages.LedMessage;
import org.openhab.binding.ulux.internal.ump.messages.LedMessage.Led;
import org.openhab.core.library.types.DecimalType;

/**
 * @author Andreas Brenk
 * @since 1.8.0
 */
public final class LedMessageHandler extends AbstractMessageHandler<LedMessage> {

	@Override
	public void handleMessage(LedMessage message, UluxMessageDatagram response) {
		for (Entry<String, UluxBindingConfig> entry : getBindingConfigs(LED).entrySet()) {
			final String itemName = entry.getKey();
			final UluxBindingConfig config = entry.getValue();

			final Led led = config.getLed();
			final byte state = message.getState(led);
			// final Color color = message.getColor(led);

			this.eventPublisher.postUpdate(itemName, new DecimalType(state));
		}
	}

}
