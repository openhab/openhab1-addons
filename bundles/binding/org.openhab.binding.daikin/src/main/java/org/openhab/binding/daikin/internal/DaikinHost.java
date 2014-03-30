/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.daikin.internal;

import java.math.BigDecimal;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * Class which represents a Daikin online controller connection 
 * and current state.
 *  
 * @author Ben Jones
 * @since 1.5.0
 */
public class DaikinHost {
	private final String id;
	
	private String host = null;
	private String username = null;
	private String password = null;

	private boolean power = false;
	private String mode = "None";
	private BigDecimal temp = BigDecimal.ZERO;
	private String fan = "None";
	private String swing = "None";
	private BigDecimal tempIn = BigDecimal.ZERO;
	private String timer = "None";
	private BigDecimal tempOut = BigDecimal.ZERO;
	private BigDecimal humidityIn = BigDecimal.ZERO;
	
	public DaikinHost(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getPower() {
		return power;
	}

	public void setPower(boolean power) {
		this.power = power;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public BigDecimal getTemp() {
		return temp;
	}

	public void setTemp(BigDecimal temp) {
		this.temp = temp;
	}

	public String getFan() {
		return fan;
	}

	public void setFan(String fan) {
		this.fan = fan;
	}

	public String getSwing() {
		return swing;
	}

	public void setSwing(String swing) {
		this.swing = swing;
	}

	public BigDecimal getTempIn() {
		return tempIn;
	}

	public void setTempIn(BigDecimal tempIn) {
		this.tempIn = tempIn;
	}

	public String getTimer() {
		return timer;
	}

	public void setTimer(String timer) {
		this.timer = timer;
	}

	public BigDecimal getTempOut() {
		return tempOut;
	}

	public void setTempOut(BigDecimal tempOut) {
		this.tempOut = tempOut;
	}

	public BigDecimal getHumidityIn() {
		return humidityIn;
	}

	public void setHumidityIn(BigDecimal humidityIn) {
		this.humidityIn = humidityIn;
	}
	
	public State getState(DaikinCommandType commandType) {
		switch (commandType) {
			case POWER:
				return getPower() ? OnOffType.ON : OnOffType.OFF;
			case MODE:
				return new StringType(getMode());
			case TEMP:
				return new DecimalType(getTemp());
			case FAN:
				return new StringType(getFan());
			case SWING:
				return new StringType(getSwing());
			case TEMPIN:
				return new DecimalType(getTempIn());
			case TIMER:
				return new StringType(getTimer());
			case TEMPOUT:
				return new DecimalType(getTempOut());
			case HUMIDITYIN:
				return new DecimalType(getHumidityIn());
				
			default:
				throw new RuntimeException("Unsupported command type: " + commandType);
		}
	}
	
	public void setState(DaikinCommandType commandType, Command value) {
		switch (commandType) {
			case POWER:
				setPower(value.equals(OnOffType.ON));
				break;
			case MODE:
				setMode(value.toString());
				break;
			case TEMP:
				setTemp(new BigDecimal(((DecimalType)value).doubleValue()));
				break;
			case FAN:
				setFan(value.toString());
				break;
			case SWING:
				setSwing(value.toString());
				break;
			case TEMPIN:
				setTempIn(new BigDecimal(((DecimalType)value).doubleValue()));
				break;
			case TIMER:
				setTimer(value.toString());
				break;
			case TEMPOUT:
				setTempOut(new BigDecimal(((DecimalType)value).doubleValue()));
				break;
			case HUMIDITYIN:
				setHumidityIn(new BigDecimal(((DecimalType)value).doubleValue()));
				break;
				
			default:
				throw new RuntimeException("Unsupported command type: " + commandType);
		}		
	}
}
