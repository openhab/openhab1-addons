/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.caldav.internal;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to handle all OAuth specific stuff.
 *
 * @author Robert Delbrück
 * @since 1.9.0
 *
 */
public class OAuthUtil {
    private static final Logger logger = LoggerFactory.getLogger(OAuthUtil.class);

    private static String deviceCode;

    private static RuntimeData runtimeData = RuntimeData.getInstance();

    public static boolean requestAccess(String clientId) throws URISyntaxException {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            URIBuilder builder = new URIBuilder();
            builder.setScheme("https").setHost("accounts.google.com").setPort(443).setPath("/o/oauth2/device/code")
                    .setParameter("client_id", clientId)
                    .setParameter("scope", "https://www.googleapis.com/auth/calendar");

            HttpPost post = new HttpPost(builder.build());
            post.addHeader("Accept", "application/json");
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            HttpResponse result = httpClient.execute(post);

            if (result.getStatusLine().getStatusCode() == 200) {
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(result.getEntity()));
                String deviceCode = jsonObject.getString("device_code");
                String userCode = jsonObject.getString("user_code");
                String verificationUrl = jsonObject.getString("verification_url");
                logger.warn("authentication required for account with client_id={}", clientId);
                logger.warn("user_code={}", userCode);
                logger.warn("verification_url={}", verificationUrl);
                OAuthUtil.deviceCode = deviceCode;
                return true;
            } else {
                return false;
            }
        } catch (IOException ex) {
            logger.error("error accessing OAuth", ex);
            throw new IllegalStateException("error accessing OAuth", ex);
        }
    }

    public static boolean poll(String calendarId, String clientId, String clientSecret, String deviceCode)
            throws URISyntaxException {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            URIBuilder builder = new URIBuilder();
            builder.setScheme("https").setHost("www.googleapis.com").setPort(443).setPath("/oauth2/v4/token")
                    .setParameter("client_id", clientId).setParameter("client_secret", clientSecret)
                    .setParameter("code", deviceCode)
                    .setParameter("grant_type", "http://oauth.net/grant_type/device/1.0");

            HttpPost post = new HttpPost(builder.build());
            post.addHeader("Accept", "application/json");
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            HttpResponse result = httpClient.execute(post);
            JSONObject jsonObject = new JSONObject(EntityUtils.toString(result.getEntity()));

            if (result.getStatusLine().getStatusCode() == 200) {
                String accessToken = jsonObject.getString("access_token");
                String refreshToken = jsonObject.getString("refresh_token");
                runtimeData.setAuthenticationBearer(calendarId, accessToken);
                runtimeData.setRefreshToken(calendarId, refreshToken);
                return true;
            } else if (result.getStatusLine().getStatusCode() == 400) {
                String error = jsonObject.getString("error");
                String errorDescription = jsonObject.getString("error_description");
                if (error.equals("authorization_pending")) {
                    return false;
                } else {
                    throw new IllegalStateException(String.format(
                            "unknown error code '%s - %s' while polling for authorization", error, errorDescription));
                }
            } else {
                throw new IllegalStateException(String.format("unknown errorcode '%s' while polling for authorization",
                        result.getStatusLine().getStatusCode()));
            }

        } catch (IOException ex) {
            logger.error("error accessing OAuth", ex);
            throw new IllegalStateException("error accessing OAuth", ex);
        }
    }

    public static void refreshToken(String calendarId, String clientId, String clientSecret, String refreshToken)
            throws URISyntaxException {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            URIBuilder builder = new URIBuilder();
            builder.setScheme("https").setHost("www.googleapis.com").setPort(443).setPath("/oauth2/v4/token")
                    .setParameter("client_id", clientId).setParameter("client_secret", clientSecret)
                    .setParameter("refresh_token", refreshToken).setParameter("grant_type", "refresh_token");

            HttpPost post = new HttpPost(builder.build());
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            HttpResponse result = httpClient.execute(post);
            if (result.getStatusLine().getStatusCode() == 200) {
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(result.getEntity()));

                String accessToken = jsonObject.getString("access_token");
                runtimeData.setAuthenticationBearer(calendarId, accessToken);
            } else {
                return;
            }
        } catch (IOException ex) {
            logger.error("error accessing OAuth", ex);
            throw new IllegalStateException("error accessing OAuth", ex);
        }
    }

    public static String getCalendars(String calendarId, String clientId, String clientSecret, String url)
            throws URISyntaxException {
        String authenticationBearer = runtimeData.getAuthenticationBearer(calendarId);
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet post = new HttpGet(url);
            post.setHeader("Authorization", "Bearer " + authenticationBearer);
            HttpResponse result = httpClient.execute(post);
            if (result.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(result.getEntity());
            } else if (result.getStatusLine().getStatusCode() == 401) {
                String refreshToken = runtimeData.getRefreshToken(calendarId);
                if (refreshToken != null) {
                    refreshToken(calendarId, clientId, clientSecret, refreshToken);
                    return getCalendars(calendarId, clientId, clientSecret, url);
                } else {
                    if (requestAccess(clientId)) {
                        while (true) {
                            if (poll(calendarId, clientId, clientSecret, deviceCode)) {
                                break;
                            }
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                    return getCalendars(calendarId, clientId, clientSecret, url);
                }
            } else {
                throw new IllegalStateException(
                        "unknown error access calendar: " + result.getStatusLine().getStatusCode());
            }

        } catch (IOException ex) {
            logger.error("error accessing OAuth", ex);
            throw new IllegalStateException("error accessing OAuth", ex);
        }
    }

    public static boolean addEvent(String calendarId, String clientId, String clientSecret, String url, String calendar)
            throws URISyntaxException {
        String authenticationBearer = runtimeData.getAuthenticationBearer(calendarId);
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPut post = new HttpPut(url);
            post.setEntity(new StringEntity(calendar));
            post.setHeader("Authorization", "Bearer " + authenticationBearer);
            HttpResponse result = httpClient.execute(post);
            if (result.getStatusLine().getStatusCode() == 201) {
                return true;
            } else if (result.getStatusLine().getStatusCode() == 401) {
                String refreshToken = runtimeData.getRefreshToken(calendarId);
                if (refreshToken != null) {
                    refreshToken(calendarId, clientId, clientSecret, refreshToken);
                    return addEvent(calendarId, clientId, clientSecret, url, calendar);
                } else {
                    if (requestAccess(clientId)) {
                        while (true) {
                            if (poll(calendarId, clientId, clientSecret, deviceCode)) {
                                break;
                            }
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                    return addEvent(calendarId, clientId, clientSecret, url, calendar);
                }
            } else {
                throw new IllegalStateException(
                        "unknown error access calendar: " + result.getStatusLine().getStatusCode());
            }

        } catch (IOException ex) {
            logger.error("error accessing OAuth", ex);
            throw new IllegalStateException("error accessing OAuth", ex);
        }
    }

    public static boolean removeEvent(String calendarId, String clientId, String clientSecret, String url,
            String calendar) throws URISyntaxException {
        String authenticationBearer = runtimeData.getAuthenticationBearer(calendarId);
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpDelete post = new HttpDelete(url);
            // post.setEntity(new StringEntity(calendar));
            post.setHeader("Authorization", "Bearer " + authenticationBearer);
            HttpResponse result = httpClient.execute(post);
            if (result.getStatusLine().getStatusCode() == 201) {
                return true;
            } else if (result.getStatusLine().getStatusCode() == 401) {
                String refreshToken = runtimeData.getRefreshToken(calendarId);
                if (refreshToken != null) {
                    refreshToken(calendarId, clientId, clientSecret, refreshToken);
                    return removeEvent(calendarId, clientId, clientSecret, url, calendar);
                } else {
                    if (requestAccess(clientId)) {
                        while (true) {
                            if (poll(calendarId, clientId, clientSecret, deviceCode)) {
                                break;
                            }
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                    return removeEvent(calendarId, clientId, clientSecret, url, calendar);
                }
            } else {
                throw new IllegalStateException(
                        "unknown error access calendar: " + result.getStatusLine().getStatusCode());
            }

        } catch (IOException ex) {
            logger.error("error accessing OAuth", ex);
            throw new IllegalStateException("error accessing OAuth", ex);
        }
    }
}
