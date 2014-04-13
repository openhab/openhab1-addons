/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xbmc.rpc.calls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

/**
 * Player.GetActivePlayers RPC
 * 
 * @author tlan, Ben Jones
 * @since 1.5.0
 */
public class PlayerGetActivePlayers extends RpcCall {

	private boolean playing;
	private int playerId;
	private String playerType;

	public PlayerGetActivePlayers(AsyncHttpClient client, String uri) {
		super(client, uri);
	}
	
	@Override
	protected String getName() {
		return "Player.GetActivePlayers";
	}

	@Override
	protected Map<String, Object> getParams() {
		return new HashMap<String, Object>();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void processResponse(Map<String, Object> response) {
		List<Object> result = getList(response, "result");
		
		playing = result.size() > 0;
		if (playing) {				
			Map<String, Object> player0 = (Map<String, Object>)result.get(0);
			playerId = (Integer)player0.get("playerid");
			playerType = (String)player0.get("type");
		}
	}
	
	public boolean isPlaying() {
		return playing;
	}

	public int getPlayerId(){
		return playerId;
	}
	
	public String getPlayerType(){
		return  playerType;
	}
}
