/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xbmc.rpc.calls;

import java.util.Collections;
import java.util.Map;

import org.openhab.binding.xbmc.rpc.RpcCall;

import com.ning.http.client.AsyncHttpClient;

/**
 * Input.Up RPC
 *
 * @author Il Plebs
 * @since 1.5.0
 */
public class InputUp extends RpcCall {

    public InputUp(AsyncHttpClient client, String uri) {
        super(client, uri);
    }

    @Override
    protected String getName() {
        return "Input.Up";
    }

    @Override
    protected Map<String, Object> getParams() {
        return Collections.emptyMap();
    }

    @Override
    protected void processResponse(Map<String, Object> response) {
    }
}