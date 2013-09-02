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
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the manufacturer specific command class. Class to request and report
 * manufacturer specific information.
 * 
 * @author Jan-Willem Spuij
 * @since 1.3.0
 */
public class ZWaveManufacturerSpecificCommandClass extends ZWaveCommandClass {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveManufacturerSpecificCommandClass.class);
	
	private static final int MANUFACTURER_SPECIFIC_GET = 0x04;
	private static final int MANUFACTURER_SPECIFIC_REPORT = 0x05;
	
	/**
	 * Creates a new instance of the ZwaveManufacturerSpecificCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveManufacturerSpecificCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.MANUFACTURER_SPECIFIC;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpoint) {

		logger.trace("Handle Message Manufacture Specific Request");
		logger.debug(String.format("Received Manufacture Specific Information for Node ID = %d", this.getNode().getNodeId()));
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
			case MANUFACTURER_SPECIFIC_GET:
				logger.warn(String.format("Command 0x%02X not implemented.", command));
				return;
			case MANUFACTURER_SPECIFIC_REPORT:
				logger.trace("Process Manufacturer Specific Report");
				
				int tempMan = ((serialMessage.getMessagePayloadByte(offset + 1)) << 8) | (serialMessage.getMessagePayloadByte(offset + 2));
				int tempDeviceType = ((serialMessage.getMessagePayloadByte(offset + 3)) << 8) | (serialMessage.getMessagePayloadByte(offset + 4));
				int tempDeviceId = ((serialMessage.getMessagePayloadByte(offset + 5)) << 8) | (serialMessage.getMessagePayloadByte(offset + 6));
				
				this.getNode().setManufacturer(tempMan);
				this.getNode().setDeviceType(tempDeviceType);
				this.getNode().setDeviceId(tempDeviceId);
				
				logger.debug(String.format("Node %d Manufacturer ID = 0x%04x", this.getNode().getNodeId(), this.getNode().getManufacturer()));
				logger.debug(String.format("Node %d Device Type = 0x%04x", this.getNode().getNodeId(), this.getNode().getDeviceType()));
				logger.debug(String.format("Node %d Device ID = 0x%04x", this.getNode().getNodeId(), this.getNode().getDeviceId()));

				this.getNode().advanceNodeStage();
				break;
			default:
			logger.warn(String.format("Unsupported Command 0x%02X for command class %s (0x%02X).", 
					command, 
					this.getCommandClass().getLabel(),
					this.getCommandClass().getKey()));
		}
	}

	/**
	 * Gets a SerialMessage with the ManufacturerSpecific GET command 
	 * @return the serial message
	 */
	public SerialMessage getManufacturerSpecificMessage() {
		logger.debug("Creating new message for application command MANUFACTURER_SPECIFIC_GET for node {}", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							2, 
								(byte) getCommandClass().getKey(), 
								(byte) MANUFACTURER_SPECIFIC_GET };
    	result.setMessagePayload(newPayload);
    	return result;		
	}

}
