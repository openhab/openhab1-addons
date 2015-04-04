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

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
final class ControlMessageHandler extends AbstractMessageHandler<ControlMessage> {

	@Override
	public void handleMessage(ControlMessage message, UluxDatagram response) {
		// TODO backgroundLight
		// TODO lockMode
	}

}
