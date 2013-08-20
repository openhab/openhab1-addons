/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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