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
 * Handles the Alarm Sensor command class. Alarm sensors indicate an
 * event for each of their supported alarm types.
 * The event is reported as occurs (0xFF) or does not occur (0x00).
 * The commands include the possibility to get a given
 * value and report a value.
 * TODO: Add support for more than one sensor type. 
 * @author Jan-Willem Spuij
 * @since 1.3.0
 */
@XStreamAlias("alarmSensorCommandClass")
public class ZWaveAlarmSensorCommandClass extends ZWaveCommandClass 
	implements ZWaveGetCommands, ZWaveCommandClassInitialization, ZWaveCommandClassDynamicState {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveAlarmSensorCommandClass.class);
	
	private static final int SENSOR_ALARM_GET = 0x01;
	private static final int SENSOR_ALARM_REPORT = 0x02;
	private static final int SENSOR_ALARM_SUPPORTED_GET = 0x03;
	private static final int SENSOR_ALARM_SUPPORTED_REPORT = 0x04;
	
	private final Set<AlarmType> alarms = new HashSet<AlarmType>();
	
	/**
	 * Creates a new instance of the ZWaveAlarmSensorCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveAlarmSensorCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.SENSOR_ALARM;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpoint) {
		logger.trace("Handle Message Sensor Alarm Request");
		logger.debug(String.format("Received Sensor Alarm Request for Node ID = %d", this.getNode().getNodeId()));
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
			case SENSOR_ALARM_GET:
			case SENSOR_ALARM_SUPPORTED_GET:
				logger.warn(String.format("Command 0x%02X not implemented.", command));
				return;
			case SENSOR_ALARM_REPORT:
				logger.trace("Process Sensor Alarm Report");
				
				int sourceNode = serialMessage.getMessagePayloadByte(offset + 1);
				int alarmTypeCode = serialMessage.getMessagePayloadByte(offset + 2);
				int value = serialMessage.getMessagePayloadByte(offset + 3);
				
				logger.debug(String.format("Sensor Alarm report from nodeId = %d", this.getNode().getNodeId()));
				logger.debug(String.format("Source node ID = %d", sourceNode));
				logger.debug(String.format("Value = 0x%02x", value));
				
				AlarmType alarmType = AlarmType.getAlarmType(alarmTypeCode);
				
				if (alarmType == null) {
					logger.error(String.format("Unknown Alarm Type = 0x%02x, ignoring report.", alarmTypeCode));
					return;
				}
				
				// alarm type seems to be supported, add it to the list.
				if (!alarms.contains(alarmType))
					this.alarms.add(alarmType);

				logger.debug(String.format("Alarm Type = %s (0x%02x)", alarmType.getLabel(), alarmTypeCode));
				
				ZWaveAlarmSensorValueEvent zEvent = new ZWaveAlarmSensorValueEvent(this.getNode().getNodeId(), endpoint, alarmType, value);
				this.getController().notifyEventListeners(zEvent);
				
				if (this.getNode().getNodeStage() != NodeStage.DONE)
					this.getNode().advanceNodeStage(NodeStage.DONE);
				break;
			case SENSOR_ALARM_SUPPORTED_REPORT:
				logger.debug("Process Sensor Supported Alarm Report");

				int numBytes = serialMessage.getMessagePayloadByte(offset + 1);

				int manufacturerId = this.getNode().getManufacturer();
				int deviceType = this.getNode().getDeviceType();
				
				// Fibaro alarm sensors do not provide a bitmap of alarm types, but list them byte by byte.
				
				if (manufacturerId == 0x010F && deviceType == 0x0700) {
					logger.warn("Detected Fibaro FGK - 101 Door / Window sensor, activating workaround for incorrect encoding of supported alarm bitmap.");
				
					for(int i=0; i < numBytes; ++i ) {
						int index = serialMessage.getMessagePayloadByte(offset + i + 2);
						if(index >= AlarmType.values().length)
							continue;
						
						AlarmType alarmTypeToAdd = AlarmType.getAlarmType(index);
						this.alarms.add(alarmTypeToAdd);
						logger.debug(String.format("Added alarm type %s (0x%02x)", alarmTypeToAdd.getLabel(), index));
					}
				} else {
					for(int i=0; i < numBytes; ++i ) {
						for(int bit = 0; bit < 8; ++bit) {
							    if( ((serialMessage.getMessagePayloadByte(offset + i +2)) & (1 << bit) ) == 0 )
							    	continue;
							    
							    int index = (i << 3) + bit;
							    if(index >= AlarmType.values().length)
							    	continue;
							    
							    // (n)th bit is set. n is the index for the alarm type enumeration.
							    
								AlarmType alarmTypeToAdd = AlarmType.getAlarmType(index);
								this.alarms.add(alarmTypeToAdd);
								logger.debug(String.format("Added alarm type %s (0x%02x)", alarmTypeToAdd.getLabel(), index));
						}
					}
				}
				
				this.getNode().advanceNodeStage(NodeStage.DYNAMIC);
				break;
			default:
			logger.warn(String.format("Unsupported Command 0x%02X for command class %s (0x%02X).", 
					command, 
					this.getCommandClass().getLabel(),
					this.getCommandClass().getKey()));
		}
	}

	/**
	 * Gets a SerialMessage with the SENSOR_ALARM_GET command 
	 * @return the serial message
	 */
	public SerialMessage getValueMessage() {
		for (AlarmType alarmType : this.alarms) {
			return getMessage(alarmType);
		}
		
		// in case there are no supported alarms, get them.
		
		return this.getSupportedMessage();
	}
	
	/**
	 * Gets a SerialMessage with the SENSOR_ALARM_GET command 
	 * @return the serial message
	 */
	public SerialMessage getMessage(AlarmType alarmType) {
		logger.debug("Creating new message for application command SENSOR_ALARM_GET for node {}", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							3, 
								(byte) getCommandClass().getKey(), 
								(byte) SENSOR_ALARM_GET,
								(byte) alarmType.getKey() };
    	result.setMessagePayload(newPayload);
    	return result;		
	}
	
	/**
	 * Gets a SerialMessage with the SENSOR_ALARM_SUPPORTED_GET command 
	 * @return the serial message, or null if the supported command is not supported.
	 */
	public SerialMessage getSupportedMessage() {
		logger.debug("Creating new message for application command SENSOR_ALARM_SUPPORTED_GET for node {}", this.getNode().getNodeId());
		
		if (this.getNode().getManufacturer() == 0x010F && this.getNode().getDeviceType() == 0x0501) {
			logger.warn("Detected Fibaro FGBS001 Universal Sensor - this device fails to respond to SENSOR_ALARM_GET and SENSOR_ALARM_SUPPORTED_GET.");
			return null;
		}
		if (this.getNode().getManufacturer() == 0x010F && this.getNode().getDeviceType() == 0x0600) {
			logger.warn("Detected Fibaro FGWPE Wall Plug - this device fails to respond to SENSOR_ALARM_GET and SENSOR_ALARM_SUPPORTED_GET.");
			return null;
		}
		
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.High);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							2, 
								(byte) getCommandClass().getKey(), 
								(byte) SENSOR_ALARM_SUPPORTED_GET };
    	result.setMessagePayload(newPayload);
    	return result;		
	}

	/**
	 * Initializes the alarm sensor command class. Requests the supported alarm types.
	 */
	@Override
	public Collection<SerialMessage> initialize() {
		ArrayList<SerialMessage> result = new ArrayList<SerialMessage>();
		
		if (this.getNode().getManufacturer() == 0x010F && this.getNode().getDeviceType() == 0x0501) {
				logger.warn("Detected Fibaro FGBS001 Universal Sensor - this device fails to respond to SENSOR_ALARM_GET and SENSOR_ALARM_SUPPORTED_GET.");
				return result;
		}
		if (this.getNode().getManufacturer() == 0x010F && this.getNode().getDeviceType() == 0x0600) {
			logger.warn("Detected Fibaro FGWPE Wall Plug - this device fails to respond to SENSOR_ALARM_GET and SENSOR_ALARM_SUPPORTED_GET.");
			return result;
		}

		result.add(this.getSupportedMessage());
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<SerialMessage> getDynamicValues() {
		ArrayList<SerialMessage> result = new ArrayList<SerialMessage>();
		
		for (AlarmType alarmType : this.alarms) {
			result.add(getMessage(alarmType));
			if (this.getNode().getManufacturer() == 0x010F && this.getNode().getDeviceType() == 0x0700) {
				logger.warn("Detected Fibaro FGK - 101 Door / Window sensor, only requesting alarm type {}.", alarmType.getLabel());
				break;
			}
		}
		
		return result;
	}

	
	/**
	 * Z-Wave AlarmType enumeration. The alarm type indicates the type
	 * of alarm that is reported.
	 * @author Jan-Willem Spuij
	 * @since 1.3.0
	 */
	@XStreamAlias("alarmType")
	public enum AlarmType {
		GENERAL(0, "General"), 
		SMOKE(1, "Smoke"),
		CARBON_MONOXIDE(2, "Carbon Monoxide"), 
		CARBON_DIOXIDE(3, "Carbon Dioxide"), 
		HEAT(4, "Heat"),
		FLOOD(5, "Flood");

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
	 * Z-Wave Alarm Sensor Event class. Indicates that an alarm value
	 * changed. 
	 * @author Jan-Willem Spuij
	 * @since 1.4.0
	 */
	public class ZWaveAlarmSensorValueEvent extends ZWaveCommandClassValueEvent {

		private AlarmType alarmType;
		
		/**
		 * Constructor. Creates a instance of the ZWaveAlarmSensorValueEvent class.
		 * @param nodeId the nodeId of the event
		 * @param endpoint the endpoint of the event.
		 * @param alarmType the alarm type that triggered the event;
		 * @param value the value for the event.
		 */
		private ZWaveAlarmSensorValueEvent(int nodeId, int endpoint,
				AlarmType alarmType, Object value) {
			super(nodeId, endpoint, CommandClass.SENSOR_ALARM, value);
			this.alarmType = alarmType;
		}

		/**
		 * Gets the alarm type for this alarm sensor value event.
		 */
		public AlarmType getAlarmType() {
			return alarmType;
		}
	}
}
