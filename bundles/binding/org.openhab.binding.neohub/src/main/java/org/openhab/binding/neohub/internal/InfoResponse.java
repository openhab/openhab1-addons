/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.neohub.internal;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * @author Sebastian Prehn
 * @since 1.5.0
 */
public class InfoResponse {

	// TODO: maybe use jackson parser instead of json.org. seems like jackson is already present on classpath - check Koubachi, netatmo bindings
	private final JSONObject json;

	/** Create wrapped around the JSON response. */
	InfoResponse(String response) {
		this.json = new JSONObject(response);
	}
	
	
	private JSONObject getJsonForDevice(String device) {
		JSONArray jsonArray = json.getJSONArray("devices");
		for(int i = 0; i < jsonArray.length(); i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			if(device.equals(jsonObject.getString(NeoStatProperty.DeviceName.protocolName))) {
				return jsonObject;
			}
		}
		return null; // nothing found TODO: should this be an exception instead? will cause nullpointer expns
	}
	
	// TODO: refactor, so that everything (datatype in json and return type) purely depends on the neoStatProperty (property and its type)
	// maybe even adjust the return types to neohub types
	public BigDecimal getCurrentSetTemperature(String device) {
		return new BigDecimal(getJsonForDevice(device).getString(NeoStatProperty.CurrentSetTemperature.protocolName));
	}
	
	public BigDecimal getCurrentTemperature(String device) {
		return new BigDecimal(getJsonForDevice(device).getString(NeoStatProperty.CurrentTemperature.protocolName));
	}
	
	public BigDecimal getCurrentFloorTemperature(String device) {
		return new BigDecimal(getJsonForDevice(device).getString(NeoStatProperty.CurrentFloorTemperature.protocolName));
	}
	
	public boolean isAway(String device) {
		return getJsonForDevice(device).getBoolean(NeoStatProperty.Away.protocolName);
	}
	
	public boolean isStandby(String device) {
		return getJsonForDevice(device).getBoolean(NeoStatProperty.Standby.protocolName);
	}
	
	public boolean isHeating(String device) {
		return getJsonForDevice(device).getBoolean(NeoStatProperty.Heating.protocolName);
	}

	public String getDeviceName(String device) {
		return getJsonForDevice(device).getString(NeoStatProperty.DeviceName.protocolName);
	}
}
