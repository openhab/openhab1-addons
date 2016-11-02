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

import org.joda.time.DateTime;

/**
 * Represents an ICS file, which can contain multiple events.
 *
 * @author Robert
 *
 */
public class CalendarFile {
    private ConcurrentHashMap<String, EventContainer> eventContainerMap = new ConcurrentHashMap<>();
    private String filename;
    private String lastGenUID;
    private DateTime lastResourceChange;
    private org.joda.time.DateTime calculatedUntil;

    public CalendarFile(String filename) {
        this.filename = filename;
    }

    public List<EventContainer> getEventContainerList() {
        return new ArrayList<>(this.eventContainerMap.values());
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getLastGenUID() {
        return lastGenUID;
    }

    public void setLastGenUID(String lastGenUID) {
        this.lastGenUID = lastGenUID;
    }

    public DateTime getLastResourceChange() {
        return lastResourceChange;
    }

    public void setLastResourceChange(DateTime lastResourceChange) {
        this.lastResourceChange = lastResourceChange;
    }

    public org.joda.time.DateTime getCalculatedUntil() {
        return calculatedUntil;
    }

    public void setCalculatedUntil(org.joda.time.DateTime calculatedUntil) {
        this.calculatedUntil = calculatedUntil;
    }

    public boolean containsJustHistoricEvents() {
        for (EventContainer eventContainer : this.getEventContainerList()) {
            if (!eventContainer.isHistoricEvent()) {
                return false;
            }
        }
        return true;
    }

    public boolean toBeRead(String digest) {
        if (this.lastGenUID == null) {
            return true;
        }
        if (!this.lastGenUID.equals(digest)) {
            return true;
        }
        return false;
    }

    public EventContainer getEventContainerForId(String eventId) {
        return this.eventContainerMap.get(eventId);
    }

    public void removeEvent(String eventId) {
        this.eventContainerMap.remove(eventId);
    }

    public boolean isEmpty() {
        return this.eventContainerMap.isEmpty();
    }

    public void addEventContainer(EventContainer eventContainer) {
        this.eventContainerMap.put(eventContainer.getEventId(), eventContainer);
    }

    public boolean containsEvent(String eventId) {
        return this.eventContainerMap.containsKey(eventId);
    }
}
