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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
 * Handles the Alarm  command class.
 * The event is reported as occurs (0xFF) or does not occur (0x00).
 * @author Chris Jackson
 * @since 1.6.0
 */
@XStreamAlias("alarmCommandClass")
public class ZWaveAlarmCommandClass extends ZWaveCommandClass 
	implements ZWaveGetCommands {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveAlarmCommandClass.class);
	
	private static final int ALARM_GET = 0x04;
	private static final int ALARM_REPORT = 0x05;
	
	private final Set<AlarmType> alarms = new HashSet<AlarmType>();
	
	/**
	 * Creates a new instance of the ZWaveAlarmCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveAlarmCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.ALARM;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpoint) {
		logger.trace("Handle Message Alarm Request");
		logger.debug(String.format("NODE %d: Received Alarm Request", this.getNode().getNodeId()));
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
			case ALARM_GET:
				logger.warn(String.format("Command 0x%02X not implemented.", command));
				return;
			case ALARM_REPORT:
				logger.trace("Process Alarm Report");

				int alarmTypeCode = serialMessage.getMessagePayloadByte(offset + 1);
				int value = serialMessage.getMessagePayloadByte(offset + 2);

				logger.debug(String.format("NODE %d: Alarm report - Value = 0x%02x", this.getNode().getNodeId(), value));
				
				AlarmType alarmType = AlarmType.getAlarmType(alarmTypeCode);
				
				if (alarmType == null) {
					logger.error(String.format("NODE %d: Unknown Alarm Type = 0x%02x, ignoring report.", this.getNode().getNodeId(), alarmTypeCode));
					return;
				}
				
				// alarm type seems to be supported, add it to the list.
				if (!alarms.contains(alarmType)) {
					this.alarms.add(alarmType);
				}

				logger.debug(String.format("NODE %d: Alarm Type = %s (0x%02x)", this.getNode().getNodeId(), alarmType.getLabel(), alarmTypeCode));
				
				ZWaveAlarmValueEvent zEvent = new ZWaveAlarmValueEvent(this.getNode().getNodeId(), endpoint, alarmType, value);
				this.getController().notifyEventListeners(zEvent);
				
				if (this.getNode().getNodeStage() != NodeStage.DONE) {
					this.getNode().advanceNodeStage(NodeStage.DONE);
				}
				break;
			default:
				logger.warn(String.format("Unsupported Command 0x%02X for command class %s (0x%02X).", 
					command, 
					this.getCommandClass().getLabel(),
					this.getCommandClass().getKey()));
				break;
		}
	}

	/**
	 * Gets a SerialMessage with the ALARM_GET command 
	 * @return the serial message
	 */
	public SerialMessage getValueMessage() {
		for (AlarmType alarmType : this.alarms) {
			return getMessage(alarmType);
		}
		
		// in case there are no supported alarms, get them.
		
		return getMessage(AlarmType.GENERAL);
	}
	
	/**
	 * Gets a SerialMessage with the ALARM_GET command 
	 * @return the serial message
	 */
	public SerialMessage getMessage(AlarmType alarmType) {
		logger.debug("NODE {}: Creating new message for application command ALARM_GET", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							3, 
								(byte) getCommandClass().getKey(), 
								(byte) ALARM_GET,
								(byte) alarmType.getKey()
							};
    	result.setMessagePayload(newPayload);
    	return result;		
	}

	/**
	 * Z-Wave AlarmType enumeration. The alarm type indicates the type
	 * of alarm that is reported.
	 */
	@XStreamAlias("alarmType")
	public enum AlarmType {
		GENERAL(0, "General");

		/**
		 * A mapping between the integer code and its corresponding Alarm type
		 * to facilitate lookup by code.
		 */
		private static Map<Integer, AlarmType> codeToAlarmTypeMapping;

		private int key;
		private String label;

		private AlarmType(int key, String label) {
			this.key = key;
			this.label = label;
		}

		private static void initMapping() {
			codeToAlarmTypeMapping = new HashMap<Integer, AlarmType>();
			for (AlarmType s : values()) {
				codeToAlarmTypeMapping.put(s.key, s);
			}
		}

		/**
		 * Lookup function based on the alarm type code.
		 * Returns null if the code does not exist.
		 * @param i the code to lookup
		 * @return enumeration value of the alarm type.
		 */
		public static AlarmType getAlarmType(int i) {
			if (codeToAlarmTypeMapping == null) {
				initMapping();
			}
			
			return codeToAlarmTypeMapping.get(i);
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
	
	
	/**
	 * Z-Wave Alarm Event class. Indicates that an alarm value
	 * changed.
	 */
	public class ZWaveAlarmValueEvent extends ZWaveCommandClassValueEvent {

		private AlarmType alarmType;
		
		/**
		 * Constructor. Creates a instance of the ZWaveAlarmValueEvent class.
		 * @param nodeId the nodeId of the event
		 * @param endpoint the endpoint of the event.
		 * @param alarmType the alarm type that triggered the event;
		 * @param value the value for the event.
		 */
		private ZWaveAlarmValueEvent(int nodeId, int endpoint,
				AlarmType alarmType, Object value) {
			super(nodeId, endpoint, CommandClass.ALARM, value);
			this.alarmType = alarmType;
		}

		/**
		 * Gets the alarm type for this alarm value event.
		 */
		public AlarmType getAlarmType() {
			return alarmType;
		}
	}
}
