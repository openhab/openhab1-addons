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
import org.openhab.binding.zwave.internal.protocol.event.ZWaveNetworkEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class processes a serial message from the zwave controller
 * @author Chris Jackson
 * @since 1.5.0
 */
public class RequestNodeNeighborUpdateMessageClass extends ZWaveCommandProcessor {
	private static final Logger logger = LoggerFactory.getLogger(RequestNodeNeighborUpdateMessageClass.class);
	
	final int REQUEST_NEIGHBOR_UPDATE_STARTED = 0x21;
	final int REQUEST_NEIGHBOR_UPDATE_DONE    = 0x22;
	final int REQUEST_NEIGHBOR_UPDATE_FAILED  = 0x23;

	public SerialMessage doRequest(int nodeId) {
		logger.debug("NODE {}: Request neighbor update", nodeId);

		// Queue the request
		SerialMessage newMessage = new SerialMessage(SerialMessageClass.RequestNodeNeighborUpdate, SerialMessageType.Request,
				SerialMessageClass.RequestNodeNeighborUpdate, SerialMessagePriority.High);
		byte[] newPayload = { (byte) nodeId };
    	newMessage.setMessagePayload(newPayload);
    	return newMessage;
	}

	@Override
	public boolean handleRequest(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		int nodeId = lastSentMessage.getMessagePayloadByte(0);

		logger.debug("NODE {}: Got NodeNeighborUpdate request.", nodeId);
		switch(incomingMessage.getMessagePayloadByte(1)) {
		case REQUEST_NEIGHBOR_UPDATE_STARTED:
			logger.debug("NODE {}: NodeNeighborUpdate STARTED", nodeId);
			// We're done
			transactionComplete = true;
			break;
		case REQUEST_NEIGHBOR_UPDATE_DONE:
			logger.debug("NODE {}: NodeNeighborUpdate DONE", nodeId);
			
			zController.notifyEventListeners(new ZWaveNetworkEvent(ZWaveNetworkEvent.Type.NodeNeighborUpdate, nodeId,
					ZWaveNetworkEvent.State.Success));
			transactionComplete = true;
			break;
		case REQUEST_NEIGHBOR_UPDATE_FAILED:
			logger.error("NODE {}: NodeNeighborUpdate FAILED", nodeId);
			
			zController.notifyEventListeners(new ZWaveNetworkEvent(ZWaveNetworkEvent.Type.NodeNeighborUpdate, nodeId,
					ZWaveNetworkEvent.State.Failure));
			transactionComplete = true;
			break;
		}
		return false;
	}
}
