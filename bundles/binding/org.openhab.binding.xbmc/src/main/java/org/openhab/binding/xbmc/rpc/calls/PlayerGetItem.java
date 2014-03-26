/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.xbmc.rpc.calls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

/**
 * Player.GetItem RPC
 * 
 * @author tlan, Ben Jones
 * @since 1.5.0
 */
public class PlayerGetItem extends RpcCall {
	
	private int playerId;

	private Map<String, Object> item;
	
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
		item = getMap(result, "item");
	}
	
	public String getItemField(String fieldName){
		executedOrException();

		if (!item.containsKey(fieldName))
			return null;
		
		return (String)item.get(fieldName);
	}
}
