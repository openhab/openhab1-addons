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
 * Handles the Thermostat OperatingState command class.
 * @author Dan Cunningham
 * @since 1.6.0
 */
@XStreamAlias("thermostatOperatingStateCommandClass")
public class ZWaveThermostatOperatingStateCommandClass extends ZWaveCommandClass
implements ZWaveGetCommands, ZWaveCommandClassDynamicState {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveThermostatOperatingStateCommandClass.class);

	private static final byte THERMOSTAT_OPERATING_STATE_GET              = 0x2;
	private static final byte THERMOSTAT_OPERATING_STATE_REPORT           = 0x3;

	private final Set<OperatingStateType> operatingStateTypes = new HashSet<OperatingStateType>();

	/**
	 * Creates a new instance of the ZWaveThermostatOperatingStateCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveThermostatOperatingStateCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.THERMOSTAT_OPERATING_STATE;
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
		logger.debug("NODE {}: Received Thermostat Operating State Request", this.getNode().getNodeId());
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
		case THERMOSTAT_OPERATING_STATE_REPORT:
			logger.trace("NODE {}: Process Thermostat Operating State Report", this.getNode().getNodeId());
			processThermostatOperatingStateReport(serialMessage, offset, endpoint);

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
	 * Processes a THERMOSTAT_OPERATING_STATE_REPORT message.
	 * @param serialMessage the incoming message to process.
	 * @param offset the offset position from which to start message processing.
	 * @param endpoint the endpoint or instance number this message is meant for.
	 */
	protected void processThermostatOperatingStateReport(SerialMessage serialMessage, int offset,
			int endpoint) {

		int value = serialMessage.getMessagePayloadByte(offset + 1); 

		logger.debug("NODE {}: Thermostat Operating State report value = {}", this.getNode().getNodeId(), value);

		OperatingStateType operatingStateType = OperatingStateType.getOperatingStateType(value);

		if (operatingStateType == null) {
			logger.error("NODE {}: Unknown Operating State Type = {}, ignoring report.", this.getNode().getNodeId(), value);
			return;
		}

		// operatingState type seems to be supported, add it to the list.
		if (!this.operatingStateTypes.contains(operatingStateType))
			this.operatingStateTypes.add(operatingStateType);

		logger.debug("NODE {}: Operating State Type = {} ({})", this.getNode().getNodeId(), operatingStateType.getLabel(), value);

		logger.debug("NODE {}: Thermostat Operating State Report value = {}", this.getNode().getNodeId(), operatingStateType.getLabel());
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
		logger.debug("NODE {}: Creating new message for application command THERMOSTAT_OPERATING_STATE_GET", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Get);
		byte[] payload = {
				(byte) this.getNode().getNodeId(),
				2,
				(byte) getCommandClass().getKey(),
				THERMOSTAT_OPERATING_STATE_GET
		};
		result.setMessagePayload(payload);
		return result;
	}

	/**
	 * Z-Wave OperatingStateType enumeration. The operating state type indicates the type
	 * of operating state that is reported from the thermostat.
	 * @author Dan Cunningham
	 * @since 1.6.0
	 */
	@XStreamAlias("operatingStateType")
	public enum OperatingStateType
	{
		IDLE(0,"Idle"),
		HEATING(1,"Heating"),
		COOLING(2,"Cooling"),
		FAN_ONLY(3,"Fan Only"),
		PENDING_HEAT(4,"Pending Heat"),
		PENDING_COOL(5,"Pending Cool"),
		VENT(6,"Vent / Economizer"),
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
		 * A mapping between the integer code and its corresponding operating state type
		 * to facilitate lookup by code.
		 */
		private static Map<Integer, OperatingStateType> codeToOperatingStateTypeMapping;

		private int key;
		private String label;

		private OperatingStateType(int key, String label) {
			this.key = key;
			this.label = label;
		}

		private static void initMapping() {
			codeToOperatingStateTypeMapping = new HashMap<Integer, OperatingStateType>();
			for (OperatingStateType s : values()) {
				codeToOperatingStateTypeMapping.put(s.key, s);
			}
		}

		/**
		 * Lookup function based on the operating state type code.
		 * Returns null if the code does not exist.
		 * @param i the code to lookup
		 * @return enumeration value of the operatingState type.
		 */
		public static OperatingStateType getOperatingStateType(int i) {
			if (codeToOperatingStateTypeMapping == null) {
				initMapping();
			}
			return codeToOperatingStateTypeMapping.get(i);
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
