/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
 * Class for DSC Alarm device properties
 * 
 * @author Russell Stephens
 * @since 1.6.0
 */
public class DSCAlarmDeviceProperties {
	private static final Logger logger = LoggerFactory.getLogger(DSCAlarmDeviceProperties.class);

	private int systemConnection = 0;
	private String systemConnectionDescription = "";
	private String systemMessage = "";
	private Date systemTime = new Date(0);
	private boolean systemTimeStamp = false;
	private boolean systemTimeBroadcast = false;
	private int systemCommand = -1;
	private int systemError = 0;
	private int systemErrorCode = 0;
	private String systemErrorDescription = "No Error";
	
	private int panelId = 0;
	private int partitionId = 0;
	private int zoneId = 0;
	private int keypadId = 0;
	
	private int generalState = 0;
	private String generalStateDescription = "";
	private int armState = 0; /* partition:0=disarmed, 1=away armed, 2=stay armed, 3=away no delay, 4=stay no delay, 5=with user code; zone:0=Armed, 1=Bypassed*/
	private String armStateDescription = "";
	private int alarmState = 0;
	private String alarmStateDescription = "";
	private int tamperState = 0;
	private String tamperStateDescription = "";
	private int faultState = 0;
	private String faultStateDescription = "";
	private int openingClosingState = 0; /* 0=None, 1=User Closing, 2=Special Closing, 3=Partial Closing, 4=User Opening, 5=Special Opening */
	private String openingClosingStateDescription = "";
	/* Bitwise representation of a zones state:
	   bit0=General State (0-Closed, 1-Open),
	   bit1=Arm State (0-Armed, 1-Bypassed)
	   bit2=Alarm State (0-No Alarm, 1-Alarm)
	   bit3=Tamper State (0-No Tamper, 1-Tamper)
	   bit4=Fault State (0-No Fault, 1-Fault) */
	private int zoneBitState = 0;
	
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

	private String troubleMessage = "";
	private boolean troubleLED = false;
	private boolean serviceRequired = false;
	private boolean acTrouble = false;
	private boolean telephoneLineTrouble = false;
	private boolean failureToCommunicate = false;
	private boolean zoneFault = false;
	private boolean zoneTamper = false;
	private boolean zoneLowBattery = false;
	private boolean lossOfTime = false;
		
	private boolean fireKeyAlarm = false;
	private boolean panicKeyAlarm = false;
	private boolean auxKeyAlarm = false;
	private boolean auxInputAlarm = false;
	private boolean armed = false;
	private boolean armedStay = false;
	private boolean armedAway = false;
	private boolean entryDelay = false;
	private boolean exitDelay = false;
	private boolean alarmed = false;
	private boolean tampered = false;
	private boolean faulted = false;
	private boolean tripped = false;
	
	enum StateType{
		CONNECTION_STATE,
		GENERAL_STATE,
		ARM_STATE,
		ALARM_STATE,
		TAMPER_STATE,
		FAULT_STATE,
		OPENING_CLOSING_STATE;
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

	enum TroubleType{
		SERVICE_REQUIRED,
		AC_TROUBLE,
		TELEPHONE_LINE_TROUBLE,
		FAILURE_TO_COMMUNICATE,
		ZONE_FAULT,
		ZONE_TAMPER,
		ZONE_LOW_BATTERY,
		LOSS_OF_TIME;
	}
	
	enum TriggerType{
		FIRE_KEY_ALARM,
		PANIC_KEY_ALARM,
		AUX_KEY_ALARM,
		AUX_INPUT_ALARM,
		ARMED,
		ARMED_STAY,
		ARMED_AWAY,
		ENTRY_DELAY,
		EXIT_DELAY,
		ALARMED,
		TAMPERED,
		FAULTED,
		TRIPPED;
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
	
	public String getSystemTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		
		return sdf.format(systemTime);
	}

	public boolean getSystemTimeStamp() {
		return systemTimeStamp;
	}

	public boolean getSystemTimeBroadcast() {
		return systemTimeBroadcast;
	}

	public int getSystemCommand() {
		return systemCommand;
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
			case OPENING_CLOSING_STATE:
				state = openingClosingState;
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
			case OPENING_CLOSING_STATE:
				stateDescription = openingClosingStateDescription;
				break;
			default:
				break;
		}
		
		return stateDescription;
	}
	
	public int getZoneBitState() {
		return zoneBitState;
	}
	
	public boolean getZoneBitState(int bit) {
		int bitState;
		boolean state = false;
		
		if(bit >= 0 && bit <= 7) {
			bitState = (this.zoneBitState >> bit) & 1;
			if(bitState == 1)
				state = true;
		}
		
		return state;
	}
	
