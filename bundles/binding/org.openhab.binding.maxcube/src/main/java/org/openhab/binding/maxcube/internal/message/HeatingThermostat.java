/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import java.text.DecimalFormat;
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

	/** Valve position in % */
	private int valvePosition;

	/** Temperature setpoint in degrees celcius */
	private double temperatureSetpoint;

	/** Actual Temperature in degrees celcius */
	private double temperatureActual;

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
		this.mode = mode;
	}

	/**
	 * Sets the valve position for this thermostat.
	 * @param valvePosition the valve position as provided by the L message
	 */
	public void setValvePosition(int valvePosition) {
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
	public void setTemperatureSetpoint(int value) {
		this.temperatureSetpoint = value / 2.0;
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
}
