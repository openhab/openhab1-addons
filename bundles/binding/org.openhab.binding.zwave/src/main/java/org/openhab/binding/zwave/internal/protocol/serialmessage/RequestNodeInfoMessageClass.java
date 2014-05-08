/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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
 * @since 1.5.0
 */
public class RequestNodeInfoMessageClass  extends ZWaveCommandProcessor {
	private static final Logger logger = LoggerFactory.getLogger(RequestNodeInfoMessageClass.class);

	public SerialMessage doRequest(int nodeId) {
		SerialMessage newMessage = new SerialMessage(nodeId, SerialMessageClass.RequestNodeInfo, SerialMessageType.Request, SerialMessageClass.ApplicationUpdate, SerialMessagePriority.High);
    	byte[] newPayload = { (byte) nodeId };
    	newMessage.setMessagePayload(newPayload);
    	return newMessage;
	}

	@Override
	public boolean handleResponse(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		logger.trace("Handle RequestNodeInfo Response");
		if(incomingMessage.getMessagePayloadByte(0) != 0x00)
			logger.debug("Request node info successfully placed on stack.");
		else
			logger.error("Request node info not placed on stack due to error.");
		
		checkTransactionComplete(lastSentMessage, incomingMessage);

		return true;
	}
}
