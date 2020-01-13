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
package org.openhab.binding.garadget.internal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class handles the connection via REST API.
 *
 * @author John Cocula
 * @since 1.9.0
 */
public class Connection {
    private final Logger logger = LoggerFactory.getLogger(Connection.class);

    protected static final ObjectMapper JSON = new ObjectMapper();

    private static final String HTTP_GET = "GET";
    private static final String HTTP_PUT = "PUT";
    private static final String HTTP_POST = "POST";
    private static final String HTTP_DELETE = "DELETE";
    private static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final String APPLICATION_JSON = "application/json";

    private static final String API_BASE_URL = "https://api.particle.io";

    // POST
    private static final String TOKEN_URL = API_BASE_URL + "/oauth/token";
    // DELETE
    private static final String ACCESS_TOKENS_URL = API_BASE_URL + "/v1/access_tokens/%1$s";
    // GET
    private static final String GET_DEVICES_URL = API_BASE_URL + "/v1/devices?access_token=%1$s";
    // GET or POST
    private static final String DEVICE_FUNC_URL = API_BASE_URL + "/v1/devices/%1$s/%2$s?format=raw&access_token=%3$s";

    private String username;
    private String password;
    private int timeout;

    public static class TokenResponse {
        @JsonProperty("access_token")
        String accessToken;

        @JsonProperty("token_type")
        String tokenType;

        @JsonProperty("expires_in")
        Integer expiresIn;

        @JsonProperty("refresh_token")
        String refreshToken;
    }

    private TokenResponse tokens;

    public Connection(String username, String password, int timeout) {
        this.username = username;
        this.password = password;
        this.timeout = timeout;
    }

    /**
     * Use the configured <code>username</code> and <code>password</code> credentials to obtain access and refresh
     * tokens for later use.
     */
    public void login() {
        String content = String.format("grant_type=password&username=%1$s&password=%2$s&expires_in=0",
                urlEncode(username), urlEncode(password));
        sendCommand(null, "createToken", "particle", "particle", content, new HttpResponseHandler() {
            @Override
            public void handleResponse(int statusCode, String responseBody) {
                logger.trace("Processing login: statusCode={}, responseBody={}", statusCode, responseBody);
                if (statusCode == HttpStatus.SC_OK) {
                    try {
                        tokens = JSON.readValue(responseBody, TokenResponse.class);
                    } catch (Exception e) {
                        logger.warn("Unable to parse token response.", e);
                    }
                }
                else {
                    logger.warn("Login failure. Status code = {}", statusCode);
                    logger.trace("Failure response: {}", responseBody);
                }
            }
        });
    }

    /**
     * Return true if this Connection had previously successfully called <code>login()</code>.
     *
     * @return
     *         true if we had previously successfully logged in.
     */
    public boolean isLoggedIn() {
        return tokens != null;
    }

    /**
     * Attempt to delete the access token on the server for this Connection, and forget the tokens locally.
     */
    public void logout() {
        sendCommand(null, "deleteToken", null, new HttpResponseHandler() {
            @Override
            public void handleResponse(int statusCode, String responseBody) {
                logger.trace("Processing logout response: statusCode={}, responseBody={}", statusCode, responseBody);
                tokens = null;
            }
        });
    }

    /**
     * Return a Map of device IDs to device objects, or an empty Map if no devices could be retrieved.
     *
     * @return
     *         a Map of device IDs to device objects, or an empty Map if no devices could be retrieved.
     */
    public Map<String, GaradgetDevice> getDevices() {
        final Map<String, GaradgetDevice> devices = new HashMap<String, GaradgetDevice>();

        sendCommand(null, "getDevices", null, new HttpResponseHandler() {
            @Override
            public void handleResponse(int statusCode, String responseBody) {
                try {
                    GaradgetDevice[] deviceList = JSON.readValue(responseBody, GaradgetDevice[].class);
                    for (int i = 0; i < deviceList.length; i++) {
                        devices.put(deviceList[i].getId(), deviceList[i]);
                    }
                } catch (Exception e) {
                    logger.warn("Unable to parse getDevices response.", e);
                }
            }
        });

        return devices;
    }

    /**
     * Send a command to the Particle REST API (convenience function).
     *
     * @param device
     *            the device context, or <code>null</code> if not needed for this command.
     * @param funcName
     *            the function name to call, or variable/field to retrieve if <code>command</code> is
     *            <code>null</code>.
     * @param command
     *            the command to send to the API.
     * @param proc
     *            a callback object that receives the status code and response body, or <code>null</code> if not
     *            needed.
     */
    public void sendCommand(AbstractDevice device, String funcName, String command, HttpResponseHandler proc) {
        sendCommand(device, funcName, username, password, command, proc);
    }

