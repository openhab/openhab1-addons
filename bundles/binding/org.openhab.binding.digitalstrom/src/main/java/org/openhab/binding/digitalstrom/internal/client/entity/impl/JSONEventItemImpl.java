/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.client.entity.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.openhab.binding.digitalstrom.internal.client.constants.JSONApiResponseKeysEnum;
import org.openhab.binding.digitalstrom.internal.client.entity.EventItem;
import org.openhab.binding.digitalstrom.internal.client.events.EventPropertyEnum;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 */
public class JSONEventItemImpl implements EventItem {
	
	private String	name = null;
	
	private Map<EventPropertyEnum, String> properties = new HashMap<EventPropertyEnum, String>();
	
	public JSONEventItemImpl(JSONObject object) {
		
		name = object.get(JSONApiResponseKeysEnum.EVENT_NAME.getKey()).toString();
		
		if (object.get(JSONApiResponseKeysEnum.EVENT_PROPERTIES.getKey()) instanceof JSONObject ) {
			
			JSONObject  propObj = (JSONObject)object.get(JSONApiResponseKeysEnum.EVENT_PROPERTIES.getKey());
			
			@SuppressWarnings("unchecked")
			Set<String> keys = propObj.keySet();
			
			for (String key: keys) {
				if (EventPropertyEnum.containsId(key)) {
					addProperty(EventPropertyEnum.getProperty(key), propObj.get(key).toString());
				}
			}
			
		}
		
	}
	
	private void addProperty(EventPropertyEnum prop, String value) {
		properties.put(prop, value);
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Map<EventPropertyEnum, String> getProperties() {
		return properties;
	}
	
}