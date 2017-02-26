/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.myq.internal;

import static org.openhab.io.net.http.HttpUtil.executeUrl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class handles the Chamberlain myQ http connection.
 *
 * <ul>
 * <li>userName: myQ Login Username</li>
 * <li>password: myQ Login Password</li>
 * <li>sercurityToken: sercurityToken for API requests</li>
 * <li>header: http header data</li>
 * <li>webSite: url of myQ API</li>
 * <li>appId: appId for API requests</li>
 * </ul>
 *
 * @author Scott Hanson
 * @author Dan Cunningham
 * @since 1.8.0
 */
public class MyqData {
    static final Logger logger = LoggerFactory.getLogger(MyqData.class);

    private static final String WEBSITE = "https://myqexternal.myqdevice.com";
    public static final String DEFAULT_APP_ID = "NWknvuBd7LoFHfXmKNMBcgajXtZEgKUh4V7WNzMidrpUUluDpVYVZx+xT4PCM5Kx";

    private static final String CRAFTSMAN_WEBSITE = "https://craftexternal.myqdevice.com";
    public static final String CRAFTSMAN_DEFAULT_APP_ID = "eU97d99kMG4t3STJZO/Mu2wt69yTQwM0WXZA5oZ74/ascQ2xQrLD/yjeVhEQccBZ";
    public static final int DEFAUALT_TIMEOUT = 5000;

    private static final String CULTURE = "en";

    private String userName;
    private String password;
    private String appId;
    private String brandId;
    private String websiteUrl;
    private int timeout;

    private String sercurityToken;
    private Properties header;

    /**
     * Constructor For Chamberlain MyQ http connection
     *
     * @param username
     *            Chamberlain MyQ UserName
     *
     * @param password
     *            Chamberlain MyQ password
     *
     * @param appId
     *            Chamberlain Application Id, defaults to DEFAULT_APP_ID if null
     *
     * @param timeout
     *            HTTP timeout in milliseconds, defaults to DEFAUALT_TIMEOUT if
     *            not > 0
     *
     * @param craftman
     *            Use Craftman url instead of MyQ
     */
    public MyqData(String username, String password, String appId, int timeout, boolean craftman) {
        this.userName = username;
        this.password = password;

        if (appId != null) {
            this.appId = appId;
        } else {
            if (craftman) {
                this.appId = CRAFTSMAN_DEFAULT_APP_ID;
            } else {
                this.appId = DEFAULT_APP_ID;
            }
        }

        if (craftman) {
            this.websiteUrl = CRAFTSMAN_WEBSITE;
            this.brandId = "3";
        } else {
            this.websiteUrl = WEBSITE;
            this.brandId = "2";
        }

        if (timeout > 0) {
            this.timeout = timeout;
        } else {
            this.timeout = DEFAUALT_TIMEOUT;
        }

        header = new Properties();
        header.put("Accept", "application/json");
        header.put("User-Agent", "Chamberlain/3.73");
        header.put("BrandId", this.brandId);
        header.put("ApiVersion", "4.1");
        header.put("Culture", CULTURE);
        header.put("MyQApplicationId", this.appId);
    }

    /**
     * Retrieves MyQ device data from myq website, throws if connection
     * fails or user login fails
     *
     */
    public MyqDeviceData getMyqData() throws InvalidLoginException, IOException {
        logger.trace("Retrieving door data");
        String url = String.format("%s/api/v4/userdevicedetails/get", websiteUrl);
        header.put("SecurityToken", getSecurityToken());
        JsonNode data = request("GET", url, null, null, true);

        return new MyqDeviceData(data);
    }

    /**
     * Validates Username and Password then saved sercurityToken to a variable
     */
    private void login() throws InvalidLoginException, IOException {
        logger.trace("attempting to login");
        String url = String.format("%s/api/v4/User/Validate", websiteUrl);

        String message = String.format(
                "{\"username\":\"%s\",\"password\":\"%s\"}",
                userName,  password);
        JsonNode data = request("POST", url, message,"application/json", true);
        LoginData login = new LoginData(data);
        sercurityToken = login.getSecurityToken();
    }

