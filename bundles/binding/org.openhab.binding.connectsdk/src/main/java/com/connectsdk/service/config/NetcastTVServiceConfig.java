/*
 * NetcastTVServiceConfig
 * Connect SDK
 * 
 * Copyright (c) 2014 LG Electronics.
 * Created by Hyun Kook Khang on 19 Jan 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.connectsdk.service.config;

import org.json.JSONException;
import org.json.JSONObject;

public class NetcastTVServiceConfig extends ServiceConfig {
    public static final String KEY_PAIRING = "pairingKey";
    String pairingKey;

    public NetcastTVServiceConfig(String serviceUUID) {
        super(serviceUUID);
    }

    public NetcastTVServiceConfig(String serviceUUID, String pairingKey) {
        super(serviceUUID);
        this.pairingKey = pairingKey;
    }

    public NetcastTVServiceConfig(JSONObject json) {
        super(json);

        pairingKey = json.optString(KEY_PAIRING, null);
    }

    public String getPairingKey() {
        return pairingKey;
    }

    public void setPairingKey(String pairingKey) {
        this.pairingKey = pairingKey;
        notifyUpdate();
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject jsonObj = super.toJSONObject();

        try {
            jsonObj.put(KEY_PAIRING, pairingKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObj;
    }

}
