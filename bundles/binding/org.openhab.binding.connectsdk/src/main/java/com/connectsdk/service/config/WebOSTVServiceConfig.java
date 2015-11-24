/*
 * WebOSTVServiceConfig
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

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

public class WebOSTVServiceConfig extends ServiceConfig {

    public static final String KEY_CLIENT_KEY = "clientKey";
    public static final String KEY_CERT = "serverCertificate";
    String clientKey;
    X509Certificate cert;

    public WebOSTVServiceConfig(String serviceUUID) {
        super(serviceUUID);
    }

    public WebOSTVServiceConfig(String serviceUUID, String clientKey) {
        super(serviceUUID);
        this.clientKey = clientKey;
        this.cert = null;
    }

    public WebOSTVServiceConfig(String serviceUUID, String clientKey, X509Certificate cert) {
        super(serviceUUID);
        this.clientKey = clientKey;
        this.cert = cert;
    }

    public WebOSTVServiceConfig(String serviceUUID, String clientKey, String cert) {
        super(serviceUUID);
        this.clientKey = clientKey;
        this.cert = loadCertificateFromPEM(cert);
    }

    public WebOSTVServiceConfig(JSONObject json) {
        super(json);

        clientKey = json.optString(KEY_CLIENT_KEY);
        cert = null; // TODO: loadCertificateFromPEM(json.optString(KEY_CERT));
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
        notifyUpdate();
    }

    public X509Certificate getServerCertificate() {
        return cert;
    }

    public void setServerCertificate(X509Certificate cert) {
        this.cert = cert;
        notifyUpdate();
    }

    public void setServerCertificate(String cert) {
        this.cert = loadCertificateFromPEM(cert);
        notifyUpdate();
    }

    public String getServerCertificateInString() {
        return exportCertificateToPEM(this.cert);
    }

    private String exportCertificateToPEM(X509Certificate cert) {
        try {
            if (cert == null) 
                return null;
            return Base64.encodeToString(cert.getEncoded(), Base64.DEFAULT);
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private X509Certificate loadCertificateFromPEM(String pemString) {
        CertificateFactory certFactory;
        try {
            certFactory = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(pemString.getBytes("US-ASCII"));

            return (X509Certificate)certFactory.generateCertificate(inputStream);
        } catch (CertificateException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject jsonObj = super.toJSONObject();

        try {
            jsonObj.put(KEY_CLIENT_KEY, clientKey);
            jsonObj.put(KEY_CERT, exportCertificateToPEM(cert));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObj;
    }

}
