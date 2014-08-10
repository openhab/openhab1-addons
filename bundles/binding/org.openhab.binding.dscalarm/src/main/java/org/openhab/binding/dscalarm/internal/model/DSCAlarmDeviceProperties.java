/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dscalarm.internal.model;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for DSC Alarm device properties;
 * @author Russell Stephens
 * @since 1.6.0
 */
public class DSCAlarmDeviceProperties {
	private static final Logger logger = LoggerFactory.getLogger(DSCAlarmDeviceProperties.class);

	private int systemConnection = 0;
	private String systemConnectionDescription = "";
	private String systemMessage = "";
	private Date systemTimeDate;
	private int systemError = 0;
	private int systemErrorCode = 0;
	private String systemErrorDescription = "No Error";
	
	private int panelId = 0;
	private int partitionId = 0;
	private int zoneId = 0;
	private int keypadId = 0;
	
	private int generalState = 0;
	private String generalStateDescription = "";
	private int armState = 0; /* partition:0=disarmed, 1=away armed, 2=stay armed, 3=zero entry delay, 4=with user code; zone:0=Armed, 1=Bypassed*/
	private String armStateDescription = "";
	private int alarmState = 0;
	private String alarmStateDescription = "";
	private int tamperState = 0;
	private String tamperStateDescription = "";
	private int faultState = 0;
	private String faultStateDescription = "";

	private String ledStates[] = {"Off","On","Flashing"};
	private int readyLEDState = 0; /* 0=Off, 1=On, 2=Flashing*/
	private String readyLEDStateDescription = "Off";
	private int armedLEDState = 0; /* 0=Off, 1=On, 2=Flashing*/
	private String armedLEDStateDescription = "Off";
	private int memoryLEDState = 0; /* 0=Off, 1=On, 2=Flashing*/
	private String memoryLEDStateDescription = "Off";
	private int bypassLEDState = 0; /* 0=Off, 1=On, 2=Flashing*/
	private String bypassLEDStateDescription = "Off";
	private int troubleLEDState = 0; /* 0=Off, 1=On, 2=Flashing*/
	private String troubleLEDStateDescription = "Off";
	private int programLEDState = 0; /* 0=Off, 1=On, 2=Flashing*/
	private String programLEDStateDescription = "Off";
	private int fireLEDState = 0; /* 0=Off, 1=On, 2=Flashing*/
	private String fireLEDStateDescription = "Off";
	private int backlightLEDState = 0; /* 0=Off, 1=On, 2=Flashing*/
	private String backlightLEDStateDescription = "Off";
	private int acLEDState = 0; /* 0=Off, 1=On, 2=Flashing*/
	private String acLEDStateDescription = "Off";
	
	enum StateType{
		CONNECTION_STATE,
		TIME_DATE,
		GENERAL_STATE,
		ARM_STATE,
		ALARM_STATE,
		TAMPER_STATE,
		FAULT_STATE;
	}
	
	enum LEDStateType{
		READY_LED_STATE,
		ARMED_LED_STATE,
		MEMORY_LED_STATE,
		BYPASS_LED_STATE,
		TROUBLE_LED_STATE,
		PROGRAM_LED_STATE,
		FIRE_LED_STATE,
		BACKLIGHT_LED_STATE,
		AC_LED_STATE;
	}

	public int getSystemConnection() {
		return systemConnection;
	}

	public String getSystemConnectionDescription() {
		return systemConnectionDescription;
	}

	public String getSystemMessage() {
		return systemMessage;
	}
	
	public String getTimeDate() {
		SimpleDateFormat tm = new SimpleDateFormat("HH:MM MM/DD/YY");
		return tm.format(systemTimeDate);
	}

	public int getSystemError() {
		return systemError;
	}
	
	public int getSystemErrorCode() {
		return systemErrorCode;
	}

	public String getSystemErrorDescription() {
		return systemErrorDescription;
	}

	public int getPanelId() {
		return panelId;
	}

	public int getPartitionId() {
		return partitionId;
	}

	public int getZoneId() {
		return zoneId;
	}

	public int getKeypadId() {
		return keypadId;
	}

	public int getState(StateType stateType) {
		int state = 0;
		
		switch(stateType) {
			case CONNECTION_STATE:
				state = systemConnection;
				break;
			case GENERAL_STATE:
				state = generalState;
				break;
			case ARM_STATE:
				state = armState;
				break;
			case ALARM_STATE:
				state = alarmState;
				break;
			case TAMPER_STATE:
				state = tamperState;
				break;
			case FAULT_STATE:
				state = faultState;
				break;
			default:
				break;
		}
		
		return state;
	}

	public String getStateDescription(StateType stateType) {
		String stateDescription = "";
		
		switch(stateType) {
			case CONNECTION_STATE:
				stateDescription = systemConnectionDescription;
				break;
			case GENERAL_STATE:
				stateDescription = generalStateDescription;
				break;
			case ARM_STATE:
				stateDescription = armStateDescription;
				break;
			case ALARM_STATE:
				stateDescription = alarmStateDescription;
				break;
			case TAMPER_STATE:
				stateDescription = tamperStateDescription;
				break;
			case FAULT_STATE:
				stateDescription = faultStateDescription;
				break;
			default:
				break;
		}
		
		return stateDescription;
	}

