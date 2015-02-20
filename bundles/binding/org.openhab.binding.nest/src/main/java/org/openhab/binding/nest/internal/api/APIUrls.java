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
package org.openhab.binding.nest.internal.api;

public final class APIUrls {
    private APIUrls() {
        throw new AssertionError("Utility class; do not instantiate.");
    }

    private final static String BASE_ACCESS_TOKEN_URL = "https://api.home.nest.com/";
    private final static String BASE_AUTHORIZATION_URL = "https://home.nest.com/";
    public final static String CLIENT_CODE_URL = BASE_AUTHORIZATION_URL + "login/oauth2?client_id=%s&state=%s";
//    public final static String ACCESS_URL = BASE_ACCESS_TOKEN_URL + "oauth2/access_token?code=%s&client_id=%s&client_secret=%s&grant_type=authorization_code";
    //204b41ce-edc4-4a1d-b38a-5910b8082a7b
//    yRXUdeiUwFsDLXezKykeQGijZ
    public final static String ACCESS_URL = BASE_ACCESS_TOKEN_URL + "oauth2/access_token?client_id=%s&code=%s&client_secret=%s&grant_type=authorization_code";
    public final static String NEST_FIREBASE_URL = "https://developer-api.nest.com";
}

