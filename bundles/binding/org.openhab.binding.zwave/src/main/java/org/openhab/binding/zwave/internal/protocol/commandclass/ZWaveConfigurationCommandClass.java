/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.zwave.internal.protocol.ConfigurationParameter;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Handles the Configuration command class. This allows reading and writing of
 * node configuration parameters
 * 
 * @author Chris Jackson
 * @since 1.4.0
 */
@XStreamAlias("configurationCommandClass")
public class ZWaveConfigurationCommandClass extends ZWaveCommandClass {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveConfigurationCommandClass.class);

	private static final int CONFIGURATIONCMD_SET = 0x04;
	private static final int CONFIGURATIONCMD_GET = 0x05;
	private static final int CONFIGURATIONCMD_REPORT = 0x06;

	// Stores the list of configuration parameters. These are used for persistence of values and restore.
	private Map<Integer, ConfigurationParameter> configParameters = new HashMap<Integer, ConfigurationParameter>();
	
	/**
	 * Creates a new instance of the ZWaveConfigurationCommandClass class.
	 * 
	 * @param node
	 *            the node this command class belongs to
	 * @param controller
	 *            the controller to use
	 * @param endpoint
	 *            the endpoint this Command class belongs to
	 */
	public ZWaveConfigurationCommandClass(ZWaveNode node, ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.CONFIGURATION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage, int offset, int endpoint) {
		logger.debug(String.format("Received Configuration Request for Node ID = %d", this.getNode().getNodeId()));
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
		case CONFIGURATIONCMD_SET:
			logger.debug("Process Configuration Set");
			processConfigurationReport(serialMessage, offset);
			break;
		case CONFIGURATIONCMD_GET:
			logger.warn(String.format("Command 0x%02X not implemented.", command));
			return;
		case CONFIGURATIONCMD_REPORT:
			logger.debug("Process Configuration Report");
			processConfigurationReport(serialMessage, offset);
			break;
		default:
			logger.warn(String.format("Unsupported Command 0x%02X for command class %s (0x%02X).", command, this
					.getCommandClass().getLabel(), this.getCommandClass().getKey()));
		}
	}

	/**
	 * Processes a CONFIGURATIONCMD_REPORT / CONFIGURATIONCMD_SET message.
	 * 
	 * @param serialMessage
	 *            the incoming message to process.
	 * @param offset
	 *            the offset position from which to start message processing.
	 * @param endpoint
	 *            the endpoint or instance number this message is meant for.
	 */
	protected void processConfigurationReport(SerialMessage serialMessage, int offset) {
		// Extract the parameter index and value
		int parameter = serialMessage.getMessagePayloadByte(offset + 1);
		int size = serialMessage.getMessagePayloadByte(offset + 2);

		// Recover the data
		int value = extractValue(serialMessage.getMessagePayload(), offset + 3, size);

		logger.debug(String.format("Node configuration report from nodeId = %d, parameter = %d, value = 0x%02X", this
				.getNode().getNodeId(), parameter, value));

		ConfigurationParameter configurationParameter = new ConfigurationParameter(parameter, value, size);
		
		this.configParameters.put(parameter, configurationParameter);
		
		ZWaveConfigurationParameterEvent zEvent = new ZWaveConfigurationParameterEvent(this.getNode().getNodeId(),
				configurationParameter);
		this.getController().notifyEventListeners(zEvent);
	}

	/**
	 * Gets a SerialMessage with the CONFIGURATIONCMD_GET command
	 * 
	 * @return the serial message
	 */
	public SerialMessage getConfigMessage(int parameter) {
		logger.debug("Creating new message for application command CONFIGURATIONCMD_GET for node {}", this.getNode()
				.getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData,
				SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
		byte[] newPayload = { (byte) this.getNode().getNodeId(), 3, (byte) getCommandClass().getKey(),
				(byte) CONFIGURATIONCMD_GET, (byte) (parameter & 0xff) };
		result.setMessagePayload(newPayload);
		return result;
	}

	/**
	 * Gets a SerialMessage with the CONFIGURATIONCMD_SET command
	 * 
	 * @param parameter the parameter to set.
	 * @return the serial message
	 */
	public SerialMessage setConfigMessage(ConfigurationParameter parameter) {
		logger.debug("Creating new message for application command CONFIGURATIONCMD_SET for node {}", this.getNode()
				.getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData,
				SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Set);
		byte[] newPayload = new byte[parameter.getSize() + 6];
		newPayload[0] = (byte) this.getNode().getNodeId();
		newPayload[1] = (byte) (4 + parameter.getSize());
		newPayload[2] = (byte) getCommandClass().getKey();
		newPayload[3] = (byte) CONFIGURATIONCMD_SET;
		newPayload[4] = (byte) (parameter.getIndex() & 0xFF);
		newPayload[5] = (byte) (parameter.getSize() & 0xFF);

		for (int i=0; i < parameter.getSize(); i++) {
			newPayload[6 + i] = (byte) (parameter.getValue() >> ((parameter.getSize() - i - 1) * 8) & 0xFF);
		}

		result.setMessagePayload(newPayload);
		return result;
	}
	
	/**
	 * Gets the stored parameter.
	 * @param index the parameter to get.
	 * @return the stored parameter value;
	 */
	public ConfigurationParameter getParameter(Integer index) {
		return this.configParameters.get(index);
	}

	/**
	 * ZWave configuration parameter received event.
	 * Sent from the Configuration Command Class to the binding
	 * when a configuration value is received.
	 * 
	 * @author Chris Jackson
	 * @since 1.4.0
	 */
	public class ZWaveConfigurationParameterEvent extends ZWaveEvent {

		private final ConfigurationParameter parameter;
		
		/**
		 * Constructor. Creates a new instance of the ZWaveConfigurationParameterEvent
		 * class.
		 * @param nodeId the nodeId of the event. Must be set to the controller node.
		 */
		public ZWaveConfigurationParameterEvent(int nodeId, ConfigurationParameter parameter) {
			super(nodeId, 1);
			this.parameter = parameter;
		}

		/**
		 * Returns the {@link ConfigurationParameter} that was received as event. 
		 * @return the configuration parameter.
		 */
		public ConfigurationParameter getParameter() {
			return parameter;
		}
	}
}
