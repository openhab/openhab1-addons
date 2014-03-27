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
public class RemoveFailedNodeMessageClass extends ZWaveCommandProcessor {
	private static final Logger logger = LoggerFactory.getLogger(RemoveFailedNodeMessageClass.class);

	public SerialMessage doRequest(int nodeId) {
		logger.debug("NODE {}: Marking node as having failed.", nodeId);

		// Queue the request
		SerialMessage newMessage = new SerialMessage(SerialMessageClass.RemoveFailedNodeID, SerialMessageType.Request, SerialMessageClass.RemoveFailedNodeID, SerialMessagePriority.High);
		byte[] newPayload = { (byte) nodeId };
    	newMessage.setMessagePayload(newPayload);
    	return newMessage;
   	}
	
	@Override
	public boolean handleResponse(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		logger.debug("Got RemoveFailedNode response.");
		
		if(incomingMessage.getMessagePayloadByte(0) == 0x00) {
			logger.debug("Remove failed node successfully placed on stack.");
		} else {
			logger.error("Remove failed node not placed on stack due to error 0x{}.", Integer.toHexString(incomingMessage.getMessagePayloadByte(0)));
			transactionComplete = true;
		}
		
		return true;
	}

	@Override
	public boolean handleRequest(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		int nodeId = lastSentMessage.getMessagePayloadByte(0);

		logger.debug("NODE {}: Got RemoveFailedNode request.", nodeId);
		if(incomingMessage.getMessagePayloadByte(0) != 0x00) {
			logger.error("NODE {}: Remove failed node failed with error 0x{}.", nodeId, Integer.toHexString(incomingMessage.getMessagePayloadByte(0)));
		}
		transactionComplete = true;
		
		return true;
	}
}
