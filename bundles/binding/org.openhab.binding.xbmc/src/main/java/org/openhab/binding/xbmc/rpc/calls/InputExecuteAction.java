/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.xbmc.rpc.calls;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

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
