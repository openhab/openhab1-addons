package org.openhab.binding.xbmc.rpc.calls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

public class PlayerGetItem extends RpcCall {
	
	private int playerId;

	private String title = "";
	private String fanart = "";
	private String showtitle = "";
	
	public PlayerGetItem(AsyncHttpClient client, String uri) {
		super(client, uri);
	}
	
	public void setPlayerId( int playerId) {
		this.playerId = playerId;
	}

	@Override
	protected String getName() {
		return "Player.GetItem";
	}
	
	@Override
	protected Map<String, Object> getParams() {		
		List<String> properties = new ArrayList<String>();
		properties.add("title");
		properties.add("showtitle");
		properties.add("fanart");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("playerid", playerId);
		params.put("properties", properties);
		return params;
	}
	
	@Override
	protected void processResponse(Map<String, Object> response) {
		Map<String, Object> result = getMap(response, "result");

		Map<String, Object> item = getMap(result, "item");
		title = getItemField(item, "title");
		showtitle = getItemField(item, "showtitle");
		fanart = getItemField(item, "fanart");
	}
	
	public String getTitle() {
		executedOrException();
		return title;
	}
	
	public String getShowtitle() {
		executedOrException();
		return showtitle;
	}
	
	public String getFanart() {
		executedOrException();
		return fanart;
	}
	
	private String getItemField(Map<String, Object> item, String fieldName){
		if (!item.containsKey(fieldName))
			return null;
		
		return (String)item.get(fieldName);
	}
}
