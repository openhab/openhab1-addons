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
public class AssignReturnRouteMessageClass extends ZWaveCommandProcessor {
	private static final Logger logger = LoggerFactory.getLogger(AssignReturnRouteMessageClass.class);

	public SerialMessage doRequest(int nodeId, int destinationId) {
		logger.debug("NODE {}: Assigning return route to node {}", nodeId, destinationId);

		// Queue the request
		SerialMessage newMessage = new SerialMessage(SerialMessageClass.AssignReturnRoute, SerialMessageType.Request,
				SerialMessageClass.AssignReturnRoute, SerialMessagePriority.High);
		byte[] newPayload = { (byte) nodeId, (byte) destinationId };
		newMessage.setMessagePayload(newPayload);
    	return newMessage;
	}
	
	@Override
	public boolean handleResponse(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		int nodeId = lastSentMessage.getMessagePayloadByte(0);
		
		logger.debug("NODE {}: Got AssignReturnRoute response.", nodeId);

		if(incomingMessage.getMessagePayloadByte(0) != 0x00) {
			logger.debug("NODE {}: AssignReturnRoute command in progress.", nodeId);
		} else {
			logger.error("NODE {}: AssignReturnRoute command failed.", nodeId);
			zController.notifyEventListeners(new ZWaveNetworkEvent(ZWaveNetworkEvent.Type.AssignReturnRoute, nodeId,
					ZWaveNetworkEvent.State.Failure));
		}
		
		return false;
	}

	@Override
	public boolean handleRequest(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		int nodeId = lastSentMessage.getMessagePayloadByte(0);

		logger.debug("NODE {}: Got AssignReturnRoute request.", nodeId);
		if(incomingMessage.getMessagePayloadByte(1) != 0x00) {
			logger.error("NODE {}: Assign return routes failed with error 0x{}.", nodeId, Integer.toHexString(incomingMessage.getMessagePayloadByte(0)));
			zController.notifyEventListeners(new ZWaveNetworkEvent(ZWaveNetworkEvent.Type.AssignReturnRoute, nodeId,
					ZWaveNetworkEvent.State.Failure));
		}
		else {
			zController.notifyEventListeners(new ZWaveNetworkEvent(ZWaveNetworkEvent.Type.AssignReturnRoute, nodeId,
					ZWaveNetworkEvent.State.Success));
		}
		
		return false;
	}
}
