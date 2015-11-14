/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xbmc.rpc.calls;

import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Input.ExecuteAction RPC
 * 
 * @author Mikolaj Siedlarek
 * @since 1.8.0
 */
public class InputExecuteAction extends RpcCall {
	
	String action;

    public InputExecuteAction(AsyncHttpClient client, String uri) {
        super(client, uri);
    }
    
    public void setAction(String action) {
    	this.action = action;
    }

    @Override
    protected String getName() {
        return "Input.ExecuteAction";
    }

    @Override
    protected Map<String, Object> getParams() {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("action", action);
        return params;
    }

    @Override
    protected void processResponse(Map<String, Object> response) {
    }

}
