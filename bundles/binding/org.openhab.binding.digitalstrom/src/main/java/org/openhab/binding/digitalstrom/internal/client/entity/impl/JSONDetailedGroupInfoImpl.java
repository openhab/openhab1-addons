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

import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openhab.binding.digitalstrom.internal.client.constants.JSONApiResponseKeysEnum;
import org.openhab.binding.digitalstrom.internal.client.entity.DetailedGroupInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 */
public class JSONDetailedGroupInfoImpl implements DetailedGroupInfo {
	
	private static final Logger logger = LoggerFactory.getLogger(JSONDetailedGroupInfoImpl.class);
	
	private String	name = null;
	private short	groupId	= 0;
	
	private List<String> deviceList = null;

	public JSONDetailedGroupInfoImpl(JSONObject jObject) {
		this.deviceList = new LinkedList<String>();
		
		if (jObject.get(JSONApiResponseKeysEnum.GROUP_NAME.getKey()) != null) {
			name = jObject.get(JSONApiResponseKeysEnum.GROUP_NAME.getKey()).toString();
		}
		
		if (jObject.get(JSONApiResponseKeysEnum.GROUP_ID.getKey()) != null) {
			try {
				this.groupId = Short.parseShort(jObject.get(JSONApiResponseKeysEnum.GROUP_ID.getKey()).toString());
			}
			catch (java.lang.NumberFormatException e) {
				logger.error("NumberFormatException by parsing groupID: "+ jObject.get(JSONApiResponseKeysEnum.GROUP_ID.getKey()).toString());
			}
		}
		
		if (jObject.get(JSONApiResponseKeysEnum.GROUP_DEVICES.getKey()) instanceof JSONArray ) {
			JSONArray array = (JSONArray) jObject.get(JSONApiResponseKeysEnum.GROUP_DEVICES.getKey());
			
			for (int i=0; i< array.size(); i++) {
				if (array.get(i) != null ) {
					deviceList.add(array.get(i).toString());
				}
			}
		}
		
	}
	
	@Override
	public short getGroupID() {
		return groupId;
	}

	@Override
	public String getGroupName() {
		return name;
	}

	@Override
	public List<String> getDeviceList() {
		return deviceList;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DetailedGroupInfo) {
			DetailedGroupInfo group = (DetailedGroupInfo)obj;
			return group.getGroupID() == this.getGroupID();
		}
		return false;
	}

}
