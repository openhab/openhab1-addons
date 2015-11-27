/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.serialmessage;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveWakeUpCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveInclusionEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveNodeInfoEvent;
import org.openhab.binding.zwave.internal.protocol.initialization.ZWaveNodeInitStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class processes a Node Information Frame (NIF) message from the zwave controller
 * @author Chris Jackson
 * @since 1.5.0
 */
public class ApplicationUpdateMessageClass  extends ZWaveCommandProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationUpdateMessageClass.class);

	@Override
	public  boolean handleRequest(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		int nodeId;
		boolean result = true;
		UpdateState updateState = UpdateState.getUpdateState(incomingMessage.getMessagePayloadByte(0));

		switch (updateState) {
		case NODE_INFO_RECEIVED:
			// We've received a NIF, and this contains the node ID.
			nodeId = incomingMessage.getMessagePayloadByte(1);
			logger.debug("NODE {}: Application update request. Node information received.", nodeId);
			
			int length = incomingMessage.getMessagePayloadByte(2);
			ZWaveNode node = zController.getNode(nodeId);
			if(node == null) {
				logger.debug("NODE {}: Application update request. Node not known!", nodeId);
				
				// We've received a NIF from a node we don't know.
				// This could happen if we add a new node using a different controller than OH.
				// We handle this the same way as if included through an AddNode packet.
				// This allows everyone to be notified.
				if(nodeId > 0 && nodeId <= 232) {
					zController.notifyEventListeners(new ZWaveInclusionEvent(ZWaveInclusionEvent.Type.IncludeDone, incomingMessage.getMessagePayloadByte(2)));
				}
				break;
			}

			node.resetResendCount();

			// Remember that we've received this so we can continue initialisation
			node.setApplicationUpdateReceived(true);

			// If we're finished initialisation, then we can treat this like a HAIL
			if(node.getNodeInitializationStage() == ZWaveNodeInitStage.DONE) {
				// If this node supports associations, then assume this should be handled through that mechanism
				if(node.getCommandClass(CommandClass.ASSOCIATION) == null) {
					// If we receive an Application Update Request and the node is already
					// fully initialised we assume this is a request to the controller to 
					// re-get the current node values
					logger.debug("NODE {}: Application update request. Requesting node state.", nodeId);

					zController.pollNode(node);
				}
			}
			else {
				for (int i = 6; i < length + 3; i++) {
					int data = incomingMessage.getMessagePayloadByte(i);
					if(data == 0xef) {
						// TODO: Implement control command classes
						break;
					}
					logger.trace(String.format("NODE %d: Command class 0x%02X is supported.", nodeId, data));
					ZWaveCommandClass commandClass = ZWaveCommandClass.getInstance(data, node, zController);
					if (commandClass != null) {
						node.addCommandClass(commandClass);
					}
				}
			}

			// Notify we received an info frame
			zController.notifyEventListeners(new ZWaveNodeInfoEvent(nodeId));

			// Treat the node information frame as a wakeup
			ZWaveWakeUpCommandClass wakeUp = (ZWaveWakeUpCommandClass)node.getCommandClass(ZWaveCommandClass.CommandClass.WAKE_UP);
			if(wakeUp != null) {
				wakeUp.setAwake(true);
			}
			break;
		case NODE_INFO_REQ_FAILED:
			// Make sure we can correlate the request before we use the nodeId
			if (lastSentMessage.getMessageClass() != SerialMessageClass.RequestNodeInfo) {
				logger.warn("Got ApplicationUpdateMessage without request, ignoring. Last message was {}.", lastSentMessage.getMessageClass());
				return false;
			}

			// The failed message doesn't contain the node number, so use the info from the request.
			nodeId = lastSentMessage.getMessageNode();
			logger.debug("NODE {}: Application update request. Node Info Request Failed.", nodeId);

			// Handle retries
			if (--lastSentMessage.attempts >= 0) {
				logger.error("NODE {}: Got Node Info Request Failed. Requeueing", nodeId);
				zController.enqueue(lastSentMessage);
			}
			else {
				logger.warn("NODE {}: Node Info Request Failed 3x. Discarding message: {}", nodeId, lastSentMessage.toString());
			}

			// Transaction is not successful
			incomingMessage.setTransactionCanceled();
			result = false;
			break;
		default:
			logger.warn("TODO: Implement Application Update Request Handling of {} ({}).", updateState.getLabel(), updateState.getKey());
		}
		
		// Check if this completes the transaction
		checkTransactionComplete(lastSentMessage, incomingMessage);

		return result;
	}
	
	/**
	 * Update state enumeration. Indicates the type of application update state that was sent.
	 * @author Jan-Willem Spuij
	 * @ since 1.3.0
	 */
	public enum UpdateState {
		NODE_INFO_RECEIVED(0x84, "Node info received"),
		NODE_INFO_REQ_DONE(0x82, "Node info request done"),
		NODE_INFO_REQ_FAILED(0x81, "Node info request failed"),
		ROUTING_PENDING(0x80, "Routing pending"),
		NEW_ID_ASSIGNED(0x40, "New ID Assigned"),
		DELETE_DONE(0x20, "Delete done"),
		SUC_ID(0x10, "SUC ID");
		
		/**
		 * A mapping between the integer code and its corresponding update state
		 * class to facilitate lookup by code.
		 */
		private static Map<Integer, UpdateState> codeToUpdateStateMapping;

		private int key;
		private String label;

		private UpdateState(int key, String label) {
			this.key = key;
			this.label = label;
		}

		private static void initMapping() {
			codeToUpdateStateMapping = new HashMap<Integer, UpdateState>();
			for (UpdateState s : values()) {
				codeToUpdateStateMapping.put(s.key, s);
			}
		}

		/**
		 * Lookup function based on the update state code.
		 * Returns null when there is no update state with code i.
		 * @param i the code to lookup
		 * @return enumeration value of the update state.
		 */
		public static UpdateState getUpdateState(int i) {
			if (codeToUpdateStateMapping == null) {
				initMapping();
			}
			
			return codeToUpdateStateMapping.get(i);
		}

		/**
		 * @return the key
		 */
		public int getKey() {
			return key;
		}

		/**
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}
	}
}
