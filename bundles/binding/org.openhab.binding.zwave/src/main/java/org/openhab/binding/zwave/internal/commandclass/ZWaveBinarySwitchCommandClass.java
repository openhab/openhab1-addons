/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.commandclass;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveEvent;
import org.openhab.binding.zwave.internal.protocol.ZWaveEvent.ZWaveEventType;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the Binary Switch command class. Binary switches can be turned on or off and report 
 * their status as on (0xFF) or off (0x00).
 * 
 * The commands include the possibility to set a given level, get a given level and report a level.
 * 
 * @author Jan-Willem Spuij
 * @since 1.3.0
 */
public class ZWaveBinarySwitchCommandClass extends ZWaveCommandClass implements ZWaveBasicCommands {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveBinarySwitchCommandClass.class);
	
	private static final int SWITCH_BINARY_SET = 0x01;
	private static final int SWITCH_BINARY_GET = 0x02;
	private static final int SWITCH_BINARY_REPORT = 0x03;
	
	/**
	 * Creates a new instance of the ZWaveBinarySwitchCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveBinarySwitchCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.SWITCH_BINARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpoint) {
		logger.trace("Handle Message Switch Binary Request");
		logger.debug(String.format("Received Switch Binary Request for Node ID = %d", this.getNode().getNodeId()));
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
			case SWITCH_BINARY_SET:
			case SWITCH_BINARY_GET:
				logger.warn(String.format("Command 0x%02X not implemented.", command));
				return;
			case SWITCH_BINARY_REPORT:
				logger.trace("Process Switch Binary Report");
				
				int value = serialMessage.getMessagePayloadByte(offset + 1); 
				logger.debug(String.format("Switch Binary report from nodeId = %d, value = 0x%02X", this.getNode().getNodeId(), value));
				Object eventValue;
				if (value == 0) {
					eventValue = "OFF";
				} else {
					eventValue = "ON";
				}
				ZWaveEvent zEvent = new ZWaveEvent(ZWaveEventType.SWITCH_EVENT, this.getNode().getNodeId(), endpoint, eventValue);
				this.getController().notifyEventListeners(zEvent);
				break;
			default:
			logger.warn(String.format("Unsupported Command 0x%02X for command class %s (0x%02X).", 
					command, 
					this.getCommandClass().getLabel(),
					this.getCommandClass().getKey()));
		}
	}

	/**
	 * Gets a SerialMessage with the SWITCH_BINARY_GET command 
	 * @return the serial message
	 */
	public SerialMessage getValueMessage() {
		logger.debug("Creating new message for application command SWITCH_BINARY_GET for node {}", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							2, 
								(byte) getCommandClass().getKey(), 
								(byte) SWITCH_BINARY_GET };
    	result.setMessagePayload(newPayload);
    	return result;		
	}
	
	/**
	 * Gets a SerialMessage with the SWITCH_BINARY_SET command 
	 * @param the level to set. 0 is mapped to off, > 0 is mapped to on.
	 * @return the serial message
	 */
	public SerialMessage setValueMessage(int level) {
		logger.debug("Creating new message for application command SWITCH_BINARY_SET for node {}", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Set);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							3, 
								(byte) getCommandClass().getKey(), 
								(byte) SWITCH_BINARY_SET,
								(byte) (level > 0 ? 0xFF : 0x00)
								};
    	result.setMessagePayload(newPayload);
    	return result;		
	}
	
}