	public boolean getZoneBitState(StateType stateType) {
		int bit = -1;
		
		switch(stateType) {
			case GENERAL_STATE:
				bit = 0;
				break;
			case ARM_STATE:
				bit = 1;
				break;
			case ALARM_STATE:
				bit = 2;
				break;
			case TAMPER_STATE:
				bit = 3;
				break;
			case FAULT_STATE:
				bit = 4;
				break;
			default:
				break;
		}
		
		return getZoneBitState(bit);
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
	
	public String getTroubleMessage() {
		return troubleMessage;
	}

	public boolean getTroubleLED() {
		return troubleLED;
	}

	public boolean getTrouble(TroubleType troubleType) {
		boolean trouble = false;
		
		switch(troubleType) {
			case SERVICE_REQUIRED:
				trouble = serviceRequired;
				break;
			case AC_TROUBLE:
				trouble = acTrouble;
				break;
			case TELEPHONE_LINE_TROUBLE:
				trouble = telephoneLineTrouble;
				break;
			case FAILURE_TO_COMMUNICATE:
				trouble = failureToCommunicate;
				break;
			case ZONE_FAULT:
				trouble = zoneFault;
				break;
			case ZONE_TAMPER:
				trouble = zoneTamper;
				break;
			case ZONE_LOW_BATTERY:
				trouble = zoneLowBattery;
				break;
			case LOSS_OF_TIME:
				trouble = lossOfTime;
				break;
			default:
				break;
		}
		
		return trouble;
		
	}
	
	public boolean getTrigger(TriggerType triggerType) {
		boolean trigger = false;
		
		switch(triggerType) {
			case FIRE_KEY_ALARM:
				trigger = fireKeyAlarm;
				break;
			case PANIC_KEY_ALARM:
				trigger = panicKeyAlarm;
				break;
			case AUX_KEY_ALARM:
				trigger = auxKeyAlarm;
				break;
			case AUX_INPUT_ALARM:
				trigger = auxInputAlarm;
				break;
			case ARMED:
				trigger = armed;
				break;
			case ARMED_STAY:
				trigger = armedStay;
				break;
			case ARMED_AWAY:
				trigger = armedAway;
				break;
			case ENTRY_DELAY:
				trigger = entryDelay;
				break;
			case EXIT_DELAY:
				trigger = exitDelay;
				break;
			case ALARMED:
				trigger = alarmed;
				break;
			case TAMPERED:
				trigger = tampered;
				break;
			case FAULTED:
				trigger = faulted;
				break;
			case TRIPPED:
				trigger = tripped;
				break;
			default:
				break;
		}
		
		return trigger;
	}

	public void setSystemConnection(int systemConnection) {
		this.systemConnection = systemConnection;
	}

	public void setSystemMessage(String systemMessage) {
		this.systemMessage = systemMessage;
	}

	public void setSystemTime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmMMddyy");
		Date date = null;
		
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			logger.error("setTimeDate(): Parse Exception occured while trying parse date string - {}. ",e);
		}
		
