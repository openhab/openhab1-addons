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
package org.openhab.io.gcal.auth;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Key;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;

/**
 * Service which downloads Calendar events, parses their content and creates
 * Quartz-jobs and triggers out of them.
 *
 * @author Adam Pienczykowski
 * @since 1.9.0
 */
public class GCalGoogleOAuth {

    private static final Logger logger = LoggerFactory.getLogger(GCalGoogleOAuth.class);
    private static final String TOKEN_PATH = "gcal";
    private static final String TOKEN_STORE_USER_ID = "openhab";

    private static String client_id = "";
    private static String client_secret = "";

    private static CalendarList calendarList = null;

    public static class Device {
        @Key
        public String device_code;
        @Key
        public String user_code;
        @Key
        public String verification_url;
        @Key
        public int expires_in;
        @Key
        public int interval;
    }

    public static class DeviceToken {
        @Key
        public String access_token;
        @Key
        public String token_type;
        @Key
        public String refresh_token;
        @Key
        public int expires_in;
    }

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Define a global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /**
     * Setup OAuth2 client_id used for authentication
     */
    public static void setClientId(String id) {
        client_id = id;
    }

    /**
     * Setup OAuth2 client_secret used for authentication
     */
    public static void setClientSecret(String secret) {
        client_secret = secret;
    }

