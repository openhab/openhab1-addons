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
	protected Map<String, Object> getMap(Map<String, Object> data, String param) {
		if (!data.containsKey(param))
			return new HashMap<String, Object>();
		
		return (Map<String, Object>)data.get(param);
	}
	
	@SuppressWarnings("unchecked")
	protected List<Object> getList(Map<String, Object> data, String param) {
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