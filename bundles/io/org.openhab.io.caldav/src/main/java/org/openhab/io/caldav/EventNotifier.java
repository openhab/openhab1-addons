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

/**
 * Public event notifier interface. Other bundles can register via this listener.
 *
 * @author Robert Delbrück
 * @since 1.8.0
 */
public interface EventNotifier {
    /**
     * invoked when event was loaded via caldav
     * 
     * @param event the interesting event
     */
    void eventLoaded(CalDavEvent event);

    /**
     * invoked when event was removed from calendar and reloaded from caldav
     * 
     * @param event the interesting event
     */
    void eventRemoved(CalDavEvent event);

    /**
     * invoked when event starts
     * 
     * @param event the interesting event
     */
    void eventBegins(CalDavEvent event);

    /**
     * invoked when event ends
     * 
     * @param event the interesting event
     */
    void eventEnds(CalDavEvent event);

    /**
     * invoked when an calendar is completely reloaded
     * 
     * @param calendarId the calendar, which have been reloaded
     */
    void calendarReloaded(String calendarId);
}
