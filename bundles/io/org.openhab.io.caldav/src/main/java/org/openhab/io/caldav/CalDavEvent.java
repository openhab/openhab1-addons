/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.io.caldav;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * A calendar event
 *
 * @author Robert Delbrück
 * @since 1.8.0
 */
public class CalDavEvent {
    private static final DateTimeFormatter SDF = DateTimeFormat.forPattern("dd.MM.yyyy/HH:mm");

    private String name;
    private DateTime start;
    private DateTime end;
    private DateTime lastChanged;
    private String id;
    private String calendarId;
    private String location;
    private String content;
    private String filename;
    private final List<String> categoryList = new ArrayList<String>();

    public CalDavEvent() {
        super();
    }

    public CalDavEvent(String name, String id, String calendarId, DateTime start, DateTime end) {
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

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public DateTime getEnd() {
        return end;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }

    // public String getId() {
    // return this.id;
    // }

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

    public DateTime getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(DateTime lastChanged) {
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    public String getShortName() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.id);
        sb.append("(");
        sb.append(this.name);
        sb.append("@");
        sb.append(SDF.print(this.start));
        sb.append("-");
        sb.append(SDF.print(this.end));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((calendarId == null) ? 0 : calendarId.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CalDavEvent other = (CalDavEvent) obj;
        if (calendarId == null) {
            if (other.calendarId != null) {
                return false;
            }
        } else if (!calendarId.equals(other.calendarId)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        // supposing that the "lastChanged" timestamp has correctly been set in case of event modification for something
        // like categories, place ... )
        if (lastChanged == null) {
            if (other.lastChanged != null) {
                return false;
            }
        } else if (!lastChanged.equals(other.lastChanged)) {
            return false;
        }

        // we NEED to compare event's start and stop date to be able to say they are equal :
        if (start == null) {
            if (other.start != null) {
                return false;
            }
        } else if (!start.equals(other.start)) {
            return false;
        }
        if (end == null) {
            if (other.end != null) {
                return false;
            }
        } else if (!end.equals(other.end)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return this.getShortName();
    }

}
