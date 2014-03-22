package org.openhab.binding.xbmc.rpc.calls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

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
		executedOrException();
		return playing;
	}

	public int getPlayerId(){
		executedOrException();
		return playerId;
	}
	
	public String getPlayerType(){
		executedOrException();
		return  playerType;
	}
}
