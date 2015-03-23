/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.alarm.internal.bus;

import org.openhab.binding.alarm.config.AlarmCondition;

/**
 * This is class holds data about a stale alarm.
 * 
 * @author Volker Daube
 * @since 1.7.0
 *
 */
public class StaleAlarmData extends AlarmData {
	private long mLastUpdateInMs=0l;
	private boolean mStaleAlarmed=false;

	public StaleAlarmData(String itemName, AlarmCondition alarmCondition) {
		super(itemName, alarmCondition);
	}

	public void setStaleAlarmed(boolean staleAlarmed) {
		mStaleAlarmed=staleAlarmed;
	}
	public boolean isStaleAlarmed() {
		return mStaleAlarmed;
	}
	/**
	 * Returns the time (@link Date) when the item was last updated (touched)
	 * @return the last update time in milliseconds 
	 */
	public long getLastUpdateInMs() {
		return mLastUpdateInMs;
	}
	/**
	 * Sets the time when the item was last updated in milliseconds
	 * @param lastUpdateInMs time in milliseconds
	 */
	public void setLastUpdateInMs(long lastUpdateInMs) {
		mLastUpdateInMs=lastUpdateInMs;
	}
	
	/**
	 * Return the time period in which an update is required or an alarm
	 * shall be issued.
	 * @return the time period in which an update is required 
	 */
	public int getStaleTime() {
		return getAlarmCondition().getAlarmTimeInSeconds();
	}
}
