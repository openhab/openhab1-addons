/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.handler;

import org.openhab.binding.ulux.internal.ump.UluxMessageDatagram;
import org.openhab.binding.ulux.internal.ump.messages.IdListMessage;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
final class IdListMessageHandler extends AbstractMessageHandler<IdListMessage> {

	@Override
	public void handleMessage(IdListMessage message, UluxMessageDatagram response) {
		// nothing to do
	}

}
