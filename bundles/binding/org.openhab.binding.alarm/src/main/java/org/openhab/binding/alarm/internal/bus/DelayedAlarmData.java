/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.alarm.internal.bus;

import java.util.concurrent.ScheduledFuture;

import org.openhab.binding.alarm.config.AlarmCondition;

/**
 * This is class holds data about an alarm.
 * 
 * @author Volker Daube
 * @since 1.7.0
 *
 */
public class DelayedAlarmData extends AlarmData {
	private ScheduledFuture<?> mScheduledFuture=null;

	public DelayedAlarmData(String itemName, AlarmCondition alarmCondition, ScheduledFuture<?> scheduledFuture) {
		super(itemName, alarmCondition);
		mScheduledFuture=scheduledFuture;
	}

	/**
	 * Returns the ScheduledFuture for this alarm.
	 * @return the ScheduledFuture for this alarm
	 */
	public ScheduledFuture<?> getScheduledFuture() {
		return mScheduledFuture;
	}
}
