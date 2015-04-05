/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.handler;

import static org.openhab.binding.ulux.UluxBindingConfigType.LUX;

import java.util.Map.Entry;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.internal.ump.UluxDatagram;
import org.openhab.binding.ulux.internal.ump.messages.LuxMessage;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.UnDefType;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
final class LuxMessageHandler extends AbstractMessageHandler<LuxMessage> {

	@Override
	public void handleMessage(LuxMessage message, UluxDatagram response) {
		for (Entry<String, UluxBindingConfig> entry : getBindingConfigs(LUX).entrySet()) {
			final String itemName = entry.getKey();

			if (message.isValid()) {
				this.eventPublisher.postUpdate(itemName, new DecimalType(message.getLux()));
			} else {
				this.eventPublisher.postUpdate(itemName, UnDefType.UNDEF);
			}
		}
	}

}
