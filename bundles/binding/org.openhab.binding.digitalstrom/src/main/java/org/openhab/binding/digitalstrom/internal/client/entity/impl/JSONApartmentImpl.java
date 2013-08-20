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
