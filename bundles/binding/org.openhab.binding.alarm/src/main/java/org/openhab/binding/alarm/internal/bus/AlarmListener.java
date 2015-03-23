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
 * @author Volker Daube
 * @since 1.7.0
 */
public interface AlarmListener {

	/**
	 * Notifies about a delayed alarm.
	 * @param itemName the item's name this alarm is for
	 * @param alarmCondition the alarm condition to set
	 */
	public void delayedAlarm(String itemName, AlarmCondition alarmCondition);
	/**
	 * Notifies about a stale alarm.
	 * @param itemName the item's name this alarm is for
	 * @param alarmCondition the alarm condition to set
	 */
	public void staleAlarm(String itemName, AlarmCondition alarmCondition);
	/**
	 * Notifies about a canceled stale alarm.
	 * @param itemName the item's name this alarm is for
	 * @param messageItemName the additional item which should be updated with an empty alamr message
	 */
	public void staleAlarmCanceled(String itemName, String messageItemName);
}
