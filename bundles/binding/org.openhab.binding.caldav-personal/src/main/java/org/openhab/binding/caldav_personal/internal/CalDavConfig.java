/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.caldav_personal.internal;

import java.util.List;

import org.openhab.core.binding.BindingConfig;

/**
 * Configuration class
 * 
 * @author Robert Delbrück
 * @since 1.8.0
 *
 */
public class CalDavConfig implements BindingConfig {
	public static enum Type {
		PRESENCE, EVENT, ACTIVE, UPCOMING
	}
	
	public static enum Value {
		NAME, DESCRIPTION, PLACE, START, END, TIME, NAMEANDTIME
	}
	
	private final List<String> calendar;
	private final Type type;
	private final int eventNr;
	private final Value value;
	
	public CalDavConfig(List<String> calendar, Type type, int eventNr,
			Value value) {
		this.calendar = calendar;
		this.type = type;
		this.eventNr = eventNr;
		this.value = value;
	}

	public List<String> getCalendar() {
		return calendar;
	}

	public Type getType() {
		return type;
	}

	public int getEventNr() {
		return eventNr;
	}

	public Value getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "CalDavPresenceConfig [calendar=" + calendar + ", type=" + type
				+ ", eventNr=" + eventNr + ", value=" + value + "]";
	}

	
}