		this.systemTime = date;
	}

	public void setSystemTimeStamp(boolean systemTimeStamp) {
		this.systemTimeStamp = systemTimeStamp;
	}

	public void setSystemTimeBroadcast(boolean systemTimeBroadcast) {
		this.systemTimeBroadcast = systemTimeBroadcast;
	}

	public void setSystemError(int systemError) {
		this.systemError = systemError;
	}

	public void setSystemErrorCode(int systemErrorCode) {
		this.systemErrorCode = systemErrorCode;
	}

	public void setSystemCommand(int systemCommand) {
		this.systemCommand = systemCommand;
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
			case OPENING_CLOSING_STATE:
				openingClosingState = state;
				openingClosingStateDescription = stateDescription;
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
			case OPENING_CLOSING_STATE:
				openingClosingStateDescription = stateDescription;
				break;
			default:
				break;
		}
	}

	public void clearZoneBitState() {
		zoneBitState = 0;
	}
	
	public void setZoneBitState(int bit, boolean set) {
		
		if(bit >= 0 && bit <= 7) {
			if(set) {
				zoneBitState |= 1 << bit;
			}
			else {
				zoneBitState &= ~(1 << bit);
				
			}
		}
	}

	public void setZoneBitState(StateType stateType, boolean set) {
		int bit = -1;
		
		switch(stateType) {
			case GENERAL_STATE:
				bit = 0;
				break;
			case ARM_STATE:
				bit = 1;
				break;
			case ALARM_STATE:
				bit = 2;
				break;
			case TAMPER_STATE:
				bit = 3;
				break;
			case FAULT_STATE:
				bit = 4;
				break;
			default:
				break;
		}
		
		setZoneBitState(bit, set);
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

	public void setTroubleMessage(String troubleCondition) {
		this.troubleMessage = troubleCondition;
	}

	public void setTroubleLED(boolean troubleLED) {
		this.troubleLED = troubleLED;
	}

	public void setTrouble(TroubleType troubleType, boolean trouble) {
		
		switch(troubleType) {
			case SERVICE_REQUIRED:
				serviceRequired = trouble;
				break;
			case AC_TROUBLE:
				acTrouble = trouble;
				break;
			case TELEPHONE_LINE_TROUBLE:
				telephoneLineTrouble = trouble;
				break;
			case FAILURE_TO_COMMUNICATE:
				failureToCommunicate = trouble;
				break;
			case ZONE_FAULT:
				zoneFault = trouble;
				break;
			case ZONE_TAMPER:
				zoneTamper = trouble;
				break;
			case ZONE_LOW_BATTERY:
				zoneLowBattery = trouble;
				break;
			case LOSS_OF_TIME:
				lossOfTime = trouble;
				break;
			default:
				break;
		}		
	}
	
	public void setTrigger(TriggerType triggerType, boolean trigger) {
		
		switch(triggerType) {
			case FIRE_KEY_ALARM:
				fireKeyAlarm = trigger;
				break;
			case PANIC_KEY_ALARM:
				panicKeyAlarm = trigger;
				break;
			case AUX_KEY_ALARM:
				auxKeyAlarm = trigger;
				break;
			case AUX_INPUT_ALARM:
				auxInputAlarm = trigger;
				break;
			case ARMED:
				armed = trigger;
				break;
			case ARMED_STAY:
				armedStay = trigger;
				break;
			case ARMED_AWAY:
				armedAway = trigger;
				break;
			case ENTRY_DELAY:
				entryDelay = trigger;
				break;
			case EXIT_DELAY:
				exitDelay = trigger;
				break;
			case ALARMED:
				alarmed = trigger;
				break;
			case TAMPERED:
				tampered = trigger;
				break;
			case FAULTED:
				faulted = trigger;
				break;
			case TRIPPED:
				tripped = trigger;
				break;
			default:
				break;
		}
	}
	
	public static void main (String[] arg) {
		DSCAlarmDeviceProperties prop = new DSCAlarmDeviceProperties();
		int bitState = prop.getZoneBitState();
		boolean state = false;
		System.out.println(bitState + " " + state);
		
		prop.setZoneBitState(StateType.GENERAL_STATE, true);
		bitState = prop.getZoneBitState();
		state = prop.getZoneBitState(StateType.GENERAL_STATE);
		System.out.println("General State: " + bitState + " " + state);
		prop.setZoneBitState(StateType.GENERAL_STATE, false);
		bitState = prop.getZoneBitState();
		state = prop.getZoneBitState(StateType.GENERAL_STATE);
		System.out.println("General State: " + bitState + " " + state);
		
		prop.setZoneBitState(StateType.ARM_STATE, true);
		bitState = prop.getZoneBitState();
		state = prop.getZoneBitState(StateType.ARM_STATE);
		System.out.println("Arm State: " + bitState + " " + state);
		prop.setZoneBitState(StateType.ARM_STATE, false);
		bitState = prop.getZoneBitState();
		state = prop.getZoneBitState(StateType.ARM_STATE);
		System.out.println("Arm State: " + bitState + " " + state);

		prop.setZoneBitState(StateType.ALARM_STATE, true);
		bitState = prop.getZoneBitState();
		state = prop.getZoneBitState(StateType.ALARM_STATE);
		System.out.println("Alarm State: " + bitState + " " + state);
		prop.setZoneBitState(StateType.ALARM_STATE, false);
		bitState = prop.getZoneBitState();
		state = prop.getZoneBitState(StateType.ALARM_STATE);
		System.out.println("Alarm State: " + bitState + " " + state);

		prop.setZoneBitState(StateType.TAMPER_STATE, true);
		bitState = prop.getZoneBitState();
		state = prop.getZoneBitState(StateType.TAMPER_STATE);
		System.out.println("Tamper State: " + bitState + " " + state);
		prop.setZoneBitState(StateType.TAMPER_STATE, false);
		bitState = prop.getZoneBitState();
		state = prop.getZoneBitState(StateType.ALARM_STATE);
		System.out.println("Tamper State: " + bitState + " " + state);

		prop.setZoneBitState(StateType.FAULT_STATE, true);
		bitState = prop.getZoneBitState();
		state = prop.getZoneBitState(StateType.FAULT_STATE);
		System.out.println("Fault State: " + bitState + " " + state);
		prop.setZoneBitState(StateType.FAULT_STATE, false);
		bitState = prop.getZoneBitState();
		state = prop.getZoneBitState(StateType.FAULT_STATE);
		System.out.println("Fault State: " + bitState + " " + state);
		
		prop.setZoneBitState(7, true);
		bitState = prop.getZoneBitState();
		state = prop.getZoneBitState(7);
		System.out.println("Seventh Bit: " + bitState + " " + state);
		prop.setZoneBitState(7, false);
		bitState = prop.getZoneBitState();
		state = prop.getZoneBitState(7);
		System.out.println("Seventh Bit: " + bitState + " " + state);
	}
}
