/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.myq.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import static org.openhab.io.net.http.HttpUtil.executeUrl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

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
	public static final String DEFAULT_APP_ID = "JVM/G9Nwih5BwKgNCjLxiFUQxQijAebyyg8QUHr7JOrP+tuPb8iHfRHKwTmDzHOu";
	public static final int DEFAUALT_TIMEOUT = 5000;

	private String userName;
	private String password;
	private String appId;
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
	 */
	public MyqData(String username, String password, String appId, int timeout) {
		this.userName = username;
		this.password = password;

		if (appId != null) {
			this.appId = appId;
		} else {
			this.appId = DEFAULT_APP_ID;
		}

		if (timeout > 0) {
			this.timeout = timeout;
		} else {
			this.timeout = DEFAUALT_TIMEOUT;
		}

		header = new Properties();
		header.put("Accept", "application/json");
		header.put("User-Agent", "myq-openhab-api/1.0");
	}

	/**
	 * Retrieves garage door device data from myq website, throws if connection
	 * fails or user login fails
	 * 
	 */
	public GarageDoorData getGarageData() throws InvalidLoginException,
			IOException {
		logger.debug("Retreiveing door data");
		String url = String.format(
				"%s/api/v4/userdevicedetails/get?appId=%s&SecurityToken=%s",
				WEBSITE, enc(appId), enc(getSecurityToken()));

		JsonNode data = request("GET", url, null, null, true);

		return new GarageDoorData(data);
	}

	/**
	 * Validates Username and Password then saved sercurityToken to a variable
	 */
	private void login() throws InvalidLoginException, IOException {
		logger.debug("attempting to login");
		String url = String
				.format("%s/api/user/validate?appId=%s&SecurityToken=null&username=%s&password=%s",
						WEBSITE, enc(appId), enc(userName), enc(password));

		JsonNode data = request("GET", url, null, null, true);
		LoginData login = new LoginData(data);
		sercurityToken = login.getSecurityToken();
	}

	/**
	 * Send Command to open/close garage door opener with MyQ API Returns false
	 * if return code from API is not correct or connection fails
	 * 
	 * @param deviceID
	 *            MyQ deviceID of Garage Door Opener.
	 * @param state
	 *            Desired state to put the door in, 1 = open, 0 = closed
	 */
	public void executeGarageDoorCommand(int deviceID, int state)
			throws InvalidLoginException, IOException {
		String message = String.format("{\"ApplicationId\":\"%s\","
				+ "\"SecurityToken\":\"%s\"," + "\"MyQDeviceId\":\"%d\","
				+ "\"AttributeName\":\"desireddoorstate\","
				+ "\"AttributeValue\":\"%d\"}", appId, sercurityToken,
				deviceID, state);
		String url = String
				.format("%s/api/v4/deviceattribute/putdeviceattribute?appId=%s&SecurityToken=%s",
						WEBSITE, enc(appId), enc(getSecurityToken()));

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
	private synchronized JsonNode request(String method, String url,
			String payload, String payloadType, boolean retry)
			throws IOException, InvalidLoginException {

		logger.debug("Requsting URL {}", url);

		String dataString = executeUrl(method, url, header,
				payload == null ? null : IOUtils.toInputStream(payload),
				payloadType, timeout);

		logger.debug("Received MyQ  JSON: {}", dataString);

		if (dataString == null) {
			throw new IOException("Null response from MyQ server");
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(dataString);
			int returnCode = rootNode.get("ReturnCode").asInt();
			logger.debug("myq ReturnCode: {}", returnCode);

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
