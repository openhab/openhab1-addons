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
 * Handles the Thermostat FanMode command class.
 * @author Dan Cunningham
 * @since 1.6.0
 */
@XStreamAlias("thermostatFanModeCommandClass")
public class ZWaveThermostatFanModeCommandClass extends ZWaveCommandClass
implements ZWaveBasicCommands, ZWaveCommandClassInitialization,
ZWaveCommandClassDynamicState {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveThermostatFanModeCommandClass.class);

	private static final byte THERMOSTAT_FAN_MODE_SET              = 0x1;
	private static final byte THERMOSTAT_FAN_MODE_GET              = 0x2;
	private static final byte THERMOSTAT_FAN_MODE_REPORT           = 0x3;
	private static final byte THERMOSTAT_FAN_MODE_SUPPORTED_GET    = 0x4;
	private static final byte THERMOSTAT_FAN_MODE_SUPPORTED_REPORT = 0x5;

	private final Set<FanModeType> fanModeTypes = new HashSet<FanModeType>();

	/**
	 * Creates a new instance of the ZWaveThermostatFanModeCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveThermostatFanModeCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.THERMOSTAT_FAN_MODE;
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
		logger.debug("NODE {}: Received Thermostat Fan Mode Request", this.getNode().getNodeId());
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
		case THERMOSTAT_FAN_MODE_SET:
		case THERMOSTAT_FAN_MODE_GET:
		case THERMOSTAT_FAN_MODE_SUPPORTED_GET:
			logger.warn("NODE {}: Command {} not implemented.", 
				this.getNode().getNodeId(),command);
			return;
		case THERMOSTAT_FAN_MODE_SUPPORTED_REPORT:
			logger.debug("NODE {}: Process Thermostat Supported Fan Mode Report", this.getNode().getNodeId());

			int payloadLength = serialMessage.getMessagePayload().length;

			for(int i = offset + 1; i < payloadLength; ++i ) {
				int bitMask = serialMessage.getMessagePayloadByte(i);
				for(int bit = 0; bit < 8; ++bit) {
					if ( (bitMask & (1 << bit) ) == 0 )
						continue;

					int index = ((i - (offset + 1)) * 8 ) + bit;             
					if(index >= FanModeType.values().length)
						continue;

					logger.debug("NODE {}: looking up Fan Mode type {}", this.getNode().getNodeId(), index);
					// (n)th bit is set. n is the index for the fanMode type enumeration.
					FanModeType fanModeTypeToAdd = FanModeType.getFanModeType(index);
					if(fanModeTypeToAdd != null){
						this.fanModeTypes.add(fanModeTypeToAdd);
						logger.debug("NODE {}: Added Fan Mode type {} ({})", this.getNode().getNodeId(), fanModeTypeToAdd.getLabel(), index);
					} else {
						logger.warn("NODE {}: Uknown fan mode type {}", this.getNode().getNodeId(), index);
					}
				}
			}

			this.getNode().advanceNodeStage(NodeStage.DYNAMIC);
			break;
		case THERMOSTAT_FAN_MODE_REPORT:
			logger.trace("NODE {}: Process Thermostat Fan Mode Report",this.getNode().getNodeId());
			processThermostatFanModeReport(serialMessage, offset, endpoint);

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
	 * Processes a THERMOSTAT_FAN_MODE_REPORT message.
	 * @param serialMessage the incoming message to process.
	 * @param offset the offset position from which to start message processing.
	 * @param endpoint the endpoint or instance number this message is meant for.
	 */
	protected void processThermostatFanModeReport(SerialMessage serialMessage, int offset,
			int endpoint) {

		int value = serialMessage.getMessagePayloadByte(offset + 1); 

		logger.debug("NODE {}: Thermostat Fan Mode report value = {}", this.getNode().getNodeId(), value);

		FanModeType fanModeType = FanModeType.getFanModeType(value);

		if (fanModeType == null) {
			logger.error("NODE {}: Unknown Fan Mode Type = {}, ignoring report.", this.getNode().getNodeId(),value);
			return;
		}

		// fanMode type seems to be supported, add it to the list.
		if (!this.fanModeTypes.contains(fanModeType))
			this.fanModeTypes.add(fanModeType);

		logger.debug("NODE {}: Thermostat Fan Mode Report value = {}", this.getNode().getNodeId(), fanModeType.getLabel());
		ZWaveCommandClassValueEvent zEvent = new ZWaveCommandClassValueEvent(this.getNode().getNodeId(), endpoint, this.getCommandClass(), new BigDecimal(value));
		this.getController().notifyEventListeners(zEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<SerialMessage> initialize() {
		ArrayList<SerialMessage> result = new ArrayList<SerialMessage>();
		result.add(this.getSupportedMessage());
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SerialMessage getValueMessage() {
		logger.debug("NODE {}: Creating new message for application command THERMOSTAT_FAN_MODE_GET", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Get);
		byte[] payload = {
				(byte) this.getNode().getNodeId(),
				2,
				(byte) getCommandClass().getKey(),
				THERMOSTAT_FAN_MODE_GET
		};
		result.setMessagePayload(payload);
		return result;
	}

	/**
	 * Gets a SerialMessage with the THERMOSTAT_FAN_MODE_SUPPORTED_GET command 
	 * @return the serial message, or null if the supported command is not supported.
	 */
	public SerialMessage getSupportedMessage() {
		logger.debug("NODE {}: Creating new message for application command THERMOSTAT_FAN_MODE_SUPPORTED_GET", this.getNode().getNodeId());

		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.High);
		byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
				2, 
				(byte) getCommandClass().getKey(), 
				(byte) THERMOSTAT_FAN_MODE_SUPPORTED_GET };
		result.setMessagePayload(newPayload);
		return result;		
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public SerialMessage setValueMessage(int value) {

		if(fanModeTypes.isEmpty())
			return this.getSupportedMessage();

		if(!fanModeTypes.contains(FanModeType.getFanModeType(value))){
			logger.error("NODE {}: Unsupported fanMode type {}", value, this.getNode().getNodeId());
			
			return null;
		}

		logger.debug("NODE {}: Creating new message for application command THERMOSTAT_FAN_MODE_SET", this.getNode().getNodeId());

		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Set);
		byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
				3, 
				(byte) getCommandClass().getKey(), 
				(byte) THERMOSTAT_FAN_MODE_SET,
				(byte) value
		};
		result.setMessagePayload(newPayload);
		return result;		
	}

	/**
	 * Z-Wave FanModeType enumeration. The fanMode type indicates the type
	 * of fanMode that is reported.
	 * @author Dan Cunningham
	 * @since 1.6.0
	 */
	@XStreamAlias("fanModeType")
	public enum FanModeType
	{
		AUTO_LOW(0,"Auto Low"),
		ON_LOW(1,"On Low"),
		AUTO_HIGH(2,"Auto High"),
		ON_HIGH(3,"On High"),
		UNKNOWN_4(4,"Unknown 4"),
		UNKNOWN_5(5,"Unknown 5"),
		CIRCULATE(6,"Circulate");

		/**
		 * A mapping between the integer code and its corresponding fan mode type
		 * to facilitate lookup by code.
		 */
		private static Map<Integer, FanModeType> codeToFanModeTypeMapping;

		private int key;
		private String label;

		private FanModeType(int key, String label) {
			this.key = key;
			this.label = label;
		}

		private static void initMapping() {
			codeToFanModeTypeMapping = new HashMap<Integer, FanModeType>();
			for (FanModeType s : values()) {
				codeToFanModeTypeMapping.put(s.key, s);
			}
		}

		/**
		 * Lookup function based on the fan mode type code.
		 * Returns null if the code does not exist.
		 * @param i the code to lookup
		 * @return enumeration value of the fan mode type.
		 */
		public static FanModeType getFanModeType(int i) {
			if (codeToFanModeTypeMapping == null) {
				initMapping();
			}
			return codeToFanModeTypeMapping.get(i);
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
