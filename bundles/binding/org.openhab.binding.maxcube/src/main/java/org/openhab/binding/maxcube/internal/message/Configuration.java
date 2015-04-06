/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;


/**
* Base class for configuration provided by the MAX!Cube C_Message. 
* 
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public final class Configuration {
	
	private DeviceType deviceType = null;
	private String rfAddress = null;
	private String serialNumber = null;
	private String name = null;
	private int roomId = -1;
	
	/** Comfort temperature in degrees celcius */
	private Float temperatureComfort;
	
	/** Valve position when in Boost-Mode in % */
	private Float boostValvePosition;
	
	/** Date when decalcification will run */
	private Float decalcificationDate;
	
	/** Maximum valve position in % */
	private Float maxValvePosition;
	
	/** Valve offset in % */
	private Float valveOffset;
	
	/** ECO temperature in degrees celcius */
	private Float temperatureEco;
	
	/** Maximum allowed setpoint temperature in degrees celcius */
	private Float temperatureSetpointMax;
	
	/** Minimum allowed setpoint temperature in degrees celcius */
	private Float temperatureSetpointMin;
	
	/** Temperature offset in degrees celcius */
	private Double temperatureOffset;
	
	/** Temperature when window is open in degrees celcius */
	private Float temperatureOpenWindow;
	
	/** Minutes until "open window" mode will automatically be disabled */
	private Float durationOpenWindow;
	
	/** Week and day programs as String */
	private String programData;
	
	/** Duration for Boost-Mode in Minutes */
	private Float boostDuration;
	
	
	
	private Configuration() {
	}
	
	public static Configuration create(Message message) {	
		Configuration configuration = new Configuration();
		configuration.setValues((C_Message) message);
		
		return configuration;
	}
	
	public static Configuration create(DeviceInformation di) {
		Configuration configuration = new Configuration();
		configuration.setValues(di.getRFAddress(), di.getDeviceType(), di.getSerialNumber(), di.getName());
		return configuration;
	}
	

	public void setValues(C_Message message) {
		setValues(message.getRFAddress(), message.getDeviceType(), message.getSerialNumber());

		
		this.temperatureComfort = message.getTempComfort();
		this.boostValvePosition = message.getBoostValve();
		this.decalcificationDate = message.getDecalcification();
		this.maxValvePosition = message.getValveMaximum();
		this.valveOffset = message.getValveOffset();
		this.temperatureEco = message.getTempEco();
		this.temperatureSetpointMax = message.getTempSetpointMax();
		this.temperatureSetpointMin = message.getTempSetpointMin();
		this.temperatureOffset = message.getTempOffset();
		this.temperatureOpenWindow = message.getTempOpenWindow();
		this.durationOpenWindow = message.getDurationOpenWindow();
		this.programData = message.getProgramData();
		this.boostDuration = message.getBoostDuration();
	}
	
	private void setValues(String rfAddress, DeviceType deviceType, String serialNumber, String name) {
		setValues(rfAddress, deviceType, serialNumber);
		this.name = name;
	}
	
	private void setValues(String rfAddress, DeviceType deviceType, String serialNumber) {
		this.rfAddress = rfAddress;
		this.deviceType = deviceType;
		this.serialNumber = serialNumber;
	}
	
	public String getRFAddress() {
		return rfAddress;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public String getSerialNumber() {
		return serialNumber;
	}
	
	public String getName() {
		return name;
	}

	public int getRoomId() {
		return roomId;
	}
	
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public Float getTemperatureComfort() {
		return temperatureComfort;
	}

	public Float getBoostValvePosition() {
		return boostValvePosition;
	}

	public Float getDecalcificationDate() {
		return decalcificationDate;
	}

	public Float getMaxValvePosition() {
		return maxValvePosition;
	}

	public Float getValveOffset() {
		return valveOffset;
	}

	public Float getTemperatureEco() {
		return temperatureEco;
	}

	public Float getTemperatureSetpointMax() {
		return temperatureSetpointMax;
	}

	public Float getTemperatureSetpointMin() {
		return temperatureSetpointMin;
	}

	public Double getTemperatureOffset() {
		return temperatureOffset;
	}

	public Float getTemperatureOpenWindow() {
		return temperatureOpenWindow;
	}

	public Float getDurationOpenWindow() {
		return durationOpenWindow;
	}

	public String getProgramData() {
		return programData;
	}

	public Float getBoostDuration() {
		return boostDuration;
	}
	
	
}
