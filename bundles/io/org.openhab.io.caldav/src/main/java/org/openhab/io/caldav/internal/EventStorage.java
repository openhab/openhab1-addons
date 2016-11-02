/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.caldav.internal;

import java.util.concurrent.ConcurrentHashMap;

import org.openhab.io.caldav.internal.model.CalendarRuntime;

public final class EventStorage {
    private static EventStorage instance;

    public static EventStorage getInstance() {
        if (instance == null) {
            instance = new EventStorage();
        }
        return instance;
    }

    private EventStorage() {
    }

    private ConcurrentHashMap<String, CalendarRuntime> eventCache = new ConcurrentHashMap<String, CalendarRuntime>();

    public ConcurrentHashMap<String, CalendarRuntime> getEventCache() {
        return eventCache;
    }

}
