/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
