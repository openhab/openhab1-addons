/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.caldav;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A calendar event
 * 
 * @author Robert Delbrück
 * @since 1.7.0
 */
public class CalDavEvent {
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy/HH:mm");
	
	private String name;
	private Date start;
	private Date end;
	private Date lastChanged;
	private String id;
	private String calendarId;
	private String location;
	private String content;
	
	public CalDavEvent() {
		super();
	}
	
	public CalDavEvent(String name, String id, String calendarId, Date start, Date end) {
		super();
		
		this.name = name;
		this.id = id;
		this.calendarId = calendarId;
		this.start = start;
		this.end = end;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getStart() {
		return start;
	}
	
	public void setStart(Date start) {
		this.start = start;
	}
	
	public Date getEnd() {
		return end;
	}
	
	public void setEnd(Date end) {
		this.end = end;
	}

	public String getId() {
		return this.calendarId + "-" + this.id + "-" + this.start.hashCode() + "-" + this.end.hashCode();
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCalendarId() {
		return calendarId;
	}

	public void setCalendarId(String calendarId) {
		this.calendarId = calendarId;
	}

	public Date getLastChanged() {
		return lastChanged;
	}

	public void setLastChanged(Date lastChanged) {
		this.lastChanged = lastChanged;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getShortName() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.id);
		sb.append("(");
		sb.append(this.name);
		sb.append("@");
		sb.append(SDF.format(this.start));
		sb.append("-");
		sb.append(SDF.format(this.end));
		sb.append(")");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((calendarId == null) ? 0 : calendarId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CalDavEvent other = (CalDavEvent) obj;
		if (calendarId == null) {
			if (other.calendarId != null)
				return false;
		} else if (!calendarId.equals(other.calendarId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
