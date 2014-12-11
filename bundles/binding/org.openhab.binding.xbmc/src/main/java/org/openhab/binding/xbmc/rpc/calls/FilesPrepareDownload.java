/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xbmc.rpc.calls;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

/**
 * Files.PrepareDownload RPC
 * 
 * @author tlan, Ben Jones
 * @since 1.5.0
 */
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
	protected void processResponse(Map<String, Object> response) {
		Map<String, Object> result = getMap(response, "result");		
		
		Map<String, Object> details = getMap(result, "details");
		if(details.containsKey("path"))
			path = (String)details.get("path");
	}
	
	public String getPath() {
		return path;
	}
}