    /**
     * Send a command to the Particle REST API (convenience function).
     *
     * @param device
     *            the device context, or <code>null</code> if not needed for this command.
     * @param funcName
     *            the function name to call, or variable/field to retrieve if <code>command</code> is
     *            <code>null</code>.
     * @param user
     *            the user name to use in Basic Authentication if the funcName would require Basic Authentication.
     * @param pass
     *            the password to use in Basic Authentication if the funcName would require Basic Authentication.
     * @param command
     *            the command to send to the API.
     * @param proc
     *            a callback object that receives the status code and response body, or <code>null</code> if not
     *            needed.
     */
    public void sendCommand(AbstractDevice device, String funcName, String user, String pass, String command,
            HttpResponseHandler proc) {
        String url = null;
        String httpMethod = null;
        String content = null;
        String contentType = null;
        Properties headers = new Properties();
        logger.trace("sendCommand: funcName={}", funcName);

        switch (funcName) {
            case "createToken":
                httpMethod = HTTP_POST;
                url = TOKEN_URL;
                content = command;
                contentType = APPLICATION_FORM_URLENCODED;
                break;
            case "deleteToken":
                httpMethod = HTTP_DELETE;
                if (tokens == null) {
                    logger.debug("Unable to execute deleteToken command; no tokens exist");
                    return;
                }
                url = String.format(ACCESS_TOKENS_URL, tokens.accessToken);
                break;
            case "getDevices":
                httpMethod = HTTP_GET;
                if (tokens == null) {
                    logger.debug("Unable to execute getDevices command; no tokens exist");
                    return;
                }
                url = String.format(GET_DEVICES_URL, tokens.accessToken);
                break;
            default:
                if (tokens == null) {
                    logger.debug("Unable to execute {} command; no tokens exist", funcName);
                    return;
                }
                url = String.format(DEVICE_FUNC_URL, device.getId(), funcName, tokens.accessToken);
                if (command == null) {
                    // retrieve a variable
                    httpMethod = HTTP_GET;
                } else {
                    // call a function
                    httpMethod = HTTP_POST;
                    content = command;
                    contentType = APPLICATION_JSON;
                }
                break;
        }

        HttpClient client = new HttpClient();

        // Only perform basic authentication when we aren't using OAuth

        if (!url.contains("access_token=")) {
            Credentials credentials = new UsernamePasswordCredentials(user, pass);
            client.getParams().setAuthenticationPreemptive(true);
            client.getState().setCredentials(AuthScope.ANY, credentials);
        }

        HttpMethod method = createHttpMethod(httpMethod, url);
        method.getParams().setSoTimeout(timeout);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

        for (String httpHeaderKey : headers.stringPropertyNames()) {
            method.addRequestHeader(new Header(httpHeaderKey, headers.getProperty(httpHeaderKey)));
            logger.trace("Header key={}, value={}", httpHeaderKey, headers.getProperty(httpHeaderKey));
        }

        try {
            // add content if a valid method is given ...
            if (method instanceof EntityEnclosingMethod && content != null) {
                EntityEnclosingMethod eeMethod = (EntityEnclosingMethod) method;
                eeMethod.setRequestEntity(new StringRequestEntity(content, contentType, null));
                logger.trace("content='{}', contentType='{}'", content, contentType);
            }

            if (logger.isDebugEnabled()) {
                try {
                    logger.debug("About to execute '{}'", method.getURI());
                } catch (URIException e) {
                    logger.debug(e.getMessage());
                }
            }

            int statusCode = client.executeMethod(method);
            if (statusCode >= HttpStatus.SC_BAD_REQUEST) {
                logger.debug("Method failed: " + method.getStatusLine());
            }

            String responseBody = IOUtils.toString(method.getResponseBodyAsStream());
            if (!responseBody.isEmpty()) {
                logger.debug("Body of response: {}", responseBody);
            }

            if (proc != null) {
                proc.handleResponse(statusCode, responseBody);
            }
        } catch (HttpException he) {
            logger.warn("{}", he);
        } catch (IOException ioe) {
            logger.debug("{}", ioe);
        } finally {
            method.releaseConnection();
        }
    }

    /**
     * Factory method to create a {@link HttpMethod}-object according to the
     * given String <code>httpMethod</code>
     *
     * @param httpMethodString
     *            the name of the {@link HttpMethod} to create
     * @param url
     *            the URL to include in the returned Method
     * @return
     *         an object of type {@link GetMethod}, {@link PutMethod},
     *         {@link PostMethod} or {@link DeleteMethod}
     * @throws
     *             IllegalArgumentException if <code>httpMethod</code> is none of
     *             <code>GET</code>, <code>PUT</code>, <code>POST</POST> or <code>DELETE</code>
     */
    private static HttpMethod createHttpMethod(String httpMethodString, String url) {

        if (HTTP_GET.equals(httpMethodString)) {
            return new GetMethod(url);
        } else if (HTTP_PUT.equals(httpMethodString)) {
            return new PutMethod(url);
        } else if (HTTP_POST.equals(httpMethodString)) {
            return new PostMethod(url);
        } else if (HTTP_DELETE.equals(httpMethodString)) {
            return new DeleteMethod(url);
        } else {
            throw new IllegalArgumentException("given httpMethod '" + httpMethodString + "' is unknown");
        }
    }

    /**
     * URL Encode a string using UTF-8 encoding
     *
     * @param str
     *            the string to encode
     * @return
     *         the encoded string
     */
    private String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.warn("Could not encode string '{}'", str, e);
            return str;
        }
    }
}
