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
import java.util.Map;

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
 * Handles the Binary Sensor command class. Binary sensors indicate there
 * status or event as on (0xFF) or off (0x00).
 * The commands include the possibility to get a given
 * value and report a value.
 * @author Jan-Willem Spuij
 * @since 1.3.0
 */
@XStreamAlias("binarySensorCommandClass")
public class ZWaveBinarySensorCommandClass extends ZWaveCommandClass implements ZWaveGetCommands, ZWaveCommandClassDynamicState {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveBinarySensorCommandClass.class);

	private static final int MAX_SUPPORTED_VERSION = 2;

	private static final int SENSOR_BINARY_GET = 0x02;
	private static final int SENSOR_BINARY_REPORT = 0x03;
	
	/**
	 * Creates a new instance of the ZWaveBinarySensorCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveBinarySensorCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.SENSOR_BINARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpoint) {
		logger.trace("Handle Message Sensor Binary Request");
		logger.debug("NODE {}: Received Sensor Binary Request (v{})", this.getNode().getNodeId(), this.getVersion());
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
			case SENSOR_BINARY_GET:
				logger.warn(String.format("NODE %d: Command 0x%02X not implemented.", this.getNode().getNodeId(), command));
				return;
			case SENSOR_BINARY_REPORT:
				logger.trace("Process Sensor Binary Report");
				int value = serialMessage.getMessagePayloadByte(offset + 1);

				SensorType sensorType = SensorType.UNKNOWN;
				if(this.getVersion() > 1 && serialMessage.getMessagePayload().length > offset + 2) {
					logger.debug("Processing Sensor Type {}", serialMessage.getMessagePayloadByte(offset + 2));
					// For V2, we have the sensor type after the value
					sensorType = SensorType.getSensorType(serialMessage.getMessagePayloadByte(offset + 2));
					logger.debug("Sensor Type is {}", sensorType);
					if(sensorType == null)
						sensorType = SensorType.UNKNOWN;
				}

				logger.debug(String.format("NODE %d: Sensor Binary report, type=%s, value=0x%02X", this.getNode().getNodeId(), sensorType.getLabel(), value));

				ZWaveBinarySensorValueEvent zEvent = new ZWaveBinarySensorValueEvent(this.getNode().getNodeId(), endpoint, sensorType, value);
				this.getController().notifyEventListeners(zEvent);
				
				if (this.getNode().getNodeStage() != NodeStage.DONE)
					this.getNode().advanceNodeStage(NodeStage.DONE);
				break;
			default:
			logger.warn(String.format("NODE %d: Unsupported Command 0x%02X for command class %s (0x%02X).", 
					this.getNode().getNodeId(),
					command, 
					this.getCommandClass().getLabel(),
					this.getCommandClass().getKey()));
		}
	}

	/**
	 * Gets a SerialMessage with the SENSOR_BINARY_GET command 
	 * @return the serial message
	 */
	public SerialMessage getValueMessage() {
		logger.debug("NODE {}: Creating new message for application command SENSOR_BINARY_GET", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							2, 
								(byte) getCommandClass().getKey(), 
								(byte) SENSOR_BINARY_GET };
    	
    	// Should there be another byte here to specify the sensor type?
    	// Looking at the RaZberry doc, it talks about requesting the sensor type
    	// and using FF for the first sensor.
    	// Maybe this is a V2 feature - need to find some docs on V2!
    	
    	
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaxVersion() {
		return MAX_SUPPORTED_VERSION;
	}
	
	/**
	 * Z-Wave SensorType enumeration. The sensor type indicates the type
	 * of sensor that is reported.
	 * @author Chris Jackson
	 * @since 1.5.0
	 */
	@XStreamAlias("sensorType")
	public enum SensorType {
		UNKNOWN(0x00, "Unknown"),
		GENERAL(0x01, "General Purpose"),
		SMOKE(0x02, "Smoke"),
		CO(0x03, "Carbon Monoxide"),
		CO2(0x04, "Carbon Dioxide"),
		HEAT(0x05, "Heat"),
		WATER(0x06, "Water"),
		FREEZE(0x07, "Freeze"),
		TAMPER(0x08,"Tamper"),
		AUX(0x09, "Aux"),
		DOORWINDOW(0x0a,"Door/Window"),
		TILT(0x0b, "Tilt"),
		MOTION(0x0c,"Motion"),
		GLASSBREAK(0x0d, "Glass Break");


		/**
		 * A mapping between the integer code and its corresponding Sensor type
		 * to facilitate lookup by code.
		 */
		private static Map<Integer, SensorType> codeToSensorTypeMapping;

		private int key;
		private String label;

		private SensorType(int key, String label) {
			this.key = key;
			this.label = label;
		}

		private static void initMapping() {
			codeToSensorTypeMapping = new HashMap<Integer, SensorType>();
			for (SensorType s : values()) {
				codeToSensorTypeMapping.put(s.key, s);
			}
		}

		/**
		 * Lookup function based on the sensor type code.
		 * Returns null if the code does not exist.
		 * @param i the code to lookup
		 * @return enumeration value of the sensor type.
		 */
		public static SensorType getSensorType(int i) {
			if (codeToSensorTypeMapping == null) {
				initMapping();
			}
			
			return codeToSensorTypeMapping.get(i);
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
	 * Z-Wave Binary Sensor Event class. Indicates that an sensor value changed. 
	 * @author Chris Jackson
	 * @since 1.5.0
	 */
	public class ZWaveBinarySensorValueEvent extends ZWaveCommandClassValueEvent {

		private SensorType sensorType;
		
		/**
		 * Constructor. Creates a instance of the ZWaveBinarySensorValueEvent class.
		 * @param nodeId the nodeId of the event
		 * @param endpoint the endpoint of the event.
		 * @param sensorType the sensor type that triggered the event;
		 * @param value the value for the event.
		 */
		private ZWaveBinarySensorValueEvent(int nodeId, int endpoint,
				SensorType sensorType, Object value) {
			super(nodeId, endpoint, CommandClass.SENSOR_BINARY, value);
			this.sensorType = sensorType;
		}

		/**
		 * Gets the alarm type for this alarm sensor value event.
		 */
		public SensorType getSensorType() {
			return sensorType;
		}
	}
}
