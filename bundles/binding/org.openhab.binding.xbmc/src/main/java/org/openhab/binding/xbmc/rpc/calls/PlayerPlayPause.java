package org.openhab.binding.xbmc.rpc.calls;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

public class PlayerPlayPause extends RpcCall {
	
	private int playerId;

	private boolean paused = false;
	
	public PlayerPlayPause(AsyncHttpClient client, String uri) {
		super(client, uri);
	}
	
	public void setPlayerId( int playerId) {
		this.playerId = playerId;
	}

	@Override
	protected String getName() {
		return "Player.PlayPause";
	}
	
	@Override
	protected Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("playerid", playerId);
		return params;
	}
	
	@Override
	protected void processResponse(Map<String, Object> response) {
		Map<String, Object> result = getMap(response, "result");

		if (result.containsKey("speed"))
			paused = (Integer)result.get("speed") == 0;
	}
	
	public boolean isPaused() {
		executedOrException();		
		return paused;
	}	
}
