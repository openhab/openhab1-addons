/**
 * Copyright 2014 Nest Labs Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software * distributed under
 * the License is distributed on an "AS IS" BASIS, * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openhab.binding.nest.internal.api.model;


import org.json.JSONException;
import org.json.JSONObject;

public final class AccessToken  {
    private final String mToken;
    private final long mExpiresIn;

    private AccessToken(String token, long expiresIn) {
        mToken = token;
        mExpiresIn = expiresIn;
    }

    public String getToken() {
        return mToken;
    }

    public long getExpiresIn() {
        return mExpiresIn;
    }

    public static AccessToken fromJSON(JSONObject json) {
        try {
            String token = json.getString(Keys.ACCESS_TOKEN.TOKEN);
            long expiresIn = json.getLong(Keys.ACCESS_TOKEN.EXPIRES_IN);
            return new AccessToken(token, expiresIn);
        } catch (JSONException excep) {
            return null;
        }
    }

    public static class Builder {
        private String mToken;
        private long mExpiresIn;

        public Builder setToken(String token) {
            mToken = token;
            return this;
        }

        public Builder setExpiresIn(long expiresIn) {
            mExpiresIn = expiresIn;
            return this;
        }

        public AccessToken build() {
            return new AccessToken(mToken, mExpiresIn);
        }
    }
}
