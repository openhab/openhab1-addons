/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.myq.internal;

import static org.openhab.io.net.http.HttpUtil.createHttpMethod;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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

    private static final String WEBSITE = "https://api.myqdevice.com";
    public static final String DEFAULT_APP_ID = "JVM/G9Nwih5BwKgNCjLxiFUQxQijAebyyg8QUHr7JOrP+tuPb8iHfRHKwTmDzHOu";

    public static final int DEFAUALT_TIMEOUT = 5000;

    private static final String CULTURE = "en";

    private String userName;
    private String password;
    private String appId;
    private String brandId;
    private String websiteUrl;
    private int timeout;

    private String sercurityToken;
    private String accountId;
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
    public MyqData(String username, String password, String appId, int timeout) {
        this.userName = username;
        this.password = password;

        if (appId != null) {
            this.appId = appId;
        } else {
           this.appId = DEFAULT_APP_ID;
        }

        this.websiteUrl = WEBSITE;
        this.brandId = "2";

        if (timeout > 0) {
            this.timeout = timeout;
        } else {
            this.timeout = DEFAUALT_TIMEOUT;
        }

        header = new Properties();
        header.put("Accept", "application/json");
        //header.put("User-Agent", "Chamberlain/3.73");
        header.put("BrandId", this.brandId);
        header.put("ApiVersion", "4.1");
        header.put("Culture", CULTURE);
        header.put("MyQApplicationId", this.appId);
        header.put("Content-Type", "application/json");
    }

    /**
     * Retrieves MyQ device data from myq website, throws if connection
     * fails or user login fails
     *
     */
    public MyqDeviceData getMyqData() throws InvalidLoginException, IOException {
        logger.trace("Retrieving door data");
        String url = String.format("%s/api/v5/Accounts/%s/Devices", websiteUrl, getAccountID());
        header.put("SecurityToken", getSecurityToken());
        JsonNode data = request("GET", url, null, null, true, false);

        return new MyqDeviceData(data);
    }

    /**
     * Validates Username and Password then saved sercurityToken to a variable
     */
    private void login() throws InvalidLoginException, IOException {
        logger.trace("attempting to login");
        String url = String.format("%s/api/v5/Login", websiteUrl);

        String message = String.format(
                "{\"Username\":\"%s\",\"Password\":\"%s\"}",
                userName,  password);
        JsonNode data = request("POST", url, message,"application/json", true, false);
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
     *            Desired state to put the door in, open, closed
     *            Desired state to put the lamp in, on, off
     */
    public void executeMyQCommand(String deviceID, String state) throws InvalidLoginException, IOException {
        String message = String.format( "{\"action_type\":\"%s\"}", state);
        String url = String.format("%s/api/v5/Accounts/%s/Devices/%s/actions", websiteUrl, getAccountID(), deviceID);
        header.put("SecurityToken", getSecurityToken());
        request("PUT", url, message, "application/json", true, true);
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
     * Get the Account ID for the current user
     */
    private void findAccount() throws InvalidLoginException, IOException {
        if (sercurityToken == null) {
            login();
        }
        logger.trace("attempting to get acount");
        String url = String.format("%s/api/v5/My/?expand=account", websiteUrl);

        String message = "{\"expand\":\"account\"}";
        header.put("SecurityToken", getSecurityToken());
        JsonNode data = request("GET", url, null, null, true, false);
        AccountData account = new AccountData(data);
        accountId = account.getAccountId();
    }

    /**
     * Returns the currently cached Account ID, this will make a call to
     * findAccount if the Account ID is not known.
     *
     * @return The cached Account ID
     * @throws IOException
     * @throws InvalidLoginException
     */
    private String getAccountID() throws IOException, InvalidLoginException {

        if (accountId == null) {
            findAccount();
        }
        return accountId;
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
     * @param retry
     *            "commands" have no return string, just return id empty.
     * @return The JsonNode representing the response data
     * @throws IOException
     * @throws InvalidLoginException
     */
    private synchronized JsonNode request(String method, String url, String payload, String payloadType, boolean retry, boolean command)
            throws IOException, InvalidLoginException {

        logger.trace("Requesting URL {}", url);

        String dataString = null;
        try {

            dataString = MYQExecuteUrl(method, url, header, payload == null ? null : IOUtils.toInputStream(payload),
                payloadType, timeout, !command);
        } catch (Exception e) {
            logger.trace("MYQExecuteUrl Error {}", e);;
        }

        logger.trace("Received MyQ JSON: {}", dataString);

        if(command && dataString == null)
            return null;

        if (dataString == null) {
            throw new IOException("Null response from MyQ server");
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(dataString);

            if(!rootNode.has("code"))
            {
                return rootNode;
            }
            double returnCode = rootNode.get("code").asDouble();
            logger.trace("myq ReturnCode: {}", returnCode);

            MyQResponseCode rc = MyQResponseCode.fromCode((int)returnCode);
            logger.trace("myq ReturnCode: {}", returnCode);
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
                        return request(method, url, payload, payloadType, false, command);
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
     * Executes the given <code>url</code> with the given <code>httpMethod</code>
     *
     * @param httpMethod the HTTP method to use
     * @param url the url to execute (in milliseconds)
     * @param httpHeaders optional HTTP headers which has to be set on request
     * @param content the content to be send to the given <code>url</code> or
     *            <code>null</code> if no content should be send.
     * @param contentType the content type of the given <code>content</code>
     * @param timeout the socket timeout to wait for data
     * @param shouldReturn will the call return data to stream back
     * @return the response body or <code>NULL</code> when the request went wrong
     */
    public static String MYQExecuteUrl(String httpMethod, String url, Properties httpHeaders, InputStream content,
            String contentType, int timeout, boolean shouldReturn) {
        HttpClient client = new HttpClient();

        HttpMethod method = createHttpMethod(httpMethod, url);
        method.getParams().setSoTimeout(timeout);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        if (httpHeaders != null) {
            for (String httpHeaderKey : httpHeaders.stringPropertyNames()) {
                method.addRequestHeader(new Header(httpHeaderKey, httpHeaders.getProperty(httpHeaderKey)));
            }
        }
        // add content if a valid method is given ...
        if (method instanceof EntityEnclosingMethod && content != null) {
            EntityEnclosingMethod eeMethod = (EntityEnclosingMethod) method;
            eeMethod.setRequestEntity(new InputStreamRequestEntity(content, contentType));
        }

        try {
            logger.trace("About to execute '{}'", method.getURI());
        } catch (URIException e) {
            logger.debug("{}", e.getMessage());
        }

        try {
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                logger.debug("Method failed: {}", method.getStatusLine());
            }

            if(!shouldReturn)
                return null;

            String responseBody = IOUtils.toString(method.getResponseBodyAsStream());
            if (!responseBody.isEmpty()) {
                logger.debug("{}", responseBody);
            }

            return responseBody;
        } catch (HttpException he) {
            logger.error("Fatal protocol violation: {}", he.toString());
        } catch (IOException ioe) {
            logger.error("Fatal transport error: {}", ioe.toString());
        } finally {
            method.releaseConnection();
        }

        return null;
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
