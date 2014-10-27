/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
 * Handles the Thermostat FanState command class.
 * @author Dan Cunningham
 * @since 1.6.0
 */
@XStreamAlias("thermostatFanStateCommandClass")
public class ZWaveThermostatFanStateCommandClass extends ZWaveCommandClass
implements ZWaveGetCommands, ZWaveCommandClassDynamicState {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveThermostatFanStateCommandClass.class);

	private static final byte THERMOSTAT_FAN_STATE_GET              = 0x2;
	private static final byte THERMOSTAT_FAN_STATE_REPORT           = 0x3;

	private final Set<FanStateType> fanStateTypes = new HashSet<FanStateType>();

	/**
	 * Creates a new instance of the ZWaveThermostatFanStateCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveThermostatFanStateCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.THERMOSTAT_FAN_STATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaxVersion() {
		return 2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpoint) {
		logger.debug("NODE {}: Received Thermostat Fan State Request", this.getNode().getNodeId());
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
		case THERMOSTAT_FAN_STATE_REPORT:
			logger.trace("NODE {}: Process Thermostat Fan State Report", this.getNode().getNodeId());
			processThermostatFanStateReport(serialMessage, offset, endpoint);

			if (this.getNode().getNodeStage() != NodeStage.DONE)
				this.getNode().advanceNodeStage(NodeStage.DONE);

			break;
		default:
			logger.warn("NODE {}: Unsupported Command {} for command class {} ({}).", 
					this.getNode().getNodeId(),
					command, 
					this.getCommandClass().getLabel(),
					this.getCommandClass().getKey());
		}
	}

	/**
	 * Processes a THERMOSTAT_FAN_STATE_REPORT message.
	 * @param serialMessage the incoming message to process.
	 * @param offset the offset position from which to start message processing.
	 * @param endpoint the endpoint or instance number this message is meant for.
	 */
	protected void processThermostatFanStateReport(SerialMessage serialMessage, int offset,
			int endpoint) {

		int value = serialMessage.getMessagePayloadByte(offset + 1); 

		logger.debug("NODE {}: Thermostat fan state report value = {}", this.getNode().getNodeId(), value);

		FanStateType fanStateType = FanStateType.getFanStateType(value);

		if (fanStateType == null) {
			logger.error("NODE {}: Unknown fan state Type = {}, ignoring report.", this.getNode().getNodeId(), value);
			return;
		}

		// fanState type seems to be supported, add it to the list.
		if (!this.fanStateTypes.contains(fanStateType))
			this.fanStateTypes.add(fanStateType);

		logger.debug("NODE {}: Thermostat fan state  Report value = {}", this.getNode().getNodeId(), fanStateType.getLabel());
		ZWaveCommandClassValueEvent zEvent = new ZWaveCommandClassValueEvent(this.getNode().getNodeId(), endpoint, this.getCommandClass(), new BigDecimal(value));
		this.getController().notifyEventListeners(zEvent);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SerialMessage getValueMessage() {
		logger.debug("NODE {}: Creating new message for application command THERMOSTAT_FAN_STATE_GET", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Get);
		byte[] payload = {
				(byte) this.getNode().getNodeId(),
				2,
				(byte) getCommandClass().getKey(),
				THERMOSTAT_FAN_STATE_GET
		};
		result.setMessagePayload(payload);
		return result;
	}

	/**
	 * Z-Wave FanStateType enumeration. The fanState type indicates the type
	 * of fanState that is reported.
	 * @author Dan Cunningham
	 * @since 1.6.0
	 */
	@XStreamAlias("fanStateType")
	public enum FanStateType
	{

		IDLE(0,"Idle"),
		RUNNING(1,"Running"),
		RUNNING_HIGH(2,"Running High"),
		State_3(3,"State 3"),
		State_4(4,"State 4"),
		State_5(5,"State 5"),
		State_6(6,"State 6"),
		State_7(7,"State 7"),				// Undefined states.  May be used in the future.
		State_8(8,"State 8"), 
		State_9(9,"State 9"), 
		State_10(10,"State 10"), 
		State_11(11,"State 11"), 
		State_12(12,"State 12"), 
		State_13(12,"State 13"), 
		State_14(14,"State 14"), 
		State_15(15,"State 15");

		/**
		 * A mapping between the integer code and its corresponding fanState type
		 * to facilitate lookup by code.
		 */
		private static Map<Integer, FanStateType> codeToFanStateTypeMapping;

		private int key;
		private String label;

		private FanStateType(int key, String label) {
			this.key = key;
			this.label = label;
		}

		private static void initMapping() {
			codeToFanStateTypeMapping = new HashMap<Integer, FanStateType>();
			for (FanStateType s : values()) {
				codeToFanStateTypeMapping.put(s.key, s);
			}
		}

		/**
		 * Lookup function based on the fanState type code.
		 * Returns null if the code does not exist.
		 * @param i the code to lookup
		 * @return enumeration value of the fanState type.
		 */
		public static FanStateType getFanStateType(int i) {
			if (codeToFanStateTypeMapping == null) {
				initMapping();
			}
			return codeToFanStateTypeMapping.get(i);
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
