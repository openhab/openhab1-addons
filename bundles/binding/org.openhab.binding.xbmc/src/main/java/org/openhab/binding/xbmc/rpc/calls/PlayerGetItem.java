/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xbmc.rpc.calls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

/**
 * Player.GetItem RPC
 * 
 * @author tlan, Ben Jones, Marcel Erkel
 * @since 1.5.0
 */
public class PlayerGetItem extends RpcCall {
	
	private int playerId;
	private List<String> properties;

	private Map<String, Object> item;
	
	public PlayerGetItem(AsyncHttpClient client, String uri) {
		super(client, uri);
	}
	
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	
	public void setProperties(List<String> properties) {
		this.properties = properties;
	}

	@Override
	protected String getName() {
		return "Player.GetItem";
	}
	
	@Override
	protected Map<String, Object> getParams() {		
		List<String> paramProperties = new ArrayList<String>();
		for (String property : properties) {
			if (property.equals("Player.Type"))
				continue;
			if (property.equals("Player.Label"))
				continue;
			String paramProperty = getParamProperty(property);
			paramProperties.add(paramProperty);
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("playerid", playerId);
		params.put("properties", paramProperties);
		return params;
	}
	
	@Override
	protected void processResponse(Map<String, Object> response) {
		Map<String, Object> result = getMap(response, "result");
		item = getMap(result, "item");
	}
	
	public String getPropertyValue(String property){
		String paramProperty = getParamProperty(property);
		if (!item.containsKey(paramProperty))
			return null;
		
		Object value = item.get(paramProperty);

		if (value instanceof List<?>) {
			List<?> values = (List<?>)value;
			
			// check if list contains any values
			if (values.size() == 0)
				return null;
			
			// some properties come back as a list with an indexer
			String paramPropertyIndex = getPropertyValue(paramProperty + "id");
			int propertyIndex;
			if (!StringUtils.isEmpty(paramPropertyIndex)) {
				// attempt to parse the property index
				try {
					propertyIndex = Integer.parseInt(paramPropertyIndex);
				} catch (NumberFormatException e) {
					return null;
				}

				// check if the index is valid
				if (propertyIndex < 0 || propertyIndex >= values.size())
					return null;
			}
			else {
				// some properties come back as a list without an indexer,
				// e.g. artist, for these we return the first in the list
				propertyIndex = 0;
			}
			
			value = values.get(propertyIndex);
		}
		
		if (value == null)
			return null;
		
		return value.toString();
	}
		
	private String getParamProperty(String property) {
		// properties entered as 'Player.Title' etc - so strip the first 7 chars
		return property.substring(7).toLowerCase();
	}
}
