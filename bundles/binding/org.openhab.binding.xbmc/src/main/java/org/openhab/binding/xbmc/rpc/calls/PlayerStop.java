package org.openhab.binding.xbmc.rpc.calls;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

public class PlayerStop extends RpcCall {
	
	private int playerId;

	public PlayerStop(AsyncHttpClient client, String uri) {
		super(client, uri);
	}
	
	public void setPlayerId( int playerId) {
		this.playerId = playerId;
	}

	@Override
	protected String getName() {
		return "Player.Stop";
	}
	
	@Override
	protected Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("playerid", playerId);
		return params;
	}
	
	@Override
	protected void processResponse(Map<String, Object> response) {
	}
}
