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
 * JSONRPC.Ping RPC
 * 
 * @author Ben Jones
 * @since 1.5.0
 */
public class JSONRPCPing extends RpcCall {
	
	private boolean pong = false;
	
	public JSONRPCPing(AsyncHttpClient client, String uri) {
		super(client, uri);
	}
	
	@Override
	protected String getName() {
		return "JSONRPC.Ping";
	}
	
	@Override
	protected Map<String, Object> getParams() {
		return new HashMap<String, Object>();
	}
	
	@Override
	protected void processResponse(Map<String, Object> response) {
		pong = response.containsKey("result") && response.get("result").equals("pong");
	}
	
	public boolean isPong() { 
		return pong;
	}
}
