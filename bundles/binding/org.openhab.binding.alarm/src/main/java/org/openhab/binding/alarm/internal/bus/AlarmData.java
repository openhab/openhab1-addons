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
 * This is class holds data about an alarm.
 * 
 * @author Volker Daube
 * @since 1.7.0
 *
 */
public class AlarmData  {
	private String mItemName=null;
	private AlarmCondition mAlarmCondition=null;

	public AlarmData(String itemName, AlarmCondition alarmCondition) {
		mItemName=itemName;
		mAlarmCondition=alarmCondition;
	}
	/**
	 * @return the item's name
	 */
	public String getItemName() {
		return mItemName;
	}
	/**
	 * @return the mAlarmCondition
	 */
	public AlarmCondition getAlarmCondition() {
		return mAlarmCondition;
	}
}
