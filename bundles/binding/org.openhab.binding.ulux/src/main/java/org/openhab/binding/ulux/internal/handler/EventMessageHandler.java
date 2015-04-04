/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.handler;

import org.openhab.binding.ulux.internal.ump.UluxDatagram;
import org.openhab.binding.ulux.internal.ump.messages.EventMessage;
import org.openhab.binding.ulux.internal.ump.messages.EventMessage.Key;
import org.openhab.core.library.types.OnOffType;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
final class EventMessageHandler extends AbstractMessageHandler<EventMessage> {

	@Override
	public void handleMessage(EventMessage message, UluxDatagram response) {
		OnOffType key1 = message.isKeyPressed(Key.KEY_1) ? OnOffType.ON : OnOffType.OFF;
		OnOffType key2 = message.isKeyPressed(Key.KEY_2) ? OnOffType.ON : OnOffType.OFF;
		OnOffType key3 = message.isKeyPressed(Key.KEY_3) ? OnOffType.ON : OnOffType.OFF;
		OnOffType key4 = message.isKeyPressed(Key.KEY_4) ? OnOffType.ON : OnOffType.OFF;

		// TODO
		this.eventPublisher.postUpdate("Key_1", key1);
		this.eventPublisher.postUpdate("Key_2", key2);
		this.eventPublisher.postUpdate("Key_3", key3);
		this.eventPublisher.postUpdate("Key_4", key4);
	}

}
