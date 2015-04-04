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
import org.openhab.binding.ulux.internal.ump.messages.ControlMessage;
import org.openhab.binding.ulux.internal.ump.messages.DateTimeMessage;
import org.openhab.binding.ulux.internal.ump.messages.StateMessage;

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

		// TODO displayActive
		// TODO proximitySensor
	}

}
