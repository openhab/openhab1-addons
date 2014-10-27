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
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class processes a serial message from the zwave controller
 * @author Chris Jackson
 * @since 1.5.0
 */
public class ApplicationCommandMessageClass  extends ZWaveCommandProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationCommandMessageClass.class);

	@Override
	public  boolean handleRequest(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		logger.trace("Handle Message Application Command Request");
		int nodeId = incomingMessage.getMessagePayloadByte(1);
		ZWaveNode node = zController.getNode(nodeId);
		
		if (node == null) {
			logger.warn("NODE {}: Not initialized yet, ignoring message.", nodeId);
			return false;
		}
		logger.debug("NODE {}: Application Command Request (Stage {})", nodeId, node.getNodeStage().getLabel());
		
		// If the node is DEAD, but we've just received a message from it, then it's not dead!
		if(node.isDead()) {
			node.setAlive();
			logger.debug("NODE {}: Node has risen from the DEAD. Set stage to {}.", nodeId, node.getNodeStage());			
		}
		// If the node is FAILED, but we've just received a message from it, then it's not dead!
		if(node.isFailed()) {
			node.setAlive();
			logger.debug("NODE {}: Node has risen from the FAILED. Set stage to {}.", nodeId, node.getNodeStage());			
		}
		
		node.resetResendCount();
		
		int commandClassCode = incomingMessage.getMessagePayloadByte(3);
		CommandClass commandClass = CommandClass.getCommandClass(commandClassCode);

		if (commandClass == null) {
			logger.error(String.format("NODE %d: Unsupported command class 0x%02x", nodeId, commandClassCode));
			return false;
		}

		logger.debug(String.format("NODE %d: Incoming command class %s (0x%02x)", nodeId, commandClass.getLabel(), commandClass.getKey()));
		ZWaveCommandClass zwaveCommandClass =  node.getCommandClass(commandClass);
		
		// Apparently, this node supports a command class that we did not get (yet) during initialization.
		// Let's add it now then to support handling this message.
		if (zwaveCommandClass == null) {
			logger.debug(String.format("NODE %d: Command class %s (0x%02x) not found, trying to add it.", 
					nodeId, commandClass.getLabel(), commandClass.getKey()));
			
			zwaveCommandClass = ZWaveCommandClass.getInstance(commandClass.getKey(), node, zController);
			
			if (zwaveCommandClass != null) {
				logger.debug(String.format("NODE %d: Adding command class %s (0x%02x)", nodeId, commandClass.getLabel(), commandClass.getKey()));
				node.addCommandClass(zwaveCommandClass);
			}
		}
		
		// We got an unsupported command class, return.
		if (zwaveCommandClass == null) {
			logger.error(String.format("NODE %d: Unsupported command class %s (0x%02x)", nodeId, commandClass.getLabel(), commandClassCode));
			return false;
		}
		
		logger.trace("NODE {}: Found Command Class {}, passing to handleApplicationCommandRequest", nodeId, zwaveCommandClass.getCommandClass().getLabel());
		zwaveCommandClass.handleApplicationCommandRequest(incomingMessage, 4, 1);

		checkTransactionComplete(lastSentMessage, incomingMessage);
		
		return false;
	}
}
