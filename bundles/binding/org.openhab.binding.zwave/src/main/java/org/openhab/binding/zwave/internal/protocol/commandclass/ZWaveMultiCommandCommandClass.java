/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Basic;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Generic;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Specific;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.NodeStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Handles the Multi Command command class.
 * @author Chris Jackson
 * @since 1.6.0
 */
@XStreamAlias("multiCommandCommandClass")
public class ZWaveMultiCommandCommandClass extends ZWaveCommandClass {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveMultiCommandCommandClass.class);
	private static final int MAX_SUPPORTED_VERSION = 1;

	/**
	 * Creates a new instance of the ZWaveMultiCommandCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveMultiCommandCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.MULTI_CMD;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpointId) {
		logger.trace("Handle Message Multi-instance/Multi-channel Request");
		logger.debug("NODE {}: Received Multi-instance/Multi-channel Request", this.getNode().getNodeId());
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
		
		}
	}
	
	private void handleMultiCommandEncapResponse(
			SerialMessage serialMessage, int offset) {
		logger.trace("Process Multi-command Encapsulation");
		CommandClass commandClass;
		ZWaveCommandClass zwaveCommandClass;
		int endpointId = serialMessage.getMessagePayloadByte(offset);
		int commandClassCode = serialMessage.getMessagePayloadByte(offset + 2);
		commandClass = CommandClass.getCommandClass(commandClassCode);
		
		if (commandClass == null) {
			logger.error(String.format("NODE %d: Unsupported command class 0x%02x", this.getNode().getNodeId(), commandClassCode));
			return;
		}
		
		logger.debug(String.format("NODE %d: Requested Command Class = %s (0x%02x)", this.getNode().getNodeId(), commandClass.getLabel() , commandClassCode));
		ZWaveEndpoint endpoint = this.endpoints.get(endpointId);
		
		if (endpoint == null){
			logger.error("NODE {}: Endpoint {} not found. Cannot set command classes.", this.getNode().getNodeId(), endpointId);
			return;
		}
		
		zwaveCommandClass = endpoint.getCommandClass(commandClass);
		
		if (zwaveCommandClass == null) {
			logger.warn(String.format("NODE %d: CommandClass %s (0x%02x) not implemented by endpoint %d, fallback to main node.", commandClass.getLabel(), commandClassCode, endpointId));
			zwaveCommandClass = this.getNode().getCommandClass(commandClass);
		}
		
		if (zwaveCommandClass == null) {
			logger.error(String.format("NODE %d: CommandClass %s (0x%02x) not implemented.", this.getNode().getNodeId(), commandClass.getLabel(), commandClassCode));
			return;
		}
		
		logger.debug(String.format("NODE %d: Endpoint = %d, calling handleApplicationCommandRequest.", this.getNode().getNodeId(), endpointId));
		zwaveCommandClass.handleApplicationCommandRequest(serialMessage, offset + 3, endpointId);
	}

}
