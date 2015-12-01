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
import static org.openhab.io.net.http.HttpUtil.executeUrl;
import java.util.Properties;

/**
 * This Class handles the Chamberlain myQ http connection.
 * 
 * <ul>
 * <li>userName: myQ Login Username</li>
 * <li>password: myQ Login Password</li>
 * <li>logDeviceData: Log Device Data</li>
 * <li>sercurityTokin: sercurityTokin for API requests</li>
 * <li>webSite: url of myQ API</li>
 * <li>appId: appId for API requests</li>
 * <li>MaxRetrys: max login attempts in a row</li>
 * </ul>
 * 
 * @author Scott Hanson
 * @since 1.8.0
 */
public class myqData {
	static final Logger logger = LoggerFactory.getLogger(myqData.class);

	private String userName;
	private String password;
	private String sercurityTokin;
	private boolean logDeviceData;

	private final String webSite = "https://myqexternal.myqdevice.com";
	private final String appId = "JVM/G9Nwih5BwKgNCjLxiFUQxQijAebyyg8QUHr7JOrP+tuPb8iHfRHKwTmDzHOu";

	private final int MaxRetrys = 3;

	/**
	 * Constructor For Chamberlain MyQ http connection
	 * 
	 * @param username
	 *            Chamberlain MyQ UserName
	 * 
	 * @param password
	 *            Chamberlain MyQ password
	 * 
	 * @param logdevicedata
	 *            Log Device Data to openHAB Log
	 * 
	 */
	public myqData(String username, String password, boolean logdevicedata) {
		this.userName = username;
		this.password = password;
		this.logDeviceData = logdevicedata;
	}

	/**
	 * Gets Garage Door Opener Data in GarageDoorData object format
	 */
	public GarageDoorData getMyqData() {
		if (this.sercurityTokin == null)
			Login();
		String json = getGarageStatus(0);
		return json != null ? new GarageDoorData(json, this.logDeviceData)
				: null;
	}

	/**
	 * Retrieves JSON string of device data from myq website returns null if
	 * connection fails or user login fails
	 * 
	 * @param attemps
	 *            Attempt number when it recursively calls itself
	 */
	private String getGarageStatus(int attemps) {
		if (this.sercurityTokin == null && Login()) {
			return null;
		}
		String url = String.format(
				"%s/api/v4/userdevicedetails/get?appId=%s&SecurityToken=%s",
				this.webSite, this.appId, this.sercurityTokin);
		try {
			Properties header = new Properties();
			header.put("Accept", "application/json");
			header.put("User-Agent", "myq-openhab-api/1.0");
			String dataString = executeUrl("GET", url, header, null, null,
					5000);

			if (dataString == null) {
				logger.error("Failed to connect to MyQ site");
				if (attemps < MaxRetrys) {
					Login();
					return getGarageStatus(++attemps);
				}
				return null;
			}
			logger.debug("Received MyQ Device Data: {}", dataString);
			return dataString;
		} catch (Exception e) {
			logger.error("Failed to connect to MyQ site");
			return null;
		}
	}

	/**
	 * Validates Username and Password then saved sercurityTokin to a variable
	 * Returns false if return code from API is not correct or connection fails
	 */
	private boolean Login() {
		String url = String
				.format("%s/api/user/validate?appId=%s&SecurityToken=null&username=%s&password=%s",
						this.webSite, this.appId, this.userName, this.password);
		try {
			Properties header = new Properties();
			header.put("Accept", "application/json");
			header.put("User-Agent", "myq-openhab-api/1.0");
			String loginString = executeUrl("GET", url, header, null, null,
					5000);

			if (loginString == null) {
				logger.error("Failed to connect to MyQ site");
				return false;
			}
			logger.debug("Received MyQ Login JSON: {}", loginString);
			LoginData login = new LoginData(loginString);
			if (login.getSuccess()) {
				this.sercurityTokin = login.getSecurityToken();
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error("Failed to connect to MyQ site");
			return false;
		}
	}

	/**
	 * Send Command to open/close garage door opener with MyQ API Returns false
	 * if return code from API is not correct or connection fails
	 * 
	 * @param deviceID
	 *            MyQ deviceID of Garage Door Opener.
	 * @param state
	 *            Desired state to put the door in, 1 = open, 0 = closed
	 * @param attemps
	 *            Attempt number when it recursively calls itself
	 */
	public boolean executeCommand(int deviceID, int state, int attemps) {
		if (this.sercurityTokin == null && Login()) {
			return false;
		}
		String message = String.format("{\"ApplicationId\":\"%s\","
									+ "\"SecurityToken\":\"%s\","
									+ "\"MyQDeviceId\":\"%d\","
									+ "\"AttributeName\":\"desireddoorstate\","
									+ "\"AttributeValue\":\"%d\"}",
									this.appId, this.sercurityTokin, deviceID, state);
		String url = String
				.format("%s/api/v4/deviceattribute/putdeviceattribute?appId=%s&SecurityToken=%s",
						this.webSite, this.appId, this.sercurityTokin);
		try {
			Properties header = new Properties();
			header.put("Accept", "application/json");
			header.put("User-Agent", "myq-openhab-api/1.0");

			String dataString = executeUrl("PUT", url, header,
					IOUtils.toInputStream(message), "application/json", 5000);

			logger.debug("Sent message: '" + message + "' to " + url);
			logger.debug("Received MyQ Execute JSON: {}", dataString);
			if (dataString == null) {
				logger.error("Failed to connect to MyQ site");

				if (attemps < MaxRetrys) {
					Login();
					return executeCommand(deviceID, state, ++attemps);
				}
				return false;
			}
		} catch (Exception e) {
			logger.error("Failed to connect to MyQ site");
			return false;
		}
		return true;
	}
}