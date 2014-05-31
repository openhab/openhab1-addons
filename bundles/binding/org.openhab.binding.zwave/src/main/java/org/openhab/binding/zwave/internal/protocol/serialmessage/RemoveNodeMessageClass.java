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
import org.openhab.binding.zwave.internal.protocol.event.ZWaveInclusionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class processes a serial message from the zwave controller
 * @author Chris Jackson
 * @since 1.5.0
 */
public class RemoveNodeMessageClass extends ZWaveCommandProcessor {
	private static final Logger logger = LoggerFactory.getLogger(RemoveNodeMessageClass.class);

	private final int REMOVE_NODE_ANY                        = 0x01;
	private final int REMOVE_NODE_CONTROLLER                 = 0x02;
	private final int REMOVE_NODE_SLAVE                      = 0x03;
	private final int REMOVE_NODE_STOP                       = 0x05;

	private final int REMOVE_NODE_STATUS_LEARN_READY         = 0x01;
	private final int REMOVE_NODE_STATUS_NODE_FOUND          = 0x02;
	private final int REMOVE_NODE_STATUS_REMOVING_SLAVE      = 0x03;
	private final int REMOVE_NODE_STATUS_REMOVING_CONTROLLER = 0x04;
	private final int REMOVE_NODE_STATUS_DONE                = 0x06;
	private final int REMOVE_NODE_STATUS_FAILED              = 0x07;
	
	public SerialMessage doRequestStart(boolean highPower) {
		logger.debug("Setting controller into EXCLUSION mode.");

		// Queue the request
		SerialMessage newMessage = new SerialMessage(SerialMessage.SerialMessageClass.RemoveNodeFromNetwork, SerialMessage.SerialMessageType.Request,
				SerialMessage.SerialMessageClass.RemoveNodeFromNetwork, SerialMessage.SerialMessagePriority.High);
		byte[] newPayload = { (byte) REMOVE_NODE_ANY, (byte)255 };

    	newMessage.setMessagePayload(newPayload);
    	return newMessage;
    }

	public SerialMessage doRequestStop() {
		logger.debug("Ending EXCLUSION mode.");

		// Queue the request
		SerialMessage newMessage = new SerialMessage(SerialMessage.SerialMessageClass.RemoveNodeFromNetwork, SerialMessage.SerialMessageType.Request,
				SerialMessage.SerialMessageClass.RemoveNodeFromNetwork, SerialMessage.SerialMessagePriority.High);
		byte[] newPayload = { (byte) REMOVE_NODE_STOP };

    	newMessage.setMessagePayload(newPayload);
    	return newMessage;
    }

	@Override
	public boolean handleRequest(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		switch(incomingMessage.getMessagePayloadByte(1)) {
		case REMOVE_NODE_STATUS_LEARN_READY:
			logger.debug("Learn ready.");
			zController.notifyEventListeners(new ZWaveInclusionEvent(ZWaveInclusionEvent.Type.ExcludeStart));
			break;
		case REMOVE_NODE_STATUS_NODE_FOUND:
			logger.debug("Node found for removal.");
			break;
		case REMOVE_NODE_STATUS_REMOVING_SLAVE:
			logger.debug("NODE {}: Removing slave.", incomingMessage.getMessagePayloadByte(2));
			zController.notifyEventListeners(new ZWaveInclusionEvent(ZWaveInclusionEvent.Type.ExcludeSlaveFound, incomingMessage.getMessagePayloadByte(2)));
			break;
		case REMOVE_NODE_STATUS_REMOVING_CONTROLLER:
			logger.debug("NODE {}: Removing controller.", incomingMessage.getMessagePayloadByte(2));
			zController.notifyEventListeners(new ZWaveInclusionEvent(ZWaveInclusionEvent.Type.ExcludeControllerFound, incomingMessage.getMessagePayloadByte(2)));
			break;
		case REMOVE_NODE_STATUS_DONE:
			logger.debug("NODE {}: Removed from network.", incomingMessage.getMessagePayloadByte(2));
			zController.sendData(doRequestStop());
			zController.notifyEventListeners(new ZWaveInclusionEvent(ZWaveInclusionEvent.Type.ExcludeDone, incomingMessage.getMessagePayloadByte(2)));
			break;
		case REMOVE_NODE_STATUS_FAILED:
			logger.debug("Failed.");
			zController.sendData(doRequestStop());
			zController.notifyEventListeners(new ZWaveInclusionEvent(ZWaveInclusionEvent.Type.ExcludeFail));
			break;
		default:
			logger.debug("Unknown request ({}).", incomingMessage.getMessagePayloadByte(1));
			break;
		}
		checkTransactionComplete(lastSentMessage, incomingMessage);

		return transactionComplete;
	}
}
