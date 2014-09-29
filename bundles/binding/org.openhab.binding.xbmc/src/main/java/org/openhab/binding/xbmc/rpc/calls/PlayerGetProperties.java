/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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

import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

/**
 * Player.GetProperties RPC
 * 
 * @author Ben Jones
 * @since 1.5.0
 */
public class PlayerGetProperties extends RpcCall {

	private int playerId;

	private boolean paused = false;
	
	public PlayerGetProperties(AsyncHttpClient client, String uri) {
		super(client, uri);
	}
	
	public void setPlayerId( int playerId) {
		this.playerId = playerId;
	}
	
	@Override
	protected String getName() {
		return "Player.GetProperties";
	}

	@Override
	protected Map<String, Object> getParams() {
		List<String> properties = new ArrayList<String>();
		properties.add("speed");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("playerid", playerId);
		params.put("properties", properties);
		return params;
	}

	@Override
	protected void processResponse(Map<String, Object> response) {
		Map<String, Object> result = getMap(response, "result");

		if (result.containsKey("speed"))
			paused = (Integer)result.get("speed") == 0;
	}
	
	public boolean isPaused() {
		return paused;
	}
}
