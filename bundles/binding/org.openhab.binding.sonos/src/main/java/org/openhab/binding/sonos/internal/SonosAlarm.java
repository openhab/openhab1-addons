/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sonos.internal;

import org.joda.time.DateTime;
import org.joda.time.Period;

/**
 * 
 * SonosAlarm represents Alarms (as in Alarm Clock). Sonos devices have a alarm clock functionality that can 
 * be used to set alarms (e.g. wake up,...) in a multi-room fashion
 * 
 * @author Karel Goderis 
 * @since 1.1.0
 * 
 */
public class SonosAlarm implements Cloneable {
	
	public Object clone() {
		try
		{
			return super.clone();
		}
		catch(Exception e){ return null; }
	}

	
	public int getID() {
		return ID;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}


	public Period getDuration() {
		return duration;
	}

	public String getRecurrence() {
		return recurrence;
	}

	public boolean getEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public String getRoomUUID() {
		return roomUUID;
	}

	public String getProgramURI() {
		return programURI;
	}

	public String getProgramMetaData() {
		return programMetaData;
	}

	public String getPlayMode() {
		return playMode;
	}

	public int getVolume() {
		return volume;
	}

	public boolean getIncludeLinkedZones() {
		return includeLinkedZones;
	}

	private final int ID;
	private DateTime startTime;
	private final Period duration;
	private final String recurrence;
	private boolean enabled;
	private final String roomUUID;
	private final String programURI;
	private final String programMetaData;
	private final String playMode;
	private final int volume;
	private final boolean includeLinkedZones;
	
	public SonosAlarm(int ID, DateTime startTime, Period duration, String recurrence,
			boolean enabled, String roomUUID, String programURI, String
		programMetaData, String playMode, int volume, boolean includeLinkedZones) {
		this.ID = ID;
		this.startTime = startTime;
		this.duration = duration;
		this.recurrence = recurrence;
		this.enabled = enabled;
		this.roomUUID = roomUUID;
		this.programURI = programURI;
		this.programMetaData = programMetaData;
		this.playMode = playMode;
		this.volume = volume;
		this.includeLinkedZones = includeLinkedZones;
	}
	
	@Override
	public String toString() {
		return "SonosAlarm [ID=" + ID + ", start=" + startTime +", duration="+duration+", enabled="+enabled+", UUID="+roomUUID+"]";
	}
}
