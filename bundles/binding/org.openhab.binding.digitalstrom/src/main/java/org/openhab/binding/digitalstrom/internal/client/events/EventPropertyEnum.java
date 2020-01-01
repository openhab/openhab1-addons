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
package org.openhab.binding.digitalstrom.internal.client.events;

import java.util.HashMap;

/**
 * @author Alexander Betker
 * @since 1.3.0
 */
public enum EventPropertyEnum {

    ZONEID("zoneID"),
    SCENEID("sceneID"),
    ORIGIN_DEVICEID("originDeviceID"),
    GROUPID("groupID"),
    GROUP_NAME("groupName"),
    DSID("dsid"),
    IS_DEVICE_CALL("isDevice");

    private final String id;

    static final HashMap<String, EventPropertyEnum> eventProperties = new HashMap<String, EventPropertyEnum>();

    static {
        for (EventPropertyEnum ev : EventPropertyEnum.values()) {
            eventProperties.put(ev.getId(), ev);
        }
    }

    public static boolean containsId(String property) {
        return eventProperties.keySet().contains(property);
    }

    public static EventPropertyEnum getProperty(String property) {
        return eventProperties.get(property);
    }

    private EventPropertyEnum(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
