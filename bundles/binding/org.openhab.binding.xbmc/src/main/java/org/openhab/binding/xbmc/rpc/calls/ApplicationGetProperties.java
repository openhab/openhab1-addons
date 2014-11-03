/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xbmc.rpc.calls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

/**
 * Player.GetProperties RPC
 * 
 * @author Ard van der Leeuw
 * @since 1.6.0
 */
public class ApplicationGetProperties extends RpcCall {

	private int volume;
	private int version_major;
	private int version_minor;
	private String version_tag;
	private String version_revision;
	private String name;
	private boolean muted;
	
	public ApplicationGetProperties(AsyncHttpClient client, String uri) {
		super(client, uri);
	}
	
	@Override
	protected String getName() {
		return "Application.GetProperties";
	}

	@Override
	protected Map<String, Object> getParams() {
		List<String> properties = new ArrayList<String>();
		properties.add("volume");
		properties.add("version");
		properties.add("name");
		properties.add("muted");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("properties", properties);
		return params;
	}

	@Override
	protected void processResponse(Map<String, Object> response) {
		Map<String, Object> result = getMap(response, "result");

		if (result.containsKey("volume")) {
			Object o = result.get("volume");
			if (o instanceof Double) {
				volume = ((Double)o).intValue();
			} else {
				if (o instanceof Integer) {
					volume = (Integer)o;
				}				
			}
		}
		if (result.containsKey("name")) {
			name = (String)result.get("name");
		}
	}
	
	public boolean isMuted() {
		return muted;
	}
	
	public Integer getVolume() {
		return volume;
	}

}
