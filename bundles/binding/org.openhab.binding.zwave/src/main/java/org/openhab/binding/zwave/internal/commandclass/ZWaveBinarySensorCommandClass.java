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
 * Handles the Binary Sensor command class. Binary sensors indicate there status or event as on
 * (0xFF) or off (0x00).
 * 
 * The commands include the possibility to get a given value and report a value.
 * 
 * @author Jan-Willem Spuij
 * @since 1.3.0
 */
public class ZWaveBinarySensorCommandClass extends ZWaveCommandClass implements ZWaveGetCommands {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveBinarySensorCommandClass.class);
	
	private static final int SENSOR_BINARY_GET = 0x02;
	private static final int SENSOR_BINARY_REPORT = 0x03;
	
	/**
	 * Creates a new instance of the ZWaveBinarySensorCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveBinarySensorCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.SENSOR_BINARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpoint) {
		logger.trace("Handle Message Sensor Binary Request");
		logger.debug(String.format("Received Sensor Binary Request for Node ID = %d", this.getNode().getNodeId()));
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
			case SENSOR_BINARY_GET:
				logger.warn(String.format("Command 0x%02X not implemented.", command));
				return;
			case SENSOR_BINARY_REPORT:
				logger.trace("Process Sensor Binary Report");
				
				int value = serialMessage.getMessagePayloadByte(offset + 1); 
				logger.debug(String.format("Sensor Binary report from nodeId = %d, value = 0x%02X", this.getNode().getNodeId(), value));
				Object eventValue;
				if (value == 0) {
					eventValue = "CLOSED";
				} else {
					eventValue = "OPEN";
				}
				ZWaveEvent zEvent = new ZWaveEvent(ZWaveEventType.SENSOR_EVENT, this.getNode().getNodeId(), endpoint, eventValue);
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
	 * Gets a SerialMessage with the SENSOR_BINARY_GET command 
	 * @return the serial message
	 */
	public SerialMessage getValueMessage() {
		logger.debug("Creating new message for application command SENSOR_BINARY_GET for node {}", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							2, 
								(byte) getCommandClass().getKey(), 
								(byte) SENSOR_BINARY_GET };
    	result.setMessagePayload(newPayload);
    	return result;		
	}
	
}
