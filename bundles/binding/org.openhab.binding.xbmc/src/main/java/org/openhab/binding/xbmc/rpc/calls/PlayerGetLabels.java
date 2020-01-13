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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.xbmc.rpc.RpcCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncHttpClient;

/**
 * Player.GetLabels RPC
 *
 * @author Plebs
 * @since 1.9.0
 */
public class PlayerGetLabels extends RpcCall {

    private static final Logger logger = LoggerFactory.getLogger(RpcCall.class);

    private int playerId;
    private List<String> properties;

    private Map<String, Object> item;

    public PlayerGetLabels(AsyncHttpClient client, String uri) {
        super(client, uri);
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    @Override
    protected String getName() {
        return "XBMC.GetInfoLabels";
    }

    @Override
    protected Map<String, Object> getParams() {
        List<String> paramProperties = new ArrayList<String>();
        for (String property : properties) {
            if (property.startsWith("Label.")) {
                String paramProperty = getParamProperty(property);
                paramProperties.add(paramProperty);
            }
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("labels", paramProperties);
        return params;
    }

    @Override
    protected void processResponse(Map<String, Object> response) {
        Map<String, Object> result = getMap(response, "result");
        item = result;
    }

    public String getPropertyValue(String property) {
        String paramProperty = getParamProperty(property);
        if (!item.containsKey(paramProperty)) {
            return null;
        }

        Object value = item.get(paramProperty);

        if (value instanceof List<?>) {
            List<?> values = (List<?>) value;

            // check if list contains any values
            if (values.size() == 0) {
                return null;
            }

            // some properties come back as a list with an indexer
            String paramPropertyIndex = getPropertyValue(paramProperty + "id");
            int propertyIndex;
            if (!StringUtils.isEmpty(paramPropertyIndex)) {
                // attempt to parse the property index
                try {
                    propertyIndex = Integer.parseInt(paramPropertyIndex);
                } catch (NumberFormatException e) {
                    return null;
                }

                // check if the index is valid
                if (propertyIndex < 0 || propertyIndex >= values.size()) {
                    return null;
                }
            } else {
                // some properties come back as a list without an indexer,
                // e.g. artist, for these we return the first in the list
                propertyIndex = 0;
            }

            value = values.get(propertyIndex);
        }

        if (value == null) {
            return null;
        }

        return value.toString();
    }

    private String getParamProperty(String property) {
        // properties entered as 'Label.Title' etc - so strip the first 6 chars
        return property.substring(6); // It should not be in lowercase
    }
}
