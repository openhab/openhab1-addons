/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rwesmarthome.internal.model;

/**
 * Class to hold a logical device.
 * 
 * @author ollie-dev
 *
 */
public class LogicalDevice {

	private String id;
	private Location location;
	private String type;
	private String name;

    public static final String Type_AlarmActuator = "AlarmActuator";
    public static final String Type_AlarmActuatorState = "AlarmActuatorState";
   	public static final String Type_DaySensor = "DaySensor";
    public static final String Type_DimmerActuator = "DimmerActuator";
    public static final String Type_DimmerActuatorState = "DimmerActuatorState";
	public static final String Type_EmailActuator = "EMailActuator";
    public static final String Type_Generic = "Generic";
	public static final String Type_GenericActuator = "GenericActuator";
	public static final String Type_GenericActuatorState = "GenericDeviceState";
	public static final String Type_GenericDeviceState = "GenericDeviceState";
    public static final String Type_GenericSensor = "GenericSensor";
    public static final String Type_HumiditySensor = "HumiditySensor";
    public static final String Type_HumiditySensorState = "HumiditySensorState";
   	public static final String Type_LuminanceSensor = "LuminanceSensor";
   	public static final String Type_LuminanceSensorState = "LuminanceSensorState";
   	public static final String Type_MotionDetectionSensor = "MotionDetectionSensor";
    public static final String Type_PushButtonSensor = "PushButtonSensor";
    public static final String Type_RoomHumiditySensor = "RoomHumiditySensor";
    public static final String Type_RoomHumiditySensorState = "RoomHumiditySensorState";
    public static final String Type_RoomTemperatureActuator = "RoomTemperatureActuator";
    public static final String Type_RoomTemperatureActuatorState = "RoomTemperatureActuatorState";
    public static final String Type_RoomTemperatureSensor = "RoomTemperatureSensor";
    public static final String Type_RoomTemperatureSensorState = "RoomTemperatureSensorState";
    public static final String Type_RollerShutterActuator = "RollerShutterActuator";
    public static final String Type_RollerShutterActuatorState = "RollerShutterActuatorState";
    public static final String Type_Router = "Router";
	public static final String Type_SmsActuator = "SMSActuator";
    public static final String Type_SwitchActuator = "SwitchActuator";
    public static final String Type_SwitchActuatorState = "SwitchActuatorState";
	public static final String Type_SmokeDetectorSensor = "SmokeDetectorSensor";
	public static final String Type_SmokeDetectionSensorState = "SmokeDetectionSensorState";
	public static final String Type_TemperatureSensor = "TemperatureSensor";
	public static final String Type_ThermostatActuator = "ThermostatActuator";
	public static final String Type_TimerSensor = "TimerSensor";
	public static final String Type_VirtualResidentSensor = "VirtualResidentSensor";
	public static final String Type_WindowDoorSensor = "WindowDoorSensor";
	public static final String Type_WindowDoorSensorState = "WindowDoorSensorState";
	
	// Following are subtypes to differentiate the different GenericActuators
	public static final String Type_GenericActuator_Value = "GenericActuator_Value";
	public static final String Type_GenericActuator_Email = "GenericActuator_Email";
	public static final String Type_GenericActuator_SMS = "GenericActuator_SMS";
	public static final String Type_GenericActuator_SunriseSunset = "GenericActuator_SunriseSunset";
	
	/**
	 * Constructor.
	 * 
	 * @param id
	 * @param location
	 * @param type
	 * @param name
	 */
	public LogicalDevice(String id, Location location, String type, String name) {
		super();
		this.id = id;
		this.location = location;
		this.type = type;
		this.name = name;
	}
	
	/**
	 * Returns the id of the logical device.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Set the id of the logical device.
	 * 
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Returns the location.
	 * 
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Sets the location.
	 * 
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
	/**
	 * Returns the type.
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Sets the type.
	 * 
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Returns the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
