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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class processes a serial message from the zwave controller
 * @author Chris Jackson
 * @since 1.5.0
 */
public class AddNodeMessageClass extends ZWaveCommandProcessor {
	private static final Logger logger = LoggerFactory.getLogger(AddNodeMessageClass.class);

	private final int ADD_NODE_ANY                       = 0x01;
	private final int ADD_NODE_CONTROLLER                = 0x02;
	private final int ADD_NODE_SLAVE                     = 0x03;
	private final int ADD_NODE_EXISTING                  = 0x04;
	private final int ADD_NODE_STOP                      = 0x05;
	private final int ADD_NODE_STOP_FAILED               = 0x06;

	private final int ADD_NODE_STATUS_LEARN_READY        = 0x01;
	private final int ADD_NODE_STATUS_NODE_FOUND         = 0x02;
	private final int ADD_NODE_STATUS_ADDING_SLAVE       = 0x03;
	private final int ADD_NODE_STATUS_ADDING_CONTROLLER  = 0x04;
	private final int ADD_NODE_STATUS_PROTOCOL_DONE      = 0x05;
	private final int ADD_NODE_STATUS_DONE               = 0x06;
	private final int ADD_NODE_STATUS_FAILED             = 0x07;
	
	private final int OPTION_HIGH_POWER                  = 0x80;

	public SerialMessage doRequestStart(boolean highPower) {
		logger.debug("Setting controller into INCLUSION mode.");

		// Queue the request
		SerialMessage newMessage = new SerialMessage(SerialMessage.SerialMessageClass.AddNodeToNetwork, SerialMessage.SerialMessageType.Request,
				SerialMessage.SerialMessageClass.AddNodeToNetwork, SerialMessage.SerialMessagePriority.High);
		byte[] newPayload = { (byte) ADD_NODE_ANY };
		if(highPower == true)
			newPayload[0] |= OPTION_HIGH_POWER;

    	newMessage.setMessagePayload(newPayload);
    	return newMessage;
    }

	public SerialMessage doRequestStop() {
		logger.debug("Ending INCLUSION mode.");

		// Queue the request
		SerialMessage newMessage = new SerialMessage(SerialMessage.SerialMessageClass.AddNodeToNetwork, SerialMessage.SerialMessageType.Request,
				SerialMessage.SerialMessageClass.AddNodeToNetwork, SerialMessage.SerialMessagePriority.High);
		byte[] newPayload = { (byte) ADD_NODE_STOP };

    	newMessage.setMessagePayload(newPayload);
    	return newMessage;
    }

	@Override
	public boolean handleRequest(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		switch(incomingMessage.getMessagePayloadByte(0)) {
		case ADD_NODE_STATUS_LEARN_READY:
			logger.debug("Learn ready.");
			break;
		case ADD_NODE_STATUS_NODE_FOUND:
			logger.debug("Node found.");
			break;
		case ADD_NODE_STATUS_ADDING_SLAVE:
			logger.debug("Adding slave {}.", incomingMessage.getMessagePayloadByte(1));
			break;
		case ADD_NODE_STATUS_ADDING_CONTROLLER:
			logger.debug("Adding controller {}.", incomingMessage.getMessagePayloadByte(1));
			break;
		case ADD_NODE_STATUS_PROTOCOL_DONE:
			logger.debug("Protocol done.");
			doRequestStop();
			break;
		case ADD_NODE_STATUS_DONE:
			logger.debug("Done.");
			break;
		case ADD_NODE_STATUS_FAILED:
			logger.debug("Failed.");
			doRequestStop();
			break;
		default:
			logger.debug("Unknown request ({}).", incomingMessage.getMessagePayloadByte(0));
			break;
		}
		checkTransactionComplete(lastSentMessage, incomingMessage);

		return transactionComplete;
	}

	@Override
	public boolean handleResponse(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		logger.debug("handleResponse.");
		checkTransactionComplete(lastSentMessage, incomingMessage);
		
		return true;
	}
}
