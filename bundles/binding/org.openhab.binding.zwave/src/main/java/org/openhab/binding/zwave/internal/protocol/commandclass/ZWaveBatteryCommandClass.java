/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

import java.util.ArrayList;
import java.util.Collection;

import org.openhab.binding.zwave.internal.config.ZWaveDbCommandClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

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

	@XStreamOmitField
	private static final Logger logger = LoggerFactory.getLogger(ZWaveBatteryCommandClass.class);
	
	private static final int BATTERY_GET = 0x02;
	private static final int BATTERY_REPORT = 0x03;

	private Integer batteryLevel = null;
	private Boolean batteryLow = null;
	
	@XStreamOmitField
	private boolean dynamicDone = false;
	
	private boolean isGetSupported = true;

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
		logger.debug("NODE {}: Received Battery Request", this.getNode().getNodeId());
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
			case BATTERY_GET:
				logger.warn("Command {} not implemented.", command);
				return;
			case BATTERY_REPORT:
				logger.trace("Process Battery Report");
				
				if(serialMessage.getMessagePayload().length < offset + 1) {
					logger.error("NODE {}: Battery report length too short");
					return;
				}
				
				batteryLevel = serialMessage.getMessagePayloadByte(offset + 1);
				logger.debug("NODE {}: Battery report value = {}", this.getNode().getNodeId(), batteryLevel);

				// A Battery level of 255 means battery low.
				// Set battery level to 0
				if(batteryLevel == 255) {
					batteryLevel = 0;
					batteryLow = true;
					logger.warn("NODE {}: BATTERY LOW!", this.getNode().getNodeId());
				}
				else {
					batteryLow = false;
				}

				// If the battery level is outside bounds, then we don't know what's up!
				if(batteryLevel < 0 || batteryLevel > 100) {
					logger.warn("NODE {}: Battery state unknown ({})!", this.getNode().getNodeId(), batteryLevel);
					batteryLevel = null;
				}
				ZWaveCommandClassValueEvent zEvent = new ZWaveCommandClassValueEvent(this.getNode().getNodeId(), endpoint, this.getCommandClass(), batteryLevel);
				this.getController().notifyEventListeners(zEvent);

				dynamicDone = true;
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
		if(isGetSupported == false) {
			logger.debug("NODE {}: Node doesn't support get requests", this.getNode().getNodeId());
			return null;
		}

		logger.debug("NODE {}: Creating new message for application command BATTERY_GET", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							2, 
								(byte) getCommandClass().getKey(), 
								(byte) BATTERY_GET };
    	result.setMessagePayload(newPayload);
    	return result;		
	}

	@Override
	public boolean setOptions (ZWaveDbCommandClass options) {
		if(options.isGetSupported != null) {
			isGetSupported = options.isGetSupported;
		}
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<SerialMessage> getDynamicValues(boolean refresh) {
		if (refresh == true) {
			dynamicDone = false;
		}

		if (dynamicDone == true) {
			return null;
		}

		ArrayList<SerialMessage> result = new ArrayList<SerialMessage>();
		result.add(getValueMessage());
		return result;
	}

	/**
	 * Returns the current battery level. If the battery level is unknown, returns null
	 * @return battery level
	 */
	public Integer getBatteryLevel() {
		return batteryLevel;
	}

	/**
	 * Returns the current battery warning state.
	 * @return true if device is saying battery is low
	 */
	public Boolean getBatteryLow() {
		if(batteryLow == null) {
			return false;
		}
		return batteryLow;
	}
}
