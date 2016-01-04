/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.serialmessage;

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
		SerialMessage newMessage = new SerialMessage(SerialMessageClass.IdentifyNode, SerialMessageType.Request, SerialMessageClass.IdentifyNode, SerialMessagePriority.High);
    	byte[] newPayload = { (byte) nodeId };
    	newMessage.setMessagePayload(newPayload);
    	return newMessage;
	}

	@Override
	public boolean handleResponse(ZWaveController zController, SerialMessage lastSentMessage, SerialMessage incomingMessage) {
		logger.trace("Handle Message Get Node ProtocolInfo Response");
		
		// Check that this request is consistent with the response
		if(lastSentMessage.getMessageClass() != SerialMessageClass.IdentifyNode) {
			logger.warn("Got IdentifyNodeMessage without request, ignoring. Last message was {}.", lastSentMessage.getMessageClass());
			return false;
		}

		int nodeId = lastSentMessage.getMessagePayloadByte(0);
		logger.debug("NODE {}: ProtocolInfo", nodeId);

		ZWaveNode node = zController.getNode(nodeId);

        boolean listening = (incomingMessage.getMessagePayloadByte(0) & 0x80) != 0 ? true : false;
        boolean routing = (incomingMessage.getMessagePayloadByte(0) & 0x40) != 0 ? true : false;
        int version = (incomingMessage.getMessagePayloadByte(0) & 0x07) + 1;
        boolean frequentlyListening = (incomingMessage.getMessagePayloadByte(1) & 0x60) != 0 ? true : false;
        boolean beaming = ((incomingMessage.getMessagePayloadByte(1) & 0x10) != 0);
        boolean security = ((incomingMessage.getMessagePayloadByte(1) & 0x01) != 0);

        int maxBaudRate = 9600;
        if ((incomingMessage.getMessagePayloadByte(0) & 0x38) == 0x10) {
            maxBaudRate = 40000;
        }

        logger.debug("NODE {}: Listening = {}", nodeId, listening);
        logger.debug("NODE {}: Routing = {}", nodeId, routing);
        logger.debug("NODE {}: Beaming = {}", nodeId, beaming);
        logger.debug("NODE {}: Version = {}", nodeId, version);
        logger.debug("NODE {}: FLIRS = {}", nodeId, frequentlyListening);
        logger.debug("NODE {}: Security = {}", nodeId, security);
        logger.debug("NODE {}: Max Baud = {}", nodeId, maxBaudRate);

        node.setListening(listening);
        node.setRouting(routing);
        node.setVersion(version);
        node.setFrequentlyListening(frequentlyListening);
        node.setSecurity(security);
        node.setBeaming(beaming);
        node.setMaxBaud(maxBaudRate);

		Basic basic = Basic.getBasic(incomingMessage.getMessagePayloadByte(3));
		if (basic == null) {
			logger.error(String.format("NODE %d: Basic device class 0x%02x not found", nodeId, incomingMessage.getMessagePayloadByte(3)));
			return false;
		}
		logger.debug("NODE {}: Basic = {}", nodeId, basic.getLabel());

		Generic generic = Generic.getGeneric(incomingMessage.getMessagePayloadByte(4));
		if (generic == null) {
			logger.error(String.format("NODE %d: Generic device class 0x%02x not found", nodeId, incomingMessage.getMessagePayloadByte(4)));
			return false;
		}
		logger.debug("NODE {}: Generic = {}", nodeId, generic.getLabel());

		Specific specific = Specific.getSpecific(generic, incomingMessage.getMessagePayloadByte(5));
		if (specific == null) {
			logger.error(String.format("NODE %d: Specific device class 0x%02x not found", nodeId, incomingMessage.getMessagePayloadByte(5)));
			return false;
		}
		logger.debug("NODE {}: Specific = {}", nodeId, specific.getLabel());
		
		ZWaveDeviceClass deviceClass = node.getDeviceClass();
		deviceClass.setBasicDeviceClass(basic);
		deviceClass.setGenericDeviceClass(generic);
		deviceClass.setSpecificDeviceClass(specific);
		
		// Add all the command classes.
		// If restored the node from configuration file then 
		// the classes will already exist and this will be ignored 

		// Add mandatory command classes as specified by it's generic device class.
		for (CommandClass commandClass : generic.getMandatoryCommandClasses()) {
			ZWaveCommandClass zwaveCommandClass = ZWaveCommandClass.getInstance(commandClass.getKey(), node, zController);
			if (zwaveCommandClass != null) {
				zController.getNode(nodeId).addCommandClass(zwaveCommandClass);
			}
		}

		// Add mandatory command classes as specified by it's specific device class.
		for (CommandClass commandClass : specific.getMandatoryCommandClasses()) {
			ZWaveCommandClass zwaveCommandClass = ZWaveCommandClass.getInstance(commandClass.getKey(), node, zController);
			if (zwaveCommandClass != null) {
				node.addCommandClass(zwaveCommandClass);
			}
		}

		checkTransactionComplete(lastSentMessage, incomingMessage);

		return true;
	}
}
