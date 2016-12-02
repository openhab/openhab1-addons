/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.caldav.internal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.openhab.io.caldav.internal.CalDavConfig;

/**
 * Containing all events for a specific calendar and the config for the calendar.
 *
 * @author Robert Delbrück
 * @since 1.9.0
 *
 */
public class CalendarRuntime {
    private final ConcurrentHashMap<String, CalendarFile> calendarFileMap = new ConcurrentHashMap<String, CalendarFile>();

    private CalDavConfig config;

    public CalendarFile getCalendarFileByFilename(String filename) {
        return calendarFileMap.get(filename);
    }

    public List<CalendarFile> getCalendarFiles() {
        return new ArrayList<>(this.calendarFileMap.values());
    }

    public CalDavConfig getConfig() {
        return config;
    }

    public void setConfig(CalDavConfig config) {
        this.config = config;
    }

    public void removeCalendarFile(String filename) {
        this.calendarFileMap.remove(filename);
    }

    public void addCalendarFile(CalendarFile calendarFile) {
        this.calendarFileMap.put(calendarFile.getFilename(), calendarFile);
    }

}
