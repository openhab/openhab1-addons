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
package org.openhab.binding.xbmc.rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

/**
 * Abstract class which all XBMC RPC calls are derived from. Handles
 * the posting of requests and validation/handling of responses.
 * 
 * Also contains a number of helper methods for writing/parsing the 
 * JSON request/response messages.
 * 
 * @author tlan, Ben Jones
 * @since 1.5.0
 */
public abstract class RpcCall{
	
	public class RpcException extends RuntimeException {
		private static final long serialVersionUID = 553643499122192425L;
		
		public RpcException(String message) {
			super(message);
		}

		public RpcException(Exception e) {
			super(e);
		}
	}

	private final AsyncHttpClient client;
	private final String uri;
	
	private boolean executed = false;

	private final ObjectMapper mapper = new ObjectMapper();
	
	public RpcCall(AsyncHttpClient client, String uri) {
		this.client = client;
		this.uri = uri;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMap(Map<String, Object> data, String param) {
		if (!data.containsKey(param))
			return new HashMap<String, Object>();
		
		return (Map<String, Object>)data.get(param);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Object> getList(Map<String, Object> data, String param) {
		if (!data.containsKey(param))
			return new ArrayList<Object>();
		
		return (List<Object>)data.get(param);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> readJson(String json) throws RpcException {
		if (json == null)
			return new HashMap<String, Object>();

		try {
			return mapper.readValue(json, Map.class);
		} catch (JsonParseException e) {
			throw new RpcException(e);
		} catch (JsonMappingException e) {
			throw new RpcException(e);
		} catch (IOException e) {
			throw new RpcException(e);
		}
	}

	private String writeJson(Map<String, Object> json) throws RpcException {
		try {
			return mapper.writeValueAsString(json);
		} catch (JsonParseException e) {
			throw new RpcException(e);
		} catch (JsonMappingException e) {
			throw new RpcException(e);
		} catch (IOException e) {
			throw new RpcException(e);
		}
	}
	
	private Map<String, Object> postRequest(Map<String, Object> request) throws RpcException {
		try {
			String body = writeJson(request);
			
			Response response = client.preparePost(uri)
				.setBody(body)
				.setHeader("content-type", "application/json")
				.setHeader("accept", "application/json")
				.execute()
				.get();

			return readJson(response.getResponseBody());
		} catch (IllegalArgumentException e) {
			throw new RpcException(e);
		} catch (InterruptedException e) {
			throw new RpcException(e);
		} catch (ExecutionException e) {
			throw new RpcException(e);
		} catch (IOException e) {
			throw new RpcException(e);
		}
	}
	
	private void validateResponse(Map<String, Object> response) throws RpcException {
		if (response.containsKey("error"))
			throw new RpcException("Error response received: " + response.get("error"));
	}
	
	protected abstract String getName();
	protected abstract Map<String, Object> getParams();
	
	protected abstract void processResponse(Map<String, Object> response) throws RpcException; 
	
	protected void execute() throws RpcException {		
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("jsonrpc", "2.0");
		request.put("method", getName());
		request.put("id", UUID.randomUUID().toString());

		Map<String, Object> params = getParams();
		if (params.size() > 0) {
			request.put("params", params);			
		}

		Map<String, Object> response = postRequest(request);
		validateResponse(response);
		processResponse(response);
		executed = true;
	}
	
	protected boolean isExecuted(){
		return executed;
	}
	
	protected void executedOrException(){
		if(!isExecuted())
			throw new IllegalStateException("Call was not executed yet");
	}
}