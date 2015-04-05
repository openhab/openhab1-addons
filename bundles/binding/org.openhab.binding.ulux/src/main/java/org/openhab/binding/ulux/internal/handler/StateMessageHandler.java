/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.handler;

import static org.openhab.binding.ulux.UluxBindingConfig.TYPE_AMBIENT_LIGHT;
import static org.openhab.binding.ulux.UluxBindingConfig.TYPE_DISPLAY;
import static org.openhab.binding.ulux.UluxBindingConfig.TYPE_PROXIMITY;

import java.util.Map.Entry;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.internal.ump.UluxDatagram;
import org.openhab.binding.ulux.internal.ump.messages.ControlMessage;
import org.openhab.binding.ulux.internal.ump.messages.DateTimeMessage;
import org.openhab.binding.ulux.internal.ump.messages.StateMessage;
import org.openhab.core.library.types.OnOffType;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
final class StateMessageHandler extends AbstractMessageHandler<StateMessage> {

	@Override
	public void handleMessage(StateMessage message, UluxDatagram response) {
		if (message.isInitRequest()) {
			response.addMessage(new ControlMessage());
		}

		if (message.isTimeRequest()) {
			response.addMessage(new DateTimeMessage());
		}

		// display state
		for (Entry<String, UluxBindingConfig> entry : getBindingConfigs(TYPE_DISPLAY).entrySet()) {
			final OnOffType newState = message.isDisplayActive() ? OnOffType.ON : OnOffType.OFF;

			this.eventPublisher.postUpdate(entry.getKey(), newState);
		}

		// proximity sensor
		for (Entry<String, UluxBindingConfig> entry : getBindingConfigs(TYPE_PROXIMITY).entrySet()) {
			final OnOffType newState = message.isProximityDetected() ? OnOffType.ON : OnOffType.OFF;

			this.eventPublisher.postUpdate(entry.getKey(), newState);
		}

		// ambient light sensor
		for (Entry<String, UluxBindingConfig> entry : getBindingConfigs(TYPE_AMBIENT_LIGHT).entrySet()) {
			final OnOffType newState = message.isAmbientLightBright() ? OnOffType.ON : OnOffType.OFF;

			this.eventPublisher.postUpdate(entry.getKey(), newState);
		}
	}

}