    /**
     * <p>
     * Perform OAuth2 authorization with Google server based on provided client_id and client_secret and
     * stores credential in local persistent store.
     * </p>
     *
     * @param newCredential If true try to obtain new credential (user interaction required)
     * @return Authorization credential object.
     */
    public static Credential getCredential(boolean newCredential) {
        Credential credential = null;
        try {
            File tokenPath = null;
            String userdata = System.getProperty("smarthome.userdata");
            if (StringUtils.isEmpty(userdata)) {
                tokenPath = new File("etc");
            } else {
                tokenPath = new File(userdata);
            }

            File tokenFile = new File(tokenPath, TOKEN_PATH);

            FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(tokenFile);
            DataStore<StoredCredential> datastore = fileDataStoreFactory.getDataStore("gcal_oauth2_token");

            credential = loadCredential("openhab", datastore);
            if (credential == null && newCredential) {

                if (StringUtils.isBlank(client_id) || StringUtils.isBlank(client_secret)) {
                    logger.warn("OAuth2 credentials are not provided");
                    return null;
                }
                GenericUrl genericUrl = new GenericUrl("https://accounts.google.com/o/oauth2/device/code");

                Map<String, String> mapData = new HashMap<String, String>();
                mapData.put("client_id", client_id);
                mapData.put("scope", CalendarScopes.CALENDAR);
                UrlEncodedContent content = new UrlEncodedContent(mapData);
                HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });
                HttpRequest postRequest = requestFactory.buildPostRequest(genericUrl, content);

                Device device = postRequest.execute().parseAs(Device.class);
                // no access token/secret specified so display the authorisation URL in the log
                logger.info(
                        "################################################################################################");
                logger.info("# Google-Integration: U S E R   I N T E R A C T I O N   R E Q U I R E D !!");
                logger.info("# 1. Open URL '{}'", device.verification_url);
                logger.info("# 2. Type provided code {} ", device.user_code);
                logger.info("# 3. Grant openHAB access to your Google calendar");
                logger.info(
                        "# 4. openHAB will automatically detect the permiossions and complete the authentication process");
                logger.info("# NOTE: You will only have {} mins before openHAB gives up waiting for the access!!!",
                        device.expires_in);
                logger.info(
                        "################################################################################################");

                if (logger.isDebugEnabled()) {
                    logger.debug("Got access code");
                    logger.debug("user code : {}", device.user_code);
                    logger.debug("device code : {}", device.device_code);
                    logger.debug("expires in: {}", device.expires_in);
                    logger.debug("interval : {}", device.interval);
                    logger.debug("verification_url : {}", device.verification_url);
                }

                mapData = new HashMap<String, String>();
                mapData.put("client_id", client_id);
                mapData.put("client_secret", client_secret);
                mapData.put("code", device.device_code);
                mapData.put("grant_type", "http://oauth.net/grant_type/device/1.0");

                content = new UrlEncodedContent(mapData);
                postRequest = requestFactory
                        .buildPostRequest(new GenericUrl("https://accounts.google.com/o/oauth2/token"), content);

                DeviceToken deviceToken;
                do {
                    deviceToken = postRequest.execute().parseAs(DeviceToken.class);

                    if (deviceToken.access_token != null) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Got access token");
                            logger.debug("device access token: {}", deviceToken.access_token);
                            logger.debug("device token_type: {}", deviceToken.token_type);
                            logger.debug("device refresh_token: {}", deviceToken.refresh_token);
                            logger.debug("device expires_in: {}", deviceToken.expires_in);
                        }
                        break;
                    }
                    logger.debug("waiting for {} seconds", device.interval);
                    Thread.sleep(device.interval * 1000);

                } while (true);

                StoredCredential dataCredential = new StoredCredential();
                dataCredential.setAccessToken(deviceToken.access_token);
                dataCredential.setRefreshToken(deviceToken.refresh_token);
                dataCredential.setExpirationTimeMilliseconds((long) deviceToken.expires_in * 1000);

                datastore.set(TOKEN_STORE_USER_ID, dataCredential);

                credential = loadCredential(TOKEN_STORE_USER_ID, datastore);
            }
        } catch (Exception e) {
            logger.warn("getCredential got exception: {}", e.getMessage());
        }

        return credential;
    }

    private static Credential loadCredential(String userId, DataStore<StoredCredential> credentialDataStore)
            throws IOException {
        Credential credential = newCredential(userId, credentialDataStore);
        if (credentialDataStore != null) {
            StoredCredential stored = credentialDataStore.get(userId);
            if (stored == null) {
                return null;
            }
            credential.setAccessToken(stored.getAccessToken());
            credential.setRefreshToken(stored.getRefreshToken());
            credential.setExpirationTimeMilliseconds(stored.getExpirationTimeMilliseconds());
            if (logger.isDebugEnabled()) {
                logger.debug("Loaded credential");
                logger.debug("device access token: {}", stored.getAccessToken());
                logger.debug("device refresh_token: {}", stored.getRefreshToken());
                logger.debug("device expires_in: {}", stored.getExpirationTimeMilliseconds());
            }
        }
        return credential;
    }

    private static Credential newCredential(String userId, DataStore<StoredCredential> credentialDataStore) {

        Credential.Builder builder = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setTransport(HTTP_TRANSPORT).setJsonFactory(JSON_FACTORY)
                .setTokenServerEncodedUrl("https://accounts.google.com/o/oauth2/token")
                .setClientAuthentication(new ClientParametersAuthentication(client_id, client_secret))
                .setRequestInitializer(null).setClock(Clock.SYSTEM);

        builder.addRefreshListener(new DataStoreCredentialRefreshListener(userId, credentialDataStore));

        return builder.build();
    }

    /**
     * Return calendarID based on calendar name.
     * if calendar name is "primary" not check primary - just return primary
     *
     * @param calendar object
     */
    public static CalendarListEntry getCalendarId(String calendar_name) {

        CalendarListEntry calendarID = null;

        if (calendarList == null) {
            Calendar client = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredential(false))
                    .setApplicationName("openHAB").build();
            try {
                calendarList = client.calendarList().list().execute();
            } catch (com.google.api.client.auth.oauth2.TokenResponseException ae) {
                logger.error("authentication failed: {}", ae.getMessage());
                return null;
            } catch (IOException e1) {
                logger.error("authentication I/O exception: {}", e1.getMessage());
                return null;
            }
        }

        if (calendarList != null && calendarList.getItems() != null) {
            for (CalendarListEntry entry : calendarList.getItems()) {
                if ((entry.getSummary().equals(calendar_name))
                        || (calendar_name.equals("primary")) && entry.isPrimary()) {
                    calendarID = entry;
                    logger.debug("Got calendar {} CalendarID: {}", calendar_name, calendarID.getId());
                }
            }
        }

        if (calendarID == null) {
            logger.warn("Calendar {} not found", calendar_name);
        }

        return calendarID;
    }

}
