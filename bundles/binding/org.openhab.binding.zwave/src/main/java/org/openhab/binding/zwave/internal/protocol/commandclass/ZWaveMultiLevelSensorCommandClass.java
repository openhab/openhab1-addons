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
 * Handles the Multi Level Sensor command class. Multi level sensors indicate
 * their states as a value + unit. The commands include the possibility to get a
 * given value and report a value.
 * 
 * @author Ben Jones
 * @author Jan-Willem Spuij
 * @since 1.3.0
 */
@XStreamAlias("multiLevelSensorCommandClass")
public class ZWaveMultiLevelSensorCommandClass extends ZWaveCommandClass implements ZWaveGetCommands, 
	ZWaveCommandClassDynamicState, ZWaveCommandClassInitialization {

	private static final Logger logger = LoggerFactory
			.getLogger(ZWaveMultiLevelSensorCommandClass.class);
	private static final int MAX_SUPPORTED_VERSION = 5;
	
	private static final int SENSOR_MULTI_LEVEL_SUPPORTED_GET = 0x01;
	private static final int SENSOR_MULTI_LEVEL_SUPPORTED_REPORT = 0x02;
	private static final int SENSOR_MULTI_LEVEL_GET = 0x04;
	private static final int SENSOR_MULTI_LEVEL_REPORT = 0x05;

	private final Set<SensorType> sensors = new HashSet<SensorType>();

	/**
	 * Creates a new instance of the ZWaveMultiLevelSensorCommandClass class.
	 * 
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveMultiLevelSensorCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.SENSOR_MULTILEVEL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaxVersion() {
		return MAX_SUPPORTED_VERSION;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpoint) {
		logger.trace("Handle Message Sensor Multi Level Request");
		logger.debug("NODE {}: Received Sensor Multi Level Request", this.getNode().getNodeId());
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
		case SENSOR_MULTI_LEVEL_GET:
		case SENSOR_MULTI_LEVEL_SUPPORTED_GET:
			logger.warn(String.format("Command 0x%02X not implemented.",
					command));
			return;
		case SENSOR_MULTI_LEVEL_SUPPORTED_REPORT:
			logger.debug("Process Multi Level Supported Sensor Report");
			
			int payloadLength = serialMessage.getMessagePayload().length;
			
			for(int i = offset + 1; i < payloadLength; ++i ) {
				for(int bit = 0; bit < 8; ++bit) {
					    if( ((serialMessage.getMessagePayloadByte(i)) & (1 << bit) ) == 0 )
					    	continue;
					    
					    int index = ((i - (offset + 1)) * 8 ) + bit + 1;             
					    if(index >= SensorType.values().length)
					    	continue;
					    
					    // (n)th bit is set. n is the index for the alarm type enumeration.
						SensorType sensorTypeToAdd = SensorType.getSensorType(index);
						this.sensors.add(sensorTypeToAdd);
						logger.debug(String.format("NODE %d: Added sensor type %s (0x%02x)", this.getNode().getNodeId(), sensorTypeToAdd.getLabel(), index));
				}
			}
			
			this.getNode().advanceNodeStage(NodeStage.DYNAMIC);
			break;
		case SENSOR_MULTI_LEVEL_REPORT:
			logger.trace("Process Multi Level Sensor Report");
			logger.debug("NODE {}: Sensor Multi Level report received", this.getNode().getNodeId());

			int sensorTypeCode = serialMessage.getMessagePayloadByte(offset + 1);
			logger.debug(String.format("NODE %d: Sensor Type = (0x%02x)", this.getNode().getNodeId(), sensorTypeCode));

			SensorType sensorType = SensorType.getSensorType(sensorTypeCode);
			
			if (sensorType == null) {
				logger.error(String.format("NODE %d: Unknown Sensor Type = 0x%02x, ignoring report.", this.getNode().getNodeId(), sensorTypeCode));
				return;
			}
			
			// sensor type seems to be supported, add it to the list.
			if (!sensors.contains(sensorType))
				this.sensors.add(sensorType);

			BigDecimal value = extractValue(serialMessage.getMessagePayload(), offset + 2);

			logger.debug(String.format("NODE %d: Sensor Value = (%f)", this.getNode().getNodeId(), value));
			
			ZWaveMultiLevelSensorValueEvent zEvent = new ZWaveMultiLevelSensorValueEvent(this.getNode().getNodeId(), endpoint, sensorType, value);
			this.getController().notifyEventListeners(zEvent);
			
			if (this.getNode().getNodeStage() != NodeStage.DONE)
				this.getNode().advanceNodeStage(NodeStage.DONE);
			break;
		default:
			logger.warn(String
					.format("Unsupported Command 0x%02X for command class %s (0x%02X).",
							command, this.getCommandClass().getLabel(), this
									.getCommandClass().getKey()));
		}
	}

	/**
	 * Gets a SerialMessage with the SENSOR_MULTI_LEVEL_GET command
	 * 
	 * @return the serial message
	 */
	public SerialMessage getValueMessage() {
		
		if (this.getVersion() > 4) {
			for (SensorType sensorType : this.sensors)
				return this.getMessage(sensorType);
		}
		
		logger.debug(
				"NODE {}: Creating new message for application command SENSOR_MULTI_LEVEL_GET",
				this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(),
				SerialMessageClass.SendData, SerialMessageType.Request,
				SerialMessageClass.ApplicationCommandHandler,
				SerialMessagePriority.Get);
		byte[] newPayload = { (byte) this.getNode().getNodeId(), 
				2,
				(byte) getCommandClass().getKey(),
				(byte) SENSOR_MULTI_LEVEL_GET };
		result.setMessagePayload(newPayload);
		return result;
	}
	
	/**
	 * Gets a SerialMessage with the SENSOR_MULTI_LEVEL_SUPPORTED_GET command
	 * 
	 * @return the serial message
	 */
	public SerialMessage getSupportedValueMessage() {
		logger.debug(
				"NODE {}: Creating new message for application command SENSOR_MULTI_LEVEL_SUPPORTED_GET",
				this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(),
				SerialMessageClass.SendData, SerialMessageType.Request,
				SerialMessageClass.ApplicationCommandHandler,
				SerialMessagePriority.High);
		byte[] newPayload = { (byte) this.getNode().getNodeId(), 
				2,
				(byte) getCommandClass().getKey(),
				(byte) SENSOR_MULTI_LEVEL_SUPPORTED_GET };
		result.setMessagePayload(newPayload);
		return result;
	}
	
	/**
	 * Gets a SerialMessage with the SENSOR_MULTI_LEVEL_GET command
	 * 
	 * @param sensorType the {@link SensorType} to get the value for.
	 * @return the serial message
	 */
	public SerialMessage getMessage(SensorType sensorType) {
		logger.debug(
				"NODE {}: Creating new message for application command SENSOR_MULTI_LEVEL_GET",
				this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(),
				SerialMessageClass.SendData, SerialMessageType.Request,
				SerialMessageClass.ApplicationCommandHandler,
				SerialMessagePriority.Get);
		byte[] newPayload = { (byte) this.getNode().getNodeId(), 
				3,
				(byte) getCommandClass().getKey(),
				(byte) SENSOR_MULTI_LEVEL_GET,
				(byte) sensorType.getKey()
				};
		result.setMessagePayload(newPayload);
		return result;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public Collection<SerialMessage> initialize() {
		ArrayList<SerialMessage> result = new ArrayList<SerialMessage>();
		
		if (this.getVersion() > 4)
			result.add(getSupportedValueMessage());
		
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<SerialMessage> getDynamicValues() {
		ArrayList<SerialMessage> result = new ArrayList<SerialMessage>();
		
		if (this.getVersion() > 4) {
			for (SensorType sensorType : this.sensors)
				result.add(this.getMessage(sensorType));
		} else {
			result.add(this.getValueMessage());
		}
		
		return result;
	}
	
	
	/**
	 * Z-Wave SensorType enumeration. The sensor type indicates the type
	 * of sensor that is reported.
	 * @author Jan-Willem Spuij
	 * @since 1.3.0
	 */
	@XStreamAlias("sensorType")
	public enum SensorType
	{
		TEMPERATURE(1,"Temperature"),
		GENERAL(2,"General"),
		LUMINANCE(3,"Luminance"),
		POWER(4,"Power"),
		RELATIVE_HUMIDITY(5,"RelativeHumidity"),
		VELOCITY(6,"Velocity"),
		DIRECTION(7,"Direction"),
		ATMOSPHERIC_PRESSURE(8,"AtmosphericPressure"),
		BAROMETRIC_PRESSURE(9,"BarometricPressure"),
		SOLAR_RADIATION(10,"SolarRadiation"),
		DEW_POINT(11,"DewPoint"),
		RAIN_RATE(12,"RainRate"),
		TIDE_LEVEL(13,"TideLevel"),
		WEIGHT(14,"Weight"),
		VOLTAGE(15,"Voltage"),
		CURRENT(16,"Current"),
		CO2(17,"CO2"),
		AIR_FLOW(18,"AirFlow"),
		TANK_CAPACITY(19,"TankCapacity"),
		DISTANCE(20,"Distance"),
		ANGLE_POSITION(21,"AnglePosition"),
		ROTATION(22,"Rotation"),
		WATER_TEMPERATURE(23,"WaterTemperature"),
		SOIL_TEMPERATURE(24,"SoilTemperature"),
		SEISMIC_INTENSITY(25,"SeismicIntensity"),
		SEISMIC_MAGNITUDE(26,"SeismicMagnitude"),
		ULTRAVIOLET(27,"Ultraviolet"),
		ELECTRICAL_RESISTIVITY(28,"ElectricalResistivity"),
		ELECTRICAL_CONDUCTIVITY(29,"ElectricalConductivity"),
		LOUDNESS(30,"Loudness"),
		MOISTURE(31,"Moisture"),
		MAX_TYPE(32,"MaxType");

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
	 * Z-Wave Multilevel Sensor Event class. Indicates that an sensor value
	 * changed. 
	 * @author Jan-Willem Spuij
	 * @since 1.4.0
	 */
	public class ZWaveMultiLevelSensorValueEvent extends ZWaveCommandClassValueEvent {

		private SensorType sensorType;
		
		/**
		 * Constructor. Creates a instance of the ZWaveMultiLevelSensorValueEvent class.
		 * @param nodeId the nodeId of the event
		 * @param endpoint the endpoint of the event.
		 * @param sensorType the sensor type that triggered the event;
		 * @param value the value for the event.
		 */
		private ZWaveMultiLevelSensorValueEvent(int nodeId, int endpoint,
				SensorType sensorType, Object value) {
			super(nodeId, endpoint, CommandClass.SENSOR_MULTILEVEL, value);
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
