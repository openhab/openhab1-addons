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
import org.openhab.binding.zwave.internal.protocol.event.ZWaveNetworkEvent.State;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveNetworkEvent.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class processes a serial message from the zwave controller
 * @author Chris Jackson
 * @since 1.5.0
 */
public class RemoveFailedNodeMessageClass extends ZWaveCommandProcessor {
	private static final Logger logger = LoggerFactory.getLogger(RemoveFailedNodeMessageClass.class);
	
	private final int FAILED_NODE_REMOVE_STARTED             = 0x00;
	private final int FAILED_NODE_NOT_PRIMARY_CONTROLLER     = 0x02;
	private final int FAILED_NODE_NO_CALLBACK_FUNCTION		 = 0x04;
	private final int FAILED_NODE_NOT_FOUND					 = 0x08;
	private final int FAILED_NODE_REMOVE_PROCESS_BUSY		 = 0x10;
	private final int FAILED_NODE_REMOVE_FAIL				 = 0x20;
	
	private final int FAILED_NODE_OK						 = 0x00;
	private final int FAILED_NODE_REMOVED					 = 0x01;
	private final int FAILED_NODE_NOT_REMOVED				 = 0x02;

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
		int nodeId = lastSentMessage.getMessagePayloadByte(0);
		
		switch(incomingMessage.getMessagePayloadByte(0)) {
			case FAILED_NODE_REMOVE_STARTED:
				logger.debug("NODE {}: Remove failed node successfully placed on stack.", nodeId);
				transactionComplete = true;
				break;
			case FAILED_NODE_NOT_PRIMARY_CONTROLLER:
				logger.error("NODE {}: Remove failed node failed as not Primary Controller for node!", nodeId);
				transactionComplete = true;
				break;
			case FAILED_NODE_NO_CALLBACK_FUNCTION:
				logger.error("NODE {}: Remove failed node failed as no callback function!", nodeId);
				transactionComplete = true;
				break;
			case FAILED_NODE_NOT_FOUND:
				logger.error("NODE {}: Remove failed node failed as node not found on controllers failed list!", nodeId);
				transactionComplete = true;
				break;
			case FAILED_NODE_REMOVE_PROCESS_BUSY:
				logger.error("NODE {}: Remove failed node failed as Controller Busy!", nodeId);
				transactionComplete = true;
				break;
			case FAILED_NODE_REMOVE_FAIL:
				logger.error("NODE {}: Remove failed node failed!", nodeId);
				transactionComplete = true;
				break;
			default:
				logger.error("NODE {}: Remove failed node not placed on stack due to error 0x{}.", nodeId, Integer.toHexString(incomingMessage.getMessagePayloadByte(0)));
				transactionComplete = true;
				break;
		}
		
		return true;
	}

	@Override
	public boolean handleRequest(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		int nodeId = lastSentMessage.getMessagePayloadByte(0);

		logger.debug("NODE {}: Got RemoveFailedNode request.", nodeId);
		switch(incomingMessage.getMessagePayloadByte(0)) {
			case FAILED_NODE_OK:
				logger.error("NODE {}: Unable to remove failed node as it is not a failed node!", nodeId);
				transactionComplete = true;
				break;
			case FAILED_NODE_REMOVED:
				logger.debug("NODE {}: Successfully removed node from controller database!", nodeId);
				zController.notifyEventListeners(new ZWaveNetworkEvent(Type.DeleteNode, nodeId, State.Success));
				transactionComplete = true;
				break;
			case FAILED_NODE_NOT_REMOVED:
				logger.error("NODE {}: Unable to remove failed node!", nodeId);
				transactionComplete = true;
				break;
			default:
				logger.error("NODE {}: Remove failed node failed with error 0x{}.", nodeId, Integer.toHexString(incomingMessage.getMessagePayloadByte(0)));
				transactionComplete = true;
				break;
		}
		
		return true;
	}
}
