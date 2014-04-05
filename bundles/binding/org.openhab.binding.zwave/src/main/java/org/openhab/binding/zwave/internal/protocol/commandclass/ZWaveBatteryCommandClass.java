/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

import java.util.ArrayList;
import java.util.Collection;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.NodeStage;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Handles the Battery command class. Devices that support this
 * command class can report their battery level and give low battery
 * warnings.
 * The commands include the possibility to get a given
 * battery level and report a battery level.
 * @author Jan-Willem Spuij
 * @since 1.3.0
 */
@XStreamAlias("batteryCommandClass")
public class ZWaveBatteryCommandClass extends ZWaveCommandClass implements ZWaveGetCommands, ZWaveCommandClassDynamicState {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveBatteryCommandClass.class);
	
	private static final int BATTERY_GET = 0x02;
	private static final int BATTERY_REPORT = 0x03;
	
	/**
	 * Creates a new instance of the ZWaveBatteryCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveBatteryCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.BATTERY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpoint) {
		logger.trace("Handle Message Battery Request");
		logger.debug("Node {}: Received Battery Request", this.getNode().getNodeId());
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
			case BATTERY_GET:
				logger.warn(String.format("Command 0x%02X not implemented.", command));
				return;
			case BATTERY_REPORT:
				logger.trace("Process Battery Report");
				
				int value = serialMessage.getMessagePayloadByte(offset + 1); 
				logger.debug(String.format("Node %d: Battery report value = 0x%02X", this.getNode().getNodeId(), value));
				ZWaveCommandClassValueEvent zEvent = new ZWaveCommandClassValueEvent(this.getNode().getNodeId(), endpoint, this.getCommandClass(), value);
				this.getController().notifyEventListeners(zEvent);

				if (this.getNode().getNodeStage() != NodeStage.DONE)
					this.getNode().advanceNodeStage(NodeStage.DONE);
				break;
			default:
			logger.warn(String.format("Unsupported Command 0x%02X for command class %s (0x%02X).", 
					command, 
					this.getCommandClass().getLabel(),
					this.getCommandClass().getKey()));
		}
	}

	/**
	 * Gets a SerialMessage with the BATTERY_GET command 
	 * @return the serial message
	 */
	public SerialMessage getValueMessage() {
		logger.debug("Node {}: Creating new message for application command BATTERY_GET", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							2, 
								(byte) getCommandClass().getKey(), 
								(byte) BATTERY_GET };
    	result.setMessagePayload(newPayload);
    	return result;		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<SerialMessage> getDynamicValues() {
		ArrayList<SerialMessage> result = new ArrayList<SerialMessage>();
		
		result.add(getValueMessage());
		
		return result;
	}
}
