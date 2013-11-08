/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.zwave.internal.protocol.NodeStage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Handles the Thermostat Setpoint command class.
 * @author Matthew Bowman
 * @since 1.4.0
 */
@XStreamAlias("thermostatSetpointCommandClass")
public class ZWaveThermostatSetpointCommandClass extends ZWaveCommandClass implements ZWaveBasicCommands, ZWaveCommandClassDynamicState {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveThermostatSetpointCommandClass.class);
	
	private static final byte THERMOSTAT_SETPOINT_SET              = 0x1;
	private static final byte THERMOSTAT_SETPOINT_GET              = 0x2;
	private static final byte THERMOSTAT_SETPOINT_REPORT           = 0x3;
	private static final byte THERMOSTAT_SETPOINT_SUPPORTED_GET    = 0x4;
	private static final byte THERMOSTAT_SETPOINT_SUPPORTED_REPORT = 0x5;

	/**
	 * Creates a new instance of the ZWaveThermostatSetpointCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveThermostatSetpointCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.THERMOSTAT_SETPOINT;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpoint) {
		logger.trace("Handle Message Thermostat Setpoint Request");
		logger.debug(String.format("Received Thermostat Setpoint Request for Node ID = %d", this.getNode().getNodeId()));
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
			case THERMOSTAT_SETPOINT_SET:
			case THERMOSTAT_SETPOINT_GET:
			case THERMOSTAT_SETPOINT_SUPPORTED_GET:
			case THERMOSTAT_SETPOINT_SUPPORTED_REPORT:
				logger.warn(String.format("Command 0x%02X not implemented.", 
						command));
				return;
			case THERMOSTAT_SETPOINT_REPORT:
				logger.trace("Process Thermostat Setpoint Report");
				processThermostatSetpointReport(serialMessage, offset, endpoint);
				
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
	 * Processes a THERMOSTAT_SETPOINT_REPORT message.
	 * @param serialMessage the incoming message to process.
	 * @param offset the offset position from which to start message processing.
	 * @param endpoint the endpoint or instance number this message is meant for.
	 */
	protected void processThermostatSetpointReport(SerialMessage serialMessage, int offset,
			int endpoint) {
		BigDecimal value = extractValue(serialMessage.getMessagePayload(), offset + 2);
		logger.debug(String.format("Thermostat Setpoint Report from Node ID = %d, value = %s", this.getNode().getNodeId(), value.toPlainString()));
		ZWaveCommandClassValueEvent zEvent = new ZWaveCommandClassValueEvent(this.getNode().getNodeId(), endpoint, this.getCommandClass(), value);
		this.getController().notifyEventListeners(zEvent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<SerialMessage> getDynamicValues() {
		List<SerialMessage> result = new ArrayList<SerialMessage>();
		result.add(getValueMessage());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SerialMessage getValueMessage() {
		logger.debug("Creating new message for application command THERMOSTAT_SETPOINT_GET for node {}", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Get);
		byte[] payload = {
				(byte) this.getNode().getNodeId(),
				3,
				(byte) getCommandClass().getKey(),
				THERMOSTAT_SETPOINT_GET,
				(byte) SetpointType.HEATING.getKey()
		};
		result.setMessagePayload(payload);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SerialMessage setValueMessage(int value) {
		return setValueMessage(new BigDecimal(value));
	}
	
	/**
	 * Gets a SerialMessage with the THERMOSTAT_SETPOINT_SET command 
	 * @param setpoint the setpoint to set.
	 * @return the serial message
	 */
	public SerialMessage setValueMessage(BigDecimal setpoint) {
		logger.debug("Creating new message for application command THERMOSTAT_SETPOINT_SET for node {}", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Set);
		byte[] payload = ArrayUtils.addAll(
			new byte[] { 
				(byte) this.getNode().getNodeId(),
				6,
				(byte) getCommandClass().getKey(),
				THERMOSTAT_SETPOINT_SET,
				(byte) SetpointType.HEATING.getKey()
			}, 
			encodeValue(setpoint) // 2 bytes
		);
		result.setMessagePayload(payload);
    	return result;
	}
	
	/**
	 * Z-Wave SetpointType enumeration. The setpoint type indicates the type
	 * of setpoint that is reported.
	 * @author Matthew Bowman
	 * @since 1.4.0
	 */
	@XStreamAlias("setpointType")
	public enum SetpointType
	{
		HEATING(1,"Heating"),
		COOLING(2,"Cooling"),
		FURNACE(7,"Furnace"),
		DRY_AIR(8,"Dry Air"),
		MOIST_AIR(9,"Moist Air"),
		AUTO_CHANGEOVER(10,"Auto Changeover"),
		HEATING_ECON(11,"Heating Economical"),
		COOLING_ECON(12, "Cooling Econmoical"),
		AWAY_HEATING(13, "Away Heating");

		/**
		 * A mapping between the integer code and its corresponding setpoint type
		 * to facilitate lookup by code.
		 */
		private static Map<Integer, SetpointType> codeToSetpointTypeMapping;

		private int key;
		private String label;

		private SetpointType(int key, String label) {
			this.key = key;
			this.label = label;
		}

		private static void initMapping() {
			codeToSetpointTypeMapping = new HashMap<Integer, SetpointType>();
			for (SetpointType s : values()) {
				codeToSetpointTypeMapping.put(s.key, s);
			}
		}

		/**
		 * Lookup function based on the setpoint type code.
		 * Returns null if the code does not exist.
		 * @param i the code to lookup
		 * @return enumeration value of the setpoint type.
		 */
		public static SetpointType getSetpointType(int i) {
			if (codeToSetpointTypeMapping == null) {
				initMapping();
			}
			return codeToSetpointTypeMapping.get(i);
		}

		/**
		 * @return the key
		 */
		public int getKey() {
			return key;
		}

		/**
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}
	}

}
