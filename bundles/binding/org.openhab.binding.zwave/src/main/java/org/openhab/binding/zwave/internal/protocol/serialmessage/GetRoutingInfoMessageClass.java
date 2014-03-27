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
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveNetworkEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class processes a serial message from the zwave controller
 * @author Chris Jackson
 * @since 1.5.0
 */
public class GetRoutingInfoMessageClass extends ZWaveCommandProcessor {
	private static final Logger logger = LoggerFactory.getLogger(GetRoutingInfoMessageClass.class);
	
	private static final int NODE_BYTES = 29; // 29 bytes = 232 bits, one for each supported node by Z-Wave;

	public SerialMessage doRequest(int nodeId) {
		logger.debug("NODE {}: Request routing info", nodeId);

		// Queue the request
		SerialMessage newMessage = new SerialMessage(SerialMessage.SerialMessageClass.GetRoutingInfo, SerialMessage.SerialMessageType.Request,
				SerialMessage.SerialMessageClass.GetRoutingInfo, SerialMessage.SerialMessagePriority.High);
		byte[] newPayload = { (byte) nodeId,
				(byte) 0,		// Don't remove bad nodes
				(byte) 0,		// Don't remove non-repeaters
				(byte) 3		// Function ID
		};
    	newMessage.setMessagePayload(newPayload);
    	return newMessage;
	}
	
	@Override
	public boolean handleResponse(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		int nodeId = lastSentMessage.getMessagePayloadByte(0);
		
		logger.debug("NODE {}: Got NodeRoutingInfo request.", nodeId);

		// Get the node
		ZWaveNode node = zController.getNode(nodeId);
		if(node == null) {
			logger.error("NODE {}: Routing information for unknown node", nodeId);
			transactionComplete = true;
			return false;
		}

		node.clearNeighbors();
		boolean hasNeighbors = false;
		for (int by = 0; by < NODE_BYTES; by++) {
			for (int bi = 0; bi < 8; bi++) {
				if ((incomingMessage.getMessagePayloadByte(by) & (0x01 << bi)) != 0) {
					hasNeighbors = true;

					// Add the node to the neighbor list
					node.addNeighbor((by << 3) + bi + 1);
				}
			}
		}

		if (!hasNeighbors) {
			logger.debug("NODE {}: No neighbors reported", nodeId);
		}
		else {
			String neighbors = "Neighbor nodes:";
			for (Integer neighborNode : node.getNeighbors()) {
				neighbors += " " + neighborNode;
			}
			logger.debug("Node {}: {}", nodeId, neighbors);
		}

		zController.notifyEventListeners(new ZWaveNetworkEvent(ZWaveNetworkEvent.Type.NodeRoutingInfo, nodeId,
				ZWaveNetworkEvent.State.Success));

		transactionComplete = true;
		return false;
	}
}
