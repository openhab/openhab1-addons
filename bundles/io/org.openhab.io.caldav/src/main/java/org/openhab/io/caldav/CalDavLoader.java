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

import java.util.List;

/**
 * Cal DAV loader interface
 *
 * @author Robert Delbrück
 * @since 1.8.0
 */
public interface CalDavLoader {
    /**
     * add listener to notify for event changes
     * 
     * @param notifier the event listener
     */
    public void addListener(EventNotifier notifier);

    /**
     * remove listener to notify for event changes
     * 
     * @param notifier the event listener
     */
    public void removeListener(EventNotifier notifier);

    /**
     * get events for a specific calendar
     * 
     * @param calendarId
     * @return
     */
    public List<CalDavEvent> getEvents(CalDavQuery query);

    /**
     * add a new event to the calendar
     * 
     * @param calDavEvent
     */
    public void addEvent(CalDavEvent event);
}
