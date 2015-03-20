/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.alarm.config;

import org.openhab.core.types.AlarmState.AlarmClass;
import org.openhab.core.types.Type;

/**
 * @author Volker Daube
 * @since 1.7.0
 */
public class AlarmCondition {
	private String alarmText;
	private MatchingFunction mMatchingFunction;
	private AlarmClass mAlarmClass;
	private Type mTriggerValue;
	private int mAlarmTimeInSeconds=0;
	private String mMessageItemName;
	/**
	 * @return the alarmText
	 */
	public String getAlarmText() {
		return alarmText;
	}
	/**
	 * @param alarmText the alarmText to set
	 */
	public void setAlarmText(String alarmText) {
		this.alarmText = alarmText;
	}
	/**
	 * @return the matchingFunction
	 */
	public MatchingFunction getMatchingFunction() {
		return mMatchingFunction;
	}
	/**
	 * @param condition the MatchingFunction to set
	 */
	public void setMatchingFunction(MatchingFunction matchingFunction) {
		this.mMatchingFunction = matchingFunction;
	}
	/**
	 * @return the alarmClass
	 */
	public AlarmClass getAlarmClass() {
		return mAlarmClass;
	}
	/**
	 * @param condition the MatchingFunction to set
	 */
	public void setAlarmClass(AlarmClass alarmClass) {
		this.mAlarmClass = alarmClass;
	}
	/**
	 * @return the triggerValue
	 */
	public Type getTriggerValue() {
		return mTriggerValue;
	}
	/**
	 * @param triggerValue the triggerValue to set
	 */
	public void setTriggerValue(Type triggerValue) {
		this.mTriggerValue = triggerValue;
	}
	/**
	 * Get the time in seconds. This is either an alarm delay time
	 * or the stale time
	 * @return the timeInSeconds
	 */
	public int getAlarmTimeInSeconds() {
		return mAlarmTimeInSeconds;
	}
	/**
	 * Sets the time in seconds. This is either an alarm delay time
	 * or the stale time
	 * @param alarmDelayInSeconds the alarmDelayInSeconds to set
	 */
	public void setAlarmTimeInSeconds(int alarmTimeInSeconds) {
		this.mAlarmTimeInSeconds = alarmTimeInSeconds;
	}
	
	/**
	 * @return the messageItemName
	 */
	public String getMessageItemName() {
		return mMessageItemName;
	}
	/**
	 * @param messageItemName the message item name to set
	 */
	public void setMessageItemName(String messageItemName) {
		this.mMessageItemName = messageItemName;
	}
	
	public String toString() {
		return "AlarmCondition ("
				+ "Function=" + mMatchingFunction +", "
				+ "AlarmClass=" + mAlarmClass +", "
				+ "triggerValue="+mTriggerValue + ", "
				+ "text="+alarmText + ", "
				+ "alarm time="+mAlarmTimeInSeconds + ", "
				+ "message item="+mMessageItemName
				+ ") "
				;
	}

	public enum MatchingFunction {
		EQ,NE,LT,LE,GE,GT,STALE;
	}

}
