/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
 * XBMC.GetInfoBooleans RPC
 * 
 * @author Nuno Araujo
 * @since 1.5.0
 */
public class XBMCGetInfoBooleans extends RpcCall {

	private boolean screenSaverActive;

	public boolean isScreenSaverActive() {
		return screenSaverActive;
	}


	public XBMCGetInfoBooleans(AsyncHttpClient client, String uri) {
		super(client, uri);
	}
	

	@Override
	protected String getName() {
		return "XBMC.GetInfoBooleans";
	}

	@Override
	protected Map<String, Object> getParams() {
		List<String> properties = new ArrayList<String>();
		properties.add("System.ScreenSaverActive");

		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("booleans", properties);
		return params;
	}

	@Override
	protected void processResponse(Map<String, Object> response) {
		Map<String, Object> result = getMap(response, "result");
		if (result.containsKey("System.ScreenSaverActive")) {
			Object o = result.get("System.ScreenSaverActive");
			if(o!=null  && o instanceof Boolean)
			{
				screenSaverActive=(Boolean)o;
			}
		}
			
	}
}
