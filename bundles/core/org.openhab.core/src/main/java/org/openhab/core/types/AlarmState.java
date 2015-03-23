/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.types;

import java.util.Calendar;
import java.util.Date;

/** 
 * This is a class representing an alarm.
 * An alarm has typically a message a
 * 
 * @author Volker Daube
 * @since 1.7.0
 *
 */
public class AlarmState {
	private String mAlarmMessage;
	private AlarmClass mAlarmClass;
	private long mAlarmTimeUTC;

	/**
	 * Creates a new AlarmState with specified alarm message text using
	 * defaults for alarm class (HIGH) and the current time as alarm time
	 * @param alarmMessage the alarm's message text
     * @see #AlarmClass
	 */
	public AlarmState(String alarmMessage) {
		this(alarmMessage, AlarmClass.HIGH);
	}

	/**
	 * Creates a new AlarmState with specifies alarm message text 
	 * and alarm class using the current time as alarm time
	 * @param alarmMessage the alarm's message text
	 * @param alarmClass the alarm's class
     * @see #AlarmClass
	 */
	public AlarmState(String alarmMessage, AlarmClass alarmClass) {
		mAlarmMessage=alarmMessage;
		mAlarmClass=alarmClass;
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		mAlarmTimeUTC=cal.getTimeInMillis();
	}

	/**
	 * Creates a new AlarmState with specifies alarm message text, 
	 * alarm class and alarm time in as UTC milliseconds from the epoch
	 * @param alarmMessage the alarm's message text
	 * @param alarmClass the alarm's class
	 * @param alarmTime
     * @see #AlarmClass
	 */
	public AlarmState(String alarmMessage, AlarmClass alarmClass, long alarmTimeUTC) {
		mAlarmClass=alarmClass;
		mAlarmTimeUTC=alarmTimeUTC;
		mAlarmMessage=alarmMessage;
	}
	/**
	 * Get the alarm's class 
	 * @return the alarmClass the alarm's class
     * @see #AlarmClass
	 */
	public AlarmClass getAlarmClass() {
		return mAlarmClass;
	}
	/** 
	 * Get the alarm's class 
	 * @param alarmClass the alarm's class to set
     * @see #AlarmClass
	 */
	public void setAlarmClass(AlarmClass alarmClass) {
		this.mAlarmClass = alarmClass;
	}
	/**
	 * Gets the time when the alarm was triggered as UTC milliseconds from the epoch
	 * @return the alarm time as UTC milliseconds from the epoch
	 */
	public long getAlarmTimeUTC() {
		return mAlarmTimeUTC;
	}
	/**
	 * Sets the time when the alarm was triggered as UTC milliseconds from the epoch
	 * @param alarmTime the alarmTime as UTC milliseconds from the epoch to set
	 */
	public void setAlarmTimeUTC(long alarmTimeUTC) {
		this.mAlarmTimeUTC = alarmTimeUTC;
	}
	/**
	 * Gets the alarm's message text
	 * @return the alarm message text
	 */
	public String getAlarmMessage() {
		return mAlarmMessage;
	}
	/**
	 * Sets the alarm's message text
	 * @param alarmMessage the alarm's message text to set
	 */
	public void setAlarmMessage(String alarmMessage) {
		this.mAlarmMessage = alarmMessage;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AlarmState (" +
				(mAlarmClass != null ? "AlarmClass=" + mAlarmClass + ", " : "") +
				"Alarm Message=" + mAlarmMessage + ", " +
				"Alarm time=" + mAlarmTimeUTC +
				")";
	}

	/**
	 * Enum with alarm classes.
	 * @author Volker Daube
	 *
	 */
	public enum AlarmClass {
		LOW, MEDIUM, HIGH;
	}	
}
