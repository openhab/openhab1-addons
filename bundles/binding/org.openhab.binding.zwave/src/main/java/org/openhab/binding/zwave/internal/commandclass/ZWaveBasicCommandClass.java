/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
 * Handles the Basic command class. Almost all devices support the Basic commands. This class 
 * contains a few basic commands that can be used to control the basic functionality of a device. 
 * The commands include the possibility to set a given level, get a given level and report a level.
 * 
 * @author Jan-Willem Spuij
 * @since 1.3.0
 */
public class ZWaveBasicCommandClass extends ZWaveCommandClass implements ZWaveBasicCommands {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveBasicCommandClass.class);
	
	private static final int BASIC_SET = 0x01;
	private static final int BASIC_GET = 0x02;
	private static final int BASIC_REPORT = 0x03;
	
	/**
	 * Creates a new instance of the ZWaveBasicCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveBasicCommandClass(ZWaveNode node, ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.BASIC;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage, int offset, int endpoint) {
		logger.trace("Handle Message Basic Request");
		logger.debug(String.format("Received Basic Request for Node ID = %d", this.getNode().getNodeId()));
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
			case BASIC_SET:
				logger.trace("Process Basic Set");
				logger.debug("Basic Set sent to the controller will be processed as Basic Report");
				// Now, some devices report their value as a basic set. For instance the Fibaro FGK - 101 Door / Window sensor.
				// Process this as if it was a value report.
				processBasicReport(serialMessage, offset, endpoint);
				break;
			case BASIC_GET:
				logger.warn(String.format("Command 0x%02X not implemented.", command));
				return;
			case BASIC_REPORT:
				logger.trace("Process Basic Report");
				processBasicReport(serialMessage, offset, endpoint);
				break;
			default:
				logger.warn(String.format("Unsupported Command 0x%02X for command class %s (0x%02X).", 
					command, 
					this.getCommandClass().getLabel(),
					this.getCommandClass().getKey()));
		}
	}

	/**
	 * Processes a BASIC_REPORT / BASIC_SET message.
	 * 
	 * @param serialMessage the incoming message to process.
	 * @param offset the offset position from which to start message processing.
	 * @param endpoint the endpoint or instance number this message is meant for.
	 */
	protected void processBasicReport(SerialMessage serialMessage, int offset, int endpoint) {
		int value = serialMessage.getMessagePayloadByte(offset + 1); 
		logger.debug(String.format("Basic report from nodeId = %d, value = 0x%02X", this.getNode().getNodeId(), value));
		Object eventValue;
		if (value == 0) {
			eventValue = "OFF";
		} else if (value < 99) {
			eventValue = value;
		} else {
			eventValue = "ON";
		}
		ZWaveEvent zEvent = new ZWaveEvent(ZWaveEventType.BASIC_EVENT, this.getNode().getNodeId(), endpoint, eventValue);
		this.getController().notifyEventListeners(zEvent);
	}

	/**
	 * Gets a SerialMessage with the BASIC GET command 
	 * @return the serial message
	 */
	public SerialMessage getValueMessage() {
		logger.debug("Creating new message for application command BASIC_GET for node {}", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							2, 
								(byte) getCommandClass().getKey(), 
								(byte) BASIC_GET };
    	result.setMessagePayload(newPayload);
    	return result;		
	}
	
	/**
	 * Gets a SerialMessage with the BASIC SET command 
	 * @param the level to set.
	 * @return the serial message
	 */
	public SerialMessage setValueMessage(int level) {
		logger.debug("Creating new message for application command BASIC_SET for node {}", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Set);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							3, 
								(byte) getCommandClass().getKey(), 
								(byte) BASIC_SET,
								(byte) level
								};
    	result.setMessagePayload(newPayload);
    	return result;		
	}

}
