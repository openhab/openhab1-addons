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
 * Player.Open RPC
 *
 * @author Ard van der Leeuw
 * @since 1.6.0
 */
public class PlayerOpen extends RpcCall {

    private String file;
    private Integer channelId;

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public PlayerOpen(AsyncHttpClient client, String uri) {
        super(client, uri);
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    protected String getName() {
        return "Player.Open";
    }

    @Override
    protected Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> item = new HashMap<String, Object>();
        if (channelId != null) {
            item.put("channelid", channelId);
        } else {
            item.put("file", file);
        }
        params.put("item", item);
        return params;
    }

    @Override
    protected void processResponse(Map<String, Object> response) {
    }
}
