/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
 * XBMC JSON RPC API: http://wiki.xbmc.org/?title=JSON-RPC_API
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