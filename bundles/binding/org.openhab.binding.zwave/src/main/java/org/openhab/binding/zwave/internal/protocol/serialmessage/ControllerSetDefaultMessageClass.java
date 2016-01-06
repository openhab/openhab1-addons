/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.serialmessage;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class processes a serial message from the zwave controller
 * @author Chris Jackson
 * @since 1.7.0
 */
public class ControllerSetDefaultMessageClass extends ZWaveCommandProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ControllerSetDefaultMessageClass.class);
	
	public SerialMessage doRequest() {
		return new SerialMessage(SerialMessageClass.SetDefault, SerialMessageType.Request, SerialMessageClass.SetDefault, SerialMessagePriority.High);
	}

	@Override
	public boolean handleRequest(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		logger.debug(String.format("Received SetDefault request"));

		checkTransactionComplete(lastSentMessage, incomingMessage);
		
		return true;
	}
}
