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
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Basic;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Generic;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Specific;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class processes a serial message from the zwave controller
 * @author Chris Jackson
 * @since 1.5.0
 */
public class IdentifyNodeMessageClass  extends ZWaveCommandProcessor {
	private static final Logger logger = LoggerFactory.getLogger(IdentifyNodeMessageClass.class);

	public SerialMessage doRequest(int nodeId) {
		SerialMessage newMessage = new SerialMessage(nodeId, SerialMessageClass.IdentifyNode, SerialMessageType.Request, SerialMessageClass.IdentifyNode, SerialMessagePriority.High);
    	byte[] newPayload = { (byte) nodeId };
    	newMessage.setMessagePayload(newPayload);
    	return newMessage;
	}

	@Override
	public boolean handleResponse(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		logger.trace("Handle Message Get Node ProtocolInfo Response");
		
		int nodeId = lastSentMessage.getMessagePayloadByte(0);
		logger.debug("NODE {}: ProtocolInfo", nodeId);
		
		ZWaveNode node = zController.getNode(nodeId);
		
		boolean listening = (incomingMessage.getMessagePayloadByte(0) & 0x80)!=0 ? true : false;
		boolean routing = (incomingMessage.getMessagePayloadByte(0) & 0x40)!=0 ? true : false;
		int version = (incomingMessage.getMessagePayloadByte(0) & 0x07) + 1;
		boolean frequentlyListening = (incomingMessage.getMessagePayloadByte(1) & 0x60)!= 0 ? true : false;
		
		logger.debug("NODE {}: Listening = {}", nodeId, listening);
		logger.debug("NODE {}: Routing = {}", nodeId, routing);
		logger.debug("NODE {}: Version = {}", nodeId, version);
		logger.debug("NODE {}: fLIRS = {}", nodeId, frequentlyListening);
		
		node.setListening(listening);
		node.setRouting(routing);
		node.setVersion(version);
		node.setFrequentlyListening(frequentlyListening);
		
		Basic basic = Basic.getBasic(incomingMessage.getMessagePayloadByte(3));
		if (basic == null) {
			logger.error(String.format("NODE %d: Basic device class 0x%02x not found", nodeId, incomingMessage.getMessagePayloadByte(3)));
			return false;
		}
		logger.debug(String.format("NODE %d: Basic = %s 0x%02x", nodeId, basic.getLabel(), basic.getKey()));

		Generic generic = Generic.getGeneric(incomingMessage.getMessagePayloadByte(4));
		if (generic == null) {
			logger.error(String.format("NODE %d: Generic device class 0x%02x not found", nodeId, incomingMessage.getMessagePayloadByte(4)));
			return false;
		}
		logger.debug(String.format("NODE %d: Generic = %s 0x%02x", nodeId, generic.getLabel(), generic.getKey()));

		Specific specific = Specific.getSpecific(generic, incomingMessage.getMessagePayloadByte(5));
		if (specific == null) {
			logger.error(String.format("NODE %d: Specific device class 0x%02x not found", nodeId, incomingMessage.getMessagePayloadByte(5)));
			return false;
		}
		logger.debug(String.format("NODE %d: Specific = %s 0x%02x", nodeId, specific.getLabel(), specific.getKey()));
		
		ZWaveDeviceClass deviceClass = node.getDeviceClass();
		deviceClass.setBasicDeviceClass(basic);
		deviceClass.setGenericDeviceClass(generic);
		deviceClass.setSpecificDeviceClass(specific);
		
		// if restored the node from configuration information
		// then we don't have to add these command classes anymore.
		if (!node.restoreFromConfig()) {
			// Add mandatory command classes as specified by it's generic device class.
			for (CommandClass commandClass : generic.getMandatoryCommandClasses()) {
				ZWaveCommandClass zwaveCommandClass = ZWaveCommandClass.getInstance(commandClass.getKey(), node, zController);
				if (zwaveCommandClass != null)
					zController.getNode(nodeId).addCommandClass(zwaveCommandClass);
			}
	
			// Add mandatory command classes as specified by it's specific device class.
			for (CommandClass commandClass : specific.getMandatoryCommandClasses()) {
				ZWaveCommandClass zwaveCommandClass = ZWaveCommandClass.getInstance(commandClass.getKey(), node, zController);
				if (zwaveCommandClass != null)
					node.addCommandClass(zwaveCommandClass);
			}
		}
		
    	// advance node stage of the current node.
		node.advanceNodeStage(NodeStage.PING);
		
		checkTransactionComplete(lastSentMessage, incomingMessage);

		return false;
	}
}
