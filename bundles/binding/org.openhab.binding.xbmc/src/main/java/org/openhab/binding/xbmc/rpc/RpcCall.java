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

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ListenableFuture;
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
public abstract class RpcCall {
	
	private static final Logger logger = LoggerFactory.getLogger(RpcCall.class);

	public class RpcException extends RuntimeException {
		private static final long serialVersionUID = 553643499122192425L;
		
		public RpcException(String message) {
			super(message);
		}

		public RpcException(String message, Exception e) {
			super(message, e);
		}
	}

	private final AsyncHttpClient client;
	private final String uri;
	
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
	private Map<String, Object> readJson(String json) {
		if (json == null)
			return new HashMap<String, Object>();

		try {
			return mapper.readValue(json, Map.class);
		} catch (JsonParseException e) {
			throw new RpcException("Failed to parse JSON", e);
		} catch (JsonMappingException e) {
			throw new RpcException("Failed to map JSON", e);
		} catch (IOException e) {
			throw new RpcException("Failed to read JSON", e);
		}
	}

	private String writeJson(Map<String, Object> json) {
		try {
			return mapper.writeValueAsString(json);
		} catch (JsonParseException e) {
			throw new RpcException("Failed to parse JSON", e);
		} catch (JsonMappingException e) {
			throw new RpcException("Failed to map JSON", e);
		} catch (IOException e) {
			throw new RpcException("Failed to write JSON", e);
		}
	}
	
	private void postRequest(Map<String, Object> request, Runnable completeHandler) {
		try {
			// we fire this request off asynchronously and let the completeHandler
			// process any response as necessary (can be null)
			ListenableFuture<Response> future = client.preparePost(uri)
				.setBody(writeJson(request))
				.setHeader("content-type", "application/json")
				.setHeader("accept", "application/json")
				.execute(new AsyncCompletionHandler<Response>() {
					@Override
					public Response onCompleted(Response response) throws Exception {
						Map<String, Object> json = readJson(response.getResponseBody());

						// if we get an error then throw an exception to stop the 
						// completion handler getting executed
						if (json.containsKey("error"))
							throw new RpcException(json.get("error").toString());
						
						processResponse(json);
						return response;
					}

					@Override
					public void onThrowable(Throwable t) {
						logger.error("Error handling POST response from XBMC", t);
					}
				});
			
			// add the future listener to handle the response once this request completes
			if (completeHandler != null) {
				future.addListener(completeHandler, client.getConfig().executorService());
			}
		} catch (Exception e) {
			logger.error("Failed sending POST request to XBMC", e);
		}
	}
	
	protected abstract String getName();
	protected abstract Map<String, Object> getParams();
	
	protected abstract void processResponse(Map<String, Object> response); 

	public void execute() {
		// nothing to do on completion
		execute(null);
	}
	
	public void execute(Runnable completeHandler) {		
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("jsonrpc", "2.0");
		request.put("method", getName());
		request.put("id", UUID.randomUUID().toString());

		Map<String, Object> params = getParams();
		if (params.size() > 0) {
			request.put("params", params);			
		}

		postRequest(request, completeHandler);
	}
}