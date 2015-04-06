/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import java.util.Calendar;
import java.util.Date;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;

/**
 * MAX!Cube heating thermostat.
 * 
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public class HeatingThermostat extends Device {
	private ThermostatModeType mode;
	private boolean modeUpdated;
	
	/** Valve position in % */
	private int valvePosition;
	private boolean valvePositionUpdated;

	/** Temperature setpoint in degrees celcius */
	private double temperatureSetpoint;
	private boolean temperatureSetpointUpdated;

	/** Actual Temperature in degrees celcius */
	private double temperatureActual;
	private boolean temperatureActualUpdated;
	
	/** Comfort temperature in degrees celcius */
	private Float temperatureComfort;
	private boolean temperatureComfortUpdated;
	
	/** Valve position when in Boost-Mode in % */
	private Float boostValvePosition;
	private boolean boostValvePositionUpdated;
	
	/** Date when decalcification will run */
	private Float decalcificationDate;
	private boolean decalcificationDateUpdated;
	
	/** Maximum valve position in % */
	private Float maxValvePosition;
	private boolean maxValvePositionUpdated;
	
	/** Valve offset in % */
	private Float valveOffset;
	private boolean valveOffsetUpdated;
	
	/** ECO temperature in degrees celcius */
	private Float temperatureEco;
	private boolean temperatureEcoUpdated;
	
	/** Maximum allowed setpoint temperature in degrees celcius */
	private Float temperatureSetpointMax;
	private boolean temperatureSetpointMaxUpdated;
	
	/** Minimum allowed setpoint temperature in degrees celcius */
	private Float temperatureSetpointMin;
	private boolean temperatureSetpointMinUpdated;
	
	/** Temperature offset in degrees celcius */
	private Double temperatureOffset;
	private boolean temperatureOffsetUpdated;
	
	/** Temperature when window is open in degrees celcius */
	private Float temperatureOpenWindow;
	private boolean temperatureOpenWindowUpdated;
	
	/** Minutes until "open window" mode will automatically be disabled */
	private Float durationOpenWindow;
	private boolean durationOpenWindowUpdated;
	
	/** Week and day programs as String */
	private String programData;
	private boolean programDataUpdated;
	
	/** Duration for Boost-Mode in Minutes */
	private Float boostDuration;
	private boolean boostDurationUpdated;
	
	
	/** Date setpoint until the termperature setpoint is valid */
	private Date dateSetpoint;

	/** Device type for this thermostat **/
	private DeviceType deviceType = DeviceType.HeatingThermostat;


	public HeatingThermostat(Configuration c) {
		super(c);
	}

	@Override
	public DeviceType getType() {
		return deviceType;
	}

	/**
	 * Sets the DeviceType for this thermostat.
	 * @param DeviceType as provided by the C message
	 */
	void setType (DeviceType type) {
		this.deviceType = type;
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Calendar getLastUpdate() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns the current mode of the thermostat.
	 */
	public StringType getModeString() {
		return new StringType (this.mode.toString());
	}

	/**
	 * Returns the current mode of the thermostat.
	 */
	public ThermostatModeType getMode() {
		return (ThermostatModeType) this.mode;
	}

	void setMode(ThermostatModeType mode) {
		if(this.mode != mode) {
			this.modeUpdated = true;
		} else {
			this.modeUpdated = false;
		}
		this.mode = mode;
	}

	/**
	 * Sets the valve position for this thermostat.
	 * @param valvePosition the valve position as provided by the L message
	 */
	public void setValvePosition(int valvePosition) {
		if(this.valvePosition != valvePosition) {
			this.valvePositionUpdated = true;
		} else {
			this.valvePositionUpdated = false;
		}
		this.valvePosition = valvePosition;
	}

	/**
	 * Returns the current valve position  of this thermostat in percent. 
	 *
	 * @return 
	 * 			the valve position as <code>DecimalType</code>
	 */
	public DecimalType getValvePosition() {
		return new DecimalType(this.valvePosition);
	}

	public void setDateSetpoint(Date date) {
		this.dateSetpoint = date;
	}

	/**
	 * Sets the actual temperature for this thermostat. 
	 * @param value the actual temperature raw value as provided by the L message
	 */
	public void setTemperatureActual(double value) {
		if(this.temperatureActual != value) {
			this.temperatureActualUpdated = true;
		} else {
			this.temperatureActualUpdated = false;
		}
		this.temperatureActual = value ;
	}

	/**
	 * Returns the measured temperature  of this thermostat. 
	 * 0�C is displayed if no actual is measured. Temperature is only updated after valve position changes
	 *
	 * @return 
	 * 			the actual temperature as <code>DecimalType</code>
	 */
	public State getTemperatureActual() {
		return new DecimalType(this.temperatureActual);
	}

	/**
	 * Sets the setpoint temperature for this thermostat. 
	 * @param value the setpoint temperature raw value as provided by the L message
	 */
	public void setTemperatureSetpoint(double value) {
		value/=2.0;
		if(this.temperatureSetpoint != value) {
			this.temperatureSetpointUpdated = true;
		} else {
			this.temperatureSetpointUpdated = false;
		}
		this.temperatureSetpoint = value;
	}

	/**
	 * Returns the setpoint temperature  of this thermostat. 
	 * 4.5°C is displayed as OFF, 30.5°C is displayed as On at the thermostat display.
	 *
	 * @return 
	 * 			the setpoint temperature as <code>DecimalType</code>
	 */
	public State getTemperatureSetpoint() {
		return new DecimalType(this.temperatureSetpoint);
	}
	
	
	
	public void setTemperatureComfort(Float value) {
		this.temperatureComfortUpdated = ((value != null) && (!value.equals(this.temperatureComfort)));
		this.temperatureComfort = value;
	}

	public State getTemperatureComfort() {
		return new DecimalType(this.temperatureComfort);
	}
	
	

	public void setBoostValvePosition(Float value) {
		this.boostValvePositionUpdated = ((value != null) && (!value.equals(this.boostValvePosition)));
		this.boostValvePosition = value;
	}

	public State getBoostValvePosition() {
		return new DecimalType(this.boostValvePosition);
	}
	
	

	public void setDecalcificationDate(Float value) {
		this.decalcificationDateUpdated = ((value != null) && (!value.equals(this.decalcificationDate)));
		this.decalcificationDate = value;
	}

	public State getDecalcificationDate() {
		return new DecimalType(this.decalcificationDate);
	}
	
	

	public void setMaxValvePosition(Float value) {
		this.maxValvePositionUpdated = ((value != null) && (!value.equals(this.maxValvePosition)));
		this.maxValvePosition = value;
	}

	public State getMaxValvePosition() {
		return new DecimalType(this.maxValvePosition);
	}
	
	

	public void setValveOffset(Float value) {
		this.valveOffsetUpdated = ((value != null) && (!value.equals(this.valveOffset)));
		this.valveOffset = value;
	}

	public State getValveOffset() {
		return new DecimalType(this.valveOffset);
	}
	
	

	public void setTemperatureEco(Float value) {
		this.temperatureEcoUpdated = ((value != null) && (!value.equals(this.temperatureEco)));
		this.temperatureEco = value;
	}

	public State getTemperatureEco() {
		return new DecimalType(this.temperatureEco);
	}

	
	
	public void setTemperatureSetpointMax(Float value) {
		this.temperatureSetpointMaxUpdated = ((value != null) && (!value.equals(this.temperatureSetpointMax)));
		this.temperatureSetpointMax = value;
	}

	public State getTemperatureSetpointMax() {
		return new DecimalType(this.temperatureSetpointMax);
	}
	
	

	public void setTemperatureSetpointMin(Float value) {
		this.temperatureSetpointMinUpdated = ((value != null) && (!value.equals(this.temperatureSetpointMin)));
		this.temperatureSetpointMin = value;
	}

	public State getTemperatureSetpointMin() {
		return new DecimalType(this.temperatureSetpointMin);
	}
	
	

	public void setTemperatureOffset(Double value) {
		this.temperatureOffsetUpdated = ((value != null) && (!value.equals(this.temperatureOffset)));
		this.temperatureOffset = value;
	}

	public State getTemperatureOffset() {
		return new DecimalType(this.temperatureOffset);
	}
	
	

	public void setTemperatureOpenWindow(Float value) {
		this.temperatureOpenWindowUpdated = ((value != null) && (!value.equals(this.temperatureOpenWindow)));
		this.temperatureOpenWindow = value;
	}

	public State getTemperatureOpenWindow() {
		return new DecimalType(this.temperatureOpenWindow);
	}
	
	

	public void setDurationOpenWindow(Float value) {
		this.durationOpenWindowUpdated = ((value != null) && (!value.equals(this.durationOpenWindow)));
		this.durationOpenWindow = value;
	}

	public State getDurationOpenWindow() {
		return new DecimalType(this.durationOpenWindow);
	}
	
	

	public void setProgramData(String value) {
		this.programDataUpdated = ((value != null) && (!value.equals(this.programData)));
		this.programData = value;
	}

	public State getProgramData() {
		return new StringType (this.programData);
	}
	
	

	public void setBoostDuration(Float value) {
		this.boostDurationUpdated = ((value != null) && (!value.equals(this.boostDuration)));
		this.boostDuration = value;
	}
	
	public State getBoostDuration() {
		return new DecimalType(this.boostDuration);
	}
	
	
	
	public boolean isTemperatureComfortUpdated() {
		return this.temperatureComfortUpdated;
	}

	public boolean isBoostValvePositionUpdated() {
		return this.boostValvePositionUpdated;
	}

	public boolean isDecalcificationDateUpdated() {
		return this.decalcificationDateUpdated;
	}

	public boolean isMaxValvePositionUpdated() {
		return this.maxValvePositionUpdated;
	}

	public boolean isValveOffsetUpdated() {
		return this.valveOffsetUpdated;
	}

	public boolean isTemperatureEcoUpdated() {
		return this.temperatureEcoUpdated;
	}

	public boolean isTemperatureSetpointMaxUpdated() {
		return this.temperatureSetpointMaxUpdated;
	}

	public boolean isTemperatureSetpointMinUpdated() {
		return this.temperatureSetpointMinUpdated;
	}

	public boolean isTemperatureOffsetUpdated() {
		return this.temperatureOffsetUpdated;
	}

	public boolean isTemperatureOpenWindowUpdated() {
		return this.temperatureOpenWindowUpdated;
	}

	public boolean isDurationOpenWindowUpdated() {
		return this.durationOpenWindowUpdated;
	}

	public boolean isProgramDataUpdated() {
		return this.programDataUpdated;
	}

	public boolean isBoostDurationUpdated() {
		return this.boostDurationUpdated;
	}

	
	
	public boolean isModeUpdated() {
		return modeUpdated;
	}

	public boolean isValvePositionUpdated() {
		return valvePositionUpdated;
	}

	public boolean isTemperatureSetpointUpdated() {
		return temperatureSetpointUpdated;
	}

	public boolean isTemperatureActualUpdated() {
		return temperatureActualUpdated;
	}

	
	
	
	
	
}
