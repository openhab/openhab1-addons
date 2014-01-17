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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openhab.binding.digitalstrom.internal.client.constants.JSONApiResponseKeysEnum;
import org.openhab.binding.digitalstrom.internal.client.entity.Apartment;
import org.openhab.binding.digitalstrom.internal.client.entity.Zone;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 */
public class JSONApartmentImpl implements Apartment{
	
	private Map<Integer, Zone> zoneMap = new HashMap<Integer, Zone>();
	
	public JSONApartmentImpl(JSONObject jObject) {
		if (jObject.get(JSONApiResponseKeysEnum.APARTMENT_GET_STRUCTURE_ZONES.getKey()) instanceof JSONArray) {
			JSONArray zones = (JSONArray) jObject.get(JSONApiResponseKeysEnum.APARTMENT_GET_STRUCTURE_ZONES.getKey());
			
			for (int i=0; i< zones.size(); i++) {
				if (zones.get(i) instanceof org.json.simple.JSONObject) {
					Zone zone = new JSONZoneImpl((JSONObject)zones.get(i));
					zoneMap.put(zone.getZoneId(), zone);
				}
			}
		}
	}

	@Override
	public Map<Integer, Zone> getZoneMap() {
		return zoneMap;
	}

}
