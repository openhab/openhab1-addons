package org.openhab.binding.xbmc.rpc.calls;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

public class FilesPrepareDownload extends RpcCall {
	
	private String imagePath;

	private String path;

	public FilesPrepareDownload(AsyncHttpClient client, String uri) {
		super(client, uri);
	}
	
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	@Override
	protected String getName() {
		return "Files.PrepareDownload";
	}

	@Override
	protected Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("path", imagePath);
		return params;
	}

	@Override
	protected void processResponse(Map<String, Object> response) throws RpcException {
		Map<String, Object> result = getMap(response, "result");		
		
		Map<String, Object> details = getMap(result, "details");
		if(details.containsKey("path"))
			path = (String)details.get("path");
	}
	
	public String getPath() {
		executedOrException();
		return path;
	}
}