	public int getLEDState(LEDStateType stateType) {
		int state = 0;
		
		switch(stateType) {
			case READY_LED_STATE:
				state = readyLEDState;
				break;
			case ARMED_LED_STATE:
				state = armedLEDState;
				break;
			case MEMORY_LED_STATE:
				state = memoryLEDState;
				break;
			case BYPASS_LED_STATE:
				state = bypassLEDState;
				break;
			case TROUBLE_LED_STATE:
				state = troubleLEDState;
				break;
			case PROGRAM_LED_STATE:
				state = programLEDState;
				break;
			case FIRE_LED_STATE:
				state = fireLEDState;
				break;
			case BACKLIGHT_LED_STATE:
				state = backlightLEDState;
				break;
			case AC_LED_STATE:
				state = acLEDState;
				break;
			default:
				break;
		}
	
	return state;

	}
	
	public String getLEDStateDescription(LEDStateType stateType) {
		String stateDescription = "Off";
		
		switch(stateType) {
			case READY_LED_STATE:
				stateDescription = readyLEDStateDescription;
				break;
			case ARMED_LED_STATE:
				stateDescription = armedLEDStateDescription;
				break;
			case MEMORY_LED_STATE:
				stateDescription = memoryLEDStateDescription;
				break;
			case BYPASS_LED_STATE:
				stateDescription = bypassLEDStateDescription;
				break;
			case TROUBLE_LED_STATE:
				stateDescription = troubleLEDStateDescription;
				break;
			case PROGRAM_LED_STATE:
				stateDescription = programLEDStateDescription;
				break;
			case FIRE_LED_STATE:
				stateDescription = fireLEDStateDescription;
				break;
			case BACKLIGHT_LED_STATE:
				stateDescription = backlightLEDStateDescription;
				break;
			case AC_LED_STATE:
				stateDescription = acLEDStateDescription;
				break;
			default:
				break;
		}
	
	return stateDescription;

	}

	public void setSystemConnection(int systemConnection) {
		this.systemConnection = systemConnection;
	}

	public void setSystemMessage(String systemMessage) {
		this.systemMessage = systemMessage;
	}

	public void setTimeDate(String timeDate) {
		SimpleDateFormat tm = new SimpleDateFormat("HH:MM MM/DD/YY");
		try {
			systemTimeDate = tm.parse(timeDate);
		} catch (ParseException parseException) {
			logger.error("setTimeDate(): parse error {} ", parseException);
		}
	}

	public void setSystemError(int systemError) {
		this.systemError = systemError;
	}

	public void setSystemErrorCode(int systemErrorCode) {
		this.systemErrorCode = systemErrorCode;
	}

	public void setSystemErrorDescription(String systemErrorDescription) {
		this.systemErrorDescription = systemErrorDescription;
	}

	public void setPanelId(int panelId) {
		this.panelId = panelId;
	}

	public void setPartitionId(int partitionId) {
		this.partitionId = partitionId;
	}
	
	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public void setKeypadId(int keypadId) {
		this.keypadId = keypadId;
	}

	public void setState(StateType stateType, int state, String stateDescription) {
		
		switch(stateType) {
			case CONNECTION_STATE:
				systemConnection = state;
				systemConnectionDescription = stateDescription;
				break;
			case GENERAL_STATE:
				generalState = state;
				generalStateDescription = stateDescription;
				break;
			case ARM_STATE:
				armState = state;
				armStateDescription = stateDescription;
				break;
			case ALARM_STATE:
				alarmState = state;
				alarmStateDescription = stateDescription;
				break;
			case TAMPER_STATE:
				tamperState = state;
				tamperStateDescription = stateDescription;
				break;
			case FAULT_STATE:
				faultState = state;
				faultStateDescription = stateDescription;
				break;
			default:
				break;
		}
	}
	
	public void setStateDescription(StateType stateType, String stateDescription) {
		
		switch(stateType) {
			case CONNECTION_STATE:
				systemConnectionDescription = stateDescription;
				break;
			case GENERAL_STATE:
				generalStateDescription = stateDescription;
				break;
			case ARM_STATE:
				armStateDescription = stateDescription;
				break;
			case ALARM_STATE:
				alarmStateDescription = stateDescription;
				break;
			case TAMPER_STATE:
				tamperStateDescription = stateDescription;
				break;
			case FAULT_STATE:
				faultStateDescription = stateDescription;
				break;
			default:
				break;
		}
	}
	
	public void setLEDState(LEDStateType stateType, int state) {
		
		switch(stateType) {
			case READY_LED_STATE:
				readyLEDState = state;
				readyLEDStateDescription = ledStates[state];
				break;
			case ARMED_LED_STATE:
				armedLEDState = state;
				armedLEDStateDescription = ledStates[state];
				break;
			case MEMORY_LED_STATE:
				memoryLEDState = state;
				memoryLEDStateDescription = ledStates[state];
				break;
			case BYPASS_LED_STATE:
				bypassLEDState = state;
				bypassLEDStateDescription = ledStates[state];
				break;
			case TROUBLE_LED_STATE:
				troubleLEDState = state;
				troubleLEDStateDescription = ledStates[state];
				break;
			case PROGRAM_LED_STATE:
				programLEDState = state;
				programLEDStateDescription = ledStates[state];
				break;
			case FIRE_LED_STATE:
				fireLEDState = state;
				fireLEDStateDescription = ledStates[state];
				break;
			case BACKLIGHT_LED_STATE:
				backlightLEDState = state;
				backlightLEDStateDescription = ledStates[state];
				break;
			case AC_LED_STATE:
				acLEDState = state;
				acLEDStateDescription = ledStates[state];
				break;
			default:
				break;
		}	
	}
}
