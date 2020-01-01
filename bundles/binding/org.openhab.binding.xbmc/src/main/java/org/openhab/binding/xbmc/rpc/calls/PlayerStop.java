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
 * Player.Stop RPC
 *
 * @author Ben Jones
 * @since 1.5.0
 */
public class PlayerStop extends RpcCall {

    private int playerId;

    public PlayerStop(AsyncHttpClient client, String uri) {
        super(client, uri);
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @Override
    protected String getName() {
        return "Player.Stop";
    }

    @Override
    protected Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("playerid", playerId);
        return params;
    }

    @Override
    protected void processResponse(Map<String, Object> response) {
    }
}
