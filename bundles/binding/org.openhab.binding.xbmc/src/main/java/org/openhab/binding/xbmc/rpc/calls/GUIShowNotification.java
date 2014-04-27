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
 * GUI.ShowNotification RPC
 * 
 * @author Ben Jones
 * @since 1.5.0
 */
public class GUIShowNotification extends RpcCall {

	private String title;
	private String message;
	private Object image = null;
	private int displaytime = 5000;

	public GUIShowNotification(AsyncHttpClient client, String uri) {
		super(client, uri);
	}
	
	public void setTitle(String title) {
		this.title = title;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public void setImage(Object image) {
		this.image = image;
	}


	public void setDisplaytime(int displaytime) {
		this.displaytime = displaytime;
	}


	@Override
	protected String getName() {
		return "GUI.ShowNotification";
	}

	@Override
	protected Map<String, Object> getParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("title", title);
		params.put("message", message);
		if (image != null) params.put("image", image);
		params.put("displaytime", displaytime);
		return params;
	}

	@Override
	protected void processResponse(Map<String, Object> response) {
		// nothing to do
	}
}
