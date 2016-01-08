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
import java.util.HashMap;
import java.util.Map;

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
 * Handles the Alarm  command class.
 * The event is reported as occurs (0xFF) or does not occur (0x00).
 * @author Chris Jackson
 * @since 1.6.0
 */
@XStreamAlias("alarmCommandClass")
public class ZWaveAlarmCommandClass extends ZWaveCommandClass 
	implements ZWaveGetCommands, ZWaveCommandClassDynamicState {

	@XStreamOmitField
	private static final Logger logger = LoggerFactory.getLogger(ZWaveAlarmCommandClass.class);
	
	private static final int ALARM_GET = 0x04;
	private static final int ALARM_REPORT = 0x05;
	
	private final Map<AlarmType, Alarm> alarms = new HashMap<AlarmType, Alarm>();
	
	@XStreamOmitField
	private boolean initialiseDone = false;
	@XStreamOmitField
	private boolean dynamicDone = false;

	private boolean isGetSupported = true;

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
		logger.debug("NODE {}: Received Alarm Request", this.getNode().getNodeId());
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
			case ALARM_GET:
				logger.warn("Command {} not implemented.", command);
				return;
			case ALARM_REPORT:
				logger.trace("Process Alarm Report");

				int alarmTypeCode = serialMessage.getMessagePayloadByte(offset + 1);
				int value = serialMessage.getMessagePayloadByte(offset + 2);

				logger.debug("NODE {}: Alarm report - Value = {}", this.getNode().getNodeId(), value);
				
				AlarmType alarmType = AlarmType.getAlarmType(alarmTypeCode);
				
				if (alarmType == null) {
					logger.error("NODE {}: Unknown Alarm Type = {}, ignoring report.", this.getNode().getNodeId(), alarmTypeCode);
					return;
				}
				
				// alarm type seems to be supported, add it to the list.
				// alarm type seems to be supported, add it to the list.
				Alarm alarm = alarms.get(alarmType);
				if (alarm == null) {
					alarm = new Alarm(alarmType);
					this.alarms.put(alarmType, alarm);
				}
				alarm.setInitialised();

				logger.debug("NODE {}: Alarm Type = {} ({})", this.getNode().getNodeId(), alarmType.getLabel(), alarmTypeCode);
				
				ZWaveAlarmValueEvent zEvent = new ZWaveAlarmValueEvent(this.getNode().getNodeId(), endpoint, alarmType, value);
				this.getController().notifyEventListeners(zEvent);
				
				dynamicDone = true;
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
		//TODO: Why does this return!!!???!!!
		for (Map.Entry<AlarmType, Alarm> entry : this.alarms.entrySet()) {
			return getMessage(entry.getValue().getAlarmType());
		}

		// in case there are no supported alarms, get them.
		
		return getMessage(AlarmType.GENERAL);
	}
	
	/**
	 * Gets a SerialMessage with the ALARM_GET command 
	 * @return the serial message
	 */
	public SerialMessage getMessage(AlarmType alarmType) {
		if(isGetSupported == false) {
			logger.debug("NODE {}: Node doesn't support get requests", this.getNode().getNodeId());
			return null;
		}

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
		GENERAL(0, "General"), 
		SMOKE(1, "Smoke"),
		CARBON_MONOXIDE(2, "Carbon Monoxide"), 
		CARBON_DIOXIDE(3, "Carbon Dioxide"), 
		HEAT(4, "Heat"),
		FLOOD(5, "Flood"),
		ACCESS_CONTROL(6, "Access Control"),
		BURGLAR(7, "Burglar"),
		POWER_MANAGEMENT(8, "Power Management"),
		SYSTEM(9, "System"),
		EMERGENCY(10, "Emergency"),
		COUNT(11, "Count"),
		UNKNOWN12(12, "Unknown12");

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
	 * Class to hold alarm state
	 * @author Chris Jackson
	 */
	@XStreamAlias("alarmState")
	private class Alarm {
		AlarmType alarmType;
		boolean initialised = false;
		
		public Alarm(AlarmType type) {
			alarmType = type;
		}
		
		public AlarmType getAlarmType() {			
			return alarmType;
		}
		
		public void setInitialised() {
			initialised = true;
		}
		
		public boolean getInitialised() {
			return initialised;
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

	@Override
	public Collection<SerialMessage> getDynamicValues(boolean refresh) {
		ArrayList<SerialMessage> result = new ArrayList<SerialMessage>();

		for (Map.Entry<AlarmType, Alarm> entry : this.alarms.entrySet()) {
			if(refresh == true || entry.getValue().getInitialised() == false) {
				result.add(getMessage(entry.getValue().getAlarmType()));
			}
		}

		return result;
	}

	@Override
	public boolean setOptions (ZWaveDbCommandClass options) {
		if(options.isGetSupported != null) {
			isGetSupported = options.isGetSupported;
		}
		
		return true;
	}
}
