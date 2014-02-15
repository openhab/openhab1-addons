/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.serialmessage;

import org.openhab.binding.zwave.internal.protocol.NodeStage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.UpdateState;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveWakeUpCommandClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class processes a serial message from the zwave controller
 * This class is the base class for the serial message class. It handles
 * the request from the application, and the processing of the responses
 * from the controller.
 * @author Chris Jackson
 * @since 1.5.0
 */
public class ApplicationUpdateMessageClass  extends ZWaveCommandProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationUpdateMessageClass.class);
	
	public  boolean handleRequest(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		logger.trace("Handle Message Application Update Request");
		int nodeId = incomingMessage.getMessagePayloadByte(1);
		
		logger.trace("NODE {}: Application Update Request from Node ", nodeId);
		UpdateState updateState = UpdateState.getUpdateState(incomingMessage.getMessagePayloadByte(0));
		
		switch (updateState) {
		case NODE_INFO_RECEIVED:
			logger.debug("NODE {}: Application update request, node information received.", nodeId);			
			int length = incomingMessage.getMessagePayloadByte(2);
			ZWaveNode node = zController.getNode(nodeId);
			
			node.resetResendCount();
			
			for (int i = 6; i < length + 3; i++) {
				int data = incomingMessage.getMessagePayloadByte(i);
				if(data == 0xef )  {
					// TODO: Implement control command classes
					break;
				}
				logger.debug(String.format("NODE %d: Adding command class 0x%02X to the list of supported command classes.", nodeId, data));
				ZWaveCommandClass commandClass = ZWaveCommandClass.getInstance(data, node, zController);
				if (commandClass != null)
					node.addCommandClass(commandClass);
			}

			// advance node stage.
			node.advanceNodeStage(NodeStage.MANSPEC01);
			
			checkTransactionComplete(lastSentMessage, incomingMessage);

			// Treat the node information frame as a wakeup
			ZWaveWakeUpCommandClass wakeUp = (ZWaveWakeUpCommandClass)node.getCommandClass(ZWaveCommandClass.CommandClass.WAKE_UP);
			if(wakeUp != null) {
				wakeUp.setAwake(true);
			}
			break;
		case NODE_INFO_REQ_FAILED:
			logger.debug("NODE {}: Application update request, Node Info Request Failed, re-request node info.", nodeId);
			
			SerialMessage requestInfoMessage = lastSentMessage;
			
			if (requestInfoMessage.getMessageClass() != SerialMessageClass.RequestNodeInfo) {
				logger.warn("NODE {}: Got application update request without node info request, ignoring.", nodeId);
				return false;
			}
				
			if (--requestInfoMessage.attempts >= 0) {
				logger.error("NODE {}: Got Node Info Request Failed while sending this serial message. Requeueing", nodeId);
				zController.enqueue(requestInfoMessage);
			} else
			{
				logger.warn("NODE {}: Node Info Request Failed 3x. Discarding message: {}", nodeId, lastSentMessage.toString());
			}
			transactionComplete = true;
			break;
		default:
			logger.warn(String.format("TODO: Implement Application Update Request Handling of %s (0x%02X).", updateState.getLabel(), updateState.getKey()));
		}		
		return false;
	}
}
