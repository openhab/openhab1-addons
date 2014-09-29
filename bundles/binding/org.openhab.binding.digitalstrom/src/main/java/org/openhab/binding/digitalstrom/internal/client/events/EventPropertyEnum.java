/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.client.events;

import java.util.HashMap;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 */
public enum EventPropertyEnum {
	
	ZONEID			("zoneID"),
	SCENEID			("sceneID"),
	ORIGIN_DEVICEID	("originDeviceID"),
	GROUPID			("groupID"),
	GROUP_NAME		("groupName"),
	DSID			("dsid"),
	IS_DEVICE_CALL	("isDevice");
	
	private final String	id;
	
	static final HashMap<String, EventPropertyEnum> eventProperties = new HashMap<String, EventPropertyEnum>();
	
	static {
		for (EventPropertyEnum ev:EventPropertyEnum.values()) {
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
