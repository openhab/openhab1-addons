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
public class SetSucNodeMessageClass extends ZWaveCommandProcessor {
	private static final Logger logger = LoggerFactory.getLogger(SetSucNodeMessageClass.class);

	public SerialMessage doRequest(int nodeId, SUCType type) {
		logger.debug("NODE {}: SetSucNodeID node as {}", nodeId, type.toString());

		// Queue the request
		SerialMessage newMessage = new SerialMessage(SerialMessageClass.SetSucNodeID, SerialMessageType.Request,
				SerialMessageClass.SetSucNodeID, SerialMessagePriority.High);
		byte[] newPayload = new byte[5];
		newPayload[0] = (byte)nodeId;
		switch(type) {
			case NONE:
				newPayload[1] = 0;
				newPayload[3] = 0;
				break;
			case BASIC:
				newPayload[1] = 1;
				newPayload[3] = 0;
				break;
			case SERVER:
				newPayload[1] = 1;
				newPayload[3] = 1;
				break;
		}

		newPayload[2] = 0;				// Low power option = false
		newPayload[4] = 1;				// Callback!!!
		newMessage.setMessagePayload(newPayload);
    	return newMessage;
	}

	@Override
	public boolean handleResponse(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		int nodeId = lastSentMessage.getMessagePayloadByte(0);
		
		logger.debug("NODE {}: SetSucNodeID node response.", nodeId);

		if(incomingMessage.getMessagePayloadByte(0) != 0x00) {
			logger.debug("NODE {}: SetSucNodeID command OK.", nodeId);
		} else {
			logger.error("NODE {}: SetSucNodeID command failed.", nodeId);
			checkTransactionComplete(lastSentMessage, incomingMessage);
		}
		
		return false;
	}

	@Override
	public boolean handleRequest(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		int nodeId = lastSentMessage.getMessagePayloadByte(0);

		logger.debug("NODE {}: SetSucNodeID node request.", nodeId);

		if (incomingMessage.getMessagePayloadByte(1) != 0x00) {
			logger.error("NODE {}: SetSucNodeID failed with error 0x{}.", nodeId,
					Integer.toHexString(incomingMessage.getMessagePayloadByte(1)));
		} else {
		}

		checkTransactionComplete(lastSentMessage, incomingMessage);
		return false;
	}
	
	public enum SUCType {
		NONE, BASIC, SERVER
	}

}
