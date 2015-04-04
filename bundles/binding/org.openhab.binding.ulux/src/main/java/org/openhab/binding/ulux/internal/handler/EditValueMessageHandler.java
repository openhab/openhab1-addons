/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.handler;

import java.util.Map.Entry;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.internal.ump.UluxDatagram;
import org.openhab.binding.ulux.internal.ump.messages.EditValueMessage;
import org.openhab.core.types.Command;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
final class EditValueMessageHandler extends AbstractMessageHandler<EditValueMessage> {

	@Override
	public void handleMessage(EditValueMessage message, UluxDatagram response) {
		final short actorId = message.getActorId();
		final short value = message.getValue();

		for (Entry<String, UluxBindingConfig> entry : getBindingConfigs(actorId).entrySet()) {
			final String itemName = entry.getKey();

			// TODO
			final Command command;
			if (value > 1) {
				command = createCommand("DecimalType", value);
			} else {
				command = createCommand("OnOffType", value);
			}

			this.eventPublisher.postCommand(itemName, command);
		}
	}

}
