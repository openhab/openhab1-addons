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

import org.joda.time.DateTime;
import org.openhab.io.caldav.CalDavEvent;

/**
 * A container for a event.
 * Each event can have multiple occurrences.
 *
 * @author Robert Delbrück
 *
 */
public class EventContainer {
    private String eventId;
    private org.joda.time.DateTime lastChanged;
    private boolean historicEvent;
    private DateTime calculatedUntil;

    private List<CalDavEvent> eventList = new ArrayList<CalDavEvent>();
    private final List<String> timerMap = new ArrayList<String>();

    public EventContainer(String eventId, DateTime lastChanged, DateTime calculatedUntil) {
        this.eventId = eventId;
        this.lastChanged = lastChanged;
        this.calculatedUntil = calculatedUntil;
    }

    public List<CalDavEvent> getEventList() {
        return eventList;
    }

    public void setEventList(List<CalDavEvent> eventList) {
        this.eventList = eventList;
    }

    public List<String> getTimerMap() {
        return timerMap;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public org.joda.time.DateTime getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(org.joda.time.DateTime lastChanged) {
        this.lastChanged = lastChanged;
    }

    public boolean isHistoricEvent() {
        return historicEvent;
    }

    public void setHistoricEvent(boolean historicEvent) {
        this.historicEvent = historicEvent;
    }

    public DateTime getCalculatedUntil() {
        return calculatedUntil;
    }

    public void setCalculatedUntil(DateTime calculatedUntil) {
        this.calculatedUntil = calculatedUntil;
    }

}
