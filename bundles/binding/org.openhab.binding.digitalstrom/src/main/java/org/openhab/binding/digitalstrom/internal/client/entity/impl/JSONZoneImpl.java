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
import org.openhab.binding.digitalstrom.internal.client.entity.Device;
import org.openhab.binding.digitalstrom.internal.client.entity.Zone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 */
public class JSONZoneImpl implements Zone {
	
	private static final Logger logger = LoggerFactory.getLogger(JSONZoneImpl.class);
	
	private int zoneId = 0;
	private String name = null;
	
	private List<DetailedGroupInfo> groupList = null;
	private List<Device> deviceList	= null;
	
	public JSONZoneImpl(JSONObject object) {
		this.groupList = new LinkedList<DetailedGroupInfo>();
		this.deviceList = new LinkedList<Device>();
		
		if (object.get(JSONApiResponseKeysEnum.APARTMENT_GET_STRUCTURE_ZONES_NAME.getKey()) != null) {
			this.name = object.get(JSONApiResponseKeysEnum.APARTMENT_GET_STRUCTURE_ZONES_NAME.getKey()).toString();//getValue(object, DigitalSTROMJSONApiResultKeysEnum.APARTMENT_GET_STRUCTURE_ZONES_NAME.getKey());
		}
		
		String zoneIdStr = null;
		if (object.get(JSONApiResponseKeysEnum.APARTMENT_GET_STRUCTURE_ZONES_ID.getKey()) != null) {
			zoneIdStr = object.get(JSONApiResponseKeysEnum.APARTMENT_GET_STRUCTURE_ZONES_ID.getKey()).toString();
		}
		if (zoneIdStr == null) {
			if (object.get(JSONApiResponseKeysEnum.QUERY_ZONE_ID.getKey()) != null) {
				zoneIdStr = object.get(JSONApiResponseKeysEnum.QUERY_ZONE_ID.getKey()).toString();
			}
		}
		
		if (zoneIdStr != null) {
			try {
				this.zoneId = Integer.parseInt(zoneIdStr);
			} catch (java.lang.NumberFormatException e) {
				logger.error("NumberFormatException by getting zoneID: "+zoneIdStr);
			}
		}
		
		if (object.get(JSONApiResponseKeysEnum.APARTMENT_GET_STRUCTURE_ZONES_DEVICES.getKey()) instanceof org.json.simple.JSONArray) {
			JSONArray list = (JSONArray)object.get(JSONApiResponseKeysEnum.APARTMENT_GET_STRUCTURE_ZONES_DEVICES.getKey());
			for (int i=0; i< list.size(); i++) {
				this.deviceList.add(new JSONDeviceImpl((JSONObject)list.get(i)));
			}
		}
		
		if (object.get(JSONApiResponseKeysEnum.APARTMENT_GET_STRUCTURE_ZONES_GROUPS.getKey()) instanceof org.json.simple.JSONArray) {
			JSONArray groupList = (JSONArray)object.get(JSONApiResponseKeysEnum.APARTMENT_GET_STRUCTURE_ZONES_GROUPS.getKey());
			for (int i=0; i< groupList.size(); i++) {
				this.groupList.add(new JSONDetailedGroupInfoImpl((JSONObject)groupList.get(i)));
			}
		}
		
	}
	
	@Override
	public int getZoneId() {
		return zoneId;
	}
	
	
	@Override
	public synchronized void setZoneId(int id) {
		if (id > 0) {
			this.zoneId = id;
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public synchronized void setName(String name) {
		this.name = name;
	}

	@Override
	public List<DetailedGroupInfo> getGroups() {
		return groupList;
	}

	@Override
	public void addGroup(DetailedGroupInfo group) {
		if (group != null) {
			synchronized(groupList) {
				if (!groupList.contains(group)) {
					groupList.add(group);
				}
			}
		}
	}

	@Override
	public List<Device> getDevices() {
		return deviceList;
	}

	@Override
	public void addDevice(Device device) {
		if (device != null) {
			synchronized(deviceList) {
				if (!deviceList.contains(device)) {
					deviceList.add(device);
				}
			}
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Zone) {
			Zone other = (Zone)obj;
			return (other.getZoneId() == this.getZoneId());
		}
		return false;
	}
	
}