    /**
     * Send Command to open/close garage door opener with MyQ API Returns false
     * if return code from API is not correct or connection fails
     *
     * @param deviceID
     *            MyQ deviceID of Garage Door Opener.
     *
     * @param name
     *            Attribute Name "desireddoorstate" or "desiredlightstate"
     *
     * @param state
     *            Desired state to put the door in, 1 = open, 0 = closed
     *            Desired state to put the lamp in, 1 = on, 0 = off
     */
    public void executeMyQCommand(int deviceID, String name, int state) throws InvalidLoginException, IOException {
        String message = String.format(
                "{\"ApplicationId\":\"%s\"," + "\"SecurityToken\":\"%s\"," + "\"MyQDeviceId\":\"%d\","
                        + "\"AttributeName\":\"%s\"," + "\"AttributeValue\":\"%d\"}",
                appId, sercurityToken, deviceID, name, state);
        String url = String.format("%s/api/v4/DeviceAttribute/PutDeviceAttribute", websiteUrl);
        header.put("SecurityToken", getSecurityToken());
        request("PUT", url, message, "application/json", true);
    }

    /**
     * Returns the currently cached security token, this will make a call to
     * login if the token does not exist.
     *
     * @return The cached security token
     * @throws IOException
     * @throws InvalidLoginException
     */
    private String getSecurityToken() throws IOException, InvalidLoginException {
        if (sercurityToken == null) {
            login();
        }
        return sercurityToken;
    }

    /**
     * Make a request to the server, optionally retry the call if there is a
     * login issue. Will throw a InvalidLoginExcpetion if the account is
     * invalid, locked or soon to be locked.
     *
     * @param method
     *            The Http Method Type (GET,PUT)
     * @param url
     *            The request URL
     * @param payload
     *            Payload string for put operations
     * @param payloadType
     *            Payload content type for put operations
     * @param retry
     *            Retry the attempt if our session key is not valid
     * @return The JsonNode representing the response data
     * @throws IOException
     * @throws InvalidLoginException
     */
    private synchronized JsonNode request(String method, String url, String payload, String payloadType, boolean retry)
            throws IOException, InvalidLoginException {

        logger.trace("Requesting URL {}", url);

        String dataString = executeUrl(method, url, header, payload == null ? null : IOUtils.toInputStream(payload),
                payloadType, timeout);

        logger.trace("Received MyQ JSON: {}", dataString);

        if (dataString == null) {
            throw new IOException("Null response from MyQ server");
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(dataString);
            int returnCode = rootNode.get("ReturnCode").asInt();
            logger.trace("myq ReturnCode: {}", returnCode);

            MyQResponseCode rc = MyQResponseCode.fromCode(returnCode);

            switch (rc) {
                case OK: {
                    return rootNode;
                }
                case ACCOUNT_INVALID:
                case ACCOUNT_NOT_FOUND:
                case ACCOUNT_LOCKED:
                case ACCOUNT_LOCKED_PENDING:
                    // these are bad, we do not want to continue to log in and
                    // lock an account
                    throw new InvalidLoginException(rc.getDesc());
                case LOGIN_ERROR:
                    // Our session key has expired, request a new one
                    if (retry) {
                        login();
                        return request(method, url, payload, payloadType, false);
                    }
                    // fall through to default
                default:
                    throw new IOException("Request Failed: " + rc.getDesc());
            }

        } catch (JsonProcessingException e) {
            throw new IOException("Could not parse response", e);
        }
    }

    /**
     * URL Encode a string using UTF-8 encoding
     *
     * @param string
     * @return
     */
    private String enc(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.warn("Could not encode string", e);
            return string;
        }
    }
}
